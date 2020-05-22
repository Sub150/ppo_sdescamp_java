package solveur;

import java.util.ArrayList;

public class Ville {
	//Variables d'instance
	String nom;
	int numero, coordX, coordY; 
    ArrayList<Voie> listeVoiesSortantes = new ArrayList<Voie>();
    
	//Méthodes
	String getNom(){
		return this.nom;
	}
	
	int getCoordX(){
		return this.coordX;
	}
	
	int getCoordY(){
		return this.coordY;
	}
	
	public ArrayList<Voie> getVoiesSortantes(){
		return this.listeVoiesSortantes;
	}
	

	
	public String toString(){
		return this.getNom();
	}
	//Constructeur
	public Ville(String nom, int num, int X, int Y){
		this.nom = nom;
		numero = num;
		coordX = X; 
		coordY = Y;
	}
	
}
