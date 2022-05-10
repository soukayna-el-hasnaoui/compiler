package group.env.frames;

import group.env.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import group.env.panel.Container;

//cette classe est celle qui sera appeler lorsque l'user cliquera sur le boutton de demarage
public class FrameDeb extends JFrame{
    private FrameDeb $this;
    public FrameDeb(String name)
    {
        this.setTitle(name);
        this.setSize(800, 550);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        //création de nos bouttons
        JButton creer = new JButton("Créer un nouveau fichier");
        creer.setPreferredSize(new Dimension(10,50));
        creer.setFocusable(false);
        JButton ouvrir = new JButton("Ouvrir un fichier");
        ouvrir.setPreferredSize(new Dimension(10,50));
        ouvrir.setFocusable(false);
        //ajout d'evenements
        creer.addActionListener(new ButtonCreerListener());
        ouvrir.addActionListener(new ButtonOuvrirListener());
        //
        //JPanel container = new JPanel(new BorderLayout(5,10)), container2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel container = new JPanel(new BorderLayout(5,10));
        Container container2 = new Container(new BorderLayout());
        container.add(creer, BorderLayout.NORTH);
        container.add(ouvrir, BorderLayout.SOUTH);
        container.setPreferredSize(new Dimension(500,110));
        container2.add(container, BorderLayout.CENTER);
        this.setContentPane(container2);
        this.pack();
        this.setVisible(true);
        $this = this;
    }

    private class ButtonCreerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("Oh vous voulez créer un nouveau fichier ? Très bien c'est partie !");
            new Frame("Env", 1000, 600, new Menu());
            $this.dispose();
        }
    }
    private class ButtonOuvrirListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("Oh vous voulez ouvrir un fichier existant? Très bien c'est partie !");
            JFrame frame = new Frame();
            JFileChooser fileChooser = new FileChooserDeb(".", frame, $this);
            frame.setSize(700,500);
            frame.setTitle("Env");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(fileChooser);
            frame.setVisible(true);
        }
    }
}