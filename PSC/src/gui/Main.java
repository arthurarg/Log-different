package gui;

import gestionIO.Enregistrement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objets.Coordonnees;
import objets.DonneesPoint;
import objets.Gabarit;
import objets.Signature;
import affichageEtTests.Image;

import comparaison.Analyse;

/* Classe Main
 * ---------------
 * Role : Implemente l'interface graphique du programme
 * --------------
 */

public class Main extends JFrame {
 
	private JPanel contentPane;
	private JTextField textFieldLogin;
	private JComboBox comboBoxSRef;
	private JComboBox comboBoxSTest;
	private JComboBox comboBoxAfficher;
	private JPanel imageAfficher;
	private JPanel imageSRef;
	private JPanel imageSTest;
	private JLayeredPane panneauEnregistrer;
	private JLayeredPane panneauAfficher;
	private JLayeredPane panneauComparer;
	private JPanel imageEnregistrer;
	private JSeparator separateurEnregistrer;
	private JSeparator separateurComparerHaut;
	private JToggleButton boutonSimilitudes;
	private JSeparator separateurAfficher;
	private JLabel labelPositions;
	private JLabel labelVitesses;
	private JLabel labelPression;
	private JLabel labelMinuties;
	private JLabel labelPositionsDynamique;
	private JLabel labelVitessesDynamique;
	private String currentSRef = "";
	private String currentSTest = "";
	private boolean currentSimilitudes = true;
	Signature s = new Signature (false);
	Gabarit g;
	
	final Thread saisieSignature = new Thread() {
		public void run() {
			s = new Signature(false);
			s.init();
			if (s.terminate)
				return;
			
			Enregistrement.enregistrer(textFieldLogin.getText(), s);
			panneauEnregistrer.remove(imageEnregistrer);
			imageEnregistrer = new JPanel();
			imageEnregistrer.setBounds(41, 40, 337, 337);
			ImageComponent img = new ImageComponent(
					conversion_taille_337(s, 0xff000000));
			imageEnregistrer.add(img);
			panneauEnregistrer.add(imageEnregistrer);
			imageEnregistrer.setOpaque(false);
		}
	};
	
