package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Lava {
    public static int x, y, width, height;
    private boolean isDamaging;
    private long lastDamageTime;
    private static final long DAMAGE_INTERVAL = 1000; // 1 second
    private static final int INITIAL_DAMAGE = 2;
    private static final int REDUCED_DAMAGE = 1;
    private int currentDamage;
    private long lavaDurationStart;

    public Lava(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isDamaging = false;
        this.lastDamageTime = System.currentTimeMillis();
        this.currentDamage = INITIAL_DAMAGE;
        this.lavaDurationStart = 0;
    }

    public void update(Player player) {
        if (isColliding(player)) {
            isDamaging = true;
            if (lavaDurationStart == 0) {
                lavaDurationStart = System.currentTimeMillis();
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDamageTime > DAMAGE_INTERVAL) {
                player.takeDamage(currentDamage);
                lastDamageTime = currentTime;
                System.out.println("Player is in lava. Life reduced by: " + currentDamage);
                
                // Reduce damage after 2 seconds
                if (currentTime - lavaDurationStart > 2000 && currentDamage > REDUCED_DAMAGE) {
                    currentDamage = REDUCED_DAMAGE;
                }
            }
            player.setInLava(true);
        } else {
            isDamaging = false;
            player.setInLava(false);
            lavaDurationStart = 0;
            currentDamage = INITIAL_DAMAGE;
        }
    }

    public static boolean isColliding(Player player) {
        Rectangle lavaRect = new Rectangle(x, y, width, height);
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return lavaRect.intersects(playerRect);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0,0)); // Color negro con alfa = 0 (totalmente transparente)
        g.fillRect(x, y, width, height);
    }


    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}