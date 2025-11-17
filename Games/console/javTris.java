// javTris.java. Simple java Tetris clone by IrPolly

import java.awt.*; import javax.swing.*; import java.awt.event.*; import java.util.*;

public class Tetris extends JPanel implements ActionListener {
    static final int W = 10, H = 20, S = 30;
    Timer timer; int[][] board = new int[H][W];
    int[] piece = new int[4]; int px = 3, py = 0, score = 0;
    int[][][] shapes = {{{1,1,1,1}}, {{1,1},{1,1}}, {{1,1,1},{0,1,0}}, {{1,1,1},{1,0,0}}, {{1,1,1},{0,0,1}}, {{1,1,0},{0,1,1}}, {{0,1,1},{1,1,0}}};
    Color[] colors = {Color.CYAN, Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.ORANGE};

    Tetris() {
        setPreferredSize(new Dimension(W*S, H*S));
        setBackground(Color.BLACK); setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 37) { px--; if (collides()) px++; }
                if (e.getKeyCode() == 39) { px++; if (collides()) px--; }
                if (e.getKeyCode() == 40) drop();
                if (e.getKeyCode() == 38) rotate();
                repaint();
            }
        });
        newPiece(); timer = new Timer(400, this); timer.start();
    }

    void newPiece() { piece = shapes[new Random().nextInt(shapes.length)]; px = 3; py = 0; if (collides()) { timer.stop(); JOptionPane.showMessageDialog(this, "Game Over! Score: " + score); System.exit(0); } }
    boolean collides() { for (int y = 0; y < piece.length; y++) for (int x = 0; x < piece[y].length; x++) if (piece[y][x] == 1) { int nx = px + x, ny = py + y; if (nx < 0 || nx >= W || ny >= H || (ny >= 0 && board[ny][nx] != 0)) return true; } return false; }
    void rotate() { int[][] r = new int[piece[0].length][piece.length]; for (int y = 0; y < piece.length; y++) for (int x = 0; x < piece[y].length; x++) r[x][piece.length-1-y] = piece[y][x]; piece = r; if (collides()) { for (int i = 0; i < 3; i++) rotate(); } }
    void drop() { py++; if (collides()) { py--; merge(); clearLines(); newPiece(); } }
    void merge() { for (int y = 0; y < piece.length; y++) for (int x = 0; x < piece[y].length; x++) if (piece[y][x] == 1 && py+y >= 0) board[py+y][px+x] = 1; }
    void clearLines() { int lines = 0; for (int y = H-1; y >= 0; y--) { boolean full = true; for (int x = 0; x < W; x++) if (board[y][x] == 0) full = false; if (full) { lines++; for (int yy = y; yy > 0; yy--) board[yy] = board[yy-1]; board[0] = new int[W]; y++; } } score += lines * 100; }

    public void actionPerformed(ActionEvent e) { drop(); repaint(); }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < H; y++) for (int x = 0; x < W; x++) if (board[y][x] == 1) { g.setColor(Color.GREEN); g.fillRect(x*S, y*S, S-1, S-1); }
        g.setColor(colors[Arrays.asList(shapes).indexOf(piece)]);
        for (int y = 0; y < piece.length; y++) for (int x = 0; x < piece[y].length; x++) if (piece[y][x] == 1) g.fillRect((px+x)*S, (py+y)*S, S-1, S-1);
        g.setColor(Color.WHITE); g.drawString("Score: " + score, 10, 20);
    }

    public static void main(String[] args) { JFrame f = new JFrame("Tetris"); f.add(new Tetris()); f.pack(); f.setDefaultCloseOperation(3); f.setLocationRelativeTo(null); f.setVisible(true); }
}