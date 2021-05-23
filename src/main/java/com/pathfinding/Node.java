package com.pathfinding;

public class Node {
    private NodeType nodeType;
    private int x;
    private int y;

    // Node position of previous expanded node
    private int lastX;
    private int lastY;

    private int distanceFromStart;

    // Determines the order of expanded nodes
    private int timeStamp;

    public Node(NodeType nodeType, int x, int y) {
        this.nodeType = nodeType;
        this.x = x;
        this.y = y;
        distanceFromStart = -1;
        timeStamp = 0;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    // G-Score g(n): Distance so far 
    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    // H-Score h(n): Heuristic Function 
    public double getDistanceToEnd(int endX, int endY) {
        int deltaX = Math.abs(x - endX);
        int deltaY = Math.abs(y - endY);
        // Manhatten Distance
        return deltaX + deltaY;
        // Euclidean Distance
        // return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setLast(int lastX, int lastY) {
        this.lastX = lastX;
        this.lastY = lastY;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }
}
