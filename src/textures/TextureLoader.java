package textures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import deferred.Deferred;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

/**
 * A class for loading different types of OpenGL textures. 
 * @author Krynn
 * @author Mathias
 */

public class TextureLoader {
	
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    
    public static int loadTexture(BufferedImage image) {
    	ByteBuffer buffer = getTextureBuffer(image);
    	
    	// You now have a ByteBuffer filled with the color data of each pixel.
    	// Now just create a texture ID and bind it. Then you can load it using 
    	// whatever OpenGL method you want, for example:
    	
    	int textureID = glGenTextures(); //Generate texture ID
    	glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
    	
    	//Send texel data to OpenGL
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    	
    	//Return the texture ID so we can bind it later again
    	return textureID;
    }
    
    public static int loadCubeMap(BufferedImage[] images) {
    	ByteBuffer[] buffer = new ByteBuffer[6];
    	for (int i = 0; i < 6; i++) {
    		buffer[i] = getTextureBuffer(images[i]);
    	}
    	
    	// You now have a ByteBuffer filled with the color data of each pixel.
    	// Now just create a texture ID and bind it. Then you can load it using 
    	// whatever OpenGL method you want, for example:
    	
    	int textureID = glGenTextures(); //Generate texture ID
    	glBindTexture(GL_TEXTURE_CUBE_MAP, textureID); //Bind texture ID
    	
    	for (int i = 0; i < 6; i++) {
    		//Send texel data to OpenGL
    		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA8, 
    				images[i].getWidth(), images[i].getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer[i]);
    		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        	glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	}
    	
    	//Return the texture ID so we can bind it later again
    	return textureID;
    }
    
    private static ByteBuffer getTextureBuffer(BufferedImage image) {
    	int[] pixels = new int[image.getWidth() * image.getHeight()];
    	image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
    	
    	ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
    	
    	for(int y = 0; y < image.getHeight(); y++){
    		for(int x = 0; x < image.getWidth(); x++){
    			int pixel = pixels[y * image.getWidth() + x];
    			buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
    			buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
    			buffer.put((byte) (pixel & 0xFF));               // Blue component
    			buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
    		}
    	}
    	
    	buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
    	
		return buffer;
    }
    
    public static BufferedImage loadImage(String loc) {
    	try {
    		return ImageIO.read(Deferred.class.getResource(loc));
    	} catch (IOException e) {
    		//Error Handling Here
    	}
    	return null;
    }
}
