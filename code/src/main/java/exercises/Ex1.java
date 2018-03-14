package exercises;

import image.Image;
import image.ImageUtils;
import image.RGBA;

import java.io.IOException;

import rasterization.LineRasterizer;
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
//
		//Blatt 1, Aufgabe 6 Beispiele
		triangleRasterExample();
		generatePlasteredImage();

		//Blatt 1, Aufgabe 7 Beispiele
		//drawInterpolatedTriangles();
	}
	
	private void createAndSaveImage(){
		
		//TODO: Blatt1, Aufgabe 1
		Image<RGBA> zeichnen = new Image<RGBA>(200,200,new RGBA(0,0,0,0.7));
		for(int  i = 0;i<100;i++){
			zeichnen.set(20+i,30,new RGBA(0.2,0.34,0.70));
		}
		try {
			ImageUtils.write(zeichnen,"Aufgabe1.png");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(zeichnen.get(20,30));

		Image<RGBA> out = flipImageUpsideDown(zeichnen);
		try {
			ImageUtils.write(out,"Aufgabe1_2.png");
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	private Image<RGBA> flipImageUpsideDown(Image<RGBA> img){
		
		Image<RGBA> out = img.clone();
		for(int x = 0;x<img.cols();x++){
			for (int y = 0;y<img.rows();y++){
				RGBA rgba = img.get(x,y);
				out.set(x,(img.rows()-1)-y,rgba);
			}
		}
		//TODO: Blatt 1, Aufgabe 2

		return out;
	}
	
	private void lineRasterExample(){
		int width = 500;
		int height = 480;
		SimpleRenderer simpleRenderer = new SimpleRenderer(width, height, new ConstantColorShader());

		//Vector2[] vectors = {new Vector2(0,40),new Vector2(70,0)};
		//Vector2[] vectors = {new Vector2(0,0),new Vector2(70,40)};
		//Vector2[] vectors = {new Vector2(70,0),new Vector2(0,40)};
		//Vector2[] vectors = {new Vector2(70,40),new Vector2(0,0)};
		//Vector2[] vectors = {new Vector2(0,0),new Vector2(40,200)};
		//Vector2[] vectors = {new Vector2(70,150),new Vector2(70,10)};
		Vector2[] vectors = {new Vector2(100,230),new Vector2(490,500)};


		//TODO: Blatt 1, Aufgabe 4
		simpleRenderer.drawPlainLine(vectors,new RGBA(0.3,0.5,0.7));


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
		Vector2 vector1 = new Vector2(20,30);
		Vector2 vector2 = new Vector2(400,100);
		Vector2 vector3 = new Vector2(600,300);
		Vector2[] vectors = {vector1,vector2,vector3};
		RGBA[] colors = {new RGBA(0.3,0.5,0.7),new RGBA(0.3,0.5,0.7),new RGBA(0.3,0.5,0.7)};

		//TODO: Blatt 1, Aufgabe 6
		simpleRenderer.drawPlainTriangle(vectors,new RGBA(0.3,0.5,0.7));

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
