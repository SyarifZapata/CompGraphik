package renderer;

import image.Image;
import image.RGBA;
import mesh.Mesh;
import projection.PinholeProjection;
import projection.TurnableRenderer;
import rasterization.Correspondence;
import rasterization.MeshRasterizer;
import shader.InterpolatedColorShader;
import shader.PixelShader;
import utils.Matrix4;
import utils.Triplet;

public class MeshRenderer implements TurnableRenderer {

	protected MeshRasterizer mRasterizer;
	protected Mesh[] meshes;
	protected int w, h;
	protected PinholeProjection projection;
	protected PixelShader shader;
	
	public MeshRenderer(int w, int h, Mesh[] meshes){
		this.mRasterizer = new MeshRasterizer(w, h);
		this.meshes = meshes;
		this.w = w;
		this.h = h;

		shader = new InterpolatedColorShader();
		shader.setImgSize(w, h);
	}
	
	public Image<RGBA> renderMesh(PinholeProjection p){
		this.projection = p;
		Image<Correspondence> correspondence = mRasterizer.rasterize(p, meshes);
		
		return colorize(correspondence);
	}
	
	public Image<RGBA> renderMesh(PinholeProjection p, MeshRasterizer mRasterizer){
		this.projection = p;
		Image<Correspondence> correspondence = mRasterizer.rasterize(p, meshes);
		
		return colorize(correspondence);
	}
	
	public void enablePerspectiveCorrect(boolean b){
		mRasterizer.enablePerspectiveCorrect(b);
	}
	
	protected Image<RGBA> colorize(Image<Correspondence> correspondence){
		for(int x = 0; x < correspondence.cols(); x++){
			for(int y = 0; y < correspondence.rows(); y++){
				Correspondence c = correspondence.get(x, y);
				if (c != null) {
					callShader(x, y, c);
				}
			}
		}
		
		return shader.getImage();
	}
	
	protected void callShader(int x, int y, Correspondence c){

		//give each triangle their own grey color:
		//grey = triangle number / number of all triangles



		double greyColor = (double)(c.triangle)/c.mesh.tvi.length;
		RGBA color = new RGBA(greyColor, greyColor, greyColor);
		//System.out.println(c.mesh.colors); need to find the right index Probably in TCI
		//System.out.println(c.triangle); // index of triangle 0-3

		RGBA[] colors = {c.mesh.colors[c.mesh.tci[c.triangle].get(0)],c.mesh.colors[c.mesh.tci[c.triangle].get(1)],c.mesh.colors[c.mesh.tci[c.triangle].get(2)]};
		shader.setTriangleColors(colors);
		shader.handleTrianglePixel(x, y, c.triCoords);
		//TODO: Blatt 3, Aufgabe 3c)
	}
	
	public void clearImg(){
		this.shader.clearImage();
	}

	@Override
	public void setProjectionView(Matrix4 currentView) {
		if(projection == null){
			return;
		}
		
		projection.setView(currentView);
	}

	@Override
	public void rotateLights(Matrix4 rotation) {
	}
}
