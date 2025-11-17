// minesweep.java by IrPolly

import java.awt.*; import javax.swing.*; import java.awt.event.*;

public class Minesweeper extends JPanel {
    final int W = 16, H = 16, MINES = 40; boolean[][] mine = new boolean[H][W], flag = new boolean[H][W], open = new boolean[H][W];
    int opened = 0; boolean dead = false;

    Minesweeper() {
        setPreferredSize(new Dimension(600, 600)); setBackground(Color.LIGHT_GRAY); setFocusable(true);
        while (MINES > 0) { int x = (int)(Math.random()*W), y = (int)(Math.random()*H); if (!mine[y][x]) { mine[y][x] = true; MINES--; } }
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (dead) return;
                int x = e.getX()/37, y = e.getY()/37;
                if (SwingUtilities.isRightMouseButton(e)) { flag[y][x] = !flag[y][x]; repaint(); }
                else if (!flag[y][x] && !open[y][x]) { open[y][x] = true; opened++; if (mine[y][x]) dead = true; else if (count(y,x)==0) flood(y,x); repaint(); }
                if (opened == W*H - 40 && !dead) { dead = true; JOptionPane.showMessageDialog(null, "You Win!"); }
                if (dead && mine[y][x]) repaint();
            }
        });
    }

    int count(int y, int x) { int c = 0; for (int dy=-1;dy<=1;dy++) for (int dx=-1;dx<=1;dx++) if (y+dy>=0 && y+dy<H && x+dx>=0 && x+dx<W && mine[y+dy][x+dx]) c++; return c; }
    void flood(int y, int x) { for (int dy=-1;dy<=1;dy++) for (int dx=-1;dx<=1;dx++) { int ny=y+dy, nx=x+dx; if (ny>=0 && ny<H && nx>=0 && nx<W && !open[ny][nx] && !mine[ny][nx] && !flag[ny][nx]) { open[ny][nx]=true; opened++; if (count(ny,nx)==0) flood(ny,nx); } } }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y=0;y<H;y++) for (int x=0;x<W;x++) {
            int X = x*37+10, Y = y*37+10;
            g.setColor(open[y][x] ? Color.WHITE : Color.GRAY); g.fillRect(X, Y, 36, 36); g.setColor(Color.BLACK); g.drawRect(X, Y, 36, 36);
            if (flag[y][x]) { g.setColor(Color.RED); g.drawString("F", X+12, Y+25); }
            else if (open[y][x] && mine[y][x]) { g.setColor(Color.RED); g.fillOval(X+8, Y+8, 20, 20); }
            else if (open[y][x] && count(y,x)>0) { g.setColor(new Color[] {Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK, Color.BLACK}[count(y,x)-1]); g.drawString(""+count(y,x), X+13, Y+25); }
        }
        if (dead) { g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 60)); g.drawString(opened == W*H-40 ? "WIN!" : "BOOM!", 150, 300); }
    }

    public static void main(String[] a) { JFrame f = new JFrame("Minesweeper"); f.add(new Minesweeper()); f.pack(); f.setDefaultCloseOperation(3); f.setLocationRelativeTo(null); f.setVisible(true); }
}