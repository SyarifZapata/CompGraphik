package image.processing.warping;

import image.Image;
import image.ImageUtils;
import image.RGBA;
import utils.Vector2;

public class Morphing {

	private Image<RGBA> imgA, imgB;
	private Image<Vector2> a2b, b2a;
	
	public Morphing(Image<RGBA> imgA, Image<RGBA> imgB, String flowFieldA2B, String flowFieldB2A){
		this.imgA = imgA;
		this.imgB = imgB;
		
		this.a2b = ImageUtils.readFlowField(flowFieldA2B);
		this.b2a = ImageUtils.readFlowField(flowFieldB2A);
	}
	
	public Image<RGBA> morph(float lambda) {
		Image<RGBA> outImage = new Image<RGBA>(imgA.cols(), imgA.rows(), new RGBA(0f, 0f, 0f));

		//TODO: Blatt 2, Aufgabe 3
		BackwardWarp warp1 = new BackwardWarp(a2b);
		Image<RGBA> newImgA = warp1.perform(imgA);
		BackwardWarp warp2 = new BackwardWarp(b2a);
		Image<RGBA> newImgB = warp2.perform(imgB);


		for (int x = 0; x < outImage.cols();x++){
			for (int y = 0; y < outImage.rows();y++){

				RGBA res = newImgA.get(x,y);
				RGBA res2 = newImgB.get(x,y);

				res2 = res2.times(1-lambda);
				res = res.times(lambda);

				RGBA bravo = res.plus(res2);



				outImage.set(x,y,(bravo));

			}
		}


		return outImage;
	}

}
