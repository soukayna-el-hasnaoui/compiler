import group.env.frames.FrameDeb;

import javax.swing.*;

public class Run {
    public static void main(String[] args) {
        //on applique le theme windows comme theme par defaut
        try {
            //UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //lancement de l'application
        new FrameDeb("Env");
        //new Frame("Env", 1000, 600, new Menu());
    }
}
