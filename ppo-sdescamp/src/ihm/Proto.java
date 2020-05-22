package ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import algorithme.Dijkstra;
import solveur.Carte;
import solveur.Chemin;
import solveur.MethodeInconnueException;
import solveur.Ville;
import solveur.VilleInconnueException;
import algorithme.CheminInexistantException;

/**
 * Prototype d'interface graphique :<br>
 * - charge une image {@code .jpg} de carte (en param&egrave;tre du main)<br>
 * - lui superpose une grille (X,Y) en vue du rep&eacute;rage de lieux par leurs coordonn&eacute;es pour en faire un
 * plan "cliquable"<br>
 * - ou permet de saisir un lieu directement par son nom.<br>
 * Ce prototype ne fait que r&eacute;afficher les coordonn&eacute;es cliqu&eacute;es et le nom du lieu saisi dans une
 * zone de texte (resultTextArea).<br>
 * <br>
 * 
 * Usage : {@code java ihm.Proto fichierImage.jpg}
 * 
 * @author Bernard.Carre -at- polytech-lille.fr
 * 
 */

@SuppressWarnings("serial")
public class Proto extends JFrame {
	Carte map = new Carte();

	/**
	 * Pr&eacute;cision de la grille de rep&eacute;rage.
	 * 
	 */
	protected static final int DELTA = 20;
	/**
	 * Taille du plan.
	 */
	protected int hauteurPlan, largeurPlan;

	/**
	 * Plan "cliquable" = image + grille.
	 */
	protected ImageCanvas canvas = new ImageCanvas();
	/**
	 * Vue "scrollable" du plan.
	 */
	protected ScrollPane mapView = new ScrollPane();
	/**
	 * Panel d'interaction avec l'utilisateur
	 */
	protected InteractionPanel interact = new InteractionPanel();

