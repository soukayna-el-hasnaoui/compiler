package group.compilatreur.as;

import group.compilatreur.al.*;
import group.compilatreur.interpreteur.Donnee;
import group.compilatreur.interpreteur.InstructionCible;
import group.env.file.FileIO;
import java.util.ArrayList;
import java.util.List;

public class AnalyseurSynthaxique{
    //declaration des variables : a syntaxique et a semantique
    private static CategorieLexical uniteCourante = CategorieLexical.UNDEFINED;
    private static List<DescVariable> listIdents = new ArrayList<>();
    private static CategorieLexical type;
        //cette variable contient la valeur de la dernière unité qui a été lu
    private static String lexeme;
    private static List<DescVariable> tableDesSymboles = new ArrayList<>();
    //declaration des variables : generation du code cible
    private static CategorieLexical operateur;
    private static DescVariable descVariable = null;
    private static List<InstructionCible> vic = new ArrayList<>();
    private static List<Donnee> vd = new ArrayList<>();
    //utilisation de la premiere solution proposer pour la gestion des etiquettes
    private static List<Integer> tabEtiquettes = new ArrayList<>();
    private static int nbrInstructionCible;

    //methodes relatives a l'a-syntaxique et l'a-semantique
    private boolean opA(){
        return uniteCourante == CategorieLexical.ADD || uniteCourante == CategorieLexical.SUB
                || uniteCourante == CategorieLexical.MUL || uniteCourante == CategorieLexical.DIV;
    }

    private boolean opC(){
        if(uniteCourante == CategorieLexical.EGAL)
            return true;
        else if(uniteCourante == CategorieLexical.SUP)
            return true;
        else if(uniteCourante == CategorieLexical.SUPEGAL)
            return true;
        else if(uniteCourante == CategorieLexical.INF)
            return true;
        else if(uniteCourante == CategorieLexical.INFEGAL)
            return true;
        else if(uniteCourante == CategorieLexical.DIFF)
            return true;
        else
            return false;
    }

    private boolean opL(){
        if(uniteCourante == CategorieLexical.AND)
            return true;
        else if(uniteCourante == CategorieLexical.OR)
            return true;
        else if(uniteCourante == CategorieLexical.NOT)
            return true;
        else return uniteCourante == CategorieLexical.DIFF;
    }

    private boolean reconnaitreTerminal (CategorieLexical uniteVoulue){
        return uniteCourante == uniteVoulue;
    }

    private boolean sexpr() throws GrammaireException{
        lexeme = AnalyseurLexical.uniteLexical;
        if(uniteCourante == CategorieLexical.REEL)
        {
            genererInstr("loadc", lexeme);
            return true;
        }
        if(uniteCourante == CategorieLexical.ENTIER)
        {
            genererInstr("loadc", lexeme);
            return true;
        }
        if(uniteCourante == CategorieLexical.IDENT)
        {
            //on verifie que la variable a déjà été déclarer
            descVariable = contains(lexeme, tableDesSymboles);
            if(descVariable != null)
            {
                if(descVariable.valeur == null)
                    throw new GrammaireException("la variable: \'" + lexeme  +  "\' doit être initialiser. Ligne: " + AnalyseurLexical.lineNumber);
                genererInstr("load",descVariable.adresse);
                return true;
            }
            throw new GrammaireException("la variable: \'" + lexeme  +  "\' doit être déclarer. Ligne: " + AnalyseurLexical.lineNumber);
        }
        return false;
    }

    private boolean exprA() throws GrammaireException{
        if(sexpr())
        {
            uniteCourante = AnalyseurLexical.uniteSuivante();
            while (opA()){
                operateur = uniteCourante;
                uniteCourante = AnalyseurLexical.uniteSuivante();
                if(!sexpr())
                    return false;
                else{
                    genererInstr(opAToString(operateur), "");
                    uniteCourante = AnalyseurLexical.uniteSuivante();
                }
            }
            return true;
        }
        return false;
    }

