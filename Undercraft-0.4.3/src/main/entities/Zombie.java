package main.entities;

import java.awt.Graphics;
import java.awt.Image;

public class Zombie {
    public int x, y; // No estáticos para manejar múltiples zombies
    private Image zombieImage;
    private Player player;
    private int width = 64;  // Ancho del zombie (ajústalo según la imagen)
    private int height = 64; // Alto del zombie (ajústalo según la imagen)
    
    private long lastDamageTime = 0; // Momento en que se hizo daño por última vez
    private long damageInterval = 1000; // Intervalo de 1 segundo (1000 milisegundos)
    public boolean isDead = false;
    private int health = 3; // El zombie tiene 2 puntos de vida

    public Zombie(int x, int y, Player player, Image zombieImage) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.zombieImage = zombieImage;
    }

    public void update() {
        // Movimiento hacia el jugador en el eje X
        if (x < player.x) {
            x += 1; // Moverse hacia la derecha
        } else if (x > player.x) {
            x -= 1; // Moverse hacia la izquierda
        }

        // Detectar si colisiona con el jugador
        if (isCollidingWithPlayer()) {
            long currentTime = System.currentTimeMillis();
            
            // Si ha pasado al menos 1 segundo desde el último daño
            if (currentTime - lastDamageTime >= damageInterval && player.vida > 0) {
                player.vida--; // Reducir la vida del jugador
                lastDamageTime = currentTime; // Actualizar el tiempo del último daño
            }
        }
    }

    public void draw(Graphics g) {
        // Solo dibuja el zombie si tiene vida (health > 0)
        if (health > 0) {
            g.drawImage(zombieImage, x, y, null);
        }
    }

    // Método para detectar colisión
    private boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();  // Asegúrate de que el jugador tenga métodos para obtener sus dimensiones
        int playerHeight = player.getHeight();
        
        return (x < player.x + playerWidth && x + width > player.x &&
                y < player.y + playerHeight && y + height > player.y);
    }

    // Método takeDamage que reduce la vida del zombie
    public void takeDamage() {
        if (health > 0) {
            health--; // Reducir la vida del zombie
            // Si la vida del zombie es 0 o menos, eliminar o marcar al zombie como "muerto"
            if (health <= 0) {
                die();
            }
        }
    }
    
    // Método para manejar la muerte del zombie
    private void die() {
    	isDead = true;
    }
    
    // funciones
    
    public boolean isDead() {
        return isDead;
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
