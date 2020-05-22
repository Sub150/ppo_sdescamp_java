package solveur;

import java.util.LinkedList;

public class Chemin {
	protected LinkedList<Voie> Chemin = new LinkedList<Voie>();
	
	double getDistanceChemin(){
		double distance = 0;
		for ( Voie v : Chemin ){
			distance+= v.getDistance();
		}
		return distance;
	}
	
	public void add(Voie v){
		Chemin.addFirst(v);
	}
	
	public void clear(){
		Chemin.clear();
	}
	
	double getCoutChemin(){	
		double cout = 0;
		for ( Voie v : Chemin ){
			cout+= v.getCout();
		}
		return cout;	
	}
	
	double getTempsChemin(){	
		double temps = 0;
		for ( Voie v : Chemin ){
			temps+= v.getTemps();
		}
		return temps;	
	}
	
	public double arrondir(double nombre,double nbApVirg){
		return(double)((int)(nombre * Math.pow(10,nbApVirg) + .5)) / Math.pow(10,nbApVirg);
	}
	
	public String toString(){
		String ch;
		int dureeH = (int)getTempsChemin() ;
		int dureeM= (int)(this.getTempsChemin()*60) - dureeH*60;
		int dureeS = (int)(this.getTempsChemin()*3600 - (dureeM*60 + dureeH*3600));

		ch ="Meilleur chemin entre "+Chemin.get(0).getVilleDepart() +" et "+Chemin.get(Chemin.size() - 1 ).getVilleArrivee()+" : \n" ;
		for ( Voie v : Chemin ){
			ch+="- "+v.getNom()+" de "+v.getVilleDepart()+" à  "+v.getVilleArrivee()+"\n";
		}
		ch+="Durée du trajet : " + dureeH+"h "+dureeM+"m "+dureeS+"s"+"\n";
		ch+="Distance du trajet : " + arrondir(this.getDistanceChemin(), 2)+"km\n";
		ch+="Cout du trajet : " + arrondir(this.getCoutChemin(), 2)+"\n";
		return (ch);
	}
}