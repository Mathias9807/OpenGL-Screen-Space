package deferred;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class Deferred {
	
	private static boolean 	running = false;
	public static double 	time, delta;
	public static boolean	secondRender = false;
	public static final double tickRate = 1d / 60;
	
	public static void loop() {
		running = true;
		double resolution = Sys.getTimerResolution();
		double lastTime = Sys.getTime() / resolution;
		double nowTime = Sys.getTime() / resolution;
		double timer = 0, tickTimer = 0;
		int fps = 0, ticks = 0;
		while (!Display.isCloseRequested() && running) {
			nowTime = Sys.getTime() / resolution;
			delta = nowTime - lastTime;
			lastTime = nowTime;
			time += delta;
			timer += delta;
			tickTimer += delta;
			
			if (timer >= 1) {
				timer--;
				Display.setTitle("Fps: " + fps + ", Ticks: " + ticks);
				fps = 0;
				ticks = 0;
			}
			
			while (tickTimer > tickRate) {
				Panel.tick((float) tickRate);
				ticks++;
				tickTimer -= tickRate;
			}
			if (Display.isActive()) {
				Render.renderOpenGL();
				Display.update();
				Display.sync(24);
				fps++;
			}
			
			System.out.println(org.lwjgl.util.glu.GLU.gluErrorString(GL11.glGetError()));
		}
		Display.destroy();
	}

	public static void main(String[] args) {
		PixelFormat pixelFormat = new PixelFormat(8, 8, 0, 0);
		ContextAttribs context = new ContextAttribs(3, 2)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		try {
			Display.setDisplayMode(Display.getAvailableDisplayModes()[2]);
			Display.setVSyncEnabled(true);
			Display.setResizable(true);
			Display.setTitle("Deferred Reflections");
			Display.create(pixelFormat, context);
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Render.initOpenGL();
		loop();
		System.exit(0);
	}

}
