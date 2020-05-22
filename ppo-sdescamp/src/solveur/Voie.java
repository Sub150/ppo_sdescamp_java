package solveur;

public abstract class Voie {
	    //Variables d'instance
		int numero;
		private int vitesse;
	    private double distance;
	    private String nom; 
	    private Ville villeDepart;
	    private Ville villeArrivee;
	    static final double prixEssence = 1.5; //constante

	    //Methodes
	    public Ville getVilleDepart(){
	    	return this.villeDepart;
	    }
	    
	    public Ville getVilleArrivee(){
	    	return this.villeArrivee;
	    }
	    
	    public double getDistance(){
	    	return this.distance;
	    }
	    
	    public int getVitesse(){
	    	return this.vitesse;
	    }
	    
	    public double getTemps(){
	    	return this.distance/this.vitesse;
	    }
	    
	    public String getNom(){
	    	return this.nom;
	    }
	    
	    public double getCout(){
	    	int conso;
	    	if (this.getVitesse() < 100)
	    		conso = 6;
	    	else conso = 7;
	    	
	    	return ( (this.getDistance()*conso)/100 * prixEssence);
	    	
	    }
	    
	    public double getPoids(String method) throws MethodeInconnueException{
	    	if ( method == "Distance")
	    		return this.getDistance();
	    	else if (method == "Cout")
	    		return this.getCout();
	    	else if (method == "Temps")
	    		return this.getTemps();
	    	else throw new MethodeInconnueException();
	    	
	    }
	    
	    abstract double getPeage();
	    
	    abstract int getNbRalentissements();
	    
	    //Constructeur 
	    Voie(Ville depart, Ville arrivee, int num, int speed, double dist, String nom){
	    	villeDepart = depart;
	    	villeArrivee = arrivee;
	    	numero = num;
	    	vitesse = speed;
	    	distance = dist;
	    	this.nom = nom;
	    	
	    }
}

