package group.compilatreur.interpreteur;

public class Donnee {
    String type;
    String valeur;

    public Donnee() {
        this.type="";
        this.valeur=null;
    }

    public Donnee(String type,String valeur) {
        this.type=type;
        this.valeur=valeur;
    }

    @Override
    public String toString() {
        return "Donnee{" +
                "type='" + type + '\'' +
                ", valeur=" + valeur +
                '}';
    }
}
