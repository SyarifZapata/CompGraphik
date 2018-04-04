package image.processing.convolution;

import image.Image;

public class GaussSeparableConvolution extends SeparableConvolution {

	private int size;
	private double sigma;
	private Image<Float> kernel = null;

	public GaussSeparableConvolution(int size, double sigma) {
		this.size = size;
		this.sigma = sigma;
	}

	@Override
	public Image<Float> getKernel() {
		if(this.kernel != null)
			return this.kernel; //for faster access

		if(size%2 == 0){
			size += 1;
		}

		int a = (int)Math.ceil(size/2.0);

		Image<Float> kernel = new Image<Float>(size, 1, 0.0f);

		float result,sum;
		sum = 0;
		double part1,part2,part3;
		for(int x = 0; x<size;x++){

			part1 = 1/(Math.sqrt(2*Math.PI)*sigma);
			part2 = (Math.pow(x-a,2))*-1;
			part3 = 2*Math.pow(sigma,2);
			result = (float)(part1*Math.exp(part2/part3));
			kernel.set(x,result);
			sum = sum+result;
		}

		for(int x = 0; x<size;x++){
			kernel.set(x,kernel.get(x)/sum);
		}


		//TODO: Blatt 2, Aufgabe c)

		this.kernel = kernel;
		return kernel;
	}
}
