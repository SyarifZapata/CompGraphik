package image.processing.convolution;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public abstract class SeparableConvolution implements ImageAlgorithm, Kernel {

	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		Image<Float> kernel = getKernel();

		if(kernel.cols() != 1 && kernel.rows() != 1)
			throw new IllegalArgumentException("Kernel cannot be a matrix for separable convolution");

		Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows(), new RGBA(0f, 0f, 0f));

		//TODO: Blatt 2, Aufgabe 1 c)

		int a = (int)(kernel.cols()/2.0);

		Image<RGBA> zwischenRes =  new Image<RGBA>(img.cols(),img.rows(),new RGBA(0,0,0));

		for(int x = 0; x < img.cols(); x++){
			for(int y = 0; y < img.rows(); y++){

				RGBA value = new RGBA(0.0,0.0,0.0);

				for(int j = 0; j < kernel.cols(); j++){
					value = value.plus(img.get(x,y+j-a).times(kernel.get(j,0)));
				}

				zwischenRes.set(x,y,value);
			}
		}

		for(int x = 0; x < img.cols(); x++){
			for(int y = 0; y < img.rows(); y++){

				RGBA value = new RGBA(0.0,0.0,0.0);

				for(int i = 0; i < kernel.cols(); i++){
					value = value.plus(zwischenRes.get(x+i-a,y).times(kernel.get(i,0)));
				}

				outImg.set(x,y,value);
			}
		}
		printKernel();
		return outImg;
	}


	@Override
	public void printKernel(){
		Image<Float> kernel = getKernel();
		StringBuilder builder = new StringBuilder("[");
		boolean first = true;

		for(int i = 0; i < kernel.size(); i++){
			if(!first)
				builder.append(", ");

			builder.append(kernel.get(i));
			first = false;
		}

		builder.append("]");

		System.out.println(builder.toString());
	}

}
