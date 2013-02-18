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
		//System.out.println(a.x+","+a.y+" -> "+b.x+","+b.y);
		int x0 = (int) a.x; int y0 = (int) a.y; int x1 = (int) b.x; int y1 = (int) b.y;
		if(x0<x1){
			int l=(int)a.y;
			int y;
			for(int x=x0; x<=x1; x++){
				y=(int)((x-x0)*(y1-y0)/(x1-x0)+y0);
				for(int z=min(l,y);z<=max(l,y);z++)
					//{System.out.println(x+" - "+z);
					{this.setRGB(x, z, rgb);}
				l=y;
			}
		}
		else if(x0==x1){
			for(int y=min(y0, y1); y<max(y0, y1); y++)
				//{System.out.println(x0+" "+y);
				{this.setRGB(x0, y, rgb);
			}
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
