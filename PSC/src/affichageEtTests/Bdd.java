package affichageEtTests;

import gestionIO.Enregistrement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import minuties.Minuties;

import objets.Gabarit;
import objets.Signature;

import comparaison.Analyse;


/* Classe Bdd
 * ---------------
 * Role : Gere le stockage des donnees et leur analyse
 * --------------
 */


public class Bdd {
	public static final String NEW_LINE = System.getProperty("line.separator" );

	public static void main(String[] args) {
		/* Appeler constructBDD() pour faire passer les tests de saisie à une personne et l'ajouter à la base de donnees
		 *  Appeler analyseBDD() pour analyser toutes les donnees stockees sur l'ordi
		 */
		//constructBDD();
		analyseBDD();
	}
	
	public static void constructBDD() {
		
		// Cree le dossier bdd
		File bddFolder = new File("bdd");
		if (!bddFolder.exists())
			bddFolder.mkdir();
		
		
		//Demande des renseignements pour le login
		System.out.println("Veuillez rentrer votre identifiant (prenom.nom) : ");
		Scanner sc = new Scanner(System.in);
		String login = sc.next().toLowerCase();
		
		
		//Cree le dossier bdd/login/
		File f = new File("bdd/" + login);
	
		if (!f.exists())
			f.mkdir();
		else {
			System.out.print("Le dossier bdd/" + login + 
					 " existe deja. Veuillez le supprimer avant de refaire passer le test à cette personne");
			return;
		}
		
		//Creation de l'arborescence entiere
		File fsignatures = new File("bdd/" + login + "/signatures");
		fsignatures.mkdir();
		File ffar = new File("bdd/" + login + "/far");
		ffar.mkdir();
		File fshoulder = new File("bdd/" + login + "/far/shoulder");
		fshoulder.mkdir();
		File fimage = new File("bdd/" + login + "/far/image");
		fimage.mkdir();
		
		
		//Enregistrement du gabarit
		System.out.println("(1/4) Enregistrement du gabarit...");
		System.out.println("Veuillez rentrer yes");
		sc.next();
		
		Enregistrement.enregistrer("gabarit", new Gabarit().sRef, f.getAbsolutePath());
		
		
		//Enregistrement des 100 signatures
		System.out.println("(2/4) Enregistrement de 100 signatures...");
		System.out.println("N'hésitez pas à faire des pauses");
		System.out.println("Veuillez taper yes");
		sc.next();
		
		for (int i = 0; i < 100; i++) {
			if ((i % 25) == 0 && i!= 0) {
				System.out.println("Faites une pause ... " + i + " signatures enregistrées");
				System.out.println("Veuillez taper yes");
				sc.next();
			}
			Enregistrement.enregistrer("" + i, new Signature(), fsignatures.getAbsolutePath());
			System.out.println(i);
		}
		
		//Enregistrement des 25 tentatives "shoulder"
		System.out.println("(3/4) Tentative d'instrusion shoulder...");
		System.out.println("Opération realisee par l'operateur");
		System.out.println("Veuillez taper yes");
		sc.next();
		
		for (int i = 0; i < 25; i++) {
			Enregistrement.enregistrer("" + i, new Signature(), fshoulder.getAbsolutePath());
			System.out.println(i);
		}
		
		//Enregistrement des 25 tentatives "image"
		System.out.println("(4/4) Tentative d'instrusion image...");
		System.out.println("Opération realisee par l'operateur");
		System.out.println("Veuillez taper yes");
		sc.next();
		
		Signature s = Enregistrement.ouvrir("gabarit",f.getAbsolutePath());
		Fenetre showmeyourface = new Fenetre();
		showmeyourface.ajouter(s);
		showmeyourface.setAlwaysOnTop(true);
		
		for (int i = 0; i < 25; i++) {
			Enregistrement.enregistrer("" + i, new Signature(), fimage.getAbsolutePath());		
			System.out.println(i);
		}
		
		
		System.out.println("Test termine ! Merci pour votre participation ! ");
		System.out.println("Veuillez taper yes");
		sc.next();
		System.exit(0);
	}

