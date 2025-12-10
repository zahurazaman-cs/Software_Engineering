package com.example.solver;

public class BoolSolveRequest {

    // "AND", "OR", "NOT", "IMPLIES", "EQUIV",
    // "COUNT_EQ", "COUNT_LE", "COUNT_GE"
    private String constraintKind;

    // number of boolean variables to use (b0, b1, ..., b(n-1))
    private Integer numBools;

    // constant k for counting constraints (#true =, <=, >= k)
    private Integer k;

    public BoolSolveRequest() {}

    public String getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(String constraintKind) {
        this.constraintKind = constraintKind;
    }

    public Integer getNumBools() {
        return numBools;
    }

    public void setNumBools(Integer numBools) {
        this.numBools = numBools;
    }

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }
}
