package entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import graphics.Screen;
import graphics.SpriteSheet;
import graphics.Texture;
import main.Main;

public class Player {
	int x, y, w, h, dir, xvel, yvel, movespeed;
	SpriteSheet sheet;
	Texture sprite;
	ArrayList<Hitbox> hitboxes;
	boolean inAir = false;
	private boolean checkYDone, checkXDone;

	public Player(int x, int y, int w, int h, String path, ArrayList<Hitbox> hitboxes) {
		movespeed = 10;
		this.hitboxes = hitboxes;
		sprite = new Texture("XD", path, w, h);
		xvel = 0;
		yvel = 1;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

	}

	public void render(Screen screen) {
		screen.drawTexture(x, y, sprite);
	}

	public void update() {
		// Collision Detect
		// Y detection
		Hitbox ybox = new Hitbox(x, y + yvel+1, w, h);
		// X Detection
		Hitbox xbox = new Hitbox(x + xvel, y, w, h);
		inAir = true;
		checkXDone = false;
		checkYDone = false;
		for (Hitbox h : hitboxes) {
			if (checkXDone && checkYDone) {
				break;
			}
			if (ybox.intersects(h)) {
				yvel = 0;
				y = (int) (h.y - this.h);
				inAir = false;
				checkYDone = true;
			}
			if (xbox.intersects(h)) {
				xvel = 0;
				checkXDone = true;
			}
		}
		if (inAir) {
			yvel++;
		}
		y += yvel;
		// Shifting Background
		if (x + xvel > 480) {
			Main.getInstance().getLevel().advance(xvel);
			for (Hitbox h : hitboxes) {
				h.shiftX(-xvel);
			}
		} else if (x + xvel < 0) {
			Main.getInstance().getLevel().advance(xvel);
			for (Hitbox h : hitboxes) {
				h.shiftX(-xvel);
			}
		} else {
			x += xvel;
		}
	}

	public void jump() {
		inAir = true;
		yvel = -10;
	}

	// Handling Key Inputs
	public KeyListener listener = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				xvel = -movespeed;
				dir = -1;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				xvel = +movespeed;
				dir = 1;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				jump();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (dir == -1) {
					xvel = 0;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (dir == 1) {
					xvel = 0;
				}
			}

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	};

	public int getXVel() {
		return xvel;
	}

	public KeyListener getListener() {
		return listener;
	}

}
