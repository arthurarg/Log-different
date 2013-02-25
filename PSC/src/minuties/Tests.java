package minuties;
import gestionIO.Enregistrement;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

import objets.DonneesPoint;
import objets.Signature;

import affichageEtTests.Fenetre;

public class Tests {


	public static void main(String[] args) {
		/*Fenetre f=new Fenetre();
		f.setText("test       ");
		for(int i=0;i<200;i++)
			f.setPixel(i, 5, 0xffff0000);*/
		Signature s;
		Fenetre f=new Fenetre();
		f.setText("test       ");
		Minuties m;
		double c;
		while(true){s=new Signature();
		f.afficherVMoyenneMobile(s, 5);}
		/*while(true){
			s=new Signature();
			f.vider();
			m=new Minuties(s);
			int couleur=0;
			for(DonneesMinutie x:m.m){
				if(x.st==1)
					c=1.0;
				else if(x.st==2)
					c=0.75;
				else if(x.st==3)
					c=0.50;
				else c=0.25;
				
				if(x.t==4)
					couleur=0xffff0000;
				else if(x.t==1)
					couleur=0;//couleur=0xff0000ff;
				else if(x.t==3)
					couleur=0;//couleur=0xff0b0b0b;
				f.setPoint((int)(s.donnees[x.p].x*f.taille_signature), (int)(s.donnees[x.p].y*f.taille_signature), (int)(c*couleur));
			}
			f.ajouterVariations(s);
		}*/
			
	}
}




class ImageViewer extends JFrame {
    
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

    private BufferedImage img;

    public ImageComponent(BufferedImage img) {
        this.img = img;
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
    }

    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }   
}
