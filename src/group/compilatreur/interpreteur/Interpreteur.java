package group.compilatreur.interpreteur;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interpreteur {
    public static String resultat = "";
    public static JTextArea textAreaResultat = new JTextArea(Interpreteur.resultat);
    private static Scanner saisie = new Scanner(System.in);
    //////
    public static void interpreteur(String vicPathname, String vdPathname){
        //initialisation des variables
        List<InstructionCible> vic = init_vic(vicPathname);
        List<Donnee> vd = init_vd(vdPathname);
        Pile stack = new Pile();
        int co = 0;
        InstructionCible instr_cour;
        //varaible propres a notre programme
        //on  utilise le type float pour pouvoir utiliser la fonction de conversion automatique de java floatToInt
        //cela nous evite d'avoir d'avoir a coder manuelement DoubleToInt
        float operande1;
        float operande2 ;
        String tempString = "";
        int index = 0;
        //execution du processus principale
        while (!vic.get(co).oper.equals("end")){
            instr_cour = vic.get(co);
            co++;
            switch (instr_cour.oper.toUpperCase()){
                //instructions de chargement/dechargement au niveau de la memoire
                case "LOAD":
                    index = Integer.parseInt(instr_cour.operd);
                    stack.empiler(vd.get(index).valeur);
                    break;
                case "LOADC":
                    stack.empiler(instr_cour.operd);
                    break;
                case "STORE":
                    index = Integer.parseInt(instr_cour.operd);
                    vd.add(index, new Donnee(null, stack.depiler()));
                    break;
                //instructions arithmetique
                case "ADD":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    operande1 +=operande2;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "SUB":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    operande1 -=operande2;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "MUL":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    operande1 *=operande2;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "DIV":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    operande1 /=operande2;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "INC":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande1++;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "DEC":
                    //avec la supposition que toutes données sont des doubles
                    operande1 = Float.parseFloat(stack.depiler());
                    operande1--;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                //instructions de comparaisons
                //vu que les testes seront fait les mnemonique, toutes ces operation reviendrons a faire une
                //soustraction. Question: Faut ils tous les implementées toutes ?
                case "INF":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande1 - operande2 > 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                case "INFEGAL":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande1 - operande2 >= 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                case "SUP":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 - operande1 > 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                case "SUPEGAL":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 - operande1 >= 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                case "EGAL":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 - operande1 == 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                case "DIFF":
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 - operande1 != 0)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                //instructions logiques
                case "NOT": //on inverse le signe
                    operande1 = Float.parseFloat(stack.depiler());
                    operande1 = -operande1;
                    tempString = "" + operande1;
                    stack.empiler(tempString);
                    break;
                case "OR": // le ou logique revient a l'addition l'addition
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 == 0 && operande1 == 0)
                        stack.empiler("0");
                    else
                        stack.empiler("1");
                    break;
                case "AND": // le ou logique revient a la multiplication
                    operande1 = Float.parseFloat(stack.depiler());
                    operande2 = Float.parseFloat(stack.depiler());
                    if(operande2 == 1 && operande1 == 1)
                        stack.empiler("1");
                    else
                        stack.empiler("0");
                    break;
                //instructions de sauts conditionnels
                case "JZERO": // jump if zero <=> jump if egal
                    if(Float.parseFloat(stack.depiler()) == 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JNZ": // jump if not not zero  <=> jump if not egal
                    if(Float.parseFloat(stack.depiler()) != 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JG": // jump if greater
                    if(Float.parseFloat(stack.depiler()) > 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JNG": // jump if not greater
                    if(!(Float.parseFloat(stack.depiler()) > 0))
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JGE": // jump if greater or egal
                    if(Float.parseFloat(stack.depiler()) >= 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JNGE": // jump if not greater or egal
                    if(!(Float.parseFloat(stack.depiler()) >= 0))
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JL": // jump if less
                    if(Float.parseFloat(stack.depiler()) < 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JNL": // jump if not less
                    if(!(Float.parseFloat(stack.depiler()) < 0))
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JLE": // jump if not less or egal
                    if(Float.parseFloat(stack.depiler()) <= 0)
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JNLE": // jump if not less or egal
                    if(!(Float.parseFloat(stack.depiler()) <= 0))
                        co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                case "JUMP":
                    co = Integer.parseInt(instr_cour.operd) - 1;
                    break;
                //instructions de lecture et d'ecriture
                case "READ":
                    //je vais ajouter une petite modification en attendant de trouver une solution
                    //a l'execution de code dans la console
                    read(vd.get(Integer.parseInt((String)instr_cour.operd)).type,vd.get(Integer.parseInt((String)instr_cour.operd)));
                    break;
                case "WRITE":
                    //je vais ajouter une petite modification en attendant de trouver une solution
                    //a l'execution de code dans la console
                    Interpreteur.resultat += vd.get(Integer.parseInt(instr_cour.operd)).valeur + "\n";
                    Interpreteur.textAreaResultat.setText(Interpreteur.resultat);
                    //System.out.println(vd.get(Integer.parseInt((String)instr_cours.operd)).valeur);
                    break;
            }
        }
    }

    public static ArrayList<InstructionCible> init_vic(String pathname){
        //
        ArrayList<InstructionCible> vd = new ArrayList<>();
        InstructionCible instructionCible;
        BufferedReader inputStream = null;
        try {
            String line;
            inputStream = new BufferedReader(new FileReader(new File(pathname)));
            while ((line = inputStream.readLine()) != null){
                instructionCible = new InstructionCible();
                //on effectue le decoupage
                decoupeLineForVIC(line, instructionCible );
                vd.add(instructionCible);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return vd;
    }

    public static ArrayList<Donnee> init_vd(String pathname){
        //
        ArrayList<Donnee> vd = new ArrayList<>();
        Donnee donnee;
        BufferedReader inputStream = null;
        try{
            String line;
            inputStream = new BufferedReader(new FileReader(new File(pathname)));
            while ((line = inputStream.readLine()) != null){
                donnee = new Donnee();
                //on effectue le decoupage
                decoupeLineForVD(line, donnee);
                vd.add(donnee);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return vd;
    }

    public static void read(String type, Donnee var ) {
        String temp = JOptionPane.showInputDialog(null,"Saisissez une valeur", "Read", JOptionPane.QUESTION_MESSAGE);
        while(temp == null){
            temp = JOptionPane.showInputDialog(null,"Saisie incorrecte, veuillez recommencer", "Read", JOptionPane.QUESTION_MESSAGE);
        }
        var.valeur = temp;
        //var.valeur = saisie.nextLine();
        var.type = type;
    }

    public static void decoupeLineForVIC(String line, InstructionCible instructionCible){
        int i = 0;
        String oper = "";
        String operd = "";

        //on determine l'operateur dans la ligne
        while (i < line.length() && line.charAt(i) != '\n' && line.charAt(i) != ' '){
            oper += line.charAt(i);
            i++;
        }
        //on affecte cet operateur a donnee.oper;
        instructionCible.oper = oper;

        //on determine l'operande dans la ligne
        i++;
        while (i < line.length() && line.charAt(i) != '\n'){
            operd += line.charAt(i);
            i++;
        }
        instructionCible.operd = operd;
    }

    public static void decoupeLineForVD(String line, Donnee donnee){
        int i = 0;
        String type = "";
        String valeur = "";
        //on determine l'operateur dans la ligne
        while (i < line.length() && line.charAt(i) != '\n' && line.charAt(i) != ' '){
            type += line.charAt(i);
            i++;
        }
        //on affecte cet operateur a donnee.oper;
        donnee.type = type;

        //on determine l'operande dans la ligne
        i++;
        while (i < line.length() && line.charAt(i) != '\n'){
            valeur += line.charAt(i);
            i++;
        }
        donnee.valeur = valeur;
    }
}
