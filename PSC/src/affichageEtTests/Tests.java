package affichageEtTests;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import objets.DonneesPoint;
import objets.Signature;

import comparaison.Analyse;


/* Classe Tests
 * ---------------
 * Role : Tester des modifications avant le commit (voir le push)
 *        Demonstration aux autres developpeurs des nouvelles avancees
 * --------------
 */

public class Tests {


	public static void main(String[] args) {
		Signature sRef = new Signature();
		Signature sTest = new Signature();
		
		sRef.calculs();
		sTest.calculs();
		
		 DonneesPoint[] tabRef = new DonneesPoint[sRef.donnees.length];
			for(int i=0;i<sRef.donnees.length;i++){
				tabRef[i] = sRef.donnees[i];
			}
		Signature sRefSave = new Signature(tabRef);
		
		 DonneesPoint[] tabTest = new DonneesPoint[sTest.donnees.length];
			for(int i=0;i<sTest.donnees.length;i++){
				tabTest[i] = sTest.donnees[i];
			}
		Signature sTestSave = new Signature(tabTest);

		double[] infos = Analyse.similitudes(sRef,sTest);
		
		int n1avant = Math.max((int) Math.floor(infos[2]*sRef.donnees.length), 0);
		int n1apres = Math.max((int) Math.floor(infos[3]*sRef.donnees.length), 0);
		int n2avant = Math.max((int) Math.floor(-infos[2]*sTest.donnees.length), 0);
		int n2apres = Math.max((int) Math.floor(-infos[3]*sTest.donnees.length), 0);

		
		Fenetre f1=new Fenetre();	
		Fenetre f2=new Fenetre();

		
		f1.ajouter(sRefSave);
		f1.ajouter(sTestSave,0xFFFF1111);
		f2.ajouter(sRef);
		f2.ajouter(sTest,0xFFFF1111);


		System.out.println("Decoupages : " + "["+n1avant+"][ sRef ]["+n1apres+"]" + "   -   " + "["+n2avant+"][ sTest ]["+n2apres+"]");
		System.out.println("Theta = "+infos[0]+"   -   "+"Lambda = "+infos[1]);
		System.out.println("Scores Positions : " + Analyse.scorePositions(sRefSave,sTestSave) + "   ->   " + Analyse.scorePositions(sRef,sTest));

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
