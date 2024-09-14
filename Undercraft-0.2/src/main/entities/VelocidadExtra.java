package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class VelocidadExtra {

    private Player player;
    private boolean isActive;
    private boolean speedActivated;
    private long activationStartTime; // Tiempo cuando se activ� la velocidad extra
    private static final int SPEED_BOOST = 10; // Velocidad extra
    private static final long BOOST_DURATION = 1000; // Duraci�n del boost en milisegundos (3 segundos)
    public static int x, y;
    public static int speedPlayer = 0;

    public VelocidadExtra(int x, int y, Player player) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.isActive = true; // El objeto est� activo cuando se crea
        this.speedActivated = false;
    }

    public void update() {
        if (isActive && checkCollision()) {
            if (!speedActivated) {
                speedPlayer = SPEED_BOOST;
                activationStartTime = System.currentTimeMillis(); // Marcar el tiempo de activaci�n
                speedActivated = true;
                System.out.println("Velocidad extra activada: " + speedPlayer);
                isActive = false; // Desactivar el objeto despu�s de recogerlo
            }
        }
        
        // Si la velocidad est� activada, verificar el tiempo transcurrido
        if (speedActivated) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - activationStartTime >= BOOST_DURATION) {
                speedPlayer = 0; // Desactivar la velocidad extra despu�s del tiempo
                speedActivated = false;
                System.out.println("Velocidad extra desactivada: " + speedPlayer);
            }
        }
    }

    public void draw(Graphics g) {
        if (isActive) {
            // Establecer el color azul
            g.setColor(Color.blue);
            g.fillRect(x, y, 50, 50); // Usar fillRect para rellenar el cuadrado con color
        }
    }

    private boolean checkCollision() {
        // Obtener el rect�ngulo del jugador
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        // Obtener el rect�ngulo del objeto VelocidadExtra
        Rectangle vidaExtraRect = new Rectangle(x, y, 50, 50);
        // Verificar si los rect�ngulos se intersectan
        return playerRect.intersects(vidaExtraRect);
    }
}
