package com.vzao.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vzao.main.Game;
import com.vzao.main.Sound;
import com.vzao.world.Camera;
import com.vzao.world.World;

public class Enemy extends Entity{
	
	public double speed =0.9;
	private int maskx=16,masky=16,maskw=20,maskh=20;
	
	protected int frames = 0, maxFrames= 10;
	public int index = 0;
	protected int maxIndex=3;
	private BufferedImage[] sprites;
	
	private int life =2;
	
	private boolean isDamaged = false;
	private int damageFrames = 5,damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites=new BufferedImage[4];
		sprites[0]=Game.spritesheet.getSprite(192, 0, 32, 32);
		sprites[1]=Game.spritesheet.getSprite(192, 32, 32, 32);
		sprites[2]=Game.spritesheet.getSprite(192, 64, 32, 32);
		sprites[3]=Game.spritesheet.getSprite(192, 96, 32, 32);
		
	}
	
	public void tick() {
		
		/*
		maskx=16;
		masky=16;
		maskw=12;
		maskh=12;
		*/
		//if(Game.rand.nextInt(100)<30) {
		
		if(this.isColiddingWithPlayer()==false) {
			
		if((int)x<Game.player.getX()&& World.isFree((int)x+speed, this.getY())
				&& !isColidding((int)(x+speed),this.getY())){
			x+=speed;
		}
		else if((int)x>Game.player.getX()&& World.isFree((int)x-speed, this.getY())
				&& !isColidding((int)x-speed, this.getY())){
			x-=speed;
		}
		
		if((int)y<Game.player.getY()&& World.isFree(getX(), (int)(y+speed))
				&& !isColidding(getX(), (int)(y+speed))){
			y+=speed;
		}
		else if((int)y>Game.player.getY()&& World.isFree(getX(), (int)(y-speed))
				&& !isColidding(getX(),(int)(y-speed))){
			y-=speed;
		}
		}else {
			//Estamos colidindo.
			if(Game.rand.nextInt(100)<10) {
				Sound.playerHurt.play();
				Game.player.life-=Game.rand.nextInt(2);
				Game.player.isDamage=true;
				
				
			}
			
		}
		
			frames++;
			if(frames==maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index=0;
				}
			}
			
			collidingBullet();
			
			if (life<=0) {
				destroySelf();
				return;
			}
			
			if(isDamaged) {
				this.damageCurrent++;
				if(this.damageCurrent==this.damageFrames) {
					this.damageCurrent=0;
					this.isDamaged=false;
				}
			}
			
		
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i=0;i<Game.bullets.size();i++) {
			Entity e = Game.bullets.get(i);
				if(Entity.isColidding(e,this)) {
					Sound.enemyHurt.play();
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
			
		}
				
		
		

		
		
		
	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle((int) (this.getX()+maskx),this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),32,32);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(double xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle((int) (xnext+maskx),ynext+masky,maskw,maskh);
		
		for(int i=0;i<Game.enemies.size();i++) {
			Enemy e = Game.enemies.get(i);
			if(e==this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx,e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	public void render(Graphics g) {
		if(!isDamaged) {
		g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
	}
	
	
	
	
	

}
