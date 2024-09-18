package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import main.ui.Window;

public class Player {

    // -------------------------- Declaracion de variables --------------------------

    public static int x, y;
    public static int width, height;
    private int speed;
    private Color color;

    // todo para el salto y su fisica
    public static int velocidadY = 0; 
    private final int salto = 14; 

    // Imagen del jugador
    private Image playerImageRight1;
    private Image playerImageRight2;
    private Image playerImageLeft1;
    private Image playerImageLeft2;
    private Image playerImageLeftStatic;
    private Image playerImageRightStatic;
    private Image corazonCompleto;
    private Image medioCorazon;
    private Image currentPlayerImage;
    
    // variables para animacion
    private long lastUpdateTime = 0;
    private final long ANIMATION_DELAY = 200;
    
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    
    // variable para vida
    public static int vida = 10;

    // variable para finalizar golpe (basicamente para evitar spameo y soluiconar el problema de apretar una vez y que se den como 5 golpes)
    
    public static int finalizarGolpe = 0;
    
    // para las plataformas
    private List<Plataforma> plataformas;
    // ------------------------------------------------------------------------------

    public Player(int x, int y, int width, int height, int speed, Color color, 
            Image playerImageRight1, Image playerImageRight2, 
            Image playerImageLeft1, Image playerImageLeft2, Image playerImageLeftStatic, Image playerImageRightStatic, Image corazonCompleto, Image medioCorazon, List<Plataforma> plataformas) {
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
        this.corazonCompleto = corazonCompleto;
        this.medioCorazon = medioCorazon;
        this.currentPlayerImage = playerImageRightStatic; // Inicialmente, el jugador mira a la derecha
        this.plataformas = plataformas;
    }

    public void update(boolean left, boolean right, boolean space, boolean up, boolean a, boolean d, boolean w, boolean c) {
        long currentTime = System.currentTimeMillis();

        // Alterna entre las dos imagenes para la animacion
        if (currentTime - lastUpdateTime > ANIMATION_DELAY) {
            if (left || a) {
                currentPlayerImage = (currentPlayerImage == playerImageLeft1) ? playerImageLeft2 : playerImageLeft1;
                isMovingLeft = true;
                isMovingRight = false;
                Espada.ladoEspada = 1;
            } else if (right || d) {
                currentPlayerImage = (currentPlayerImage == playerImageRight1) ? playerImageRight2 : playerImageRight1;
                isMovingRight = true;
                isMovingLeft = false;
                Espada.ladoEspada = 2;
            } else {
                // No se mueve
                if (isMovingLeft) {
                    currentPlayerImage = playerImageLeftStatic;
                    Espada.ladoEspada = 1;
                } else if (isMovingRight) {
                    currentPlayerImage = playerImageRightStatic;
                    Espada.ladoEspada = 2;
                }
            }
            lastUpdateTime = currentTime;
        }
        
        // Movimiento
        if (left || a) {
            x -= speed;
            right = false;
        }
        if (right || d) {
            x += speed;
            left = false;

        }
        


        if ((space || up || w) && !verificarSalto()) {
            velocidadY = -salto; 
        }
        
        if ((space || up || w) && !verificarSaltoPlataforma()) {
            velocidadY = -salto; 
        }

        // Colisiones
        y += velocidadY;
        velocidadY += 1;

        if (x <= 20) x = 20;
        if (x >= Window.WIDTH - 30 - width) x = Window.WIDTH - 30 - width;
        if (y > ((Window.HEIGHT / 2) + 157) - height) {
            y = ((Window.HEIGHT / 2) + 157) - height;
            velocidadY = 0; 
        }
        
        // Golpe de espada
        
        if(c && finalizarGolpe == 0) {
        	Espada.golpeDado = true;
        	finalizarGolpe = 1;
        }
        
 
    }

    public void draw(Graphics g) {
        // Dibuja al jugador con la imagen si est� disponible
        if (currentPlayerImage != null) {
            g.drawImage(currentPlayerImage, x, y, width, height, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, width, height);    
        }
        
        // Dibuja las vidas del jugador
        if (corazonCompleto != null) {
            int corazonWidth = 30;
            int corazonHeight = 30;
            int espaciado = 5; // Espacio entre corazones
            int inicioX = 500; // Posición X inicial para dibujar corazones
            int inicioY = 30; // Posición Y para dibujar corazones

            // Dibuja los corazones completos
            if(vida != 0) {
	            for (int i = 0; i < vida / 2; i++) {
	                g.drawImage(corazonCompleto, inicioX + i * (corazonWidth + espaciado), inicioY, corazonWidth, corazonHeight, null);
	            }
	
	            // Si la vida es impar, dibuja un medio corazón
	            if (vida % 2 != 0) {
	                g.drawImage(medioCorazon, inicioX + (vida / 2) * (corazonWidth + espaciado), inicioY, corazonWidth, corazonHeight, null);
	            }            	
            }

        }

    }

    public boolean verificarSalto() {
        return y != ((Window.HEIGHT / 2) + 157) - height;
    }
    
    public boolean verificarSaltoPlataforma() {
        boolean enPlataforma = false;
        
        for (Plataforma plataforma : plataformas) {
            // Verifica si el jugador est� en el rango horizontal de la plataforma
            if (x + width > plataforma.getX() && x < plataforma.getX() + plataforma.getWidth()) {
                // Verifica si el jugador est� en el rango vertical de la plataforma
                if (y + height <= plataforma.getY() + 10 && y + height + velocidadY >= plataforma.getY()) {
                    enPlataforma = true;
                    y = plataforma.getY() - height; // Ajusta la posici�n del jugador para que est� justo encima de la plataforma
                    velocidadY = 0; // Detiene el movimiento vertical
                }
            }
        }
        
        return !enPlataforma;
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
