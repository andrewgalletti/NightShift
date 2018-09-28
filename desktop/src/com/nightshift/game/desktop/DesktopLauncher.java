package com.nightshift.game.desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nightshift.game.NightShift;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.title = "Night Shift";
		//The dimensions of the map. When we change map size, change these
		config.width = screenSize.width;
		config.height = screenSize.height;
		new LwjglApplication(new NightShift(), config);
	}
}