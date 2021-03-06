// Assignment: ISU
// Name: Max Luo and Dami Peng
// Date: June 22, 2022
// Description: an abstract class for character attributes

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.AlphaComposite;

public abstract class Character {

	// worldX & worldY are the positions of each respective character
	// each character has current hp, max hp, and dmg.
	// each character also have speed xVel, yVel
	protected int worldX, worldY, hp, maxHp, dmg;
	protected double speed, xVel, yVel;
	// draws a rectangle
	protected Rectangle player;

	// images for each frame sprite
	public BufferedImage left, right, idle_left, idle_right, left_w1, left_w2, left_w3, left_w4, right_w1, right_w2,
			right_w3, right_w4, left_atk, right_atk;
	public String direction;

	// attacking && hit detection
	boolean isAtk = false;
	boolean isHit = false;
	boolean invincible = false;
	boolean dead = false;
	int invincibleCount = 0;

	// for animation
	public int spriteCounter = 0;
	public int spriteNum = 1;

}