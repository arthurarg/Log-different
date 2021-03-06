package objets;

import gestionIO.PaveGLULOGIC;
import gestionIO.PaveScreen;

import java.awt.Robot;
import java.util.LinkedList;

import minuties.MinutiesVectorielles;

/* Classe Signature
 * ---------------
 * Role : Caracterise une signature, qui est un tableau de DonneesPoint apres reparsing
 * --------------
 */

public class Signature {
	
	public enum Complexite{
		FAIBLE, MOYENNE, FORTE
	}
	
	public static boolean AffichageTpsReel = true;
	public static final int N = 100;
	Acquisition a;
	
	private DonneesPoint[] donnees;
	public boolean terminate;
	public boolean doigtPose;
	
	//renvoie une copie du tableau .donnees afin de le protéger
	public DonneesPoint[] getDonnees() {
		if (this.donnees == null)
			return null;
		
		DonneesPoint[] temp = new DonneesPoint[this.donnees.length];
		for (int i=0; i< temp.length; i++)
			temp[i] = new DonneesPoint(this.donnees[i].x,this.donnees[i].y,this.donnees[i].t, this.donnees[i].vx,this.donnees[i].vy,this.donnees[i].s);
		return temp;
	}
	
	public int size() {
		return N;
	}
	

	
	//Constructeur générique : s'occupe de la saisie
	public Signature() {
		// Trouve le nom de l'OS
		if (getOsName().equals("mac"))
			a = Acquisition.GLULOGIC;
		else
			a = Acquisition.WINDOWS;	
		
		//Initialise terminate
		this.terminate = false;
		this.doigtPose = false;
		
		//Initialise le champs signature
		init();
	}
	
	public Signature (boolean b) {
		// Trouve le nom de l'OS
		if (getOsName().equals("mac"))
			a = Acquisition.GLULOGIC;
		else
			a = Acquisition.WINDOWS;
		
		this.donnees = null;
		this.terminate = false;
		this.doigtPose = false;
		if (b)
			init();
	}
	
	// Construit la signature a partir d'un tableau de DonneesPoints
	public Signature(DonneesPoint[] tab) {
		this.donnees= new DonneesPoint[tab.length];
		for (int i=0;i<tab.length;i++)
			this.donnees[i]= new DonneesPoint(tab[i].x,tab[i].y,tab[i].t,tab[i].vx,tab[i].vy,tab[i].s);
		this.calculs();
	}
	
	// Recupère la signature
	public void init() {	
		if (a == Acquisition.WINDOWS) {

			// Variables utilisees lors de l'enregistrement
			LinkedList<Coordonnees> positions, vitesse;
			LinkedList<Long> temps;
			positions = new LinkedList<Coordonnees>();
			vitesse = new LinkedList<Coordonnees>();
			temps = new LinkedList<Long>();

			// Acquisition de la signature
			PaveScreen pave = new PaveScreen();

			// On attend que l'utilisateur pose son doigt sur le pavé
			doigtPose = false;
			while (!pave.pose()) {
				attendre(10);
			}
			Coordonnees p, l = new Coordonnees(0, 0);
			long t0 = System.currentTimeMillis();
			// On enregistre le trace tant que le doigt est sur le pave
			doigtPose = true;
			while (pave.pose()) {

				p = pave.position();
				if (!p.equals(l)) {
					positions.add(new Coordonnees(p.x / 1200, p.y / 1200));
					temps.add(System.currentTimeMillis() - t0);
				}

				l = new Coordonnees(p.x, p.y);
				attendre(1);
			}
			doigtPose = false;

			// On enregistre les vecteurs vitesse a partir de calculs effectues
			// sur positions et temps
			boolean premier = true;
			Coordonnees c;
			for (int i = 0; i < positions.size(); i++) {
				c = positions.get(i);
				if (!premier) {
					long time = temps.get(i) - temps.get(i - 1);
					vitesse.add(new Coordonnees((c.x - l.x) / time, (c.y - l.y)
							/ time));
				} else
					premier = false;
				l = c;
			}
			vitesse.addLast(new Coordonnees(0, 0));

			// On supprime le pave cree pour la saisie d'une nouvelle signature
			pave.destroy();

			// Initialisation du champ donnees, caracteristique de la signature
			this.donnees = new DonneesPoint[temps.size()];
			for (int i = 0; i < this.donnees.length; i++) {
				int j = donnees.length - i - 1;
				this.donnees[i] = new DonneesPoint(positions.get(j).x,
						positions.get(j).y, temps.get(j), vitesse.get(j).x,
						vitesse.get(j).y,0);
			}

			// Translation du barycentre en 0.5,0.5
			// this.recalibrerDonnees();
			this.calculs();
		}
		

		if (a == Acquisition.GLULOGIC) {
			Robot r = null;

			PaveGLULOGIC ct = new PaveGLULOGIC();
			ct.run();
					
			try { r = new Robot();} catch (Exception e) {}

			doigtPose = false;
			while (ct.getY() == 1 && !terminate) {
				r.mouseMove(150, 150);
				attendre(1);
			}
			doigtPose = true;
			while (ct.getY() == 0 && !terminate) {
				r.mouseMove(150, 150);
				attendre(1);
			}
			doigtPose = false;
						
			if (!terminate) {
				this.donnees = ct.getSignature();
				this.calculs();
			}
			else
				this.donnees = null;
			
		}
	}


	
	//Détermine la complexité de la signature
	public int complexite(){
		return (int) (2 * MinutiesVectorielles.nombreMinuties(this) + this.donnees[N-1].t/500);
	}
	
