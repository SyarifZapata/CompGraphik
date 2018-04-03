package exercises;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import java.io.IOException;

import renderer.SimpleRenderer;
import shader.AdditiveColorShader;
import shader.ConstantColorShader;
import shader.InterpolatedColorShader;
import utils.Vector2;

public class Ex1{
	
	public static void main(String[] args){
		new Ex1();
	}
	

	public Ex1(){

		//Blatt 1, Aufgabe 1 Beispiele
		createAndSaveImage();
		
		//Blatt 1, Aufgabe 4
		lineRasterExample();
		
		//Blatt 1, Aufgabe 6 Beispiele
		triangleRasterExample();
		generatePlasteredImage();

		//Blatt 1, Aufgabe 7 Beispiele
		drawInterpolatedTriangles();
	}
	
	private void createAndSaveImage(){
		
		//TODO: Blatt1, Aufgabe 1
		
	}
	
	private Image<RGBA> flipImageUpsideDown(Image<RGBA> img){
		
		Image<RGBA> out = img.clone();
		
		//TODO: Blatt 1, Aufgabe 2

		return out;
	}
	
	private void lineRasterExample(){
		int width = 500;
		int height = 480;
		SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());
		
		//TODO: Blatt 1, Aufgabe 4

		try {
			String imgName = "lineRasterExample.png";
			ImageUtils.write(simpleRenderer.getImg(), imgName);
			System.out.println("Image created: "+imgName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void triangleRasterExample(){
		int width = 500;
		int height = 480;
		SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

		//TODO: Blatt 1, Aufgabe 6

		try {
			String imgName = "triangleRasterExample.png";
			ImageUtils.write(simpleRenderer.getImg(), imgName);
			System.out.println("Image created: "+imgName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generatePlasteredImage(){
		SimpleRenderer renderer = new SimpleRenderer(500, 480, new AdditiveColorShader());
		
		for(double x=0; x<500; x+=10){
			for(double y=0; y<480; y+=10){
				Vector2[] triangle = {new Vector2(x - 0.49, y - 0.49), new Vector2(x + 9.99, y - 0.9), new Vector2(x, y+9)};
				renderer.drawPlainTriangle(triangle, null);
				
				Vector2[] triangle2 = {new Vector2(x+9.25, y+0.75), new Vector2(x+9.49, y+10), new Vector2(x+0.51, y+9.4)};
				renderer.drawPlainTriangle(triangle2, null);
			}
		}		

		try {
			String imgName = "plastered.png";
			ImageUtils.write(renderer.getImg(), imgName);
			System.out.println("Image created: "+imgName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void drawInterpolatedTriangles(){
		SimpleRenderer renderer = new SimpleRenderer(500, 480, new InterpolatedColorShader());

		//TODO: Blatt 1, Aufgabe 7
		
		try {
			String imgName = "interpolated.png";
			ImageUtils.write(renderer.getImg(), imgName);
			System.out.println("Image created: "+imgName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
		
}
