package com.pathfinding;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    private JFrame frame;
    private JButton startSearch;
    private JLabel algorithms;
    private JPanel panel;

    public Pathfinding() {
        frame = new JFrame();

        startSearch = new JButton("Start Seach");
        startSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                algorithms.setText("A*");         
            }
        });

        algorithms = new JLabel("Algorithm");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(startSearch);
        panel.add(algorithms);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Path Finding");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main( String[] args ) {
        new Pathfinding();
    }
}
