/**
 * Yuki Tetsuka
 *
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 *
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023.util;

import javax.swing.*;

import java.awt.*;

import static enshuReport2_2023.Paint.*;

public class ShowCtrl {

    private static JDialog dialog;

    public static void nowLoading(){

        dialog = new JDialog((Frame) null, "Now Loading", false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // ダイアログを閉じるボタンを表示する
        dialog.setUndecorated(false);

        // 非モーダルダイアログを表示
        JLabel label = new JLabel("読み込み中...");
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog.add(label);

        // ダイアログを表示
        dialog.setSize(200, 100);
        dialog.setVisible(true);
    }

    public static void closeNowLoading(){

        if( dialog != null ){
            dialog.dispose();
        }
    }


    public static void showError(){
        showError("不明なエラーです");
    }
    public static void showError(String mes){
        JOptionPane.showMessageDialog(mainFrame, mes);
    }

    public static void setDesign(){

        int margin_top = 50;
        int margin_right = 20;
        int interval = 10;

//		画面のサイズ
        double widthMax = mainFrame.getSize().getWidth();
        double heightMax = mainFrame.getSize().getHeight();

//		現在の位置
        double _w = widthMax - margin_right;
        double _h = margin_top;

//		checkボックス設定
        double checkbox_w = 80;
        double checkbox_h = 30;

        for ( Checkbox checkbox : cgCheckBoxList ){

            int x = (int) (_w - checkbox_w);
            int y = (int) _h ;
            int w = (int) checkbox_w;
            int h = (int) checkbox_h;

            checkbox.setBounds(x, y, w, h);

//			更新
            _h += checkbox_h + interval;
        }

//		間隔
        _w -= checkbox_w + interval;
        checkbox_w = 120;
        double __h = margin_top;

        for ( Checkbox checkbox : colorList ){

            int x = (int) (_w - checkbox_w);
            int y = (int) __h ;
            int w = (int) checkbox_w;
            int h = (int) checkbox_h;

            checkbox.setBounds(x, y, w, h);

//			更新
            __h += checkbox_h + interval;
        }

//		間隔
        _h += 20;
        _w = widthMax - margin_right;

        checkbox_w = 100;

        for ( Component component : otherList ){

            int x = (int) (_w - checkbox_w);
            int y = (int) _h ;
            int w = (int) checkbox_w;
            int h = (int) checkbox_h;

            component.setBounds(x, y, w, h);

//			更新
            _h += checkbox_h + interval;
        }

//		間隔
        _h += 20;

//		ボタン設定
        double button_w = 120;
        double button_h = 30;

        for ( Button button : buttonList ){

            int x = (int) (_w - button_w);
            int y = (int) _h ;
            int w = (int) button_w;
            int h = (int) button_h;

            button.setBounds(x, y, w, h);

//			更新
            _h += checkbox_h + interval;
        }
    }
}
