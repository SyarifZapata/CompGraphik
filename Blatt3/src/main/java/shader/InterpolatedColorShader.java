package shader;

import utils.BarycentricCoordinates;
import utils.Vector2;
import image.RGBA;

public class InterpolatedColorShader extends PixelShader {


	/**
	 * Colors a pixel (x,y) of a line between startPoint and endPoint with 
	 * the interpolated colors of if RGBA[] lineColors
	 */
	@Override
	public void handleLinePixel(int x, int y, Vector2 startPoint, Vector2 endPoint) {
		//TODO: Blatt 1, Aufgabe 6
		double totallength = endPoint.minus(startPoint).length();
		Vector2 currentVector = new Vector2(x,y);
		double lambda1 = currentVector.minus(startPoint).length()/totallength;
		double lambda2 = 1-lambda1;

		RGBA farbe = lineColors[0].times(lambda1).plus(lineColors[1].times(lambda2));

		getImage().set(x,y,farbe);

	}

	/**
	 * Colors a pixel (x,y) interpolating the color from the barycentric coordinates
	 * and the colors of RGBA[] triangleColors
	 */
	@Override
	public void handleTrianglePixel(int x, int y,
									BarycentricCoordinates triCoords) {
		//TODO: Blatt 1, Aufgabe 6
		RGBA farbe = triangleColors[0].times(triCoords.x).plus(triangleColors[1].times(triCoords.y))
				.plus(triangleColors[2].times(triCoords.z));
		getImage().set(x,y,farbe);
	}

	@Override
	public void setLineColors(RGBA[] colors) {
		lineColors = colors;
	}

	@Override
	public void setTriangleColors(RGBA[] colors) {
		triangleColors = colors;
	}

}
