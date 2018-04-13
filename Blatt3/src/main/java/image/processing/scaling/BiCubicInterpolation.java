package image.processing.scaling;

import image.Image;
import image.RGBA;

public class BiCubicInterpolation implements Interpolation {

	private Image<RGBA> img;
	
	@Override
	public void setImage(Image<RGBA> img) {
		this.img = img;
	}
	
	@Override
	public RGBA access(double x, double y) {

		RGBA res = new RGBA(0.0f, 0.0f, 0.0f);
		RGBA line1,line2,line3,line4;

		//TODO: Blatt 2, Aufgabe 2

		double xx = x%1; double yy = y%1;
		int ax1,ax2,ax3,ax4,bx1,bx2,bx3,bx4,cx1,cx2,cx3,cx4,dx1,dx2,dx3,dx4;
		int ay1,ay2,ay3,ay4,by1,by2,by3,by4,cy1,cy2,cy3,cy4,dy1,dy2,dy3,dy4;
		ax1 = (int)x-1;	ay1 = (int)y-1;
		ax2 = ax1+1;	ay2 = ay1;
		ax3 = ax1+2;	ay3 = ay1;
		ax4 = ax1+3;	ay4 = ay1;

		bx1 = ax1;	by1 = ay1+1;
		bx2 = ax2;	by2 = ay1+1;
		bx3 = ax3;	by3 = ay1+1;
		bx4 = ax4;	by4 = ay1+1;

		cx1 = ax1;	cy1 = ay1+2;
		cx2 = ax2;	cy2 = ay1+2;
		cx3 = ax3;	cy3 = ay1+2;
		cx4 = ax4;	cy4 = ay1+2;

		dx1 = ax1;	dy1 = ay1+3;
		dx2 = ax2;	dy2 = ay1+3;
		dx3 = ax3;	dy3 = ay1+3;
		dx4 = ax4;	dy4 = ay1+3;

		RGBA p1,p2,p3,p4,q1,q2,q3,q4,r1,r2,r3,r4,s1,s2,s3,s4;
		p1 = img.get(ax1,ay1);
		p2 = img.get(ax2,ay2);
		p3 = img.get(ax3,ay3);
		p4 = img.get(ax4,ay4);
		q1 = img.get(bx1,by1);
		q2 = img.get(bx2,by2);
		q3 = img.get(bx3,by3);
		q4 = img.get(bx4,by4);
		r1 = img.get(cx1,cy1);
		r2 = img.get(cx2,cy2);
		r3 = img.get(cx3,cy3);
		r4 = img.get(cx4,cy4);
		s1 = img.get(dx1,dy1);
		s2 = img.get(dx2,dy2);
		s3 = img.get(dx3,dy3);
		s4 = img.get(dx4,dy4);

		line1 = interpolateCubically(p1,p2,p3,p4,(float)xx);
		line2 = interpolateCubically(q1,q2,q3,q4,(float)xx);
		line3 = interpolateCubically(r1,r2,r3,r4,(float)xx);
		line4 = interpolateCubically(s1,s2,s3,s4,(float)xx);
		res = interpolateCubically(line1,line2,line3,line4,(float)yy);

		return res;
	}
	
	/**
	 * Bicubic interpolation as follows:
	 * f(x) = px³ + qx² + rx + b, with
	 * 	p = (c3 - c2) - (c0 - c1)
	 * 	q = 2(c0 - c1) - (c3 - c2)
	 * 	r = c2 - c0
	 * Note: clamping of result is important!
	 * 
	 * TEACHING: remove this method
	 */
	private RGBA interpolateCubically(RGBA c0, RGBA c1, RGBA c2, RGBA c3, float x){
		
		RGBA res = new RGBA(0.0f, 0.0f, 0.0f);
		
		//TODO: Blatt 2, Aufgabe 2
		RGBA p,q,r;
		p = (c3.minus(c2)).minus(c0.minus(c1));
		q = ((c0.minus(c1)).times(2)).minus(c3.minus(c2));
		r = c2.minus(c0);
		res = (((p.times(Math.pow(x,3))).plus(q.times(Math.pow(x,2)))).plus(r.times(x))).plus(c1);
		
		res.clamp();
		return res;
	}

	

}
