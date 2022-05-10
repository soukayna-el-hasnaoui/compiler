package group.env.file;

import java.io.*;

public class FileIO {
    private FileWriter fileWriter = null;
    private FileReader fileReader = null;

    //on defini deux methode
        //l'un pour la lecteur
        //l'autre pour l'ecriture
    public static String read(String pathname) {
        BufferedReader fileReader = null;
        String str = "";
        try {
             fileReader = new BufferedReader(new FileReader(new File(pathname)));
            int  i = 0;
            while((i = fileReader.read()) != -1)
                str += (char)i;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(fileReader != null)
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return str;
    }
    public static boolean write(String pathname, String content){
        BufferedWriter fileWriter = null;
        try {
            fileWriter = new BufferedWriter(new FileWriter(new File(pathname)));
            fileWriter.write(content);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            if(fileWriter != null)
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
