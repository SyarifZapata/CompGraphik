package image.processing.warping;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import renderer.SimpleRenderer;
import shader.InterpolatedColorShader;
import utils.Vector2;

public class ForwardWarp implements ImageAlgorithm {

	private Image<Vector2> flowField;
	
	public ForwardWarp(Image<Vector2> flowField){
		this.flowField = flowField;
	}
	
	public ForwardWarp(){
		super();
	}
	
	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		//render the targets as triangles
		SimpleRenderer r = new SimpleRenderer(img.cols(), img.rows(), new InterpolatedColorShader());
		
		//TODO: Blatt 2, Aufgabe 3
		for (int x = 0; x < img.cols() - 1; x++) {
			for (int y = 0; y < img.rows() - 1; y++) {
				Vector2 v1,v2,v3,v4;
				v1 = new Vector2(x, y).plus(flowField.get(x, y));
				v2 = new Vector2(x + 1, y).plus(flowField.get(x + 1, y));
				v3 = new Vector2(x, y + 1).plus(flowField.get(x, y + 1));
				v4 = new Vector2(x + 1, y + 1).plus(flowField.get(x + 1, y + 1));
				r.drawTriangle(new Vector2[]{v1, v2, v3}, new RGBA[]{img.get((int) v1.x, (int) v1.y), img.get((int) v2.x, (int) v2.y), img.get((int) v3.x, (int) v3.y)});
				r.drawTriangle(new Vector2[]{v2, v3, v4}, new RGBA[]{img.get((int) v2.x, (int) v2.y), img.get((int) v3.x, (int) v3.y), img.get((int) v4.x, (int) v4.y)});
			}
		}

		return r.getImg();
	}
	
	public void createRipples(int w, int h, double wavelength){
		Image<Vector2> ripples = new Image<Vector2>(w, h);
		
		for(int x = 0; x < w; x++){
			for(int y = 0; y < h; y++){
				Vector2 val = new Vector2(10.f*Math.cos(2.f*3.1415*(float)y/wavelength), 5.f*Math.sin(2.f*3.1415*(float)x/wavelength));
				ripples.set(x, y, val);
			}
		}
		
		this.flowField = ripples;
		
	}

}
