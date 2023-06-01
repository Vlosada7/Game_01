package com.vzao.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.vzao.main.Game;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(3,3,100, 8);
		g.setColor(Color.GREEN);
		g.fillRect(3,3,(int)((Game.player.life/Game.player.maxLife)*100), 8);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.BOLD,10));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife,35,10);
		g.setColor(Color.RED);
		g.fillRect(3, 12, 100, 8);
		g.setColor(Color.YELLOW);
		g.fillRect(3, 12, (int)(Game.player.ammo), 8);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.BOLD,10));
		g.drawString((int)Game.player.ammo+"/"+(int)Game.player.maxAmmo,35,20);
	}

}
