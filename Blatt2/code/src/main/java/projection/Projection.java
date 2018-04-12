package projection;

import illumination.PointLight;
import utils.Vector3;
import utils.Matrix4;
import utils.Vector4;

public abstract class Projection {

	public static Matrix4 getRotationX(double angle) {
		
		//TODO: Blatt 3
		double cosi = Math.cos(angle);
		double sini = Math.sin(angle);

		Matrix4 matrix4 = new Matrix4();
		matrix4.set(1,1,cosi);
		matrix4.set(2,2,cosi);
		matrix4.set(2,1,sini);
		matrix4.set(1,2,sini*-1);
		return matrix4;
	}

	public static Matrix4 getRotationY(double angle) {
		//TODO: Blatt 3
		double cosi = Math.cos(angle);
		double sini = Math.sin(angle);

		Matrix4 matrix4 = new Matrix4();
		matrix4.set(0,0,cosi);
		matrix4.set(2,2,cosi);
		matrix4.set(2,0,sini*-1);
		matrix4.set(0,2,sini);
		return matrix4;
	}

	public static Matrix4 getRotationZ(double angle) {
		//TODO: Blatt 3

		double cosi = Math.cos(angle);
		double sini = Math.sin(angle);

		Matrix4 matrix4 = new Matrix4();
		matrix4.set(0,0,cosi);
		matrix4.set(1,1,cosi);
		matrix4.set(1,0,sini);
		matrix4.set(0,1,sini*-1);
		return matrix4;
	}

	public static Matrix4 getTranslation(Vector3 trans) {
		//TODO: Blatt 3

		Matrix4 matrix4 = new Matrix4();
		matrix4.set(0,3,trans.x);
		matrix4.set(1,3,trans.y);
		matrix4.set(2,3,trans.z);
		return new Matrix4();
	}

	public static Matrix4 getScaling(Vector3 scale) {
		//TODO: Blatt 3

		Matrix4 matrix4 = new Matrix4();
		matrix4.set(0,0,scale.x);
		matrix4.set(1,1,scale.y);
		matrix4.set(2,2,scale.z);
		return matrix4;
	}

	protected int width, height;
	
	public Projection(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public abstract Vector3 project(Vector3 pt);
	public abstract Vector4 getEye();
	public abstract Matrix4 getViewMatrixOfLightSource(PointLight pointLight);
	public abstract Matrix4 getMatrix();
}
