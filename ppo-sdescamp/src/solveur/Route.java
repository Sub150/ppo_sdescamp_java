package solveur;

public class Route extends Voie {
	//Variables d'instance
	int nbRalentissements;
	
	//Methodes 	
	@Override
	int getNbRalentissements() {
		return this.nbRalentissements;
	} 

	@Override
	double getPeage(){
		return 0;
	}
	
	//Constructeur
	public Route(Ville depart, Ville arrivee, int num, int speed, double dist, String nom, int Ral){
		super(depart, arrivee, num, speed, dist, nom );
		nbRalentissements=Ral;
	}
	
	public double getTemps(){
		return super.getTemps() + ((double)(this.getNbRalentissements())/(double)(60));
	}
		
}