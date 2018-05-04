package reflectance;

import image.RGBA;
import utils.Vector3;

public class CookTorrance extends Brdf {
	
	final private double eps = 1e-4;
	
	private double m;
	private double r0;
	private RGBA albedo;
	
	public CookTorrance(RGBA albedo, double m, double r0){
		this.albedo = albedo;
		this.m = m;
		this.r0 = r0;
	}

	@Override
	protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n) {
		//Blatt 4, Aufgabe 4

		Vector3 halfAngle = toEye.plus(toLight).times(1 / (toEye.plus(toLight).length()));

		double d = (1/(4* Math.pow(m,2) *  Math.pow(n.dot(halfAngle),4)))* Math.exp(-(((1-Math.pow(n.dot(halfAngle),2)))/(Math.pow(n.dot(halfAngle),2)*Math.pow(m,2))));

		double g1 = 1;
		double g2 = (2* Math.abs(n.dot(halfAngle))* Math.abs(n.dot(toEye)))/Math.abs(toEye.dot(halfAngle));
		double g3 = (2* Math.abs(n.dot(halfAngle))* Math.abs(n.dot(toLight)))/Math.abs(toEye.dot(halfAngle));
		double g4 = Math.min(g1,g2);
		double g = Math.min(g3,g4);

		double f = r0 + ((1 - r0)*Math.pow((1-Math.abs(halfAngle.dot(toEye))),5));

		double multiplier = (f*d*g)/(4*toEye.dot(n));

		albedo = albedo.times(multiplier);
		return albedo;
	}
	
	
}
