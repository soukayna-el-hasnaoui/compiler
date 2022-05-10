package group.compilatreur.al;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AnalyseurLexical {
    //contient le dernier lexeme qui vient d'etre analyser
    public static String uniteLexical = "";
    public static int lineNumber = 1;
    //contient la liste des mots-clés de notre langage
    private static Map<String, CategorieLexical> mots_cles;
    //la variable contenatn le fichier source est static et public pour que toutes les classes puissent y acceder
    //on exige que le fichier soit enregistrer pour faire la compilation
    private static RandomAccessFile fichierSource = null;
    //constructeur par defaut
    public AnalyseurLexical(RandomAccessFile fichierSource){
        //initialisation du fichier source
        AnalyseurLexical.fichierSource = fichierSource;
        mots_cles = new HashMap<>();
        //on remplie cette table manuellement
        mots_cles = new HashMap<>();
        mots_cles.put("int", CategorieLexical.INT);
        mots_cles.put("float", CategorieLexical.FLOAT);
        mots_cles.put("while", CategorieLexical.TANTQUE);
        mots_cles.put("if", CategorieLexical.SI);
        mots_cles.put("else if", CategorieLexical.SINONSI);
        mots_cles.put("else", CategorieLexical.SINON);
        mots_cles.put("do", CategorieLexical.REPETER);
        mots_cles.put("const", CategorieLexical.CONSTANTE);
        mots_cles.put("boolean", CategorieLexical.BOOLEAN);
        mots_cles.put("print", CategorieLexical.PRINT);
        mots_cles.put("scan", CategorieLexical.SCAN);
        mots_cles.put("string", CategorieLexical.STRING);
        mots_cles.put("begin", CategorieLexical.BEGIN);
        mots_cles.put("end", CategorieLexical.END);
    }

    public static CategorieLexical uniteSuivante(){
        try {
            //nous permettra de faire la concatenation des unités lexicales
            String temp = "";
            char c = 0;
            c = readChar(AnalyseurLexical.fichierSource);
            while(c == ' ' || c == '\r' || c ==  '\n' || c == '\t'){
                if(c == '\r' || c ==  '\n')
                    lineNumber++;
                c = readChar(AnalyseurLexical.fichierSource);
            }
            switch (c) {
                case '+':
                    temp = "";
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '+') {
                        AnalyseurLexical.uniteLexical = "++";
                        return CategorieLexical.INC;
                    }
                    else if(estChiffre(c))
                    {
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                        while(estChiffre(c)){
                            temp += c;
                            c = readChar(AnalyseurLexical.fichierSource);
                        }
                        if(c == ',')
                        {
                            temp += c;
                            c = readChar(AnalyseurLexical.fichierSource);
                            if(!estChiffre(c))
                                return CategorieLexical.UNDEFINED;
                            while(estChiffre(c)){
                                temp += c;
                                c = readChar(AnalyseurLexical.fichierSource);
                            }
                            delire(AnalyseurLexical.fichierSource);
                            AnalyseurLexical.uniteLexical = temp;
                            return CategorieLexical.REEL;
                        }
                        delire(AnalyseurLexical.fichierSource);
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.ENTIER;
                    }
                    else
                    {
                        delire(AnalyseurLexical.fichierSource);
                        AnalyseurLexical.uniteLexical = "+";
                        return CategorieLexical.ADD;
                    }
                    //
                case '-':
                    temp = "";
                    temp += c;
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '-'){
                        AnalyseurLexical.uniteLexical = "--";
                        return CategorieLexical.DEC;
                    }
                    else if(estChiffre(c))
                    {
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                        while(estChiffre(c)){
                            temp += c;
                            c = readChar(AnalyseurLexical.fichierSource);
                        }
                        if(c == ',')
                        {
                            temp += c;
                            c = readChar(AnalyseurLexical.fichierSource);
                            if(!estChiffre(c))
                                return CategorieLexical.UNDEFINED;
                            while(estChiffre(c)){
                                temp += c;
                                c = readChar(AnalyseurLexical.fichierSource);
                            }
                            delire(AnalyseurLexical.fichierSource);
                            AnalyseurLexical.uniteLexical = temp;
                            return CategorieLexical.REEL;
                        }
                        delire(AnalyseurLexical.fichierSource);
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.ENTIER;
                    }
                    else
                    {
                        delire(AnalyseurLexical.fichierSource);
                        AnalyseurLexical.uniteLexical = "-";
                        return CategorieLexical.SUB;
                    }
                    //
                case '=':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '='){
                        AnalyseurLexical.uniteLexical = "==";
                        return CategorieLexical.EGAL;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "=";
                    return CategorieLexical.AFFECT;
                //
                case '/':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '/'){
                        temp = "//";
                        c = readChar(AnalyseurLexical.fichierSource);
                        while(c != '\n' && c != '\r')
                        {
                            temp+=c;
                            c = readChar(AnalyseurLexical.fichierSource);
                        }
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.CMTLIGNE;
                    }
                    if(c ==  '*'){
                        temp = "/*";
                        c = readChar(AnalyseurLexical.fichierSource);
                        char cnext = readChar(fichierSource);
                        String condition = "" + c + cnext;
                        while(c != '*' || cnext != '/')
                        {
                            temp+=condition;
                            c = cnext;
                            cnext = readChar(fichierSource);
                            condition = "" + cnext;
                        }
                        temp += '/';
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.CMLMULTILIGNE;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "/";
                    return CategorieLexical.DIV;
                //
                case '*':
                    AnalyseurLexical.uniteLexical = "*";
                    return CategorieLexical.MUL;
                //
                case '<':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '='){
                        AnalyseurLexical.uniteLexical = "<=";
                        return CategorieLexical.INFEGAL;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "<";
                    return CategorieLexical.INF;
                //
                case '>':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '='){
                        AnalyseurLexical.uniteLexical = ">=";
                        return CategorieLexical.SUPEGAL;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = ">";
                    return CategorieLexical.SUP;
                //
                case '!':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '='){
                        AnalyseurLexical.uniteLexical = "!=";
                        return CategorieLexical.DIFF;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "!";
                    return CategorieLexical.NOT;
                case '&':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '&'){
                        AnalyseurLexical.uniteLexical = "&&";
                        return CategorieLexical.AND;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "&";
                    return CategorieLexical.UNDEFINED;
                case '|':
                    c = readChar(AnalyseurLexical.fichierSource);
                    if(c ==  '|'){
                        AnalyseurLexical.uniteLexical = "||";
                        return CategorieLexical.OR;
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = "|";
                    return CategorieLexical.UNDEFINED;
                case '{':
                    AnalyseurLexical.uniteLexical = "{";
                    return CategorieLexical.ACOLADOUV;
                case '}':
                    AnalyseurLexical.uniteLexical = "}";
                    return CategorieLexical.ACOLADFERM;
                case '(':
                    AnalyseurLexical.uniteLexical = "(";
                    return CategorieLexical.PARENTOUV;
                case ')':
                    AnalyseurLexical.uniteLexical = ")";
                    return CategorieLexical.PARENTHFERM;
                case ';':
                    AnalyseurLexical.uniteLexical = ";";
                    return CategorieLexical.ENDLINE;
                case ',':
                    AnalyseurLexical.uniteLexical = ",";
                    return CategorieLexical.VIRGULE;
                case '_':
                    temp += "_";
                    //lorsqu'on trouve plusieus underscore, on lit jusqu'au dernier charactere avant d'avoir undefined
                    c = readChar(AnalyseurLexical.fichierSource);
                    while(c != ' ' && c != '\r' && c != '\n' && c != '\t' && (estChiffre(c) || estLettre(c) || c == '_') ){
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                    }
                    delire(AnalyseurLexical.fichierSource);
                    AnalyseurLexical.uniteLexical = temp;
                    return CategorieLexical.UNDEFINED;
                case '"' :
                    temp += "\"";
                    c = readChar(AnalyseurLexical.fichierSource);
                    while (c != '"' && fichierSource.getFilePointer() < fichierSource.length()){
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                    }
                    if(fichierSource.getFilePointer() < fichierSource.length())
                    {
                        temp += "\"";
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.TEXT;
                    }
                    temp+=c;
                    AnalyseurLexical.uniteLexical = temp;
                    return CategorieLexical.UNDEFINED;

                default:
                    if(estChiffre(c))
                    {
                        temp = "";
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                        while(estChiffre(c)){
                            temp += c;
                            c = readChar(AnalyseurLexical.fichierSource);
                        }
                        if(c == ',')
                        {
                            c = readChar(AnalyseurLexical.fichierSource);
                            if(!estChiffre(c))
                            {
                                delire(AnalyseurLexical.fichierSource);
                                delire(AnalyseurLexical.fichierSource);
                                AnalyseurLexical.uniteLexical = temp;
                                //on ne retoune pas undefined mais, mais nombre
                                return CategorieLexical.ENTIER;
                            }

                            temp += ",";

                            while(estChiffre(c)){
                                temp += c;
                                c = readChar(AnalyseurLexical.fichierSource);
                            }
                            delire(AnalyseurLexical.fichierSource);
                            AnalyseurLexical.uniteLexical = temp;
                            return CategorieLexical.REEL;
                        }
                        delire(AnalyseurLexical.fichierSource);
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.ENTIER;
                    }
                    if(estLettre(c)) {
                        temp = "";
                        temp += c;
                        c = readChar(AnalyseurLexical.fichierSource);
                        boolean undo =  false;
                        while((estLettre(c)||estChiffre(c)) || c == '_') {
                            temp += c;
                            if(c == '_') {
                                c = readChar(AnalyseurLexical.fichierSource);
                                if(c == '_'){
                                    undo = true;
                                }
                                else undo = false;
                                if(undo == true){
                                    temp += "_";
                                    //lorsqu'on trouve plusieus underscore, on lit jusqu'au dernier charactere avant d'avoir undefined
                                    c = readChar(AnalyseurLexical.fichierSource);
                                    while(c != ' ' && c != '\r' && c != '\n' && c != '\t' && (estChiffre(c) || estLettre(c) || c == '_') ){
                                        temp += c;
                                        c = readChar(AnalyseurLexical.fichierSource);
                                    }
                                    delire(AnalyseurLexical.fichierSource);
                                    AnalyseurLexical.uniteLexical = temp;
                                    return CategorieLexical.UNDEFINED;
                                }
                                if(!estLettre(c) && !estChiffre(c))
                                {
                                    delire(AnalyseurLexical.fichierSource);
                                    AnalyseurLexical.uniteLexical = temp;
                                    return CategorieLexical.UNDEFINED;
                                }
                                temp += c;
                            }
                            c = readChar(AnalyseurLexical.fichierSource);
                        }
                        delire(AnalyseurLexical.fichierSource);
                        Set<String> keys = mots_cles.keySet();
                        for (String s : keys) {
                            if(s.equals(temp)){
                                AnalyseurLexical.uniteLexical = temp;
                                return mots_cles.get(temp);
                            }
                        }
                        AnalyseurLexical.uniteLexical = temp;
                        return CategorieLexical.IDENT;
                    }
                    AnalyseurLexical.uniteLexical = "" + c;
                    return CategorieLexical.UNDEFINED;
            }
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return CategorieLexical.UNDEFINED;
    }

    private static boolean estChiffre(char c) {
        if(c >= '0' && c <= '9')
            return true;
        return false;
    }

    private static boolean estLettre(char c) {
        if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
            return true;
        return false;
    }

    private static char readChar(RandomAccessFile file) {
        int c = 0;
        try {
            c = file.read();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return (char)c;
    }

    private static void delire(RandomAccessFile fichier)
    {
        try
        {
            if(fichier.getFilePointer() < fichier.length())
                fichier.seek(fichier.getFilePointer() - 1);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static boolean openFichierSource(String pathname, String mode){
        //ouverture du fichier source, l'endroite de cette ouverture devra etre changer plus tard
        try {
            fichierSource = new RandomAccessFile(new File(pathname), mode);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
