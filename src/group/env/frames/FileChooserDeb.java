package group.env.frames;

import group.env.file.FileIO;
import group.env.menu.Menu;

import javax.swing.*;
import java.io.File;

public class FileChooserDeb extends JFileChooser {
    private JFrame parent;
    private JFrame parentSuper;
    public FileChooserDeb(String s, JFrame parent){
        super(s);
        this.parent = parent;
    }
    public FileChooserDeb(String s, JFrame parent, JFrame parentSuper){
        super(s);
        this.parent = parent;
        this.parentSuper = parentSuper;
    }

    @Override
    public void approveSelection() {
        super.approveSelection();

        File file = getSelectedFile();
        if(file != null) {
            System.out.println("Choix effectué avec succes !");
            System.out.println(file);
            this.parent.dispose();
            this.parentSuper.dispose();
            Frame.setOpenedFilePath(file.toString());
            new Frame("Env", 1000, 600, new Menu(), FileIO.read(file.toString()));
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
