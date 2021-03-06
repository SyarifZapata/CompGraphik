package rasterization;

import image.Image;
import mesh.Mesh;
import projection.Projection;
import utils.BarycentricCoordinates;
import utils.Vector2;
import utils.Vector3;
import utils.Triplet;

/**
 * Rasterizes meshes into correspondence images
 */
public class MeshRasterizer implements TrianglePixelHandler{

	public static enum ZDirection{
		Forward,
		Backward;
	}

	protected Image<Correspondence> correspondenceImage;
	protected ZDirection zDir;
	protected double zd;
	protected Mesh currentMesh;
	protected int currentTriangle;
	protected double[] currentDepths;
	protected float cNear;
	protected boolean perspectiveCorrect;
	protected int width, height;

	public MeshRasterizer(int w, int h){
		width = w;
		height = h;
		zDir = ZDirection.Backward;
		zd = (zDir == ZDirection.Forward) ? 1.0 : -1.0;
		currentDepths = new double[3];
		cNear = 1.0f;
		perspectiveCorrect = false;
	}

	/**
	 * Rasterizes a single mesh into the correspondence image
	 */
	public Image<Correspondence> rasterize(Projection p, Mesh[] meshes){
		correspondenceImage = new Image<Correspondence>(width, height);
		TriangleRasterizer r = new TriangleRasterizer(this, width, height);
		return rasterize(p, r, meshes);
	}

	public Image<Correspondence> rasterize(Projection p, TriangleRasterizer r, Mesh[] meshes){

//		for(Mesh mesh : meshes){
//			//TODO: Blatt 3, Aufgabe 3 a)
//
//
//			for (int i = 0; i < mesh.tvi.length; i++){
//
//				Vector3 point_1 = mesh.vertices[mesh.tvi[i].get(0)];
//
//				Vector3 point_2 = mesh.vertices[mesh.tvi[i].get(1)];
//				Vector3 point_3 = mesh.vertices[mesh.tvi[i].get(2)];
//
//				Vector3 a = p.project(point_1);
//				Vector3 b = p.project(point_2);
//				Vector3 c = p.project(point_3);
//
//				Vector3[] vector3s = {a,b,c};
//
//				currentDepths = new double[]{vector3s[0].z,vector3s[1].z, vector3s[2].z};
//				currentMesh = mesh;
//				currentTriangle = i;
//
//				Vector2 vector2_1 = new Vector2(a.x,a.y);
//				Vector2 vector2_2 = new Vector2(b.x,b.y);
//				Vector2 vector2_3 = new Vector2(c.x,c.y);
//
//				Vector2[] vectors = {vector2_1,vector2_2,vector2_3};
//
//
//
//				if(currentDepths[0]<cNear && currentDepths[1]<cNear && currentDepths[2]<cNear){
//					r.rasterTriangle(vectors);
//				}
//
//			}
//
//			//TODO: Blatt 3, Aufgabe 4
//		}
//
//		return correspondenceImage;


		//von Michi
		Vector3[] vector3s = new Vector3[3];
		Vector2[] vector2s = new Vector2[3];

		for(Mesh mesh : meshes){

			//TODO: Blatt 3, Aufgabe 3 a)
			/*for(int i=0;i<mesh.tvi.length;i++){
				Vector3 a = mesh.vertices[mesh.tvi[i].get(0)];
				Vector3 b = mesh.vertices[mesh.tvi[i].get(1)];
				Vector3 c = mesh.vertices[mesh.tvi[i].get(2)];
				a = p.project(a);
				b = p.project(b);
				c = p.project(c);
				Vector2 aa = new Vector2(a.x,a.y);
				Vector2 bb = new Vector2(b.x,b.y);
				Vector2 cc = new Vector2(c.x,c.y);
				Vector2 tri[] = {aa,bb,cc};
				currentDepths = new double[]{a.z,b.z,c.z};
				currentMesh = mesh;
				currentTriangle = i;
				//if(currentDepths[0]>cNear&&currentDepths[1]>cNear&&currentDepths[2]>cNear)//near plane
				//todo clipping mit zd
				r.rasterTriangle(tri);
			}*/
			//TODO: Blatt 3, Aufgabe 4

			//von Michi
			currentMesh = mesh;

			for (int triangle=0; triangle < mesh.tvi.length; triangle ++) {

				currentTriangle = triangle;
				boolean draw = true; // clipping by depth (cNear)

				for (int i = 0; i < 3; i++) {
					vector3s[i] = p.project(mesh.vertices[mesh.tvi[triangle].get(i)]);
					vector2s[i] = new Vector2(vector3s[i].x, vector3s[i].y);

					currentDepths[i] = vector3s[i].z;
					//System.out.printf("currentDepths[i]: %.2f  -  (-cNear): %.2f  -  zd: %.2f\n",currentDepths[i],-cNear,zd);
					if (currentDepths[i] > -cNear) { draw = false; }
				}

				if (draw) { r.rasterTriangle(vector2s); }
			}

		}

		return correspondenceImage;
	}

	public void enablePerspectiveCorrect(boolean b){
		perspectiveCorrect = b;
	}

	@Override
	public void handleTrianglePixel(int x, int y, BarycentricCoordinates triCoords) {

		//TODO: Blatt 3, Aufgabe 3b)

		double d = triCoords.interpolate(currentDepths[0], currentDepths[1], currentDepths[2]);

		if (correspondenceImage.get(x,y) != null){
			if(correspondenceImage.get(x,y).depth < d){
				correspondenceImage.set(x, y, new Correspondence(currentMesh, currentTriangle, triCoords, d));
			}

		}else {
			correspondenceImage.set(x, y, new Correspondence(currentMesh, currentTriangle, triCoords, d));
		}

	}

	private BarycentricCoordinates getWorldLambda(BarycentricCoordinates oldLambda){

		double w0 = currentDepths[0];
		double w1 = currentDepths[1];
		double w2 = currentDepths[2];

		double d = w1*w2 + w2*oldLambda.y*(w0-w1) + w1*oldLambda.z*(w0-w2);

		if(d == 0) return oldLambda;

		double y = w0*w2*oldLambda.y/d;
		double z = w0*w1*oldLambda.z/d;
		double x = 1 - y - z;

		return new BarycentricCoordinates(x,y,z);
	}

}
