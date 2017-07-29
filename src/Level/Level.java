package Level;

import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import entities.Hitbox;
import entities.Player;
import graphics.Screen;
import graphics.Texture;

public class Level {
	// Declare shit
	Player player;
	Screen screen;
	Texture bg;
	ArrayList<Hitbox> onScreen = new ArrayList<Hitbox>();
	ArrayList<String> hitboxNumbers = new ArrayList<String>();
	ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	public int ScreenPosX = 0;

	public Level(Screen screen) {
		this.screen = screen;

		bg = new Texture("BG", "/sprites/bg.png", 1920, 540);
		try {
			loadLevel(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player = new Player(100, 100, 50, 50, "/sprites/xd.png", onScreen);
	}

	public void loadLevel(int level) throws IOException {
		// loads the level hitbox file
		FileReader in_file = new FileReader("src/sprites/level.lv");
		BufferedReader buff_in = new BufferedReader(in_file);
		String tempString = buff_in.readLine();
		while (tempString != null) {
			hitboxNumbers.add(tempString);
			tempString = buff_in.readLine();
		}
		for (int i = 0; i < hitboxNumbers.size(); i++) {
			StringTokenizer tempToken = new StringTokenizer(hitboxNumbers.get(i));
			hitboxes.add(new Hitbox(tempToken.nextToken(), tempToken.nextToken(), tempToken.nextToken(),
					tempToken.nextToken()));
		}

	}

	public void update() {
		player.update();
		for (Hitbox h : hitboxes) {
			if (onScreen(h.x)||onScreen(h.x+h.width)) {
				if (!onScreen.contains(h)) {
					onScreen.add(h);
					System.out.println("add");
				}
			} else {
				if (onScreen.contains(h)) {
					System.out.println("removed");
					onScreen.remove(h);
				}
			}
		}
	}

	public boolean onScreen(int i) {
		return (0 < i) && (i < 960);
	}

	public void render() {
		screen.drawTexture(ScreenPosX, 0, bg);
		player.render(screen);
		System.out.println(onScreen.size());
		for (Hitbox h : onScreen) {

			screen.drawRect(h.x, h.y, h.width, h.height, 0xff0000);
		}

	}

	public void advance(int xvel) {
		ScreenPosX -= xvel;
		for (Hitbox h : hitboxes) {
			h.shiftX(-xvel);
		}
	}

	public KeyListener getListener() {
		return player.getListener();
	}
}
