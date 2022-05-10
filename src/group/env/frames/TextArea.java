package group.env.frames;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TextArea extends JTextArea {
    public TextArea(String content){
        super(content);
        Font myFont = null;
        try{
            myFont = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/Ubuntu/Ubuntu-Regular.ttf"));
            myFont = myFont.deriveFont(15f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(myFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setSelectedTextColor(Color.WHITE);
        this.setSelectionColor(Color.GRAY);
        this.setMargin(new Insets(4,4,4,4));
        if(myFont != null)
            this.setFont(myFont);
    }
}
