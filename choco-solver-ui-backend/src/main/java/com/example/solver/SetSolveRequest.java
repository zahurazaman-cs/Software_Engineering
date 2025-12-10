package com.example.solver;

public class SetSolveRequest {

    // "CARD_EQ", "CARD_LE", "CARD_GE",
    // "MEMBER", "NOT_MEMBER",
    // "EQUAL", "SUBSET_EQ", "DISJOINT"
    private String constraintKind;

    // Universe is {0, 1, ..., universeSize-1}
    private Integer universeSize;

    // k for cardinality constraints
    private Integer k;

    // x for membership constraints (x in / not in S)
    private Integer element;

    public SetSolveRequest() {}

    public String getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(String constraintKind) {
        this.constraintKind = constraintKind;
    }

    public Integer getUniverseSize() {
        return universeSize;
    }

    public void setUniverseSize(Integer universeSize) {
        this.universeSize = universeSize;
    }

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public Integer getElement() {
        return element;
    }

    public void setElement(Integer element) {
        this.element = element;
    }
}
