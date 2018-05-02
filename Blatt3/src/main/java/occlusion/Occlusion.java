package occlusion;

import illumination.PointLight;
import image.Image;
import mesh.Mesh;
import projection.PinholeProjection;
import projection.Projection;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import utils.Matrix4;
import utils.Vector3;

public class Occlusion {
	
	public Image<Correspondence> shadowMap;
	private PinholeProjection shadowProjection;
	
	private int height, width;
	private double shadowBias;
	private int pcfMask;
	private ShadowType shadowType;
	
	public Occlusion(int height, int width, ShadowType shadowType, double shadowBias, int pcfMask){
		this.height = height;
		this.width = width;
		this.shadowType = shadowType;
		this.pcfMask = pcfMask;
		this.shadowBias = shadowBias;
	}
	
	public void generateShadowMap(Projection projection, PointLight lightSource, Mesh[] meshes){
		
		//TODO: Blatt 5 Aufgabe 1 b)
		
		shadowMap = new Image<Correspondence>(1,1);
	}
	
	
	//returns if the position is in shadow or not:
	//1 = completely visible
	//0 = completely in the shadow(s)
	public double inShadow(Vector3 position){
		
		//TODO: Blatt 5 Aufgabe 1 c)
		//TODO: Blatt 5 Aufgabe 2
		
		return 1.0;
		
	}

}