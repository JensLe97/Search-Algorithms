package com.pathfinding;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Class that implements certain pathfinding algorithms:
 * 
 * - A* Search
 * - Dijkstra Algorithm
 * - Breadth First Search
 * - Depth First Search
 * - Iterative Deepening
 * 
 * Visualization in a Java GUI Grid with Start Node, Goal and Obstacles.
 */
public class Pathfinding {
    // GUI items
    private JFrame frame;
    private JButton startSearch;
    private JButton clearCanvas;
    private JButton resetCanvas;
    private JComboBox<Algorithm> algorithms;
    private JLabel pathLength;
    private JCheckBox diagonals;
    private JPanel controlPanel;
    private Grid gridCanvas;

    public Pathfinding() {
        SearchAlgorithm algorithm = new SearchAlgorithm();

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                // GUI init
                frame = new JFrame();

                // gridCanvas (Panel) with nodes
                gridCanvas = new Grid();
                gridCanvas.setBorder(BorderFactory.createEmptyBorder(GuiConsts.CANVAS_HEIGHT, GuiConsts.CANVAS_WIDTH, 0, 0));
                gridCanvas.setLayout(new FlowLayout());

                // Combo Box for algorithms
                algorithms = new JComboBox<Algorithm>(Algorithm.values());
                // Labels showing the current Algorithm and the path length
                pathLength = new JLabel("PathLength: ");

                // Checkbox for selecting diagonal search
                diagonals = new JCheckBox("Diagonal", true);

                // Search Button
                startSearch = new JButton("Start Seach");
                startSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        algorithm.StartSearch((Algorithm) algorithms.getSelectedItem(), gridCanvas, pathLength, diagonals.isSelected());
                    }
                });

                // Clear Button
                clearCanvas = new JButton("Clear");
                clearCanvas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gridCanvas.emptyGridNodes();    
                    }
                });

                // Reset Button
                resetCanvas = new JButton("Reset");
                resetCanvas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gridCanvas.resetGridNodes();    
                    }
                });

                // Space between Control Elements
                int verticalSpace = 20;
                // controlPanel with buttons
                controlPanel = new JPanel();
                controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                controlPanel.setLayout(new GridLayout(0, 1));
                controlPanel.add(algorithms);
                controlPanel.add(Box.createVerticalStrut(verticalSpace));
                controlPanel.add(startSearch);
                controlPanel.add(Box.createVerticalStrut(verticalSpace));
                controlPanel.add(clearCanvas);
                controlPanel.add(Box.createVerticalStrut(verticalSpace));
                controlPanel.add(resetCanvas);
                controlPanel.add(Box.createVerticalStrut(verticalSpace));
                controlPanel.add(pathLength);
                controlPanel.add(Box.createVerticalStrut(verticalSpace));
                controlPanel.add(diagonals);
                
                frame.setLayout(new FlowLayout());
                frame.add(controlPanel, BorderLayout.WEST);
                frame.add(gridCanvas, BorderLayout.EAST);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Path Finding");
                frame.pack();
                frame.setVisible(true);
                frame.setResizable(false);
                frame.setSize(GuiConsts.WIDTH, GuiConsts.HEIGHT);
            }
        });
    }

    public static void main( String[] args ) {
        new Pathfinding();
    }
}
