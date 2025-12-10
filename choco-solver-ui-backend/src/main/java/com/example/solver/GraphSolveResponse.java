package com.example.solver;

public class GraphSolveResponse {

    private boolean success;
    private String message;

    // adjacency matrix: adj[i][j] = true if edge (i,j) present
    private boolean[][] adjacency;

    // degree of each node
    private int[] degrees;

    public GraphSolveResponse() {}

    public GraphSolveResponse(boolean success, String message) {
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

    public boolean[][] getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(boolean[][] adjacency) {
        this.adjacency = adjacency;
    }

    public int[] getDegrees() {
        return degrees;
    }

    public void setDegrees(int[] degrees) {
        this.degrees = degrees;
    }
}
