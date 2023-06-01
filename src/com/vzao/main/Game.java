package com.vzao.main;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.vzao.entities.Boss;
import com.vzao.entities.BulletShoot;
import com.vzao.entities.Enemy;
import com.vzao.entities.Entity;
import com.vzao.entities.Player;
import com.vzao.graficos.Spritesheet;
import com.vzao.graficos.UI;
import com.vzao.world.World;

public class Game extends Canvas implements Runnable, KeyListener,MouseListener{
	
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 200;
	public static final int SCALE = 2;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static List<Boss> boss;
	public static Spritesheet spritesheet;
	
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	
	public UI ui;
	
	public static String gameState= "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame=false;
	
	public Menu menu;
	

	
	
	
	public Game() {
		
		Sound.musicBackground.loop();;
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//Inicializando objetos:
		ui=new UI();
		image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		boss = new ArrayList<Boss>();
		
		
		spritesheet = new Spritesheet("/spritesheetZ.png");
		player= new Player(0,0,32,32,spritesheet.getSprite(64, 0, 32,32));
		entities.add(player);
		world = new World("/level1.png");
		
		
		menu = new Menu();
		
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	
		
		
		
	}
	
	public synchronized void stop() {
		isRunning=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		
		if(gameState=="NORMAL"){
		this.restartGame=false;
		for(int i = 0; i < entities.size(); i ++) {
			Entity e = entities.get(i);
			if(e instanceof Player) {
				//Estou dando tick no player.
			}
			e.tick();
		}
		
		for(int i = 0;i<bullets.size();i++) {
			bullets.get(i).tick();
			
		}
		
		if(enemies.size()==0&&boss.size()==0) {
			//Avan�ar para o proximo level.
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			//System.out.println(newWorld);
			World.restartGame(newWorld);
		}
		}else if(gameState=="GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver==30) {
				this.framesGameOver=0;
				if(this.showMessageGameOver) 
					this.showMessageGameOver=false;
					else 
						this.showMessageGameOver=true;
					
			}
			
			if(restartGame) {
				this.restartGame=false;
				Game.gameState="NORMAL";
				CUR_LEVEL=1;
				String newWorld = "level"+CUR_LEVEL+".png";
				//System.out.println(newWorld);
				World.restartGame(newWorld);
				
			}
		}else if(gameState=="MENU") {
			//
			menu.tick();
		}
		}

			
			
		
		
		
			
			

		
	

	
	public void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
			
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Graphics2D g2 = (Graphics2D)g;
		world.render(g);
		for(int i = 0; i < entities.size(); i ++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0;i<bullets.size();i++) {
			bullets.get(i).render(g);
			
		}
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null);
		g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null);
		if(gameState=="GAME_OVER") {
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(0,0,0,200));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial",Font.BOLD,48));
			g.setColor(Color.WHITE);
			g.drawString("Game Over :(", 180, (HEIGHT*SCALE)/2);
			g.setFont(new Font("arial",Font.BOLD,18));
			if(showMessageGameOver) 
				g.drawString(">Pressione Enter para reiniciar<", 190, 330);	 
		}else if(gameState=="MENU") {
			menu.render(g);
			
		}
		bs.show();
	}


	public void run() {
		long lastTime = System.nanoTime();
		double amoutOfTicks = 60.0;
		double ns = 1000000000 / amoutOfTicks;
		double delta=0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime)/ns;
			lastTime=now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
				
			}
			
			if(System.currentTimeMillis()- timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer+=1000;
				
				
			}
			
		}
		
		stop();
		
	}

	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_D ||
				e.getKeyCode()==KeyEvent.VK_RIGHT) {
			player.right=true;
			///execute tal a��o!
		}else if(e.getKeyCode()== KeyEvent.VK_A ||
				e.getKeyCode()==KeyEvent.VK_LEFT) { 
			player.left=true;
		}//Separar os dois para conseguir ir para duas dire��es sem serem opostas.
		
		if(e.getKeyCode()==KeyEvent.VK_W||
				e.getKeyCode()==KeyEvent.VK_UP) {
			player.up=true;
			
			if(gameState=="MENU") {
				menu.up=true;
			}
		}else if(e.getKeyCode()==KeyEvent.VK_S||
				e.getKeyCode()==KeyEvent.VK_DOWN) {
			player.down=true;
			
			if(gameState=="MENU") {
				menu.down=true;
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			player.shoot=true;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			restartGame=true;
			if(gameState=="MENU") {
				menu.enter=true;
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
			gameState="MENU";
			menu.pause=true;
		}
		
	}

	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_D||
				e.getKeyCode()==KeyEvent.VK_RIGHT) {
			player.right = false;
			player.index=0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_A||
				e.getKeyCode()==KeyEvent.VK_LEFT) {
			player.left=false;
			player.index=0;
		}
		if(e.getKeyCode()==KeyEvent.VK_W||
				e.getKeyCode()==KeyEvent.VK_UP) {
			player.up = false;
			player.index=0;
		}else if(e.getKeyCode()==KeyEvent.VK_S||
				e.getKeyCode()==KeyEvent.VK_DOWN) {
			player.down= false;
			player.index=0;
		}
		
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			player.shoot=false;
		}
		
		
	}
	

	
	public void keyTyped(KeyEvent e) {
	
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot=true;
		player.mx = (e.getX()/2);
		player.my=(e.getY()/2);
		
	
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.mouseShoot=false;
		
	}
	
	

}

