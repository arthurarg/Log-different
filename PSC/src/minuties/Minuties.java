package minuties;
import java.util.LinkedList;

import objets.Signature;
import affichageEtTests.Image;

public class Minuties {
	double vmax, vmin;
	Signature s;
	LinkedList<DonneesMinutie> m;
	double v[], a[]; // taux
	int p[], pInv[];
	int length;
	
	Minuties(Signature s){
		this.s=s;
		
		s.inverserDonnees();
		
		length=s.donnees.length;
		m=new LinkedList<DonneesMinutie>();
		v=new double[length];
		a=new double[length];
		p=new int[length];
		pInv=new int[length];
		
		vitessesLimites();
		
		detection(4);
	}
	
	void detection4(int n){
		double a[]=new double[length-1];
		for(int i=0;i<length-1;i++)
			a[i]=s.donnees[i].differenceVitesses(s.donnees[i+1])/(s.donnees[i].t-s.donnees[i+1].t);
		int[] p=trie(a);
		LinkedList<Donnee> al=lissage(a, p);
		
		int limite=Image.min(n, al.size()), i=1;
		double aMax=al.get(0).m, aMin=al.get(limite-1).m;
		for(Donnee x:al){
			System.out.println(1+(int)(4*(aMax-x.m)/(aMax-aMin)));
			m.add(new DonneesMinutie(x.posMoy, 4, x.m, 1+(int)(4*(aMax-x.m)/(aMax-aMin))));
			if(i>=2)
				break;
			else i++;
		}
	}
	
	LinkedList<Donnee> lissage(double [] t, int [] p){
		LinkedList<Donnee> l=new LinkedList<Donnee>();
		
		int j=1, d=0, f=0, s=1;
		
		while(j<t.length){
			if(p[j]==p[d]-1)
				d=j;
			else if(p[j]==p[f]+1)
				f=j;
			else{
				l.add(new Donnee(t, p[d], p[f], s));
				d=j;
				f=j;
				s=j;
			}
			j++;
		}
		return l;
	}
	
	int[] trie(double[] t){
		int l=t.length;
		int p[]= new int[l];
		for(int i=0;i<l;i++){
			p[i]=i;
		}
		
		int i=0;
		while(i<l-1){
			if(t[i+1]>t[i]){
				swap(t, i, i+1);
				swap(p, i, i+1);
				i=Image.max(0, i-1);
			}
			else i++;
		}
		return p;
	}
	
	void vitessesLimites(){
		vmin=s.donnees[0].normeVitesse();
		vmax=vmin;
		for(int i=0; i<s.donnees.length-1; i++){
			if(s.donnees[i].normeVitesse()<vmin)
				vmin=s.donnees[i].normeVitesse();
			if(s.donnees[i].normeVitesse()>vmax)
				vmax=s.donnees[i].normeVitesse();
		}
	}
	
	double t(int i){
		return (s.donnees[i].normeVitesse()-vmin)/(vmax-vmin);
	}
	
	void swap(double[] t, int i, int j){
		double e=t[i];
		t[i]=t[j];
		t[j]=e;
	}
	
	void swap(int[] t, int i, int j){
		int e=t[i];
		t[i]=t[j];
		t[j]=e;
	}
	
	void trieVitesses(){ // decroissant
		int l=s.donnees.length;
		for(int i=0;i<l;i++){
			v[i]=t(i);
			p[i]=i;
		}
		
		int i=0;
		while(i<l-1){
			if(v[i+1]>v[i]){
				swap(v, i, i+1);
				swap(p, i, i+1);
				i=Image.max(0, i-1);
			}
			else i++;
		}
		for(int k=0;k<l;k++)
			pInv[p[k]]=k;
	}
	
	void trieAngles(){ // decroissant
		int l=s.donnees.length;
		for(int i=0;i<l-2;i++){
			a[i]=angle(i+1, i+2);
			p[i]=i;
		}
		
		int i=0;
		while(i<l-1){
			if(a[i+1]>a[i]){
				swap(a, i, i+1);
				swap(p, i, i+1);
				i=Image.max(0, i-1);
			}
			else i++;
		}
		for(int k=0;k<l;k++)
			pInv[p[k]]=k;
	}
	
	void inverser(){
		int l=s.donnees.length;
		for(int i=0;i<(int)(l/2);i++){
			swap(v, i, l-1-i);
			swap(p, i, l-1-i);
			pInv[p[i]]=i;
			pInv[p[l-1-i]]=l-1-i;
		}
	}
	
	void aff(){
		int l=s.donnees.length;
		for(int i=0;i<l;i++){
			System.out.print(p[i]+" - ");
		}
		System.out.println("");
		for(int i=0;i<l;i++){
			System.out.print(v[i]+" - ");
		}
		System.out.println("");
	}
	
