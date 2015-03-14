package deferred;

import static shaders.Shaders.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.*;

import textures.Textures;

public class Light {
	
	public static final int SHADER_LIGHT_PARAM_OFFS = 16;
	
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
	public Vector4f pos;
	public Vector3f brightness;
	public boolean  isActive;
	
	private boolean	usesShadowMap;
	public FBO		shadowMap;
	
	public Light() {
		pos = new Vector4f();
		brightness = new Vector3f();
		isActive = false;
	}
	
	public void createShadowMap(int width, int height) {
		if (usesShadowMap) return;
		
		shadowMap = new FBO(width, height).withDepthTexture();
		usesShadowMap = true;
	}
	
	public static void update() {
		for (int i = 0; i < lights.size(); i++) {
			Light l = lights.get(i);
			setParam1i("light[" + i + "].isActive", l.isActive ? 1 : 0);
			setParam4f("light[" + i + "].pos", l.pos.x, l.pos.y, l.pos.z, l.pos.w);
			setParam3f("light[" + i + "].brightness", l.brightness.x, l.brightness.y, l.brightness.z);
			
			Textures.bind(lights.get(i).shadowMap.getDepthTextureId(), SHADER_LIGHT_PARAM_OFFS + i);
			setParam1i("light[" + i + "].usesShadowMap", l.usesShadowMap ? 1 : 0);
			setParam1i("light[" + i + "].shadowMap", SHADER_LIGHT_PARAM_OFFS + i);
		}
	}
	
	public static void updateShadowMaps() {
		for (int i = 0; i < lights.size(); i++) {
			FBO shadowMap = lights.get(i).shadowMap;
			
			
		}
	}

}
