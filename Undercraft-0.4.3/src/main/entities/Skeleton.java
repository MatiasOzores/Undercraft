package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Skeleton {

    private int x, y;
    private Player player;
    private int speed = 1;

    private long lastDamageTime = 0; // Momento en que se hizo daño por última vez
    private long damageInterval = 1000; // Intervalo de 1 segundo para el daño
    private long lastArrowShotTime = 0; // Momento en que se disparó la última flecha
    private long arrowInterval = 2000; // Intervalo de disparo inicial (se recalcula aleatoriamente)
    private final int minArrowInterval = 1000; // Intervalo mínimo de 1.5 segundos
    private final int maxArrowInterval = 2000; // Intervalo máximo de 3.5 segundos
    private Random random = new Random();
    
    public boolean isDead = false;
    private int health = 5; // El esqueleto tiene 5 puntos de vida
    private List<Arrow> arrows; // Lista de flechas disparadas

    // Variables para manejar el estado de "preparación" y "quieto"
    private boolean isPreparingToShoot = false; // Si el esqueleto se está preparando para disparar
    private long preparationEndTime = 0; // Tiempo en el que termina la preparación
    private long preparationDuration = 1000; // El esqueleto se queda quieto por 1 segundo antes de disparar

    public Skeleton(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.arrows = new ArrayList<>(); // Inicializar la lista de flechas
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        // Si el esqueleto está en preparación para disparar, esperar hasta que termine la preparación
        if (isPreparingToShoot) {
            if (currentTime >= preparationEndTime) {
                shootArrow(); // Disparar flecha después de la preparación
                lastArrowShotTime = currentTime; // Actualizar el tiempo del último disparo
                isPreparingToShoot = false; // Termina el estado de preparación

                // Generar un nuevo intervalo aleatorio para el próximo disparo
                arrowInterval = minArrowInterval + random.nextInt(maxArrowInterval - minArrowInterval);
            }
        } else {
            // Movimiento hacia el jugador
            if (x < player.getX()) {
                x += speed; // Moverse hacia la derecha
            } else if (x > player.getX()) {
                x -= speed; // Moverse hacia la izquierda
            }

            // Iniciar la preparación para disparar si ha pasado suficiente tiempo
            if (currentTime - lastArrowShotTime >= arrowInterval) {
                isPreparingToShoot = true;
                preparationEndTime = currentTime + preparationDuration; // Esperar 1 segundo antes de disparar
            }
        }

        // Actualizar la posición de las flechas
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            arrow.update();

            // Eliminar flechas que salieron de los límites
            if (arrow.isOutOfBounds()) {
                arrows.remove(i);
                i--;
            }
        }

        // Detectar colisión con el jugador
        if (isCollidingWithPlayer()) {
            if (currentTime - lastDamageTime >= damageInterval) {
                player.vida--; // Reducir la vida del jugador
                lastDamageTime = currentTime;
            }
        }
    }

    public void draw(Graphics g) {
        // Dibujar el esqueleto
        g.setColor(Color.gray);
        g.fillRect(x, y, 64, 64);

        // Dibujar las flechas
        for (Arrow arrow : arrows) {
            arrow.draw(g);
        }
    }

    // Función para verificar colisión con el jugador
    private boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        return (x < player.getX() + playerWidth && x + 64 > player.getX() &&
                y < player.getY() + playerHeight && y + 64 > player.getY());
    }

    // Función para disparar una flecha
    private void shootArrow() {
        boolean movingRight = player.getX() > x; // Dispara hacia la dirección del jugador
        arrows.add(new Arrow(x, y + 32, movingRight, player)); // Dispara desde el centro vertical del esqueleto
    }
    
    public void takeDamage() {
        if (health > 0) {
            health--; // Reducir la vida del esqueleto
            // Si la vida del esqueleto es 0 o menos, eliminar o marcar al esqueleto como "muerto"
            if (health <= 0) {
                die();
            }
        }
    }
    
    // Método para manejar la muerte del esqueleto
    private void die() {
        isDead = true;
    }

    // Función para saber si está muerto
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
        return 64;
    }
    
    public int getHeight() {
        return 64;
    }
}
