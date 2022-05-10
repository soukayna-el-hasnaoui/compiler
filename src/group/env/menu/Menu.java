package group.env.menu;

import group.compilatreur.al.AnalyseurLexical;
import group.compilatreur.al.CategorieLexical;
import group.compilatreur.as.AnalyseurSynthaxique;
import group.compilatreur.as.GrammaireException;
import group.compilatreur.interpreteur.Interpreteur;
import group.env.file.FileIO;
import group.env.frames.FileChooser;
import group.env.frames.Frame;
import group.env.frames.TextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.ImageIcon;

public class Menu extends JMenuBar {
    public static JPopupMenu popupMenu = new JPopupMenu();
    public Menu()
    {
        //creation du menu fichier
        JMenu fichier = new JMenu("Fichier");
            //ajout des items
            JMenuItem creer = new JMenuItem("Créer", new ImageIcon("icons\\create.png"));
            JMenuItem ouvrir = new JMenuItem("Ouvrir", new ImageIcon("icons\\open.png"));
            JMenuItem save = new JMenuItem("Sauvegarder", new ImageIcon("icons\\save.png"));
            fichier.add(creer);
            fichier.add(ouvrir);
            fichier.add(save);
            //ajout de raccourci
            fichier.setMnemonic('f');
            creer.setMnemonic('n');
            save.setMnemonic('s');
            ouvrir.setMnemonic('o');
            //ajout d'evenement
            creer.addActionListener(new MenuItemCreer());
            ouvrir.addActionListener(new MenuItemOuvrir());
            save.addActionListener(new MenuItemSauvegarder());
        //creation du menu édition
        JMenu edition = new JMenu("Edition");
            //ajout d'items
            JMenuItem copier = new JMenuItem("Copier", new ImageIcon("icons\\copy.png"));
            JMenuItem coller = new JMenuItem("Coller", new ImageIcon("icons\\paste.png"));
            JMenuItem couper = new JMenuItem("Couper", new ImageIcon("icons\\cut.png"));
            edition.add(couper);
            edition.add(copier);
            edition.add(coller);
            //ajout de raccourci
            edition.setMnemonic('e');
            copier.setMnemonic('c');
            coller.setMnemonic('v');
            couper.setMnemonic('x');
            //ajout d'evenement
            copier.addActionListener(new MenuItemCopier());
            coller.addActionListener(new MenuItemColler());
            couper.addActionListener(new MenuItemCouper());
        //creation du menu de d'action
        JMenu action = new JMenu("Action");
            //ajout des items
            JMenuItem compiler = new JMenuItem("Compiler", new ImageIcon("icons\\compile.png"));
            JMenuItem run = new JMenuItem("Run", new ImageIcon("icons\\run.png"));
            action.add(compiler);
            action.add(run);
            //ajout de raccourci
            action.setMnemonic('a');
            compiler.setMnemonic('c');
            run.setMnemonic('r');
        //ajout d'evenement
            //ajout d'evenement
            compiler.addActionListener(new MenuItemCompiler());
            run.addActionListener(new MenuItemRun());

        //on ajoute les menu creer a notre menu bar
        this.add(fichier);
        this.add(edition);
        this.add(action);

        //mise en place de la popupmenu
        JMenuItem popupFermer = new JMenuItem("Fermer", new ImageIcon("icons\\close.png"));
        JMenuItem popupCreer = new JMenuItem("Créer", new ImageIcon("icons\\create.png"));
        JMenuItem popupOuvrir = new JMenuItem("Ouvrir", new ImageIcon("icons\\open.png"));
        JMenuItem popupSave = new JMenuItem("Sauvegarder", new ImageIcon("icons\\save.png"));
        JMenuItem popupRun = new JMenuItem("Run", new ImageIcon("icons\\run.png"));
        JMenuItem popupCompiler = new JMenuItem("Compiler", new ImageIcon("icons\\compile.png"));
        popupFermer.addActionListener(actionEvent -> {
            //avant de supprimer l'element on supprimer son text area correspondant
            Frame.textAreas.remove(Frame.onglets.getSelectedIndex());
            Frame.onglets.remove(Frame.onglets.getSelectedIndex());
        });
            //
        popupCreer.addActionListener(new MenuItemCreer());
        popupOuvrir.addActionListener(new MenuItemOuvrir());
        popupSave.addActionListener(new MenuItemSauvegarder());
        popupCompiler.addActionListener(new MenuItemCompiler());
        popupRun.addActionListener(new MenuItemRun());
            //
        popupMenu.add(popupFermer);
        popupMenu.add(popupCompiler);
        popupMenu.add(popupRun);
        popupMenu.add(popupCreer);
        popupMenu.add(popupOuvrir);
        popupMenu.add(popupSave);
    }

