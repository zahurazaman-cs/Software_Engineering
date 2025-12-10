package com.example.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.set.PropCardinality;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.springframework.stereotype.Service;

@Service
public class SetSolverService {

    public SetSolveResponse solve(SetSolveRequest req) {

        if (req.getConstraintKind() == null) {
            return new SetSolveResponse(false, "constraintKind is required");
        }

        // Universe size (how many possible elements)
        int universeSize = (req.getUniverseSize() != null) ? req.getUniverseSize() : 5;
        if (universeSize <= 0) {
            return new SetSolveResponse(false, "universeSize must be >= 1");
        }

        // Universe = {0, 1, ..., universeSize-1}
        int[] universe = buildUniverse(universeSize);

        Model model = new Model("SetVar constraints");
        String kind = req.getConstraintKind().toUpperCase();

        switch (kind) {

            // ---------------- CARDINALITY |S| =,<=,>= k ----------------
            case "CARD_EQ":
            case "CARD_LE":
            case "CARD_GE": {
                if (req.getK() == null) {
                    return new SetSolveResponse(false, "k is required for cardinality constraints");
                }
                int k = req.getK();

                // S is a subset of the universe
                SetVar s = model.setVar("S", new int[]{}, universe);

                // card(S) is an IntVar, then we link it to S with PropCardinality
                IntVar card = model.intVar("cardS", 0, universeSize);
                Constraint cardConstraint =
                        new Constraint("cardinality", new PropCardinality(s, card));
                cardConstraint.post();

                if (kind.equals("CARD_EQ")) {
                    model.arithm(card, "=", k).post();
                } else if (kind.equals("CARD_LE")) {
                    model.arithm(card, "<=", k).post();
                } else { // CARD_GE
                    model.arithm(card, ">=", k).post();
                }

                return solveSingleSet(model, s, "Cardinality constraint on S satisfied.");
            }

            // ---------------- MEMBERSHIP x ∈ S or x ∉ S ----------------
            case "MEMBER":
            case "NOT_MEMBER": {
                if (req.getElement() == null) {
                    return new SetSolveResponse(false, "element is required for membership constraints");
                }
                int x = req.getElement();
                if (x < 0 || x >= universeSize) {
                    return new SetSolveResponse(false,
                            "element must be between 0 and universeSize-1");
                }

                SetVar s = model.setVar("S", new int[]{}, universe);
                IntVar elem = model.intVar("elem", x, x);

                if (kind.equals("MEMBER")) {
                    model.member(elem, s).post();
                } else { // NOT_MEMBER
                    model.notMember(elem, s).post();
                }

                return solveSingleSet(model, s, "Membership constraint on S satisfied.");
            }

            // ---------------- RELATIONS between S1 and S2 ---------------
            case "EQUAL":
            case "SUBSET_EQ":
            case "DISJOINT": {

                SetVar s1 = model.setVar("S1", new int[]{}, universe);
                SetVar s2 = model.setVar("S2", new int[]{}, universe);

                if (kind.equals("EQUAL")) {
                    // S1 = S2  ⇔ S1 ⊆ S2 and S2 ⊆ S1
                    model.subsetEq(new SetVar[]{s1, s2}).post();
                    model.subsetEq(new SetVar[]{s2, s1}).post();
                } else if (kind.equals("SUBSET_EQ")) {
                    // S1 ⊆ S2
                    model.subsetEq(new SetVar[]{s1, s2}).post();
                } else { // DISJOINT
                    // S1 and S2 are disjoint
                    model.disjoint(s1, s2).post();
                }

                return solveTwoSets(model, s1, s2, "Set relation constraint satisfied.");
            }

            default:
                return new SetSolveResponse(false, "Unknown constraintKind: " + kind);
        }
    }

    // ---------- helpers ----------

    private int[] buildUniverse(int n) {
        int[] u = new int[n];
        for (int i = 0; i < n; i++) {
            u[i] = i;
        }
        return u;
    }

    private SetSolveResponse solveSingleSet(Model model, SetVar s, String msg) {
        Solver solver = model.getSolver();
        if (!solver.solve()) {
            return new SetSolveResponse(false, "No solution found for this SetVar constraint.");
        }

        int[] values = s.getValue().toArray();
        SetSolveResponse resp = new SetSolveResponse(true, msg);
        resp.setS1Elements(values);
        return resp;
    }

    private SetSolveResponse solveTwoSets(Model model, SetVar s1, SetVar s2, String msg) {
        Solver solver = model.getSolver();
        if (!solver.solve()) {
            return new SetSolveResponse(false, "No solution found for this SetVar relation.");
        }

        int[] s1Vals = s1.getValue().toArray();
        int[] s2Vals = s2.getValue().toArray();

        SetSolveResponse resp = new SetSolveResponse(true, msg);
        resp.setS1Elements(s1Vals);
        resp.setS2Elements(s2Vals);
        return resp;
    }
}
