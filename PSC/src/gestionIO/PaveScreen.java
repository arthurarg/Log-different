package gestionIO;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import objets.Coordonnees;


public class PaveScreen implements Pave {
	
	public JFrame screen;
	MouseSpy espion;
	
	
	public PaveScreen() {
	screen = new JFrame();

	//Dimensions
	int w = Toolkit.getDefaultToolkit().getScreenSize().width-100;
	int h = Toolkit.getDefaultToolkit().getScreenSize().height-100;
	screen.setSize(w,h);
	
	//Ajout d'un MouseInputListener
	espion = new MouseSpy();
	screen.addMouseListener(espion);
	screen.addMouseMotionListener(espion);
	
	//Ajout d'un panneau
	JPanel panneau = new JPanel();
	screen.add(panneau);
	
	//Rends la fenêtre visible
	screen.setVisible(true);
	}
	
	//Renvoi true si le doigt appuie sur le touchpad
	public boolean  pose() {
		return (espion.estPose);
	}
	//Renvoi la position actuelle du curseur
	public Coordonnees position(){
		return (espion.positionActuelle);
	}
	
	//Detruit la zone d'imput signature (ici un Jframe)
	public void destroy() {
		screen.setVisible(false);
		screen.dispose();
	}
}



