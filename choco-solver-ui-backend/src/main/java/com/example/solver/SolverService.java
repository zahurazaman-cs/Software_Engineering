package com.example.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.stereotype.Service;

@Service
public class SolverService {

    public SolveResponse solve(SolveRequest req) {

        String type = req.getConstraintType();
        if (type == null) {
            return new SolveResponse(false, "constraintType is required");
        }

        switch (type.toUpperCase()) {
            case "BASIC":
                return solveBasicComparison(req);
            case "SUM":
                return solveSumConstraint(req);
            case "ARITH":
                return solveArithmeticExpression(req);
            default:
                return new SolveResponse(false, "Unknown constraintType: " + type);
        }
    }

    // ---------- 1) BASIC: x ? y  ----------
    private SolveResponse solveBasicComparison(SolveRequest req) {
        Model model = new Model("Basic comparison");

        int xMin = defaultInt(req.getxMin(), 0);
        int xMax = defaultInt(req.getxMax(), 100);
        int yMin = defaultInt(req.getyMin(), 0);
        int yMax = defaultInt(req.getyMax(), 100);

        IntVar x = model.intVar("x", xMin, xMax);
        IntVar y = model.intVar("y", yMin, yMax);

        String op = symbolFromComparison(req.getComparisonOperator());
        if (op == null) {
            return new SolveResponse(false, "Invalid comparisonOperator for BASIC");
        }

        model.arithm(x, op, y).post();

        Solver solver = model.getSolver();
        if (solver.solve()) {
            SolveResponse resp = new SolveResponse(true, "Basic comparison solution found.");
            resp.setX(x.getValue());
            resp.setY(y.getValue());
            return resp;
        } else {
            return new SolveResponse(false, "No solution found for basic comparison.");
        }
    }

    // ---------- 2) SUM: x + y ? z ----------
    private SolveResponse solveSumConstraint(SolveRequest req) {
        if (req.getZ() == null) {
            return new SolveResponse(false, "z (constant) is required for SUM constraint");
        }

        Model model = new Model("Sum constraint");

        int xMin = defaultInt(req.getxMin(), 0);
        int xMax = defaultInt(req.getxMax(), 100);
        int yMin = defaultInt(req.getyMin(), 0);
        int yMax = defaultInt(req.getyMax(), 100);
        int z = req.getZ();

        IntVar x = model.intVar("x", xMin, xMax);
        IntVar y = model.intVar("y", yMin, yMax);

        // sum = x + y
        IntVar sum = model.intVar("sum", xMin + yMin, xMax + yMax);
        model.sum(new IntVar[]{x, y}, "=", sum).post();

        String op = symbolFromComparison(req.getComparisonOperator());
        if (op == null) {
            return new SolveResponse(false, "Invalid comparisonOperator for SUM");
        }

        // sum ? z   (e.g. sum = z, sum < z, etc.)
        model.arithm(sum, op, z).post();

        Solver solver = model.getSolver();
        if (solver.solve()) {
            int xVal = x.getValue();
            int yVal = y.getValue();

            SolveResponse resp = new SolveResponse(true, "Sum constraint solution found.");
            resp.setX(xVal);
            resp.setY(yVal);
            resp.setZ(z);
            resp.setSumResult(xVal + yVal);
            return resp;
        } else {
            return new SolveResponse(false, "No solution found for sum constraint.");
        }
    }

    // ---------- 3) ARITH: direct arithmetic X op Y ----------
    private SolveResponse solveArithmeticExpression(SolveRequest req) {
        if (req.getxValue() == null || req.getyValue() == null) {
            return new SolveResponse(false, "xValue and yValue are required for ARITH");
        }
        if (req.getExpressionOperator() == null) {
            return new SolveResponse(false, "expressionOperator is required for ARITH");
        }

        int x = req.getxValue();
        int y = req.getyValue();

        String op = req.getExpressionOperator().toUpperCase();
        Integer result;

        switch (op) {
            case "ADD":
                result = x + y;
                break;
            case "SUB":
                result = x - y;
                break;
            case "MUL":
                result = x * y;
                break;
            case "DIV":
                if (y == 0) {
                    return new SolveResponse(false, "Division by zero");
                }
                result = x / y; // integer division
                break;
            default:
                return new SolveResponse(false, "Unknown expressionOperator: " + op);
        }

        SolveResponse resp = new SolveResponse(true, "Arithmetic expression evaluated.");
        resp.setX(x);
        resp.setY(y);
        resp.setExpressionResult(result);
        return resp;
    }

    // ---------- helpers ----------

    private int defaultInt(Integer value, int defaultVal) {
        return value != null ? value : defaultVal;
    }

    private String symbolFromComparison(String code) {
        if (code == null) return null;
        switch (code.toUpperCase()) {
            case "EQ": return "=";
            case "NE": return "!=";
            case "GT": return ">";
            case "LT": return "<";
            case "GE": return ">=";
            case "LE": return "<=";
            default: return null;
        }
    }
}
