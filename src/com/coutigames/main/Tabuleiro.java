package com.coutigames.main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Tabuleiro {

	public static BufferedImage spritesheet;
	
	public static final int WIDTH = 11,HEIGHT = 7;
	public static int[][] TABULEIRO;
	
	// Variáveis globais no Game
	public static boolean isGameOver = false; // Indica se o jogo está no estado de Game Over
	private static long startTime = System.currentTimeMillis(); // Marca o início do cronômetro
	private Font gameOverFont = new Font("Arial", Font.BOLD, 50); // Fonte para Game Over
	private static final Font restartFont = new Font("Arial", Font.PLAIN, 20); // Fonte para instruções
	
	public static int GRID_SIZE = 40;
	
	public static int DOCE_0 = 0,DOCE_1 = 1, DOCE_2 = 2;
	public static int DOCE_3 = 3,DOCE_4 = 4, DOCE_5 = 5;
	public static int DOCE_6 = 6,DOCE_7 = 7, DOCE_8 = 8;
	
	
	public BufferedImage DOCE_0_SPRITE = Tabuleiro.getSprite(1299,188,117,117);
	public BufferedImage DOCE_1_SPRITE = Tabuleiro.getSprite(1310,338,95,127);
	public BufferedImage DOCE_2_SPRITE = Tabuleiro.getSprite(971,281,106,116);
	
	public BufferedImage DOCE_3_SPRITE = Tabuleiro.getSprite(1118,179,134,136);
	public BufferedImage DOCE_4_SPRITE = Tabuleiro.getSprite(808,15,129,136);
	public BufferedImage DOCE_5_SPRITE = Tabuleiro.getSprite(629,178,151,137);
	
	public BufferedImage DOCE_6_SPRITE = Tabuleiro.getSprite(1028,1535,125,118);
	public BufferedImage DOCE_7_SPRITE = Tabuleiro.getSprite(859,1528,130,134);
	public BufferedImage DOCE_8_SPRITE = Tabuleiro.getSprite(984,147,77,115);
	
	public Tabuleiro() {
		
		TABULEIRO = new int[WIDTH][HEIGHT];
		
		for(int x = 0; x < WIDTH; x++) {//Lógica para multiplicar a casas dos doces
			for(int y = 0; y < HEIGHT; y++) {
				TABULEIRO[x][y] = new Random().nextInt(9);//Aqui altero a quantidade de doces que posso colocar
			}
		}
	}
	
	public static BufferedImage getSprite(int x,int y,int width,int height) {
		if(spritesheet == null) {
			try {
				spritesheet = ImageIO.read(Tabuleiro.class.getResource("/spritesheet.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return spritesheet.getSubimage(x, y, width, height);
	}
	
	// Método principal de atualização
	public void update() {
	    if (isGameOver) {
	        return; // Pausa o jogo se estiver em Game Over
	    }

	    ArrayList<Candy> combos = new ArrayList<Candy>();

	    // Verifica o tempo restante
	    long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // Tempo em segundos
	    if (elapsedTime > 15) {
	        gameOver();
	        return;
	    }

	    // Lógica para encontrar combos horizontalmente
	    for (int yy = 0; yy < HEIGHT; yy++) {
	        if (combos.size() == 3) {
	            processCombo(combos);
	            return;
	        }

	        combos.clear();
	        for (int xx = 0; xx < WIDTH; xx++) {
	            int cor = TABULEIRO[xx][yy];
	            if (combos.size() == 0) {
	                combos.add(new Candy(xx, yy, cor));
	            } else if (combos.get(combos.size() - 1).CANDY_TYPE == cor) {
	                combos.add(new Candy(xx, yy, cor));
	            } else {
	                combos.clear();
	                combos.add(new Candy(xx, yy, cor));
	            }

	            if (combos.size() == 3) {
	                processCombo(combos);
	                return;
	            }
	        }
	    }

	    // Lógica para encontrar combos verticalmente
	    for (int xx = 0; xx < WIDTH; xx++) {
	        if (combos.size() == 3) {
	            processCombo(combos);
	            return;
	        }

	        combos.clear();
	        for (int yy = 0; yy < HEIGHT; yy++) {
	            int cor = TABULEIRO[xx][yy];
	            if (combos.size() == 0) {
	                combos.add(new Candy(xx, yy, cor));
	            } else if (combos.get(combos.size() - 1).CANDY_TYPE == cor) {
	                combos.add(new Candy(xx, yy, cor));
	            } else {
	                combos.clear();
	                combos.add(new Candy(xx, yy, cor));
	            }

	            if (combos.size() == 3) {
	                processCombo(combos);
	                return;
	            }
	        }
	    }
	}

	// Método para processar um combo
	private void processCombo(ArrayList<Candy> combos) {
	    for (int i = 0; i < combos.size(); i++) {
	        int xtemp = combos.get(i).x;
	        int ytemp = combos.get(i).y;
	        TABULEIRO[xtemp][ytemp] = new Random().nextInt(9);
	    }
	    combos.clear();
	    Game.points++;
	    Game.frame.setTitle("Candy_Crush_Couti - Pontos: " + Game.points);
	    System.out.println("Pontuei!");

	    // Reseta o cronômetro ao pontuar
	    startTime = System.currentTimeMillis();
	}

	// Método para Game Over
	private void gameOver() {
	    isGameOver = true;
	    System.out.println("Game Over! Você não pontuou em 15 segundos.");
	}
	// Método para reiniciar o jogo
	public static void restartGame() {
	    isGameOver = false;
	    Game.points = 0;
	    startTime = System.currentTimeMillis();
	    Game.frame.setTitle("Candy_Crush_Couti - Pontos: 0");

	    // Reinicializa o tabuleiro
	    for (int xx = 0; xx < WIDTH; xx++) {
	        for (int yy = 0; yy < HEIGHT; yy++) {
	            TABULEIRO[xx][yy] = new Random().nextInt(9);
	        }
	    }
	    System.out.println("Jogo reiniciado!");
	}

	
	public void render(Graphics g) {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				g.setColor(Color.black);
				g.draw3DRect(x*GRID_SIZE + 24, y*GRID_SIZE + 24, GRID_SIZE, GRID_SIZE, false);
				//g.drawRect(x*GRID_SIZE + 24, y*GRID_SIZE + 24, GRID_SIZE, GRID_SIZE);
				int doce = TABULEIRO[x][y];
				if(doce == DOCE_0) {
					g.setColor(Color.orange);
					//g.fill3DRect(x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25, false);
					//g.fillRect(x*GRID_SIZE + 12 + 24, y*GRID_SIZE + 12 + 24, 25, 25);					
					g.drawImage(DOCE_0_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_1) {
					g.setColor(Color.green);
					//g.fillRect(x*GRID_SIZE + 12 + 24, y*GRID_SIZE + 12 + 24, 25, 25);			
					g.drawImage(DOCE_1_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_2) {
					g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_2_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_3) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_3_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_4) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_4_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_5) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_5_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_6) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_6_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_7) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_7_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
				if(doce == DOCE_8) {
					//g.setColor(Color.yellow);
					//g.fillRect(x*GRID_SIZE + 12+ 24, y*GRID_SIZE + 12+ 24, 25, 25);			
					g.drawImage(DOCE_8_SPRITE, x*GRID_SIZE + 10 + 22, y*GRID_SIZE + 10 + 22, 25, 25,null);
				}
							
				
				if(Game.selected) {
					int posx = Game.previousx/GRID_SIZE;
					int posy = Game.previousy/GRID_SIZE;
					g.setColor(Color.RED);					
					//g.drawRect(posx*GRID_SIZE + 24,posy*GRID_SIZE+ 24,GRID_SIZE,GRID_SIZE);
					g.draw3DRect(posx*GRID_SIZE + 24,posy*GRID_SIZE+ 24,GRID_SIZE,GRID_SIZE, false);
				}
				 if (isGameOver) {//AQUI PARA INICIAR NO GAME OVER
				        g.setColor(Color.BLACK);
				        g.setFont(gameOverFont);
				        g.drawString("GAME OVER", 90, 120);

				        g.setColor(Color.black);
				        g.setFont(restartFont);
				        g.drawString("Pressione 'R' para reiniciar", 125, 170);
				    }
				}
			}
		}
	}
	
