package com.example.solver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.stereotype.Service;

@Service
public class RealSolverService {

    // We approximate real numbers by integers using this scale.
    // Example: 1.23  ->  123
    private static final int SCALE = 100;  // 2 decimal digits

    public RealSolveResponse solve(RealSolveRequest req) {

        if (req.getConstraintKind() == null) {
            return new RealSolveResponse(false, "constraintKind is required");
        }

        String kind = req.getConstraintKind().toUpperCase();

        // Default domains if not provided
        double xMin = defaultDouble(req.getxMin(), 0.0);
        double xMax = defaultDouble(req.getxMax(), 10.0);
        double yMin = defaultDouble(req.getyMin(), 0.0);
        double yMax = defaultDouble(req.getyMax(), 10.0);

        int xMinInt = scale(xMin);
        int xMaxInt = scale(xMax);
        int yMinInt = scale(yMin);
        int yMaxInt = scale(yMax);

        Model model = new Model("RealVar approximated with IntVar");
        IntVar x = model.intVar("x", xMinInt, xMaxInt);
        IntVar y = model.intVar("y", yMinInt, yMaxInt);

        switch (kind) {
            case "X_LE_CONST":
            case "X_GE_CONST":
            case "X_EQ_CONST": {
                if (req.getConstant() == null) {
                    return new RealSolveResponse(false, "constant is required");
                }
                int c = scale(req.getConstant());
                String op = kindToOpConst(kind);
                model.arithm(x, op, c).post();
                break;
            }

            case "X_LE_Y":
            case "X_GE_Y":
            case "X_EQ_Y": {
                String op = kindToOpXY(kind);
                model.arithm(x, op, y).post();
                break;
            }

            case "LIN_EQ":
            case "LIN_LE":
            case "LIN_GE": {
                if (req.getCoef1() == null || req.getCoef2() == null || req.getConstant() == null) {
                    return new RealSolveResponse(false, "coef1, coef2 and constant are required for linear constraints");
                }
                int a1 = req.getCoef1();
                int a2 = req.getCoef2();
                int c = scale(req.getConstant());
                String op = kindToOpLinear(kind);

                // a1*x + a2*y ? c
                model.scalar(new IntVar[]{x, y}, new int[]{a1, a2}, op, c).post();
                break;
            }

            case "ABS_DIFF_LE": {
                if (req.getDistanceBound() == null) {
                    return new RealSolveResponse(false, "distanceBound is required for ABS_DIFF_LE");
                }
                int d = scale(req.getDistanceBound());
                // |x - y| <= d  is equivalent to
                // x - y <= d  AND  y - x <= d
                model.arithm(x, "-", y, "<=", d).post();
                model.arithm(y, "-", x, "<=", d).post();
                break;
            }

            default:
                return new RealSolveResponse(false, "Unknown constraintKind: " + kind);
        }

        Solver solver = model.getSolver();
        if (!solver.solve()) {
            return new RealSolveResponse(false, "No solution found for this RealVar constraint.");
        }

        double xVal = unscale(x.getValue());
        double yVal = unscale(y.getValue());

        RealSolveResponse resp = new RealSolveResponse(true, "RealVar solution found.");
        resp.setX(xVal);
        resp.setY(yVal);
        return resp;
    }

    private double defaultDouble(Double value, double defaultVal) {
        return value != null ? value : defaultVal;
    }

    private int scale(double value) {
        return (int) Math.round(value * SCALE);
    }

    private double unscale(int value) {
        return value / (double) SCALE;
    }

    private String kindToOpConst(String kind) {
        switch (kind) {
            case "X_LE_CONST": return "<=";
            case "X_GE_CONST": return ">=";
            case "X_EQ_CONST": return "=";
            default: throw new IllegalArgumentException("Unexpected kind for const: " + kind);
        }
    }

    private String kindToOpXY(String kind) {
        switch (kind) {
            case "X_LE_Y": return "<=";
            case "X_GE_Y": return ">=";
            case "X_EQ_Y": return "=";
            default: throw new IllegalArgumentException("Unexpected kind for XY: " + kind);
        }
    }

    private String kindToOpLinear(String kind) {
        switch (kind) {
            case "LIN_EQ": return "=";
            case "LIN_LE": return "<=";
            case "LIN_GE": return ">=";
            default: throw new IllegalArgumentException("Unexpected kind for linear: " + kind);
        }
    }
}
