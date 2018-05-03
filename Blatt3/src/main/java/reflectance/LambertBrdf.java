package reflectance;

import image.RGBA;
import utils.Vector3;

public class LambertBrdf extends Brdf{

	public RGBA albedo;
	
	
	public LambertBrdf(final RGBA albedo){
		super();
		this.albedo = albedo;
	}
	
	protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n){
		//Blatt 4, Aufgabe 3 a)
		if(toLight.dot(n)>0){
			return albedo;
		}else {
			return new RGBA(0, 0, 0);
		}


	}


	
}
