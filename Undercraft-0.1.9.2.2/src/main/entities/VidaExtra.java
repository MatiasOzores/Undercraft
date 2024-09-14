package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class VidaExtra {
    
    private Player player;
    public static int x, y;
    private boolean isActive;
    

    public VidaExtra(int x, int y, Player player) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.isActive = true; // El objeto est� activo cuando se crea
    }

    public void update() {
        if (isActive && checkCollision()) {
            if(Player.vida < 10) {
            	Player.vida++;
            }
        	
            isActive = false;
            // Aqu� podr�as realizar otras acciones, como aumentar la puntuaci�n del jugador
        }
    }
    
    public void draw(Graphics g) {
        if (isActive) {
            // Establecer el color verde
            g.setColor(Color.GREEN);
            g.fillRect(x, y, 50, 50); // Usar fillRect para rellenar el cuadrado con color
        }
    }

    private boolean checkCollision() {
        // Obtener el rect�ngulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rect�ngulo del objeto VidaExtra
        Rectangle vidaExtraRect = new Rectangle(x, y, 50, 50);
        // Verificar si los rect�ngulos se intersectan
        return playerRect.intersects(vidaExtraRect);
    }
}
