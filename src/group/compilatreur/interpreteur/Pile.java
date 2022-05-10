package group.compilatreur.interpreteur;

import java.util.ArrayList;

public class Pile {
    ArrayList<String> pile= new ArrayList<>();
    int s=-1;

    public void empiler(String d) {
        pile.add(d);
        s++;
    }
    public String depiler() {
        String d=pile.get(s);
        pile.remove(s);
        s--;
        return d;
    }
}
