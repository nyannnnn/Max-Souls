// Assignment: ISU
// Name: Max Luo and Dami Peng
// Date: June 22, 2022
// Description: a class for player

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.AlphaComposite;

public class Max extends Character {

	// imports gamepanel and control handler
	GamePanel gp;
	KeyHandler keyH;

	// character attributes
	private double jumpSpeed;
	private double gravity;
	private boolean airborne;
	private boolean jumping = false;
	private int screenX;
	private int screenY;

	// player exclusive frames || enemies dont need hp or jump sprites
	BufferedImage left_up, right_up, heart, empty_heart;

	// Name: Max
	// Purpose: to make a max
	// Param: GamePanel, KeyHandler
	// Return: n/a
	public Max(GamePanel gp, KeyHandler keyH) {
		this.gp = gp; // import game panel to draw and update
		this.keyH = keyH; // import keyH to control

		// mmmmmm
		setDefaultValues();
		getMaxImg();
	}

	// Name: setDefaultValues
	// Purpose: easy access to player attributes
	// Param: n/a
	// Return: void
	public void setDefaultValues() {
		airborne = true;
		xVel = 0;
		yVel = 0;
		speed = 6;
		jumpSpeed = 20;
		gravity = 0.8;
		player = new Rectangle((int) (gp.tileSize * 0), (int) (gp.tileSize * 15), gp.tileSize * 2, gp.tileSize * 2);
		maxHp = 4;
		hp = maxHp;
		dmg = 1;
		// default sprite
		direction = "right";
		screenX = gp.screenX / 2 - (gp.tileSize / 2);
		screenY = gp.screenY / 2 - (gp.tileSize / 2);
	}

	// Name: getMaxImg
	// Purpose: initialize the sprites using buffered images
	// Param: n/a
	// Return: void
	public void getMaxImg() {
		try {
			// hp hearts
			heart = ImageIO.read(getClass().getResourceAsStream("/Heart/heart.png"));
			empty_heart = ImageIO.read(getClass().getResourceAsStream("/Heart/heart_empty.png"));
			// sprites of character
			left = ImageIO.read(getClass().getResourceAsStream("/max/max_left.png"));
			right = ImageIO.read(getClass().getResourceAsStream("/max/max_right.png"));
			left = ImageIO.read(getClass().getResourceAsStream("/max/max_left.png"));
			right = ImageIO.read(getClass().getResourceAsStream("/max/max_right.png"));
			left_w1 = ImageIO.read(getClass().getResourceAsStream("/max/max_leftwalk1.png"));
			left_w2 = ImageIO.read(getClass().getResourceAsStream("/max/max_leftwalk2.png"));
			left_w3 = ImageIO.read(getClass().getResourceAsStream("/max/max_leftwalk3.png"));
			left_w4 = ImageIO.read(getClass().getResourceAsStream("/max/max_leftwalk4.png"));
			right_w1 = ImageIO.read(getClass().getResourceAsStream("/max/max_rightwalk1.png"));
			right_w2 = ImageIO.read(getClass().getResourceAsStream("/max/max_rightwalk2.png"));
			right_w3 = ImageIO.read(getClass().getResourceAsStream("/max/max_rightwalk3.png"));
			right_w4 = ImageIO.read(getClass().getResourceAsStream("/max/max_rightwalk4.png"));
			left_up = ImageIO.read(getClass().getResourceAsStream("/max/max_leftjump.png"));
			right_up = ImageIO.read(getClass().getResourceAsStream("/max/max_rightjump.png"));
			left_atk = ImageIO.read(getClass().getResourceAsStream("/max/max_leftatk.png"));
			right_atk = ImageIO.read(getClass().getResourceAsStream("/max/max_rightatk.png"));

		} catch (FileNotFoundException e) {
			System.out.println("Where file");
		} catch (IOException e) {
			System.out.println("Why");
		}
	}

	// Name: move
	// Purpose: check for player movement
	// Param: n/a
	// Return: void
	public void move() {
		// attack
		if (keyH.attack && xVel == 0) {
			isAtk = true;
			if (direction.equals("idle_l")) {
				direction = "left_atk";
			} else if (direction.equals("idle_r")) {
				direction = "right_atk";
			}
		}
		// idle
		else if (!keyH.attack) {
			isAtk = false;
			if (direction.equals("left_atk")) {
				direction = "idle_l";
			} else if (direction.equals("right_atk")) {
				direction = "idle_r";
			}
		}
		// move left
		if (keyH.left) {
			if (xVel >= -3) {
				xVel -= speed;
			}
			direction = "left";
			// move right
		} else if (keyH.right) {
			if (xVel <= 3) {
				xVel += speed;
			}
			direction = "right";
		}
		// if not moving left or right set horizontal vel to 0
		else {
			xVel = 0;
		}
		// if not in air
		if (!airborne) {
			if (direction.equals("left_up") || direction.equals("idle_l")) {
				direction = "left";
			} else if (direction.equals("right_up") || direction.equals("idle_r")) {
				direction = "right";
			}
		}
		// if in air
		if (airborne) {
			if (jumping) {
				if (keyH.right || direction.equals("idle_r")) {
					direction = "right_up";
				} else if (keyH.left || direction.equals("idle_l")) {
					direction = "left_up";
				}
			}
			yVel -= gravity;
		} else {
			// jumping
			yVel = 0;
			if (keyH.jump) {
				gp.soundEffect(5);
				// sides of jump
				jumping = true;
				airborne = true;
				yVel = jumpSpeed;
			} else {
				jumping = false;
			}
		}

		// idle frame || left & right
		if (xVel == 0) {
			if (direction.equals("left")) {
				direction = "idle_l";
			} else if (direction.equals("right")) {
				direction = "idle_r";
			}
		}

		// update player location
		player.x += xVel;
		player.y -= yVel;

		// animation
		spriteCounter++;
		if (spriteCounter > 10) {
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 3;
			} else if (spriteNum == 3) {
				spriteNum = 4;
			} else if (spriteNum == 4) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}

