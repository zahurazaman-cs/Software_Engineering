package com.example.solver;

public class SetSolveResponse {

    private boolean success;
    private String message;

    // Elements of S (for single-set constraints)
    private int[] s1Elements;

    // Elements of S1 and S2 (for relations)
    private int[] s2Elements;

    public SetSolveResponse() {}

    public SetSolveResponse(boolean success, String message) {
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

    public int[] getS1Elements() {
        return s1Elements;
    }

    public void setS1Elements(int[] s1Elements) {
        this.s1Elements = s1Elements;
    }

    public int[] getS2Elements() {
        return s2Elements;
    }

    public void setS2Elements(int[] s2Elements) {
        this.s2Elements = s2Elements;
    }
}
