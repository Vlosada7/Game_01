package com.vzao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.vzao.main.Game;
import com.vzao.main.Sound;
import com.vzao.world.Camera;
import com.vzao.world.World;

public class Player extends Entity{
	
	public boolean right,up,left,down;
	public int down_dir=0;
	public int up_dir=1;
	public int right_dir=2; 
	public int left_dir=3;
	public int dir = down_dir;
	public double speed = 1.7;
	public  double life = 100; 
	public final double maxLife=100;
	public int ammo = 0;
	public final int maxAmmo=100;
	
	protected int frames = 0, maxFrames= 10;
	public int index = 0;
	protected int maxIndex=3;
	private boolean moved = false;
	
	public boolean isDamage = false;
	private int damagedFrame = 0;
	
	public boolean shoot = false,mouseShoot=false;
	public int mx,my;
	
	
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	public BufferedImage playerDamage;
	
	public boolean hasGun = false;
	
	

	
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 128, 32, 32);
		
		

		//looping
		
		for(int i=0;i<4;i++) {
			upPlayer[i]=Game.spritesheet.getSprite(96, 0+(i*32), width, height);
		}
		for(int i=0;i<4;i++) {
			downPlayer[i]=Game.spritesheet.getSprite(64, 0+(i*32), width, height);
		}
		for(int i=0; i<4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(128, 0 + (i*32), 32, 32);
		}
		for(int i=0; i<4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(160, 0+(i*32), 32, 32);
		}
	

	
		
		
		
		///Jeito sem looping.
		/*
		downPlayer[0] = Game.spritesheet.getSprite(64, 0, 32, 32);
		downPlayer[1] = Game.spritesheet.getSprite(64, 32, 32, 32);
		downPlayer[2] = Game.spritesheet.getSprite(64, 64, 32, 32);
		downPlayer[3] = Game.spritesheet.getSprite(64, 96, 32, 32);
		
		rightPlayer[0] = Game.spritesheet.getSprite(128, 0, 32, 32);
		rightPlayer[1]=Game.spritesheet.getSprite(128, 32, 32, 32);
		rightPlayer[2]=Game.spritesheet.getSprite(128, 64, 32, 32);
		rightPlayer[3]=Game.spritesheet.getSprite(128, 96, 32, 32);
		
		leftPlayer[0]=Game.spritesheet.getSprite(160, 0, 32, 32);
		leftPlayer[1]=Game.spritesheet.getSprite(160, 32, 32, 32);
		leftPlayer[2]=Game.spritesheet.getSprite(160, 64, 32, 32);
		leftPlayer[3]=Game.spritesheet.getSprite(160, 96, 32, 32);
		
		upPlayer[0] = Game.spritesheet.getSprite(96, 0, 32, 32);
		upPlayer[1] = Game.spritesheet.getSprite(96, 32, 32, 32);
		upPlayer[2] = Game.spritesheet.getSprite(96, 64, 32, 32);
		upPlayer[3] = Game.spritesheet.getSprite(96, 96, 32, 32);
		*/ 
	
	}
	
	public void tick() {
		moved=false;
		if(right && World.isFree((int)(x+speed),this.getY())){
			moved=true;
			dir = right_dir;
			
			
			x+=speed;
	
		}
		else if(left && World.isFree((int)(x-speed),this.getY())){
			moved=true;
			dir=left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved=true;
			dir=up_dir;
			y-=speed;
		}
		if(down && World.isFree(this.getX(),(int)(y+speed))){
			moved=true;
			dir=down_dir;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames==maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index=0;
				}
			}
		}
		this.checkCollisionWeapon();
		this.checkCollisionLifePack();
		this.checkCollisionBullet();
		
		if(this.isDamage) {
			this.damagedFrame++;
			if(this.damagedFrame==1) {
				this.damagedFrame=0;
				isDamage=false;
			}
			
		}
		
		if(shoot && hasGun&&ammo>0) {
			//Criar bala e atirar!
			Sound.shootSound.play();
			shoot=false;
			int dx = 0;
			int dy =0;
			int px=0;
			int py=0;
			ammo--;
			if(dir==right_dir) {
				px=26;
				py=18;
				 dx=1; 
			}else if(dir==left_dir) {
				px=2;
				py=18;
				 dx=-1;
			
			}else if(dir==down_dir) {
				px=7;
				py=20;
				dy=1;
			}else if(dir==up_dir) {
				px=23;
				py=16;
				dy=-1;
			}
			
			BulletShoot bullet=new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
			Game.bullets.add(bullet);
		
		}
		
		if(mouseShoot && hasGun && ammo>0) {
			Sound.shootSound.play();
			mouseShoot=false;
			double angle = Math.atan2(my-(this.getY()+16-Camera.y),mx- (this.getX()+16-Camera.x));
			double dx = Math.cos(angle);
			double dy =Math.sin(angle);
			int px=16;
			int py=16;
			ammo--;
			if(dir==right_dir) {
				px=26;
				py=18;
				 dx=1; 
			}else if(dir==left_dir) {
				px=2;
				py=18;
				 dx=-1;
			
			}else if(dir==down_dir) {
				px=7;
				py=20;
				dy=1;
			}else if(dir==up_dir) {
				px=23;
				py=16;
				dy=-1;
			}
		
			
			BulletShoot bullet=new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
			Game.bullets.add(bullet);
		}
		
		
		
		
		
		
		
		if(life<=0) {
			//Game over
			life=0;
			
			Sound.gameOver.play();
			Game.gameState="GAME_OVER";
	
		}
		
		Camera.x=Camera.clamp(this.getX()-(Game.WIDTH/2),0,World.WIDTH*32-Game.WIDTH);
		Camera.y=Camera.clamp(this.getY()-(Game.HEIGHT/2),0,World.HEIGHT*32-Game.HEIGHT);
			
		
			
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0;i<Game.entities.size();i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof LifePack) {
				if(Entity.isColidding(this, e)) {
					if(life>=maxLife) {
						life=maxLife;
						return;
					}else if(life>90) {
						Sound.lifeSound.play();
						life+=maxLife-life;
						Game.entities.remove(e);
						return;
						
					}else if(life<=90)
						Sound.lifeSound.play();
						life+=10;
						Game.entities.remove(e);
						return;
				
					
				}
				}
			}
		
		
					
				
			
		
		
		
	}
	
	public void checkCollisionBullet() {
		for(int i = 0; i<Game.entities.size();i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Bullet) {
				if(Entity.isColidding(this, e)) {
					if(ammo>=maxAmmo) {
						ammo=maxAmmo;
						return;
					}else if(ammo>90) {
						Sound.bulletSound.play();
						ammo+=maxAmmo-ammo;
						Game.entities.remove(e);
						return;
						
					}else if(ammo<=90)
						Sound.bulletSound.play();
						ammo+=10;
						Game.entities.remove(e);
						return;
				
					
					
				}
				
			}
		}
		
	}
	
	public void checkCollisionWeapon() {
		for(int i = 0; i<Game.entities.size();i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Weapon) {
				if(Entity.isColidding(this, e)) {
					Sound.pickGun.play();
					hasGun=true;
					Game.entities.remove(i);
					return;
					
				}
				
			}
		}
		
	}

	public void render(Graphics g) {
		if(!isDamage) {
			if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
				if(hasGun) {
					///desenhar arma para baixo.
					g.drawImage(WEAPON_DOWN, this.getX()-4-Camera.x, this.getY()+10-Camera.y, null);
				}
			}else if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
				if(hasGun) {
					///desenha arma para cima.
					g.drawImage(WEAPON_UP, this.getX()+12-Camera.x, this.getY()+6-Camera.y, null);
					
				}
			}else if(dir==right_dir) {
				g.drawImage(rightPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
				if(hasGun) {
					///desenhar arma para direita.
					g.drawImage(WEAPON_RIGHT, this.getX()+9-Camera.x, this.getY()+8-Camera.y, null);
				}
			}else if(dir==left_dir) {
				g.drawImage(leftPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
				if(hasGun) {
					///Desenha arma para esquerda.
					g.drawImage(WEAPON_LEFT, this.getX()-7-Camera.x, this.getY()+7-Camera.y, null);
					
				}
			}
		}else {
			g.drawImage(playerDamage, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
			
		
	
	}

}

