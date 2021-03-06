package objets;

/* Classe DonneesPoint
 * ---------------
 * Role : Objet qui regroupe toutes les informations inherentes a un point
 * --------------
 */


public class DonneesPoint {
 
	public double x, y, t, vx, vy, s;
	
	
	//Constructeur, toString et equals

	public DonneesPoint (double x, double y, double t, double vx, double vy, double s) {
		this.x=x;
		this.y=y;
		this.t=t;
		this.vx=vx;
		this.vy=vy;
		this.s=s;
	}
	public String toString() {
		return ("("+this.x + "," + this.y + "," + this.t + "," + this.vx + "," + this.vy + "," +this.s +")");
	}
	
	//Renvoie true s'il s'agit du meme point
	public boolean equals(DonneesPoint c) {
		return (this.x==c.x && this.y==c.y && this.t==c.t && this.vx==c.vx && this.vy==c.vy && this.s==c.s);
	}
	
	//Ajoute deux DonneesPoint, avec un facteur lambda de pond��ration
	public DonneesPoint add(DonneesPoint c, double lambda) {
		return new DonneesPoint (1/lambda * c.x+this.x,
								1/lambda * c.y+this.y,
								1/lambda * c.t+this.t,
								1/lambda * c.vx+this.vx,
								1/lambda * c.vy+this.vy,
								1/lambda * c.s+this.s);
		
	}
	
	
	
	//Calculs basiques
	public double distance (DonneesPoint a) {
		return Math.sqrt(Math.pow(this.x - a.x,2) + Math.pow(this.y - a.y,2));
	}
	public double differenceVitesses (DonneesPoint a) {
		return Math.sqrt(Math.pow(this.vx - a.vx,2) + Math.pow(this.vy - a.vy,2));
	}
	public double normeVitesse () {
		return Math.sqrt(Math.pow(this.vx,2) + Math.pow(this.vy,2));
	}
	public double norme () {
		return Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
	}
	public double angleVecteurVitesse() {
		if (this.vx==0)
			if (this.vy!=0)
				return Math.signum(vy)*Math.PI/2;
			else 
				return  Double.POSITIVE_INFINITY;
		else if (this.vx > 0)
			return Math.atan(vy/vx);
		else
			return Math.PI + Math.atan(vy/vx);
	}
	
	
}
