package main.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.entities.Player;

public class Menu_Pausa {
	private Window window;
	private Player player;
    private boolean visible;
    private Font regularFont = new Font("Minecraft", Font.PLAIN, 40);
    private String salir = "Salir";
    private String controles = "Controles";
    private String reanudar = "Reanudar";
    private String[] opciones = { reanudar,controles,salir };
    private int currentSelection = 0;

    public Menu_Pausa(Window window, Player player) {
        this.visible = false;
        this.window = window;
    }

    public void showMenu() {
        visible = true;
    }

    public void hideMenu() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void draw(Graphics g, int width, int height) {
        if (!visible) return;

        // Fondo semi-transparente
        g.setColor(new Color(0, 0, 0, 150)); // Color negro con opacidad
        g.fillRect(0, 0, width, height);

        // Dibujar opciones centradas
        g.setFont(regularFont);
        FontMetrics fm = g.getFontMetrics(regularFont);
        int yOffset = (height / 2) - 80; // Ajusta la altura según necesidades
        for (int i = 0; i < opciones.length; i++) {
            int textWidth = fm.stringWidth(opciones[i]);
            int xPosition = (width - textWidth) / 2; // Centra el texto horizontalmente

            if (i == currentSelection) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.white);
            }
            g.drawString(opciones[i], xPosition, yOffset + (i * 60));
        }
    }

    public void keyPressed(KeyEvent e) {
        if (!visible) return;

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            currentSelection = (currentSelection - 1 + opciones.length) % opciones.length;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            currentSelection = (currentSelection + 1) % opciones.length;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (currentSelection == 0) {
                hideMenu();
            }
            
            else if(currentSelection == 1) {
            	showMenu();
            	Menu_Controles.openControles();
            }
            
            else if (currentSelection == 2) {
                hideMenu();
		        Window.frameSeleccionado = 1;
		        Window.nivelSeleccionado = 1;
		        Window.palancaActivada = false;
		        Window.palancaActivada2 = false;
		        Player.vida = 10;
                window.closeWindow(); // Cierra la ventana cuando se selecciona "Salir"
            }
        }
    }


}