package group.compilatreur.as;

import java.util.ArrayList;
import java.util.List;

public class DescVariable {
    String type;
    String ident;
    String adresse;
    String valeur;
    String nature;

    public DescVariable(String type, String ident, String adresse, String valeur, String nature) {
        this.type = type;
        this.ident = ident;
        this.adresse = adresse;
        this.valeur = valeur;
        this.nature = nature;
    }
    public DescVariable(){}

}
