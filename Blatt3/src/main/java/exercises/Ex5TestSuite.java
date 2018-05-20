package exercises;

import exercises.ex5Tests.*;
import testSuite.VisualTestSuite;

public class Ex5TestSuite {
	
	public static void main(String args[]){
		
		String dir = "data/resources/ex5";
		
		new VisualTestSuite(
				//Phong
				new PhongTest(dir + "/PhongTest.png", "Phong: Rendering"),
				new PhongFromLight(dir + "/PhongFromLight.png", "Phong: Directon of LightSource"),
				new PhongShadowMap(dir + "/ShadowMap.png", "Phong: ShadowMap"),
				new PhongShadow(dir + "/PhongShadow.png", "Phong: Shadow Renderer"),
				
				//Reflecance
				new ReflectanceLambert(dir + "/ReflectanceTest.png", "Reflectance: Rendering"),
				new ReflectanceLambertFromLight(dir  + "/DirectonOfLightSource.png", "Reflectance: Direction of LightSource"),
				new ReflectanceShadowMap(dir + "/ShadowMap.png", "Reflectance: ShadowMap"), //unnötig
				new ReflectanceShadow(dir + "/ReflectanceShadow.png", "Reflectance: Shadow Renderer"),

				//Soft Shadows
				new SoftShadowPhongRenderer(dir + "/SoftShadowPhong.png", "Soft Shadow: Phong Renderer"),
				new SoftShadowReflectanceRenderer(dir + "/SoftShadowReflectance.png", "Soft Shadow: Reflectance Renderer"),
				
				//Soft Shadow Extended
				new SoftShadowReflectanceExtended(dir + "/SoftShadowExtended.png", "Soft Shadow: Cook-Torrance + OrenNayar")
				);

		
	}

}
