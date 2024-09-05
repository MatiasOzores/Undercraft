package main.entities;

import java.awt.Color;
import java.awt.Graphics;


public class Plataforma {
    public static int x, y;
    public static int width, height;
    private Color color;
    private Player player;

    public Plataforma(int x, int y, int width, int height, Color color, Player player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.player = player;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void update() {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        if (playerX < x + width && playerX + playerWidth > x &&
                playerY < y + height && playerY > y) {
                player.y = y + height;
                player.velocidadY = 1; // Evita que se quede atascado
                
                System.out.println("ABA");
        }
        
        // Colisión desde arriba (el jugador aterriza en la plataforma)
        else if (playerX < x + width && playerX + playerWidth > x &&
            playerY + playerHeight <= y && playerY + playerHeight + player.velocidadY >= y) {
            player.y = y - playerHeight;
            player.velocidadY = 0;
        }

        // Colisión lateral derecha (el jugador se mueve hacia la derecha y golpea la plataforma)
        else if (playerX + playerWidth > x && playerX + playerWidth < x + width &&
            playerY < y + height && playerY + playerHeight > y) {
            player.x = x - playerWidth;
        }

        // Colisión lateral izquierda (el jugador se mueve hacia la izquierda y golpea la plataforma)
        else if (playerX < x + width && playerX > x &&
            playerY < y + height && playerY + playerHeight > y) {
            player.x = x + width;
        }

        // Colisión desde abajo (el jugador salta y golpea la parte inferior de la plataforma)

    }
}