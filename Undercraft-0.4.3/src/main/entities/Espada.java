package main.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Espada {
	
    private Player player;
	public int tipoEspada;
	public static int x, y, ladoEspada;
	private List<Zombie> zombies;
	private List<Skeleton> skeletons;
	public static boolean golpeDado = false;
	
	public Espada(int tipoEspada, Player player, List<Zombie> zombies, List<Skeleton> skeletons) {
		this.x = player.getX()+20;
		this.y = player.getY()+20;
		this.player = player;
		this.zombies = zombies;
		this.skeletons = skeletons;
	}
	
	public void update() {
		
		if(ladoEspada == 2) {
			x = player.getX()+player.getWidth();
		}
		
		else if(ladoEspada == 1) {
			x = player.getX()-20;
		}

		y = player.getY();
		
		if (golpeDado) {
		    for (Zombie enemigo : zombies) {
		        if (isCollidingWithEnemy(enemigo)) {  // Verificar colisión

		            enemigo.takeDamage();  // Aplica el daño al zombie

		            golpeDado = false;  // Reiniciar el estado del golpe
		            break;  // Romper el bucle después de aplicar el daño, ya que no puedes golpear varios a la vez
		        }
		    }
		    
		    for (Skeleton enemigo : skeletons) {
		        if (isCollidingWithEnemy2(enemigo)) {  // Verificar colisión

		            enemigo.takeDamage();  // Aplica el daño al zombie

		            golpeDado = false;  // Reiniciar el estado del golpe
		            break;  // Romper el bucle después de aplicar el daño, ya que no puedes golpear varios a la vez
		        }
		    }
		}

	}
	
	public void draw(Graphics g) {
	}

	private boolean isCollidingWithEnemy(Zombie zombie) {
        int swordX = (ladoEspada == 2) ? player.getX() + player.getWidth() : player.getX() - 20;
        int swordWidth = 40; // Ancho de la espada
        int swordHeight = player.getHeight(); // Altura de la espada
        
        // Rectángulo de la espada
        int swordY = player.getY();
        
        // Rectángulo del enemigo
        int enemyX = zombie.getX();
        int enemyY = zombie.getY();
        int enemyWidth = zombie.getWidth();
        int enemyHeight = zombie.getHeight();
        
        // Comprobar colisión
        return (swordX < enemyX + enemyWidth && swordX + swordWidth > enemyX &&
                swordY < enemyY + enemyHeight && swordY + swordHeight > enemyY);
    }
	
	private boolean isCollidingWithEnemy2(Skeleton skeleton) {
        int swordX = (ladoEspada == 2) ? player.getX() + player.getWidth() : player.getX() - 20;
        int swordWidth = 40; // Ancho de la espada
        int swordHeight = player.getHeight(); // Altura de la espada
        
        // Rectángulo de la espada
        int swordY = player.getY();
        
        // Rectángulo del enemigo
        int enemyX = skeleton.getX();
        int enemyY = skeleton.getY();
        int enemyWidth = skeleton.getWidth();
        int enemyHeight = skeleton.getHeight();
        
        // Comprobar colisión
        return (swordX < enemyX + enemyWidth && swordX + swordWidth > enemyX &&
                swordY < enemyY + enemyHeight && swordY + swordHeight > enemyY);
    }
	
	
}
