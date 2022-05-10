package group.compilatreur.interpreteur;

public class InstructionCible {
    public String oper;
    public String operd;

    public  InstructionCible(){
        this.operd = null;
        this.oper = null;
    }

    public InstructionCible(String oper, String operd ){
        this.operd = operd;
        this.oper = oper;
    }
}
