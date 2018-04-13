package image.processing.convolution;

import image.Image;

public class SobelHConvolution extends Convolution {

	private Image<Float> kernel = null;

	@Override
	public Image<Float> getKernel() {
		if(this.kernel != null)
			return kernel;

		Image<Float> kernelNew = new Image<Float>(3, 3, 0.0f);

		//TODO: Blatt 2, Aufgabe 1 d)

		kernelNew.set(0,0,-1.0f);
		kernelNew.set(1,0,-2.0f);
		kernelNew.set(2,0,-1.0f);
		kernelNew.set(0,2,1.0f);
		kernelNew.set(1,2,2.0f);
		kernelNew.set(2,2,1.0f);


		this.kernel = kernelNew;
		return kernelNew;
	}

}
