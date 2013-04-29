package minuties;
import java.util.LinkedList;

/* Classe DonneesAnglesSortedLinkedList
 * ---------------
 * Role : Manipule une liste ordonnée d'angles
 * --------------
 */

public class DonneesAnglesSortedLinkedList extends LinkedList<DonneesAngles>{
   	private static final long serialVersionUID = 1L;

	public boolean add(DonneesAngles d) {
        int index = 0;
        for( ; index<size() ; index++){
            DonneesAngles current = get(index);

            if(d.angle<current.angle){
                break;
            }
        }

        add(index, d);
        return true;

    };
}
