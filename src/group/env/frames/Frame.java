package group.env.frames;

import group.env.menu.Menu;
import javafx.scene.control.SplitPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Frame extends JFrame{
    //les attributs
    private JPanel container = new JPanel(new BorderLayout(5,3));
    private static JTextArea zoneDeSaisie;
    private static TextArea zoneDeStatus;
    private static FileTree listFichier;
    private static String selectionTextContent = "";
    private static  String openedFilePath = null;
    private static JSplitPane split1 = new JSplitPane(), split2 = new JSplitPane();
    public static JScrollPane zoneDeSaisieScrollPane;
    //attributs relatifs a la gestions des onglets
    private static final int nbrOnglet = 10;
    public static int compteurTextArea = 0;
    public static List<JTextArea> textAreas = new ArrayList<>();
    public static JTabbedPane onglets = new JTabbedPane();

    //constructeurs par defaut;
    public Frame()
    {
        this.setTitle("Env");
        this.setSize(900, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this);
    }

    //constructeur avec parametre
    //pour la creation d'un nouveau fichier
    public Frame(String nom, int x, int y, Menu menu){
        //initialisation des composants principaux de notre Frame
        this.setTitle(nom);
        this.setSize(x, y);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //initialisation des elements graphique
        this.initContentpane(nom,x,y,menu,"");
        this.fermetureOngletEvent();

        //on redefini le contentPane
        this.getContentPane().add(split2, BorderLayout.CENTER);

        //on affiche la Frame
        this.setVisible(true);
    }
    //
    //pour l'ouverture d'un fichier existant
    public Frame(String nom, int x, int y, Menu menu, String zoneDeSaisie){
        //initialisation des composants principaux de notre Frame
        this.setTitle(nom);
        this.setSize(x, y);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //initialisation des elements graphique
        this.initContentpane(nom,x,y,menu,zoneDeSaisie);
        this.fermetureOngletEvent();

        //on affiche la Frame
        this.getContentPane().add(split2, BorderLayout.CENTER);
        this.setVisible(true);
    }

    //methodes

    private void initContentpane(String nom, int x, int y, Menu menu, String zoneDeSaisie)
    {
        String name = "unsaved";
        if(!zoneDeSaisie.equals(""))
            name = this.openedFilePath;
        else zoneDeSaisie = "//ecrivez votre code ici";
        this.zoneDeSaisie = new TextArea(zoneDeSaisie);
        try {
            Frame.listFichier = new FileTree(new File("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        zoneDeSaisieScrollPane = new JScrollPane(this.zoneDeSaisie);
        this.zoneDeStatus = new TextArea("Infos divers");
        this.onglets.add(name,zoneDeSaisieScrollPane);
        Frame.textAreas.add(this.zoneDeSaisie);

        this.setJMenuBar(menu);
        split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,listFichier,this.onglets);
        split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1,new JScrollPane(this.zoneDeStatus) );
        split2.setDividerLocation(this.getHeight()-100);

    }

    private void fermetureOngletEvent(){
        onglets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(Menu.popupMenu.isPopupTrigger(e))
                    Menu.popupMenu.show(onglets, e.getX(), e.getY());
            }
        });
    }

    //getters and setters

    public JPanel getContainer() {
        return container;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    public static JTextArea getZoneDeSaisie() {
        return Frame.zoneDeSaisie;
    }

    public static void setZoneDeSaisie(JTextArea zoneDeSaisie) {
        Frame.zoneDeSaisie = zoneDeSaisie;
    }

    public static TextArea getZoneDeStatus() {
        return Frame.zoneDeStatus;
    }

    public static void setZoneDeStatus(TextArea zoneDeStatus) {
        Frame.zoneDeStatus = zoneDeStatus;
    }

    public static FileTree getListFichier() {
        return Frame.listFichier;
    }

    public static void setListFichier(FileTree listFichier) {
        Frame.listFichier = listFichier;
    }

    public static String getSelectionTextContent() {
        return selectionTextContent;
    }

    public static void setSelectionTextContent(String selectionTextContent) {
        Frame.selectionTextContent = selectionTextContent;
    }

    public static String getOpenedFilePath() {
        return openedFilePath;
    }

    public static void setOpenedFilePath(String openedFilePath) {
        Frame.openedFilePath = openedFilePath;
    }

}