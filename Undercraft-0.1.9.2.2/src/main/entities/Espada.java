package main.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Espada {
	
    private Player player;
	public int tipoEspada;
	public static int x, y, ladoEspada;
	
	public Espada(int tipoEspada, Player player) {
		this.x = player.getX()+20;
		this.y = player.getY()+20;
		this.player = player;
	}
	
	public void update() {
		
		if(ladoEspada == 2) {
			x = player.getX()+player.getWidth();
		}
		
		else if(ladoEspada == 1) {
			x = player.getX()-20;
		}

		y = player.getY();
	}
	
	public void draw(Graphics g) {
	}
	
	
}
