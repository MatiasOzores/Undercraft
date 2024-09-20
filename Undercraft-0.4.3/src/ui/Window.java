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
import main.entities.Lava;
import main.entities.Plataforma;
import main.entities.Player;
import main.entities.Skeleton;
import main.entities.VidaExtra;
import main.entities.Zombie;

public class Window extends JFrame implements Runnable, KeyListener {

	// -------------------------- Declaracion de variables --------------------------
	
	private int aux = 1;
	
	
	// general
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 942, HEIGHT = 582;
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
    private Item portalActivacion;
    private Item portalActivacion2;
    private Item cofreNether;
    
    // lava
    private List<Lava> lava;
    
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
    
    // para puerta
    public boolean puertaAbierta = false;
    
    
    
    // Imagen de textura
    private Image tierraTextura;
    private Image cespedTextura;
    private Image fondoN1F1, fondoN1F2, fondoN1F3, fondoN1F4, fondoN2F1, fondoN2F2, fondoN2F3, fondoN2F4, fondoN2F5, fondoN3F1;
    private Image playerImageRight1;
    private Image playerImageRight2;
    private Image playerImageLeft1;
    private Image playerImageLeft2;
    private Image playerImageLeftStatic;
    private Image playerImageRightStatic;
    private Image corazonCompleto;
    private Image medioCorazon;
    private Image plataformaTextura;
    private Image imagenCasa;
    private Image zombieImage;
    private Image palancaDesactivada, palancaActivadaImg, transparente;
    private Image roca,roble, lanaAmarilla, lanaCeleste, netherSuperficie,netherrack,ladrilloNether;
    
	// para objetos
    private Image puertaAbiertaImg, puertaCerradaImg;
    private Image cofreAbierto, cofreCerrado;
    private Image mecheroImg;
    private Image portalNetherAct, portalNetherDes;
    
    // tipografias
    Font font = new Font("Minecraft", Font.PLAIN, 20);
    
    // Para el menu de pausa
    private Menu_Pausa menuPausa;
    
    // Para manejar los niveles 
	public static int nivelSeleccionado = 2;
	public static int frameSeleccionado = 4;
	
	// varaible para en el caso de que la palanca se active
	public static boolean palancaActivada = false;
	public static boolean palancaActivada2 = false;

	// para controlar la cantidad de enemigos restantes
	public int enemigosRestantes1; // enemigos restantes de la emboscada 1
	public int enemigosRestantes2; // enemigos restantes de la emboscada 2
	
	private List<Plataforma> plataformas;
	
	// para pasar los frames
	private int siguienteFrame = 0;
	
	// variable para mover plataforma
	private int movPlataforma = 0, movPlataforma2 = 0;
	private int estadoPlataforma = 0, estadoPlataforma2 = 0;
	
	// para emboscada y aparicion de enemigos
	private boolean mostrarMensajeEmboscada = false;
	private boolean enemigosAparecidos = false;
	private boolean empezarEmboscada = false, empezarEmboscada2 = false;
	private Timer emboscadaTimer, empezarEmboscadaTimer;
	private Timer emboscadaTimer2, empezarEmboscadaTimer2;
    
	// para morir
	
	private boolean gameOverTimerStarted = false;

	
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
        	
        	// para suelos
            tierraTextura = ImageIO.read(getClass().getResource("BloqueTierra.png"));
            cespedTextura = ImageIO.read(getClass().getResource("BloqueCesped.png"));
            
            // para los fondos
            fondoN1F1 = ImageIO.read(getClass().getResource("fondoOverworldFrame1.png"));
            fondoN1F2 = ImageIO.read(getClass().getResource("fondoOverworldFrame2.png"));
            fondoN1F3 = ImageIO.read(getClass().getResource("fondoOverworldFrame3.png"));    
            fondoN1F4 = ImageIO.read(getClass().getResource("fondoOverworldFrame4.png"));  
            
            fondoN2F1 = ImageIO.read(getClass().getResource("fondoNetherFrame1.png"));  
            fondoN2F2 = ImageIO.read(getClass().getResource("fondoNetherFrame2.png")); 
            fondoN2F3 = ImageIO.read(getClass().getResource("fondoNetherFrame3.png")); 
            fondoN2F4 = ImageIO.read(getClass().getResource("fondoNetherFrame4.png")); 
            fondoN2F5 = ImageIO.read(getClass().getResource("fondoNetherFrame5.png")); 
            
            fondoN3F1 = ImageIO.read(getClass().getResource("fondoOverworldJunglaFrame1.png"));             
            
            playerImageRight1 = ImageIO.read(getClass().getResource("SteveSpriteMoveRight1.png"));
            playerImageRight2 = ImageIO.read(getClass().getResource("SteveSpriteMoveRight2.png"));
            playerImageLeft1 = ImageIO.read(getClass().getResource("SteveSpriteMoveLeft1.png"));
            playerImageLeft2 = ImageIO.read(getClass().getResource("SteveSpriteMoveLeft2.png"));
            playerImageLeftStatic = ImageIO.read(getClass().getResource("SteveSpriteLeft.png"));
            playerImageRightStatic = ImageIO.read(getClass().getResource("SteveSpriteRight.png"));
            corazonCompleto = ImageIO.read(getClass().getResource("CorazonCompleto.png"));
            medioCorazon = ImageIO.read(getClass().getResource("MedioCorazon.png"));
            plataformaTextura = ImageIO.read(getClass().getResource("BloqueCesped.png"));
            imagenCasa = ImageIO.read(getClass().getResource("Mr_Undercraft.png"));
            zombieImage = ImageIO.read(getClass().getResource("ZombieSpriteRigth.png"));

