package image.processing.convolution;

import image.Image;

public class MeanConvolution extends Convolution {

	private int size;
	private Image<Float> kernel = null;

	public MeanConvolution(int size) {
		this.size = size;
	}

	public Image<Float> getKernel(){
		if(this.kernel != null)
			return this.kernel;


		if(size%2 == 0){
			size += 1;
		}
		float value = 1.0f/(size*size);
		Image<Float> kernelNew = new Image<Float>(size, size, value);

		//TODO: Blatt 2, Aufgabe 1 a)



		this.kernel = kernelNew;
		return kernelNew;
	}

}
