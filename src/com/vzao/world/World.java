package com.vzao.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vzao.entities.Boss;
import com.vzao.entities.Bullet;
import com.vzao.entities.Enemy;
import com.vzao.entities.Entity;
import com.vzao.entities.LifePack;
import com.vzao.entities.Player;
import com.vzao.entities.Weapon;
import com.vzao.graficos.Spritesheet;
import com.vzao.main.Game;

public class World {
	
	private static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 32;
	
	
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels= new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT= map.getHeight();
			tiles=new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels,0, map.getWidth());
			for(int xx=0; xx<map.getWidth();xx++){
				for(int yy = 0; yy<map.getHeight();yy++) {
					int pixelAtual = pixels[xx+(yy*map.getWidth())];
					tiles[xx+(yy*map.getWidth())] = new FloorTile(xx*32,yy*32,Tile.TILE_FLOOR);
					
					
					
					if(pixelAtual == 0xFF000000) {
						//Floor
						tiles[xx+(yy*map.getWidth())] = new FloorTile(xx*32,yy*32,Tile.TILE_FLOOR);
					}else if(pixelAtual==0xFFFFFFFF) {
						//Parede
						tiles[xx+(yy*map.getWidth())] = new WallTile(xx*32,yy*32,Tile.TILE_WALL);
						
					}else if(pixelAtual==0xFF009E12) {
						tiles[xx+(yy*map.getWidth())] = new FloorTile(xx*32,yy*32,Tile.TILE_GRASS);
						
					}else if(pixelAtual==0xFF685104) {
						tiles[xx+(yy*map.getWidth())]= new WallTile(xx*32,yy*32,Tile.TILE_WOOD);
						
					}else if(pixelAtual== 0xFF0026FF) {
						//player
						Game.player.setX(xx*32);
						Game.player.setY(yy*32);
					}else if(pixelAtual == 0xFFFF0000) {
						//Enemy
						Enemy en =new Enemy(xx*32,yy*32,32,32,Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if(pixelAtual==0xFF7F006E) {
						//Boss
						Boss en = new Boss(xx*32,yy*32,64,64,Entity.BOSS_EN);
						Game.entities.add(en);
						Game.boss.add(en);
						
						
						
						
					}else if(pixelAtual == 0xFF808080) {
						//Weapon
						Weapon pack= new Weapon(xx*32,yy*32,32,32,Entity.WEAPON_EN);
						Game.entities.add(pack);
						
					}else if(pixelAtual == 0xFFFFD800) {
						//Bullet
						Bullet pack = new Bullet(xx*32,yy*32,32,32,Entity.BULLET_EN);
						Game.entities.add(pack);
						
					}else if(pixelAtual == 0xFFFF7FB6) {
						//LifePack
						LifePack pack = new LifePack(xx*32,yy*32,32,32,Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					}
					
					
					
				}
				
			}
			
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(double xnext,int ynext) {
		int x1=(int) (xnext/ TILE_SIZE);
		int y1=ynext/TILE_SIZE;
		
		int x2 = (int) ((xnext + TILE_SIZE-1)/TILE_SIZE);
		int y2 = ynext/TILE_SIZE;
		
		int x3 = (int) (xnext/TILE_SIZE);
		int y3 = (ynext+TILE_SIZE-1)/TILE_SIZE;
		
		int x4 = (int) ((xnext+TILE_SIZE-1)/TILE_SIZE);
		int y4 = (ynext+TILE_SIZE-1)/TILE_SIZE;
		
		return !((tiles[x1+(y1*World.WIDTH)] instanceof WallTile)||
				(tiles[x2+(y2*World.WIDTH)] instanceof WallTile)||
				(tiles[x3+(y3*World.WIDTH)] instanceof WallTile)||
				(tiles[x4+(y4*World.WIDTH)] instanceof WallTile));
		
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.boss = new ArrayList<Boss>();
		Game.spritesheet = new Spritesheet("/spritesheetZ.png");
		Game.player= new Player(0,0,32,32,Game.spritesheet.getSprite(64, 0, 32,32));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		
		
		return;
	}
	
	public void render(Graphics g) {
		int xstart= Camera.x/32;
		int ystart= Camera.y/32;
		
		int xfinal = xstart+Game.WIDTH/32;
		int yfinal = ystart+Game.HEIGHT/32;
		
		for(int xx= xstart;xx<=xfinal;xx++) {
			for(int yy = ystart; yy<=yfinal; yy++) {
				if(xx<0||yy<0||xx>=WIDTH||yy>=HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}

}
