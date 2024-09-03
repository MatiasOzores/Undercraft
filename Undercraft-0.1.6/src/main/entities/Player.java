package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import main.ui.Window;

public class Player {

    // -------------------------- Declaracion de variables --------------------------

    public int x, y;
    private int width, height;
    private int speed;
    private Color color;

    // todo para el salto y su fisica
    private int velocidadY = 0; 
    private final int salto = 12; 

    // Imagen del jugador
    private Image playerImageRight1;
    private Image playerImageRight2;
    private Image playerImageLeft1;
    private Image playerImageLeft2;
    private Image playerImageLeftStatic;
    private Image playerImageRightStatic;
    private Image currentPlayerImage;
    


    // variables para animacion
    
    private long lastUpdateTime = 0;
    private final long ANIMATION_DELAY = 200;
    
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    // ------------------------------------------------------------------------------

    public Player(int x, int y, int width, int height, int speed, Color color, 
            Image playerImageRight1, Image playerImageRight2, 
            Image playerImageLeft1, Image playerImageLeft2, Image playerImageLeftStatic, Image playerImageRightStatic) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
        this.playerImageRight1 = playerImageRight1;
        this.playerImageRight2 = playerImageRight2;
        this.playerImageLeft1 = playerImageLeft1;
        this.playerImageLeft2 = playerImageLeft2;
        this.playerImageLeftStatic = playerImageLeftStatic;
        this.playerImageRightStatic = playerImageRightStatic;
        this.currentPlayerImage = playerImageRightStatic; // Inicialmente, el jugador mira a la derecha
    }

    public void update(boolean left, boolean right, boolean space, boolean up) {
        long currentTime = System.currentTimeMillis();

        // Alterna entre las dos imagenes para la animacion (es la parte que menos entiendo) ------------------------
        if (currentTime - lastUpdateTime > ANIMATION_DELAY) {
            if (left) {
                currentPlayerImage = (currentPlayerImage == playerImageLeft1) ? playerImageLeft2 : playerImageLeft1;
                isMovingLeft = true;
                isMovingRight = false;
            } else if (right) {
                currentPlayerImage = (currentPlayerImage == playerImageRight1) ? playerImageRight2 : playerImageRight1;
                isMovingRight = true;
                isMovingLeft = false;
            } else {
                // No se mueve
                if (isMovingLeft) {
                    currentPlayerImage = playerImageLeftStatic;
                } else if (isMovingRight) {
                    currentPlayerImage = playerImageRightStatic;
                }
            }
            lastUpdateTime = currentTime;
        }
        
        // ----------------------------------------------------------------------------

        if (left) {
            x -= speed; // cada vez que se presiona la flecha izquierda se mueve hacia la izquierda
        }
        if (right) {
            x += speed; // cada vez que se presiona la flecha derecha se mueve hacia la derecha
        }

        if ((space || up) && !verificarSalto()) {
            velocidadY = -salto; 
        }

        
        // Colisiones -----------------------------------------------------
        
        y += velocidadY;
        velocidadY += 1;

        if (x <= 20) x = 20;
        if (x >= Window.WIDTH - 30 - width) x = Window.WIDTH - 30 - width;
        if (y > ((Window.HEIGHT / 2) + 150) - height) {
            y = ((Window.HEIGHT / 2) + 150) - height;
            velocidadY = 0; 
        }
        
        // Fin Colisiones -----------------------------------------------------
    }

    public void draw(Graphics g) {
        // Dibuja al jugador con la imagen si está disponible
        if (currentPlayerImage != null) {
            g.drawImage(currentPlayerImage, x, y, width, height, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, width, height);    
        }
    }

    public boolean verificarSalto() {
        return y != ((Window.HEIGHT / 2) + 150) - height;
    }
}
