package utils;

public class BarycentricCoordinateTransform {
	private double det;
	private Vector2 a,b,c;

	public BarycentricCoordinateTransform(Vector2 a, Vector2 b, Vector2 c){
		this.a = a;
		this.b = b;
		this.c = c;

		det = a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y);
	}


	public boolean isDegenerate(){
		return det==0;
	}



	public BarycentricCoordinates getBarycentricCoordinates(double x, double y){
		double lambdaX = 0, lambdaY = 0, lambdaZ = 0;

		assert(!isDegenerate());

		double divider = (((b.y-c.y)*(a.x-c.x))+((c.x-b.x)*(a.y-c.y)));

		//TODO: Blatt 1, Aufgabe 5
		lambdaX = (((b.y-c.y)*(x-c.x))+((c.x-b.x)*(y-c.y)))/ divider;		//Formel aus dem Skript Folienblock 2, S.23
		lambdaY = (((c.y-a.y)*(x-c.x))+((a.x-c.x)*(y-c.y)))/ divider;
		lambdaZ = 1 - (lambdaX + lambdaY);
		return new BarycentricCoordinates(lambdaX,lambdaY,lambdaZ);
	}
}