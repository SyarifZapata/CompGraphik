package rasterization;

import utils.Vector2;

public class LineRasterizer {

	private LinePixelHandler handler;
	private int w, h;

	public LineRasterizer(LinePixelHandler linePixelHandler, int w, int h){
		this.handler = linePixelHandler;
		this.w = w;
		this.h = h;
	}


	public void rasterLine(Vector2[] line){
		assert(line.length > 1);
		for(int i = 0; i < line.length-1; ++i)
		{
			bresenham(line[i],line[i+1]);
		}
	}

	private void bresenham(Vector2 startPoint, Vector2 endPoint){
		//TODO: Blatt 1, Aufgabe 3:

		Vector2 d = endPoint.minus(startPoint);
		double dx = Math.abs(d.x);
		double dy = Math.abs(d.y);

		int yIncrement = 1;
		int xIncrement = 1;
		int x = (int)startPoint.x;
		int y = (int)startPoint.y;
		int xEnd = (int)endPoint.x;
		int yEnd = (int)endPoint.y;


		// When the distance in y axis is greater than the y axis.
		if(dx < dy){

			double error = 2*dx -dy;

			if(y>yEnd){
				//yIncrement = -1;

				int tempx = x;
				int tempy = y;
				x = xEnd;
				y = yEnd;
				xEnd = tempx;
				yEnd = tempy;
			}

			if(x>xEnd){
				xIncrement = -1;
			}

			while (y<yEnd) {
				y += yIncrement;
				if (error > 0) {
					x += xIncrement;
					error = error + dx - dy;
				} else {
					error = error + dx;
				}
				if (x < w && y < h && x >= 0 && y >= 0) {
					handler.handleLinePixel(x, y, startPoint, endPoint);
				}
			}


		}else{

			double error = 2*dy -dx;

			if(x>xEnd){

				int tempx = x;
				int tempy = y;
				x = xEnd;
				y = yEnd;
				xEnd = tempx;
				yEnd = tempy;

			}

			if(y>yEnd){
				yIncrement = -1;
			}

			while (x<xEnd) {
				x += xIncrement;
				if (error > 0) {
					y += yIncrement;
					error = error + dy - dx;
				} else {
					error = error + dy;
				}

				if (x < w && y < h && x >= 0 && y >= 0) {
					handler.handleLinePixel(x, y, startPoint, endPoint);
				}
			}


		}

	}

}