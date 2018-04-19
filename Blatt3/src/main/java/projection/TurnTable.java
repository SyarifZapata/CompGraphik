package projection;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import utils.Matrix4;
import utils.Vector3;

public class TurnTable implements MouseListener, MouseMotionListener {
	
	private TurnableRenderer renderer;
	private TurnableTest test;
	private int w, h, mouseX, mouseY;
	private Vector3 translation;
	public double elevation, azimuth, zoomStep;
	
	private Matrix4 currentView;
	
	public TurnTable(TurnableRenderer renderer, TurnableTest test, int w, int h){
		this.w = w;
		this.h = h;
		this.renderer = renderer;
		this.test = test;
		this.elevation = test.getRotationX();
		this.azimuth = test.getRotationY();
		this.currentView = test.getView();
		this.translation = test.getTranslation();
		zoomStep = 0.2;
		updateView(azimuth, elevation);
	}
	
	private void updateView(double azimuth, double elevation) {
		//System.out.println("UpdateView");
		//System.out.println(translation);
		currentView = buildViewMatrix(azimuth, elevation, translation);
		//System.out.println(currentView);
		setRendererViewMatrix(currentView);
		//System.out.println("Updated");
	}
	
	private Matrix4 buildViewMatrix(double azimuth, double elevation, Vector3 translation){
		//TODO: Blatt 3, Aufgabe 2
		Matrix4 zwischenRes = Projection.getRotationY(azimuth);
		//System.out.println(zwischenRes);
		currentView = Matrix4.multiply(zwischenRes,Projection.getRotationZ(elevation));
		//System.out.println(result);
		currentView.set(0,3,translation.x);
		currentView.set(1,3,translation.y);
		currentView.set(2,3,translation.z);
		//System.out.println(currentView);
		return currentView;
	}
	
	private void setRendererViewMatrix(Matrix4 currentView){
		renderer.setProjectionView(currentView);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.isShiftDown()){
			zoom(e.getY() < mouseY);
			mouseY = e.getY();
		} else {
			if(SwingUtilities.isRightMouseButton(e)){
				double lazimuth = handleAzimuth(e.getX(), 0);
				double lelevation = handleElevation(e.getY(), 0);
	
				Matrix4 lrot = buildViewMatrix(lazimuth, lelevation, new Vector3(0,0,0));
				renderer.rotateLights(lrot);
			}else{
				azimuth = handleAzimuth(e.getX(), azimuth);
				elevation = handleElevation(e.getY(), elevation);
				
				updateView(azimuth, elevation);
			}
			test.draw();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {	}
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }

	private void zoom(boolean in){
		//TODO: Blatt 3, Aufgabe 2


		if(in){
//			currentView.set(2,3, currentView.get(2,3)+zoomStep);
			translation = translation.plus(new Vector3(0,0,zoomStep));

		}else {
//			currentView.set(2,3, currentView.get(2,3)-zoomStep);
			translation = translation.minus(new Vector3(0,0,zoomStep));
		}
		
		updateView(azimuth, elevation);
		//System.out.println(currentView);
		test.draw();
	}
	
	private double handleAzimuth(int newMouseX, double azimuth){
		//TODO: Blatt 3, Aufgabe 2
		double x = mouseX - newMouseX;
		double ratio = (x/w)*180;
		ratio = ratio * (Math.PI/180);
		azimuth = ratio;

		return azimuth;
	}

	private double handleElevation(int newMouseY, double elevation){
		//TODO: Blatt 3, Aufgabe 2
		double y = mouseY - newMouseY;
		double ratio = (y/h)*180;
		ratio = ratio * (Math.PI/180);
		elevation = ratio;
		return elevation;
	}
	
	public String toString(){
		return "rotationX: " + elevation + 
				" rotationY: " + azimuth + 
				" translation: (" + translation.x + ", " + translation.y + ", " + translation.z + ")";
	}

}