            transparente = ImageIO.read(getClass().getResource("transparente.png"));
            
            // objetos
            puertaAbiertaImg = ImageIO.read(getClass().getResource("PuertaAbierta.png"));
            puertaCerradaImg = ImageIO.read(getClass().getResource("PuertaCerrada.png"));
            palancaDesactivada = ImageIO.read(getClass().getResource("PalancaLeft.png"));
            palancaActivadaImg = ImageIO.read(getClass().getResource("PalancaRight.png"));
            cofreCerrado = ImageIO.read(getClass().getResource("Cofre.png"));
            cofreAbierto = ImageIO.read(getClass().getResource("CofreAbierto.png"));
            portalNetherAct = ImageIO.read(getClass().getResource("PortalNetherActivo.png"));
            portalNetherDes = ImageIO.read(getClass().getResource("PortalNether.png")); 
            
            // materiales
            roble = ImageIO.read(getClass().getResource("BloqueMadera.png"));
            roca = ImageIO.read(getClass().getResource("BloqueRoca.png"));
            lanaCeleste = ImageIO.read(getClass().getResource("BloqueLanaCeleste.png"));
            lanaAmarilla = ImageIO.read(getClass().getResource("BloqueLanaAmarillo.png"));
            netherSuperficie = ImageIO.read(getClass().getResource("BloqueNecelio.png"));
            mecheroImg = ImageIO.read(getClass().getResource("Mechero.png"));           
            netherrack = ImageIO.read(getClass().getResource("BloqueNetherrack.jpg")); 
            ladrilloNether = ImageIO.read(getClass().getResource("BloqueLadrillosNether.png")); 
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Inicializamos el menú de pausa
        menuPausa = new Menu_Pausa(this, player);
        
        
		// Instanciamos entidades
        zombies = new ArrayList<>();
        creepers = new ArrayList<>();
        skeletons = new ArrayList<>();        
        lava = new ArrayList<>();
        // Instanciamos la lava
        
        lava.add(new Lava(-10000,-10000,32,32));
        
        
        // Instanciamos la lista de plataformas
        plataformas = new ArrayList<>();
        
        player = new Player(30, 0- 64, 34, 34, 5, Color.yellow,
                playerImageRight1, playerImageRight2, playerImageLeft1, playerImageLeft2,
                playerImageLeftStatic, playerImageRightStatic, corazonCompleto, medioCorazon, plataformas);

        
        // P1
        plataformas.add(new Plataforma(448, 288, 160, 32, plataformaTextura, player));
        
        // P2
        plataformas.add(new Plataforma(224, 352, 160, 32, plataformaTextura, player));
        
        // P3
        plataformas.add(new Plataforma(0, 160, 100, 32, plataformaTextura, player));
        
        // P4
        plataformas.add(new Plataforma(WIDTH-70, 0 - 90, 40, HEIGHT, transparente, player)); 
        
        // P5
        plataformas.add(new Plataforma(10000,10000, 150, 100, plataformaTextura, player)); 
        
        // P6
        plataformas.add(new Plataforma(192,225, 160, 32, plataformaTextura, player)); 
        
        // P7
        plataformas.add(new Plataforma(832,320, 32, 32, roca, player));
        
        
        // Objeto de la vida
        // vida = new VidaExtra(200,300, player);
        
        // spawn enemigos 