    //mise en place des actions relatives a notre menu
    private class MenuItemCreer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button creer
            Frame.setOpenedFilePath(null);
            TextArea textArea = new TextArea("//ecrivez votre code ici");
            Frame.zoneDeSaisieScrollPane = new JScrollPane(textArea);
            Frame.onglets.add("unsaved",Frame.zoneDeSaisieScrollPane);
            Frame.textAreas.add(textArea);
        }
    }
    private class MenuItemOuvrir implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button ouvrir
            Frame frame = new Frame();
            JFileChooser fileChooser = new FileChooser(".", frame);
            frame.getContentPane().add(fileChooser);
            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        }
    }
    private class MenuItemSauvegarder implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            boolean unsaved = false;
            String currentOngletPath = Frame.onglets.getTitleAt(Frame.onglets.getSelectedIndex());
            if(currentOngletPath.equals("unsaved"))
            {
                Frame.setOpenedFilePath(null);
                unsaved = true;
            }
            else
            {
                Frame.setOpenedFilePath(currentOngletPath);
            }
            Frame.setZoneDeSaisie(Frame.textAreas.get(Frame.onglets.getSelectedIndex()));
            String pathname = "";
            if(Frame.getOpenedFilePath() == null)
                pathname = JOptionPane.showInputDialog(null,"Veuillez entrer entrer le nom du fichier", "Sauvegarde de fichier", JOptionPane.QUESTION_MESSAGE);
            else
                pathname = Frame.getOpenedFilePath();
            if(FileIO.write(pathname, Frame.getZoneDeSaisie().getText()+"\n"))
            {
                JOptionPane.showMessageDialog(null,"Opération effectuée avec succès", "Résultat Opération",JOptionPane.INFORMATION_MESSAGE);
                //Frame.setListFichier(new FileTree(new File(".")));
                //on enregistre le chemin du fichier, au cas ou nous voudrions faire une autre sauvegarde
                Frame.setOpenedFilePath(pathname);
                if(unsaved)
                    Frame.onglets.setTitleAt(Frame.onglets.getSelectedIndex(),pathname);
            }
            else
                JOptionPane.showMessageDialog(null,"Opération échoué", "Résultat Opération",JOptionPane.INFORMATION_MESSAGE);

        }
    }
    private class MenuItemCouper implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button couper
            Frame.getZoneDeSaisie().cut();
            /*Frame.setSelectionTextContent(Frame.getZoneDeSaisie().getSelectedText());
            System.out.println(Frame.getSelectionTextContent());*/
        }
    }
    private class MenuItemColler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button coller
            Frame.getZoneDeSaisie().paste();
        }
    }
    private class MenuItemCopier implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button copier
            Frame.getZoneDeSaisie().copy();
            /*Frame.setSelectionTextContent(Frame.getZoneDeSaisie().getSelectedText());
            System.out.println(Frame.getSelectionTextContent());*/
        }
    }
    private class MenuItemRun implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //a chaque nouveau clique on rafraichie le contenu de l'interpreteur
            Interpreteur.resultat = "";
            Interpreteur.textAreaResultat.setText("");
            //action a effectuer pour le button run
            Frame frame = new Frame();
            frame.setTitle("Interpreteur");
            //mise en place du contenu de la fenetre interpreteur
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(Interpreteur.textAreaResultat, BorderLayout.CENTER);
            frame.setContentPane(panel);
            frame.setVisible(true);
            //on appelle l'interpreteur
            //ne pas oublier de remplacer vicPathname et vdPathname par des variables concrete
            Interpreteur.interpreteur("VIC.txt","VD.txt");
        }
    }
    private class MenuItemCompiler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //action a effectuer pour le button compiler
            boolean unsaved = false;
            String currentOngletPath = Frame.onglets.getTitleAt(Frame.onglets.getSelectedIndex());
            if(currentOngletPath.equals("unsaved"))
            {
                Frame.setOpenedFilePath(null);
                unsaved = true;
            }
            else
            {
                Frame.setOpenedFilePath(currentOngletPath);
            }
            Frame.setZoneDeSaisie(Frame.textAreas.get(Frame.onglets.getSelectedIndex()));
            String pathname = "";
            //on exige que le fichier soit prealablement enregistrer
            if(Frame.getOpenedFilePath() == null)
            {
                //on provode l'action de sauvegarde
                pathname = JOptionPane.showInputDialog(null,"Veuillez entrer entrer le nom du fichier", "Sauvegarde de fichier", JOptionPane.QUESTION_MESSAGE);
                if(FileIO.write(pathname, Frame.getZoneDeSaisie().getText()+"\n"))
                {
                    Frame.setOpenedFilePath(pathname);
                    if(unsaved)
                        Frame.onglets.setTitleAt(Frame.onglets.getSelectedIndex(),pathname);
                }
                else
                    JOptionPane.showMessageDialog(null,"Opération échoué", "Résultat Opération",JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                //on sauvegarde le contenu du fichier avant de faire la compilation
                pathname = Frame.getOpenedFilePath();
                if(FileIO.write(pathname, Frame.getZoneDeSaisie().getText()+"\n"))
                {
                    Frame.setOpenedFilePath(pathname);
                }
                else
                    JOptionPane.showMessageDialog(null,"Opération échoué", "Résultat Opération",JOptionPane.INFORMATION_MESSAGE);

                //on accede au fichier source
                try( RandomAccessFile fichierSource = new RandomAccessFile(new File(Frame.getOpenedFilePath()),"r")) {
                    var al = new AnalyseurLexical(fichierSource);
                    var as = new AnalyseurSynthaxique();
                    try {
                        as.aSynthaxique();
                        Frame.getZoneDeStatus().setText("aucune erreur detectée");
                    } catch (GrammaireException e) {
                        Frame.getZoneDeStatus().setText(e.getMessage());
                    }
                    /*var categorieLexical = CategorieLexical.UNDEFINED;
                    var temp = "";
                    while(fichierSource.getFilePointer() != fichierSource.length()){
                        categorieLexical = AnalyseurLexical.uniteSuivante();
                        temp += AnalyseurLexical.uniteLexical + " : " + categorieLexical + "\n";
                        System.out.println(temp);
                    }
                    //1ere option: dans la fenetre ou on a ecris le code
                    //Frame.getZoneDeStatus().setText(temp);
                    //2eme option: dans une nouvelle frame
                    Frame frame = new Frame();
                    frame.setTitle("Compilation");
                    JScrollPane scrollPane = new JScrollPane(new TextArea(temp));
                    frame.setContentPane(scrollPane);
                    frame.setVisible(true);*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
