package affichageEtTests;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import minuties.DonneesAnglesSortedLinkedList;
import objets.Coordonnees;
import objets.DonneesPoint;
import objets.Signature;


/* Classe Fenetre
 * ---------------
 * Role : Affichage graphique de signature
 * --------------
 */

public class Fenetre extends JFrame {
	private static final long serialVersionUID = 1L;
	
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
		DonneesPoint[] tab = s.getDonnees();
		
		for(int i=0; i<tab.length-1; i++){
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i+1].x, tab[i+1].y);
			
			r.tracerSegment(a.fois(taille_signature), b.fois(taille_signature), rgb);
		}
		
		return r;
	}
	
	/* Les fonctions afficherTableau permettent d'afficher un tableau de double en fonction des indices.
	 * Les valeurs peuvent être quelconques. Tout est normalisé et adapté à l'affichage
	 * */
	public void afficherTableau(double[] t, int rgb){
		LinkedList<Integer> l=new LinkedList<Integer>();
		for(int i=0;i<t.length;i++)
			l.add(i);
		afficherTableau(t, l, rgb);
	}
	
	public void afficherTableau(double[] t){
		afficherTableau(t, 0xff000000);
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
		DonneesPoint[] tab = s.getDonnees();
		int len=tab.length;
		
		double duree=tab[0].t-tab[len-1].t;
		double vmin, vmax;
		vmin=tab[0].normeVitesse();
		vmax=vmin;
		for(int i=0; i<len-1; i++){
			if(tab[i].normeVitesse()<vmin)
				vmin=tab[i].normeVitesse();
			if(tab[i].normeVitesse()>vmax)
				vmax=tab[i].normeVitesse();
		}
		
		Coordonnees l=new Coordonnees(1, (tab[0].normeVitesse()-vmin)/(vmax-vmin));
		Coordonnees c;
		
		for(int i=1; i<len-1; i++){
			c = new Coordonnees((tab[i].t-tab[len-1].t)/duree,(tab[i].normeVitesse()-vmin)/(vmax-vmin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			l=c;
		}
		
		// supr
		double smin, smax;
		smin=tab[0].s;
		smax=smin;
		for(int i=0; i<len-1; i++){
			if(tab[i].s<smin)
				smin=tab[i].s;
			if(tab[i].s>smax)
				smax=tab[i].s;
		}
		
		l=new Coordonnees(1, (tab[0].s-smin)/(smax-smin));
		
		for(int i=1; i<len-1; i++){
			c = new Coordonnees((tab[i].t-tab[len-1].t)/duree,(tab[i].s-smin)/(smax-smin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			System.out.println(tab[i].s);
			l=c;
		}
		
		double dtot=0, d=0;
		for(int i=0; i<len-1; i++)
			dtot=dtot+tab[i].norme();
		l=new Coordonnees(0, (tab[0].normeVitesse()-vmin)/(vmax-vmin));
		
		for(int i=1; i<len-1; i++){
			d=d+tab[i].norme();
			c = new Coordonnees(d/dtot,(tab[i].normeVitesse()-vmin)/(vmax-vmin));
			//r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);
			l=c;
		}
		double a[]=new double[len-2];
		for(int i=0;i<len-2;i++)
			a[i]=tab[i].differenceVitesses(tab[i+1])/(tab[i].t-tab[i+1].t);
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
			c = new Coordonnees((tab[i].t-tab[len-1].t)/duree,(a[i]-amin)/(amax-amin));
			//r.tracerSegment(l.fois(0.5*taille_signature), c.fois(0.5*taille_signature), 0xff000000);
			l=c;
		}
		// spur
		
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
		
	}
	
	public void afficherVMoyenneMobile(Signature s, int n){
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		
		DonneesPoint[] tab = s.getDonnees();
		int len=tab.length;
		
		double duree=tab[0].t-tab[len-n].t;
		double vmin, vmax, v;
		vmin=0;
		for(int i=0; i<n; i++){
			
			vmin=vmin+tab[i].normeVitesse();
		}
		vmax=vmin;
		for(int i=1; i<len-n; i++){
			v=0;
			for(int j=0; j<n; j++){
				v=v+tab[i+j].normeVitesse();
			}
			if(v<vmin)
				vmin=v;
			if(v>vmax)
				vmax=v;
		}

		
		v=0;
		for(int i=0; i<n; i++){
			v=v+tab[i].normeVitesse();
		}
		Coordonnees l=new Coordonnees(1, (v-vmin)/(vmax-vmin));
		Coordonnees c;
		
		for(int i=1; i<len-n; i++){
			v=0;
			for(int j=0; j<n; j++){
				v=v+tab[i+j].normeVitesse();
			}
			c = new Coordonnees((tab[i].t-tab[len-n].t)/duree,(v-vmin)/(vmax-vmin));
			r.tracerSegment(l.fois(0.9*taille_signature), c.fois(0.9*taille_signature), 0xff000000);			//System.out.println(l.fois(0.9*taille_signature).x+","+l.fois(0.9*taille_signature).y+"   ->   "+c.fois(0.9*taille_signature).x+","+c.fois(0.9*taille_signature).y);
			l=c;
		}
		
		this.getContentPane().add(new ImageComponent(r));
		this.setVisible(true);
		
	}

	Image conversionVariations(Signature s){
		
		Image r = new Image(taille_signature, taille_signature, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();
		
		int rgb, c=0xff000000;
		double vmin=tab[0].normeVitesse(), vmax=vmin;
		
		for(int i=0; i<tab.length-1; i++){
			if(tab[i].normeVitesse()<vmin)
				vmin=tab[i].normeVitesse();
			if(tab[i].normeVitesse()>vmax)
				vmax=tab[i].normeVitesse();
		}
		
		double t;
		for(int i=0; i<tab.length-1; i++){
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i+1].x, tab[i+1].y);
			t=(tab[i].normeVitesse()-vmin)/(vmax-vmin);
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
	
	
	//Ajoute la signature a la fenetre, avec la couleur rgb
	public void ajouter(Signature s, int rgb){
		this.getContentPane().add(new ImageComponent(conversion(s, rgb)));
		this.setVisible(true);
	}
	
	public void ajouterSignatureEtMinuties(Signature s, DonneesAnglesSortedLinkedList l, int n){
		Image tmp =conversion(s, 0xff000000);
		DonneesPoint[] tab = s.getDonnees();
		
		for(int i=0; i<n; i++){
			Coordonnees a = new Coordonnees(tab[l.get(i).j].x,tab[l.get(i).j].y);
			Coordonnees b = new Coordonnees(tab[l.get(i).i].x,tab[l.get(i).i].y);
			Coordonnees c = new Coordonnees(tab[l.get(i).k].x,tab[l.get(i).k].y);
			tmp.tracerSegment(a.fois(taille_signature), b.fois(taille_signature), 0xFFFF1111);
			tmp.tracerSegment(c.fois(taille_signature), b.fois(taille_signature), 0xFFFF1111);
		}
		this.getContentPane().add(new ImageComponent(tmp));
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
