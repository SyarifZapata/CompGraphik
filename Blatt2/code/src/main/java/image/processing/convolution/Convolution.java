package image.processing.convolution;

import image.Image;
import image.RGBA;
import image.processing.ImageAlgorithm;

public abstract class Convolution implements ImageAlgorithm, Kernel {

	@Override
	public Image<RGBA> perform(Image<RGBA> img) {
		Image<RGBA> outImg = new Image<RGBA>(img.cols(), img.rows());
		Image<Float> kernel = getKernel();

		//TODO: Blatt 2, Aufgabe 1 b)
		int a = (int)(kernel.cols()/2.0);

		for(int x = 0; x < img.cols(); x++){
			for(int y = 0; y < img.rows(); y++){

				RGBA value = new RGBA(0.0,0.0,0.0);
				//double res = 0.00;
				for(int i = 0; i < kernel.cols(); i++){
					for(int j = 0; j < kernel.cols(); j++){

					 	value = value.plus(img.get(x+i-a,y+j-a).times(kernel.get(i,j)));
						//res = res + (img.get(x+i-a,y+j-a).pack()*(kernel.get(i,j)));


					}
				}
				outImg.set(x,y,value);
			}
		}

		printKernel();
		return outImg;
	}
	
	public abstract Image<Float> getKernel();
	
	@Override
	public void printKernel(){
		Image<Float> kernel = getKernel();
		StringBuilder builder = new StringBuilder("[");
		boolean first = true;
		
		for(int y = 0; y < kernel.rows(); y++){
			
			StringBuilder row = new StringBuilder();
			if(first) first = false;
			else row.append(" ");
			
			for(int x = 0; x < kernel.cols(); x++){
				row.append(kernel.get(x, y));
				row.append(" ");
			}
			
			builder.append(row);
			builder.append("\n");
		}
		
		builder.deleteCharAt(builder.length()-1);
		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");
		
		System.out.println(builder.toString());
	}

}
