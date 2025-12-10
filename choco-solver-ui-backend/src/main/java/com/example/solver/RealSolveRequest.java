package com.example.solver;

public class RealSolveRequest {

    // "X_LE_CONST", "X_GE_CONST", "X_EQ_CONST",
    // "X_LE_Y", "X_GE_Y", "X_EQ_Y",
    // "LIN_EQ", "LIN_LE", "LIN_GE",
    // "ABS_DIFF_LE"
    private String constraintKind;

    // Domains for x and y (reals, we will scale them)
    private Double xMin;
    private Double xMax;
    private Double yMin;
    private Double yMax;

    // Constant for comparisons and linear constraints
    private Double constant;

    // Integer coefficients a1, a2 in a1*x + a2*y
    private Integer coef1;
    private Integer coef2;

    // Bound d for |x - y| <= d
    private Double distanceBound;

    public RealSolveRequest() {}

    public String getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(String constraintKind) {
        this.constraintKind = constraintKind;
    }

    public Double getxMin() {
        return xMin;
    }

    public void setxMin(Double xMin) {
        this.xMin = xMin;
    }

    public Double getxMax() {
        return xMax;
    }

    public void setxMax(Double xMax) {
        this.xMax = xMax;
    }

    public Double getyMin() {
        return yMin;
    }

    public void setyMin(Double yMin) {
        this.yMin = yMin;
    }

    public Double getyMax() {
        return yMax;
    }

    public void setyMax(Double yMax) {
        this.yMax = yMax;
    }

    public Double getConstant() {
        return constant;
    }

    public void setConstant(Double constant) {
        this.constant = constant;
    }

    public Integer getCoef1() {
        return coef1;
    }

    public void setCoef1(Integer coef1) {
        this.coef1 = coef1;
    }

    public Integer getCoef2() {
        return coef2;
    }

    public void setCoef2(Integer coef2) {
        this.coef2 = coef2;
    }

    public Double getDistanceBound() {
        return distanceBound;
    }

    public void setDistanceBound(Double distanceBound) {
        this.distanceBound = distanceBound;
    }
}
