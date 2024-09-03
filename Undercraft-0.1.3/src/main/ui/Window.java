package main.ui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame implements Runnable, KeyListener {

	// -------------------------- Declaracion de variables --------------------------
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 950, HEIGHT = 600;
	private Canvas canvas;
	private Thread thread;
	private boolean running;
	
	// para dibujos/graficos
	private BufferStrategy bs;
	private Graphics g;
	
	// para los frames
	private final int FPS = 60;
	private double TARGETTIME = 1000000000/FPS;
	private double delta = 0;

	
	// ------------------------------------------------------------------------------
	
	public Window() {
		setTitle("Undercraft");
		setSize(WIDTH,HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		canvas = new Canvas();
		
		// Establecemos la resolucion del canvas, que basicamente va ser la dimension del jframe por eso ponemos las misma variables.
		canvas.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH,HEIGHT));
		canvas.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		canvas.setFocusable(true); // Esto te permite interactuar con el canvas, por ejemplo lo utilizaremos para los eventos del teclado (keylistener)
		
		add(canvas);
	}
	
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {
		
	}
	
	
	// funcion que dibujara todo el juego (cada segundo literalmente)
	private void draw() {
		bs = canvas.getBufferStrategy();
		
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		// ---------------------------------------------- Empieza el dibujo
		
        // Dibuja fondo
        g.setColor(Color.gray);
        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
		
		
		// ---------------------------------------------- Termina el dibujo
		
		g.dispose();
		bs.show();
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub	
	
		// -------------- Tiempo restringido a 60 FPS --------------
		long now = 0;
		long lastTime = System.nanoTime();
		long time = 0;
		
		while(running) {
			
			now = System.nanoTime();
			delta += (now - lastTime)/TARGETTIME;
			lastTime = now;
			
			if(delta >= 1) {
				update();
				draw();
				delta --;
			}
			
			if(time >= 1000000000) {
				time = 0;
	
			}

		}
		
		// ----------------------------------------------------------
		
		
		stop();
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	private void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Funciones ---------------------------------------------------------
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
