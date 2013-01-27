package affichageEtTests;
import java.awt.image.BufferedImage;

import objets.Coordonnees;


/* Classe Image
 * ---------------
 * Role : 
 * --------------
 */


public class Image extends BufferedImage {

	public Image(int width, int height, int imageType) {
		super(width, height, imageType);
		vider();
	}
	
	public void vider(){
		for(int x=0;x<this.getWidth();x++)
			for(int y=0;y<this.getHeight();y++)
				this.setRGB(x, y, 0x0);
	}
	
	public void tracerSegment(Coordonnees a, Coordonnees b, int rgb){ // en pixel
		if(a.x<b.x){
			int l=(int)a.y;
			int y;
			for(int x=(int)a.x; x<(int)b.x; x++){
				y=(int)((x-a.x)*(b.y-a.y)/(b.x-a.x)+a.y);
				for(int z=min(l,y);z<=max(l,y);z++)
					this.setRGB(x, z, rgb);
				l=y;
			}
		}
		else if(a.x==b.x){
			for(int y=(int)min(a.y, b.y); y<(int)max(a.y, b.y); y++)
				this.setRGB((int)a.x, y, rgb);
		}
		else
			tracerSegment(b, a, rgb);
	}
	
	public static int min(int a, int b){
		if(a>b)
			return b;
		else return a;
	}
	
	public static int max(int a, int b){
		if(a<b)
			return b;
		else return a;
	}
	
	public static double min(double a, double b){
		if(a>b)
			return b;
		else return a;
	}
	
	public static double max(double a, double b){
		if(a<b)
			return b;
		else return a;
	}

}
