package main.entities;

import java.awt.Color;
import java.awt.Graphics;

import main.ui.Window;

public class Player {
	
	// -------------------------- Declaracion de variables --------------------------
	
	public int x, y;
    private int width, height;
    private int speed;
    private Color color;
    
    // todo para el salto y su fisica
    private int velocidadY = 0; 
    private final int salto = 8; 
	
	// ------------------------------------------------------------------------------

    public Player(int x, int y, int width, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }	
	
    public void update(boolean left, boolean right, boolean space, boolean up) {
    	
    	// en el caso de que el booleano left o right sea true va entrar en su respectivo caso...
    	
        if (left) {
            x -= speed; // se desplaza cierta cantidad de pixeles determinados por speed hacia la izquierda
        }
        if (right) {
            x += speed; // se desplaza cierta cantidad de pixeles determinados por speed hacia la derecha
        }
        
        if ((space || up) && !verificarSalto()) {
            velocidadY = -salto; // simula el salto
        }
        
        y += velocidadY;  // esto lo que hace es que inicialmente cuando no se dan saltos esto al estar inicializado en 0 no va a disminuir nada en "y", pero cuando saltamos este toma el valor negativo de salto, por lo tanto va a dar ese efecto de salto y gracias a la colision del suelo 

        velocidadY += 1; // simula gravedad
        
        
       
        // Colisiones del jugador ---------------------------------------------------------------------------
        
        // Pared izquierda
        if(x <= 20) {
        	x = 20;
        }
        
        // Pared derecha
        if(x >= Window.WIDTH-30-width) {
        	x = Window.WIDTH-30-width;
        }
        
        // Suelo
        if (y > Window.HEIGHT/2 - height) {
            y = Window.HEIGHT/2 - height;
            velocidadY = 0; 
        }
        
        
    }
    
    public void draw(Graphics g) {
    	
    	// Dibuja al jugador con las dimensiones y el color determinado
        g.setColor(color);
        g.fillRect(x, y, width, height);	
        
    }
	
    public boolean verificarSalto() {
    	if(y != Window.HEIGHT/2 - height) {
    		return true;
    	}
    	
    	return false;
    }
	
}
