package minuties;

public class Donnee {
	int l, d, f, posMoy;
	double m;
	
	Donnee(double[] t, int d, int f, int s){
		this.d=d;
		this.f=f;
		l=f-d+1;
		
		m=0;
		for(int i=s;i<s+l;i++)
			m=m+t[i];
		m=m/l;
		
		posMoy=(f+d)/2;
	}
}