		// if u get hit then turn invincible
		if (invincible == true) {
			invincibleCount++;
			if (invincibleCount > 80) {
				invincible = false;
				invincibleCount = 0;
			}
		}
	}

	// Name: draw
	// Purpose: draw the character sprites
	// Param: Graphics2D
	// Return: void
	public void draw(Graphics2D g2) {

		int hp_X = gp.tileSize / 2;
		int hp_Y = gp.tileSize / 2;

		int i = 0;

		// missing hp
		while (i < gp.max.maxHp) {
			g2.drawImage(empty_heart, hp_X, hp_Y, gp.tileSize, gp.tileSize, null);
			i++;
			hp_X += gp.tileSize;
		}

		// reset variables
		hp_X = gp.tileSize / 2;
		hp_Y = gp.tileSize / 2;
		i = 0;

		while (i < gp.max.hp) {
			g2.drawImage(heart, hp_X, hp_Y, gp.tileSize, gp.tileSize, null);
			i++;
			hp_X += gp.tileSize;
		}

		// initialize image

		BufferedImage image = null;
		// checks for left
		if (direction.equals("left")) {
			if (spriteNum == 1)
				image = left_w1;
			if (spriteNum == 2)
				image = left_w4;
			if (spriteNum == 3)
				image = left_w2;
			if (spriteNum == 4)
				image = left_w3;

		}
		// checks for right
		else if (direction.equals("right")) {
			if (spriteNum == 1)
				image = right_w1;
			if (spriteNum == 2)
				image = right_w4;
			if (spriteNum == 3)
				image = right_w2;
			if (spriteNum == 4)
				image = right_w3;
		}
		// checks for right jump
		else if (direction.equals("right_up")) {
			image = right_up;
		}
		// checks for left jump
		else if (direction.equals("left_up")) {
			image = left_up;
		}
		// checks for idle
		else if (direction.equals("idle_l")) {
			image = left;
		} else if (direction.equals("idle_r")) {
			image = right;
		} else if (direction.equals("left_atk")) {

			image = left_atk;

		} else if (direction.equals("right_atk")) {

			image = right_atk;
		}

		// debug
		if (image == null) {
			System.out.println("null");
		}

		int x = screenX;
		int y = screenY;
		if (screenX > player.x)
			x = player.x;
		if (screenY > player.y)
			y = player.y;

		int bottomOffSet = gp.screenY - screenY;
		if (bottomOffSet > gp.worldHeight - player.y) {
			y = gp.screenY - (gp.worldHeight - player.y);
		}

		// turns invisible when hit
		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}

		// fixes character sprite when attack || x dimension is different
		if (direction.equals("left_atk")) {
			g2.drawImage(image, x - gp.tileSize, y, gp.tileSize * 3, gp.tileSize * 2, null);
		} else if (direction.equals("right_atk")) {
			g2.drawImage(image, x, y, gp.tileSize * 3, gp.tileSize * 2, null);
			// not attacks
		} else {
			g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null);
		}

		// reset invisibility for contacting enemies
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}

	// Name: checkCollision
	// Purpose: checks the collision around player with blocks
	// Param: Tile object
	// Return: boolean value of whether the block you collide with is a block that
	// is actually collidable
	public boolean checkCollision(Tile t) {
		// initalization
		Rectangle block = t.getHitbox();
		if (player.intersects(block)) {
			// coordinate attributes
			double left1 = player.getX();
			double right1 = player.getX() + player.getWidth();
			double top1 = player.getY();
			double bottom1 = player.getY() + player.getHeight();
			double left2 = block.getX();
			double right2 = block.getX() + block.getWidth();
			double top2 = block.getY();
			double bottom2 = block.getY() + block.getHeight();

			// check collision from left side of the block
			if (right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
				if (t.isCollision()) {
					player.x = block.x - player.width;
					return true;
				}
			}
			// check collision from right side of the block
			else if (left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2
					&& right2 - left1 < bottom2 - top1) {
				if (t.isCollision()) {
					player.x = block.x + block.width;
					return true;
				} else if (t.getTileN() == 13 || t.getTileN() == 14 || t.getTileN() == 15 || t.getTileN() == 16) {
					gp.stopSound(1);
					gp.changeWord = true;
					return true;
				}
			}
			// check collision from top side of the block
			else if (bottom1 > top2 && top1 < top2) {
				if (t.isCollision()) {
					airborne = false;
					yVel = 0;
					player.y = block.y - player.height;
					return true;
				} else if (t.getTileN() == 8) {
					System.out.println(true);
					dead = true;
					return true;
				} else if (t.getTileN() == 13 || t.getTileN() == 14 || t.getTileN() == 15 || t.getTileN() == 16) {
					gp.changeWord = true;
					return true;
				}
			}
			// check collsion from bottom side of the block
			else if (top1 < bottom2 && bottom1 > bottom2) {
				if (t.isCollision()) {
					airborne = true;
					player.y = block.y + block.height;
				} else
					airborne = true;
			}
		}

		// check if the collision is within the ground
		if ((player.y + gp.tileSize * 2) >= gp.tileSize * 17) {
			airborne = false;
		}
		return false;

	}

	// Name: checkTylerCollision
	// Purpose: check if the player comes in contact with tyler
	// Param: Tyler object
	// Return: void
	public void checkTylerCollision(Tyler tyler) {
		// initalization of attributes
		Rectangle ty = tyler.player;
		double left1 = player.getX();
		double right1 = player.getX() + player.getWidth();
		double top1 = player.getY();
		double bottom1 = player.getY() + player.getHeight();
		double left2 = ty.getX();
		double right2 = ty.getX() + ty.getWidth();
		double top2 = ty.getY();
		double bottom2 = ty.getY() + ty.getHeight();

		if (player.intersects(ty)) {
			// if the player comes in contact with tyler from the left
			if (right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
				player.x = ty.x - player.width;
				tyler.direction = "left_atk";

			}
			// if the player comes in contact with tyler from the right
			else if (left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2
					&& right2 - left1 < bottom2 - top1) {
				player.x = ty.x + ty.width;
				tyler.direction = "right_atk";

			}
			// if get hit, then go invincible
			if (invincible == false) {
				gp.soundEffect(2);
				gp.soundEffect(4);
				hp--;
				invincible = true;
			}
			// if max dies by tyler
			if (hp <= 0) {
				gp.startDeath = System.currentTimeMillis();
				gp.soundEffect(8);
				gp.soundEffect(5);
				gp.soundEffect(7);
				dead = true;
			}
		}
	}

	// Name: checkWongCollision
	// Purpose: check if the player comes in contact with wong
	// Param: Wong object
	// Return: void
	public void checkWongCollision(Wong wong) {
		// initalization of attributes (got lazy and didnt want to change rectangle name
		// hehe)
		Rectangle ty = wong.player;
		double left1 = player.getX();
		double right1 = player.getX() + player.getWidth();
		double top1 = player.getY();
		double bottom1 = player.getY() + player.getHeight();
		double left2 = ty.getX();
		double right2 = ty.getX() + ty.getWidth();
		double top2 = ty.getY();
		double bottom2 = ty.getY() + ty.getHeight();

		if (player.intersects(ty)) {
			// if the player comes in contact with tyler from the left
			if (right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
				player.x = ty.x - player.width;

			}
			// if the player comes in contact with tyler from the right
			else if (left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2
					&& right2 - left1 < bottom2 - top1) {
				player.x = ty.x + ty.width;

			}
			// decrease hp
			if (invincible == false) {
				gp.soundEffect(10);
				gp.soundEffect(2);
				hp--;
				invincible = true;
			}
			// when dies to ms wong
			if (hp <= 0) {
				gp.soundEffect(8);
				gp.soundEffect(5);
				dead = true;
			}
		}
	}

	// Purpose: checks wong projectile
	// Param: n/a
	// Return: void
	public void checkProjCollision(Rectangle proj) {
		if (player.intersects(proj)) {
			// when hit by projectile
			if (invincible == false) {
				gp.soundEffect(10);
				gp.soundEffect(2);
				hp--;
				invincible = true;
			}
			// when die by ms wong
			if (hp <= 0) {
				gp.soundEffect(8);
				gp.soundEffect(5);
				dead = true;
			}
		}
	}

	// Purpose: keep player in bound
	// Param: n/a
	// Return: void
	public void keepInBound() {
		// check x bound
		if (player.x < 0) {
			player.x = 0;
		}

		if (player.x > 8064 - 16 * 3 * 13) {
			player.x = 8064 - 16 * 3 * 13;
		}

		// check y bound
		if (player.y < 0) {
			player.y = 0;
			yVel = 0;
		} else if (player.y > gp.screenY - player.height) {
			player.y = gp.screenY - player.height;
			airborne = false;
			yVel = 0;
		}
	}

	// setters and getters
	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public boolean isAirborne() {
		return airborne;
	}

	public void setAirborne(boolean airborne) {
		this.airborne = airborne;
	}

}

// spsss this is a hidden easter egg. i love you ding kai peng
// pspsppspss second easter egg || max pls do somthething :)