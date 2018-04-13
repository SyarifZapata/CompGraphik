package image.processing.convolution;

import image.Image;

public class LaplaceConvolution extends Convolution {

	private Image<Float> kernel = null;

	/**
	 * A simple Laplace Operator to detect edges.
	 */
	@Override
	public Image<Float> getKernel() {
		if(this.kernel != null)
			return this.kernel;

		Image<Float> kernelNew = new Image<Float>(3, 3, (1.0f/8.0f)*-1);

		//TODO: Blatt 2, Aufgabe 1 d)
		kernelNew.set(1,1,1.0f);

		this.kernel = kernelNew;
		return kernelNew;
	}

}
