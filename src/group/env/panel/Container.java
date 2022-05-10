package group.env.panel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Container extends JPanel {

    public Container(BorderLayout borderLayout)
    {
        super(borderLayout);
        //on change la police de notre frame
        Font myFont = null;
        try{
            myFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/Ubuntu/Ubuntu-Regular.ttf"));
            myFont = myFont.deriveFont(13f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(myFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(myFont != null)
            this.setFont(myFont);
    }
    public Container(FlowLayout flowLayout)
    {
        super(flowLayout);
    }
    //redefinition de methode
    public Insets getInsets() {
        Insets normal = super.getInsets();
        return new Insets(normal.top + 180, normal.left + 100,
                                 normal.bottom + 250, normal.right + 100);
    }
}
