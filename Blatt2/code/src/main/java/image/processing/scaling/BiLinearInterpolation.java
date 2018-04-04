package image.processing.scaling;

import image.Image;
import image.RGBA;

public class BiLinearInterpolation implements Interpolation {

	private Image<RGBA> img;
	
	public void setImage(Image<RGBA> img){
		this.img = img;
	}
	
	@Override
	public RGBA access(double x, double y) {

		//TODO: Blatt 2, Aufgabe 2

		int ax0 = (int)x;
		int ax1 = ax0 +1;
		int ay0 = (int)y;
		int ay1 = ay0 +1;

		RGBA linksOben = img.get(ax0,ay0);
		RGBA rechtsOben = img.get(ax1,ay0);
		RGBA linksUnten = img.get(ax0,ay1);
		RGBA rechtsUnten = img.get(ax1,ay1);

		double xDist = x%1;


		RGBA oben = new RGBA(rechtsOben.r*xDist + (1-xDist)*linksOben.r, rechtsOben.g*xDist + (1-xDist)*linksOben.g,rechtsOben.b*xDist + (1-xDist)*linksOben.b);
		RGBA unten = new RGBA(rechtsUnten.r*xDist + (1-xDist)*linksUnten.r,rechtsUnten.g*xDist + (1-xDist)*linksUnten.g,rechtsUnten.b*xDist + (1-xDist)*linksUnten.b);

		double yDist = y%1;

		RGBA res = new RGBA(unten.r*yDist + (1-yDist)*oben.r, unten.g*yDist + (1-yDist)*oben.g,unten.b*yDist + (1-yDist)*oben.b);


		return res;
	}

	/**
	 * Returns c0*(1-dx) + c1*dx
	 * where dx is the distance from c0 to the point to be interpolated.
	 */
	private RGBA linearlyInterpolate(RGBA c0, RGBA c1, double dx) {
		
		RGBA res = new RGBA(0.0f, 0.0f, 0.0f);
		
		//TODO: Blatt 2, Aufgabe 2
		res = c1.times(dx).plus(c0.times(1-dx));
		
		return res;
	}

}
