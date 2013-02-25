package affichageEtTests;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;

import comparaison.Analyse;

import objets.Gabarit;
import objets.Signature;

/* Classe Tests
 * ---------------
 * Role : Tester des modifications avant le commit (voir le push)
 *        Demonstration aux autres developpeurs des nouvelles avancees
 * --------------
 */

public class Tests {


	public static void main(String[] args) {
		SerieTests();
	}
	
	// Test sur 100 signatures
	public static void SerieTests () {
		Signature sRef = new Gabarit().sRef;
		Fenetre f1=new Fenetre();
		f1.ajouter(sRef);
		
		double[] tabPositions = new double[100];
		double[] tabVitesses = new double[100];
		double[] tabPressions = new double[100];
		
		for (int i = 0; i<100; i++) {
			Signature sTest = new Signature();
			Analyse.similitudes(new Signature (sRef.donnees), sTest); // on ne veut pas modifier sRef en la coupant (on passe par un tiers)
			tabPositions[i] = Analyse.scorePositions(sTest, sRef);
			tabVitesses[i] = Analyse.scoreVitesses(sTest, sRef);
			tabPressions[i] = Analyse.scorePressions(sTest, sRef);
		}
		
		
		final String NEW_LINE = System.getProperty("line.separator" );
		
		//On ��crit dans le fichier scorePositions.txt
		try {
			//Ouverture du fichier .txt
			FileWriter fichier = new FileWriter("scorePositions" + ".txt");
			
			//Enregistrement
			fichier.write("GROOOOOOVANI EST UN TOS...");
			for(int j=0; j<100;j++) {
				fichier.write("" + NEW_LINE + tabPositions[j]);
			}
			fichier.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'a pu etre ouvert, " +
					" ou un probleme d'ecriture a ete rencontre"); }
		
		
		// On ��crit dans le fichier scoreVitesses.txt
		try {
			//Ouverture du fichier .txt
			FileWriter fichier = new FileWriter("scoreVitesses" + ".txt");
			
			//Enregistrement
			fichier.write("... Et arthur un daron");
			for(int j=0; j<100;j++) {
				fichier.write("" + NEW_LINE + tabVitesses[j]);
			}
			fichier.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'a pu etre ouvert, " +
					" ou un probleme d'ecriture a ete rencontre"); }
		
		
		//On ��crit dans le fichier scorePositions.txt
				try {
					//Ouverture du fichier .txt
					FileWriter fichier = new FileWriter("scorePressions" + ".txt");
					
					//Enregistrement
					fichier.write("GROOOOOOVANI EST UN TOS...");
					for(int j=0; j<100;j++) {
						fichier.write("" + NEW_LINE + tabPressions[j]);
					}
					fichier.close();
				}
				catch (IOException e) {
					System.out.println("Le fichier n'a pu etre ouvert, " +
							" ou un probleme d'ecriture a ete rencontre"); }
	}
}




class ImageViewer extends JFrame {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int location=100;
	
    public ImageViewer(BufferedImage img) {
    	this.setLocation(location, location);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageComponent ic = new ImageComponent(img);
        add(ic);
        pack();
        setVisible(true);
        location=location+100;
    }
    
    public ImageViewer(BufferedImage img, String name) {
        this.setTitle(name);
    	this.setLocation(location, location);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageComponent ic = new ImageComponent(img);
        add(ic);
        pack();
        setVisible(true);
        location=location+100;
    }

}

class ImageComponent extends JComponent {

	private static final long serialVersionUID = 1225166596942897848L;
	private BufferedImage img;

    public ImageComponent(BufferedImage img) {
        this.img = img;
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }   
}
