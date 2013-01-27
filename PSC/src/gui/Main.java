package gui;

import gestionIO.Enregistrement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import javax.swing.DefaultComboBoxModel;

import objets.Coordonnees;
import objets.Signature;


import java.nio.file.AccessMode;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


import affichageEtTests.Fenetre;
import affichageEtTests.Image;


import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.Box;

import comparaison.Analyse;
import java.awt.Color;


/* Classe Main
 * ---------------
 * Role : Implemente l'interface graphique du programme
 * --------------
 */


public class Main extends JFrame {
	

	private JPanel contentPane;
	private JTextField textField;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLayeredPane layeredPane;
	private JLayeredPane layeredPane_2;
	private JLayeredPane layeredPane_1;
	private JPanel panel_3;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel;
	private JToggleButton tglbtnNewToggleButton;
	private JLabel lblScorePositions;
	private JLabel label;
	private JLabel lblDecoupages;
	private JSeparator separator_3;
	

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String myPath = "gabarits/";
				try{
					comboBox.removeAllItems();
					comboBox_1.removeAllItems();
					comboBox_2.removeAllItems();
					File folder = new File(myPath);
				    File[] listOfFiles = folder.listFiles();
				    for (int i = 0; i < listOfFiles.length; i++) {
				    	if (listOfFiles[i].getName().endsWith(".txt")){
				    		comboBox.addItem(listOfFiles[i].getName().subSequence(0, listOfFiles[i].getName().length()-4));
				    		comboBox_1.addItem(listOfFiles[i].getName().subSequence(0, listOfFiles[i].getName().length()-4));
				    		comboBox_2.addItem(listOfFiles[i].getName().subSequence(0, listOfFiles[i].getName().length()-4));
				    	}
				    }
				} catch (Exception ee){}
			   
			  
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
		);
		
		layeredPane = new JLayeredPane();
		tabbedPane.addTab("Enregistrer", null, layeredPane, null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(6, 6, 41, 16);
		layeredPane.add(lblLogin);
		
		textField = new JTextField();
		textField.setBounds(59, 0, 225, 28);
		layeredPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enregistrer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String login = textField.getText();
				Signature s = new Signature();
				Enregistrement.enregistrer(login,s);
				 layeredPane.remove(panel_3);
				 panel_3 = new JPanel();
				 panel_3.setBounds(74, 34, 271, 271);
				 ImageComponent img = new ImageComponent(conversion_taille_337(s, 0xff000000));
				 panel_3.add(img);
				 layeredPane.add(panel_3);
				 panel_3.setOpaque(false);
				
			}
		});
		btnNewButton.setBounds(296, 1, 117, 29);
		layeredPane.add(btnNewButton);
		
		panel_3 = new JPanel();
		panel_3.setBounds(41, 40, 337, 337);
		layeredPane.add(panel_3);
		panel_3.setOpaque(false);
		
		separator = new JSeparator();
		separator.setBounds(6, 34, 407, 12);
		layeredPane.add(separator);
		

		layeredPane_2 = new JLayeredPane();
		tabbedPane.addTab("Afficher", null, layeredPane_2, null);

		
		
		JLabel lblSignature = new JLabel("Signature");
		lblSignature.setBounds(6, 6, 59, 16);
		layeredPane_2.add(lblSignature);
		

		comboBox_2 = new JComboBox();
		comboBox_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				 if (e.getStateChange() == ItemEvent.SELECTED ) {
					 Signature s = Enregistrement.ouvrir((String) comboBox_2.getSelectedItem());

					 
					 layeredPane_2.remove(panel);
					 panel = new JPanel();
					 panel.setBounds(74, 34, 271, 271);
					 panel.add(new ImageComponent(conversion_taille_337(s, 0xff000000)));
					 panel.setOpaque(false);
					 layeredPane_2.add(panel);
					 
			     }
				
			}
		});

		comboBox_2.setBounds(77, 2, 336, 27);
		layeredPane_2.add(comboBox_2);
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(41, 40, 337, 337);
		layeredPane_2.add(panel);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 413, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 271, Short.MAX_VALUE)
		);
		panel.setLayout(gl_panel);
		
		separator_3 = new JSeparator();
		separator_3.setBounds(6, 34, 407, 12);
		layeredPane_2.add(separator_3);
		
		layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Comparer", null, layeredPane_1, null);
		
		JLabel lblSignatureRference = new JLabel("Signature r\u00E9f\u00E9rence");
		
		lblSignatureRference.setBounds(6, 6, 121, 16);
		layeredPane_1.add(lblSignatureRference);
		
		JLabel lblSignatureTest = new JLabel("Signature test");
		lblSignatureTest.setBounds(6, 34, 87, 16);
		lblSignatureTest.setForeground(Color.RED);
		layeredPane_1.add(lblSignatureTest);
		
		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				 if (e.getStateChange() == ItemEvent.SELECTED ) {

					 mettreAJour();
					 
			     }
				
			}
		});
		comboBox.setBounds(139, 2, 274, 27);
		layeredPane_1.add(comboBox);
		
		tglbtnNewToggleButton = new JToggleButton("Appliquer similitudes");
		tglbtnNewToggleButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mettreAJour();
			}
		});



		tglbtnNewToggleButton.setBounds(96, 62, 226, 29);
		layeredPane_1.add(tglbtnNewToggleButton);
		
		comboBox_1 = new JComboBox();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				 if (e.getStateChange() == ItemEvent.SELECTED ) {

					 mettreAJour();
					 
			     }
				
			}
		});
		comboBox_1.setBounds(139, 30, 274, 27);
		layeredPane_1.add(comboBox_1);
		
		panel_1 = new JPanel();
		panel_1.setBounds(109, 103, 200, 200);
		layeredPane_1.add(panel_1);
		panel_1.setOpaque(false);
		
		panel_2 = new JPanel();
		panel_2.setBounds(109, 103, 200, 200);
		 
		layeredPane_1.add(panel_2);
		contentPane.setLayout(gl_contentPane);
		panel_2.setOpaque(false);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(6, 89, 407, 12);
		layeredPane_1.add(separator_1);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(6, 307, 407, 19);
		layeredPane_1.add(separator_2);
		
		lblNewLabel = new JLabel("Score Position :");
		lblNewLabel.setBounds(131, 315, 282, 16);
		layeredPane_1.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Theta, lambda");
		lblNewLabel_1.setBounds(131, 338, 282, 16);
		layeredPane_1.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("Points coup\u00E9s : ");
		lblNewLabel_2.setBounds(131, 361, 282, 16);
		layeredPane_1.add(lblNewLabel_2);
		
		lblScorePositions = new JLabel("Score Positions");
		lblScorePositions.setBounds(6, 315, 115, 16);
		layeredPane_1.add(lblScorePositions);
		
		label = new JLabel("��   -   ��");
		label.setBounds(6, 338, 61, 16);
		layeredPane_1.add(label);
		
		lblDecoupages = new JLabel("Decoupages");
		lblDecoupages.setBounds(6, 361, 87, 16);
		layeredPane_1.add(lblDecoupages);
	}
	
	void mettreAJour(){
		try{
		Signature sRef = Enregistrement.ouvrir((String) comboBox.getSelectedItem());
		Signature sTest = Enregistrement.ouvrir((String) comboBox_1.getSelectedItem());
		int n1avant,n2avant,n1apres,n2apres;
		double lambda, theta;
		if(tglbtnNewToggleButton.isSelected()){
			double[] infos = Analyse.similitudes(sRef,sTest);			
			n1avant = Math.max((int) Math.floor(infos[2]*sRef.donnees.length), 0);
			n1apres = Math.max((int) Math.floor(infos[3]*sRef.donnees.length), 0);
			n2avant = Math.max((int) Math.floor(-infos[2]*sTest.donnees.length), 0);
			n2apres = Math.max((int) Math.floor(-infos[3]*sTest.donnees.length), 0);
			theta = infos[0];
			lambda = infos[1];
			layeredPane_1.remove(panel_1);
			 panel_1 = new JPanel();
			 panel_1.setBounds(109, 103, 200, 200);
			 panel_1.add(new ImageComponent(conversion_taille_200(sRef, 0xff000000)));
			 layeredPane_1.add(panel_1);
			 panel_1.setOpaque(false);

			 layeredPane_1.remove(panel_2);
			 panel_2 = new JPanel();
			 panel_2.setBounds(109, 103, 200, 200);
			 panel_2.add(new ImageComponent(conversion_taille_200(sTest,0xFFFF1111)));
			 layeredPane_1.add(panel_2);
			 panel_2.setOpaque(false);

		}
		else{
			n1avant = 0;
			n1apres = 0;
			n2avant = 0;
			n2apres = 0;
			lambda = 1;
			theta = 0;
			 
			 layeredPane_1.remove(panel_1);
			 panel_1 = new JPanel();
			 panel_1.setBounds(109, 103, 200, 200);
			 panel_1.add(new ImageComponent(conversion_taille_200(sRef, 0xff000000)));
			 layeredPane_1.add(panel_1);
			 panel_1.setOpaque(false);

 
			 layeredPane_1.remove(panel_2);
			 panel_2 = new JPanel();
			 panel_2.setBounds(109, 103, 200, 200);
			 panel_2.add(new ImageComponent(conversion_taille_200(sTest,0xFFFF1111)));
			 layeredPane_1.add(panel_2);
			 panel_2.setOpaque(false);
			 sTest.calculs();
			 sRef.calculs();
			 

		}

		lblNewLabel.setText(""+Analyse.scorePositions(sTest, sRef));
		lblNewLabel_1.setText(Math.round(theta*180/Math.PI)+"   -   "+((float) Math.round(1000*lambda))/1000);
		lblNewLabel_2.setText(n1avant+"-sRef-"+n1apres + "   -   " +n2avant+"-sTest-"+n2apres);
		} catch(Exception e){}
		
	}
	Image conversion_taille_200(Signature s, int rgb){
		
		Image r = new Image(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			
			r.tracerSegment(a.fois(200), b.fois(200), rgb);
		}
		
		return r;
	}
	Image conversion_taille_337(Signature s, int rgb){
		
		Image r = new Image(337, 337, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			
			r.tracerSegment(a.fois(337), b.fois(337), rgb);
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




