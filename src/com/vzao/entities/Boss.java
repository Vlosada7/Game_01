package com.vzao.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vzao.main.Game;
import com.vzao.main.Sound;
import com.vzao.world.Camera;
import com.vzao.world.World;

public class Boss extends Entity {
	
	public double speed =1.1;
	private int maskx=0,masky=0,maskw=64,maskh=64;
	
	protected int frames = 0, maxFrames= 10;
	public int index = 0;
	protected int maxIndex=3;
	private BufferedImage[] sprites;
	
	private int life =100;
	
	private boolean isDamaged = false;
	private int damageFrames = 5,damageCurrent = 0;

	public Boss(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites=new BufferedImage[4];
		sprites[0]=Game.spritesheet.getSprite(0, 160, 64, 64);
		sprites[1]=Game.spritesheet.getSprite(64, 160, 64, 64);
		sprites[2]=Game.spritesheet.getSprite(128, 160, 64, 64);
		sprites[3]=Game.spritesheet.getSprite(192, 160, 64, 64);
		
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
			if(Game.rand.nextInt(100)<90) {
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
		Game.boss.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i=0;i<Game.bullets.size();i++) {
			Entity e = Game.bullets.get(i);
				if(Entity.isColidding(e,this)) {
					Sound.bossHurt.play();
					isDamaged = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
			
	}
	}
	public boolean isColiddingWithPlayer() {
		Rectangle boss = new Rectangle((int) (this.getX()+maskx),this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),32,32);
		
		return boss.intersects(player);
	}
	
	public boolean isColidding(double xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle((int) (xnext+maskx),ynext+masky,maskw,maskh);
		
		for(int i=0;i<Game.boss.size();i++) {
			Boss e = Game.boss.get(i);
			if(e==this)
				continue;
			Rectangle targetBoss = null;
			if(enemyCurrent.intersects(targetBoss)) {
				return true;
			}
			
		}
		
		return false;
	}
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
			}else {
				g.drawImage(Entity.BOSS_FEEDBACK, this.getX()-Camera.x, this.getY()-Camera.y, null);
			}
		//g.setColor(Color.red);
		//g.fillRect(this.getX()+maskx - Camera.x, this.getY()+masky- Camera.y, maskw,maskh);
		
		
	}

}
