package com.example.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GraphSolverService {

    public GraphSolveResponse solve(GraphSolveRequest req) {

        if (req.getConstraintKind() == null) {
            return new GraphSolveResponse(false, "constraintKind is required");
        }

        int n = (req.getNumNodes() != null) ? req.getNumNodes() : 4;
        if (n < 2) {
            return new GraphSolveResponse(false, "numNodes must be >= 2");
        }

        if (req.getNodeIndex() == null) {
            return new GraphSolveResponse(false, "nodeIndex is required");
        }
        int node = req.getNodeIndex();
        if (node < 0 || node >= n) {
            return new GraphSolveResponse(false, "nodeIndex must be between 0 and numNodes-1");
        }

        if (req.getDegreeK() == null) {
            return new GraphSolveResponse(false, "degreeK is required");
        }
        int k = req.getDegreeK();
        if (k < 0 || k >= n) {
            return new GraphSolveResponse(false, "degreeK must be between 0 and numNodes-1");
        }

        String kind = req.getConstraintKind().toUpperCase();

        Model model = new Model("Graph degree constraints");

        // Undirected graph modeled by BoolVar adjacency matrix
        BoolVar[][] edge = new BoolVar[n][n];

        // Create edge variables only for i < j
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                edge[i][j] = model.boolVar("e_" + i + "_" + j);
            }
        }

        // Degree variables
        IntVar[] degree = new IntVar[n];

        for (int i = 0; i < n; i++) {
            List<BoolVar> incident = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                if (i < j) {
                    incident.add(edge[i][j]);
                } else { // i > j
                    incident.add(edge[j][i]);
                }
            }
            BoolVar[] incArr = incident.toArray(new BoolVar[0]);
            degree[i] = model.intVar("deg_" + i, 0, n - 1);
            model.sum(incArr, "=", degree[i]).post();
        }

        // Apply degree constraint on the selected node
        switch (kind) {
            case "DEGREE_EQ":
                model.arithm(degree[node], "=", k).post();
                break;
            case "DEGREE_LE":
                model.arithm(degree[node], "<=", k).post();
                break;
            case "DEGREE_GE":
                model.arithm(degree[node], ">=", k).post();
                break;
            default:
                return new GraphSolveResponse(false, "Unknown constraintKind: " + kind);
        }

        Solver solver = model.getSolver();
        if (!solver.solve()) {
            return new GraphSolveResponse(false, "No graph satisfies this degree constraint.");
        }

        // Build adjacency matrix and degree array for the solution
        boolean[][] adjacency = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                boolean val = edge[i][j].getValue() == 1;
                adjacency[i][j] = val;
                adjacency[j][i] = val;
            }
        }

        int[] degreeVals = new int[n];
        for (int i = 0; i < n; i++) {
            degreeVals[i] = degree[i].getValue();
        }

        GraphSolveResponse resp = new GraphSolveResponse(true, "Graph degree constraint satisfied.");
        resp.setAdjacency(adjacency);
        resp.setDegrees(degreeVals);
        return resp;
    }
}