	/**
	 * Charge une image de carte et construit l'interface graphique.
	 * 
	 * @param fichierImage
	 *            Nom du fichier image de carte
	 * @throws java.io.IOException
	 *             Erreur d'acc&egrave;s au fichier
	 */
	public Proto(String fichierImage, Carte M) throws java.io.IOException {
		map = M;
		// Chargement de l'image
		Image im = new ImageIcon(fichierImage).getImage();
		hauteurPlan = im.getHeight(this);
		largeurPlan = im.getWidth(this);

		// Preparation de la vue scrollable de l'image
		canvas.setImage(im); // image a afficher dans le Canvas
		canvas.addMouseListener(interact.getSelectionPanel()); // notification de clic sur la grille
		mapView.setSize(hauteurPlan + DELTA, largeurPlan + DELTA);
		mapView.add(canvas); // apposition de la vue scrollable sur l'ImageCanvas

		// Construction de l'ensemble
		setLayout(new BorderLayout());
		add(mapView, BorderLayout.CENTER);
		add(interact, BorderLayout.SOUTH);

		// Evenement de fermeture de la fenetre : quitter l'application
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				System.exit(0);
			}
		});
	}

	/**
	 * Classe utilitaire interne (sous-classe de {@code java.awt.Canvas}) = image de carte + grille "cliquable".
	 * 
	 */
	class ImageCanvas extends Canvas {
		Image image;

		void setImage(Image img) {
			image = img;
			setSize(largeurPlan, hauteurPlan);
			repaint();
		}

		/**
		 * Affiche l'image + la grille.
		 * 
		 */
		public void paint(Graphics g) {
			if (image != null)
				g.drawImage(image, DELTA, DELTA, this);

			// Grille de repÃ©rage apposÃ©e
			int lignes = hauteurPlan / DELTA;
			int colonnes = largeurPlan / DELTA;
			g.setColor(Color.gray);
			for (int i = 1; i <= lignes; i++) {
				g.drawString("" + i, 0, (i + 1) * DELTA);
				g.drawLine(DELTA, i * DELTA, DELTA + largeurPlan, i * DELTA);
			}
			g.drawLine(DELTA, (lignes + 1) * DELTA, DELTA + largeurPlan, (lignes + 1) * DELTA);
			for (int i = 1; i <= colonnes; i++) {
				g.drawString("" + i, i * DELTA, DELTA / 2);
				g.drawLine(i * DELTA, DELTA, i * DELTA, DELTA + hauteurPlan);
			}
			g.drawLine((colonnes + 1) * DELTA, DELTA, (colonnes + 1) * DELTA, DELTA + hauteurPlan);
		}
	}

	/**
	 * 
	 * Panel d'interaction avec l'utilisateur comprenant: <BR>
	 * - un sous-panel de s&eacute;lection utilisateur : {@link InteractionPanel#selectionPanel} (&agrave; l'&eacute;coute de clics utilisateur)<BR>
	 * - une zone de texte pour l'affichage des r&eacute;sultats : {@link InteractionPanel#resultTextArea}.
	 * 
	 */
	class InteractionPanel extends JPanel {
		/**
		 * sous-panel de s&eacute;lection utilisateur
		 */
		SelectionSubPanel selectionPanel = new SelectionSubPanel();
		/**
		 * zone de texte pour l'affichage des r&eacute;sultats
		 */
		JTextArea resultTextArea = new JTextArea(5, 30);

		InteractionPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(selectionPanel);
			resultTextArea.setEditable(false); // non editable (produit par les resultats de l'application)
			add(resultTextArea);
			resultTextArea.setText("Bienvenue sur notre GPS.\nChoisissez les villes de départ et d'arrivée \nainsi que la méthode puis cliquez sur GO !");
		}

		/**
		 * 
		 * Sous-panel de s&eacute;lection utilisateur :<BR>
		 * - &agrave; l'&eacute;coute (implements MouseListener) de clics utilisateur sur le plan:
		 * enregistre les coordonn&eacute;es (x,y) et les affiche dans des {@code Label} "X=...", "Y=..."<BR>
		 * - permet de saisir un nom de lieu dans le JTextField "lieu"<BR>
		 * - bouton : "GO!" : reporte les saisies dans la zone "resultTextArea"
		 * 
		 */
		class SelectionSubPanel extends JPanel implements MouseListener {
			Ville v, villeDep = null, villeArr = null;
			String method = "";
			Dijkstra d = new Dijkstra();
			Chemin ch = new Chemin();	
			/**
			 * Valeurs de x, y cliqu&eacute;s
			 */
			int x, y;
			/**
			 * Affichage de X,Y cliqu&eacute;
			 */
			JLabel xLabel = new JLabel("X =");
			/**
			 * Affichage de X,Y cliqu&eacute;
			 */
			JLabel yLabel = new JLabel("Y =");
			/**
			 * Champ de saisie d'un nom de lieu
			 */
			JLabel lieuLabel = new JLabel("LIEU");
			JTextField lieu1 = new JTextField(20);
			JTextField lieu2 = new JTextField(20);
			/**
			 * Reporte les saisies dans la zone "resultTextArea"
			 */
			JButton go = new JButton("GO!"); // reporte les saisies dans la zone "resultTextArea"
			
			JButton setdepart = new JButton("SET DEPART");
			JButton setarrivee = new JButton("SET ARRIVEE");
			
			ButtonGroup buttonGroup = new ButtonGroup();
			JRadioButton pr = new JRadioButton("Plus rapide");
			JRadioButton pc = new JRadioButton("Plus court");
			JRadioButton mc = new JRadioButton("Moins cher");
			

			// Construction et ecouteur du bouton "GO!"
			SelectionSubPanel() {
				
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // organisation verticale
				add(xLabel);
				add(yLabel);
				add(lieuLabel);
				add(lieu1);
				add(setdepart);
				add(lieu2);
				add(setarrivee);
				buttonGroup.add(pr);
				buttonGroup.add(pc);
				buttonGroup.add(mc);
				add(pr);
				add(pc);
				add(mc);
				add(go);
				setdepart.addActionListener(evt -> {
					try {
						villeDep = map.getVilleFromNom(lieu1.getText());
						lieu1.setText(villeDep.toString());
						resultTextArea.setText("Ville de départ :" + villeDep + "\n");
					}
					catch ( VilleInconnueException e) {
						villeDep = v;
						if ( villeDep != null ){
							lieu1.setText(villeDep.toString());
							resultTextArea.setText("Ville de départ :" + villeDep + "\n");
						}
						else 
							resultTextArea.setText("Cette ville n'existe pas");
					}
				});
				
				setarrivee.addActionListener(evt -> {
					try {
						villeArr =  map.getVilleFromNom(lieu2.getText());
						lieu2.setText(villeArr.toString());
						resultTextArea.setText("Ville d'arrivee :" + villeArr + "\n");
					} catch ( VilleInconnueException e) 
					{
						villeArr = v ;
						if ( villeArr != null ) {
							lieu2.setText(villeArr.toString());
							resultTextArea.setText("Ville d'arrivee :" + villeArr + "\n");
						}
						else 
						resultTextArea.setText("Cette ville n'existe pas");
					}
				});
				
				pr.addActionListener(evt -> {
					if ( pr.isSelected()) 
						method = "Temps";
				});
				pc.addActionListener(evt -> {
					if ( pc.isSelected()) 
						method = "Distance";
				});
				mc.addActionListener(evt -> {
					if ( mc.isSelected()) 
						method = "Cout";
				});
				
				
				go.addActionListener(evt -> {
					if ( villeDep == null || villeArr == null ){
						resultTextArea.setText("Vous n'avez pas entré de villes valides");
					}
					else if ( method == ""){
						resultTextArea.setText("Vous n'avez choisi aucune methode");
					}
					else if ( villeDep == villeArr ){
						resultTextArea.setText("Veuillez rentrer une ville d'arrivée différente de la ville de départ");
					}
					else {
						try {
							Chemin ch= d.PCC(villeDep, villeArr, method);
							String m = "";
							if ( method == "Temps" ) {
								m = "Le plus rapide";
							}
							else if ( method == "Cout") {
								m = "Le moins cher";
							}
							else if ( method == "Distance"){
								m = "Le plus court";
							}
							resultTextArea.setText("Vous avez choisi la ville de départ : "
									+villeDep.toString() 
									+"\n Vous avez choisi comme ville d'arrivée : "
									+villeArr.toString()
									+"\n Vous avez choisi comme methode : "
									+m+"\n"
									+"Veuillez suivre l'itinéraire suivant : \n"
									+ch.toString());
							
						} catch ( CheminInexistantException e ) 
						{
							resultTextArea.setText("Il n'existe aucun chemin entre "+ villeDep +" et "+ villeArr+"\n");	
						}
						catch ( MethodeInconnueException e) 
						{
							resultTextArea.setText("Il n'existe aucun chemin entre "+ villeDep +" et "+ villeArr+"\n");	
						}
					}
				}); 
			}

			/**
			 * 
			 * R&eacute;action au clic utilisateur sur la grille (implements MouseListener) <BR>
			 * - methode "void mouseReleased(MouseEvent e)" : s&eacute;lection de coordonn&eacute;es<BR>
			 * - autres mÃ©thodes sans effet <BR>
			 * 
			 */
			public void mouseReleased(MouseEvent e) {
				x = e.getX() / DELTA;
				y = e.getY() / DELTA;
				xLabel.setText("X = " + x);
				yLabel.setText("Y = " + y);
				try {
					v = map.getVilleFromCoord(x,y);
					lieuLabel.setText("Lieu : " + v);
				} catch ( VilleInconnueException ec ) 
				{
					v = null;
					lieuLabel.setText("Lieu : Aucune ville selectionnee");
				}

				

			}

			/**
			 * NOP
			 */
			public void mousePressed(MouseEvent e) {
			}

			/**
			 * NOP
			 */
			public void mouseClicked(MouseEvent e) {
			}

			/**
			 * NOP
			 */
			public void mouseEntered(MouseEvent e) {
			}

			/**
			 * NOP
			 */
			public void mouseExited(MouseEvent e) {
			}
		}

		SelectionSubPanel getSelectionPanel() {
			return selectionPanel;
		}

	}

	/**
	 * 
	 * Usage : {@code java applications.Proto fichierImage.jpg}
	 * 
	 * @param argv[0]
	 *            fichierImage.jpg
	 * @throws java.io.IOException
	 *             Erreur d'acc&egrave;s au fichier
	 */
	public static void main(String argv[]) throws java.io.IOException {
		if (argv.length != 1)
			System.err.println("usage : java applications.Proto fichierImage.jpg");
		else {
			Proto window = new Proto(argv[0], new Carte());
			window.setTitle("Projet GPS");
			window.setSize(600, 640);
			window.setVisible(true);
		}
	}
}