package minutiesV1;

public class DonneesMinutie {
	int p, t, st; // p : position ; t : type ; st : sous-type (varie de 1 � 4)
	double c; // c : valeur
	
	DonneesMinutie(){		
		c=0;
		st=0;
	}
	DonneesMinutie(int p, int t){
		this.p=p;
		this.t=t;
		c=0;
		st=0;
	}
	DonneesMinutie(int p, int t, double c){
		this.p=p;
		this.t=t;
		this.c=c;
		st=0;
	}
	DonneesMinutie(int p, int t, double c, int st){
		this.p=p;
		this.t=t;
		this.c=c;
		this.st=st;
	}
	public String toString(){
		return "( "+t+"."+st+" , "+p+" , "+c+" )";
	}
}
