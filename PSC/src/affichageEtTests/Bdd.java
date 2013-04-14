package affichageEtTests;

import gestionIO.Enregistrement;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import minuties.Minuties;
import objets.Coordonnees;
import objets.DonneesPoint;
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
	static boolean continuer = false;
	
	static Gabarit g;
	final static Thread saisieGabarit = new Thread() {
		public void run() {
			g = new Gabarit(false);
			g.init();
		}
	};
	
	public static void main(String[] args) {
		
		/* Appeler constructBDD() pour faire passer les tests de saisie à une personne et l'ajouter à la base de donnees
		 *  Appeler analyseBDD() pour analyser toutes les donnees stockees sur l'ordi
		 */
		constructBDD();
		//recalculerGabarits();
		//analyseBDD();
	}
	
	
	
	public static void constructBDD() {
		
		
		// Cree le dossier bdd
		File bddFolder = new File("bdd");
		if (!bddFolder.exists())
			bddFolder.mkdir();
		
		
		
		//Elements graphiques
		JFrame frame = new JFrame();
		frame.setBounds(50, 50, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JLayeredPane display = new JLayeredPane();
		display.setBounds(0, 0, 500, 500);
		display.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "entrer");
		display.getActionMap().put("entrer", new AbstractAction() {
			public void actionPerformed (ActionEvent e) {
				continuer = true;
			}
		});

		
		JTextField textFieldLogin = new JTextField();
		textFieldLogin.setBounds(0, 25, 225, 28);
		textFieldLogin.setColumns(10);
		
		JLabel labelLogin = new JLabel("Login : ");
		labelLogin.setBounds(6, 6, 59, 16);
		JTextArea labelSignal = new JTextArea("Appuyer sur entrer pour continuer");
		labelSignal.setBounds(0, 400, 500, 100);
		labelSignal.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
		labelSignal.setBackground(Color.lightGray);
		
		
		JButton boutonDepart = new JButton("Let's start");
		boutonDepart.setBounds(6, 60, 117, 29);
		boutonDepart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				continuer = true;
			}
		});
		
		JLabel imageDoigt;
		try {imageDoigt = new JLabel(new ImageIcon(ImageIO.read(new File("images/doigt.png")))); }
		catch (Exception ex) {System.err.println("Image non chargée"); return;}
		imageDoigt.setBounds(0,0,500,400);

		

		JLabel imageDoigtOK;
		try {imageDoigtOK = new JLabel(new ImageIcon(ImageIO.read(new File("images/doigtOK.png")))); }
		catch (Exception ex) {System.err.println("Image non chargée"); return;}
		imageDoigtOK.setBounds(0,0,500,400);

		
		display.add(imageDoigt);
		imageDoigt.setVisible(false);
		display.add(imageDoigtOK);
		imageDoigtOK.setVisible(false);
		display.add(textFieldLogin);
		display.add(labelLogin);
		display.add(labelSignal);
		labelSignal.setVisible(false);
		display.add(boutonDepart);
		
		frame.add(display);
		frame.setVisible(true);
		
		
		// On attend qu'un login soit entre
		continuer = false;
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		boutonDepart.setVisible(false);
		textFieldLogin.setVisible(false);
		labelLogin.setVisible(false);
		labelSignal.setVisible(true);
		
		//Récupération du login
		String login = textFieldLogin.getText().toLowerCase();
		
		
		//Cree le dossier bdd/login/
		File f = new File("bdd/" + login);
	
		if (!f.exists())
			f.mkdir();
		else {
			labelSignal.setText("Le dossier bdd/" + login + 
					 " existe deja. Veuillez le supprimer avant de refaire passer le test à cette personne");
			return;
		}
		
		//Creation de l'arborescence entiere
		File fsignatures = new File("bdd/" + login + "/signatures");
		fsignatures.mkdir();
		File fgabarit = new File("bdd/" + login + "/gabarit");
		fgabarit.mkdir();
		File ffar = new File("bdd/" + login + "/far");
		ffar.mkdir();
		File fshoulder = new File("bdd/" + login + "/far/shoulder");
		fshoulder.mkdir();
		File fimage = new File("bdd/" + login + "/far/image");
		fimage.mkdir();
	
		
		
		//Enregistrement du gabarit
		continuer = false;
		labelSignal.setText("(1/4) Enregistrement du gabarit..." + NEW_LINE + "Appuyer sur Entree");
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		
		//Enregistrement des 10 signatures utilisée pour le gabarit ds le disque dur
		saisieGabarit.start();
		imageDoigt.setVisible(true);
		while (saisieGabarit.isAlive()) {
			if (!imageDoigtOK.isVisible() && g!= null && g.s.doigtPose) {
				imageDoigt.setVisible(false);
				imageDoigtOK.setVisible(true);
			}
			if (!imageDoigt.isVisible() && g!= null && !g.s.doigtPose) {
				imageDoigtOK.setVisible(false);
				imageDoigt.setVisible(true);
			}
		}
		imageDoigtOK.setVisible(false);
		imageDoigt.setVisible(false);
		
		for (int k=0; k<g.tab.length; k++)
			Enregistrement.enregistrer("" + k, g.tab[k], fgabarit.getAbsolutePath());
		Enregistrement.enregistrer("gabarit", g.sRef, f.getAbsolutePath());
		
		
		//Enregistrement des 100 signatures
		continuer = false;
		labelSignal.setText("(2/4) Enregistrement de 100 signatures..." + NEW_LINE + "N'hésitez pas à faire des pauses" + NEW_LINE + "Appuyer sur Entree");
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		
		for (int i = 0; i < 100; i++) {
			if ((i % 25) == 0 && i!= 0) {
				continuer = false;
				labelSignal.setText("Faites une pause ... " + i + " signatures enregistrées" + NEW_LINE +"Appuyer sur Entree" );
				while (!continuer) {
					try {Thread.currentThread().sleep(1000);}
					catch(Exception e) {};
				}
			}
			labelSignal.setText("" + i + "/100 signatures enregistrées");
			Enregistrement.enregistrer("" + i, new Signature(), fsignatures.getAbsolutePath());
		}
		
		//Enregistrement des 25 tentatives "shoulder"
		continuer = false;
		labelSignal.setText("(3/4) Tentative d'instrusion shoulder..." + NEW_LINE + "Opération realisee par l'operateur" + NEW_LINE + "Appuyer sur Entree");
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		
		for (int i = 0; i < 25; i++) {
			labelSignal.setText("" + i + "/25 signatures enregistrées");
			Enregistrement.enregistrer("" + i, new Signature(), fshoulder.getAbsolutePath());			
		}
		
		//Enregistrement des 25 tentatives "image"
		continuer = false;
		labelSignal.setText("(4/4) Tentative d'instrusion image..." + NEW_LINE + "Opération realisee par l'operateur" + NEW_LINE + "Appuyer sur Entree");
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		
		Signature s = Enregistrement.ouvrir("gabarit",f.getAbsolutePath());
		JPanel imageEnregistrer = new JPanel();
		imageEnregistrer.setBounds(41, 40, 337, 337);
		ImageComponent img = new ImageComponent(conversion_taille_200(s, 0xff000000));
		imageEnregistrer.add(img);
		display.add(imageEnregistrer);
		imageEnregistrer.setOpaque(false);
		
		
		for (int i = 0; i < 25; i++) {
			Enregistrement.enregistrer("" + i, new Signature(), fimage.getAbsolutePath());	
			labelSignal.setText("" + i + "/25 signatures enregistrées");
		}
		
		continuer = false;
		labelSignal.setText("Test termine ! Merci pour votre participation !" + NEW_LINE + "Appuyer sur Entree");
		while (!continuer) {
			try {Thread.currentThread().sleep(1000);}
			catch(Exception e) {};
		}
		frame.dispose();
		System.exit(0);
	}

	
	public static void recalculerGabarits() {
		//Liste les fichiers présents dans bdd/
		String[] names = new File("bdd/").list();
		String login;
		
		for (int k = 0; k < names.length; k++) {
			login = names[k];	
			if (new File("bdd/" + login + "/gabarit").exists()) {
				Signature[] tab = new Signature[Gabarit.nbSign];
				for (int j=0; j<tab.length; j++)
					tab[j] = Enregistrement.ouvrir(""+j,"bdd/" + login + "/gabarit");
				Enregistrement.enregistrer("gabarit",new Gabarit(tab).sRef, "bdd/" + login);
				
			}
		}
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
			Signature s = new Signature(sRef.getDonnees());
			Signature sTest = Enregistrement.ouvrir("" + i, "bdd/" + login + "/" + where);
			
			DonneesPoint[][] analyse = Analyse.similitudes(s, sTest);
			Signature s1 = new Signature(analyse[0]);
			Signature s2 = new Signature(analyse[1]);
			
			scorePos += "" + Analyse.scorePositions(s1, s2) + NEW_LINE;
			scoreVit += "" + Analyse.scoreVitesses(s1, s2) + NEW_LINE;
			scorePre += "" + Analyse.scorePressions(s1, s2) + NEW_LINE;
			scoreMin0 += "" + Minuties.comparaison(s1, s2, 0) + NEW_LINE;
			scoreMin1 += "" + Minuties.comparaison(s1, s2, 1) + NEW_LINE;
			scoreMin2 += "" + Minuties.comparaison(s1, s2, 2) + NEW_LINE;
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
					Signature s = new Signature(sRef.getDonnees());
					Signature sTest = Enregistrement.ouvrir("" + i, "bdd/" + login + "/" + where);
					
					DonneesPoint[][] analyse = Analyse.similitudes(s, sTest);
					Signature s1 = new Signature(analyse[0]);
					Signature s2 = new Signature(analyse[1]);
					
					scorePos += "" + Analyse.scorePositions(s1, s2) + NEW_LINE;
					scoreVit += "" + Analyse.scoreVitesses(s1, s2) + NEW_LINE;
					scorePre += "" + Analyse.scorePressions(s1, s2) + NEW_LINE;
					scoreMin0 += "" + Minuties.comparaison(s1, s2, 0) + NEW_LINE;
					scoreMin1 += "" + Minuties.comparaison(s1, s2, 1) + NEW_LINE;
					scoreMin2 += "" + Minuties.comparaison(s1, s2, 2) + NEW_LINE;
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
	
	
	static Image conversion_taille_200(Signature s, int rgb) {

		Image r = new Image(200, 200, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();
		
		for (int i = 0; i < tab.length - 1; i++) {
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i + 1].x,
					tab[i + 1].y);

			r.tracerSegment(a.fois(194).plus(1, 1), b.fois(194).plus(1, 1), rgb);
		}

		return r;
	}
}
