package exercises;

import exercises.ex4Tests.*;
import testSuite.VisualTestSuite;

public class Ex4TestSuite {

	public static void main(String args[]){
		
		String dir = "data/resources/ex4";
		
		new VisualTestSuite(
				new TeapotLambertTest(dir + "/LambertRenderer.png", "Lambert Renderer"),
				new TeapotPhongShadingTest(dir + "/PhongReflectanceModel.png", "Phong Reflectance Model"),
				new TeapotReflectanceLambert(dir + "/LambertRenderer.png", "Reflectance Renderer: Lambert", false),
				new TeapotReflectanceLambert(dir + "/ReflectanceRendererMultipleLightSources.png", "Reflectance Renderer: Lambert with multiple lightsources", true),
				new TeapotReflectanceCookTorranceTest(dir + "/Cook-Torrance.png", "Cook-Torrance: Lambert + Cook-Torrance"),
				new TeapotReflectanceOrenNayarTest(dir + "/OrenNayar.png", "OrenNayar"),
				new TeapotCookTorranceOrenNayar(dir + "/Cook-TorranceOrenNayar.png", "Cook-Torrance + OrenNayar")
				);
	}
}
