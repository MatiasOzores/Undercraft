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

public class Menu_Screen extends JFrame implements Runnable, KeyListener{
	


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
    
    // presiona enter
    private boolean showPressEnter = true; // Controla la visibilidad del texto
    private int blinkCounter = 0; // Contador para manejar el parpadeo
    private final int BLINK_INTERVAL = 800; // Intervalo de parpadeo (ajusta según sea necesario)

    
	// ------------------------------------------------------------------------------
    
    public Menu_Screen() {
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
            showPressEnter = !showPressEnter; // Alterna la visibilidad del texto
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

	    // Dibuja titulo
	    g.setFont(tittleFont);
	    int tittleWidth = g.getFontMetrics().stringWidth(tittle); // variable para que quede centrado
	    g.setColor(Color.white);
	    g.drawString(tittle, ((Window.WIDTH / 2) - (tittleWidth / 2)) - 2, (Window.HEIGHT / 2) - 123);

	    // Dibuja "Presiona Enter" que titilea
	    if (showPressEnter) {
	        g.setFont(new Font("Minecraft", Font.PLAIN, 30));
	        String pressEnter = "Presiona Enter";
	        int pressEnterWidth = g.getFontMetrics().stringWidth(pressEnter); 
	        g.setColor(Color.white);
	        g.drawString(pressEnter, (Window.WIDTH / 2) - (pressEnterWidth / 2), (Window.HEIGHT / 2) + 50);
	    }
	    
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
	
	
	// Funciones --------------------------------------------------------------
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            startGame = true; 
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
