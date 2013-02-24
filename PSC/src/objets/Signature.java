package objets;

import gestionIO.Pave;
import gestionIO.PaveGLULOGIC;
import gestionIO.PaveScreen;
import gestionIO.PaveTUIO;

import java.awt.Robot;
import java.awt.Toolkit;
import java.util.LinkedList;

import TUIO.TuioClient;
import affichageEtTests.FenetreTempsReel;
//import java.awt.Robot;
//import java.awt.AWTException;

/* Classe Signature
 * ---------------
 * Role : Caracterise une signature, tableau de DonneesPoint apres reparsing
 * --------------
 */

public class Signature {
	public static boolean AffichageTpsReel = true;
	public static final int N = 50;
	// Choisir Acquisition.WINDOWS ou Acquisition.TUIO ou
	// Acquisition.GLULOGIC selon le mode souhaité
	Acquisition a = Acquisition.GLULOGIC;
	public DonneesPoint[] donnees;

	public Signature() {

		if (a == Acquisition.WINDOWS) {

			// Variables utilisees lors de l'enregistrement
			LinkedList<Coordonnees> positions, vitesse;
			LinkedList<Long> temps;
			positions = new LinkedList<Coordonnees>();
			vitesse = new LinkedList<Coordonnees>();
			temps = new LinkedList<Long>();

			// Acquisition de la signature
			Pave pave = new PaveScreen();

			// On attend que l'utilisateur pose son doigt sur le pav������
			while (!pave.pose()) {
				attendre(10);
			}
			Coordonnees p, l = new Coordonnees(0, 0);
			long t0 = System.currentTimeMillis();
			// On enregistre le trace tant que le doigt est sur le pave
			while (pave.pose()) {

				p = pave.position();
				if (!p.equals(l)) {
					positions.add(new Coordonnees(p.x / 1200, p.y / 1200));
					temps.add(System.currentTimeMillis() - t0);
				}

				l = new Coordonnees(p.x, p.y);
				attendre(1);
			}

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
		if (a == Acquisition.TUIO) {

			TuioClient client = new TuioClient(3333);
			PaveTUIO demo = new PaveTUIO();
			client.removeAllTuioListeners();
			client.addTuioListener(demo);
			FenetreTempsReel F = null;
			Robot r = null;

			client.connect();
			// Prepare la fenetre et le robot qui fixe la souris
			if (AffichageTpsReel) {
				F = new FenetreTempsReel(demo);
				try {
					r = new Robot();
				} catch (Exception e) {
				}
			}
			while (demo.getY() == 0) {
				if (AffichageTpsReel)
					r.mouseMove(F.frame.getX() + F.frame.getWidth() / 2,
							F.frame.getY() + F.frame.getHeight() / 2);
				attendre(1);
			}

			client.disconnect();
			if (AffichageTpsReel)
				F.destroyWindow();

			this.donnees = demo.getSignature();
			// this.recalibrerDonnees();
			this.calculs();
		}

		if (a == Acquisition.GLULOGIC) {
			Robot r = null;
			int width = Toolkit.getDefaultToolkit().getScreenSize().width;
			int height = Toolkit.getDefaultToolkit().getScreenSize().height;
			
			
			PaveGLULOGIC ct = new PaveGLULOGIC();
			ct.run();
			
			try { r = new Robot();} catch (Exception e) {}
	
			while (ct.getY() == 0) {
				r.mouseMove(width/2, height/2);
				attendre(1);
			}
			this.donnees = ct.getSignature();
			this.calculs();
		}
	}

	// Construit la signature a partir d'un tableau de DonneesPoints
	public Signature(DonneesPoint[] tab) {
		this.donnees = tab;
	}

	// Remet les donnees dans l'ordre du trac��
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
	void attendre(long t) {

		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
		}
	}

	// Recentre le barycentre en (0,5;0,5)
	/*
	 * void recalibrerDonnees() { System.out.print("ASQQSD"); double xG = 0, yG
	 * = 0;
	 * 
	 * // Calcul des coordonnees du barycentre for (int j = 0; j <
	 * this.donnees.length; j++) { xG += this.donnees[j].x; yG +=
	 * this.donnees[j].y; } xG /= this.donnees.length; yG /=
	 * this.donnees.length;
	 * 
	 * // Translation (changement d'origine; barycentre en (0.5,0.5) ) for (int
	 * j = 0; j < this.donnees.length; j++) { this.donnees[j].x -= (xG - 0.5);
	 * this.donnees[j].y -= (yG - 0.5); } }
	 */

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
}

enum Acquisition {
	TUIO, GLULOGIC, WINDOWS
}
