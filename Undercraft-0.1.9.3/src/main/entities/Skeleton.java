package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Skeleton {

    private int x, y;
    private Player player;
    private int speed = 1;

    private long lastDamageTime = 0; // Momento en que se hizo da�o por �ltima vez
    private long damageInterval = 1000; // Intervalo de 1 segundo para el da�o
    private long lastArrowShotTime = 0; // Momento en que se dispar� la �ltima flecha
    private long arrowInterval = 2000; // Intervalo de 2 segundos entre disparos
    
    private List<Arrow> arrows; // Lista de flechas disparadas

    // Variables para manejar el estado de "preparaci�n" y "quieto"
    private boolean isPreparingToShoot = false; // Si el esqueleto se est� preparando para disparar
    private long preparationEndTime = 0; // Tiempo en el que termina la preparaci�n
    private long preparationDuration = 1000; // El esqueleto se queda quieto por 1 segundo antes de disparar

    public Skeleton(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.arrows = new ArrayList<>(); // Inicializar la lista de flechas
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        // Si el esqueleto est� en preparaci�n para disparar, esperar 1 segundo
        if (isPreparingToShoot) {
            if (currentTime >= preparationEndTime) {
                shootArrow(); // Disparar flecha despu�s de la preparaci�n
                lastArrowShotTime = currentTime; // Actualizar el tiempo del �ltimo disparo
                isPreparingToShoot = false; // Termina el estado de preparaci�n
            } 
        } else {
            // Si no est� preparando para disparar, moverse hacia el jugador
            if (x < player.x) {
                x += speed; // Moverse hacia la derecha
            } else if (x > player.x) {
                x -= speed; // Moverse hacia la izquierda
            }

            // Iniciar la preparaci�n para disparar si ha pasado suficiente tiempo
            if (currentTime - lastArrowShotTime >= arrowInterval) {
                isPreparingToShoot = true;
                preparationEndTime = currentTime + preparationDuration; // Esperar 1 segundo antes de disparar
            }
        }

        // Actualizar la posici�n de las flechas, esto sigue ocurriendo aunque el esqueleto est� quieto
        for (int i = 0; i < arrows.size(); i++) {
            Arrow arrow = arrows.get(i);
            arrow.update();

            // Eliminar flechas que salieron de los l�mites
            if (arrow.isOutOfBounds()) {
                arrows.remove(i);
                i--;
            }
        }

        // Detectar colisi�n con el jugador
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

    // Funci�n para verificar colisi�n con el jugador
    private boolean isCollidingWithPlayer() {
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        return (x < player.x + playerWidth && x + 64 > player.x &&
                y < player.y + playerHeight && y + 64 > player.y);
    }

    // Funci�n para disparar una flecha
    private void shootArrow() {
        boolean movingRight = player.getX() > x; // Dispara hacia la direcci�n del jugador
        arrows.add(new Arrow(x, y + 32, movingRight, player)); // Dispara desde el centro vertical del esqueleto
    }
}
