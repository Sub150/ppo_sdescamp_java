package parseur;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import algorithme.CheminInexistantException;
import algorithme.Dijkstra;
import solveur.Carte;
import solveur.Chemin;
import solveur.MethodeInconnueException;
import solveur.VilleInconnueException;

import java.io.*;

/**
 * Exemple d'application ({@code main}) utilisant {@link MapHandler}.<br>
 * 
 * Usage : {@code java parseur.MapParser fichier.xml}
 * 
 * @author Bernard.Carre -at- polytech-lille.fr
**/


public class MapParser {

	/**
	 * Usage : {@code java parseur.MapParser fichier.xml}
	 * 
	 * @param argv[0] fichier.xml
	 * @throws java.io.IOException Erreur d'acc&egrave;s au fichier.
	 * @throws org.xml.sax.SAXException Erreur de parsing.
	**/
  public static void main(String argv[]) throws IOException, SAXException {
    System.out.println("analyse de "+argv[0]+"...");

    // Le parseur SAX
    XMLReader reader;
		reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

    // Creation d'un flot XML sur le fichier d'entree
    InputSource input = new InputSource(new FileInputStream(argv[0]));
    
    MapHandler M = new MapHandler();
    // Connexion du ContentHandler
    reader.setContentHandler(M);
    // Lancement du traitement...
    reader.parse(input);
    Carte C = new Carte();
    C = M.getMap();
    Dijkstra d = new Dijkstra();
    try {
    	Chemin ch= d.PCC(C.getVilleFromNom("Lille"), C.getVilleFromNom("Calais"), "Distance");
        System.out.printf("%s",ch);
    }
    catch (CheminInexistantException e) 
    {	
    	System.out.printf("pb de chemin\n");
    }
    catch (MethodeInconnueException e){
    	System.out.printf("pb de methode\n");
    }
    catch (VilleInconnueException e){
    	System.out.printf("pb de ville\n");
    }

	

  }
}
