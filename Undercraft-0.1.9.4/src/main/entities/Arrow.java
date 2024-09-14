package main.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Arrow {

    private int x, y;
    private int width = 10;  // Ancho de la flecha
    private int height = 5;  // Alto de la flecha
    private int speed = 5;   // Velocidad de la flecha
    private boolean movingRight;
    private Player player;   // Referencia al jugador
    private boolean hasCollided = false; // Indica si la flecha ya colisionó

    public Arrow(int x, int y, boolean movingRight, Player player) {
        this.x = x;
        this.y = y;
        this.movingRight = movingRight;
        this.player = player; // Asignar referencia al jugador
    }

    public void update() {
        // Mover la flecha hacia la derecha o izquierda
        if (movingRight) {
            x += speed;
        } else {
            x -= speed;
        }

        // Verificar colisión con el jugador solo si no ha colisionado antes
        if (!hasCollided && isCollidingWithPlayer()) {
            player.vida--; // Reducir la vida del jugador
            System.out.println("¡Flecha impactó al jugador! Vida restante: " + player.vida);
            hasCollided = true; // Marcar la flecha como colisionada
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, width, height); // Dibujar la flecha como un rectángulo
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOutOfBounds() {
        // Verificar si la flecha sale de los límites de la pantalla
        return x < 0 || x > 800; // Cambia el ancho de acuerdo a tu ventana
    }

    // Verificar colisión con el jugador
    private boolean isCollidingWithPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        // Comprobar si la flecha está dentro de los límites del jugador
        return (x < playerX + playerWidth && x + width > playerX &&
                y < playerY + playerHeight && y + height > playerY);
    }
}
