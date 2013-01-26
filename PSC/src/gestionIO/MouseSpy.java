package gestionIO;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import objets.Coordonnees;

public class MouseSpy extends MouseInputAdapter {
	boolean estPose;
	Coordonnees positionActuelle;
	
	public MouseSpy() {
		positionActuelle = new Coordonnees(0,0);
	}

	public void mousePressed(MouseEvent e) {
		this.estPose = true;
	}
	
	public void mouseReleased(MouseEvent e) {
		this.estPose = false;
	}
	
	public void mouseMoved(MouseEvent e) {
		this.positionActuelle.x = e.getX();
		this.positionActuelle.y = e.getY();
	}
	
	public void mouseDragged(MouseEvent e) {
		this.positionActuelle.x = e.getX();
		this.positionActuelle.y = e.getY();
	}
}