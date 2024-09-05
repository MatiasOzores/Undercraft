package main.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Plataforma {
    private int x, y;
    private int width, height;
    private Color color;
    private Player player;
    // variables para logica de colision

    
    public Plataforma(int x, int y, int width, int height, Color color, Player player) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.player = player;
    }

    public void draw(Graphics g) {
    	g.setColor(color);
    	g.fillRect(x, y, width, height);
    }

    public void update() {
    	
    	// trabajas la colision de la plataforma con el jugador
    	if((player.getX() >= x && player.getX() <= x + width)) {
    		System.out.println("X está, pero falta Y");
    		
    		if((player.getY() == y - player.getHeight() )) {
    			System.out.println("Y ESTA COMPLETO");
    			player.y = y - player.getHeight(); 
    			player.velocidadY = 0;
    		}
    	}
    }
    
}
