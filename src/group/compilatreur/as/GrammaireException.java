package group.compilatreur.as;

public class GrammaireException extends Exception {
    public GrammaireException(String errorType){
        super(errorType);
    }
}
