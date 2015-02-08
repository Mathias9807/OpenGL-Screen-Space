package deferred;

import static deferred.FBO.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static shaders.Shaders.*;
import static textures.Textures.*;

import java.nio.FloatBuffer;

import models.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;

import textures.Textures;

public class Render {
	
	private static int fov = 60;
	
	private static FBO FBOData, FBOBackFaces, FBORender;

	private static VAO[] VAOArray = new VAO[8];
	
	public static Matrix4f matrixProj, matrixView, matrixModel;
	
	public static void initOpenGL() {
		// Load models
		Model.loadModels();
		
		// Load framebuffers
		FBOData 		= new FBO(Display.getWidth(), Display.getHeight()).withColor(3).withDepth();
		FBORender 		= new FBO(Display.getWidth(), Display.getHeight()).withColor(3);
		FBOBackFaces 	= new FBO(Display.getWidth(), Display.getHeight()).withDepthTexture();
		
		// Put models into vertex array objects
		VAOArray = VAO.createVAOArray();

		// Load textures
		loadTextures();
		
		// Create matrices
		float nearP  = 0.1f;
		float farP   = 100;
		float tweenP = farP - nearP;
		float sumP   = farP + nearP;
		float aspect = (float) Display.getWidth() / Display.getHeight();
		
		matrixProj = new Matrix4f();
		matrixProj.setIdentity();
		matrixProj.m00 = (float) (1 / Math.tan(fov / 360d * Math.PI)) / aspect; // fov / 2 / 180 = fov / 360
		matrixProj.m11 = (float) (1 / Math.tan(fov / 360d * Math.PI));
		matrixProj.m22 = -sumP / tweenP;
		matrixProj.m23 = -1;
		matrixProj.m32 = -2 * nearP * farP / tweenP;
		matrixProj.m33 = 0;
		matrixView = new Matrix4f();
		matrixView.setIdentity();
		matrixModel = new Matrix4f();
		matrixModel.setIdentity();
		
		// Load shader uniforms
		setShader(SHADERSplitter);
		setParam1i("texture0", 0);
		useMatrix(matrixProj,  "proj");
		useMatrix(matrixView,  "view");
		useMatrix(matrixModel, "model");
		
		setShader(SHADERDepth);
		useMatrix(matrixProj,  "proj");
		useMatrix(matrixView,  "view");
		useMatrix(matrixModel, "model");
		
		setShader(SHADERLighting);
		setParam1i("texture0", 0);
		setParam1i("texture1", 1);
		useMatrix(matrixProj,  "proj");
		useMatrix(matrixView,  "view");
		
		setShader(SHADERPost);
		setParam1i("texture0", 0);
		setParam1i("texture1", 1);
		setParam1i("texture2", 2);
		useMatrix(matrixProj,  "proj");
		useMatrix(matrixView,  "view");
		
		// Set additional OpenGL states
		glEnable(GL_DEPTH_TEST);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glClearColor(0, 0, 0, 0);
	}
	
	
	public static void renderOpenGL() {
		setFrameBuffer(FBOData);
		setShader(SHADERSplitter);
		useMatrix(matrixView, "view");
		bind(Textures.texID[0], 0);
		
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
		
		renderScene();
		
		
		setFrameBuffer(FBOBackFaces);
		setShader(SHADERDepth);
		useMatrix(matrixView, "view");
		
		glClear(GL_DEPTH_BUFFER_BIT);
		
		glCullFace(GL_FRONT);
		renderScene();
		glCullFace(GL_BACK);
		

		setFrameBuffer(FBORender);
		setShader(SHADERLighting);
		useMatrix(matrixView, "view");
		bind(FBOData.getTexturesId()[0], 0);
		bind(FBOData.getTexturesId()[1], 1);
		
		glDisable(GL_DEPTH_TEST);
		VAOArray[0].render();
		glEnable(GL_DEPTH_TEST);
		
		
		setFrameBuffer(null);
		setShader(SHADERPost);
		useMatrix(matrixView, "view");
		useMatrix(Matrix4f.invert(matrixProj, null), "projInv");
		bind(FBORender.getTexturesId()[0], 0);
		bind(FBORender.getTexturesId()[1], 1);
		bind(FBOBackFaces.getDepthTextureId(), 2);

		glDisable(GL_DEPTH_TEST);
		VAOArray[0].render();
		glEnable(GL_DEPTH_TEST);
	}
	
	private static void renderScene() {
		matrixModel.setIdentity();
		matrixModel.translate(new Vector3f(0, -1, 0));
		matrixModel.rotate((float) Deferred.time * 0.1f, new Vector3f(0, 1, 0));
		matrixModel.scale(new Vector3f(0.2f, 0.2f, 0.2f));
		useMatrix(matrixModel, "model");
		VAOArray[1].render();
		
		/*matrixModel.setIdentity();
		matrixModel.translate(new Vector3f(0, -1, 0));
		matrixModel.rotate((float) -Math.PI / 2, new Vector3f(1, 0, 0));
		matrixModel.scale(new Vector3f(4, 4, 4));
		useMatrix(matrixModel, "model");
		VAOArray[0].render();*/
	}
	
	public static void useMatrix(Matrix4f m, String var) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		m.store(buffer);
		buffer.flip();
		setParam4m(var, buffer);
	}
	
}
