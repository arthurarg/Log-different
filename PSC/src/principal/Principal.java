package principal;

import gestionIO.Enregistrement;

import java.util.Scanner;

import objets.Signature;

import comparaison.Analyse;

public class Principal {

	public static void main(String[] args) {
		boolean success = false, arreterBoucle = false;
		
		while (!success &&  !arreterBoucle) {
			//Menu de base
			int choix = 0;
			System.out.println("Que voulez-vous faire ?");
			System.out.println("1. S'authentifier");
			System.out.println("2. Enregister une nouvelle signature");
			System.out.println("3. Quitter");
			Scanner sc = new Scanner(System.in);
			while(choix!= 1 && choix!=2 && choix !=3) {
				try {
					choix = Integer.parseInt(sc.nextLine());
				}
				catch (Exception e) {
					choix = 0;
					System.out.println("Veuillez rentrer un chiffre");
				}
			}
			
			//On traite les diff�rents cas
			switch (choix) {
				// Tentative d'authentification
				case 1 :
					Signature sRef = Enregistrement.recupererSignature();
					
					for (int i=1; i<=3;i++) {
						System.out.println("Tentative n�" + i);
						Signature sTest = new Signature();
						if (comparer(sTest,sRef)) {
							success = true;
							break;
						}
					}
					if (!success)
						System.out.println("Casse toi pauvre con !");
					break;
					
					
				// Enregistrer un nouveau gabarit
				case 2 :
					definirGabarit();
					break;
				
				// Quitter le programme
				case 3 :
					arreterBoucle = true;
					break;
			}
			
			
		}
		if (arreterBoucle)
			System.out.println("Programme interrompu volontairement");
		if (success)
			System.out.println("Authentification r�ussie");

	}
	
	
	
	
	
	public static boolean comparer(Signature sTest, Signature sRef) {
		// Traitement et d�placement de sTest
		// TODOOO Analyse.optimiserSignature(sTest, sRef);
		
		// Effectue la comparaison
		if (Analyse.compareVitessesMoyennes(sTest,sRef) || Analyse.comparePositions(sTest,sRef) || Analyse.compareVitesses(sTest,sRef))
				return true;
		
		else
			return false;	
	}
	
	public static void definirGabarit() {
		// A voir; package � cr�er par radargenson ?
		
		//M�thode extr�mement na�ve (� gerber)
		Scanner sc = new Scanner(System.in);
		System.out.println("Quel sera votre login :");
		
		String login = sc.nextLine();
		Signature s = new Signature();
		Enregistrement.enregistrer(login,s);
		System.out.println("Nouvelle signature enregistr�e !");
		
	}
}
