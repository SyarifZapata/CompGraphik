package renderer;

import illumination.PointLight;
import image.Image;
import image.RGBA;
import rasterization.Correspondence;
import utils.*;
import mesh.Mesh;

public class LambertMeshRenderer extends MeshRenderer{

	private double MATERIAL_ALBEDO;
	private PointLight lightSource;
	
	protected Image<RGBA> img;

	public LambertMeshRenderer(int w, int h, Mesh[] meshes) {
		super(w, h, meshes);
		img = new Image<RGBA>(w, h);
		
		Vector3 lightPosition = new Vector3(-2, 2, 4);
		RGBA lightColor = new RGBA(0.7, 0.2, 0.2);
		lightSource = new PointLight(lightPosition, lightColor);
		
		MATERIAL_ALBEDO = 0.7;
	}
	
	public void setLightColor(RGBA lightColor){
		lightSource = new PointLight(lightSource.position, lightColor);
	}
	public void setAlbedo(double albedo){
		MATERIAL_ALBEDO = albedo;
	}
	
	@Override
	protected Image<RGBA> colorize(Image<Correspondence> correspondence){
		
		for(int x = 0; x < correspondence.cols(); x++){
			for(int y = 0; y < correspondence.rows(); y++){
				Correspondence c = correspondence.get(x, y);
				if (c != null) {
					colorizePixel(x, y, c);
				}
			}
		}
		
		return img;
	}
	
	private void colorizePixel(int x, int y, Correspondence c){
		//TODO: Blatt 4, Aufgabe 1
		//Lambert=Ilight.<L.N>.albado
		RGBA light = lightSource.color;
		Vector3 l = lightSource.position;
		Vector3 n = new Vector3(x,y,c.depth);
		double scalarProd = l.dot(n);
		RGBA lambert = light.times(scalarProd).times(MATERIAL_ALBEDO);
		if(scalarProd != 0){
			img.set(x,y,lambert);
		}

	}
	
	@Override
	public void clearImg(){
		for(int i=0; i<img.size(); i++){
			if(img.get(i)!=null){
				RGBA reset = img.get(i);
				reset.a = 1f;
				reset.r = 0; 
				reset.g = 0;
				reset.b = 0;
			}
		}
	}
	
	@Override
	public void rotateLights(Matrix4 rotation) {
		Vector3 lp = lightSource.position;
		Vector4 t = new Vector4(lp.x, lp.y, lp.z, 1);
		Vector4 rotated = rotation.multiply(t);
		rotated.times(1.0/rotated.z);
		lightSource.position = new Vector3(rotated.x, rotated.y, rotated.z);
	}
}
