package com.vzao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.vzao.main.Game;
import com.vzao.world.Camera;

public class Weapon extends Entity {
	
	protected int frames = 0, maxFrames= 30;
	public int index = 0;
	protected int maxIndex=3;
	private BufferedImage[] sprites;

	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		sprites=new BufferedImage[4];
		sprites[0]=Game.spritesheet.getSprite(224, 0, 32, 32);
		sprites[1]=Game.spritesheet.getSprite(224, 32, 32, 32);
		sprites[2]=Game.spritesheet.getSprite(224, 64, 32, 32);
		sprites[3]=Game.spritesheet.getSprite(224, 96, 32, 32);
		
		
		
	}
	
	public void tick() {
		frames++;
		if(frames==maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index=0;
			}
			
			
	
		}	
	}
	
	public void render(Graphics g) {
		
			g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
	}

}
