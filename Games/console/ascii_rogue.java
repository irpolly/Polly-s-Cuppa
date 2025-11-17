// ascii_rogue.java by IrPolly

import java.awt.*; import java.awt.event.*; import javax.swing.*;

public class Rogue extends JPanel implements KeyListener {
    char[][] m; int px=1,py=1,hp=20,gold=0,lv=1; boolean alive=true;
    String msg = "Welcome to the dungeon! @ = you, g = goblin, $ = gold";

    Rogue() {
        setPreferredSize(new Dimension(800,600)); setBackground(Color.BLACK); setFocusable(true); addKeyListener(this);
        m = ("##################################################" +
             "#..................#.................#..........#" +
             "#..####.####..##### #......#####.....#..####....#" +
             "#.....#.#....#......#......#....#....#..#..#....#" +
             "#.....#.#....#......#......#....#....#..#..#....#" +
             "#..####.####..#####.#####..#####....#..####....#" +
             "#................................................#" +
             "##################################################").split("\n");
        for (int y=0;y<m.length;y++) m[y] = m[y].toCharArray();
        spawn('g', 15); spawn('$', 20);
    }

    void spawn(char c, int n) { for (int i=0;i<n;) { int x=(int)(Math.random()*48)+1, y=(int)(Math.random()*6)+1; if (m[y][x]=='.') { m[y][x]=c; i++; } } }
    public void keyPressed(KeyEvent e) {
        if (!alive) return;
        int dx=0,dy=0; char k=e.getKeyChar();
        if (k=='w') dy--; if (k=='s') dy++; if (k=='a') dx--; if (k=='d') dx++;
        if (dx==0 && dy==0) return;
        int nx=px+dx, ny=py+dy;
        if (m[ny][nx]=='.' || m[ny][nx]=='$' || m[ny][nx]=='g') {
            if (m[ny][nx]=='g') { hp-=3; msg="Goblin hits you!"; m[ny][nx]='.'; if (hp<=0) { alive=false; msg="You died!"; } }
            if (m[ny][nx]=='$') { gold+=10; msg="You found gold!"; }
            m[py][px]='.'; px=nx; py=ny; m[py][px]='@';
        }
        repaint();
    }
    public void keyTyped(KeyEvent e){} public void keyReleased(KeyEvent e){}

    public void paintComponent(Graphics g) {
        super.paintComponent(g); g.setColor(Color.GREEN);
        for (int y=0;y<m.length;y++) for (int x=0;x<m[y].length;x++) {
            g.setColor(m[y][x]=='#'?Color.GRAY : m[y][x]=='@'?Color.WHITE : m[y][x]=='g'?Color.RED : m[y][x]=='$'?Color.YELLOW : Color.DARK_GRAY);
            g.drawString(""+m[y][x], x*15, y*30+30);
        }
        g.setColor(Color.WHITE); g.drawString("HP:"+hp+" Gold:"+gold+"  "+msg, 10, 560);
    }

    public static void main(String[] a) { JFrame f=new JFrame("Tiny Rogue"); f.add(new Rogue()); f.pack(); f.setDefaultCloseOperation(3); f.setVisible(true); }
}