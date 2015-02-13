package deferred;

import static shaders.Shaders.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
	public Vector3f pos;
	public Vector3f diffuse, specular;
	public float 	quadratic;
	public boolean  isActive;
	
	public Light() {
		pos = new Vector3f();
		diffuse = new Vector3f();
		quadratic = 0;
		isActive = false;
	}
	
	public static void update() {
		for (int i = 0; i < lights.size(); i++) {
			Light l = lights.get(i);
			setParam1i("light[" + i + "].isActive", l.isActive ? 1 : 0);
			setParam1f("light[" + i + "].quadratic", l.quadratic);
			setParam3f("light[" + i + "].pos", l.pos.x, l.pos.y, l.pos.z);
			setParam3f("light[" + i + "].diffuse", l.diffuse.x, l.diffuse.y, l.diffuse.z);
			setParam3f("light[" + i + "].specular", l.specular.x, l.specular.y, l.specular.z);
		}
	}

}
