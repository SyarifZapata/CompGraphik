package reflectance;

import image.RGBA;
import utils.Vector3;

public class OrenNayar extends Brdf{
	
	public RGBA albedo;
	public double sigmaSquarred;
	private double eps = 0.0001;

	public OrenNayar(RGBA albedo, double roughness) {
		this.albedo = albedo;
		this.sigmaSquarred = roughness;
	}

	@Override
	protected RGBA getRadiance(Vector3 toEye, Vector3 toLight, Vector3 n){

		//Blatt 4, Aufgabe 5

		double a = 1 - 0.5 * (sigmaSquarred/(sigmaSquarred + 0.33));
		double b = 0.45 * (sigmaSquarred/(sigmaSquarred + 0.09));

		double lNscalar = toLight.dot(n)/(toLight.length()* n.length());
		double vNscalar = toEye.dot(n)/(n.length() * toEye.length());

		double phiIn = Math.acos(lNscalar);
		double phiOut = Math.acos(vNscalar);

		double alpha = Math.max(phiIn,phiOut);
		double beta = Math.min(phiIn,phiOut);

		double tanBeta = Math.sin(beta)/ (Math.cos(beta) + 0.0001);

		return albedo.times(a + (b * Math.max(0,Math.cos(phiIn-phiOut)) * Math.sin(alpha) * tanBeta));

	}


}
