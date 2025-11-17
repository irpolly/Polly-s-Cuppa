// Poke Clone in java - Single-file text-based Pokemon-style RPG!
// Explore maps, battle wild monsters, catch them, level up. WASD to move, C to catch, B battle menu.
// Run: java pokeClone.java (Java 11+)

import java.awt.*; import javax.swing.*; import java.awt.event.*; import java.util.*;

public class PokemonRPG extends JPanel implements KeyListener, ActionListener {
    static final int W = 40, H = 25, TILE = 20;
    char[][] map = new char[H][W]; Player player = new Player();
    List<Monster> monsters = new ArrayList<>(); Random r = new Random();
    Timer timer; boolean battleMode = false; Monster enemy;
    String msg = "Welcome to Monster RPG! Explore (WASD), battle wild monsters, catch with C!";

    PokemonRPG() {
        setPreferredSize(new Dimension(W*TILE, H*TILE + 100)); setBackground(Color.BLACK);
        setFocusable(true); addKeyListener(this);
        generateMap(); player.x = 20; player.y = 12; spawnMonsters();
        timer = new Timer(1000, this); timer.start();
    }

    void generateMap() {
        for (int y = 0; y < H; y++) for (int x = 0; x < W; x++) map[y][x] = r.nextBoolean() ? '.' : '#';
        // Add some paths
        for (int i = 0; i < 100; i++) { int x = r.nextInt(W), y = r.nextInt(H); map[y][x] = '.'; }
    }

    void spawnMonsters() {
        monsters.clear();
        for (int i = 0; i < 20; i++) {
            int x, y; do { x = r.nextInt(W); y = r.nextInt(H); } while (map[y][x] != '.');
            monsters.add(new Monster("Wild " + names[r.nextInt(names.length)], r.nextInt(10, 30)));
        }
    }

    static String[] names = {"Pika", "Charm", "Bulba", "Squirt", "Rat", "Cater", "Weed", "Pidge", "Nido"};

    static class Player {
        int x = 0, y = 0, hp = 100, maxHp = 100, atk = 20, lvl = 5; List<Monster> party = new ArrayList<>();
        Player() { party.add(new Monster("Starter Pika", 25)); }
        boolean move(int dx, int dy, char[][] map) {
            int nx = x + dx, ny = y + dy;
            if (nx >= 0 && nx < W && ny >= 0 && ny < H && map[ny][nx] == '.') {
                x = nx; y = ny; return true;
            }
            return false;
        }
    }

    static class Monster {
        String name; int hp, maxHp, atk, lvl;
        Monster(String n, int l) { name = n; lvl = l; maxHp = hp = 20 + l * 5; atk = 10 + l * 2; }
    }

    @Override public void keyPressed(KeyEvent e) {
        if (battleMode) return;
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) player.move(0, -1, map);
        if (code == KeyEvent.VK_S) player.move(0, 1, map);
        if (code == KeyEvent.VK_A) player.move(-1, 0, map);
        if (code == KeyEvent.VK_D) player.move(1, 0, map);
        if (code == KeyEvent.VK_C) tryCatch();
        checkWildBattle();
        repaint();
    }

    void checkWildBattle() {
        for (int i = monsters.size() - 1; i >= 0; i--) {
            Monster m = monsters.get(i);
            if (Math.abs(player.x - m.x) + Math.abs(player.y - m.y) < 2 && r.nextInt(10) < 3) {
                battleMode = true; enemy = new Monster(m.name, m.lvl); msg = "Wild " + m.name + " appeared!";
                monsters.remove(i); repaint(); return;
            }
        }
    }

    void tryCatch() {
        if (enemy == null || !battleMode) return;
        if (r.nextInt(100) < 50 + player.lvl * 2 - enemy.lvl * 3) {
            enemy.hp = enemy.maxHp; player.party.add(enemy); endBattle("Caught " + enemy.name + "!");
        } else {
            msg = "Catch failed! " + enemy.name + " broke free!";
        }
    }

    void endBattle(String m) {
        battleMode = false; enemy = null; msg = m;
        spawnMonsters(); // Respawn
    }

    public void actionPerformed(ActionEvent e) { repaint(); } // Auto repaint

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g); g.setColor(Color.DARK_GRAY); g.fillRect(0, 0, W*TILE, H*TILE);
        // Draw map
        for (int y = 0; y < H; y++) for (int x = 0; x < W; x++) {
            g.setColor(map[y][x] == '#' ? Color.GRAY : Color.GREEN); g.fillRect(x*TILE, y*TILE, TILE, TILE);
        }
        // Player
        g.setColor(Color.YELLOW); g.fillOval(player.x*TILE + 2, player.y*TILE + 2, TILE-4, TILE-4);
        // Monsters
        for (Monster m : monsters) {
            g.setColor(Color.RED); g.fillOval(m.x*TILE + 4, m.y*TILE + 4, TILE-8, TILE-8);
        }
        // UI
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(msg, 10, H*TILE + 25);
        g.drawString("HP: " + player.hp + "/" + player.maxHp + " LVL: " + player.lvl + " Party: " + player.party.size(), 10, H*TILE + 50);
        if (battleMode) drawBattle(g);
    }

    void drawBattle(Graphics g) {
        g.setColor(new Color(0,0,50)); g.fillRect(0, H*TILE - 100, getWidth(), 100);
        g.setColor(Color.WHITE); g.drawString("BATTLE! " + enemy.name + " HP: " + enemy.hp, 10, H*TILE - 70);
        g.drawString("Press C to Catch! Use party to fight (auto).", 10, H*TILE - 50);
        // Simulate auto-attack
        if (r.nextBoolean()) {
            int dmg = Math.max(1, player.atk - r.nextInt(enemy.atk / 2));
            enemy.hp -= dmg; g.drawString("Dealt " + dmg + " dmg!", 10, H*TILE - 30);
            if (enemy.hp <= 0) endBattle("Wild " + enemy.name + " fainted!");
        }
    }

    public void keyTyped(KeyEvent e) {} public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Monster RPG - Pokemon Clone"); f.add(new PokemonRPG());
        f.pack(); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); f.setLocationRelativeTo(null); f.setVisible(true);
    }
}