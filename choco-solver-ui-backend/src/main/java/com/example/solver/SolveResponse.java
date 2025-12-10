package com.example.solver;

public class SolveResponse {

    private boolean success;
    private String message;

    private Integer x;
    private Integer y;
    private Integer z;

    private Integer expressionResult; // for X+Y, X-Y, etc.
    private Integer sumResult;        // for x + y in SUM constraint

    public SolveResponse() {}

    public SolveResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }

    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }

    public Integer getZ() { return z; }
    public void setZ(Integer z) { this.z = z; }

    public Integer getExpressionResult() { return expressionResult; }
    public void setExpressionResult(Integer expressionResult) { this.expressionResult = expressionResult; }

    public Integer getSumResult() { return sumResult; }
    public void setSumResult(Integer sumResult) { this.sumResult = sumResult; }
}
