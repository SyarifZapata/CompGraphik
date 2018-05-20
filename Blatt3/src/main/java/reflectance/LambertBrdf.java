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
		return albedo;

	}


	
}
