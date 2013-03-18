package minutiesVectorielles;
import java.util.LinkedList;

public class DonneesAnglesSortedLinkedList extends LinkedList<DonneesAngles>{
   
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
