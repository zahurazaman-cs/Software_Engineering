package com.example.solver;

public class SolveRequest {

    private String constraintType;       // "BASIC", "SUM", "ARITH"
    private String comparisonOperator;   // "EQ", "NE", "GT", "LT", "GE", "LE"
    private String expressionOperator;   // "ADD", "SUB", "MUL", "DIV"

    private Integer xMin;
    private Integer xMax;
    private Integer yMin;
    private Integer yMax;

    private Integer xValue;  // for ARITH
    private Integer yValue;  // for ARITH

    private Integer z;       // constant for SUM

    public SolveRequest() {}

    public String getConstraintType() { return constraintType; }
    public void setConstraintType(String constraintType) { this.constraintType = constraintType; }

    public String getComparisonOperator() { return comparisonOperator; }
    public void setComparisonOperator(String comparisonOperator) { this.comparisonOperator = comparisonOperator; }

    public String getExpressionOperator() { return expressionOperator; }
    public void setExpressionOperator(String expressionOperator) { this.expressionOperator = expressionOperator; }

    public Integer getxMin() { return xMin; }
    public void setxMin(Integer xMin) { this.xMin = xMin; }

    public Integer getxMax() { return xMax; }
    public void setxMax(Integer xMax) { this.xMax = xMax; }

    public Integer getyMin() { return yMin; }
    public void setyMin(Integer yMin) { this.yMin = yMin; }

    public Integer getyMax() { return yMax; }
    public void setyMax(Integer yMax) { this.yMax = yMax; }

    public Integer getxValue() { return xValue; }
    public void setxValue(Integer xValue) { this.xValue = xValue; }

    public Integer getyValue() { return yValue; }
    public void setyValue(Integer yValue) { this.yValue = yValue; }

    public Integer getZ() { return z; }
    public void setZ(Integer z) { this.z = z; }
}
