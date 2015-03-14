package deferred;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.*;

public class Panel {
	
	public static Vector3f pos = new Vector3f(0, 0, -3);
	public static Vector3f rot = new Vector3f();
	
	public static void tick(float delta) {
		float speed = 5 * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) pos.z += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) pos.z -= speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) pos.x += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) pos.x -= speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) pos.y += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) pos.y -= speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) rot.x += 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) rot.x -= 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) rot.y += 0.01f;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) rot.y -= 0.01f;
		
		Render.matrixView.setIdentity();
		Render.matrixView.rotate(rot.x, new Vector3f(1, 0, 0));
		Render.matrixView.rotate(rot.y, new Vector3f(0, 1, 0));
		Render.matrixView.translate(pos);
	}
	
}
