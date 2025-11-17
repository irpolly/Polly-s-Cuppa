// 2048.java by IrPolly
import java.awt.*; import javax.swing.*; import java.awt.event.*;

public class Game2048 extends JPanel {
    int[][] b = new int[4][4]; int score = 0; boolean lost = false;
    Color[] c = {new Color(205,193,180), new Color(238,228,218), new Color(237,224,200), new Color(242,177,121), new Color(245,149,99), new Color(246,124,95), new Color(246,94,59), new Color(237,207,114), new Color(237,204,97), new Color(237,200,80), new Color(237,197,63), new Color(237,194,46), new Color(60,58,50)};

    Game2048() {
        setPreferredSize(new Dimension(480,480)); setBackground(new Color(187,173,160)); setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (lost) return;
                int k = e.getKeyCode();
                if (k == 37 || k == 38 || k == 39 || k == 40) {
                    boolean moved = false;
                    if (k == 37) moved = slideLeft();
                    if (k == 39) { rotate(); slideLeft(); rotate(); moved = true; }
                    if (k == 38) { rotate(); rotate(); rotate(); slideLeft(); rotate(); moved = true; }
                    if (k == 40) { rotate(); rotate(); slideLeft(); rotate(); rotate(); moved = true; }
                    if (moved) addRandom();
                    repaint();
                    if (!canMove() && !lost) { lost = true; JOptionPane.showMessageDialog(null, "Game Over! Score: " + score); }
                }
            }
        });
        addRandom(); addRandom();
    }

    void addRandom() { java.util.List<Point> e = new java.util.ArrayList<>(); for (int y=0;y<4;y++) for (int x=0;x<4;x++) if (b[y][x]==0) e.add(new Point(x,y)); if (!e.isEmpty()) { Point p = e.get(new java.util.Random().nextInt(e.size())); b[p.y][p.x] = Math.random() < 0.9 ? 2 : 4; } }
    boolean slideLeft() { boolean m = false; for (int y=0;y<4;y++) { int[] r = new int[4]; int p=0; for (int x=0;x<4;x++) if (b[y][x]!=0) { if (p>0 && r[p-1]==b[y][x]) { r[p-1]*=2; score+=r[p-1]; m=true; } else r[p++]=b[y][x]; } for (int x=0;x<4;x++) { if (b[y][x]!=r[x]) m=true; b[y][x]=x<p ? r[x] : 0; } } return m; }
    void rotate() { int[][] t = new int[4][4]; for (int y=0;y<4;y++) for (int x=0;x<4;x++) t[x][3-y]=b[y][x]; b=t; }
    boolean canMove() { for (int y=0;y<4;y++) for (int x=0;x<4;x++) if (b[y][x]==0 || (x<3 && b[y][x]==b[y][x+1]) || (y<3 && b[y][x]==b[y+1][x])) return true; return false; }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y=0;y<4;y++) for (int x=0;x<4;x++) {
            int v = b[y][x]; g.setColor(v==0 ? new Color(205,193,180) : c[(int)(Math.log(v)/Math.log(2))-1]);
            g.fillRoundRect(x*110+15, y*110+15, 100, 100, 15, 15);
            if (v>0) { g.setColor(v<8 ? new Color(119,110,101) : Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, v<1000 ? 40 : 30)); g.drawString(""+v, x*110+55, y*110+70); }
        }
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 24)); g.drawString("Score: " + score, 20, 40);
    }

    public static void main(String[] a) { JFrame f = new JFrame("2048"); f.add(new Game2048()); f.pack(); f.setDefaultCloseOperation(3); f.setLocationRelativeTo(null); f.setVisible(true); }
}