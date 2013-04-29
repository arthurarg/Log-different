package objets;

/* Classe Coordonnees
 * ---------------
 * Role : Objet de base pour manipuler des coordonnees
 * --------------
 */

public class Coordonnees {
	
	public double x, y;
	
	public Coordonnees() {
	}
	
	public Coordonnees(double x, double y) {
		this.x=x;
		this.y=y;
	}

	public boolean equals(Coordonnees c) {
		return (this.x==c.x && this.y==c.y);
	}
	
	public String toString() {
		return ("("+this.x + "," + this.y + ")");
	}
	
	public Coordonnees fois(double x){
		return new Coordonnees(x*this.x, x*this.y);
	}
	
	public Coordonnees plus(double n, double m){
		return new Coordonnees(this.x+n, this.y+m);
	}
}