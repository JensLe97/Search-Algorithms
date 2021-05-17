package com.pathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Grid extends JPanel implements MouseListener, MouseMotionListener {

    private Node[][] gridNodes;
    private int startPosX, startPosY, endPosX, endPosY;

    public Grid() {
        gridNodes = new Node[GuiConsts.CELLS_X][GuiConsts.CELLS_Y];
        emptyGridNodes();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void emptyGridNodes(){
        for (int x = 0; x < GuiConsts.CELLS_X; x++) {
            for (int y = 0; y < GuiConsts.CELLS_Y; y++) {
                gridNodes[x][y] = new Node(NodeType.EMPTY, x, y);
            }
        }
        startPosX = startPosY = endPosX = endPosY = -1;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int x = 0; x < GuiConsts.CELLS_X; x++) {
            for(int y = 0; y < GuiConsts.CELLS_Y; y++) {
                switch(gridNodes[x][y].getNodeType()) {
                    case EMPTY:
                        g.setColor(Color.WHITE);
                        break;
                    case OBSTACLE:
                        g.setColor(Color.BLACK);
                        break;
                    case START:
                        g.setColor(Color.GREEN);
                        break;
                    case END:
                        g.setColor(Color.RED);
                        break;
                    case EXPANDED:
                        g.setColor(Color.CYAN);
                        break;
                    case FRINGE:
                        g.setColor(Color.YELLOW);
                        break;
                    case PATH:
                        g.setColor(Color.MAGENTA);
                        break;
                    default:
                        throw new IllegalArgumentException("NodeType does not exist!");
                }
                g.fillRect(x * GuiConsts.CELL_SIZE, y * GuiConsts.CELL_SIZE, GuiConsts.CELL_SIZE, GuiConsts.CELL_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect(x * GuiConsts.CELL_SIZE, y * GuiConsts.CELL_SIZE, GuiConsts.CELL_SIZE, GuiConsts.CELL_SIZE);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() <= GuiConsts.CANVAS_WIDTH && e.getY() <= GuiConsts.CANVAS_HEIGHT) {
            int posX = e.getX() / GuiConsts.CELL_SIZE;
            int posY = e.getY() / GuiConsts.CELL_SIZE;
            
            Node node = gridNodes[posX][posY];
            // Left Mouse Button: Obstacles
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (node.getNodeType() == NodeType.OBSTACLE) {
                    node.setNodeType(NodeType.EMPTY);
                } else if (node.getNodeType() == NodeType.EMPTY) {
                    node.setNodeType(NodeType.OBSTACLE);
                }
            // Middle Mouse Button: Start Node
            } else if (SwingUtilities.isMiddleMouseButton(e)) {
                // Reset the previous start node first
                if (startPosX != -1 && startPosY != -1) {
                    gridNodes[startPosX][startPosY].setNodeType(NodeType.EMPTY);
                    gridNodes[startPosX][startPosY].setDistanceFromStart(-1);
                }
                node.setDistanceFromStart(0);
                startPosX = posX;
                startPosY = posY;
                node.setNodeType(NodeType.START);
            // Right Mouse Button: End Node
            } else if (SwingUtilities.isRightMouseButton(e)) {
                // Reset the previous start node first
                if (endPosX != -1 && endPosY != -1) {
                    gridNodes[endPosX][endPosY].setNodeType(NodeType.EMPTY);
                }
                node.setDistanceFromStart(0);
                endPosX = posX;
                endPosY = posY;
                node.setNodeType(NodeType.END);
            }      

            this.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getX() >= 0 && e.getY() >= 0 && e.getX() <= GuiConsts.CANVAS_WIDTH && e.getY() <= GuiConsts.CANVAS_HEIGHT) {
            int posX = e.getX() / GuiConsts.CELL_SIZE;
            int posY = e.getY() / GuiConsts.CELL_SIZE;
            
            Node node = gridNodes[posX][posY];
            
            // Left Mouse Button: Draw Obstacles
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (node.getNodeType() == NodeType.EMPTY) {
                    node.setNodeType(NodeType.OBSTACLE);
                } 
            } 

            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    public Node[][] getNodes() {
        return gridNodes;
    }

    public int getStartPosX(){
        return startPosX;
    }

    public int getStartPosY(){
        return startPosY;
    }

    public int getEndPosX(){
        return endPosX;
    }

    public int getEndPosY(){
        return endPosY;
    }
}
