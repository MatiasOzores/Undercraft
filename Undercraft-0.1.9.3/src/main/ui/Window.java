package main.ui;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

import main.entities.Creeper;
import main.entities.Espada;
import main.entities.Item;
import main.entities.Plataforma;
import main.entities.Player;
import main.entities.Skeleton;
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
    private List<Zombie> zombies;
    private List<Creeper> creepers;
    private List<Skeleton> skeletons;
    private Item itemEspada;
    
    // plataforma y estructuras
    private Plataforma casa;
    private Item palanca;
    private Item mechero;
    
    
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

	// para controlar la cantidad de enemigos restantes
	public int enemigosRestantes1; // enemigos restantes de la emboscada 1
	
	
	private List<Plataforma> plataformas;
	
	// para pasar los frames
	private int siguienteFrame = 0;
	
	// variable para mover plataforma
	private int movPlataforma = 200;
	private int estadoPlataforma = 0;
	
	// para emboscada y aparicion de enemigos
	private boolean mostrarMensajeEmboscada = false;
	private boolean enemigosAparecidos = false;
	private boolean empezarEmboscada = false;
	private Timer emboscadaTimer, empezarEmboscadaTimer;
    
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
        zombies = new ArrayList<>();
        creepers = new ArrayList<>();
        skeletons = new ArrayList<>();        
        
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

        zombies.add(new Zombie(-20, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));  // Asume que tienes una variable zombieImage
        zombies.add(new Zombie(WIDTH+20, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+200, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+300, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+400, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+500, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+550, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+570, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-10, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-25, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-100, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-150, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-300, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-500, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));
        zombies.add(new Zombie(-700, ((Window.HEIGHT / 2) + 150) - 64, player, zombieImage));

        
        creepers.add(new Creeper(WIDTH+100, ((Window.HEIGHT / 2) + 150) - 64, player));
        creepers.add(new Creeper(-1000, ((Window.HEIGHT / 2) + 150) - 64, player));
        skeletons.add(new Skeleton(-500, ((Window.HEIGHT / 2) + 150) - 64, player));
        skeletons.add(new Skeleton(WIDTH+100, ((Window.HEIGHT / 2) + 150) - 64, player));
    	palanca = new Item(20,190-35,35,35,player);
    	mechero = new Item(395,340,35,35,player);
    	itemEspada = new Item(25,115,55,35,player);  
      
        
  
        

        
        

        
        
        // Objeto de la espada
        
        espada = new Espada(1,player, zombies);
   
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
    					palancaActivada = false;
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
    				}
    				palanca.update(ePressed);
    				
    			}
    			
				if(frameSeleccionado == 2) {
					if(player.getX() >= 840 && player.getY() == 86 && palancaActivada) {
						frameSeleccionado++;
    					player.x = WIDTH/2;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
					}
					
					itemEspada.update(ePressed);
				}
				
				if(frameSeleccionado == 3) {

														
					
					if(enemigosAparecidos) {
				        for (Zombie zombie : zombies) {
				            zombie.update();
				        }
				        
				        /*
				        for (Creeper creeper: creepers) {
				            creeper.update();
				        }
				        
				        for (Skeleton skeleton: skeletons) {
				        	skeleton.update();
				        }
				        
				        */
					}
					
					for (int i = 0; i < zombies.size(); i++) {
					    Zombie enemigo = zombies.get(i);
					    
					    // Si el zombie está muerto, lo removemos de la lista
					    if (enemigo.isDead()) {
					        zombies.remove(i);
					        i--; // Decrementar el índice para no saltar ningún elemento
					    }
					}

					enemigosRestantes1 = zombies.size();

					// para pasar al sigte frame
					
    				if(player.getX() >= WIDTH-player.getWidth()-30 && enemigosRestantes1 == 0) {
    					frameSeleccionado++;
    					palancaActivada = false;
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
    				}
				}
				
				if(frameSeleccionado == 4) {
					
			        mechero.update(ePressed);
					
			        // ultimo frame del nivel 1 (a desarrollar nivel 2)
					if(player.getX() >= WIDTH-150-60 && palancaActivada) {
    					System.out.println("Nivel Terminado");
    					nivelSeleccionado++;
    					frameSeleccionado = 1;
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

        		itemEspada.draw(g);
        		
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
        	
        	else if (frameSeleccionado == 3) {
        	    
        	    plataformas.get(2).x = 10000;
        	    plataformas.get(2).y = 10000;
        	    
        	    // Elimina plataformas
        	    plataformas.get(4).y = 10000;
        	    plataformas.get(4).x = 10000;
        	    
        	    plataformas.get(3).x = 10000;
        	    plataformas.get(3).y = 10000;
        	    
        	    plataformas.get(5).x = 10000;
        	    plataformas.get(5).y = 10000;
        	    
        	    plataformas.get(6).x = 10000;
        	    plataformas.get(6).y = 10000;
        	    
        	    plataformas.get(1).y = 10000;
        	    plataformas.get(0).y = 10000;

    	        g.setColor(Color.RED);
    	        g.setFont(new Font("Minecraft", Font.BOLD, 20)); // Ajusta el estilo del texto
    	        g.drawString("Restantes: " + enemigosRestantes1, WIDTH-210, 90);
        	    
        	    empezarEmboscadaTimer = new Timer(1000, new ActionListener() {
        	        @Override
        	        public void actionPerformed(ActionEvent e) {
        	            empezarEmboscada = true;       
        	            empezarEmboscadaTimer.stop();            
        	        }
        	    });
        	    empezarEmboscadaTimer.start(); 
        	    
        
        	    // para manejar el tiempo que hay entre que aparece el jugador en el 3er frame hasta que aparezca el mensaje de emboscada
        	    if(empezarEmboscada) {
	        	    // Inicia un temporizador para esperar 3 segundos antes de que aparezcan los enemigos
	        	    emboscadaTimer = new Timer(3000, new ActionListener() {
	        	        @Override
	        	        public void actionPerformed(ActionEvent e) {
	        	            enemigosAparecidos = true;        // Los enemigos pueden aparecer
	        	            emboscadaTimer.stop();            // Detiene el temporizador
	        	        }
	        	    });
	        	    emboscadaTimer.start(); // Inicia el temporizador
	        	
	        	    if (!enemigosAparecidos) {
	        	        g.setColor(Color.RED);
	        	        g.setFont(new Font("Minecraft", Font.BOLD, 50)); // Ajusta el estilo del texto
	        	        g.drawString("Emboscada", Window.WIDTH / 2 - 150, Window.HEIGHT / 2);
	        	    }
	        	    
	        	    // Dibuja a los enemigos si el temporizador ha terminado
	        	    if (enemigosAparecidos) {
	        	        for (Zombie zombie : zombies) {
	        	            zombie.draw(g);
	        	        }
	        	        
	        	        /*
	        	        
				        for (Creeper creeper: creepers) {
				            creeper.draw(g);
				        }
				        
				        for (Skeleton skeleton: skeletons) {
				        	skeleton.draw(g);
				        }
				        
				        */
	        	    }        	    	
        	    }

        	}
        	
			else if(frameSeleccionado == 4) {
				
				// actualiza mechero
                mechero.draw(g);
				
        	    plataformas.get(0).x = 170;
        	    plataformas.get(0).y = 395;  
        	    plataformas.get(0).width = 70; 
        	    
        	    plataformas.get(1).x = 360;
        	    plataformas.get(1).y = 375;
        	    plataformas.get(1).width = 70;
        	    
        	    plataformas.get(2).x = 550;
        	    plataformas.get(2).y = 385;
        	    plataformas.get(2).width = 70;
        	    
        	    plataformas.get(3).x = WIDTH-150;
        	    plataformas.get(3).y = ((Window.HEIGHT / 2) - 10);
        	    plataformas.get(3).width = 60;
        	    plataformas.get(3).height = 130;
        	    
        	    
        	   
        	    plataformas.get(4).x = -10000;
        	    plataformas.get(4).y = -10000;
        	    plataformas.get(4).width = 40;
        	    plataformas.get(4).height = 30;
        	   
        	    plataformas.get(5).x = -10000;
        	    plataformas.get(5).y = -10000;
        	    
        	    plataformas.get(6).x = -10000;
        	    plataformas.get(6).y = -10000;

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
        
        if (key == KeyEvent.VK_ESCAPE) {
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
