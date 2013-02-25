package affichageEtTests;
import affichageEtTests.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import objets.Coordonnees;
import objets.Signature;


/* Classe Fenetre
 * ---------------
 * Role : Affichage graphique de signatures et/ou de texte
 * --------------
 */

public class Fenetre extends JFrame {
	
	public int taille_signature, taille;
	JLabel texte;
	Image points;
	
	//Construit une fenêtre vide, prete a afficher une signature et du texte
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
	}
	
	//Vide la fenêtre de tout contenu
	public void vider() {
		this.getContentPane().removeAll();
		points=new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		this.getContentPane().add(texte);
		this.getContentPane().add(new ImageComponent(points));
	}
	
	//Renvoie l'image de la signature pour affichage
	Image conversion(Signature s, int rgb){
		
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		Coordonnees l=new Coordonnees(s.donnees[0].x, s.donnees[0].y);
		
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			
			r.tracerSegment(a.fois(taille_signature), b.fois(taille_signature), rgb);
		}
		
		return r;
	}
	
	public void afficherTableau(double[] t){
		LinkedList<Integer> l=new LinkedList<Integer>();
		for(int i=0;i<t.length;i++)
			l.add(i);
		afficherTableau(t, l, 0xff000000);
	}
	
	public void afficherTableau(double[] t, LinkedList<Integer> list){
		afficherTableau(t, list, 0xff000000);
	}
	
	public void afficherTableau(double[] t, LinkedList<Integer> list, int rgb){
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		int len=t.length;
		
		double min, max;
		min=t[0];
		max=min;
		for(int i=0; i<len-1; i++){
			if(t[i]<min)
				min=t[i];
			if(t[i]>max)
				max=t[i];
		}
		
		Coordonnees l=new Coordonnees(0, (t[0]-min)/(max-min));
		Coordonnees c;
		
		for(int i:list){
			c = new Coordonnees((double)i/len,(t[i]-min)/(max-min));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), rgb);
			l=c;
		}
		
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
	}
	
	public void afficherV(Signature s){
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		int len=s.donnees.length;
		
		double duree=s.donnees[0].t-s.donnees[len-1].t;
		double vmin, vmax;
		vmin=s.donnees[0].normeVitesse();
		vmax=vmin;
		for(int i=0; i<len-1; i++){
			if(s.donnees[i].normeVitesse()<vmin)
				vmin=s.donnees[i].normeVitesse();
			if(s.donnees[i].normeVitesse()>vmax)
				vmax=s.donnees[i].normeVitesse();
		}
		
		Coordonnees l=new Coordonnees(1, (s.donnees[0].normeVitesse()-vmin)/(vmax-vmin));
		Coordonnees c;
		
		for(int i=1; i<len-1; i++){
			c = new Coordonnees((s.donnees[i].t-s.donnees[len-1].t)/duree,(s.donnees[i].normeVitesse()-vmin)/(vmax-vmin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			l=c;
		}
		
		// supr
		double smin, smax;
		smin=s.donnees[0].s;
		smax=smin;
		for(int i=0; i<len-1; i++){
			if(s.donnees[i].s<smin)
				smin=s.donnees[i].s;
			if(s.donnees[i].s>smax)
				smax=s.donnees[i].s;
		}
		
		l=new Coordonnees(1, (s.donnees[0].s-smin)/(smax-smin));
		
		for(int i=1; i<len-1; i++){
			c = new Coordonnees((s.donnees[i].t-s.donnees[len-1].t)/duree,(s.donnees[i].s-smin)/(smax-smin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			System.out.println(s.donnees[i].s);
			l=c;
		}
		
		double dtot=0, d=0;
		for(int i=0; i<len-1; i++)
			dtot=dtot+s.donnees[i].norme();
		l=new Coordonnees(0, (s.donnees[0].normeVitesse()-vmin)/(vmax-vmin));
		
		for(int i=1; i<len-1; i++){
			d=d+s.donnees[i].norme();
			c = new Coordonnees(d/dtot,(s.donnees[i].normeVitesse()-vmin)/(vmax-vmin));
			//r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			l=c;
		}
		double a[]=new double[len-2];
		for(int i=0;i<len-2;i++)
			a[i]=s.donnees[i].differenceVitesses(s.donnees[i+1])/(s.donnees[i].t-s.donnees[i+1].t);
		double amin, amax;
		amin=a[0];
		amax=amin;
		for(int i=0; i<len-2; i++){
			if(a[i]<amin)
				amin=a[i];
			if(a[i]>amax)
				amax=a[i];
		}
		
		l=new Coordonnees(1, (a[0]-amin)/(amax-amin));
		
		for(int i=1; i<len-2; i++){
			c = new Coordonnees((s.donnees[i].t-s.donnees[len-1].t)/duree,(a[i]-amin)/(amax-amin));
			//r.tracerSegment(l.fois(0.5*taille_signature), c.fois(0.5*taille_signature), 0xff000000);
			l=c;
		}
		// spur
		
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
		
	}
	
	public void afficherVMoyenneMobile(Signature s, int n){
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		int len=s.donnees.length;
		
		double duree=s.donnees[0].t-s.donnees[len-n].t;
		double vmin, vmax, v;
		vmin=0;
		for(int i=0; i<n; i++){
			
			vmin=vmin+s.donnees[i].normeVitesse();
		}
		vmax=vmin;
		for(int i=1; i<len-n; i++){
			v=0;
			for(int j=0; j<n; j++){
				v=v+s.donnees[i+j].normeVitesse();
			}
			if(v<vmin)
				vmin=v;
			if(v>vmax)
				vmax=v;
		}

		
		v=0;
		for(int i=0; i<n; i++){
			v=v+s.donnees[i].normeVitesse();
		}
		Coordonnees l=new Coordonnees(1, (v-vmin)/(vmax-vmin));
		Coordonnees c;
		
		for(int i=1; i<len-n; i++){
			v=0;
			for(int j=0; j<n; j++){
				v=v+s.donnees[i+j].normeVitesse();
			}
			c = new Coordonnees((s.donnees[i].t-s.donnees[len-n].t)/duree,(v-vmin)/(vmax-vmin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);			//System.out.println(l.fois(0.9*taille_signature).x+","+l.fois(0.9*taille_signature).y+"   ->   "+c.fois(0.9*taille_signature).x+","+c.fois(0.9*taille_signature).y);
			l=c;
		}
		
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
		
	}
	
	//Role inconnu
	//TODO ameliorer le commentaire de la ligne au-dessus
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
	
	
	//Role inconnu
	//TODO ameliorer le commentaire de la ligne au-dessus
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
	
	
	//Ajoute la signature a la fenetre, avec la couleur rgb
	public void ajouter(Signature s, int rgb){
		this.getContentPane().add(new ImageComponent(conversion(s, rgb)));
		this.setVisible(true);
	}
	
	//Ajoute la signature a la fenetre, couleur noire
	public void ajouter(Signature s){
		ajouter(s, 0xff000000);
	}
	
	public void ajouterVariations(Signature s){
		this.getContentPane().add(new ImageComponent(conversionVariations(s)));
		this.setVisible(true);
	}
	
	public void setPixel2(Coordonnees p, int rgb){
		points.setRGB((int)p.x, (int)p.y, rgb);
		this.setVisible(true);
	}
	
	public void tracerSegment(Coordonnees a, Coordonnees b, int rgb){
		points.tracerSegment(a, b, rgb);
		this.setVisible(true);
	}
	
	public void tracerSegment(Coordonnees a, Coordonnees b){
		points.tracerSegment(a, b, 0xff000000);
		this.setVisible(true);
	}
	
	public void tracerSegment(int ax, int ay, int bx, int by){
		points.tracerSegment(new Coordonnees(ax, ay), new Coordonnees(bx, by), 0xff000000);
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
	
	//Affiche du texte dans la fenetre
	public void setText(String t){
		texte.setText(t);
		texte.setVisible(true);
		this.setVisible(true);
	}
}
