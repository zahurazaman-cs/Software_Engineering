package com.example.solver;

public class BoolSolveResponse {

    private boolean success;
    private String message;

    // values of b0, b1, ..., b(n-1)
    private boolean[] boolValues;

    // total number of true booleans in the solution
    private Integer trueCount;

    public BoolSolveResponse() {}

    public BoolSolveResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean[] getBoolValues() {
        return boolValues;
    }

    public void setBoolValues(boolean[] boolValues) {
        this.boolValues = boolValues;
    }

    public Integer getTrueCount() {
        return trueCount;
    }

    public void setTrueCount(Integer trueCount) {
        this.trueCount = trueCount;
    }
}
