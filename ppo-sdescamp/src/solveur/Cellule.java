package solveur;

class Cellule<E> {
    E value;
    Cellule<E> next;
    
    //Methode
    E getValeur(){
        return value;
    }
    
    Cellule<E> getNext(){
        return next;
    }
    
    //Constructeur 
    Cellule(E x,Cellule<E> suiv){
        this.value = x;
        this.next=suiv;
    }
}