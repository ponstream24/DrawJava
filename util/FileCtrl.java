package enshuReport2_2023.util;

import enshuReport2_2023.Figure;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static enshuReport2_2023.Paint.*;
import static enshuReport2_2023.util.ShowCtrl.closeNowLoading;
import static enshuReport2_2023.util.ShowCtrl.nowLoading;

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

                if( loadingFile != null ){
                    fileChooser.setCurrentDirectory(new File(loadingFile));
                }

                int result = fileChooser.showSaveDialog(mainFrame);

                if (result == JFileChooser.APPROVE_OPTION) {

                    selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    mainFrame.setTitle(selectedFilePath);
                    loadingFile = selectedFilePath;
                }

                else return false;
            }

            if( selectedFilePath == null ) return false;

            nowLoading();

            FileOutputStream fos = new FileOutputStream(selectedFilePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(coordsList);
            oos.close();
            fos.close();

            closeNowLoading();

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

                Object[] options = {"開く", "新規作成", "キャンセル"};

                // ダイアログを表示
                int typeResult = JOptionPane.showOptionDialog(
                        null,
                        "既存のファイルを読み込みますか？",
                        "読み込み",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

//                既存
                if( typeResult == 0 ){

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    if( loadingFile != null ){
                        fileChooser.setCurrentDirectory(new File(loadingFile));
                    }

                    int result = fileChooser.showOpenDialog(mainFrame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        mainFrame.setTitle(selectedFilePath);
                        loadingFile = selectedFilePath;
                    }
                    else{
                        return false;
                    }
                }

//                新規
                else if( typeResult == 1 ){
                    mainFrame.setTitle("新規ファイル");
                    return true;
                }
//                キャンセル
                else{
                    return false;
                }
            }

            else{
                selectedFilePath = path;
            }

            nowLoading();

            FileInputStream fis = new FileInputStream(selectedFilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            coordsList = (ArrayList<LinkedList<Figure>>) ois.readObject();
            ois.close();
            fis.close();

            closeNowLoading();

            return true;
        } catch (IOException | ClassNotFoundException ignored) {
            JOptionPane.showMessageDialog(mainFrame, "ファイルの読み込みに失敗しました。");
        }

        return false;
    }
}
