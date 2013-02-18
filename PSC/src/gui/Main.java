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
import java.awt.image.BufferedImage;
import java.io.File;

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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import objets.Coordonnees;
import objets.Signature;
import affichageEtTests.Image;

import comparaison.Analyse;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;


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
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel;
	private JToggleButton tglbtnNewToggleButton;
	private JSeparator separator_3;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	
	private String currentSRef = "";
	private String currentSTest = "";
	private boolean currentSimilitudes = false;
	

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
		
		panel_3 = new JPanel();
		panel_3.setBounds(41, 40, 337, 337);
		layeredPane.add(panel_3);
		panel_3.setOpaque(false);
		
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
				
				if (login!=null && login.matches("[a-z0-9_-]{1,}")) {
					Signature s = new Signature();
					Enregistrement.enregistrer(login,s);
					layeredPane.remove(panel_3);
				 	panel_3 = new JPanel();
				 	panel_3.setBounds(41, 40, 337, 337);
				 	ImageComponent img = new ImageComponent(conversion_taille_337(s, 0xff000000));
				 	panel_3.add(img);
				 	layeredPane.add(panel_3);
				 	panel_3.setOpaque(false);
				}
			}
		});
		btnNewButton.setBounds(296, 1, 117, 29);
		layeredPane.add(btnNewButton);
		
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
					 panel.setBounds(41, 40, 337, 337);
					 panel.add(new ImageComponent(conversion_taille_337(s, 0xff000000)));
					 panel.setOpaque(false);
					 layeredPane_2.add(panel);
					 
			     }
				
			}
		});

		comboBox_2.setBounds(77, 2, 207, 27);
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
		
		JButton boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					String myPath = "gabarits/";
					try{
						File folder = new File(myPath+(String) comboBox_2.getSelectedItem()+".txt");
						folder.delete();
						comboBox.removeAllItems();
						comboBox_1.removeAllItems();
						comboBox_2.removeAllItems();
						folder = new File(myPath);
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
		boutonSupprimer.setBounds(296, 1, 117, 29);
		layeredPane_2.add(boutonSupprimer);
		
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
		panel_1.setLayout(null);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(-88, 43, 282, 16);
		panel_1.add(lblNewLabel);
		
		panel_2 = new JPanel();
		panel_2.setBounds(109, 103, 200, 200);
		 
		layeredPane_1.add(panel_2);
		contentPane.setLayout(gl_contentPane);
		panel_2.setOpaque(false);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(6, 89, 407, 12);
		layeredPane_1.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(6, 304, 407, 12);
		layeredPane_1.add(separator_2);
		
		JLabel label = new JLabel("Scores");
		label.setBounds(6, 315, 115, 16);
		layeredPane_1.add(label);
		
		label_1 = new JLabel("Positions");
		label_1.setBounds(16, 338, 77, 16);
		layeredPane_1.add(label_1);
		
		label_2 = new JLabel("Vitesses");
		label_2.setBounds(111, 338, 77, 16);
		layeredPane_1.add(label_2);
		
		label_3 = new JLabel("Pression");
		label_3.setBounds(200, 338, 77, 16);
		layeredPane_1.add(label_3);
		
		label_4 = new JLabel("Minuties");
		label_4.setBounds(289, 338, 77, 16);
		layeredPane_1.add(label_4);
		
		label_5 = new JLabel("");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(16, 358, 60, 16);
		layeredPane_1.add(label_5);
		String Newligne=System.getProperty("line.separator");
	}
	
	void mettreAJour(){
		
		if (currentSRef==(String) comboBox.getSelectedItem() && currentSTest==(String) comboBox_1.getSelectedItem() && currentSimilitudes==tglbtnNewToggleButton.isSelected()){return;}
		System.out.println(1);
		currentSRef=(String) comboBox.getSelectedItem();
		currentSTest=(String) comboBox_1.getSelectedItem();
		currentSimilitudes=tglbtnNewToggleButton.isSelected();
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

		label_5.setText(""+((double)Math.round(Analyse.scorePositions(sTest, sRef)*1000))/1000);
		//label.setText("θ="+Math.round(theta*180/Math.PI)+" λ="+((float) Math.round(1000*lambda))/1000);

		} catch(Exception e){}
		
	}
	Image conversion_taille_200(Signature s, int rgb){
		
		Image r = new Image(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
			
			r.tracerSegment(a.fois(194).plus(1, 1), b.fois(194).plus(1, 1), rgb);
		}
		
		return r;
	}
	Image conversion_taille_337(Signature s, int rgb){
		
		Image r = new Image(337, 337, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0; i<s.donnees.length-1; i++){
			Coordonnees a = new Coordonnees(s.donnees[i].x, s.donnees[i].y);
			Coordonnees b = new Coordonnees(s.donnees[i+1].x, s.donnees[i+1].y);
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