	public static void analyseBDD() {
		// Cree le dossiers results et les sous-dossiers
		File resultsFolder = new File("results");
		if (!resultsFolder.exists())
			resultsFolder.mkdir();
		
		File signFolder = new File("results/signatures");
		if (!signFolder.exists())
			signFolder.mkdir();
		File sign1Folder = new File("results/signatures/scorePositions");
		if (!sign1Folder.exists())
			sign1Folder.mkdir();
		File sign2Folder = new File("results/signatures/scoreVitesses");
		if (!sign2Folder.exists())
			sign2Folder.mkdir();
		File sign3Folder = new File("results/signatures/scorePressions");
		if (!sign3Folder.exists())
			sign3Folder.mkdir();
		File sign4Folder = new File("results/signatures/scoreMinuties0");
		if (!sign4Folder.exists())
			sign4Folder.mkdir();
		File sign5Folder = new File("results/signatures/scoreMinuties1");
		if (!sign5Folder.exists())
			sign5Folder.mkdir();
		File sign6Folder = new File("results/signatures/scoreMinuties2");
		if (!sign6Folder.exists())
			sign6Folder.mkdir();
		
		File shoulderFolder = new File("results/shoulder");
		if (!shoulderFolder.exists())
			shoulderFolder.mkdir();
		File shoulder1Folder = new File("results/shoulder/scorePositions");
		if (!shoulder1Folder.exists())
			shoulder1Folder.mkdir();
		File shoulder2Folder = new File("results/shoulder/scoreVitesses");
		if (!shoulder2Folder.exists())
			shoulder2Folder.mkdir();
		File shoulder3Folder = new File("results/shoulder/scorePressions");
		if (!shoulder3Folder.exists())
			shoulder3Folder.mkdir();
		File shoulder4Folder = new File("results/shoulder/scoreMinuties0");
		if (!shoulder4Folder.exists())
			shoulder4Folder.mkdir();
		File shoulder5Folder = new File("results/shoulder/scoreMinuties1");
		if (!shoulder5Folder.exists())
			shoulder5Folder.mkdir();
		File shoulder6Folder = new File("results/shoulder/scoreMinuties2");
		if (!shoulder6Folder.exists())
			shoulder6Folder.mkdir();
		
		File imageFolder = new File("results/image");
		if (!imageFolder.exists())
			imageFolder.mkdir();
		File image1Folder = new File("results/image/scorePositions");
		if (!image1Folder.exists())
			image1Folder.mkdir();
		File image2Folder = new File("results/image/scoreVitesses");
		if (!image2Folder.exists())
			image2Folder.mkdir();
		File image3Folder = new File("results/image/scorePressions");
		if (!image3Folder.exists())
			image3Folder.mkdir();
		File image4Folder = new File("results/image/scoreMinuties0");
		if (!image4Folder.exists())
			image4Folder.mkdir();
		File image5Folder = new File("results/image/scoreMinuties1");
		if (!image5Folder.exists())
			image5Folder.mkdir();
		File image6Folder = new File("results/image/scoreMinuties2");
		if (!image6Folder.exists())
			image6Folder.mkdir();
		
		
		
		//Liste les fichiers présents dans bdd/
		String[] names = new File("bdd/").list();


		//________________________________________________
		//Enregistre les données par personne
		//________________________________________________
		for (int k = 0; k < names.length; k++) {
			
			String login = names[k];
			System.out.println(login);
			System.out.println("Ce login a deja été traité : " + dejaVu(login));
			
			if (new File("bdd/" + login + "/gabarit.txt").exists() && !dejaVu(login)) {
				//Recupere le gabarit
				Signature sRef = Enregistrement.ouvrir("gabarit", "bdd/" + login);
		
				//Enregistrement des scores obtenus pour les vrais signatures
				analyseParPersonne(sRef,login,"signatures",100,sign1Folder,sign2Folder,sign3Folder,sign4Folder,sign5Folder,sign6Folder);
				//Enregistrement des scores obtenus pour les tentatives shoulder
				analyseParPersonne(sRef,login,"far/shoulder",25,shoulder1Folder,shoulder2Folder,shoulder3Folder,shoulder4Folder,shoulder5Folder,shoulder6Folder);
				//Enregistrement des scores obtenus pour les tentatives image
				analyseParPersonne(sRef,login,"far/image",25,image1Folder,image2Folder,image3Folder,image4Folder,image5Folder,image6Folder);
			}
		}
		
		//________________________________________________
		//Agglomère l'ensemble des données dans 9 fichier .txt
		//________________________________________________
		System.out.println("Agrégation des données en cours");
		analyseGlobale(names,"signature",signFolder,sign1Folder,sign2Folder,sign3Folder,sign4Folder,sign5Folder,sign6Folder);
		analyseGlobale(names,"far/shoulder",shoulderFolder,shoulder1Folder,shoulder2Folder,shoulder3Folder,shoulder4Folder,shoulder5Folder,shoulder6Folder);
		analyseGlobale(names,"far/image",imageFolder,image1Folder,image2Folder,image3Folder,image4Folder,image5Folder,image6Folder);
		System.out.println("Agrégation des données réussie !");

	}
	
