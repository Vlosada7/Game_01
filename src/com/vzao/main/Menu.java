package com.vzao.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Menu {
	
	public String[] options= {"Novo Jogo","Carregar Jogo","Sair"};
	
	public int currentOption=0;
	public int maxOption= options.length-1;
	
	public boolean up,down,enter;
	
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			Sound.menuChoice.play();
			up=false;
			currentOption--;
			if(currentOption<0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			Sound.menuChoice.play();
			down=false;
			currentOption++;
			if(currentOption>maxOption) {
				currentOption = 0;
		}
		}
		if(enter) {
			Sound.menuChoice.play();
			enter=false;
			if(options[currentOption]=="Novo Jogo"||options[currentOption]=="Continuar") {
				Game.gameState="NORMAL";
				pause=false;
				
			}else if(options[currentOption]=="Sair") {
				System.exit(1);
			}
			
		}
		
	}
	
	
	
	public static void saveGame(String[] val1,int[] val2,int encode) {
		BufferedWriter write =null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i<val1.length;i++) {
			String current = val1[i];
			current+=":";
			char[] value =  Integer.toString(val2[i]).toCharArray();
			for(int n = 0;n<value.length;n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i<val1.length-1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}

	public void render(Graphics g) {
		Graphics2D g2= (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,38));
		g.drawString(">Jogo do Vzão<", 170, 80);
		
		//opções de menu
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,24));
		if(pause==false) {
			g.drawString("Novo jogo", 255, 200);
		}else
			g.drawString("Resumir", 265, 200);
		g.setFont(new Font("arial",Font.BOLD,24));
		g.drawString("Carregar Jogo", 240, 250);
		g.setFont(new Font("arial",Font.BOLD,24));
		g.drawString("Sair", 290, 300);
		
		if(options[currentOption]=="Novo Jogo") {
			g.drawString(">", 240, 200);	
		}else if(options[currentOption]=="Carregar Jogo") {
			g.drawString(">", 220, 250);
		}else if(options[currentOption]=="Sair") {
			g.drawString(">", 270, 300);
		}
			
	}
	
}
