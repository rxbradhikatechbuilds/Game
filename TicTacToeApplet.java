
/*Minor project game Tic tac toe using Applet and awt concept in java  */
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeApplet extends Applet implements MouseListener, ActionListener {
    char[][] board = new char[3][3];
    boolean xTurn = true;
    boolean gameOver = false;
    String winner = "";
    int moveCount = 0;
    int xMoves = 0;
    int oMoves = 0;
    Button restartButton;

    public void init() {
        setBackground(Color.BLACK);
        addMouseListener(this);

        restartButton = new Button("Restart Game");
        restartButton.addActionListener(this);
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(Color.LIGHT_GRAY);
        add(restartButton);

        setSize(1000, 800);
    }

    public void paint(Graphics g) {
        // Draw title
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.setColor(Color.RED);
        g.drawString("TIC TAC TOE GAME", 700, 200);

        // Draw move count
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Moves Played: " + moveCount, 300, 80);

        // Draw X and O move counts
        g.drawString("X Moves: " + xMoves, 250, 500);
        g.drawString("O Moves: " + oMoves, 450, 500);

        // Draw grid
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(5));

        int startX = 700, startY = 300, cellSize = 180;
        for (int i = 1; i < 3; i++) {
            g2.drawLine(startX, startY + i * cellSize, startX + 3 * cellSize, startY + i * cellSize);
            g2.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + 3 * cellSize);
        }

        // Draw X and O
        g.setFont(new Font("Arial", Font.BOLD, 60));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != '\0') {
                    g.setColor(board[i][j] == 'X' ? Color.BLUE : Color.MAGENTA);
                    g.drawString(String.valueOf(board[i][j]),
                            startX + j * cellSize + 90,
                            startY + i * cellSize + 80);
                }
            }
        }

        // Show winner or draw
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String msg = winner.equals("") ? "It's a Draw!" : winner + " Wins!";
            g.drawString("Game Over! " + msg, 800, 900);
        }

        // Display move history in a table format
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Move History (X vs O):", 250, 550);

        // Create a simple grid for move history
        int historyX = 250, historyY = 570, cellWidth = 100, cellHeight = 30;
        g.setColor(Color.GRAY);
        for (int i = 0; i < 3; i++) {
            g.drawLine(historyX, historyY + i * cellHeight, historyX + 2 * cellWidth, historyY + i * cellHeight); // Horizontal
                                                                                                                  // lines
        }
        g.drawLine(historyX, historyY + 3 * cellHeight, historyX + 2 * cellWidth, historyY + 3 * cellHeight); // Last
                                                                                                              // horizontal
                                                                                                              // line

        // Display the X and O moves in the history table
        for (int i = 0; i < 3; i++) {
            g.drawString("X: " + countMoves('X', i), historyX + 10, historyY + i * cellHeight + 20);
            g.drawString("O: " + countMoves('O', i), historyX + cellWidth + 10, historyY + i * cellHeight + 20);
        }
    }

    // Method to count the number of moves (X or O) for each row
    private int countMoves(char player, int row) {
        int count = 0;
        for (int col = 0; col < 3; col++) {
            if (board[row][col] == player) {
                count++;
            }
        }
        return count;
    }

    public void mousePressed(MouseEvent e) {
        if (gameOver)
            return;

        int x = e.getX();
        int y = e.getY();
        int startX = 700, startY = 300, cellSize = 180;

        int row = (y - startY) / cellSize;
        int col = (x - startX) / cellSize;

        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            if (board[row][col] == '\0') {
                board[row][col] = xTurn ? 'X' : 'O';

                if (xTurn) {
                    xMoves++;
                } else {
                    oMoves++;
                }

                xTurn = !xTurn;
                moveCount++;
                checkGameOver();
                repaint();

                if (gameOver && !winner.equals("")) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                        }
                        restartGame();
                    }).start();
                }
            }
        }
    }

    void checkGameOver() {
        // Rows and Columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winner = String.valueOf(board[i][0]);
                gameOver = true;
                return;
            }
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winner = String.valueOf(board[0][i]);
                gameOver = true;
                return;
            }
        }

        // Diagonals
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winner = String.valueOf(board[0][0]);
            gameOver = true;
            return;
        }
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winner = String.valueOf(board[0][2]);
            gameOver = true;
            return;
        }

        // Draw
        boolean full = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    full = false;
                    break;
                }
            }
        }
        if (full) {
            gameOver = true;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            restartGame();
        }
    }

    void restartGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
        }
        xTurn = true;
        gameOver = false;
        winner = "";
        moveCount = 0;
        xMoves = 0;
        oMoves = 0;
        repaint();
    }

    // Unused Mouse Events
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