	//--------
	//Fonctions utilisées pour analyser les données
	//--------
	
	private static void analyseParPersonne(Signature sRef, String login, String where, int n, 
											File folder1, File folder2, File folder3, File folder4, File folder5, File folder6) {
		
		String scorePos = "", scoreVit = "", scorePre = "",scoreMin0 = "",scoreMin1 = "",scoreMin2 = "";
		for (int i = 0; i<n; i++) {
			Signature s = new Signature(sRef.donnees);
			Signature sTest = Enregistrement.ouvrir("" + i, "bdd/" + login + "/" + where);
			Analyse.similitudes(s, sTest);
			scorePos += "" + Analyse.scorePositions(sTest, s) + NEW_LINE;
			scoreVit += "" + Analyse.scoreVitesses(sTest, s) + NEW_LINE;
			scorePre += "" + Analyse.scorePressions(sTest, s) + NEW_LINE;
			scoreMin0 += "" + Minuties.comparaison(sTest, s, 0) + NEW_LINE;
			scoreMin1 += "" + Minuties.comparaison(sTest, s, 1) + NEW_LINE;
			scoreMin2 += "" + Minuties.comparaison(sTest, s, 2) + NEW_LINE;
		}
		ecrireTexte(login, scorePos, folder1.getAbsolutePath());
		ecrireTexte(login, scoreVit, folder2.getAbsolutePath());
		ecrireTexte(login, scorePre, folder3.getAbsolutePath());
		ecrireTexte(login, scoreMin0, folder4.getAbsolutePath());
		ecrireTexte(login, scoreMin1, folder5.getAbsolutePath());
		ecrireTexte(login, scoreMin2, folder6.getAbsolutePath());
	}
	
