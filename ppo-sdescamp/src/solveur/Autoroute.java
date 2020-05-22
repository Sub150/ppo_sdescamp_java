package solveur;

public class Autoroute extends Voie{
	//Variables d'instance
	double peage;
	
	//Methodes
	@Override
	double getPeage() {
		return this.peage;
	}

	@Override
	int getNbRalentissements() {
		return 0;
	}
	
	//Constructeur 
	public Autoroute(Ville depart, Ville arrivee, int num, int speed, double dist, String nom, double peage){
		super(depart, arrivee, num, speed, dist, nom );
		this.peage=peage;
	}
	
	public double getCout(){
		return super.getCout() + this.getPeage();
	}
	
	
	
}