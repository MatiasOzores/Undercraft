package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.ui.Window;

public class Item {

    private int x, y, width, height;
    private Player player;
    private boolean isActive; // Estado del objeto (si está activo o no)
    private boolean isColliding; // Estado de colisión

    public Item(int x, int y, int width, int height, Player player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.isActive = true; // El objeto está activo al inicio
        this.isColliding = false; // Al principio no está colisionando
    }

    public void update(boolean isEPressed) {
        // Verificar si el jugador está colisionando con el objeto
        if (isActive && checkCollision()) {
            isColliding = true;
            if (isEPressed) { // Si se presiona la tecla E
            	Window.palancaActivada = true;
                System.out.println("Colisión detectada. Has presionado la tecla E.");
                isActive = false; // Se puede desactivar el objeto o realizar alguna acción
            }
        } else {
            isColliding = false; // No hay colisión
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height); // Dibujar solo si el objeto está activo
        }
    }

    // Función para verificar si el jugador está colisionando con el objeto
    private boolean checkCollision() {
        // Obtener el rectángulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rectángulo del objeto Item
        Rectangle itemRect = new Rectangle(x, y, width, height);
        // Verificar si los rectángulos se intersectan (colisión)
        return playerRect.intersects(itemRect);
    }
}
