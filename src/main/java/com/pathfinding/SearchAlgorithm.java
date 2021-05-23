package com.pathfinding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.Timer;

public class SearchAlgorithm {
    Node[][] gridNodes;
    private int pathLength;
    private boolean pathFound;
    private boolean diagonals;
    int timeStamp = 0;

    public void StartSearch(Algorithm algorithm, Grid gridCanvas, JLabel pathLengthLabel, boolean diagonals) {
        timeStamp = 0;
        pathFound = false;
        pathLength = -1;
        this.diagonals = diagonals;

        gridNodes = gridCanvas.getNodes();
        int startPosX = gridCanvas.getStartPosX();
        int startPosY = gridCanvas.getStartPosY();
        int endPosX = gridCanvas.getEndPosX();
        int endPosY = gridCanvas.getEndPosY();

        if (startPosX == -1 || startPosY == -1 || endPosX == -1 || endPosY == -1) {
            pathLengthLabel.setText("No Start/End!"); 
            return;
        }

        // Open List = Fringe: New nodes that can be expanded
        Queue<Node> openList;

        switch (algorithm) {
            case AStar:
                // Comparator compares the F-Score: f(n) = g(n) + h(n)
                openList = new PriorityQueue<Node>((node1, node2) -> 
                    ((node2.getDistanceFromStart() + (int) Math.floor(node2.getDistanceToEnd(endPosX, endPosY))) ==
                     (node1.getDistanceFromStart() + (int) Math.floor(node1.getDistanceToEnd(endPosX, endPosY)))) ?
                      node1.getTimeStamp() - node2.getTimeStamp() :
                     (node1.getDistanceFromStart() + (int) Math.floor(node1.getDistanceToEnd(endPosX, endPosY))) -
                     (node2.getDistanceFromStart() + (int) Math.floor(node2.getDistanceToEnd(endPosX, endPosY)))
                );
                break;
            case Dijkstra:
                // Comparator compares the F-Score: f(n) = g(n)
                // Here, Dijkstra is the same as BFS since all distances are 1
                openList = new PriorityQueue<Node>((node1, node2) -> 
                    (node2.getDistanceFromStart() ==
                     node1.getDistanceFromStart()) ?
                     node1.getTimeStamp() - node2.getTimeStamp() :
                     node1.getDistanceFromStart() -
                     node2.getDistanceFromStart()
                );
                break;
            case Greedy:
                // Comparator compares the F-Score: f(n) = h(n)
                openList = new PriorityQueue<Node>((node1, node2) -> 
                    ((int) Math.floor(node2.getDistanceToEnd(endPosX, endPosY)) ==
                     (int) Math.floor(node1.getDistanceToEnd(endPosX, endPosY))) ?
                      node1.getTimeStamp() - node2.getTimeStamp() :
                     (int) Math.floor(node1.getDistanceToEnd(endPosX, endPosY)) -
                     (int) Math.floor(node2.getDistanceToEnd(endPosX, endPosY))
                );
                break;
            case BFS:
                // FIFO Queue: nodes that are inserted first are removed first
                // Insert at the END and remove from the beginning
                openList = new ArrayDeque<Node>();
                break;
            case DFS:
                // LIFO Queue: nodes that are inserted first are removed last
                // Insert at the BEGINNING and remove from the beginning
                // Use LinkedList for only adding/removing from one side
                openList = new LinkedList<Node>();
                break;
            default:
                openList = new PriorityQueue<Node>();
                throw new IllegalArgumentException("Algorithm does not exist!");
        }  
        
        openList.add(gridNodes[startPosX][startPosY]);
        
        int timerDelay;
        if (diagonals) {
            timerDelay = 30;
        } else {
            timerDelay = 10;
        }
        final Timer timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (openList.isEmpty() || pathFound) {
                    ((Timer) ae.getSource()).stop();

                    if (getPathLength() != -1) {
                        pathLengthLabel.setText("PathLength: " + getPathLength());
                    } else {
                        pathLengthLabel.setText("No Path Found!");
                    }
                    return;
                }

                // Mark all nodes in the Open List als Expanded
                for (Node node : openList) {
                    if (node.getNodeType() != NodeType.START && node.getNodeType() != NodeType.END) {
                        node.setNodeType(NodeType.EXPANDED);
                    }
                }

                // Increase the G-Score by 1
                int distanceFromStart = openList.peek().getDistanceFromStart() + 1;
                // Closed List: Already expanded nodes
                ArrayList<Node> closedList = expandNeighboringNodes(openList.peek(), distanceFromStart, algorithm);
                openList.remove();
                if (!closedList.isEmpty()) {
                    for (Node node : closedList) {
                            // Change TimeStamp for identifying each node
                            timeStamp++;
                            node.setTimeStamp(timeStamp);
                            if (algorithm == Algorithm.DFS) {
                                // Insert at the beginning
                                ((LinkedList<Node>) openList).addFirst(node);
                            } else {
                                // Insert at the end
                                openList.add(node);
                            }
                    }
                    gridCanvas.revalidate();
                    gridCanvas.repaint(); 
                }
            }
        });
        timer.start();
    }

    private ArrayList<Node> expandNeighboringNodes(Node currentNode, int distanceFromStart, Algorithm algorithm) {
        ArrayList<Node> expandedNeighboringNodes = new ArrayList<Node>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                // Do not consider diagonal neighbors
                if (!diagonals && (x == y || (x == 1 && y == -1) || (x == -1 && y == 1))) {
                    continue;
                }

                int xBound = currentNode.getX() + x;
                int yBound = currentNode.getY() + y;
                // Node is inside the grid
                if (xBound >= 0 && xBound < GuiConsts.CELLS_X && yBound >= 0 && yBound < GuiConsts.CELLS_Y) {
                    Node neighboringNode = gridNodes[xBound][yBound];
                    int neighDistToStart = neighboringNode.getDistanceFromStart();

                    // Check if the neighbor has not been expanded before and that it is not an obstacle
                    if (((algorithm == Algorithm.DFS && neighDistToStart == -1) 
                      || (algorithm != Algorithm.DFS && neighDistToStart > distanceFromStart) || neighDistToStart == -1)
                      && neighboringNode.getNodeType() != NodeType.OBSTACLE) {
                        expandNode(neighboringNode, currentNode.getX(), currentNode.getY(), distanceFromStart);
                        expandedNeighboringNodes.add(neighboringNode);
                    }
                }
            }
        }
        return expandedNeighboringNodes;
    }

    private void expandNode(Node currentNode, int lastX, int lastY, int distanceFromStart) {
        // Mark the node as a Fringe node if it is no start or end node
        if (currentNode.getNodeType() != NodeType.START && currentNode.getNodeType() != NodeType.END) {
            currentNode.setNodeType(NodeType.FRINGE);
        }
        currentNode.setLast(lastX, lastY);
        currentNode.setDistanceFromStart(distanceFromStart);
        
        // Check if the end node is reached: Then Backtrack
        if (currentNode.getNodeType() == NodeType.END) {
            backtrackPath(currentNode.getLastX(), currentNode.getLastY(), distanceFromStart);
            pathFound = true;
        }
    }

    private void backtrackPath(int lastX, int lastY, int distanceFromStart) {
        pathLength = distanceFromStart;

        // Mark every node in the shortest path
        while (distanceFromStart > 1) {
            Node currentNode = gridNodes[lastX][lastY];
            currentNode.setNodeType(NodeType.PATH);
            lastX = currentNode.getLastX();
            lastY = currentNode.getLastY();
            distanceFromStart--;
        }
    }

    public int getPathLength() {
        return pathLength;
    }

    public boolean isPathFound() {
        return pathFound;
    }
}
