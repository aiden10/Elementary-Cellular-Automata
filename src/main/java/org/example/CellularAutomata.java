package org.example;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

public class CellularAutomata extends JPanel{
    public static final int GRID_SIZE = 1024;
    public static int[][] board = new int [GRID_SIZE][GRID_SIZE];
    private static final int sceneLength = 1920;
    private static final int sceneHeight = 1080;

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        fillGrid(board, g);
    }
    private void fillGrid(int[][] board, Graphics g){
        Color color;
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int tileX = (int) ((sceneLength / (double) GRID_SIZE) * j);
                int tileY = (int) ((sceneHeight / (double) GRID_SIZE) * i);

                if (board[i][j] == 0){
                    color = Color.WHITE;
                }
                else {
                    color = Color.BLACK;
                }
                g2d.setColor(color);
                g2d.fillRect(tileX, tileY, sceneLength/GRID_SIZE, sceneHeight/GRID_SIZE);
            }
        }
    }
    public Dimension getPreferredSize() {
        return new Dimension(sceneLength, sceneHeight);
    }
    private static void createGUI(CellularAutomata mainPanel){
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    public void redrawBoard(){
        repaint();
    }
    public void updateState(int currentRow, int[] rule){
        for (int i = 0; i < board.length; i++){

            int one = board[currentRow][(i - 1 + GRID_SIZE) % GRID_SIZE];
            int two = board[currentRow][i];
            int three = board[currentRow][(i + 1) % GRID_SIZE];

            if (one == 1 && two == 1 && three == 1) board[currentRow + 1][i] = rule[0];
            if (one == 1 && two == 1 && three == 0) board[currentRow + 1][i] = rule[1];
            if (one == 1 && two == 0 && three == 1) board[currentRow + 1][i] = rule[2];
            if (one == 1 && two == 0 && three == 0) board[currentRow + 1][i] = rule[3];
            if (one == 0 && two == 1 && three == 1) board[currentRow + 1][i] = rule[4];
            if (one == 0 && two == 1 && three == 0) board[currentRow + 1][i] = rule[5];
            if (one == 0 && two == 0 && three == 1) board[currentRow + 1][i] = rule[6];
            if (one == 0 && two == 0 && three == 0) board[currentRow + 1][i] = rule[7];
        }
    }

    public static int[] ruleToArray(int rule){
        int[] ruleArray = new int[8];
        String ruleString = String.format("%8s", Integer.toBinaryString(rule)).replace(' ', '0');
        for (int i = 0; i < ruleString.length(); i++){
            ruleArray[i] = (ruleString.charAt(i) - '0');
        }
        return ruleArray;
    }
    public static void resetBoard(){
        for(int i = 0; i < GRID_SIZE; i++){
            Arrays.fill(board[i], 0);
        }
        board[0][GRID_SIZE / 2] = 1;
    }

    public static void main(String[] args) throws InterruptedException {
        CellularAutomata panel = new CellularAutomata();
        resetBoard();

        SwingUtilities.invokeLater(() -> {
            createGUI(panel);
            panel.redrawBoard(); // Update the display with the initial board
        });
        int intRule = 0;

        while (true) {
            int[] rule = CellularAutomata.ruleToArray(intRule);
            System.out.println(intRule);
            for (int i = 0; i < GRID_SIZE - 1; i++){
                panel.updateState(i, rule);
                panel.redrawBoard();
                Thread.sleep(1);
            }
//            for (int i = GRID_SIZE - 1; i > 1; i--){
//                Arrays.fill(board[i], 0);
//                panel.redrawBoard();
//                Thread.sleep(15);
//            }
            intRule = (intRule + 1 + 255) % 255;
        }
    }
}