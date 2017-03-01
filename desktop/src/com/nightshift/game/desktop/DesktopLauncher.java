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
		config.width = 500;
		config.height = 500;

		new LwjglApplication(new NightShift(), config);
	}
}
