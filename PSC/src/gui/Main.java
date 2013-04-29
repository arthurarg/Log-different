package gui;

import gestionIO.Enregistrement;

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

import javax.imageio.ImageIO;
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
		imageEnregistrer.setBounds(41, 40, 337, 337);
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

		try {
			imageDoigt1 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigt.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageDoigt1.setBounds(0, 50, 400, 370);
		panneauEnregistrer.add(imageDoigt1);
		imageDoigt1.setVisible(false);

		try {
			imageDoigtOK1 = new JLabel(new ImageIcon(ImageIO.read(new File(
					"images/doigtOK.png"))));
		} catch (Exception ex) {
			System.err.println("Image non chargée");
			return;
		}
		imageDoigtOK1.setBounds(0, 50, 400, 370);
		panneauEnregistrer.add(imageDoigtOK1);
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

		// //////////////////
		// ActionOnEvent
		// //////////////////

		boutonAuthentification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				saisieSignature = new Thread() {
					public void run() {
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
							System.out.println("yes");
						else
							System.out.println("nein");

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
				String login = textFieldLogin.getText();
				String myPath = "gabarits/";

				// Si login nul, invalide, ou deja existant, on ne fait rien
				if (login == null || !login.matches("[a-z0-9_-]{1,}"))
					return;

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

						if (g.s.terminate)
							return;

						Enregistrement.enregistrer(textFieldLogin.getText(),
								g.sRef);
						panneauEnregistrer.remove(imageEnregistrer);
						imageEnregistrer = new JPanel();
						imageEnregistrer.setBounds(41, 40, 337, 337);
						ImageComponent img = new ImageComponent(
								conversion_taille_337(g.sRef, 0xff000000));
						imageEnregistrer.add(img);
						panneauEnregistrer.add(imageEnregistrer);
						imageEnregistrer.setOpaque(false);

					}
				};
				affichageDynamiqueGabarit = new Thread() {
					public void run() {
						imageDoigt1.setVisible(true);

						while (saisieGabarit.isAlive()) {
							//TODO on actualise la couleur des jolis petits carrés
							if (g!=null) {
								for (int j =0; j < g.etat.length; j++) {
									switch(g.etat[j]) {
									case -1: // Rouge
										
									break;
									case 0: //Gris
										
									break;
									case 1: // Orange
										
									break;
									case 2:  // Vert
										
									break;
									}
								}
							}
							if (!imageDoigtOK1.isVisible() && g != null && g.s.doigtPose) {
								imageDoigt1.setVisible(false);
								imageDoigtOK1.setVisible(true);
							}
							if (!imageDoigt1.isVisible() && g != null && !g.s.doigtPose) {
								imageDoigt1.setVisible(true);
								imageDoigtOK1.setVisible(false);
							}
						}
						imageDoigt1.setVisible(false);
						imageDoigtOK1.setVisible(false);
					}
				};

				saisieGabarit.start();
				affichageDynamiqueGabarit.start();
			}

		});

		/*
		 * comboBoxAfficher.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) {
		 * 
		 * if (e.getStateChange() == ItemEvent.SELECTED) { Signature s =
		 * Enregistrement .ouvrir((String) comboBoxAfficher.getSelectedItem());
		 * 
		 * panneauAfficher.remove(imageAfficher); imageAfficher = new JPanel();
		 * imageAfficher.setBounds(41, 40, 337, 337); imageAfficher.add(new
		 * ImageComponent(conversion_taille_337( s, 0xff000000)));
		 * imageAfficher.setOpaque(false); panneauAfficher.add(imageAfficher);
		 * 
		 * }
		 * 
		 * } });
		 */

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

		/*
		 * comboBoxSRef.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) {
		 * 
		 * if (e.getStateChange() == ItemEvent.SELECTED) {
		 * 
		 * mettreAJour();
		 * 
		 * }
		 * 
		 * } });
		 * 
		 * boutonSimilitudes.addChangeListener(new ChangeListener() { public
		 * void stateChanged(ChangeEvent e) { mettreAJour(); } });
		 * 
		 * comboBoxSTest.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) {
		 * 
		 * if (e.getStateChange() == ItemEvent.SELECTED) {
		 * 
		 * mettreAJour();
		 * 
		 * }
		 * 
		 * } });
		 */
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
			// int n1avant, n2avant, n1apres, n2apres;
			// double lambda, theta;
			if (boutonSimilitudes.isSelected()) {
				DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
				sRef = new Signature(analyse[0]);
				sTest = new Signature(analyse[1]);
				/*
				 * n1avant = Math.max( (int) Math.floor(infos[2] * sRef.size()),
				 * 0); n1apres = Math.max( (int) Math.floor(infos[3] *
				 * sRef.size()), 0); n2avant = Math.max( (int)
				 * Math.floor(-infos[2] * sRef.size()), 0); n2apres = Math.max(
				 * (int) Math.floor(-infos[3] * sRef.size()), 0); theta =
				 * infos[0]; lambda = infos[1];
				 */
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

			} else {
				/*
				 * n1avant = 0; n1apres = 0; n2avant = 0; n2apres = 0; lambda =
				 * 1; theta = 0;
				 */

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

	Image conversion_taille_337(Signature s, int rgb) {

		Image r = new Image(337, 337, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();

		for (int i = 0; i < tab.length - 1; i++) {
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i + 1].x, tab[i + 1].y);
			r.tracerSegment(a.fois(330).plus(1, 1), b.fois(330).plus(1, 1), rgb);
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
