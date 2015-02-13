package textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.GL_COMPARE_REF_TO_TEXTURE;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import org.lwjgl.opengl.*;

public class Textures {
	
	public static int[] texID;
	
	private static final String TEXTURE_FILE = "/textures/";
	
	
	public static void loadTextures() {
		ArrayList<Integer> l = new ArrayList<Integer>();
		{
			l.add(load("wall.png"));
			l.add(load("Bake.png"));
		}
		Object[] transfer = l.toArray();
		texID = new int[transfer.length];
		for (int i = 0; i < transfer.length; i++) {
			texID[i] = (Integer) transfer[i];
		}
	}
	
	private static int load(String path, int filter, int edgeWrapping) {
		int t;
		t = TextureLoader.loadTexture(TextureLoader.loadImage(TEXTURE_FILE + path));
    	
    	//Setup wrap mode
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, edgeWrapping);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, edgeWrapping);
    	
    	//Setup texture scaling filtering
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

		return t;
	}
	
	private static int load(String path) {
		return load(path, GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
	}
	
	private static int loadCubeMap(BufferedImage[] images) {
		int t;
		t = TextureLoader.loadCubeMap(images);
    	
		return t;
	}
	
	public static void bind(int t, int loc) {
		glActiveTexture(GL_TEXTURE0 + loc);
		glBindTexture(GL_TEXTURE_2D, t);
	}
	
	public static void bindCubeMap(int t, int loc) {
		glActiveTexture(GL_TEXTURE0 + loc);
		glBindTexture(GL_TEXTURE_CUBE_MAP, t);
	}

}
