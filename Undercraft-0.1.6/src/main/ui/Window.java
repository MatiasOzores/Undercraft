package main.ui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.entities.Player;

public class Window extends JFrame implements Runnable, KeyListener {

	// -------------------------- Declaracion de variables --------------------------
	
	// general
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
	
	// para entidades
	
    private Player player;
    
    // para eventos de teclado

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean space = false;
    
    // Imagen de textura
    private Image tierraTextura;
    private Image cespedTextura;
    private Image fondo;
    private Image playerImageRight1;
    private Image playerImageRight2;
    private Image playerImageLeft1;
    private Image playerImageLeft2;
    private Image playerImageLeftStatic;
    private Image playerImageRightStatic;

    
    
	
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
		
	    // Añadir el KeyListener al Canvas
		
	    canvas.addKeyListener(this);
        
        // Carga de imagenes
        
        try {
            tierraTextura = ImageIO.read(getClass().getResource("tierra.png"));
            cespedTextura = ImageIO.read(getClass().getResource("cesped.png"));
            fondo = ImageIO.read(getClass().getResource("overworld_noche.png"));
            playerImageRight1 = ImageIO.read(getClass().getResource("SteveSpriteMoveRight1.png"));
            playerImageRight2 = ImageIO.read(getClass().getResource("SteveSpriteMoveRight2.png"));
            playerImageLeft1 = ImageIO.read(getClass().getResource("SteveSpriteMoveLeft1.png"));
            playerImageLeft2 = ImageIO.read(getClass().getResource("SteveSpriteMoveLeft2.png"));
            playerImageLeftStatic = ImageIO.read(getClass().getResource("SteveSpriteLeft.png"));
            playerImageRightStatic = ImageIO.read(getClass().getResource("SteveSpriteRight.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
		// Instanciamos entidades
		
        player = new Player(WIDTH / 2 - 50, ((Window.HEIGHT / 2) + 150) - 70, 64, 64, 5, Color.yellow, 
                playerImageRight1, playerImageRight2, playerImageLeft1, playerImageLeft2, playerImageLeftStatic, playerImageRightStatic);
        
	}
	
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {
		player.update(leftPressed, rightPressed, space, upPressed);
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
		
	    // Dibuja el fondo
	    if (fondo != null) {
	        g.drawImage(fondo, 0, 0, Window.WIDTH, Window.HEIGHT, null); // Ajusta las dimensiones según sea necesario
	    } else {
	        g.setColor(Color.gray);
	        g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);
	    }
		
        
        // Dibuja suelo (textura tierra)
        if (tierraTextura != null) {
            for (int x = 0; x < WIDTH; x += tierraTextura.getWidth(null)) {
                for (int y = (HEIGHT / 2) + 150; y < HEIGHT; y += tierraTextura.getHeight(null)) {
                    g.drawImage(tierraTextura, x, y, null);
                }
            }
        }
        
	     // Dibuja suelo (textura césped)
        if (cespedTextura != null) {
            for (int x = 0; x < WIDTH; x += cespedTextura.getWidth(null)) {
                g.drawImage(cespedTextura, x, 450, null); 
            }
        }      
        
		// Dibuja al jugador
        
        player.draw(g);
        
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
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }

        if (key == KeyEvent.VK_SPACE) {
            space = true;
        }
        
        if (key == KeyEvent.VK_UP) {
            upPressed = true;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }

        if (key == KeyEvent.VK_SPACE) {
            space = false;
        }
        
        if (key == KeyEvent.VK_UP) {
            upPressed = false;
        }
	}

}
