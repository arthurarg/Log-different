package minuties;

import java.util.LinkedList;
import java.util.List;

import objets.Coordonnees;
import objets.DonneesPoint;
import objets.Signature;
import affichageEtTests.Fenetre;
import affichageEtTests.Image;
import minutiesV1.Sommet;

public class MinutiesVectorielles {
	public static DonneesAnglesSortedLinkedList construireListeAngles(
			Signature s, double dist) {
		DonneesAnglesSortedLinkedList l = new DonneesAnglesSortedLinkedList();
		DonneesPoint[] tab = s.getDonnees();

		for (int i = 0; i < tab.length - 1; i++) {

			int j = i;
			while (tab[i].distance(tab[j]) < dist && j > 0) {
				j--;
			}
			int k = i;
			while (tab[i].distance(tab[k]) < dist && k < tab.length - 2) {
				k++;
			}
			if (tab[i].distance(tab[j]) >= dist
					&& tab[i].distance(tab[k]) >= dist) {
				double ijx = tab[j].x - tab[i].x;
				double ijy = tab[j].y - tab[i].y;
				double normeij = Math.sqrt(Math.pow(ijx, 2) + Math.pow(ijy, 2));
				double ikx = tab[k].x - tab[i].x;
				double iky = tab[k].y - tab[i].y;
				double normeik = Math.sqrt(Math.pow(ikx, 2) + Math.pow(iky, 2));
				double angle = Math.acos((ijx * ikx + ijy * iky)
						/ (normeij * normeik));
				l.add(new DonneesAngles(angle, j, i, k));
			}
		}

		for (int i = 0; i < l.size() - 1; i++) {
			for (int j = i + 1; j < l.size(); j++) {
				if (l.get(i).k > l.get(j).j && l.get(j).k > l.get(i).j) {
					l.remove(j);
					j--;
				}
			}
		}
		return l;
	}

	public static double scoreMinutiesCourbure(Signature sTest, Signature sRef) {
		DonneesAnglesSortedLinkedList lRef = MinutiesVectorielles.construireListeAngles(
				sRef, 0.05);
		DonneesAnglesSortedLinkedList lTest = MinutiesVectorielles.construireListeAngles(
				sTest, 0.05);

		int nMinutiesTest = 0;
		int nMinutiesRef = 0;

		while (lRef.get(nMinutiesRef).angle < Math.PI / 3 && nMinutiesRef < 6) {
			nMinutiesRef++;
		}
		if (nMinutiesRef != 0) {
			double maxRef = lRef.get(nMinutiesRef).angle
					- lRef.get(nMinutiesRef - 1).angle;
			for (int p = nMinutiesRef + 1; p < lRef.size() && p < 7; p++) {
				if (maxRef < lRef.get(p).angle - lRef.get(p - 1).angle) {
					maxRef = lRef.get(p).angle - lRef.get(p - 1).angle;
					nMinutiesRef = p;
				}
			}
		}

		while (lTest.get(nMinutiesTest).angle < Math.PI / 3 && nMinutiesRef < 6) {
			nMinutiesTest++;
		}
		if (nMinutiesTest != 0) {
			double maxTest = lTest.get(nMinutiesTest).angle
					- lTest.get(nMinutiesTest - 1).angle;
			for (int p = nMinutiesTest + 1; p < lTest.size() && p < 7; p++) {
				if (maxTest < lTest.get(p).angle - lTest.get(p - 1).angle) {
					maxTest = lTest.get(p).angle - lTest.get(p - 1).angle;
					nMinutiesTest = p;
				}
			}
		}

		if (nMinutiesTest != nMinutiesRef) {
			System.out.println("nTest = " + nMinutiesTest + "   " + "nRef = "
					+ nMinutiesRef);

			return comparerListeDonneesAngles(sRef, sTest,
					lRef.subList(0, Math.max(nMinutiesRef, nMinutiesTest)),
					lTest.subList(0, Math.max(nMinutiesRef, nMinutiesTest)));
		} else {
			System.out.println("Nb minuties :" + nMinutiesTest);
			return comparerListeDonneesAngles(sRef, sTest,
					lRef.subList(0, nMinutiesRef),
					lTest.subList(0, nMinutiesTest));
		}
	}

	static double comparerListeDonneesAngles(Signature sRef, Signature sTest,
			List<DonneesAngles> lRef, List<DonneesAngles> lTest) {
		if (lRef.size() == 0) {
			return 0;
		}
		List<DonneesAngles> lRefTemp = new LinkedList<DonneesAngles>();
		List<DonneesAngles> lTestTemp = new LinkedList<DonneesAngles>();
		for (int i = 0; i < lRef.size(); i++) {
			lRefTemp.add(lRef.get(i));
			lTestTemp.add(lTest.get(i));
		}
		lRefTemp.remove(0);
		lTestTemp.remove(0);
		double min = comparerListeDonneesAngles(sRef, sTest, lRefTemp,
				lTestTemp)
				+ Math.sqrt(Math.pow(
						sRef.getDonnees()[lRef.get(0).i].x
								- sTest.getDonnees()[lTest.get(0).i].x, 2)
						+ Math.pow(
								sRef.getDonnees()[lRef.get(0).i].y
										- sTest.getDonnees()[lTest.get(0).i].y,
								2)) / Math.sqrt(2);
		for (int i = 1; i < lTest.size(); i++) {
			lRefTemp = new LinkedList<DonneesAngles>();
			lTestTemp = new LinkedList<DonneesAngles>();
			for (int k = 0; k < lRef.size(); k++) {
				lRefTemp.add(lRef.get(k));
				lTestTemp.add(lTest.get(k));
			}

			lRefTemp.remove(0);
			lTestTemp.remove(i);

			if (lRefTemp.size() == lRef.size()) {
				return 57;
			}
			if (min > comparerListeDonneesAngles(sRef, sTest, lRefTemp,
					lTestTemp)
					+ Math.sqrt(Math.pow(sRef.getDonnees()[lRef.get(0).i].x
							- sTest.getDonnees()[lTest.get(i).i].x, 2)
							+ Math.pow(sRef.getDonnees()[lRef.get(0).i].y
									- sTest.getDonnees()[lTest.get(i).i].y, 2))
					/ Math.sqrt(2)) {
				min = comparerListeDonneesAngles(sRef, sTest, lRefTemp,
						lTestTemp)
						+ Math.sqrt(Math.pow(sRef.getDonnees()[lRef.get(0).i].x
								- sTest.getDonnees()[lTest.get(i).i].x, 2)
								+ Math.pow(sRef.getDonnees()[lRef.get(0).i].y
										- sTest.getDonnees()[lTest.get(i).i].y,
										2)) / Math.sqrt(2);
			}
		}

		return min / lRef.size();
	}

	public static double comparaison(Signature s1, Signature s2) {
		return 1 - scoreMinutiesCourbure(s1, s2);
	}

}
