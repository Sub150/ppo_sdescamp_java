package solveur;
import java.util.*;

public class Carte {
	//ListeChainee<Voie> listeVoies = new ListeChainee<Voie>(10);
	protected ArrayList<Ville> carte = new ArrayList<Ville>();
	
	public void addVille(Ville v){
		carte.add(v);
	}
	
	public void addVoie(Voie v){
		v.getVilleDepart().listeVoiesSortantes.add(v);
	}
	
	public Ville getVilleFromNom(String v) throws VilleInconnueException{
		for (Ville ville : carte)
			if ( ville.getNom().equals(v))
				return ville;
		throw new VilleInconnueException();
	}
	
	public Ville getVilleFromCoord(int x, int y) throws VilleInconnueException{
		for ( Ville v : carte )
			if (v.getCoordX() == x && v.getCoordY()==y)
				return v;
		throw new VilleInconnueException();
	}
}
