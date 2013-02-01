package affichageEtTests;

import gestionIO.PaveTUIO;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/* Classe affichageEtTests
 * ---------------
 * Role : - Paramètre de la fenêtre Jcomponent qui est ouverte lors de l'affichege en temps réèl
 * --------------
 */

public class FenetreTempsReel {
	private final int window_width = 640;
	private final int window_height = 480;

	static private PaveTUIO Courant;
	private JFrame frame;
	private GraphicsDevice device;


	public FenetreTempsReel(PaveTUIO demo) {
		Courant = demo;
		device = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		setupWindow();
		showWindow();
	}

	public void setupWindow() {

		frame = new JFrame();
		frame.add(Courant);

		frame.setTitle("Signatures en Temps Reel");
		frame.setResizable(false);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});

		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);

			}
		});
	}

	public void destroyWindow() {

		frame.setVisible(false);

		frame = null;
	}

	public void showWindow() {

		int width = window_width;
		int height = window_height;
		Courant.setSize(width, height);

		frame.pack();
		Insets insets = frame.getInsets();
		frame.setSize(width, height + insets.top);

		frame.setVisible(true);
		frame.repaint();

	}

}
