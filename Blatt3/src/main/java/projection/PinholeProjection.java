package projection;

import illumination.PointLight;

import utils.Matrix3;
import utils.Vector3;
import utils.Vector4;
import utils.Matrix4;

public class PinholeProjection extends Projection {

	protected Matrix4 camera, view, projection;

	public PinholeProjection(int width, int height){
		super(width, height);
		initializeCamera();
		initializeView();
		projection = Matrix4.multiply(camera, view);
	}

	private void initializeCamera(){
		camera = new Matrix4();
		//TODO: Blatt 3
		camera.set(0,0,width);
		camera.set(1,1, height);
		camera.set(0,2,width/2);
		camera.set(1,2,height/2);
	}

	private void initializeView(){
		view = new Matrix4();
	}

	@Override
	public Vector3 project(Vector3 pt) {

		//TODO: Blatt 3
		Vector4 vector4 = new Vector4(pt.x,pt.y,pt.z,1);

		Vector4 result = projection.multiply(vector4);
		double x, y, z;

		if(result.z!=0){
			x = result.x/result.z;
			y = result.y/result.z;
			z = result.z;
		}else{
			x = result.x;
			y = result.y;
			z = result.z;
		}

		return new Vector3(x,y,z);
	}

	@Override
	public Matrix4 getMatrix(){
		return projection;
	}

	public Matrix4 getView(){
		return view;
	}

	public void setView(Matrix4 m){
		view = m;
		projection = Matrix4.multiply(camera, view);
	}

	public void setCamera(Matrix4 m){
		camera = m;
		projection = Matrix4.multiply(camera, view);
	}

	public Vector4 getEye(){
		Matrix4 ti = new Matrix4(projection);
		ti = ti.inverted();

		Vector4 e = ti.multiply(new Vector4(0,0,0,1));
		e = e.times(1.0/e.w);

		return e;
	}

	public Matrix4 getViewMatrixOfLightSource(PointLight lightSource){
		//TODO: Blatt 5, Aufgabe 1 a.)
		double l = lightSource.position.length();

		//z
		Vector3 z = new Vector3(lightSource.position.x/l,lightSource.position.y/l, lightSource.position.z/l);

		//y
		Vector3 x0 = new Vector3(1,0,0);
		Vector3 temp = lightSource.position.cross(x0);
		l = lightSource.position.cross(x0).length();
		Vector3 y = new Vector3(temp.x/l,temp.y/l,temp.z/l);

		//x
		l = y.cross(z).length();
		temp = y.cross(z);
		Vector3 x = new Vector3(temp.x/l,temp.y/l,temp.z/l);

		Matrix4 m = new Matrix4();
		m.set(0,0,x.x);
		m.set(0,1,x.y);
		m.set(0,2,x.z);
		m.set(1,0,y.x);
		m.set(1,1,y.y);
		m.set(1,2,y.z);
		m.set(2,0,z.x);
		m.set(2,1,z.y);
		m.set(2,2,z.z);


		//d
		m.set(0,3,0);
		m.set(1,3,0);
		m.set(2,3, -lightSource.position.length()); //negative z Richtung
		//System.out.println(m);

		return  m;
	}

}
// d = x = 0, y = 0, z = - lightsource.position.length