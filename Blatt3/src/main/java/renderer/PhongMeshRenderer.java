package renderer;

import illumination.PhongPointLight;
import image.Image;
import image.RGBA;
import mesh.Mesh;
import occlusion.Occlusion;
import occlusion.ShadowType;
import rasterization.Correspondence;
import reflectance.PhongMaterial;
import utils.Matrix4;
import utils.Triplet;
import utils.Vector3;
import utils.Vector4;

import java.util.Vector;

public class PhongMeshRenderer extends MeshRenderer {

	protected Image<RGBA> img;
	protected PhongPointLight lightSource;
	protected PhongMaterial material;
	
	//Shadows
	private boolean shadows = false;
	private Occlusion shadowSystem;
	private double shadowBias;
	private int pcfMaskSize;
	private ShadowType shadowType = ShadowType.HARD;
	
	public PhongMeshRenderer(int w, int h, Mesh[] meshes, RGBA lightColor, double matShininess){
		super(w, h, meshes);
		img = new Image<RGBA>(w, h);
		
		Vector3 lightPosition = new Vector3(-4, 4, 5);
		RGBA ambientLight = new RGBA(0.2, 0.2, 0.4);
		lightSource = new PhongPointLight(lightPosition, lightColor, ambientLight);
		
		RGBA matAmbient = new RGBA(0.4, 0.4, 0.4);
		RGBA matDiffuse = new RGBA(0.6, 0.2, 0.2);
		RGBA matSpecular = new RGBA(0.7, 0.7, 0.7);
		material = new PhongMaterial(matAmbient, matDiffuse, matSpecular, matShininess);
	}
	
	public RGBA getLightColor(){
		return lightSource.color;
	}
	public PhongPointLight getLightSource(){
		return lightSource;
	}
	public PhongMaterial getMaterial(){
		return material;
	}
	public void setMaterial(PhongMaterial mat){
		material = mat;
	}
	
	@Override
	protected Image<RGBA> colorize(Image<Correspondence> correspondence){
		if(shadows){
			shadowSystem = new Occlusion(480, 480, shadowType, shadowBias, pcfMaskSize);
			shadowSystem.generateShadowMap(projection, lightSource, meshes);
		}
		
		Vector4 eye = projection.getEye();
		for(int xx = 0; xx < correspondence.cols(); xx++){
			for(int yy = 0; yy < correspondence.rows(); yy++){
				Correspondence c = correspondence.get(xx, yy);
				if (c != null) {
					shadePhong(xx, yy, c, eye);
				}
			}
		}
		
		return img;
	}

	protected void shadePhong(int x, int y, Correspondence c, Vector4 eye){
		//TODO: Blatt 4, Aufgabe 2
		//Iphong = ra.Ia + rd.ic.<L,N>+rs.ic <R,V>^m

		RGBA ra = material.ambient;
		RGBA ia = lightSource.ambient;
		RGBA rd = material.diffuseReflectance;
		RGBA rs = material.specularReflectance;
		RGBA ic = lightSource.color;
		double m = material.shininess;

		Vector3 p,p1,p2,p3;

		p1 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(0)];
		p2 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(1)];
		p3 = c.mesh.vertices[c.mesh.tvi[c.triangle].get(2)];

		p = c.triCoords.interpolate(p1,p2,p3);

		// will it also work the other way around? p-lightsource postition?
		Vector3 l = lightSource.position.minus(p).normalize();

		Vector3 first = c.mesh.normals[c.mesh.tni[c.triangle].get(0)];
		Vector3 second = c.mesh.normals[c.mesh.tni[c.triangle].get(1)];
		Vector3 third = c.mesh.normals[c.mesh.tni[c.triangle].get(2)];

		Vector3 n = c.triCoords.interpolate(first,second,third).normalize();

		// why wont it works with eye - betrachter? 
		Vector3 betrachter = p.minus(new Vector3(eye.x/eye.w,eye.y/eye.w,eye.z/eye.w)).normalize();

		double scalarProduct_LN = l.dot(n);
		Vector3 r = l.minus(n.times(scalarProduct_LN*2)).normalize();
		double scalarProduct_RV = r.dot(betrachter);

		if(scalarProduct_LN<0){
			scalarProduct_LN = 0;
		}

		if(scalarProduct_RV<0){
			scalarProduct_RV =0;
		}


		RGBA phong = ra.multElementWise(ia)
				.plus(rd.multElementWise(ic).times(scalarProduct_LN))
				.plus(rs.multElementWise(ic).times(Math.pow(scalarProduct_RV,m)));
		//phong.clamp();
		if(shadowSystem != null){
			double shadowEff = shadowSystem.inShadow(p);
			img.set(x, y, phong.times(shadowEff));
		}else {
			img.set(x,y,phong);
		}





		//TODO: Blatt 5, Aufgabe 1 c)

	}

	public void enableShadow() {
		shadows = true;
	}

	public void setShadowBias(double shadowBias) {
		this.shadowBias = shadowBias; 
	}

	//For testing purposes
	public Occlusion getShadowSystem() {
		return shadowSystem;
	}

	public void setShadowType(ShadowType shadowType) {
		this.shadowType = shadowType;
	}

	public void setPCFMaskSize(int pcfMaskSize) {
		this.pcfMaskSize = pcfMaskSize;
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
