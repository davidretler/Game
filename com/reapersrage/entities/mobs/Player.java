package com.reapersrage.entities.mobs;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.reapersrage.game.Game;
import com.reapersrage.gfx.GameTile;
import com.reapersrage.gfx.Screen;
import com.reapersrage.input.Keyboard;

/**
 * Created with IntelliJ IDEA. User: Soulevoker Date: 10/27/13 Time: 1:24 PM
 * Copyright © Reapers' Rage 2013
 */
public class Player {
	private int x;
	private int y;
	//O = original R = resized
	private BufferedImage OImage;
	private BufferedImage RImage;
	private int dir;
	private int width;
	private int height;
        
        //Attributes of the player
        private int health;

	public Player(int x, int y, int width, int height) {
		dir = 0;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		try {
			OImage = ImageIO
					.read(GameTile.class
							.getResourceAsStream("/com/reapersrage/res/textures/jim.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			resize(OImage,this.width,this.height);
		} catch (IOException e) {
			e.printStackTrace();
		}
                
                //sets initial health
                health = 100;
	}

	public void update(int dir) {
		switch (dir) {
		case 1:
			y += 5;
			break;
		case 2:
			y -= 5;
			break;
		case 3:
			x -= 5;
			break;
		case 4:
			x += 5;
			break;

		default:

			break;

		}

	}
        //Updates the player direction from a list of possible directions
        //Array approach allows us to move diagnally
        //Array: {up, down, left, right}
        //Note to self: THE TOP RIGHT CORNER IS 0,0
        public void update(boolean[] dirs){
            if(dirs[0]){
                //Up
                y -= 5;
            }
            if(dirs[1]){
                //Down
                y += 5;
            }
            if(dirs[2]){
                //Left
                x -= 5;
            }
            if(dirs[3]){
                //Right
                x += 5;
            }
            
        }

	public void drawPlayer(Graphics2D g) {
		g.drawImage(RImage, x, y, null);
	}
	

	public BufferedImage resize(BufferedImage original, int scaledWidth, int scaledHeight)
			throws IOException {

		RImage = new BufferedImage(scaledWidth,
				scaledHeight, original.getType());

		Graphics2D g2d = RImage.createGraphics();
		g2d.drawImage(OImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		return RImage;
	}
        
        public int getHealth(){
            return health;
        }
        
        //changes players health. Negative lowers health (damage)
        public void changeHealth(int change) {
            health += change;
        }
        
        //get the position
         public int getX(){
             return x;
        }
        public int getY(){
            return y;
        }
        

}
