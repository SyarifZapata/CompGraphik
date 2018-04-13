package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;
import image.processing.convolution.GaussSeparableConvolution;

public class GaussianDownsampling implements ImageAlgorithm {

	/**
	 * For sake of simplicity, scale factor is 2
	 */
	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		
		Image<RGBA> outImg = new Image<RGBA>(img.cols()/2, img.rows()/2);
		Image<RGBA> filtered = new GaussSeparableConvolution(3, 1.0).perform(img);
		//TODO: Blatt 2, Aufgabe 2
		for(int x=0; x<img.cols();x++){
			for(int y=0; y<img.rows();y++){
				outImg.set(x/2,y/2,filtered.get(x,y));
				y++;
			}
			x++;
		}

		return outImg; 
	}

}
