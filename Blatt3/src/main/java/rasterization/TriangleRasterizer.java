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


		//xmin x max, y finden für box
		double xmax,xmin,ymax,ymin;
		xmax = Math.min(w,Math.max(va.x,Math.max(vb.x,vc.x)));
		xmin = Math.max(0,Math.min(va.x,Math.min(vb.x,vc.x)));
		ymax = Math.min(h,Math.max(va.y,Math.max(vb.y,vc.y)));
		ymin = Math.max(0,Math.min(va.y,Math.min(vb.y,vc.y)));


		for(int x=(int)xmin;x<(int)xmax+1;x++){
			for(int y=(int)ymin;y<(int)ymax+1;y++){
				BarycentricCoordinates bcC = bct.getBarycentricCoordinates(x,y);
				if(bcC.isInside() && x<w && y<h){
					handler.handleTrianglePixel(x,y,bcC);
				}

			}
		}



	}
}
