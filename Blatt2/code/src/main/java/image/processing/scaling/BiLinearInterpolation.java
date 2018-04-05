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

		RGBA oben = linearlyInterpolate(linksOben,rechtsOben,xDist);
		RGBA unten = linearlyInterpolate(linksUnten,rechtsUnten,xDist);

		double yDist = y%1;

		RGBA res = linearlyInterpolate(oben,unten,yDist);


		return res;
	}

	/**
	 * Returns c0*(1-dx) + c1*dx
	 * where dx is the distance from c0 to the point to be interpolated.
	 */
	private RGBA linearlyInterpolate(RGBA c0, RGBA c1, double dx) {
		
		RGBA res;
		
		//TODO: Blatt 2, Aufgabe 2
		res = c1.times(dx).plus(c0.times(1-dx));
		
		return res;
	}

}
