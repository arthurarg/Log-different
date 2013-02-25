package gestionIO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JComponent;
import objets.DonneesPoint;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioPoint;
import TUIO.TuioTime;

/* Classe PaveTUIO
 * ---------------
 * Role : - Recuperation des donnees lors de la saisie d'une signature
 *        - Impl��mentation de la JFrame de l'affichage en temps r��el
 * --------------
 */

public class PaveTUIO extends JComponent implements TuioListener {
	public DonneesPoint[] donnees;
	public LinkedList<DonneesPoint> ListeDoi = new LinkedList<DonneesPoint>();
	public long t0;
	public int y = 0;


	private Hashtable<Long, TuioCursor> cursorList = new Hashtable<Long, TuioCursor>();

	public static final int finger_size = 15;
	static final int table_size = 760;

	public static int width, height;
	private float scale = 1.0f;

	// Renvoie la valeur de y
	public int getY() {
		return this.y;
	}

	// Renvoie le tableau de DonneesPoint releve lors de la saisie
	public DonneesPoint[] getSignature() {
		return this.donnees;
	}

	// Recupere les donnees intiales de la saisie
	public void addTuioCursor(TuioCursor tcur) {
		cursorList.put(tcur.getSessionID(), tcur);
		repaint();
		t0 = tcur.getStartTime().getTotalMilliseconds();
		DonneesPoint debut = new DonneesPoint((double) tcur.getX(),
				(double) tcur.getY(), (double) tcur.getTuioTime()
						.getTotalMilliseconds() - t0,
				(double) tcur.getXSpeed(), (double) tcur.getYSpeed(),0);
		ListeDoi.addLast(debut);

	}

	// Recupere les donnees lors de la saisie
	public void updateTuioCursor(TuioCursor tcur) {
		DonneesPoint milieu = new DonneesPoint((double) tcur.getX(),
				tcur.getY(), (double) tcur.getTuioTime().getTotalMilliseconds()
						- t0, (double) tcur.getXSpeed(),
				(double) tcur.getYSpeed(),0);
		ListeDoi.addLast(milieu);
	}

	// Stocke toutes les donnees enregistrees lors de la saisie dans un tableau
	public void removeTuioCursor(TuioCursor tcur) {
			DonneesPoint fin = new DonneesPoint((double) tcur.getX(),
					(double) tcur.getY(), (double) tcur.getTuioTime()
							.getTotalMilliseconds() - t0,
					(double) tcur.getXSpeed(), (double) tcur.getYSpeed(),0);
			ListeDoi.addLast(fin);
			y = 1;

			int Nbrepoints = ListeDoi.size();
			System.out.println("Nbre de points " + Nbrepoints + ListeDoi);
			this.donnees = new DonneesPoint[Nbrepoints];
			for (int i = 0; i < this.donnees.length; i++) {
				this.donnees[i] = new DonneesPoint(ListeDoi.get(i).x,
						ListeDoi.get(i).y, ListeDoi.get(i).t,
						ListeDoi.get(i).vx, ListeDoi.get(i).vy,0);
			}
		
	}


	public void refresh(TuioTime frameTime) {
		repaint();
		// System.out.println("refresh "+frameTime.getTotalMilliseconds());
	}

	public void setSize(int w, int h) {
		super.setSize(w, h);
		width = w;
		height = h;
		scale = height / (float) PaveTUIO.table_size;
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);

		int w = (int) Math.round(width - scale * finger_size / 2.0f);
		int h = (int) Math.round(height - scale * finger_size / 2.0f);

		Enumeration<TuioCursor> cursors = cursorList.elements();
		while (cursors.hasMoreElements()) {
			TuioCursor tcur = cursors.nextElement();
			if (tcur == null)
				continue;
			Vector<TuioPoint> path = tcur.getPath();
			TuioPoint current_point = path.elementAt(0);
			if (current_point != null) {
				// draw the cursor path
				g2.setPaint(Color.blue);
				for (int i = 0; i < path.size(); i++) {
					TuioPoint next_point = path.elementAt(i);
					g2.drawLine(current_point.getScreenX(w),
							current_point.getScreenY(h),
							next_point.getScreenX(w), next_point.getScreenY(h));
					current_point = next_point;
				}
			}

			// draw the finger tip
			g2.setPaint(Color.lightGray);
			int s = (int) (scale * finger_size);
			g2.fillOval(current_point.getScreenX(w - s / 2),
					current_point.getScreenY(h - s / 2), s, s);
			g2.setPaint(Color.black);
			g2.drawString(tcur.getCursorID() + "", current_point.getScreenX(w),
					current_point.getScreenY(h));
		}

	}

}
