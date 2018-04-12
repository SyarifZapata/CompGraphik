package testSuite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * Shamelessly copied from http://bioportal.weizmann.ac.il/course/prog2/ \ 
 * tutorial/uiswing/components/example-1dot4/ScrollablePicture.java
 * @author Manuel Kaufmann
 */

public class ScrollablePicture extends JLabel implements Scrollable {

	private static final long serialVersionUID = 1L;
	private int maxUnitIncrement = 1;
    private boolean missingPicture = false;

    public ScrollablePicture(ImageIcon i, int m) {
        super(i);
        if (i == null) {
            missingPicture = true;
            setText("No picture found.");
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setBackground(Color.BLACK);
        }
        maxUnitIncrement = m;

        //Let the user scroll by dragging to outside the window.
//        setAutoscrolls(true); //enable synthetic drag events
//        addMouseMotionListener(this); //handle mouse drags
    }

    //Methods required by the MouseMotionListener interface:
//    public void mouseMoved(MouseEvent e) { }
//    public void mouseDragged(MouseEvent e) {
//        //The user is dragging us, so scroll!
//        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
//        scrollRectToVisible(r);
//    }

    public Dimension getPreferredSize() {
        if (missingPicture) {
            return new Dimension(320, 480);
        } else {
            return super.getPreferredSize();
        }
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement
                   - currentPosition;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }
}