package gui;

import gestionIO.Enregistrement;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objets.Coordonnees;
import objets.DonneesPoint;
import objets.Gabarit;
import objets.Signature;
import affichageEtTests.Image;

import comparaison.Analyse;
import comparaison.Comparaison;

/* Classe Main
 * ---------------
 * Role : Interface graphique du programme
 * --------------
 */

public class Main extends JFrame {
	
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField textFieldLogin;
	private JComboBox<String> comboBoxSRef;
	private JComboBox<String> comboBoxSTest;
	private JComboBox<String> comboBoxAuthentification;
	private JPanel imageSRef;
	private JPanel imageSTest;
	private JLayeredPane panneauEnregistrer;
	private JLayeredPane panneauComparer;
	private JLayeredPane panneauAuthentification;
	private JPanel imageEnregistrer;
	private JSeparator separateurEnregistrer;
	private JToggleButton boutonSimilitudes;
	private JLabel labelPositionsDynamique;
	private JLabel labelVitessesDynamique;
	private JLabel[][] imageProgression;
	private int[] imageProgressionCurrent;
	private JLabel complexite;
	private JLabel imageFort;
	private JLabel imageMoyen;
	private JLabel imageFaible;
	private JLabel imageGodlike;
	private JLabel imageAttente;
	private JLabel imageSucces;
	private JLabel imageEchec;
	private JLabel imageDoigt1;
	private JLabel imageDoigtOK1;
	private JLabel imageDoigt2;
	private JLabel imageDoigtOK2;
	private String currentSRef = "";
	private String currentSTest = "";
	private boolean currentSimilitudes = true;
	Signature s = new Signature(false);
	Gabarit g;
	Thread saisieSignature, affichageDynamiqueSignature, saisieGabarit,
			affichageDynamiqueGabarit;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Declare le lib path
					// Comme ça, on peut lancer le programme avec un petit double clic
					// Merci à http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
					System.setProperty( "java.library.path", "lib/");
					Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
					fieldSysPath.setAccessible( true );
					fieldSysPath.set( null, null );
					
