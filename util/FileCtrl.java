package enshuReport2_2023.util;

import enshuReport2_2023.Figure;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static enshuReport2_2023.Paint.*;

public class FileCtrl {

    public static boolean fileSave(){

        if( loadingFile != null ) {
            File file = new File(loadingFile);
            if (file.exists()) {
                return fileSave(0);
            }
        }

        return fileSave(1);
    }

    /**
     * 保存
     * @param mode 0 上書き,1 新規
     */
    public static boolean fileSave(int mode){

        try {

            String selectedFilePath;

            if( mode == 0 ){
                selectedFilePath = loadingFile;
            }

            else{
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showSaveDialog(mainFrame);

                if (result == JFileChooser.APPROVE_OPTION) {

                    selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    mainFrame.setTitle(selectedFilePath);
                    loadingFile = selectedFilePath;
                }

                else return false;
            }

            if( selectedFilePath == null ) return false;

            FileOutputStream fos = new FileOutputStream(selectedFilePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(coordsList);
            oos.close();
            fos.close();

            return true;
        } catch (IOException e) {
            // TODO: handle exception
        }
        return false;
    }

    public static boolean fileLoad(){
        return fileLoad(null);
    }

    @SuppressWarnings("unchecked")
    public static boolean fileLoad(String path){

        try {

            String selectedFilePath;

            if( path == null ){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(mainFrame);

                if (result == JFileChooser.APPROVE_OPTION) {

                    selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();

                    mainFrame.setTitle(selectedFilePath);
                    loadingFile = selectedFilePath;

                }
                else{
                    mainFrame.setTitle("新規ファイル");
                    return false;
                }
            }

            else{
                selectedFilePath = path;
            }

            FileInputStream fis = new FileInputStream(selectedFilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            coordsList = (ArrayList<LinkedList<Figure>>) ois.readObject();
            ois.close();
            fis.close();
            return true;
        } catch (IOException | ClassNotFoundException ignored) {
            JOptionPane.showMessageDialog(mainFrame, "ファイルの読み込みに失敗しました。");
        }

        return false;
    }
}