        zombies.add(new Zombie(-20, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));  // Asume que tienes una variable zombieImage
        zombies.add(new Zombie(WIDTH+20, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+200, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+300, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+400, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+500, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+550, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(WIDTH+570, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-10, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-25, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-100, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-150, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-300, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-500, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));
        zombies.add(new Zombie(-700, ((Window.HEIGHT / 2) + 157) - 64, player, zombieImage));

        
        creepers.add(new Creeper(WIDTH+100, ((Window.HEIGHT / 2) + 150) - 64, player));
        creepers.add(new Creeper(-1000, ((Window.HEIGHT / 2) + 150) - 64, player));
        skeletons.add(new Skeleton(WIDTH+100, 158-30, player));
        skeletons.add(new Skeleton(WIDTH+250, 158-30, player));
        skeletons.add(new Skeleton(WIDTH+400, 158-30, player));
        skeletons.add(new Skeleton(-10, 158-30, player));
        skeletons.add(new Skeleton(-300, 158-30, player));
        skeletons.add(new Skeleton(-400, 158-30, player));
        skeletons.add(new Skeleton(-500, 158-30, player));
        
        
    	palanca = new Item(20,160-22,38,22,player,palancaDesactivada,palancaActivadaImg);
    	mechero = new Item(385,280,30,30,player,mecheroImg,null);
    	itemEspada = new Item(32*0,32*4,32,32,player,cofreCerrado,cofreAbierto); 
    	portalActivacion = new Item(32*25,32*9,128,160,player,portalNetherDes,portalNetherAct); 
    	portalActivacion2 = new Item(32*25,32*9,128,160,player,portalNetherAct,portalNetherAct); 
    	cofreNether = new Item(32*18,32*3,32,32,player,cofreCerrado,cofreAbierto);
      
        
  
        

        
        

        
        
        // Objeto de la espada
        
        espada = new Espada(1,player, zombies, skeletons);
   
	}
	
	
	// funcion que actualizara todo el juego (cada segundo literalmente)
	private void update() {
		// en el caso de que muera 
	    if (player.vida == 0 && !gameOverTimerStarted) {
	        gameOverTimerStarted = true;

	        // Pausar el juego por 3 segundos
	        Timer timer = new javax.swing.Timer(1000, e -> {
	            // Una vez que pasen los 3 segundos, cierra la ventana y abre el menú
	            ((Timer)e.getSource()).stop(); // Detiene el temporizador
		        running = false; 
	            closeGameAndOpenMenu();
		        frameSeleccionado = 1;
		        nivelSeleccionado = 1;
		        palancaActivada = false;
		        palancaActivada2 = false;
		        player.vida = 10;
	        });

	        timer.setRepeats(false); // Solo debe ejecutarse una vez
	        timer.start();
	        


	    }
		
		// para el menu de pause en el caso de que no sea visible
        if (!menuPausa.isVisible()) {
        	
        	player.update(leftPressed, rightPressed, space, upPressed,aPressed,dPressed,wPressed,cPressed,lava);
        	// vida.update();
    		espada.update();
    		
    		
    	// Se manejan los frames y niveles ----------------------------------------------------------------------------------------------------------------------------------------------------	
    		
    		
    		if(nivelSeleccionado == 1) {
    			
    			// PARA EL PRIMER FRAME
    			if(frameSeleccionado == 1) {
    				
    				// para el efecto de la puerta
    				if(palancaActivada) {
    					puertaAbierta = true;
    				}
    				// para avanzar de frame
    				if(player.getX() >= WIDTH-player.getWidth()-75 && palancaActivada) {
    					frameSeleccionado++;
    					palancaActivada = false;
    					player.x = 30;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
    				}
    				palanca.update(ePressed);
    				
    			}
    			
    			// PARA EL SEGUNDO FRAME
				if(frameSeleccionado == 2) {
					if(player.getX() >= 850 && player.getY() <= 160 && palancaActivada) {
						frameSeleccionado++;
    					player.x = WIDTH/2;
    					player.y = ((Window.HEIGHT / 2) + 150) - 70;
					}
					
					itemEspada.update(ePressed);
				}
				
				if(frameSeleccionado == 3) {

														
					
			        for (Zombie zombie : zombies) {
			            zombie.update();
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
					
					// lava
					lava.get(0).update(player);
		

					if(!palancaActivada) {
				        mechero.update(ePressed);						
					}
					
					

			        
			        if(palancaActivada) {
			        
			        	
		                portalActivacion.update(ePressed);	
		                if(portalActivacion.isEPressed && aux == 0) {
		                	palancaActivada2 = true;
		                }
		                
		                if(!ePressed) {
		                	aux = 0;
		                }
		                
			        }
			

	                
			        // ultimo frame del nivel 1 (a desarrollar nivel 2)
					if(player.getX() >= 830 && palancaActivada && palancaActivada2) {
    					nivelSeleccionado++;
    					frameSeleccionado = 1;
    					player.x = 30;
					}							
				}

    		}	
    		
            // En el caso de que el nivel sea 2

    		if(nivelSeleccionado == 2) {
    			if(frameSeleccionado == 1) {
    				if(player.getX() >= 850 && player.getY() <= 62) {	
    					frameSeleccionado++;
    					player.x = 30;
    					player.y = 62;
					}
            	    
    			}
    			
    			if(frameSeleccionado == 2) {
					lava.get(0).update(player);
    				
    				if(player.getX() >= 835 && player.getY() <= 158) {
    					frameSeleccionado++;
    					player.x = WIDTH/2;
    					player.y = 0;
					}
    				
    			}
    			
    			if(frameSeleccionado == 3) {
					        
			        for (Skeleton skeleton: skeletons) {
			        	skeleton.update();
			        }
			        
			        enemigosRestantes2 = skeletons.size();
				        
					
					
					for (int i = 0; i < skeletons.size(); i++) {
					    Skeleton enemigo = skeletons.get(i);
					    
					    // Si el zombie está muerto, lo removemos de la lista
					    if (enemigo.isDead()) {
					        skeletons.remove(i);
					        i--; // Decrementar el índice para no saltar ningún elemento
					    }
					}

					enemigosRestantes2 = skeletons.size();
					
					if(enemigosRestantes2 == 0 && player.x >= 870 && player.y <= 158) {
						frameSeleccionado++;
						player.x = 30;
						palancaActivada = false;
					}
    				
    			}
    			
    			if(frameSeleccionado == 4) {
					lava.get(0).update(player);
    				cofreNether.update(ePressed);
    				
    				if(palancaActivada && player.x >= 870) {
    					frameSeleccionado++;
    					player.x = 30;
    					palancaActivada = false;
    					
    				}
    			}
    			
    			if(frameSeleccionado == 5) {
					lava.get(0).update(player);   				
	                portalActivacion.update(ePressed);	
	                
	                if(player.x >= 830) {
	                	nivelSeleccionado++;
	                	frameSeleccionado = 1;
	                	player.x = 30;
	                }
    			}
    		}   		
    		
    		if(nivelSeleccionado == 3) {
    			if(frameSeleccionado == 1) {
    				
    			}
    		}
    		
    		
            for (Plataforma plataforma : plataformas) {
                plataforma.update();
            }
    		
    		// Se manejan los frames y niveles ----------------------------------------------------------------------------------------------------------------------------------------------------
    			    		
        }
	}
	
	
	// funcion que dibujara todo el juego (cada segundo literalmente)
	private void draw() {
		if (!running) return;
		
		bs = canvas.getBufferStrategy();
		
		if(bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		// ---------------------------------------------- Empieza el dibujo
		
	    // Dibuja el fondoN1F1
		if(nivelSeleccionado == 1) {
			if(frameSeleccionado == 1) {
			    if (fondoN1F1 != null) {
			        g.drawImage(fondoN1F1, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			        g.drawImage(roble, 832, 352, 32, 32, null);
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
			
			if(frameSeleccionado == 2) {
				
			    if (fondoN1F2 != null) {
			        g.drawImage(fondoN1F2, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			        g.drawImage(roble, 832, 352, 32, 32, null);
			        
			        
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
			
			if(frameSeleccionado == 3) {
				
			    if (fondoN1F3 != null) {
			        g.drawImage(fondoN1F3, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			        
			        
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
			
			if(frameSeleccionado == 4) {
				
			    if (fondoN1F4 != null) {
			        g.drawImage(fondoN1F4, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			        
			        
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
		}
		
		else if(nivelSeleccionado == 2) {
			if(frameSeleccionado == 1) {
			    if (fondoN2F1 != null) {
			        g.drawImage(fondoN2F1, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
			
			if(frameSeleccionado == 2) {
			    if (fondoN2F2 != null) {
			        g.drawImage(fondoN2F2, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}	
			
			if(frameSeleccionado == 3) {
			    if (fondoN2F3 != null) {
			        g.drawImage(fondoN2F3, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}
			
			if(frameSeleccionado == 4) {
			    if (fondoN2F4 != null) {
			        g.drawImage(fondoN2F4, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}	
			
			if(frameSeleccionado == 5) {
			    if (fondoN2F5 != null) {
			        g.drawImage(fondoN2F5, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
			    } else {
			        g.setColor(Color.gray);
			        g.fillRect(0, 0, 500, 500);
			    }				
			}	
		}
		
		else if(nivelSeleccionado == 3) {
		    if (fondoN3F1 != null) {
		        g.drawImage(fondoN3F1, 0, 0, 928, 544, null); // Ajusta las dimensiones según sea necesario
		    } else {
		        g.setColor(Color.gray);
		        g.fillRect(0, 0, 500, 500);
		    }			
		}

		
        
	    // Dibuja la línea de césped (32x32 px)
	    if (cespedTextura != null && nivelSeleccionado == 1) {
	    	if((nivelSeleccionado == 1) && frameSeleccionado != 4 ) {
		        for (int x = 0; x < WIDTH; x += 32) { // Asegúrate que WIDTH sea divisible por 32
		            g.drawImage(cespedTextura, x, 448, 32, 32, null); // Primera fila de césped
		        }
	    	}
	    	
	    	else {
		        for (int x = 0; x < 32*5; x += 32) { // Asegúrate que WIDTH sea divisible por 32
		            g.drawImage(cespedTextura, x, 448, 32, 32, null); // Primera fila de césped
		        }	
		        
		        for (int x = 32*22; x < 32*28; x += 32) { // Asegúrate que WIDTH sea divisible por 32
		            g.drawImage(cespedTextura, x, 448, 32, 32, null); // Primera fila de césped
		        }
	    	}

	    }

	    // Dibuja las dos líneas de tierra (32x32 px)
	    if (tierraTextura != null && nivelSeleccionado == 1) {
	    	if(nivelSeleccionado == 1 && frameSeleccionado != 4 ) {
		        for (int x = 0; x < WIDTH; x += 32) {
		            for (int y = 480; y <= 512; y += 32) { // Dos filas debajo de la línea de césped
		                g.drawImage(tierraTextura, x, y, 32, 32, null);
		            }
		        }	    		
	    	}
	    }
   
        
        
        // Plataforma
        
        
        
        // Dibuja la vida extra
        
        // vida.draw(g);
        espada.draw(g);
        
        // Dibuja el nivel actual
        g.setFont(font);
        g.setColor(Color.green);
        if(nivelSeleccionado == 1) {
            g.drawString("Nivel 1: Overworld", WIDTH-210, 50);         	
        }
        
        else if(nivelSeleccionado == 2) {
            g.drawString("Nivel 2: Nether", WIDTH-210, 50);          	
        }

                
        
        // Dibujo de Nivel 1 ---------------------------------------------
        
        
        if(nivelSeleccionado == 1) {
        	if(frameSeleccionado == 1) {
                palanca.draw(g);
                
                if (puertaAbierta) {
                    // Dibujar imagen de la puerta abierta
                    g.drawImage(puertaAbiertaImg, 832, 384, 32, 64, null);
                } else {
                    // Dibujar imagen de la puerta cerrada
                    g.drawImage(puertaCerradaImg, 832, 384, 32, 64, null);
                }
                
                
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                }
        	}
        	
        	else if(frameSeleccionado == 2) {

        		// Aca el objetivo ahora es agarrar esta espada, en el anterior frame fue la palanca
        		itemEspada.draw(g);
        		
        		plataformas.get(0).width = 160;
        		plataformas.get(0).height = 32;
        		plataformas.get(0).x = 32*6;
        		plataformas.get(0).y = 32*8;
        		plataformas.get(0).textura = lanaCeleste;
        		
        		plataformas.get(1).width = 160;
        		plataformas.get(1).height = 32;
        		plataformas.get(1).x = 32*15;
        		plataformas.get(1).y = 32*7;
        		plataformas.get(1).textura = lanaAmarilla;
        		
        		plataformas.get(2).width = 32*4;
        		plataformas.get(2).height = 32;
        		plataformas.get(2).x = 32*14;
        		plataformas.get(2).y = 32*10;
        		plataformas.get(2).textura = roca;
        		
        		plataformas.get(3).width = 32*4;
        		plataformas.get(3).height = 32;
        		plataformas.get(3).x = 32*14;
        		plataformas.get(3).y = 32*10;
        		plataformas.get(3).textura = lanaAmarilla;
        		
        		plataformas.get(4).width = 32*5;
        		plataformas.get(4).height = 32;
        		plataformas.get(4).x = 32*7;
        		plataformas.get(4).y = 32*12;
        		plataformas.get(4).textura = lanaCeleste;
        		        		
        		// plataforma techo izquierda
        		plataformas.get(5).width = 32*3;
        		plataformas.get(5).height = 32;
        		plataformas.get(5).x = 32*0;
        		plataformas.get(5).y = 32*5;
        		plataformas.get(5).textura = roble;

    
        		// plataforma techo derecha
        		plataformas.get(6).width = 32*5;
        		plataformas.get(6).height = 32;
        		plataformas.get(6).x = 32*24;
        		plataformas.get(6).y = 32*5;
        		plataformas.get(6).textura = roble;


        		
        	    // movimiento de la plataforma
	            /*
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
        	    */
        		
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                }
                
                // dibujo extra
                
                // parte izquierda del techo
                g.drawImage(roca, 32*2, 32*5, 32, 32, null);
                
                // derecha
                g.drawImage(roca, 32*24, 32*5, 32, 32, null);
                // derecha
                g.drawImage(roca, 32*28, 32*5, 32, 32, null);
                
        	}
        	
        	else if (frameSeleccionado == 3) {
        	    
        	    plataformas.get(6).x = -10000;
        	    plataformas.get(6).y = -10000;
        		
        	    plataformas.get(5).x = -10000;
        	    plataformas.get(5).y = -10000;
        	    
        	    plataformas.get(4).x = -10000;
        	    plataformas.get(4).y = -10000;
        	    
        	    plataformas.get(3).x = -10000;
        	    plataformas.get(3).y = -10000;
        	    
        	    plataformas.get(2).x = -10000;
        	    plataformas.get(2).y = -10000;
        	    
        	    plataformas.get(1).x = -10000;
        	    plataformas.get(1).y = -10000;
        	    
        	    plataformas.get(0).x = -10000;
        	    plataformas.get(0).y = -10000;

    	        g.setColor(Color.RED);
    	        g.setFont(new Font("Minecraft", Font.BOLD, 20)); // Ajusta el estilo del texto
    	        g.drawString("Restantes: " + enemigosRestantes1, WIDTH-210, 90);

        	    
    	        for (Zombie zombie : zombies) {
    	            zombie.draw(g);
    	        }        	    

                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                }
        	}
        	
			else if(frameSeleccionado == 4) {
				
				// actualiza mechero
                mechero.draw(g);
				// actualiza portal
                portalActivacion.draw(g);
                
                // lava
                
                lava.get(0).x = 32*5;
                lava.get(0).y = 32*14-10;
                lava.get(0).width = 32*17;
                lava.get(0).height = 32*2;
                
        	    plataformas.get(6).x = 32*6;
        	    plataformas.get(6).y = 32*12;
        	    plataformas.get(6).width = 32*3;
        	    plataformas.get(6).height = 32*1;
        	    plataformas.get(6).textura = roca;
        	    
        		
        	    plataformas.get(5).x = 32*11;
        	    plataformas.get(5).y = 32*10;
        	    plataformas.get(5).width = 32*3;
        	    plataformas.get(5).height = 32*1;
        	    plataformas.get(5).textura = roca;
        	    
        	    plataformas.get(4).x = 32*19;
        	    plataformas.get(4).y = 32*12;
        	    plataformas.get(4).width = 32*1;
        	    plataformas.get(4).height = 32*1;
        	    plataformas.get(4).textura = roca;
        	    
        	    plataformas.get(3).x = -10000;
        	    plataformas.get(3).y = -10000;
        	    
        	    plataformas.get(2).x = -10000;
        	    plataformas.get(2).y = -10000;
        	    
        	    plataformas.get(1).x = -10000;
        	    plataformas.get(1).y = -10000;
        	    
        	    plataformas.get(0).x = -10000;
        	    plataformas.get(0).y = -10000;
        	    
		        
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                }
                
                for (Lava lava : lava) {
                    lava.draw(g);
                }

			}
        	

        }
        
        // En el caso de que el nivel sea 2

		if(nivelSeleccionado == 2) {
			if(frameSeleccionado == 1) {
				
        	    plataformas.get(6).x = 32*6;
        	    plataformas.get(6).y = 32*12;
        	    plataformas.get(6).width = 32*3;
        	    plataformas.get(6).height = 32*1;
        	    plataformas.get(6).textura = netherSuperficie;
        	    
        	    plataformas.get(5).x = 32*10;
        	    plataformas.get(5).y = 32*9;
        	    plataformas.get(5).width = 32*3;
        	    plataformas.get(5).height = 32*1;
        	    plataformas.get(5).textura = netherSuperficie;
  
        	    
        	    plataformas.get(4).x = 32*6;
        	    plataformas.get(4).y = 32*6;
        	    plataformas.get(4).width = 32*3;
        	    plataformas.get(4).height = 32*1;
        	    plataformas.get(4).textura = netherSuperficie;
        	    
        	    plataformas.get(3).x = 32*10;
        	    plataformas.get(3).y = 32*4;
        	    plataformas.get(3).width = 32*2;
        	    plataformas.get(3).height = 32*1;
        	    plataformas.get(3).textura = netherSuperficie;
        	    
        	    plataformas.get(2).x = 32 * 13 + movPlataforma;
        	    plataformas.get(2).y = 32*4;
        	    plataformas.get(2).width = 32*2;
        	    plataformas.get(2).height = 32*1;
        	    plataformas.get(2).textura = netherSuperficie;
        	    
        	    lava.get(0).x = -10000;
        	    lava.get(0).y = -10000;
        	    
        	    // movimiento plataforma ---------------------------------------
        	    // para adelante
        	    if(plataformas.get(2).x < 32 * 22 && estadoPlataforma == 0) {
        	    	movPlataforma+=2;
        	    	
        	    }
        	    
        	    else {
        	    	estadoPlataforma = 1;
        	    }
        	    
        	    // para atras
        	    
        	    if(plataformas.get(2).x > 32 * 13 && estadoPlataforma == 1) {
        	    	movPlataforma-=2;
        	    }        	    
        	    
        	    else {
        	    	estadoPlataforma = 0;
        	    }
        	    
        	    // movimiento plataforma ---------------------------------------
        	    
        	    plataformas.get(1).x = 32 * 24 ;
        	    plataformas.get(1).y = 32*3;
        	    plataformas.get(1).width = 32*5;
        	    plataformas.get(1).height = 32*1;
        	    plataformas.get(1).textura = netherSuperficie;
        	    
        	    plataformas.get(0).x = 32 * 28 ;
        	    plataformas.get(0).y = 32*8;
        	    plataformas.get(0).width = 32*1;
        	    plataformas.get(0).height = 32*6;
        	    plataformas.get(0).textura = netherrack;
        	    
                /*
                g.setColor(Color.gray);
		        g.fillRect(830, 160, 10, 100);
		        */
        	    
        	    
        	    
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }

			}
			
			if(frameSeleccionado == 2) {
        	    plataformas.get(6).x = 32*0;
        	    plataformas.get(6).y = 32*4;
        	    plataformas.get(6).width = 32*3;
        	    plataformas.get(6).height = 32*1;
        	    plataformas.get(6).textura = netherSuperficie;
        	    
        	    plataformas.get(5).x = 32*9;
        	    plataformas.get(5).y = 32*11;
        	    plataformas.get(5).width = 32*2;
        	    plataformas.get(5).height = 32*1;
        	    plataformas.get(5).textura = netherSuperficie;
  
        	    
        	    plataformas.get(4).x = 32*6;
        	    plataformas.get(4).y = 32*6;
        	    plataformas.get(4).width = 32*2;
        	    plataformas.get(4).height = 32*1;
        	    plataformas.get(4).textura = netherSuperficie;
        	    
        	    plataformas.get(3).x = 32*20;
        	    plataformas.get(3).y = 32*8;
        	    plataformas.get(3).width = 32*1;
        	    plataformas.get(3).height = 32*1;
        	    plataformas.get(3).textura = netherSuperficie;
        	    
        	    plataformas.get(2).x = 32*23;
        	    plataformas.get(2).y = 32*6;
        	    plataformas.get(2).width = 32*6;
        	    plataformas.get(2).height = 32*1;
        	    plataformas.get(2).textura = ladrilloNether;
        	    
        	    plataformas.get(1).x = 32*14 + movPlataforma;
        	    plataformas.get(1).y = 32*9;
        	    plataformas.get(1).width = 32*2;
        	    plataformas.get(1).textura = netherSuperficie;
        	    
        	    plataformas.get(0).x = -10000;
        	    plataformas.get(0).y = -10000;
   

        	    // lava
        	    
                lava.get(0).x = 32*0;
                lava.get(0).y = 32*15;
                lava.get(0).width = WIDTH;
                lava.get(0).height = 32*2;
        	    
        	    // movimiento plataforma ---------------------------------------
        	    // para adelante
        	    if(plataformas.get(1).x < 32 * 17 && estadoPlataforma == 0) {
        	    	movPlataforma+=1;
        	    	
        	    }
        	    
        	    else {
        	    	estadoPlataforma = 1;
        	    }
        	    
        	    // para atras
        	    
        	    if(plataformas.get(1).x > 32 * 14 && estadoPlataforma == 1) {
        	    	movPlataforma-=1;
        	    }        	    
        	    
        	    else {
        	    	estadoPlataforma = 0;
        	    }
        	    
        	    // movimiento plataforma ---------------------------------------
        	    
 
        	    
                /*
                g.setColor(Color.gray);
		        g.fillRect(830, 160, 10, 100);
		        */
        	    
        	    
        	    
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }				
                
                for (Lava lava : lava) {
                    lava.draw(g);
                }
			}
			
			if(frameSeleccionado == 3) {

				
                lava.get(0).x = -10000;
                lava.get(0).y = -10000;
                lava.get(0).width = WIDTH;
                lava.get(0).height = 32*2;
				
                
        	    plataformas.get(6).x = 32*0;
        	    plataformas.get(6).y = 32*6;
        	    plataformas.get(6).width = WIDTH;
        	    plataformas.get(6).textura = ladrilloNether;

        	    
        	    plataformas.get(5).x = -10000;
        	    plataformas.get(5).y = -10000;

  
        	    
        	    plataformas.get(4).x = -10000;
        	    plataformas.get(4).y = -10000;

        	    
        	    plataformas.get(3).x = -10000;
        	    plataformas.get(3).y = -10000;

        	    
        	    plataformas.get(2).x = -10000;
        	    plataformas.get(2).y = -10000;
        	    
        	    plataformas.get(1).x = -10000;
        	    plataformas.get(1).y = -10000;

        	    
        	    plataformas.get(0).x = -10000;
        	    plataformas.get(0).y = -10000;
                
                
        	    // Emboscada 2
        	    
        	    g.setColor(Color.RED);
    	        g.setFont(new Font("Minecraft", Font.BOLD, 20)); // Ajusta el estilo del texto
    	        g.drawString("Restantes: " + enemigosRestantes2, WIDTH-210, 90);
        	            
	        	

	        	    
	  
		        for (Skeleton skeleton: skeletons) {
		        	skeleton.draw(g);
		        }
    	
	        	    	    
        	    
        	    
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }		
                
			}
			
			if(frameSeleccionado == 4) {
        	    plataformas.get(6).x = 32*0;
        	    plataformas.get(6).y = 32*6;
        	    plataformas.get(6).width = 32*11;
        	    plataformas.get(6).textura = ladrilloNether;

        	    
        	    plataformas.get(5).x = 32*17;
        	    plataformas.get(5).y = 32*8;
        	    plataformas.get(5).width = 32*7;
        	    plataformas.get(5).textura = ladrilloNether;

  
        	    
        	    plataformas.get(4).x = 32*24;
        	    plataformas.get(4).y = 32*7;
        	    plataformas.get(4).width = 32*1;
        	    plataformas.get(4).textura = ladrilloNether;

        	    
        	    plataformas.get(3).x = 32*25;
        	    plataformas.get(3).y = 32*6;
        	    plataformas.get(3).width = 32*1;
        	    plataformas.get(3).height = 32*1;
        	    plataformas.get(3).textura = ladrilloNether;

        	    
        	    plataformas.get(2).x = 32*26;
        	    plataformas.get(2).y = 32*5;
        	    plataformas.get(2).width = 32*1;
        	    plataformas.get(2).height = 32*1;
        	    plataformas.get(2).textura = ladrilloNether;
        	    
        	    plataformas.get(1).x = 32*27;
        	    plataformas.get(1).y = 32*4;
        	    plataformas.get(1).width = 32*2;
        	    plataformas.get(1).height = 32*1;
        	    plataformas.get(1).textura = ladrilloNether;

        	    
        	    plataformas.get(0).x = 32*17;
        	    plataformas.get(0).y = 32*4;
        	    plataformas.get(0).width = 32*4;
        	    plataformas.get(0).height = 32*1;
        	    plataformas.get(0).textura = ladrilloNether;		
			
			
                lava.get(0).x = 0;
                lava.get(0).y = 32*15;
                lava.get(0).width = WIDTH;
                lava.get(0).height = 32*2;
        	    
                // dibujo el cofre
                
 
                cofreNether.draw(g);
                
                for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }	
			}
			
			if(frameSeleccionado == 5) {

				lava.get(0).x = 0;
				lava.get(0).y = 32*15;
				lava.get(0).width = WIDTH;
				lava.get(0).height = 32 * 2;
				
        	    plataformas.get(6).x = 32*0;
        	    plataformas.get(6).y = 32*4;
        	    plataformas.get(6).width = 32*3;
        	    plataformas.get(6).textura = ladrilloNether;

        	    
        	    plataformas.get(5).x = 32*0;
        	    plataformas.get(5).y = 32*7;
        	    plataformas.get(5).width = 32*1;
        	    plataformas.get(5).textura = ladrilloNether;

  
        	    
        	    plataformas.get(4).x = 32*0;
        	    plataformas.get(4).y = 32*8;
        	    plataformas.get(4).width = 32*8;
        	    plataformas.get(4).height = 32*1;        	    
        	    plataformas.get(4).textura = ladrilloNether;

        	    
        	    plataformas.get(3).x = 32*13 + movPlataforma2;
        	    plataformas.get(3).y = 32*9;
        	    plataformas.get(3).width = 32*2;
        	    plataformas.get(3).height = 32*1;
        	    plataformas.get(3).textura = ladrilloNether;

        	    
        	    plataformas.get(2).x = 32*16 + movPlataforma;
        	    plataformas.get(2).y = 32*12;
        	    plataformas.get(2).width = 32*2;
        	    plataformas.get(2).height = 32*1;
        	    plataformas.get(2).textura = ladrilloNether;
        	    
        	    plataformas.get(1).x = 32*22;
        	    plataformas.get(1).y = 32*14;
        	    plataformas.get(1).width = 32*7;
        	    plataformas.get(1).height = 32*1;
        	    plataformas.get(1).textura = netherSuperficie;
        	    
        	    plataformas.get(0).x = -10000;
        	    plataformas.get(0).y = -10000;
  
        	    
        	    // movimiento plataforma ---------------------------------------
        	    // para adelante
        	    if(plataformas.get(2).x < 32 * 19 && estadoPlataforma == 0) {
        	    	movPlataforma+=1;
        	    	
        	    }
        	    
        	    else {
        	    	estadoPlataforma = 1;
        	    }
        	    
        	    // para atras
        	    
        	    if(plataformas.get(2).x > 32 * 16 && estadoPlataforma == 1) {
        	    	movPlataforma-=1;
        	    }        	    
        	    
        	    else {
        	    	estadoPlataforma = 0;
        	    }
        	    
        	    // movimiento plataforma ---------------------------------------
        	    
        	    // movimiento plataforma ---------------------------------------
        	    // para adelante
        	    if(plataformas.get(3).x < 32 * 13 && estadoPlataforma2 == 0) {
        	    	movPlataforma2+=1;
        	    	
        	    }
        	    
        	    else {
        	    	estadoPlataforma2 = 1;
        	    }
        	    
        	    // para atras
        	    
        	    if(plataformas.get(3).x > 32 * 10 && estadoPlataforma2 == 1) {
        	    	movPlataforma2-=1;
        	    }        	    
        	    
        	    else {
        	    	estadoPlataforma2 = 0;
        	    }
        	    
        	    // movimiento plataforma ---------------------------------------
				
				for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }					
			}
		}
		
		
		if(nivelSeleccionado == 3) {
			if(frameSeleccionado == 1) {
				
				plataformas.get(6).x = -10000;
        	    plataformas.get(6).y = -10000;
        	    
        	    plataformas.get(5).x = 32 * 5;
        	    plataformas.get(5).y = 32 * 13;
        	    plataformas.get(5).width = 32 * 2 ;
        	    
        	    plataformas.get(4).x = 32 * 25;
        	    plataformas.get(4).y = 32 * 7;
        	    plataformas.get(4).width = 32 * 4 ;
        	    plataformas.get(4).height = 32 * 1 ;
        	    plataformas.get(4).textura = roca ;
        	    
        	    plataformas.get(3).x = 32 * 19;
        	    plataformas.get(3).y = 32 * 9;
        	    plataformas.get(3).width = 32 * 2 ;
        	    plataformas.get(3).height = 32 * 1 ;
        	    plataformas.get(3).textura = roca ;
        	    
        	    
        	    plataformas.get(2).x = 32 * 11;
        	    plataformas.get(2).y = 32 * 6;
        	    plataformas.get(2).width = 32 * 3; 

        	    
        	    plataformas.get(1).x = 32 * 5;
        	    plataformas.get(1).y = 32 * 9 ;
        	     /* plataformas.get(1).height = 32 * 2 ; */ 
        	    plataformas.get(1).width = 32 *  2;
        	    plataformas.get(1).textura = roca;

        	    
        	    plataformas.get(0).x = 32 * 11 ;
        	    plataformas.get(0).y = 32 * 11 ;
        	    plataformas.get(0).width = 32 * 3; 
        	    
        	    
				for (Plataforma plataforma : plataformas) {
                    plataforma.draw(g);
                    plataforma.update();
                }			
			}
		}
		


        // dibujos extras
        
        
		// Dibuja al jugador
        
        player.draw(g);
        
        // Final Dibujo de Nivel 1 ---------------------------------------------
        
        // Si el jugador muere se muestra en la pantalla 
        if (gameOverTimerStarted) {
            // Fondo rojo
            g.setColor(Color.red);
            g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

            // Sombra del texto "GAME OVER"
            Font gameOverFont = new Font("Minecraft", Font.BOLD, 100);
            g.setFont(gameOverFont);
            String gameOverText = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(gameOverText);
            int x = (Window.WIDTH - textWidth) / 2; // Centrar el texto
            int y = Window.HEIGHT / 2;

            // Dibuja sombra (negra, levemente desplazada)
            g.setColor(Color.black);
            g.drawString(gameOverText, x + 5, y + 5);

            // Dibuja el texto principal (blanco)
            g.setColor(Color.white);
            g.drawString(gameOverText, x, y);

            // Dibuja mensaje adicional de "Presiona ENTER"
            Font smallFont = new Font("Minecraft", Font.PLAIN, 40);
            g.setFont(smallFont);
            
        }

        
        // Dibujar Menu_Pausa si está visible
        menuPausa.draw(g, WIDTH, HEIGHT);
        
		// ---------------------------------------------- Termina el dibujo
		
		g.dispose();
		bs.show();
	}
	
	
	
	@Override
	public void run() {
	    // -------------- Tiempo restringido a 60 FPS --------------
	    long now = 0;
	    long lastTime = System.nanoTime();
	    long time = 0;
	    
	    while (running) {
	        now = System.nanoTime();
	        delta += (now - lastTime) / TARGETTIME;
	        lastTime = now;

	        if (delta >= 1) {
	            update(); // Actualiza lógica del juego
	            draw();   // Dibuja en pantalla
	            delta--;
	        }

	        if (time >= 1000000000) {
	            time = 0;
	        }
	    }

	    // Detenemos y limpiamos los recursos
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
	
	private void closeGameAndOpenMenu() {
	    // Cerrar la ventana actual
	    this.dispose();
	    
	    // Abrir la pantalla del menú
	    new Menu_Screen().start();
	}


}
