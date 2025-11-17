// Sneck.java. Simple java Snake clone by IrPolly

import java.awt.*; import javax.swing.*; import java.awt.event.*; import java.util.*;

public class Snake extends JPanel implements KeyListener {
    static final int SIZE = 20, WIDTH = 30, HEIGHT = 20;
    enum Dir { UP, DOWN, LEFT, RIGHT } Dir dir = Dir.RIGHT;
    Deque<Point> snake = new ArrayDeque<>();
    Point food; Timer timer; int score = 0, delay = 120;

    Snake() {
        setPreferredSize(new Dimension(WIDTH*SIZE, HEIGHT*SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        reset();
        timer = new Timer(delay, e -> { move(); repaint(); });
        timer.start();
    }

    void reset() {
        snake.clear();
        snake.add(new Point(5, 10));
        spawnFood();
        dir = Dir.RIGHT;
        score = 0;
        delay = 120;
        timer.setDelay(delay);
    }

    void spawnFood() {
        Random r = new Random();
        do food = new Point(r.nextInt(WIDTH), r.nextInt(HEIGHT));
        while (snake.contains(food));
    }

    void move() {
        Point head = snake.getFirst();
        Point newHead = switch (dir) {
            case UP -> new Point(head.x, head.y - 1);
            case DOWN -> new Point(head.x, head.y + 1);
            case LEFT -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Score: " + score + "\nPlay again?", "Snake", JOptionPane.YES_NO_OPTION);
            reset();
            timer.start();
            return;
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            score++;
            spawnFood();
            if (score % 5 == 0 && delay > 40) timer.setDelay(delay = delay - 10); // speed up
        } else snake.removeLast();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        snake.forEach(p -> g.fillRect(p.x*SIZE, p.y*SIZE, SIZE, SIZE));
        g.setColor(Color.RED);
        g.fillOval(food.x*SIZE, food.y*SIZE, SIZE, SIZE);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> { if (dir != Dir.DOWN) dir = Dir.UP; }
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> { if (dir != Dir.UP) dir = Dir.DOWN; }
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> { if (dir != Dir.RIGHT) dir = Dir.LEFT; }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> { if (dir != Dir.LEFT) dir = Dir.RIGHT; }
        }
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Snake");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new Snake());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}