package renderer;

import java.util.ArrayList;

import occlusion.Occlusion;
import occlusion.ShadowType;
import illumination.PointLight;
import image.Image;
import image.RGBA;
import rasterization.Correspondence;
import reflectance.Brdf;
import reflectance.LambertBrdf;
import utils.Matrix4;
import utils.Triplet;
import utils.Vector3;
import utils.Vector4;
import mesh.Mesh;

public class ReflectanceMeshRenderer extends MeshRenderer{

	protected Image<RGBA> img;
	public ArrayList<Brdf> matBrdf = new ArrayList<Brdf>();
	public ArrayList<PointLight> lightSources = new ArrayList<PointLight>();
	
	//Shadows
	private boolean shadows = false;
	private Occlusion shadowSystem;
	private double shadowBias;
	private int pcfMaskSize;
	public ShadowType shadowType = ShadowType.HARD;
	
	public ReflectanceMeshRenderer(int w, int h, Mesh[] meshes) {
		super(w, h, meshes);
		img = new Image<RGBA>(w, h);
		matBrdf.add(new LambertBrdf(new RGBA(0.7, 0.7, 0.7)));
		lightSources.add(new PointLight(new Vector3(-4, 4, 5), new RGBA(0.7, 0.2, 0.2)));
	}
	
	@Override
	protected Image<RGBA> colorize(Image<Correspondence> correspondence){
		if(shadows){
			shadowSystem = new Occlusion(480, 480, shadowType, shadowBias, pcfMaskSize);
		}
		
		Vector4 eye = projection.getEye();
		for(PointLight lightSource : lightSources){
			//TODO: Blatt 5, Aufgabe 1 c)
			for(int x = 0; x < correspondence.cols(); x++){
				for(int y = 0; y < correspondence.rows(); y++){
					Correspondence c = correspondence.get(x, y);
					if (c != null) {
						shade(x, y, c, eye, lightSource);
					}
				}
			}
		}
		return img;
	}

	// matBrdf:  [reflectance.LambertBrdf@366820c, reflectance.CookTorrance@111a3d90]
	//lightSources		[illumination.PointLight@77468339]
	protected void shade(int x, int y, Correspondence c, Vector4 eye, PointLight lightSource){

		Vector3 p,p1,p2,p3;

		p1 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(0)];
		p2 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(1)];
		p3 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(2)];

		p = c.triCoords.interpolate(p1,p2,p3);

		Vector3 first = c.mesh.normals[c.mesh.tni[c.triangle].get(0)];
		Vector3 second = c.mesh.normals[c.mesh.tni[c.triangle].get(1)];
		Vector3 third = c.mesh.normals[c.mesh.tni[c.triangle].get(2)];

		Vector3 n = c.triCoords.interpolate(first,second,third).normalize();

		RGBA radiance1 = matBrdf.get(0).getRadiance(eye,p,lightSource,n);
		RGBA radiance2 = matBrdf.get(1).getRadiance(eye,p,lightSource,n);

		RGBA radiance = radiance1.multElementWise(radiance2);

		img.set(x,y,radiance);

		//TODO: Blatt 4, Aufgabe 3 b)
		//TODO: Blatt 5, Aufgabe 1 c)
		
	}
	
	public void enableShadow(){
		shadows = true;
	}
	public void setShadowBias(double bias){
		shadowBias = bias;
	}
	public void setPCFMaskSize(int size){
		pcfMaskSize = size;
	}
	public void setShadowType(ShadowType sType){
		shadowType = sType;
	}
	public Occlusion getShadowSystem() {
		return shadowSystem;
	}
	
	@Override
	public void clearImg(){
		for(int i=0; i<img.size(); ++i){
			if(img.get(i)!=null){
				img.get(i).r = 0;
				img.get(i).g = 0;
				img.get(i).b = 0;
				img.get(i).a = 1;
			}
		}
	}
	
	@Override
	public void rotateLights(Matrix4 rotation) {
		for(PointLight lightSource: lightSources){
			Vector3 lp = lightSource.position;
			Vector4 t = new Vector4(lp.x, lp.y, lp.z, 1);
			Vector4 rotated = rotation.multiply(t);
			rotated.times(1.0/rotated.z);
			lightSource.position = new Vector3(rotated.x, rotated.y, rotated.z);
		}
	}
	
}