	void aff2(){
		int l=s.donnees.length;
		for(int i=0;i<l;i++){
			System.out.print(p[i]+" - ");
		}
		System.out.println("");
		for(int i=0;i<l;i++){
			System.out.print(a[i]+" - ");
		}
		System.out.println("");
	}
	
	public void detection(int n){
		trieVitesses();
		detection1(n);
		
		/*inverser();
		detection2(n);
		
		trieAngles();
		detection3(n);*/
		
		detection4(n);
	}

	void detection1(int n){ // grandes vitesses
		DonneesMinutie m[]=new DonneesMinutie[n];
		
		int i=0, j=1;
		int d=0, f=0;
		while(i<n && j<s.donnees.length){
			if(p[j]==p[d]-1)
				d=j;
			else if(p[j]==p[f]+1)
				f=j;
			else{
				m[i]=moyenneVitesses(d, f, 1);
				i++;
				d=j;
				f=j;
			}
			j++;
		}// � la fin de la boucle i=n si "s.donnees.length" suffisamment grand ; dans tous les cas, i minuities d�tect�es
		
		double t=(1-m[i-1].c)/i;
		for(int k=0;k<i;k++)
			m[k].st = 4-(int)( (m[k].c-m[i-1].c)/t ) ;
		if(m[0].st==0)
			m[0].st=1;
		
		for(int k=0;k<2;k++)// 2->i-1
			this.m.add(m[k]);
	}
	
	void detection2(int n){ // faibles vitesses
		DonneesMinutie m[]=new DonneesMinutie[n];
		
		int i=0, j=1;
		int d=0, f=0;
		while(i<n && j<s.donnees.length){
			if(p[j]==p[d]-1)
				d=j;
			else if(p[j]==p[f]+1)
				f=j;
			else{
				m[i]=moyenneVitesses(d, f, 2);
				i++;
				d=j;
				f=j;
			}
			j++;
		}// � la fin de la boucle i=n si "s.donnees.length" suffisamment grand ; dans tous les cas, i minuities d�tect�es
		
		double t=(m[i-1].c)/i;
		for(int k=0;k<i;k++)
			m[k].st = 5-(int)( (m[i-1].c-m[k].c)/t ) ;
		if(m[i-1].st==0)
			m[i-1].st=1;
		
		for(int k=0;k<i-1;k++){
			this.m.add(m[k]);System.out.println(m[k]);}
	}
	
	void detection3(int n){ // angles
		DonneesMinutie m[]=new DonneesMinutie[n];aff2();
		
		int i=0, j=1;
		int d=0, f=0;
		while(i<n && j<s.donnees.length){
			if(p[j]==p[d]-1)
				d=j;
			else if(p[j]==p[f]+1)
				f=j;
			else{
				m[i]=moyenneAngles(d, f);
				i++;
				d=j;
				f=j;
			}
			j++;
		}// � la fin de la boucle i=n si "s.donnees.length" suffisamment grand ; dans tous les cas, i minuities d�tect�es
		
		double t=(1-m[i-1].c)/i;
		for(int k=0;k<i;k++)
			m[k].st = 4-(int)( (m[k].c-m[i-1].c)/t ) ;
		if(m[0].st==0)
			m[0].st=1;
		
		for(int k=0;k<i-1;k++)
			this.m.add(m[k]);
	}
	
	DonneesMinutie moyenneVitesses(int d, int f, int t){
		double m=0;
		for(int i=p[d];i<=p[f];i++)
			m=m+v[pInv[i]];
		return new DonneesMinutie((int)((p[f]+p[d])/2), t, m/(p[f]-p[d]+1.0));
	}
	
	DonneesMinutie moyenneAngles(int d, int f){
		double m=0;
		for(int i=p[d];i<=p[f];i++)
			m=m+a[pInv[i]];
		return new DonneesMinutie((int)((p[f]+p[d])/2), 3, m/(p[f]-p[d]+1.0));
	}
	
	public void detectionT1(){
		double t;
		boolean b1=false, b2=false;
		
		for(int i=0; i<s.donnees.length-1; i++){
			t=t(i);
			
			if(t>0.99 && b1==false){
				b1=true;
				m.add(new DonneesMinutie(i, 1));
			}
			if(t<0.99 && b1==true){
				b1=false;
				m.add(new DonneesMinutie(i, 1));
			}
			
			if(t<0.10 && b2==false){
				b2=true;
				m.add(new DonneesMinutie(i, 2));
			}
			if(t>0.10 && b2==true){
				b2=false;
				m.add(new DonneesMinutie(i, 2));
			}
		}
	}
	
	public void detectionT2(){
		for(int i=1; i<s.donnees.length-1; i++){
			if(angle(i, i+1)>Math.PI/2){
				m.add(new DonneesMinutie(i, 3));
			}
		}
	}
	
	double angle(int i, int j){
		return Math.abs(s.donnees[i].angleVecteurVitesse()-s.donnees[j].angleVecteurVitesse());
	}
}
