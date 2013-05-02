package gestionIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import objets.DonneesPoint;
import objets.Signature;


/* Classe Enregistrement
 * ---------------
 * Role : Gestion du stockage des donnees sur le disque dur (ecriture et lecture)
 * --------------
 */


public class Enregistrement {
	public static final String NEW_LINE = System.getProperty("line.separator" );
	
	
	//Enregistre une signature sur le disque dur
	public static void enregistrer (String login, Signature s) {
		enregistrer(login, s, "gabarits");
	}
	//Recupère une signature sur le disque dur, dans le dossier gabarits
	public static Signature ouvrir(String login) {
		return ouvrir(login,"gabarits");
	}
	
	// Méthode plus générale ou l'on choisit le chemin relatif
	public static void enregistrer (String name, Signature s, String path) {
		try {
			//Ouverture du fichier .txt
			FileWriter fichier = new FileWriter(path + "/" + name + ".txt");
			
			//Enregistrement s.donnees (taille puis contenu)
			DonneesPoint[] temp = s.getDonnees();
			fichier.write("" + temp.length);
			for(int j=0; j<temp.length;j++) {
				fichier.write("" + NEW_LINE + temp[j].x + " " + 
												temp[j].y + " " +
												temp[j].t + " " +
												temp[j].vx + " " +
												temp[j].vy + " " +
			                       				temp[j].s);
			}
			fichier.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'a pu etre ouvert, " +
					" ou un probleme d'ecriture a ete rencontre"); }

		
	}
	
	//Méthode plus générale pour récuperer une signature deja enregistree sur le disque dur
	public static Signature ouvrir (String name, String path) {
		//Declaration du tableau et variable temporaire de lecture
		DonneesPoint[] tab;
		String[] nombres;
		
		//Ouverture du fichier .txt et initialisation tableau donnees
		try {
			FileReader file = new FileReader(path + "/" + name + ".txt");
			BufferedReader in = new BufferedReader(file);
			
			
			tab = new DonneesPoint[Integer.parseInt(in.readLine())];
			for (int j=0; j<tab.length;j++) {
				tab[j] = new DonneesPoint(0,0,0,0,0,0);
			}
			
			for (int j=0; j<tab.length;j++) {
				nombres = in.readLine().split(" ");
				if (nombres.length != 6) {
					System.out.println("Erreur de lecture du fichier .txt :");
					for (int w=0; w<nombres.length;w++)
						System.out.println(nombres[w]);
				}
				tab[j] = new DonneesPoint(Double.parseDouble(nombres[0]),
						                  Double.parseDouble(nombres[1]),
						                  Double.parseDouble(nombres[2]),
						                  Double.parseDouble(nombres[3]),
						                  Double.parseDouble(nombres[4]),
						                  Double.parseDouble(nombres[5]));
			}
			file.close();
			in.close();
		}
		catch (IOException e) {
			System.out.println("Le fichier n'existe pas"); 
			return null;
		}
		
		//Creation de la signature a partir du tableau
		return new Signature(tab);
	}
}
