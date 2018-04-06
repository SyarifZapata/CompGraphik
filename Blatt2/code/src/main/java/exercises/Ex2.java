package exercises;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.convolution.*;
import image.processing.scaling.*;
import image.processing.warping.BackwardWarp;
import image.processing.warping.ForwardWarp;
import image.processing.warping.Morphing;

import java.io.File;
import java.io.IOException;

public class Ex2 {

	static final String outPath = "data/ex2/";

	public static void main(String[] args) {
		prepareOutput(outPath);
		
		//Blatt 2 Aufgabe 1 Beispiele
		blatt2Aufgabe1();
		
		//Blatt 2 Aufgabe 2 Beispiele
		blatt2Aufgabe2();
		
		//Blatt 3 Aufgabe 3 Beispiele
		blatt2Aufgabe3();
		
	}
	public static void prepareOutput(String outPath){
		File outDir = new File(outPath); 
		if( ! outDir.exists()){
			outDir.mkdirs();
		}
		
	}
	
	public static void blatt2Aufgabe1(){
		applyKernel(new MeanConvolution(5), outPath + "meanfilter.png", false);

		long start = System.nanoTime();
		applyKernel(new GaussConvolution(5, 2.0), "data/ex2/gaussfilter.png", false);
		long elapsedTime = System.nanoTime()-start;
		System.out.println("2D Gauss Performance: " + String.valueOf(elapsedTime/1000000000.0) + " seconds");

		start = System.nanoTime();
		applyKernel(new GaussSeparableConvolution(5, 2.0), "data/ex2/gaussfiltered_separable.png", false);
		elapsedTime = System.nanoTime()-start;
		System.out.println("1D Gauss Performance: " + String.valueOf(elapsedTime/1000000000.0) + " seconds");

		applyKernel(new LaplaceConvolution(), "data/ex2/laplacefilter.png", true);
		applyKernel(new SobelHConvolution(), "data/ex2/sobelfilter.png", true);
	}
	
	public static void blatt2Aufgabe2(){
		applyKernel(new NNDownsampling(), "data/ex2/nnDownsampling.png", false);
		
		applyKernel(new GaussianDownsampling(), "data/ex2/GaussianDownsampling.png", false);
		
		applyKernel(new Upsampling(new NNInterpolation()),  "data/ex2/nnUpsampling.png", false);
		
		applyKernel(new Upsampling(new BiLinearInterpolation()), "data/ex2/linearUpsampling.png", false);

		applyKernel(new Upsampling(new BiCubicInterpolation()), "data/ex2/BiCubicUpsampling.png", false);
	}
	
	public static void blatt2Aufgabe3(){
		
		backwardWarp();
		
		forwardWarp();
		
		//morph();
	}

	
	public static void applyKernel(ImageAlgorithm algorithm, String outName, Boolean normalize){
		try {
			Image<RGBA> original = ImageUtils.read("data/resources/cat.png");
			
			Image<RGBA> filtered = algorithm.perform(original);
			
			if(normalize){
				ImageUtils.normalize(filtered);
			}
			
			ImageUtils.write(filtered, outName);
			System.out.println("Image created: " + outName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void backwardWarp(){
		try{
			Image<RGBA> img = ImageUtils.read("data/resources/thomas.png");
			
			BackwardWarp warp = new BackwardWarp();
			double strength = 3d;
			int radius = 500;
			warp.createSwirlField(img.cols(), img.rows(), radius, strength);
			Image<RGBA> warped = warp.perform(img);
			
			String outName = "data/ex2/backwardwarp.png";
			ImageUtils.write(warped, outName);
			System.out.println("Image created: " + outName);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void forwardWarp(){
		try{
			Image<RGBA> img = ImageUtils.read("data/resources/thomas.png");
			
			int wavelength = 100;
			ForwardWarp warp = new ForwardWarp();
			warp.createRipples(img.cols(), img.rows(), wavelength);
			Image<RGBA> warped = warp.perform(img);

			String outName = "data/ex2/forwardwarp.png";
			ImageUtils.write(warped, outName);
			System.out.println("Image created: " + outName);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void morph(){
		try{
			Image<RGBA> anette = ImageUtils.read("data/resources/anette.png");
			Image<RGBA> thomas = ImageUtils.read("data/resources/thomas.png");
			
			for(float lambda = -0.4f; lambda <= 1.4; lambda += 0.2){
				Morphing morpher = new Morphing(thomas, anette, "data/resources/t_a.f2image", "data/resources/a_t.f2image");
				Image<RGBA> morphed = morpher.morph(lambda);
				String outName = "data/ex2/thomas_to_anette_"+lambda+".png";
				ImageUtils.write(morphed, outName);
				System.out.println("Image created: " + outName);
				
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

