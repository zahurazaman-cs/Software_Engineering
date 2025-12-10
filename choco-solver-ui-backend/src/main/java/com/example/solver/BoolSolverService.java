package com.example.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.springframework.stereotype.Service;

@Service
public class BoolSolverService {

    public BoolSolveResponse solve(BoolSolveRequest req) {

        if (req.getConstraintKind() == null) {
            return new BoolSolveResponse(false, "constraintKind is required");
        }

        int n = (req.getNumBools() != null) ? req.getNumBools() : 2;
        if (n <= 0) {
            return new BoolSolveResponse(false, "numBools must be >= 1");
        }

        Model model = new Model("BoolVar constraints");
        BoolVar[] bs = model.boolVarArray("b", n);

        String kind = req.getConstraintKind().toUpperCase();

        switch (kind) {
            case "AND":
                // all booleans must be true
                model.sum(bs, "=", n).post();
                break;

            case "OR":
                // at least one must be true
                model.sum(bs, ">=", 1).post();
                break;

            case "NOT":
                // NOT(b0) must be true -> b0 = 0
                model.arithm(bs[0], "=", 0).post();
                break;

            case "IMPLIES":
                if (n < 2) {
                    return new BoolSolveResponse(false, "IMPLIES needs at least 2 booleans");
                }
                // b0 -> b1  is equivalent to  b0 <= b1
                model.arithm(bs[0], "<=", bs[1]).post();
                break;

            case "EQUIV":
                if (n < 2) {
                    return new BoolSolveResponse(false, "EQUIV needs at least 2 booleans");
                }
                // equivalence: b0 = b1
                model.arithm(bs[0], "=", bs[1]).post();
                break;

            case "COUNT_EQ":
            case "COUNT_LE":
            case "COUNT_GE":
                if (req.getK() == null) {
                    return new BoolSolveResponse(false, "k is required for counting constraints");
                }
                int k = req.getK();
                if (kind.equals("COUNT_EQ")) {
                    model.sum(bs, "=", k).post();
                } else if (kind.equals("COUNT_LE")) {
                    model.sum(bs, "<=", k).post();
                } else { // COUNT_GE
                    model.sum(bs, ">=", k).post();
                }
                break;

            default:
                return new BoolSolveResponse(false, "Unknown constraintKind: " + kind);
        }

        Solver solver = model.getSolver();
        if (!solver.solve()) {
            return new BoolSolveResponse(false, "No solution found for this BoolVar constraint.");
        }

        boolean[] values = new boolean[n];
        int trueCount = 0;
        for (int i = 0; i < n; i++) {
            values[i] = bs[i].getValue() == 1;
            if (values[i]) trueCount++;
        }

        BoolSolveResponse resp = new BoolSolveResponse(true, "BoolVar solution found.");
        resp.setBoolValues(values);
        resp.setTrueCount(trueCount);
        return resp;
    }
}
