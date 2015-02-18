package deferred;

import static shaders.Shaders.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
	public Vector3f pos;
	public Vector3f brightness;
	public boolean  isActive;
	
	public Light() {
		pos = new Vector3f();
		brightness = new Vector3f();
		isActive = false;
	}
	
	public static void update() {
		for (int i = 0; i < lights.size(); i++) {
			Light l = lights.get(i);
			setParam1i("light[" + i + "].isActive", l.isActive ? 1 : 0);
			setParam3f("light[" + i + "].pos", l.pos.x, l.pos.y, l.pos.z);
			setParam3f("light[" + i + "].brightness", l.brightness.x, l.brightness.y, l.brightness.z);
		}
	}

}
