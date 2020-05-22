package parseur;

import org.xml.sax.*;

import solveur.Ville;
import solveur.VilleInconnueException;
import solveur.Autoroute;
import solveur.Carte;
import solveur.Route;

/**
 * Prototype d'un Handler XML pour SAX: ne fait que tracer (print) les &eacute;l&eacute;ments reconnus.<br>
 * Exemple d'utilisation: voir {@link MapParser}
 * 
 * @author Bernard.Carre -at- polytech-lille.fr
 * 
 **/

public class MapHandler implements ContentHandler {
	/**
	 * Nom de la balise courante.
	 * 
	 **/
	Carte map = new Carte();
	String currentTag, nom;
	double distance;
	Ville villeDep, villeArr;
	int numero, vitesse, ralentissement, peage, coordX, coordY ;
	
	/**
	 * Traitement d'ouverture du document XML : balise {@code <docbase>}.
	 * 
	 **/
	public void startDocument() throws SAXException {
		System.out.println("start document...");
	}
	/**
	 * Traitement de fermeture du document XML : balise {@code </docbase>}
	 * 
	 **/

	public void endDocument() throws SAXException {
		System.out.println("\nDocument termine.");
	}

	/**
	 * Traitement de balise ouvrante.<br>
	 * Ici : affiche son nom ({@code localName}) et positionne : {@link #currentTag} = ({@code localName}).
	 *  
	 **/ 
	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
			throws SAXException {
		if (localName.equals("ville"))
			System.out.println("\nVILLE... ");

		else if (localName.equals("route"))
			System.out.println("\nROUTE... ");
		else if (localName.equals("autoroute"))
			System.out.println("\nAUTOROUTE... ");

		else // autres elements...
			System.out.println("startElement: " + localName);
		currentTag = localName; // set variable "currentTag"
	}

	/**
	 * Traitement de balise fermante.<br>
	 * Ici: affiche "FIN " + son nom ({@code localName}) et reset de la variable {@link #currentTag}.
	 *  
	 **/
	public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
		if (localName.equals("ville")){
			map.addVille( new Ville(nom, numero, coordX, coordY) );
			System.out.println("FIN VILLE.");
		}
		else if (localName.equals("route")){
			map.addVoie( new Route(villeDep, villeArr, numero, vitesse, distance, nom, ralentissement) );
			System.out.println("FIN ROUTE.");
		}
		else if (localName.equals("autoroute")){
			map.addVoie( new Autoroute(villeDep, villeArr, numero, vitesse, distance, nom, peage) );
			System.out.println("FIN AUTOROUTE.");
		}
		else // Autres elements eventuels...
			System.out.println("endElement: " + localName);
		currentTag = null; // reset variable "currentTag"
	}
	
	public Carte getMap(){
		return this.map;
	}
	/**
	 * Traitement du contenu texte ({@code char[] ch}) d'une balise.<BR>
	 * Ici : l'affiche (si existe, balises terminales d'information).
	 *  
	 **/
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentTag != null) {
			if (currentTag.equals("docbase") || currentTag.equals("ville") || currentTag.equals("route")
					|| currentTag.equals("autoroute")) {
				// Enclosing elements : no proper characters description, but introducing elements of sub-content 
				System.out.println("content:");
			} 
			else if (currentTag.equals("numero")) {
				String subContent = new String(ch, start, length);
				numero = Integer.parseInt(subContent);
				System.out.println("Numero: " + numero); 
			}
			else if (currentTag.equals("nom")) {
				String subContent = new String(ch, start, length);
				nom = subContent;
				System.out.println("Nom: " + nom);
			}
			else if (currentTag.equals("vitesse")) {
				String subContent = new String(ch, start, length);
				vitesse = Integer.parseInt(subContent);
				System.out.println("Vitesse: " + vitesse);
			}
			else if (currentTag.equals("peage")) {
				String subContent = new String(ch, start, length);
				peage = Integer.parseInt(subContent);
				System.out.println("Peage: " + peage);
			}
			else if (currentTag.equals("distance")) {
				String subContent = new String(ch, start, length);
				distance = Double.parseDouble(subContent);
				System.out.println("Distance: " + distance);
			}
			else if (currentTag.equals("entre")) {
				String subContent = new String(ch, start, length);
				try {
					villeDep = map.getVilleFromNom(subContent);
					System.out.println("Ville de depart: " + villeDep);
				} catch (VilleInconnueException e) {
					System.out.println("Problème dans la map");
				}
			}
			else if (currentTag.equals("et")) {
				String subContent = new String(ch, start, length);
				try {
					villeArr = map.getVilleFromNom(subContent);
					System.out.println("Ville de d'arrivee: " + villeArr);
				} catch ( VilleInconnueException e) 
				{
					System.out.println("Problème dans la map");
				}
			}
			else if (currentTag.equals("x")) {
				String subContent = new String(ch, start, length);
				coordX = Integer.parseInt(subContent);
				System.out.println("CoordX: " + coordX);
			}
			else if (currentTag.equals("y")) {
				String subContent = new String(ch, start, length);
				coordY = Integer.parseInt(subContent);
				System.out.println("CoordY: " + coordY);
			}
			else if (currentTag.equals("ralentis")) {
				String subContent = new String(ch, start, length);
				ralentissement = Integer.parseInt(subContent);
				System.out.println("Ralentissement: " + ralentissement);
			}
			else { 
				// Elements of sub-content
				String subContent = new String(ch, start, length);
				System.out.println("content:" + subContent);
			}
		}
	}

	// Autres mÃ©thodes de ContentHandler: pas de traitement particulier ici.
	/**
	 * NOP
	 *  
	 **/	
	public void startPrefixMapping(String prefix, String uri) {
	}
	/**
	 * NOP
	 *  
	 **/
	public void endPrefixMapping(String prefix) {
	}
	/**
	 * NOP
	 *  
	 **/
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}
	/**
	 * NOP
	 *  
	 **/
	public void processingInstruction(String target, String data) throws SAXException {
	}
	/**
	 * NOP
	 *  
	 **/
	public void skippedEntity(String name) throws SAXException {
	}
	/**
	 * NOP
	 *  
	 **/
	public void setDocumentLocator(Locator locator) {
	}
}
