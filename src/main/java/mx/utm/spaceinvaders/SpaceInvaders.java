package mx.utm.spaceinvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/** A small Java Swing game used as a Jenkins build artifact. */
public final class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 560;
    private final List<Invader> invaders = new ArrayList<>();
    private final List<Shot> shots = new ArrayList<>();
    private final Timer timer = new Timer(16, this);
    private int shipX = WIDTH / 2 - 26;
    private boolean left;
    private boolean right;
    private int score;
    private int direction = 1;
    private int ticks;
    private boolean gameOver;

    private SpaceInvaders() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(7, 12, 29));
        setFocusable(true);
        addKeyListener(this);
        createInvaders();
        timer.start();
    }

    private void createInvaders() {
        invaders.clear();
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 9; column++) {
                invaders.add(new Invader(110 + column * 62, 70 + row * 45));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(34, 211, 238));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.drawString("SPACE INVADERS", 20, 32);
        g.setColor(Color.WHITE);
        g.drawString("Puntos: " + score, 650, 32);

        g.setColor(new Color(148, 163, 184));
        for (int star = 0; star < 45; star++) {
            int x = (star * 83) % WIDTH;
            int y = 45 + (star * 47) % (HEIGHT - 55);
            g.fillRect(x, y, 2, 2);
        }

        g.setColor(new Color(74, 222, 128));
        for (Invader invader : invaders) {
            g.fillRoundRect(invader.x, invader.y, 34, 24, 6, 6);
            g.setColor(new Color(5, 46, 22));
            g.fillRect(invader.x + 8, invader.y + 7, 5, 5);
            g.fillRect(invader.x + 22, invader.y + 7, 5, 5);
            g.setColor(new Color(74, 222, 128));
        }

        g.setColor(new Color(96, 165, 250));
        int[] shipXs = {shipX, shipX + 26, shipX + 52};
        int[] shipYs = {HEIGHT - 46, HEIGHT - 72, HEIGHT - 46};
        g.fillPolygon(shipXs, shipYs, 3);
        for (Shot shot : shots) {
            g.setColor(new Color(250, 204, 21));
            g.fillRect(shot.x, shot.y, 4, 13);
        }

        if (gameOver) {
            g.setColor(new Color(255, 255, 255, 220));
            g.fillRoundRect(205, 205, 390, 120, 16, 16);
            g.setColor(new Color(15, 23, 42));
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
            String message = invaders.isEmpty() ? "¡Ganaste!" : "Fin del juego";
            g.drawString(message, 320, 255);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
            g.drawString("Presiona R para reiniciar", 280, 290);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!gameOver) {
            if (left) shipX = Math.max(10, shipX - 7);
            if (right) shipX = Math.min(WIDTH - 62, shipX + 7);
            moveInvaders();
            moveShots();
        }
        repaint();
    }

    private void moveInvaders() {
        ticks++;
        if (ticks % 18 != 0) return;
        boolean edge = invaders.stream().anyMatch(invader -> invader.x <= 20 || invader.x >= WIDTH - 55);
        if (edge) {
            direction *= -1;
            for (Invader invader : invaders) invader.y += 16;
        }
        for (Invader invader : invaders) {
            invader.x += direction * 8;
            if (invader.y > HEIGHT - 115) gameOver = true;
        }
    }

    private void moveShots() {
        Iterator<Shot> shotIterator = shots.iterator();
        while (shotIterator.hasNext()) {
            Shot shot = shotIterator.next();
            shot.y -= 11;
            if (shot.y < 45) {
                shotIterator.remove();
                continue;
            }
            Iterator<Invader> invaderIterator = invaders.iterator();
            while (invaderIterator.hasNext()) {
                Invader invader = invaderIterator.next();
                if (shot.x >= invader.x && shot.x <= invader.x + 34
                        && shot.y >= invader.y && shot.y <= invader.y + 24) {
                    invaderIterator.remove();
                    shotIterator.remove();
                    score += 10;
                    break;
                }
            }
        }
        if (invaders.isEmpty()) gameOver = true;
    }

    private void restart() {
        shipX = WIDTH / 2 - 26;
        shots.clear();
        score = 0;
        direction = 1;
        ticks = 0;
        gameOver = false;
        createInvaders();
    }

    @Override public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (event.getKeyCode() == KeyEvent.VK_SPACE && !gameOver && shots.size() < 3) {
            shots.add(new Shot(shipX + 24, HEIGHT - 84));
        }
        if (event.getKeyCode() == KeyEvent.VK_R && gameOver) restart();
    }
    @Override public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
    }
    @Override public void keyTyped(KeyEvent event) { }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders - UTM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new SpaceInvaders());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static final class Invader {
        private int x;
        private int y;
        private Invader(int x, int y) { this.x = x; this.y = y; }
    }
    private static final class Shot {
        private final int x;
        private int y;
        private Shot(int x, int y) { this.x = x; this.y = y; }
    }
}
