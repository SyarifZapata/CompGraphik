package image.processing.convolution;

import image.Image;

public class GaussConvolution extends Convolution {

	private int size;
	private double sigma;
	private Image<Float> kernel = null;
	
	public GaussConvolution(int size, double sigma) {
		this.size = size;
		this.sigma = sigma;
	}
	
	public Image<Float> getKernel(){
		if(this.kernel != null)
			return this.kernel;

		// make sure the matrix has a distinct center.
		if(size%2 == 0){
			size += 1;
		}
		
		Image<Float> kernelNew = new Image<Float>(size, size, 0.0f);

		int a = (int)Math.ceil(size/2.0);
		float result,sum;
		double part1,part2,part3;
		sum = 0;
		for(int x = 0; x<size ; x++){
			for(int y = 0; y<size ; y++){
				part1 = 1/(2*Math.PI*Math.pow(sigma,2));
				part2 = (Math.pow(x-a,2)+Math.pow(y-a,2))*-1;
				part3 = 2*Math.pow(sigma,2);
				result = (float)(part1*Math.exp(part2/part3));
				kernelNew.set(x,y,result);
				sum += result;
			}
		}
		for(int x = 0; x<size ; x++){
			for(int y = 0; y<size ; y++){
				kernelNew.set(x,y,kernelNew.get(x,y)/sum);
			}
		}
		
		//TODO: Blatt 2, Aufgabe 1 a)
		
		this.kernel = kernelNew;
		return kernelNew;
	}
	
}
