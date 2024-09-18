package main.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.ui.Window;

public class Item {

    private int x, y, width, height;
    private Player player;
    private boolean isActive; // Estado del objeto (si está activo o no)
    private boolean isColliding; // Estado de colisión
    private Image activeImage; // Imagen cuando el objeto está activo
    private Image inactiveImage; // Imagen cuando el objeto está desactivado

    // Constructor que recibe dos imágenes
    public Item(int x, int y, int width, int height, Player player, Image activeImage, Image inactiveImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.player = player;
        this.isActive = true; // El objeto está activo al inicio
        this.isColliding = false; // Al principio no está colisionando
        this.activeImage = activeImage; // Imagen activa
        this.inactiveImage = inactiveImage; // Imagen desactivada
    }

    public void update(boolean isEPressed) {
        // Verificar si el jugador está colisionando con el objeto
        if (isActive && checkCollision()) {
            isColliding = true;
            if (isEPressed) { // Si se presiona la tecla E
                Window.palancaActivada = true;
                isActive = false; // Cambiar a estado desactivado
            }
        } else {
            isColliding = false; // No hay colisión
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            // Dibujar la imagen activa si está activo
            g.drawImage(activeImage, x, y, width, height, null);
        } else {
            // Dibujar la imagen desactivada si no está activo
            g.drawImage(inactiveImage, x, y, width, height, null);
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
