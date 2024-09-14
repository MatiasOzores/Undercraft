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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.entities.Espada;
import main.entities.Item;
import main.entities.Plataforma;
import main.entities.Player;
import main.entities.VidaExtra;
import main.entities.Zombie;

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
    private VidaExtra vida;
    private Espada espada;
    private Zombie zombies;
    
    
    // plataforma y estructuras
    private Plataforma casa;
    private Item palanca;
    
    
    // para eventos de teclado

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean space = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    private boolean wPressed = false;
    private boolean cPressed = false;
    private boolean ePressed = false;
    
    
    
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
    private Image corazonCompleto;
    private Image plataformaTextura;
    private Image imagenCasa;
    private Image zombieImage;
    // tipografias
    Font font = new Font("Minecraft", Font.PLAIN, 20);
    
    // Para el menu de pausa
    private Menu_Pausa menuPausa;
    
    // Para manejar los niveles 
	public int nivelSeleccionado = 1;
	public int frameSeleccionado = 1;
	// varaible para en el caso de que la palanca se active
	public static boolean palancaActivada = false;
	
	
	private List<Plataforma> plataformas;
	
	// para pasar los frames
	private int siguienteFrame = 0;
	
	// variable para mover plataforma
	private int movPlataforma = 200;
	private int estadoPlataforma = 0;
    
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
            corazonCompleto = ImageIO.read(getClass().getResource("CorazonCompleto.png"));
            plataformaTextura = ImageIO.read(getClass().getResource("roca.jpg"));
            imagenCasa = ImageIO.read(getClass().getResource("Mr_Undercraft.png"));
            zombieImage = ImageIO.read(getClass().getResource("ZombieSpriteRigth.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Inicializamos el menú de pausa
        menuPausa = new Menu_Pausa(this);
        
        
		// Instanciamos entidades
		
        // Instanciamos la lista de plataformas
        plataformas = new ArrayList<>();
        
        player = new Player(30, ((Window.HEIGHT / 2) + 150) - 70, 64, 64, 5, Color.yellow,
                playerImageRight1, playerImageRight2, playerImageLeft1, playerImageLeft2,
                playerImageLeftStatic, playerImageRightStatic, corazonCompleto, plataformas);
        
        // A�adimos plataformas a la lista
        // plataforma 1 inicialmente
        plataformas.add(new Plataforma(270, 390, 150, 30,plataformaTextura, player));
        
        // plataforma 2 inicialmente
        plataformas.add(new Plataforma(500, 330, 150, 30, plataformaTextura, player));
        
        // plataforma 3 inicialmente
        plataformas.add(new Plataforma(240, 260, 150, 30, plataformaTextura, player));
        
        // inicialmente esto es la estructura de la palanca
        plataformas.add(new Plataforma(0, 190, 150, 100, plataformaTextura, player));
        
        // inicialmente esto es la casa 
        plataformas.add(new Plataforma(WIDTH-200, ((Window.HEIGHT / 2) + 150) - 250, 200, 250, plataformaTextura, player)); 
        
        plataformas.add(new Plataforma(10000,10000, 150, 100, plataformaTextura, player)); 
        
        plataformas.add(new Plataforma(10000,10000, 100, 30, plataformaTextura, player)); 
        // Objeto de la vida
        // vida = new VidaExtra(200,300, player);
        
        // spawn enemigos 
        if(frameSeleccionado == 1) {
        	zombies = new Zombie(600, ((Window.HEIGHT / 2) + 150) - 70, player, zombieImage);
        	palanca = new Item(20,190-35,35,35,player);
        }
        
        

        
        
        // Objeto de la espada
        
        espada = new Espada(1,player);
   
	}
	
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {
		
		// para el menu de pause en el caso de que no sea visible
        if (!menuPausa.isVisible()) {
        	
        	player.update(leftPressed, rightPressed, space, upPressed,aPressed,dPressed,wPressed,cPressed);
        	// vida.update();
    		espada.update();
    		
    		
    	// Se manejan los frames y niveles ----------------------------------------------------------------------------------------------------------------------------------------------------	
    		
    		
    		if(nivelSeleccionado == 1) {
    			if(frameSeleccionado == 1) {
    				if(player.getX() >= WIDTH-220-player.getWidth() && palancaActivada) {
    					frameSeleccionado++;
    					System.out.println("A");
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
    				}
    				palanca.update(ePressed);
    				
    			}
    			
				if(frameSeleccionado == 2) {
					if(player.getX() >= 840 && player.getY() == 86) {
						frameSeleccionado++;
    					System.out.println("B");
    					player.x = WIDTH/2;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
					}
					
					
				}
				
				if(frameSeleccionado == 3) {
					if(player.getX() >= WIDTH - 150) {
						frameSeleccionado++;
    					System.out.println("C");
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
					}							
					
					zombies.update();
				}
				
				if(frameSeleccionado == 4) {
					if(player.getX() >= WIDTH - 150) {
						frameSeleccionado++;
    					System.out.println("D");
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
					}							
				}
				
				if(frameSeleccionado == 5) {
					if(player.getX() >= WIDTH - 150) {
						frameSeleccionado++;
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
    					System.out.println("NIVEL 1 TERMINADO");
					}							
				}

    			
	            for (Plataforma plataforma : plataformas) {
	                plataforma.update();
	            }
    			
    		}	
    	// Se manejan los frames y niveles ----------------------------------------------------------------------------------------------------------------------------------------------------
    			

    		
        }
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
        
	     // Dibuja suelo (textura cesped)
        if (cespedTextura != null) {
            for (int x = 0; x < WIDTH; x += cespedTextura.getWidth(null)) {
                g.drawImage(cespedTextura, x, 450, null); 
            }
        }      
        
        
        // Plataforma
        

		// Dibuja al jugador
        
        player.draw(g);
        
        
        // Dibuja la vida extra
        
        // vida.draw(g);
        espada.draw(g);
        
        // Dibuja el nivel actual
        g.setFont(font);
        g.setColor(Color.green);
        g.drawString("Nivel 1: Overworld", WIDTH-210, 50); 
        
        
        // Dibujar Menu_Pausa si está visible
        menuPausa.draw(g, WIDTH, HEIGHT);
        
        
        // Dibujar plataforma
        
        // Dibujo de Nivel 1 ---------------------------------------------
        
        
        if(nivelSeleccionado == 1) {
        	if(frameSeleccionado == 1) {
                palanca.draw(g);
        	}
        	
        	else if(frameSeleccionado == 2) {
        		plataformas.get(2).x = 520;
        		plataformas.get(2).y = 210;
        		
        		
        		plataformas.get(4).y = 150;
        		plataformas.get(4).height = 300;
        		plataformas.get(4).x = WIDTH - plataformas.get(4).width;	
        		
        		plataformas.get(3).x = 240;
        		plataformas.get(3).y = 260;
        		plataformas.get(3).width = 150;
        		plataformas.get(3).height = 30;
        		
        		plataformas.get(5).x = 0;
        		plataformas.get(5).y = 150;
        		
        	    plataformas.get(6).x = movPlataforma;
        	    
        	    plataformas.get(6).y = 150;
        	    
        	    
        	    // movimiento de la plataforma
        	    if(movPlataforma <= 350 && estadoPlataforma == 0) {
        	    	movPlataforma++;
        	    }
        	    
        	    else {
        	    	estadoPlataforma = 1;
        	    	movPlataforma--;
        	    	
        	    	if(movPlataforma == 200) {
        	    		estadoPlataforma = 0;
        	    	}
        	    }

        	}
        	
			else if(frameSeleccionado == 3) {
				
	       		plataformas.get(2).x = 10000;
        		plataformas.get(2).y = 10000;
        		
        		
        		plataformas.get(4).y = 10000;
        		plataformas.get(4).height = 300;
        		plataformas.get(4).x = 10000;	
        		
        		plataformas.get(3).x = 10000;
        		plataformas.get(3).y = 10000;
        		plataformas.get(3).width = 150;
        		plataformas.get(3).height = 30;
        		
        		plataformas.get(5).x = 10000;
        		plataformas.get(5).y = 10000;
        		
        	    plataformas.get(6).x = 10000;
        	    plataformas.get(6).y = 10000;
        	    
        	    plataformas.get(1).y = 10000;
        	    plataformas.get(1).y = 10000;
        	    
        	    plataformas.get(0).y = 10000;
        	    plataformas.get(0).y = 10000;
        	    
        	    
        	    // Spawn enemigos
        	    zombies.draw(g);
        	}
        	
			else if(frameSeleccionado == 4) {
        	    plataformas.get(0).x = 170;
        	    plataformas.get(0).y = 400;  
        	    plataformas.get(0).width = 70; 
        	    
        	    plataformas.get(1).x = 360;
        	    plataformas.get(1).y = 380;
        	    plataformas.get(1).width = 70;
        	    
        	    plataformas.get(2).x = 550;
        	    plataformas.get(2).y = 390;
        	    plataformas.get(2).width = 70;
        	    
        	}
        	
			else if(frameSeleccionado == 5) {
        		
        	}
        	
        	
            for (Plataforma plataforma : plataformas) {
                plataforma.draw(g);
            }
        }
        

        
        // Final Dibujo de Nivel 1 ---------------------------------------------
        
        
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
	
	 // Clase Window
    public void closeWindow() {
        running = false; // Detiene el ciclo del juego
        dispose(); // Cierra la ventana
        new Menu_Screen().start();
    }
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void keyPressed(KeyEvent e) {
        if (menuPausa.isVisible()) {
            menuPausa.keyPressed(e);
            return;
        }

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

        if (key == KeyEvent.VK_P) {
            menuPausa.showMenu();
        }
        
        if(key == KeyEvent.VK_A) {
        	aPressed = true;
        }
        
        if(key == KeyEvent.VK_W) {
        	wPressed = true;
        }
        
        if(key == KeyEvent.VK_D) {
        	dPressed = true;
        }
        
        if(key == KeyEvent.VK_C) {
        	cPressed = true;
        }
        
        if(key == KeyEvent.VK_E) {
        	ePressed = true;
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
        
        
        
        // para wasd
        
        if (key == KeyEvent.VK_W) {
            wPressed = false;
        }
        
        if (key == KeyEvent.VK_A) {
            aPressed = false;
        }
        
        if (key == KeyEvent.VK_D) {
            dPressed = false;
        }
        
        
        // para golpe
        
        if (key == KeyEvent.VK_C) {
            cPressed = false;
            Player.finalizarGolpe = 0;
        }
        
        // interaccion entre items y jugador
        if(key == KeyEvent.VK_E) {
        	ePressed = false;
        }
	}

}
