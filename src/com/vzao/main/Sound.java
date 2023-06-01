package com.vzao.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	
	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound ("/theme.wav");
	public static final Sound playerHurt = new Sound("/hurt.wav");
	public static final Sound bulletSound= new Sound("/Bullet.wav");
	public static final Sound shootSound = new Sound("/shoot.wav");
	public static final Sound lifeSound = new Sound("/life.wav");
	public static final Sound pickGun = new Sound("/pickgun.wav");
	public static final Sound enemyHurt = new Sound("/enemyhurt.wav");
	public static final Sound bossHurt = new Sound("/bosshurt.wav");
	public static final Sound menuChoice = new Sound("/menuchoice.wav");
	public static final Sound gameOver= new Sound("/gameover.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e) {}
		
		
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e) {}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() { 
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {}
	}
	


	
		
		
	

}
