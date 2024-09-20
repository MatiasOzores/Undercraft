package main.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Menu_Controles extends JFrame implements Runnable, KeyListener{
	


	// -------------------------- Declaracion de variables --------------------------
	
	private static final long serialVersionUID = 1L; 
	private Canvas canvas;
    private Thread thread;
    private boolean running;
    private boolean startGame = false;

    private BufferStrategy bs;
    private Graphics g;
    private Image backgroundImage;

    private Font tittleFont = new Font("Minecraft", Font.PLAIN, 60);
    private String tittle = "Undercraft";
    private Font messageFont = new Font("Minecraft", Font.PLAIN, 30);
    private String controls1 = "3. Presiona la C para 'ATACAR'";
    private String controls2 = "2. Presiona la E para 'INTERACTUAR'";
    private String controls4 = "4. Salta con 'ESPACIO' o 'W'";    
    private String controls3 = "1. Muevete con 'A,D' o con las flechas";    
    
    // presiona enter
    private int blinkCounter = 0; // Contador para manejar el parpadeo
    private final int BLINK_INTERVAL = 800; // Intervalo de parpadeo (ajusta según sea necesario)

    
	// ------------------------------------------------------------------------------
    
    public Menu_Controles() {
        // seteamos los valores que necesitamos si o si para el frame
        setTitle("Undercraft");
        setSize(Window.WIDTH, Window.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);  

        // instanciamos canvas y sus dimensiones (con las mismas dimensiones de window)
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setMaximumSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setMinimumSize(new Dimension(Window.WIDTH, Window.HEIGHT));
        canvas.setFocusable(true);

        add(canvas);
        canvas.addKeyListener(this);
        
        setVisible(true);
        
        try {
            backgroundImage = ImageIO.read(getClass().getResource("MenuScreen.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        blinkCounter++;
        if (blinkCounter >= BLINK_INTERVAL) {
            blinkCounter = 0;
        }
    }

	
	public void draw() {
	    bs = canvas.getBufferStrategy();
	    if (bs == null) {
	        canvas.createBufferStrategy(3);
	        return;
	    }
	    g = bs.getDrawGraphics();

	    // ---------------------------------------------- Empieza el dibujo

	    // Dibuja fondo
	    g.drawImage(backgroundImage, 0, 0, Window.WIDTH, Window.HEIGHT, null);


	    // Dibuja un mensaje
	    g.setFont(messageFont);
	    int messageWidth = g.getFontMetrics().stringWidth(controls1); // variable para centrar
	    g.setColor(Color.yellow);
	    g.drawString(controls1, ((Window.WIDTH / 2) - (messageWidth / 2)), (Window.HEIGHT / 2) - 20);
	    
	    int messageWidth2 = g.getFontMetrics().stringWidth(controls2); // variable para centrar
	    g.setColor(Color.yellow);
	    g.drawString(controls2, ((Window.WIDTH / 2) - (messageWidth2 / 2)), (Window.HEIGHT / 2) - 100);

	    int messageWidth3 = g.getFontMetrics().stringWidth(controls2); // variable para centrar
	    g.setColor(Color.yellow);
	    g.drawString(controls3, ((Window.WIDTH / 2) - (messageWidth3 / 2)), (Window.HEIGHT / 2) - 180);
	    
	    int messageWidth4 = g.getFontMetrics().stringWidth(controls4); // variable para centrar
	    g.setColor(Color.yellow);
	    g.drawString(controls4, ((Window.WIDTH / 2) - (messageWidth4 / 2)), (Window.HEIGHT / 2) + 60);
	    
	    // Dibuja un bot�n
	    g.setColor(Color.white);
	    g.drawString("Enter para salir", WIDTH/2 + 362, 460);

	    
	    
	    
	    // ---------------------------------------------- Termina el dibujo

	    g.dispose();
	    bs.show();
	}

	
	
	@Override
	public void run() {
        while (running) {
            draw();
            update();
            if (startGame) {
                running = false;
                this.dispose(); // Cerrar el menú
                new Window().start(); // Iniciar el juego
            }
            
            /*
            if(startOptions) {
            	running = false;
            	this.dispose();
            	new Menu_Options().start();
            }
            
            */
        }
	}
	
	public void start() {
       thread = new Thread(this);
       thread.start();
       running = true;		
	}
	
	public void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }		
	}
	
    
    public static void openControles() {
        new Menu_Controles().start();
    }
	
	
	
	// Funciones --------------------------------------------------------------
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            running = false; // Detiene el ciclo del juego
            dispose(); // Cierra la ventana
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
