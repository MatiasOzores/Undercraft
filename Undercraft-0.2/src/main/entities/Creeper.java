package main.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Creeper {

    // Variables del creeper
    private int x, y;
    private int speed = 2; // Velocidad de movimiento del creeper
    private Player player;
    private int explosionRadius = 100; // Rango en el que se activa el temporizador
    private int activationRadius = 50; // Rango m�s peque�o para activar el temporizador (m�s cerca del centro)
    private long timerStart = 0; // Tiempo en el que se activ� el temporizador
    private boolean isTimerActive = false; // Indica si el temporizador est� activo
    private long explosionTime = 3000; // Tiempo para la explosi�n (3 segundos)
    private boolean hasExploded = false; // Indica si el creeper ya explot�
    private int maxDamage = 5; // Da�o m�ximo cuando est� en el centro

    public Creeper(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public void update() {
        // Si el creeper no ha explotado a�n
        if (!hasExploded) {
            // Detectar colisi�n o cercan�a del jugador
            if (isPlayerInActivationRange()) {
                if (!isTimerActive) {
                    // Activar el temporizador si no est� activo
                    timerStart = System.currentTimeMillis();
                    isTimerActive = true;
                } else {
                    // Si el temporizador est� activo, comprobar el tiempo transcurrido
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - timerStart >= explosionTime) {
                        // El creeper explota y calcula el da�o basado en la distancia
                        int damage = calculateDamage();
                        player.vida -= damage;
                        hasExploded = true;
                    }
                }
            } else {
                // Si el jugador sale del rango de activaci�n, desactivar el temporizador y reiniciarlo
                if (isTimerActive) {
                    isTimerActive = false;
                    timerStart = 0;
                }
                // Mover al creeper hacia el jugador si el temporizador no est� activo
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

    // Funci�n para verificar si el jugador est� dentro del rango de activaci�n
    private boolean isPlayerInActivationRange() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));
        return distance <= activationRadius;
    }

    // Funci�n para calcular el da�o basado en la distancia
    private int calculateDamage() {
        int playerX = player.getX();
        int playerY = player.getY();
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2));

        // El da�o disminuye linealmente con la distancia
        if (distance > explosionRadius) {
            return 0; // Sin da�o si est� fuera del rango de explosi�n
        }

        // Calcular da�o basado en qu� tan cerca est� del creeper
        double damageFactor = 1 - ((distance - (explosionRadius - activationRadius)) / activationRadius); // Factor de da�o entre 0 y 1
        return (int) (maxDamage * damageFactor); // Da�o proporcional a la cercan�a
    }

    // Funci�n para seguir al jugador
    private void followPlayer() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Movimiento horizontal
        if (x < playerX) {
            x += speed;
        } else if (x > playerX) {
            x -= speed;
        }
    }
}
