package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public class Upsampling implements ImageAlgorithm {
	
	Interpolation interpolation;
	
	public Upsampling(Interpolation method){
		this.interpolation = method;
	}
	
	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		interpolation.setImage(img);
		
		Image<RGBA> outImg = new Image<RGBA>(img.cols()*2, img.rows()*2, new RGBA(0.0f, 0.0f, 0.0f));

		//TODO: Blatt 2, Aufgabe 2
		for(int x=0; x<outImg.cols();x++){
			for(int y=0; y<outImg.rows();y++){
				RGBA interpolated = interpolation.access(x/2.0,y/2.0);
				outImg.set(x,y,interpolated);
			}
		}

		return outImg;
	}

}
