package utils;

public class Determinants {

	public static double det2x2(double a1, double a2, 
			double b1, double b2){
		return 
		+ a1 * b2 
		- b1 * a2;
	}

	public static double det3x3(double a1, double a2, double a3, 
		     double b1, double b2, double b3, 
		     double c1, double c2, double c3){
	    return 
		+ a1 * det2x2(b2, b3, c2, c3) 
		- b1 * det2x2(a2, a3, c2, c3) 
		+ c1 * det2x2(a2, a3, b2, b3);
	}
}
