package algorithme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import solveur.Chemin;
import solveur.MethodeInconnueException;
import solveur.Ville;
import solveur.Voie;

public class Dijkstra {
	private Chemin chemin = new Chemin();
	private Map<Ville, Boolean> Mark = new HashMap<Ville,Boolean>();
	private Map<Ville, Voie> Pere = new HashMap<Ville,Voie>();
	private Map<Ville, Double> Potentiel = new HashMap<Ville,Double>();
	
	public Chemin PCC (Ville dep, Ville arr, String method) throws CheminInexistantException, MethodeInconnueException {
		Mark.clear();
		Pere.clear();
		Potentiel.clear();
		chemin.clear();
		ArrayList<Voie> listeVoies = new ArrayList<Voie>();
		Voie curr;
		Ville Current; 
		Mark.put(dep,true);	
		Potentiel.put(dep, (double) 0);
		Current = dep;
		
		//Exécution
		while ( Current != arr){
			listeVoies=Current.getVoiesSortantes(); 
			for ( Voie succ : listeVoies ){
				if (Mark.get(succ.getVilleArrivee()) == null){
					if (Potentiel.get(succ.getVilleArrivee()) == null || 
							Potentiel.get(succ.getVilleArrivee()) 
							> Potentiel.get(succ.getVilleDepart()) + succ.getPoids(method)) {
						Potentiel.put(succ.getVilleArrivee(),(Potentiel.get(succ.getVilleDepart()) 
								+ succ.getPoids(method)) ) ;
						Pere.put(succ.getVilleArrivee(), succ);
					}
				}
			}
			//Recherche de la ville au plus petit potentiel non marquée
			double min=-1;
			Boolean premier = true;
			Current = null;
			for (Ville v : Potentiel.keySet()){
				if ( Mark.get(v) == null){
					if (premier || Potentiel.get(v) <= min ){
						min = Potentiel.get(v);
						Current = v;
						premier = false;
					}
				}
			}
			if ( Current == null ){
				throw new CheminInexistantException();
			}
			else 
				Mark.put(Current,true);
		}
		//Construction du chemin
		curr = Pere.get(arr);
		while (curr.getVilleDepart() != dep){
			chemin.add(curr);
			curr = Pere.get(curr.getVilleDepart());
		}
		chemin.add(curr);	
		return chemin;
	}
}