	private static void analyseGlobale(String[] names, String where, 
							File folder0, File folder1, File folder2, File folder3, File folder4, File folder5, File folder6){
		String scorePos = "", scoreVit = "", scorePre = "",scoreMin0 = "",scoreMin1 = "",scoreMin2 = "";
		
		for (int k = 0; k < names.length; k++) {
			String login = names[k];
			
			if (dejaVu(login)) {
				scorePos += renvoiTexte(login, folder1.getAbsolutePath());
				scoreVit += renvoiTexte(login, folder2.getAbsolutePath());
				scorePre += renvoiTexte(login, folder3.getAbsolutePath());
				scoreMin0 += renvoiTexte(login, folder4.getAbsolutePath());
				scoreMin1 += renvoiTexte(login, folder5.getAbsolutePath());
				scoreMin2 += renvoiTexte(login, folder6.getAbsolutePath());
			}
			else if (new File("bdd/" + login + "/gabarit.txt").exists()) {
				Signature sRef = Enregistrement.ouvrir("gabarit", "bdd/" + login);
				
				for (int i = 0; i<100; i++) {
					Signature s = new Signature(sRef.donnees);
					Signature sTest = Enregistrement.ouvrir("" + i, "bdd/" + login + "/" + where);
					Analyse.similitudes(s, sTest);
					scorePos += "" + Analyse.scorePositions(sTest, s) + NEW_LINE;
					scoreVit += "" + Analyse.scoreVitesses(sTest, s) + NEW_LINE;
					scorePre += "" + Analyse.scorePressions(sTest, s) + NEW_LINE;
					scoreMin0 += "" + Minuties.comparaison(sTest, s, 0) + NEW_LINE;
					scoreMin1 += "" + Minuties.comparaison(sTest, s, 1) + NEW_LINE;
					scoreMin2 += "" + Minuties.comparaison(sTest, s, 2) + NEW_LINE;
				}	
			}
		}
		ecrireTexte("_scorePositionsTotal.txt", scorePos, folder0.getAbsolutePath());
		ecrireTexte("_scoreVitessesTotal.txt", scoreVit, folder0.getAbsolutePath());
		ecrireTexte("_scorePressionsTotal.txt", scorePre, folder0.getAbsolutePath());
		ecrireTexte("_scoreMinuties0Total.txt", scoreMin0, folder0.getAbsolutePath());
		ecrireTexte("_scoreMinuties1Total.txt", scoreMin1, folder0.getAbsolutePath());
		ecrireTexte("_scoreMinuties2Total.txt", scoreMin2, folder0.getAbsolutePath());
		
	}
	
	
	
	
	
	//--------
	//Fonctions auxiliaires : écrire un fichier .txt, ouvrir un fichier .txt et dire si le login a deja été analyse
	//--------
	
	private static void ecrireTexte(String name, String text, String path) {
		try {
			//Ouverture du fichier .txt
			File fileToWrite = new File(path + "/" + name + ".txt");
			if (fileToWrite.exists())
				fileToWrite.delete();
			fileToWrite.createNewFile();
			
			
			FileWriter fichier = new FileWriter(fileToWrite);
			
			//Enregistrement
			System.out.print("Ecriture du fichier...");
			fichier.write(text);
			fichier.close();

			System.out.println(" Ok");
		}
		catch (IOException e) {
			System.out.println("Le fichier " + path + "/" + name + ".txt" + " n'a pu etre ouvert, " +
					" ou un probleme d'ecriture a ete rencontre"); 
		}
	}
	
	private static String renvoiTexte(String name, String path) {
		String s = "";
		File fileToOpen = new File(path + "/" + name + ".txt");
		if (!fileToOpen.exists()) {
			System.out.println("Ce fichier n'existe pas. Une erreur dans le dossier results existe. Veuillez le suprrimmer et le reconstruire");
			return "";
		}
		
		try {
			FileReader file = new FileReader(fileToOpen);
			BufferedReader in = new BufferedReader(file);
			String tampon = "";
			
			while((tampon = in.readLine()) != null)
				s+= tampon + NEW_LINE;

			in.close();
			return s;
		}
		catch (IOException e) {return null;}
	}
	
	
	private static boolean dejaVu (String login) {
		return (new File("results/signatures/scorePositions/" + login + ".txt").exists() &&
				new File("results/signatures/scoreVitesses/" + login + ".txt").exists() &&
				new File("results/signatures/scorePressions/" + login + ".txt").exists() &&
				new File("results/shoulder/scorePositions/" + login + ".txt").exists() &&
				new File("results/shoulder/scoreVitesses/" + login + ".txt").exists() &&
				new File("results/shoulder/scorePressions/" + login + ".txt").exists() &&
				new File("results/image/scorePositions/" + login + ".txt").exists() &&
				new File("results/image/scoreVitesses/" + login + ".txt").exists() &&
				new File("results/image/scorePressions/" + login + ".txt").exists());
	}
}
