package se.yrgo.jumpybirb;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import se.yrgo.jumpybirb.JumpyBirb;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(600, 800);
		config.setResizable(true);
		config.setTitle("Jumpy_Birb");
		new Lwjgl3Application(new JumpyBirb(), config);
	}
}
