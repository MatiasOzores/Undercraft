import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame implements Runnable, KeyListener {

	// Declaracion de variables
	public static final int WIDTH = 950, HEIGHT = 600;
	private Canvas canvas;
	private Thread thread;
	private boolean running;
	
	private BufferStrategy bs;
	private Graphics g;
	
	
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
	
	public static void main(String[] args) {
		new Window().start();
	}
	
	
	private void update() {
		
	}
	
	private void draw() {
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub	
	
		while(running) {
			update();
			draw();
		}
		
		
		
		stop();
	}
	
	private void start() {
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

}
