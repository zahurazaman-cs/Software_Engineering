package com.example.solver;

public class GraphSolveRequest {

    // "DEGREE_EQ", "DEGREE_LE", "DEGREE_GE"
    private String constraintKind;

    // number of nodes in the graph
    private Integer numNodes;

    // which node's degree we constrain (0..numNodes-1)
    private Integer nodeIndex;

    // degree value k
    private Integer degreeK;

    public GraphSolveRequest() {}

    public String getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(String constraintKind) {
        this.constraintKind = constraintKind;
    }

    public Integer getNumNodes() {
        return numNodes;
    }

    public void setNumNodes(Integer numNodes) {
        this.numNodes = numNodes;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public Integer getDegreeK() {
        return degreeK;
    }

    public void setDegreeK(Integer degreeK) {
        this.degreeK = degreeK;
    }
}
