package rasterization;


import utils.BarycentricCoordinateTransform;
import utils.BarycentricCoordinates;
import utils.Vector2;

public class TriangleRasterizer {

	private TrianglePixelHandler handler;
	private int w, h;


	public TriangleRasterizer(TrianglePixelHandler handler, int w, int h){
		this.handler = handler;
		this.w = w;
		this.h = h;
	}

	public void rasterTriangle(Vector2[] trianglePoints){
		Vector2 va = trianglePoints[0];
		Vector2 vb = trianglePoints[1];
		Vector2 vc = trianglePoints[2];

		BarycentricCoordinateTransform bct = new BarycentricCoordinateTransform(va, vb, vc);
		if (bct.isDegenerate()) return;

		//TODO: Blatt 1, Aufgabe 6

		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				BarycentricCoordinates bcC = bct.getBarycentricCoordinates(x,y);
				if(bcC.isInside() && x<w && y<h){
					handler.handleTrianglePixel(x,y,bcC);
				}

			}
		}



	}
}