	final Thread saisieGabarit = new Thread() {
		public void run() {
			g = new Gabarit(false);
			g.init();
			if (g.s.terminate)
				return;
			
			Enregistrement.enregistrer(textFieldLogin.getText(), g.sRef);
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
		panneauEnregistrer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "escape");
		panneauEnregistrer.getActionMap().put("escape", new AbstractAction() {
			public void actionPerformed (ActionEvent e) {
				if (g!=null)
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

		// ////////////////////
		// Panneau Afficher
		// ////////////////////

		panneauAfficher = new JLayeredPane();
		tabbedPane.addTab("Afficher", null, panneauAfficher, null);

		JLabel labelSignature = new JLabel("Signature");
		labelSignature.setBounds(6, 6, 59, 16);
		panneauAfficher.add(labelSignature);

		comboBoxAfficher = new JComboBox();
		comboBoxAfficher.setBounds(77, 2, 207, 27);
		panneauAfficher.add(comboBoxAfficher);

		imageAfficher = new JPanel();
		imageAfficher.setOpaque(false);
		imageAfficher.setBounds(41, 40, 337, 337);
		panneauAfficher.add(imageAfficher);

		separateurAfficher = new JSeparator();
		separateurAfficher.setBounds(6, 34, 407, 12);
		panneauAfficher.add(separateurAfficher);

		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.setBounds(296, 1, 117, 29);
		panneauAfficher.add(boutonSupprimer);

		// ////////////////////
		// Panneau Comparer
		// ////////////////////

		panneauComparer = new JLayeredPane();
		tabbedPane.addTab("Comparer", null, panneauComparer, null);

		JLabel labelSRef = new JLabel("Signature r\u00E9f\u00E9rence");
		labelSRef.setBounds(6, 6, 121, 16);
		panneauComparer.add(labelSRef);

		JLabel labelSTest = new JLabel("Signature test");
		labelSTest.setBounds(6, 34, 87, 16);
		labelSTest.setForeground(Color.RED);
		panneauComparer.add(labelSTest);

		comboBoxSTest = new JComboBox();
		comboBoxSTest.setBounds(139, 30, 274, 27);
		panneauComparer.add(comboBoxSTest);

		comboBoxSRef = new JComboBox();
		comboBoxSRef.setBounds(139, 2, 274, 27);
		panneauComparer.add(comboBoxSRef);

		boutonSimilitudes = new JToggleButton("Appliquer similitudes");
		boutonSimilitudes.setBounds(96, 62, 226, 29);
		panneauComparer.add(boutonSimilitudes);
		boutonSimilitudes.setSelected(true);

		imageSRef = new JPanel();
		imageSRef.setBounds(109, 103, 200, 200);
		panneauComparer.add(imageSRef);
		imageSRef.setOpaque(false);
		imageSRef.setLayout(null);

		imageSTest = new JPanel();
		imageSTest.setBounds(109, 103, 200, 200);
		panneauComparer.add(imageSTest);
		imageSTest.setOpaque(false);

		separateurComparerHaut = new JSeparator();
		separateurComparerHaut.setBounds(6, 89, 407, 12);
		panneauComparer.add(separateurComparerHaut);

		JSeparator separateurComparerBas = new JSeparator();
		separateurComparerBas.setBounds(6, 304, 407, 12);
		panneauComparer.add(separateurComparerBas);

		JLabel labelScores = new JLabel("Scores");
		labelScores.setBounds(6, 315, 115, 16);
		panneauComparer.add(labelScores);

		labelPositions = new JLabel("Positions");
		labelPositions.setBounds(16, 338, 77, 16);
		panneauComparer.add(labelPositions);

		labelVitesses = new JLabel("Vitesses");
		labelVitesses.setBounds(111, 338, 77, 16);
		panneauComparer.add(labelVitesses);

		labelPression = new JLabel("Pression");
		labelPression.setBounds(200, 338, 77, 16);
		panneauComparer.add(labelPression);

		labelMinuties = new JLabel("Minuties");
		labelMinuties.setBounds(289, 338, 77, 16);
		panneauComparer.add(labelMinuties);

		labelPositionsDynamique = new JLabel("");
		labelPositionsDynamique.setHorizontalAlignment(SwingConstants.CENTER);
		labelPositionsDynamique.setBounds(16, 358, 60, 16);
		panneauComparer.add(labelPositionsDynamique);
		
		labelVitessesDynamique = new JLabel("");
		labelVitessesDynamique.setHorizontalAlignment(SwingConstants.CENTER);
		labelVitessesDynamique.setBounds(109, 358, 60, 16);
		panneauComparer.add(labelVitessesDynamique);

		// //////////////////
		// ActionOnEvent
		// //////////////////

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String myPath = "gabarits/";
				try {
					comboBoxSRef.removeAllItems();
					comboBoxSTest.removeAllItems();
					comboBoxAfficher.removeAllItems();
					File folder = new File(myPath);
					File[] listOfFiles = folder.listFiles();
					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().endsWith(".txt")) {
							comboBoxSRef
									.addItem(listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
							comboBoxSTest
									.addItem(listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
							comboBoxAfficher
									.addItem(listOfFiles[i].getName()
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

				if (login != null && login.matches("[a-z0-9_-]{1,}")) {
					saisieGabarit.start();
				}
			}
		});

		comboBoxAfficher.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					Signature s = Enregistrement
							.ouvrir((String) comboBoxAfficher.getSelectedItem());

					panneauAfficher.remove(imageAfficher);
					imageAfficher = new JPanel();
					imageAfficher.setBounds(41, 40, 337, 337);
					imageAfficher.add(new ImageComponent(conversion_taille_337(
							s, 0xff000000)));
					imageAfficher.setOpaque(false);
					panneauAfficher.add(imageAfficher);

				}

			}
		});

		boutonSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String myPath = "gabarits/";
				try {
					File folder = new File(myPath
							+ (String) comboBoxAfficher.getSelectedItem()
							+ ".txt");
					folder.delete();
					comboBoxSRef.removeAllItems();
					comboBoxSTest.removeAllItems();
					comboBoxAfficher.removeAllItems();
					folder = new File(myPath);
					File[] listOfFiles = folder.listFiles();

					for (int i = 0; i < listOfFiles.length; i++) {
						if (listOfFiles[i].getName().endsWith(".txt")) {
							comboBoxSRef
									.addItem(listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
							comboBoxSTest
									.addItem(listOfFiles[i].getName()
											.subSequence(
													0,
													listOfFiles[i].getName()
															.length() - 4));
							comboBoxAfficher
									.addItem(listOfFiles[i].getName()
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

		comboBoxSRef.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					mettreAJour();

				}

			}
		});

		boutonSimilitudes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mettreAJour();
			}
		});

		comboBoxSTest.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					mettreAJour();

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
			int n1avant, n2avant, n1apres, n2apres;
			double lambda, theta;
			if (boutonSimilitudes.isSelected()) {
				DonneesPoint[][] analyse = Analyse.similitudes(sRef, sTest);
				sRef = new Signature(analyse[0]);
				sTest = new Signature(analyse[1]);
				/*n1avant = Math.max(
						(int) Math.floor(infos[2] * sRef.size()), 0);
				n1apres = Math.max(
						(int) Math.floor(infos[3] * sRef.size()), 0);
				n2avant = Math.max(
						(int) Math.floor(-infos[2] * sRef.size()), 0);
				n2apres = Math.max(
						(int) Math.floor(-infos[3] * sRef.size()), 0);
				theta = infos[0];
				lambda = infos[1];*/
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
				n1avant = 0;
				n1apres = 0;
				n2avant = 0;
				n2apres = 0;
				lambda = 1;
				theta = 0;

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
					+ ((double) Math.round(Analyse.scoreVitesses(
							sTest, sRef) * 1000)) / 1000);


		} catch (Exception e) {
		}

	}

	Image conversion_taille_200(Signature s, int rgb) {

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

	Image conversion_taille_337(Signature s, int rgb) {

		Image r = new Image(337, 337, BufferedImage.TYPE_INT_ARGB);
		DonneesPoint[] tab = s.getDonnees();
		
		for (int i = 0; i < tab.length - 1; i++) {
			Coordonnees a = new Coordonnees(tab[i].x, tab[i].y);
			Coordonnees b = new Coordonnees(tab[i + 1].x,
					tab[i + 1].y);
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
