package image.processing.scaling;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

/**
 * Performs nearest neighbour downsampling.
 */
public class NNDownsampling implements ImageAlgorithm {

	/**
	 * For sake of simplicity, scale factor is 2
	 */
	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		Image<RGBA> outImg = new Image<RGBA>(img.cols()/2, img.rows()/2);

		//TODO: Blatt 2, Aufgabe 2
		for(int x=0; x<img.cols();x++){
			for(int y=0; y<img.rows();y++){
				outImg.set(x/2,y/2,img.get(x,y));
				y++;
			}
			x++;
		}

		return outImg;
	}

}
