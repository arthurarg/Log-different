package affichageEtTests;
import affichageEtTests.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

import objets.Coordonnees;
import objets.Signature;

public class Fenetre extends JFrame {
	
	public int taille_signature, taille;
	JLabel texte;
	Image points;
	
	public Fenetre() {
		taille=500;
		this.setTitle("Signatures");
		this.setSize(taille, taille);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setVisible(true);
		
		taille_signature=taille;
		
		texte=new JLabel();
		texte.setLocation(0, 0);
		texte.setSize(taille, taille);
		texte.setHorizontalAlignment(JLabel.RIGHT);
		texte.setVerticalAlignment(JLabel.TOP);
		
		points=new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		
		this.getContentPane().add(texte);
		this.getContentPane().add(new ImageComponent(points));
		
		
		//texte.setVisible(true);//setVisible(true);
	}
	
	public void vider() {
		this.getContentPane().removeAll();
		points=new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		this.getContentPane().add(texte);
		this.getContentPane().add(new ImageComponent(points));
		
		//setVisible(true);
	}
	
	Image conversion(Signature s, int rgb){
		
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		Coordonnees l=new Coordonnees(s.donnees[0].x, s.donnees[0].y);
		
		//for(Coordonnees x:s.positions){
			//r.tracerSegment(l.fois(taille_signature), x.fois(taille_signature), rgb);
			//l=new Coordonnees(x.x, x.y);
		//}
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			
			r.tracerSegment(a.fois(taille_signature), b.fois(taille_signature), rgb);
		}
		
		return r;
	}
	
	Image conversionVariations(Signature s){
		
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		Coordonnees l=new Coordonnees(s.donnees[0].x, s.donnees[0].y);
		
		int rgb, c=0xff000000;
		double vmin=s.donnees[0].normeVitesse(), vmax=vmin;
		
		for(int i=0; i<s.donnees.length-1; i++){
			if(s.donnees[i].normeVitesse()<vmin)
				vmin=s.donnees[i].normeVitesse();
			if(s.donnees[i].normeVitesse()>vmax)
				vmax=s.donnees[i].normeVitesse();
		}
		
		double t;
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			t=(s.donnees[i].normeVitesse()-vmin)/(vmax-vmin);
			rgb=c+couleur(t);
			r.tracerSegment(a.fois(taille_signature), b.fois(taille_signature), rgb);
		}
		
		return r;
	}
	
	static int couleur(double t){
		int r, b;
		
		if(t<0.5){
			b=255;
			r=(int)(2*t*255);
		}
		else{
			r=255;
			b=(int)(255-2*(t-0.5)*255);
		}
		return b+r*0x010000;
	}
	
	public void ajouter(Signature s, int rgb){
		this.getContentPane().add(new ImageComponent(conversion(s, rgb)));
		this.setVisible(true);
	}
	
	public void ajouter(Signature s){
		ajouter(s, 0xff000000);
	}
	
	public void ajouterVariations(Signature s){
		this.getContentPane().add(new ImageComponent(conversionVariations(s)));
		this.setVisible(true);
	}
	
	public void setPixel2(Coordonnees p, int rgb){
		
		points.setRGB((int)p.x, (int)p.y, rgb);
		
		/*this.getContentPane().add(new ImageComponent(points));
		this.getContentPane().add(texte);*/
		
		this.setVisible(true);
	}
	
	public void setPixel1(Coordonnees p, int rgb){
		
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		r.setRGB((int)p.x, (int)p.y, rgb);
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
	}
	
	public void setPixel(int x, int y, int rgb){
		setPixel2(new Coordonnees(x, y), rgb);
	}
	
	public void setPoint(int x, int y, int rgb){
		int size=2;
		//Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		for(int i=Image.max(x-size, 1); i<=Image.min(x+size, taille); i++){
			for(int j=Image.max(y-size, 1); j<=Image.min(y+size, taille); j++)
				points.setRGB(i, j, rgb);//r.setRGB(i, j, rgb);
		}
		//this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
	}
	
	public void setText(String t){
		texte.setText(t);
		texte.setVisible(true);
		this.setVisible(true);
	}
}
