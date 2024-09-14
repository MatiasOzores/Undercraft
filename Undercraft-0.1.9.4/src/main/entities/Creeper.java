package main.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Creeper {

    // Variables del creeper
    private int x, y;
    private int speed = 2; // Velocidad de movimiento del creeper
    private Player player;
    private int explosionRadius = 100; // Rango en el que se activa el temporizador
    private int activationRadius = 50; // Rango más pequeño para activar el temporizador (más cerca del centro)
    private long timerStart = 0; // Tiempo en el que se activó el temporizador
    private boolean isTimerActive = false; // Indica si el temporizador está activo
    private long explosionTime = 3000; // Tiempo para la explosión (3 segundos)
    private boolean hasExploded = false; // Indica si el creeper ya explotó
    private int maxDamage = 5; // Daño máximo cuando está en el centro

    public Creeper(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public void update() {
        // Si el creeper no ha explotado aún
        if (!hasExploded) {
            // Detectar colisión o cercanía del jugador
            if (isPlayerInActivationRange()) {
                if (!isTimerActive) {
                    // Activar el temporizador si no está activo
                    timerStart = System.currentTimeMillis();
                    isTimerActive = true;
                    System.out.println("¡Temporizador activado!");
                } else {
                    // Si el temporizador está activo, comprobar el tiempo transcurrido
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - timerStart >= explosionTime) {
                        // El creeper explota y calcula el daño basado en la distancia
                        int damage = calculateDamage();
                        player.vida -= damage;
                        hasExploded = true;
                        System.out.println("¡El creeper explotó! Daño infligido: " + damage + ". Vida restante del jugador: " + player.vida);
                    }
                }
            } else {
                // Si el jugador sale del rango de activación, desactivar el temporizador y reiniciarlo
                if (isTimerActive) {
                    System.out.println("Jugador salió del rango de activación, temporizador reiniciado.");
                    isTimerActive = false;
                    timerStart = 0;
                }
                // Mover al creeper hacia el jugador si el temporizador no está activo
                followPlayer();
            }
        }
    }

    public void draw(Graphics g) {
        // Dibujar solo si el creeper no ha explotado
        if (!hasExploded) {
            g.setColor(Color.green);
            g.fillRect(x, y, 64, 64);
        }
    }

    // Función para verificar si el jugador está dentro del rango de activación
    private boolean isPlayerInActivationRange() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
        return distance <= activationRadius;
    }

    // Función para calcular el daño basado en la distancia
    private int calculateDamage() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));

        // El daño disminuye linealmente con la distancia
        if (distance > explosionRadius) {
            return 0; // Sin daño si está fuera del rango de explosión
        }

        // Calcular daño basado en qué tan cerca está del creeper
        double damageFactor = 1 - ((distance - (explosionRadius - activationRadius)) / activationRadius); // Factor de daño entre 0 y 1
        return (int) (maxDamage * damageFactor); // Daño proporcional a la cercanía
    }

    // Función para seguir al jugador
    private void followPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Movimiento horizontal
        if (x < playerX) {
            x += speed;
        } else if (x > playerX) {
            x -= speed;
        }

        // Movimiento vertical
        if (y < playerY) {
            y += speed;
        } else if (y > playerY) {
            y -= speed;
        }
    }
}