    private boolean instrRead() throws GrammaireException{
        if(reconnaitreTerminal(CategorieLexical.SCAN))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction de lecture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);;
        if(reconnaitreTerminal(CategorieLexical.PARENTOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction de lecture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.IDENT))
        {
            lexeme = AnalyseurLexical.uniteLexical;
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        else throw new GrammaireException("instruction de lecture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.PARENTHFERM))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction de lecture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.ENDLINE))
        {
            //on verifie que la variable a déjà été déclarer
            descVariable = contains(lexeme, tableDesSymboles);
            if(descVariable != null)
            {
                descVariable.valeur = "0";
                genererInstr("read",descVariable.adresse);
                return true;
            }
            throw new GrammaireException("la variable: \'" + lexeme  +  "\' doit être déclarer. Ligne: " + AnalyseurLexical.lineNumber);
        }
        throw new GrammaireException("instruction de lecture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
    }

    private boolean instWrite() throws GrammaireException{
        if(reconnaitreTerminal(CategorieLexical.PRINT))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'ecriture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.PARENTOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'ecriture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.IDENT))
        {
            lexeme = AnalyseurLexical.uniteLexical;
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        if(reconnaitreTerminal(CategorieLexical.PARENTHFERM))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'ecriture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.ENDLINE))
        {
            //on verifie que la variable a déjà été déclarer
            descVariable = contains(lexeme, tableDesSymboles);
            if(descVariable != null)
            {
                genererInstr("write",descVariable.adresse);
                return true;
            }
            throw new GrammaireException("la variable: \'" + lexeme  +  "\' doit être déclarer. Ligne: " + AnalyseurLexical.lineNumber);
        }
        throw new GrammaireException("instruction d'ecriture incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
    }

    private boolean declVar() throws GrammaireException {
        if (reconnaitreTerminal(CategorieLexical.FLOAT) || reconnaitreTerminal(CategorieLexical.INT))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.IDENT)){
            listIdents.add(new DescVariable("", AnalyseurLexical.uniteLexical, "", null,""));
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        else throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.AFFECT)){
            listIdents.get(listIdents.size()-1).valeur = "0";
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(!exprA())
                throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
            else genererInstr("store", listIdents.size() + tableDesSymboles.size() + "");
        }
        while(reconnaitreTerminal(CategorieLexical.VIRGULE))
        {
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(reconnaitreTerminal(CategorieLexical.IDENT)){
                listIdents.add(new DescVariable("", AnalyseurLexical.uniteLexical, "", null,""));
                uniteCourante = AnalyseurLexical.uniteSuivante();
            }
            else throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
            if(reconnaitreTerminal(CategorieLexical.AFFECT)){
                listIdents.get(listIdents.size()-1).valeur = "0";
                uniteCourante = AnalyseurLexical.uniteSuivante();
                if(!exprA())
                    throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
                else genererInstr("store", listIdents.size() + tableDesSymboles.size() + "");
            }
        }
        if(reconnaitreTerminal(CategorieLexical.ENDLINE))
            return true;
        throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
    }

    private boolean declConst() throws GrammaireException{
        //au niveau syntaxique on le choix:
        //une constante est initialiser obligatoirement lors de ca declaration:
        //les constante seront traiter comme des variable normales en gardant biensur les valeur en memoire
        //peut etre initialiser ulterieurement mais une seule fois
        //nous oblige a avoir une table des constante pour se souvenir qu'on a effectivement une constante
        if(reconnaitreTerminal(CategorieLexical.CONSTANTE))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else
            throw new GrammaireException("instruction de declaration de constante incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if (reconnaitreTerminal(CategorieLexical.FLOAT) || reconnaitreTerminal(CategorieLexical.INT))
        {
            type = uniteCourante;
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        else throw new GrammaireException("instruction de declaration de constante incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.IDENT)){
            listIdents.add(new DescVariable("", AnalyseurLexical.uniteLexical, "", null,""));
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        else throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.AFFECT)){
            listIdents.get(listIdents.size()-1).valeur = "0";
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(!exprA())
                throw new GrammaireException("instruction de declaration de constante incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
            else genererInstr("store", listIdents.size() + tableDesSymboles.size() + "");
        }
        else throw new GrammaireException("instruction de declaration de constante incorrecte, affectation manquante. Ligne: " + AnalyseurLexical.lineNumber);
        while(reconnaitreTerminal(CategorieLexical.VIRGULE))
        {
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(reconnaitreTerminal(CategorieLexical.IDENT)){
                listIdents.add(new DescVariable("", AnalyseurLexical.uniteLexical, "", null,""));
                uniteCourante = AnalyseurLexical.uniteSuivante();
            }
            else throw new GrammaireException("instruction de declaration incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
            if(reconnaitreTerminal(CategorieLexical.AFFECT)){
                listIdents.get(listIdents.size()-1).valeur = "0";
                uniteCourante = AnalyseurLexical.uniteSuivante();
                if(!exprA())
                    throw new GrammaireException("instruction de declaration de constante incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
                else genererInstr("store", listIdents.size() + tableDesSymboles.size() + "");
            }
            else throw new GrammaireException("instruction de declaration de constante incorrecte, affectation manquante. Ligne: " + AnalyseurLexical.lineNumber);
        }
        if(reconnaitreTerminal(CategorieLexical.ENDLINE))
            return true;
        throw new GrammaireException("instruction de declaration de constante incorrecte. Ligne: " + AnalyseurLexical.lineNumber);
    }

    private boolean contition() throws GrammaireException {
        CategorieLexical opL, opL1;
        if(!exprA())
            throw new GrammaireException("condition non valide . Ligne: " + AnalyseurLexical.lineNumber);
        if(opC())
        {
            opL1 = uniteCourante;
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        else throw new GrammaireException("condition non valide . Ligne: " + AnalyseurLexical.lineNumber);
        if(!exprA())
            throw new GrammaireException("condition non valide . Ligne: " + AnalyseurLexical.lineNumber);
        genererInstr(opCToString(opL1), "");
        if(opL()){
            opL = uniteCourante;
            uniteCourante = AnalyseurLexical.uniteSuivante();
            contition();
            genererInstr(opLToString(opL), "");
        }
        return true;
    }

    private boolean instructionIf() throws GrammaireException{
        if(reconnaitreTerminal(CategorieLexical.SI))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.PARENTOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        contition();
        if(reconnaitreTerminal(CategorieLexical.PARENTHFERM))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        genererInstr("jzero", "");
        if(reconnaitreTerminal(CategorieLexical.ACOLADOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        while(uniteCourante != CategorieLexical.ACOLADFERM){
            instruction();
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        if(reconnaitreTerminal(CategorieLexical.ACOLADFERM))
        {
            uniteCourante = AnalyseurLexical.uniteSuivante();
            //on verifie s'il y'a une partie else
            if(reconnaitreTerminal(CategorieLexical.SINON))
            {
                genererInstr("jump", "");
                tabEtiquettes.add(nbrInstructionCible);
                uniteCourante = AnalyseurLexical.uniteSuivante();
                if(reconnaitreTerminal(CategorieLexical.SI)){
                    instructionIf();
                }
                else
                {
                    if(reconnaitreTerminal(CategorieLexical.ACOLADOUV))
                        uniteCourante = AnalyseurLexical.uniteSuivante();
                    else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
                    while(uniteCourante != CategorieLexical.ACOLADFERM){
                        instruction();
                        uniteCourante = AnalyseurLexical.uniteSuivante();
                    }
                    if(reconnaitreTerminal(CategorieLexical.ACOLADFERM))
                    {
                        tabEtiquettes.add(nbrInstructionCible);
                        return true;
                    }
                    else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
                }
            }
            tabEtiquettes.add(nbrInstructionCible);
            return true;
        }
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
    }

    /*private boolean partElse() throws GrammaireException{
        if(reconnaitreTerminal(CategorieLexical.SINON))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.SI)){
            instructionIf();
        }
        else {
            if(reconnaitreTerminal(CategorieLexical.ACOLADOUV))
                uniteCourante = AnalyseurLexical.uniteSuivante();
            else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
            while(uniteCourante != CategorieLexical.ACOLADFERM){
                instruction();
                uniteCourante = AnalyseurLexical.uniteSuivante();
            }
            if(reconnaitreTerminal(CategorieLexical.ACOLADFERM))
                return true;
            else throw new GrammaireException("instruction d'alternative non valide . Ligne: " + AnalyseurLexical.lineNumber);
        }
        return true;
    }
     */

    private boolean instructionWhile() throws GrammaireException{
        if(reconnaitreTerminal(CategorieLexical.TANTQUE))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction repetitive non valide . Ligne: " + AnalyseurLexical.lineNumber);
        if(reconnaitreTerminal(CategorieLexical.PARENTOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction repetitive non valide . Ligne: " + AnalyseurLexical.lineNumber);
        int temp = nbrInstructionCible;
        contition();
        if(reconnaitreTerminal(CategorieLexical.PARENTHFERM))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction repetitive non valide . Ligne: " + AnalyseurLexical.lineNumber);
        genererInstr("jzero", "");
        if(reconnaitreTerminal(CategorieLexical.ACOLADOUV))
            uniteCourante = AnalyseurLexical.uniteSuivante();
        else throw new GrammaireException("instruction repetitive non valide . Ligne: " + AnalyseurLexical.lineNumber);
        while(uniteCourante != CategorieLexical.ACOLADFERM){
            instruction();
            uniteCourante = AnalyseurLexical.uniteSuivante();
        }
        if(reconnaitreTerminal(CategorieLexical.ACOLADFERM))
        {
            genererInstr("jump", "");
            tabEtiquettes.add(nbrInstructionCible);
            tabEtiquettes.add(temp);
            return true;
        }
        else throw new GrammaireException("instruction repetitive non valide . Ligne: " + AnalyseurLexical.lineNumber);
    }

    private void instruction() throws GrammaireException{

        if(uniteCourante == CategorieLexical.INT || uniteCourante == CategorieLexical.FLOAT)
        {
            type = uniteCourante;
            declVar();
            int i = 0;
            int size = tableDesSymboles.size();
            for(DescVariable temp : listIdents)
            {
                i++;
                if(contains(temp.ident, tableDesSymboles) == null)
                {
                    descVariable = new DescVariable(type+"", temp.ident, i+size+"",temp.valeur, null);
                    tableDesSymboles.add(descVariable);
                }
                else{

                    listIdents.clear();
                    throw new GrammaireException("variable déjà declaree . Ligne: " + AnalyseurLexical.lineNumber);
                }
            }
            listIdents.clear();
        }

        else if(uniteCourante == CategorieLexical.CONSTANTE)
        {
            declConst();
            int i = 0;
            for(DescVariable temp : listIdents)
            {
                i++;
                if(contains(temp.ident, tableDesSymboles) == null)
                {
                    descVariable = new DescVariable(type+"", temp.ident, i+tableDesSymboles.size()+"",temp.valeur, "const");
                    tableDesSymboles.add(descVariable);
                }
                else{
                    listIdents.clear();
                    throw new GrammaireException("constante deja declarée . Ligne: " + AnalyseurLexical.lineNumber);
                }
            }
            listIdents.clear();
        }

        else if (uniteCourante == CategorieLexical.CMTLIGNE || uniteCourante == CategorieLexical.CMLMULTILIGNE) {}

        else if(uniteCourante == CategorieLexical.SCAN)
        {
            instrRead();
        }

        else if((uniteCourante == CategorieLexical.PRINT))
        {
            instWrite();
        }

        else if(uniteCourante == CategorieLexical.SI)
        {
            instructionIf();
        }

        else if(uniteCourante == CategorieLexical.TANTQUE)
        {
            instructionWhile();
        }

        else if(uniteCourante == CategorieLexical.IDENT)
        {
            lexeme = AnalyseurLexical.uniteLexical;
            descVariable = contains(lexeme, tableDesSymboles);
            if(descVariable == null)
                throw new GrammaireException("la variable: \'" + lexeme  +  "\' doit être declarer. Ligne: " + AnalyseurLexical.lineNumber);
            if(descVariable.nature != null)
                throw new GrammaireException("la variable: \'" + lexeme  +  "\' est une constante, affectation non possible. Ligne: " + AnalyseurLexical.lineNumber);
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(!reconnaitreTerminal(CategorieLexical.AFFECT))
                throw new GrammaireException("instruction d'affectation non valide . Ligne: " + AnalyseurLexical.lineNumber);
            uniteCourante = AnalyseurLexical.uniteSuivante();
            if(exprA())
            {
                System.out.println(uniteCourante);
                if(!reconnaitreTerminal(CategorieLexical.ENDLINE))
                    throw new GrammaireException("instruction d'affectation non valide . Ligne: " + AnalyseurLexical.lineNumber);
                descVariable.valeur = "0";
                genererInstr("store", descVariable.adresse );
            }
            else throw new GrammaireException("instruction d'affectation non valide . Ligne: " + AnalyseurLexical.lineNumber);
        }

        else if (uniteCourante == CategorieLexical.CMTLIGNE || uniteCourante == CategorieLexical.CMLMULTILIGNE){

        }

        else
        {
            throw new GrammaireException("erreur synthaxique a la ligne: " + AnalyseurLexical.lineNumber);
        }
    }

    public void aSynthaxique () throws GrammaireException{
        AnalyseurLexical.lineNumber = 1;
        nbrInstructionCible = 0;
        tableDesSymboles.clear();
        listIdents.clear();
        vd.clear();
        vic.clear();
        tabEtiquettes.clear();
        //on commence par faire une premiere lecture pour initialiser la sequence
        uniteCourante = AnalyseurLexical.uniteSuivante();

        //on lance la procedure d'analyse
        if(uniteCourante == CategorieLexical.BEGIN)
        {
            uniteCourante = AnalyseurLexical.uniteSuivante();
            while (uniteCourante != CategorieLexical.END)
            {
                this.instruction();
                uniteCourante = AnalyseurLexical.uniteSuivante();
            }
            //apres l'analyse il faut remplacer les etiquettes par leurs adresse reelle
            int temp;
            int j = 0;
            for(int i = 0; i < vic.size(); i++)
            {
                if(vic.get(i).oper.equalsIgnoreCase("jzero") || vic.get(i).oper.equalsIgnoreCase("jump") )
                {
                    temp = tabEtiquettes.get(j) + 1;
                    vic.get(i).operd = "" + temp;
                    j++;
                }
            }
            //on enregistre le code cible dans un fichier
            String str = "";
            for (InstructionCible cible : vic)
                 str += cible.oper.toUpperCase() + " " + cible.operd.toUpperCase() + "\n";
            str += "end\n";
            FileIO.write("VIC.txt", str);
            //on genere la vd
            str = "";
            for(DescVariable descVariable: tableDesSymboles)
                str += descVariable.type + " 0" + "\n";
            FileIO.write("VD.txt", str);
        }
        else throw new GrammaireException("erreur synthaxique le programme doit commencer par \"begin\"");
    }

    //methodes relatives a la generation de code
    private boolean genererInstr(String oper, String operd){
        ++nbrInstructionCible;
        return vic.add(new InstructionCible(oper, operd));
    }

    private String opAToString(CategorieLexical lexical){
        return lexical + "";
    }

    private String opCToString(CategorieLexical lexical){
        return lexical + "";
    }

    private String opLToString(CategorieLexical lexical){
        return lexical + "";
    }

    public DescVariable contains (String lexeme, List<DescVariable> tableDesSymboles){
        for(DescVariable descVariable: tableDesSymboles)
        {
            if(descVariable.ident.equals(lexeme))
            {
                return descVariable;
            }
        }
        return null;
    }
}