package com.example.solver;

public class RealSolveResponse {

    private boolean success;
    private String message;

    // Solution values for x and y in REAL form (after scaling back)
    private Double x;
    private Double y;

    public RealSolveResponse() {}

    public RealSolveResponse(boolean success, String message) {
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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
