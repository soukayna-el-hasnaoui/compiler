package group.env.frames;

import group.env.file.FileIO;

import javax.swing.*;
import java.io.*;

public class FileChooser extends JFileChooser {
    private JFrame parent;
    public FileChooser(String s, JFrame parent){
        super(s);
        this.parent = parent;
    }

    @Override
    public void approveSelection() {
        super.approveSelection();
        //dans cette situation on doit creer une nouveau onglet pour chaque nouvau fichier qui est ouvert
        //dans un nouveau onglet
        File file = getSelectedFile();
        if(file != null) {
            System.out.println("Choix effectué avec succes !");
            System.out.println(file);
            this.parent.dispose();
            TextArea textArea = new TextArea(FileIO.read(file.toString()));
            Frame.zoneDeSaisieScrollPane = new JScrollPane(textArea);
            Frame.onglets.add(file.toString(),Frame.zoneDeSaisieScrollPane);
            Frame.textAreas.add(textArea);
            //Frame.getZoneDeSaisie().setText(FileIO.read(file.toString()));
            Frame.setOpenedFilePath(file.toString());
        }
    }

    @Override
    public void cancelSelection() {
        super.cancelSelection();
        this.parent.dispose();
        System.out.println("Fermeture effectuée avec succes !");
        this.parent.dispose();
    }
}