	// Remet les donnees dans l'ordre du tracé
	public void inverserDonnees() {
		int l = donnees.length;
		DonneesPoint t;
		for (int i = 0; i < (int) (l / 2); i++) {
			t = donnees[l - 1 - i];
			donnees[l - 1 - i] = donnees[i];
			donnees[i] = t;
		}
	}

	// Fonction d'attente pour eviter boucle infinie
	public void attendre(long t) {

		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
		}
	}


	// Reparsing de la signature en N points
	public void calculs() {
		// Declaration des variables
		double M = 0, distanceEqui = 0;
		DonneesPoint[] temp = new DonneesPoint[N];

		// Calcul distance totale parcourue
		for (int i = 0; i < this.donnees.length - 1; i++) {
			M += this.donnees[i].distance(this.donnees[i + 1]);
		}
		distanceEqui = M / ((double) (N - 1));

		// Initialisation variable boucle
		temp[0] = this.donnees[0];
		int marqueur = 1;
		double rapport = 0, distanceActuelle = 0;
		DonneesPoint pointMarque = this.donnees[0];

		// Resampling de la signature
		int i = 0;
		while (i < this.donnees.length - 1) {

			if (distanceActuelle + pointMarque.distance(this.donnees[i + 1]) < distanceEqui) {
				distanceActuelle += pointMarque.distance(this.donnees[i + 1]);
				pointMarque = this.donnees[i + 1];
				i++;
			} else {
				rapport = (distanceEqui - distanceActuelle)
						/ pointMarque.distance(this.donnees[i + 1]);
				pointMarque = new DonneesPoint(	(1 - rapport) * pointMarque.x + rapport * (this.donnees[i + 1].x), 
												(1 - rapport) * pointMarque.y + rapport * (this.donnees[i + 1].y),
												(1 - rapport) * pointMarque.t + rapport * (this.donnees[i + 1].t),
												(1 - rapport) * pointMarque.vx + rapport * (this.donnees[i + 1].vx), 
												(1 - rapport) * pointMarque.vy + rapport * (this.donnees[i + 1].vy),
												(1 - rapport) * pointMarque.s + rapport * (this.donnees[i + 1].s));
				temp[marqueur] = pointMarque;
				marqueur++;
				distanceActuelle = 0;
			}

		}
		temp[N - 1] = this.donnees[this.donnees.length - 1];

		// On modifie le s.donnees
		this.donnees = new DonneesPoint[N];
		for (int j = 0; j < N; j++)
			this.donnees[j] = new DonneesPoint(temp[j].x, temp[j].y, temp[j].t,
					temp[j].vx, temp[j].vy, temp[j].s);
	}
	
	public static String getOsName() {
		  String os = "";
		  if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
		    os = "windows";
		  } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
		    os = "linux";
		  } else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
		    os = "mac";
		  }
		 
		  return os;
	}

	
}


enum Acquisition {
	TUIO, GLULOGIC, WINDOWS
}


