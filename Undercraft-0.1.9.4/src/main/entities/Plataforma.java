package main.entities;

import java.awt.Graphics;
import java.awt.Image;

public class Plataforma {
    public int x, y;
    public int width, height;
    private Image textura;
    private Player player;

    public Plataforma(int x, int y, int width, int height, Image textura, Player player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textura = textura;
        this.player = player;
    }

    public void draw(Graphics g) {
        // Dibujar la imagen repetidamente para cubrir toda la plataforma
        for (int i = 0; i < width; i += textura.getWidth(null)) {
            for (int j = 0; j < height; j += textura.getHeight(null)) {
                g.drawImage(textura, x + i, y + j, null);
            }
        }
    }

    public void update() {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        // Detectar colisión por arriba (el jugador aterriza en la plataforma)
        if (playerX + playerWidth > x && playerX < x + width &&
            playerY + playerHeight <= y && playerY + playerHeight + player.velocidadY >= y) {
            // Aterriza en la plataforma
            player.y = y - playerHeight;
            player.velocidadY = 0;
        }
        // Detectar colisión por abajo (el jugador golpea la plataforma desde abajo)
        else if (playerX + playerWidth > x && playerX < x + width &&
                 playerY >= y + height && playerY + player.velocidadY <= y + height) {
            // Golpea la plataforma desde abajo
            player.y = y + height;
            player.velocidadY = 1;  // Empujar al jugador hacia abajo para que no se quede atascado
        }
        // Detectar colisión por la derecha (el jugador golpea el lado izquierdo de la plataforma)
        else if (playerX < x + width && playerX + playerWidth > x + width &&
                 playerY + playerHeight > y && playerY < y + height) {
            // Colisión por el lado derecho
            player.x = x + width;
        }
        // Detectar colisión por la izquierda (el jugador golpea el lado derecho de la plataforma)
        else if (playerX + playerWidth > x && playerX < x &&
                 playerY + playerHeight > y && playerY < y + height) {
            // Colisión por el lado izquierdo
            player.x = x - playerWidth;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
