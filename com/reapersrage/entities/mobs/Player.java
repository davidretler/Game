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
	private int width;
	private int height;

        
        //Attributes of the player
        private int health;

        //Pixels for the player to move every update
        private double[] velocity = new double[2];
        //private final int START_SPEED = 5;
        //Maximum velocity
        private final double MAX_V;
        //How much to accelerate the player by key presses
        private final double acceleration;
        //How much the player slows down due to the map
        private final double friction;
        //If the player is about to hit a wall NORTH EAST SOUTH WEST
        private boolean[] wall = new boolean[4];
        

	public Player(int x, int y, int width, int height) {
		//Initial x position (pixels)
		this.x = x;
                //Initial y position (pixels)
		this.y = y;
                //Widith and height of player 
		this.width = width;
		this.height = height;
                //Velocty increase per update
                this.acceleration = .8;
                this.friction = 1;
                this.velocity = new double[]{0.0,0.0};
                this.MAX_V = 10.0;
                
                
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
                health = 1000;
	}

        //Updates the player direction from a list of possible directions
        //Array approach allows us to move diagnally
        //Array: {up, down, left, right}
        //Note to self: THE TOP RIGHT CORNER IS 0,0
        public void update(boolean[] dirs){
            parseInput(dirs);
            int xNew = x;
            int yNew = y;
            boolean[] collision = checkCollision(x, y, velocity);
            if(!collision[1]){
                //Down
                yNew = yNew + (int)velocity[1];
            }
            if(!collision[0]){
                //Right
                xNew = xNew + (int)velocity[0];
            }
            //If we've hit a wall, set the position to just touch the wall and set veloicty to 0
            if(wall[0]){
                //North
                yNew = 0;
                velocity[1] = 0;
            }
            else if(wall[1]){
                //East
                xNew = Game.getStaticWidth() - width;
                velocity[0] = 0;
            }
            else if(wall[2]){
                //South
                yNew = Game.getStaticHeight() - height;
                velocity[1] = 0;
            }
            else if(wall[3]){
                //West
                xNew = 0;
                velocity[0] = 0;
            }
            
            x = xNew;
            y = yNew;
        }
        
        //Parses the keypresses
        //If the key is pressed, increments the velocity by acceleration
        //velocity gets updated based on the inputs and acceleration
        public void parseInput(boolean[] dirs){
            //For each direction, check input
            if(dirs[0]){
                //Up
                if(-1.0*velocity[1] < MAX_V)
                    velocity[1] -= acceleration;
            }
            if(dirs[1]){
                //Down
                if(velocity[1] < MAX_V)
                    velocity[1] += acceleration;
            }
            if(dirs[2]){
                //Left
                if(-velocity[0] < MAX_V)
                    velocity[0] -= acceleration;
            }
            if(dirs[3]){
                //Right
                if(velocity[0] < MAX_V)
                    velocity[0] += acceleration;
            }
            //if not accelerating in y direction
            if(!dirs[0] && !dirs[1] && velocity[1] != 0){
                double newVelocity = velocity[1];
                newVelocity -= velocity[1] > 0 ? friction : -friction;
                if(velocity[1] > 0 && newVelocity < 0)
                    newVelocity = 0;
                else if(velocity[1] < 0 && newVelocity > 0)
                    newVelocity = 0;
                velocity[1] = newVelocity;
                 
            }
            //If not accelerating in x
            if(!dirs[2] && !dirs[3] && velocity[0] != 0){
                double newVelocity = velocity[0];
                newVelocity -= velocity[0] > 0 ? friction : -friction;
                if(velocity[0] < 0 && newVelocity > 0)
                    newVelocity = 0;
                else if(velocity[0] > 0 && newVelocity < 0)
                    newVelocity = 0;
                velocity[0] = newVelocity;
               
            }
            
        }

        //Checks for a collision in both x and y and return an array of booleans indicating such
        //for collisions with walls. Collisions with mobs is handeled by each individual mob
        public boolean[] checkCollision(int x, int y, double[] v){
            boolean[] collisions =  new boolean[]{false, false};            
            wall[0] = false;
            wall[1] = false;
            wall[2] = false;
            wall[3] = false;
            //WEST wall
            if(x + (int)v[0] < 0){
                collisions[0] = true;
                wall[3] = true;
            }
            //EAST wall
            else if(x + (int)v[0] + width > Game.getStaticWidth()){
                collisions[0] = true;
                wall[1] = true;
            }
            //NORTH wall
            if(y + (int)v[1] < 0){
                collisions[1] = true;
                wall[0] = true;
            }
            //SOUTH Wall
            else if(y + (int)v[1] + height > Game.getStaticHeight()){
                collisions[1] = true;
                wall[2] = true;
            }
            
            return collisions;
        }
        
        //Draws the player
	public void drawPlayer(Graphics2D g) {
		g.drawImage(RImage, x, y, null);
	}
	
        //Resizes the player (for buffs?)
	public BufferedImage resize(BufferedImage original, int scaledWidth, int scaledHeight)
			throws IOException {

		RImage = new BufferedImage(scaledWidth,
				scaledHeight, original.getType());

		Graphics2D g2d = RImage.createGraphics();
		g2d.drawImage(OImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		return RImage;
	}
        
        //Player's health
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
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        public int getVelX(){
            return (int)velocity[0];
        }
        public int getVelY(){
            return (int)velocity[1];
        }
}
