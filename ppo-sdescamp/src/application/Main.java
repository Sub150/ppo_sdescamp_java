package application;

import java.io.FileInputStream;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ihm.Proto;
import parseur.MapHandler;
import solveur.Carte;

public class Main {
	static Carte minimap = new Carte();
	
	public static void main(String argv[]) throws IOException, SAXException {
		if (argv.length != 2 )
			System.err.println("usage : java applications.Proto fichierImage.jpg");
		else {
		System.out.println("analyse de "+argv[0]+"...");
	    // INDEX OUT OF BOUDS 
	    // Initialisation du parseur SAX
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
	    
	    //Lancement de l'interface graphique
	    Proto window = new Proto(argv[1], C);
		window.setTitle("Mini GPS");
		window.setSize(600, 640);
		window.setVisible(true);
		}
	  }

}