					Main frame = new Main();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE));
		contentPane.setLayout(gl_contentPane);

		// ////////////////////
		// Panneau Enregistrer
		// ////////////////////
		panneauEnregistrer = new JLayeredPane();
		panneauEnregistrer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "escape");
		panneauEnregistrer.getActionMap().put("escape", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				if (g != null)
					g.s.terminate = true;
			}
		});
		tabbedPane.addTab("Enregistrer", null, panneauEnregistrer, null);

		imageEnregistrer = new JPanel();
		imageEnregistrer.setBounds(41, 40, 337, 284);
		panneauEnregistrer.add(imageEnregistrer);
		imageEnregistrer.setOpaque(false);

		JLabel labelLogin = new JLabel("Login");
		labelLogin.setBounds(6, 6, 41, 16);
		panneauEnregistrer.add(labelLogin);

		textFieldLogin = new JTextField();
		textFieldLogin.setBounds(59, 0, 225, 28);
		panneauEnregistrer.add(textFieldLogin);
		textFieldLogin.setColumns(10);

		JButton boutonEnregistrer = new JButton("Enregistrer");
		boutonEnregistrer.setBounds(296, 1, 117, 29);
		panneauEnregistrer.add(boutonEnregistrer);

		separateurEnregistrer = new JSeparator();
		separateurEnregistrer.setBounds(6, 34, 407, 12);
		panneauEnregistrer.add(separateurEnregistrer);
		
		complexite = new JLabel();
		
		imageFort = new JLabel();
		try {
			imageFort = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/fort.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		panneauEnregistrer.add(imageFort);
		imageFort.setBounds(179, 336, 300, 40);
		imageFort.setVisible(false);
		
		
		imageFaible = new JLabel();
		try {
			imageFaible = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/faible.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		panneauEnregistrer.add(imageFaible);
		imageFaible.setBounds(179, 336, 300, 40);
		imageFaible.setVisible(false);
		
		imageMoyen = new JLabel();
		try {
			imageMoyen = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/moyen.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		panneauEnregistrer.add(imageMoyen);
		imageMoyen.setBounds(179, 336, 300, 40);
		imageMoyen.setVisible(false);
		
		imageGodlike = new JLabel();
		try {
			imageGodlike = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/godlike.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		panneauEnregistrer.add(imageGodlike);
		imageGodlike.setBounds(179, 336, 300, 40);
		imageGodlike.setVisible(false);
		
		try {
			imageAttente = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/attente.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageAttente.setBounds(0, 50, 400, 290);
		panneauEnregistrer.add(imageAttente);
		imageAttente.setVisible(false);
		
		try {
			imageDoigt1 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigt.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageDoigt1.setBounds(0, 50, 400, 290);
		panneauEnregistrer.add(imageDoigt1);
		imageDoigt1.setVisible(false);

		try {
			imageDoigtOK1 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigtOK.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		
		imageProgressionCurrent = new int[10];
		imageProgression = new JLabel[10][4];
		for(int i=0;i<10;i++){
			imageProgressionCurrent[i]=-1;
			try {
				imageProgression[i][0] = new JLabel(new ImageIcon(ImageIO.read(new File(
						"images/blanc.png"))));
			} catch (Exception ex) {
				System.err.println("Image non chargée");
				return;
			}
			imageProgression[i][0].setBounds(-2+38*i, 334, 82, 47);
			panneauEnregistrer.add(imageProgression[i][0]);
			
			try {
				imageProgression[i][1] = new JLabel(new ImageIcon(ImageIO.read(new File(
						"images/vert.png"))));
			} catch (Exception ex) {
				System.err.println("Image non chargée");
				return;
			}
			imageProgression[i][1].setBounds(-2+38*i, 334, 82, 47);
			panneauEnregistrer.add(imageProgression[i][1]);
			
			try {
				imageProgression[i][2] = new JLabel(new ImageIcon(ImageIO.read(new File(
						"images/orange.png"))));
			} catch (Exception ex) {
				System.err.println("Image non chargée");
				return;
			}
			imageProgression[i][2].setBounds(-2+38*i, 334, 82, 47);
			panneauEnregistrer.add(imageProgression[i][2]);
			
			try {
				imageProgression[i][3] = new JLabel(new ImageIcon(ImageIO.read(new File(
						"images/rouge.png"))));
			} catch (Exception ex) {
				System.err.println("Image non chargée");
				return;
			}
			imageProgression[i][3].setBounds(-2+38*i, 334, 82, 47);
			panneauEnregistrer.add(imageProgression[i][3]);
			
			imageProgression[i][0].setVisible(false);
			imageProgression[i][1].setVisible(false);
			imageProgression[i][2].setVisible(false);
			imageProgression[i][3].setVisible(false);
		}

		imageDoigtOK1.setBounds(0, 50, 400, 290);
		panneauEnregistrer.add(imageDoigtOK1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 325, 407, 12);
		panneauEnregistrer.add(separator);
		imageDoigtOK1.setVisible(false);

		// ////////////////////
		// Authentification
		// ////////////////////
		panneauAuthentification = new JLayeredPane();
		panneauAuthentification.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "escape");
		panneauAuthentification.getActionMap().put("escape", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				if (s != null)
					s.terminate = true;
			}
		});
		tabbedPane.addTab("Authentification", null, panneauAuthentification,null);

		JLabel labelLogin2 = new JLabel("Login");
		labelLogin2.setBounds(6, 6, 59, 16);
		panneauAuthentification.add(labelLogin2);

		comboBoxAuthentification = new JComboBox<String>();
		comboBoxAuthentification.setBounds(57, 2, 207, 27);
		panneauAuthentification.add(comboBoxAuthentification);

		JButton boutonAuthentification = new JButton("OK");
		boutonAuthentification.setBounds(266, 1, 50, 29);
		panneauAuthentification.add(boutonAuthentification);

		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.setBounds(318, 1, 100, 29);
		panneauAuthentification.add(boutonSupprimer);

		try {
			imageDoigt2 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigt.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageDoigt2.setBounds(0, 50, 400, 370);
		panneauAuthentification.add(imageDoigt2);
		imageDoigt2.setVisible(false);

		try {
			imageDoigtOK2 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigtOK.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageDoigtOK2.setBounds(0, 50, 400, 370);
		panneauAuthentification.add(imageDoigtOK2);
		imageDoigtOK2.setVisible(false);
		
		try {
			imageSucces = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/succes.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageSucces.setBounds(35, 40, 400, 370);
		panneauAuthentification.add(imageSucces);
		imageSucces.setVisible(false);
		
		try {
			imageEchec = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/echec.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageEchec.setBounds(35, 40, 400, 370);
		panneauAuthentification.add(imageEchec);
		imageEchec.setVisible(false);
		


		// //////////////////
		// ActionOnEvent
		// //////////////////

		boutonAuthentification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saisieSignature = new Thread() {
					public void run() {
						imageEchec.setVisible(false);
						imageSucces.setVisible(false);

						s = new Signature(false);

						// Curseur transparent
						BufferedImage cursorImg = new BufferedImage(16, 16,
								BufferedImage.TYPE_INT_ARGB);
						Cursor blankCursor = Toolkit.getDefaultToolkit()
								.createCustomCursor(cursorImg, new Point(0, 0),
										"blank cursor");
						getContentPane().setCursor(blankCursor);

						s.init();
						// Retablis le curseur
						getContentPane().setCursor(Cursor.getDefaultCursor());
						
						if (s.terminate)
							return;



						// Authentification réussie ??
						if (Comparaison.comparer(Enregistrement
								.ouvrir((String) comboBoxAuthentification
										.getSelectedItem()), s))
							imageSucces.setVisible(true);
						else
							imageEchec.setVisible(true);

					}
				};

				affichageDynamiqueSignature = new Thread() {
					public void run() {
						imageDoigt2.setVisible(true);

						while (saisieSignature.isAlive()) {
							if (!imageDoigtOK2.isVisible() && s != null
									&& s.doigtPose) {
								imageDoigt2.setVisible(false);
								imageDoigtOK2.setVisible(true);
							}
							if (!imageDoigt2.isVisible() && s != null
									&& !s.doigtPose) {
								imageDoigt2.setVisible(true);
								imageDoigtOK2.setVisible(false);
							}
						}
						imageDoigt2.setVisible(false);
						imageDoigtOK2.setVisible(false);
					}
				};
				try {Thread.sleep(100);}
				catch (Exception e2){};
				saisieSignature.start();
				affichageDynamiqueSignature.start();

			}
		});

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String myPath = "gabarits/";
				try {
					comboBoxAuthentification.removeAllItems();
					File folder = new File(myPath);
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().endsWith(".txt")) {

							comboBoxAuthentification.addItem(""
									+ listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
						}
					}
				} catch (Exception ee) {
				}

			}
		});

		boutonEnregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					imageAttente = new JLabel(new ImageIcon(ImageIO.read(new File(
							"images/attente.png"))));
				} catch (Exception ex) {
					System.err.println("Image non chargée");
					return;
				}
				imageAttente.setBounds(0, 50, 400, 290);
				panneauEnregistrer.add(imageAttente);
				
				imageAttente.setVisible(false);
				imageEnregistrer.removeAll();
				if (complexite.isVisible())
					complexite.setVisible(false);
				if(imageFort.isVisible())
					imageFort.setVisible(false);
				if(imageFaible.isVisible())
					imageFaible.setVisible(false);
				if(imageMoyen.isVisible())
					imageMoyen.setVisible(false);
				if(imageGodlike.isVisible())
					imageGodlike.setVisible(false);
				
				String login = textFieldLogin.getText();
				String myPath = "gabarits/";

				// Si login nul, invalide, ou deja existant, on ne fait rien
				if (login == null || !login.matches("[a-z0-9_-]{1,}"))
					return;
				
				//Creation du dossier si nécessaire
				File Folder = new File(myPath);
				if (!Folder.exists())
					Folder.mkdir();
				
				File[] tab = new File(myPath).listFiles();
				for (int j = 0; j < tab.length; j++) {
					if (login.equals(tab[j].getName().subSequence(0, tab[j].getName().length() - 4)))
							return;
				}
				
				
				saisieGabarit = new Thread() {
					public void run() {
						// Cree un curseur tranparent
						BufferedImage cursorImg = new BufferedImage(16, 16,
								BufferedImage.TYPE_INT_ARGB);
						Cursor blankCursor = Toolkit.getDefaultToolkit()
								.createCustomCursor(cursorImg, new Point(0, 0),
										"blank cursor");
						getContentPane().setCursor(blankCursor);

						g = new Gabarit(false);
						g.init();

						// Retablis le curseur
						getContentPane().setCursor(Cursor.getDefaultCursor());

						if (g.s.terminate){
							for(int i=0;i<10;i++){
								imageProgressionCurrent[i]=-1;
								imageProgression[i][1].setVisible(false);
								imageProgression[i][2].setVisible(false);
								imageProgression[i][3].setVisible(false);
								imageProgression[i][0].setVisible(false);
							}
							return;
						}
							
						
						Enregistrement.enregistrer(textFieldLogin.getText(),
								g.sRef);

						panneauEnregistrer.remove(imageEnregistrer);
						panneauEnregistrer.remove(imageAttente);
						imageEnregistrer = new JPanel();
						imageEnregistrer.setBounds(41, 40, 337, 337);
						ImageComponent img = new ImageComponent(
								conversion_taille_284(g.sRef, 0xff000000));
						imageEnregistrer.add(img);
						panneauEnregistrer.add(imageEnregistrer);
						imageEnregistrer.setOpaque(false);
						
						
						for(int i=0;i<10;i++){
							imageProgression[i][1].setVisible(false);
							imageProgression[i][2].setVisible(false);
							imageProgression[i][3].setVisible(false);
							imageProgression[i][0].setVisible(false);
						}
						
						String str = " Complexité : ";
						int score = g.sRef.complexite();
						if (score<3)
							{str+= "Faible";imageFaible.setVisible(true);}							
						else if (score<5)
							{str+="Moyen";imageMoyen.setVisible(true);}	
						else if (score<7)
							{str+="Fort";imageFort.setVisible(true);}	
						else {
							{str+="God Like";imageGodlike.setVisible(true);}	
							try {
								AudioClip clip = Applet.newAudioClip(
								new File("sons/godlike.wav").toURI().toURL());
								clip.play();
							} catch (MalformedURLException murle) {
								System.out.println(murle);
							}

						}
						str+= " (Score " + Math.min(score, 10) + "/10)";
						
						
						complexite = new JLabel(str);
						complexite.setBounds(20, 348, 300, 16);
						panneauEnregistrer.add(complexite);
						complexite.setVisible(true);

					}
				};
				affichageDynamiqueGabarit = new Thread() {
					public void run() {
						imageDoigt1.setVisible(true);

						
						while (saisieGabarit.isAlive()) {
							if (g!=null) {
								for (int j =0; j < g.etat.length; j++) {
									if(g.etat[j]!=imageProgressionCurrent[j]){
									imageProgressionCurrent[j]=g.etat[j];
									imageProgression[j][0].setVisible(false);
									imageProgression[j][1].setVisible(false);
									imageProgression[j][2].setVisible(false);
									imageProgression[j][3].setVisible(false);
									switch(g.etat[j]) {
									case -1: // Rouge
										imageProgression[j][3].setVisible(true);
									break;
									case 0: //Gris
										imageProgression[j][0].setVisible(true);
									break;
									case 1: // Orange
										imageProgression[j][2].setVisible(true);
									break;
									case 2:  // Vert
										imageProgression[j][1].setVisible(true);
									break;
									}
									}
								}
							}
							
							boolean bool = false;
							if(g!=null){
							for(int i=0; i<10; i++){
								if(g.etat[i]==0||g.etat[i]==-1){
									bool = true;
									if(g.etat[i]==-1){
									for(int j=1;j<10;j++){
										if(g.etat[j]==1){
											bool=false;
										}
									}
									}
								}								
							}
							}
							

							
							if(!bool&&g!=null){
								imageDoigt1.setVisible(false);
								imageDoigtOK1.setVisible(false);
								imageAttente.setVisible(true);
							}
							else {
								if (!imageDoigtOK1.isVisible() && g != null && g.s.doigtPose) {
									imageDoigt1.setVisible(false);
									imageDoigtOK1.setVisible(true);
									imageAttente.setVisible(false);
								}
								if (!imageDoigt1.isVisible() && g != null && !g.s.doigtPose) {
									imageDoigt1.setVisible(true);
									imageDoigtOK1.setVisible(false);
									imageAttente.setVisible(false);
								}
							}
						}
						imageDoigt1.setVisible(false);
						imageDoigtOK1.setVisible(false);
					}
				};
				try {Thread.sleep(100);}
				catch (Exception ex1) {};
				saisieGabarit.start();
				affichageDynamiqueGabarit.start();
			}

		});


		boutonSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String myPath = "gabarits/";
				try {
					File folder = new File(myPath
							+ (String) comboBoxAuthentification
									.getSelectedItem() + ".txt");
					folder.delete();

					comboBoxAuthentification.removeAllItems();
					folder = new File(myPath);
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().endsWith(".txt")) {
							comboBoxAuthentification.addItem(""
									+ listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
						}
					}
				} catch (Exception ee) {
				}

			}
		});

	}

	void mettreAJour() {

		if (currentSRef == (String) comboBoxSRef.getSelectedItem()
				&& currentSTest == (String) comboBoxSTest.getSelectedItem()
				&& currentSimilitudes == boutonSimilitudes.isSelected()) {
			return;
		}
		currentSRef = (String) comboBoxSRef.getSelectedItem();
		currentSTest = (String) comboBoxSTest.getSelectedItem();
		currentSimilitudes = boutonSimilitudes.isSelected();
		try {
			Signature sRef = Enregistrement.ouvrir((String) comboBoxSRef
					.getSelectedItem());
			Signature sTest = Enregistrement.ouvrir((String) comboBoxSTest
					.getSelectedItem());
			if (boutonSimilitudes.isSelected()) {
				DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
				sRef = new Signature(analyse[0]);
				sTest = new Signature(analyse[1]);

				panneauComparer.remove(imageSRef);
				imageSRef = new JPanel();
				imageSRef.setBounds(109, 103, 200, 200);
				imageSRef.add(new ImageComponent(conversion_taille_200(sRef,
						0xff000000)));
				panneauComparer.add(imageSRef);
				imageSRef.setOpaque(false);

				panneauComparer.remove(imageSTest);
				imageSTest = new JPanel();
				imageSTest.setBounds(109, 103, 200, 200);
				imageSTest.add(new ImageComponent(conversion_taille_200(sTest,
						0xFFFF1111)));
				panneauComparer.add(imageSTest);
				imageSTest.setOpaque(false);

			} 
			else {
				panneauComparer.remove(imageSRef);
				imageSRef = new JPanel();
				imageSRef.setBounds(109, 103, 200, 200);
				imageSRef.add(new ImageComponent(conversion_taille_200(sRef,
						0xff000000)));
				panneauComparer.add(imageSRef);
				imageSRef.setOpaque(false);

				panneauComparer.remove(imageSTest);
				imageSTest = new JPanel();
				imageSTest.setBounds(109, 103, 200, 200);
				imageSTest.add(new ImageComponent(conversion_taille_200(sTest,
						0xFFFF1111)));
				panneauComparer.add(imageSTest);
				imageSTest.setOpaque(false);

			}

			labelPositionsDynamique
					.setText(""
							+ ((double) Math.round(Analyse.scorePositions(
									sTest, sRef) * 1000)) / 1000);
			labelVitessesDynamique
					.setText(""
							+ ((double) Math.round(Analyse.scoreVitesses(sTest,
									sRef) * 1000)) / 1000);

		} catch (Exception e) {
		}

	}

	Image conversion_taille_200(Signature s, int rgb) {

		Image r = new Image(200, 200, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();

		for (int i = 0; i < tab.length - 1; i++) {
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i + 1].x, tab[i + 1].y);

			r.tracerSegment(a.fois(194).plus(1, 1), b.fois(194).plus(1, 1), rgb);
		}

		return r;
	}

	Image conversion_taille_284(Signature s, int rgb) {

		Image r = new Image(284, 284, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();

		for (int i = 0; i < tab.length - 1; i++) {
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i + 1].x, tab[i + 1].y);
			r.tracerSegment(a.fois(280).plus(1, 1), b.fois(280).plus(1, 1), rgb);
		}

		return r;
	}
}

class ImageComponent extends JComponent {

	private static final long serialVersionUID = 1225166596942897848L;
	private BufferedImage img;

	public ImageComponent(BufferedImage img) {
		this.img = img;
		setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}
}
