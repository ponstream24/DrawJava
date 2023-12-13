# DrawJava

簡単なお絵描きソフトです

* [目的](#目的)
* [課題](#課題)
* [理論](#理論)
* [苦労した点](#苦労した点)
* [プログラム(抜粋)](#プログラム(抜粋))
* [実行結果](#実行結果)
* [動作確認手法環境](#動作確認手法環境)
* [考察](#考察)
* [まとめ](#まとめ)
* [感想など](#感想など)
* [主な機能](#主な機能)
* [描画の種類](#描画の種類)

## 目的
前回のお絵描きソフトよりも進化したお絵描きソフトを作る。

## 課題
お絵かきソフトを進化させるにあたり、主に以下の機能の実装に取り組んだ。
1. 保存・読み込み機能
2. 編集履歴の操作
3. ダイヤログ
4. ボタンチェックボックス

など特にUIに関する部分を充実させた。その他機能は[こちら](#%E4%B8%BB%E3%81%AA%E6%A9%9F%E8%83%BD)に記載する

## 理論
* スムーズな線分を実現するため、Polylineを使用した。一本の線分だけではなく、連続した線分を使用することでより滑らかな線分を表現することができる。

## 苦労した点
* ボタンを設置した時にキー入力が行われない問題
  * AWTのフォーカスについてつまずいたが、ボタンにも`addKeyListener`をつけ問題なく動作した。
* 履歴の配列
  * UndoとRedoをそれぞれ配列に格納しているが、Undoに現在のデータを格納するタイミングや、取り出す際の重複問題に苦戦した。

## プログラム(抜粋) と 実行結果(抜粋)
### 閉じる前に確認
Paint.java
```java
this.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        if (isSaved || showSaveConfirmDialog()) {
            dispose();
        }
    }
});
```
Paint.java
```java
private boolean showSaveConfirmDialog() {

    int choice = JOptionPane.showConfirmDialog(this, "保存しますか?", "確認", JOptionPane.YES_NO_CANCEL_OPTION);

    if (choice == JOptionPane.YES_OPTION) {

        if (save()) {
            isSaved = true;
            return true;
        } else {
            return false;
        }
    } else return choice == JOptionPane.NO_OPTION;
}
```
Windowを閉じるリクエストが発生した時に、`保存済みか||保存完了したか`を確認して保存漏れが内容にした。

#### 動作
![ensyu5](https://chibakoudai.com/contents/tetsuka/ensyu5.png)
* 以下の時に確認が表示された
  * Cmd + Wで閉じようとした時
  * Cmd + Qで閉じようとした時
  * xを押して閉じようとした時
  * 終了ボタンを押して閉じようとした時
* 以下の分岐を確認できた
  * はい
    * 新規保存
        * システム終了
    * 上書き保存
        * システム終了
    * キャンセル
      * 確認ダイアログを閉じる
  * いいえ
    * 確認ダイアログを閉じる
      * システム終了
  * 取り消し
    * 確認ダイアログを閉じる

### HistoryCtrlクラス
HistoryCtrl.java
```java
package enshuReport2_2023.util;

import enshuReport2_2023.Figure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class HistoryCtrl {

    private final Stack<ArrayList<LinkedList<Figure>>> undoList = new Stack<>();
    private final Stack<ArrayList<LinkedList<Figure>>> redoList = new Stack<>();

    /**
     * 一つ戻る
     * @return null or Stack
     */
    public ArrayList<LinkedList<Figure>> undo() {

//        undoするものなし
        if (this.undoList.size() <= 0) return null;

//        ドゥー
        return doUndo();
    }

    private ArrayList<LinkedList<Figure>> doUndo() {

        ArrayList<LinkedList<Figure>> result = this.undoList.pop();

        this.redoList.push(result);

        return result;
    }

    /**
     * やり直し
     * @return null or Stack
     */
    public ArrayList<LinkedList<Figure>> redo() {

        if (this.redoList.size() <= 0) return null;

        return doRedo();
    }

    private ArrayList<LinkedList<Figure>> doRedo() {

        ArrayList<LinkedList<Figure>> result = this.redoList.pop();

        this.undoList.push(result);

        return result;
    }

    /**
     * 履歴に追加
     */
    public void add(ArrayList<LinkedList<Figure>> history) {

        ArrayList<LinkedList<Figure>> h = new ArrayList<>(history);

        this.undoList.push(h);
        this.redoList.clear();
    }

    public Stack<ArrayList<LinkedList<Figure>>> getUndoList() {

        return this.undoList;
    }

    public Stack<ArrayList<LinkedList<Figure>>> getRedoList() {
        return this.redoList;
    }

    public ArrayList<LinkedList<Figure>> getNextUndo() {

        if (this.undoList.size() == 0) return null;

        return this.undoList.get(this.undoList.size() - 1);
    }

    public ArrayList<LinkedList<Figure>> getNextRedo() {

        if (this.redoList.size() == 0) return null;

        return this.redoList.get(this.redoList.size() - 1);
    }
}
```
StackでUndoとRedoのリストを管理して、随時追加削除を行っている。

#### 動作
Undo実行前
![ensyu13](https://chibakoudai.com/contents/tetsuka/ensyu13.png)
Undo実行後
![ensyu14](https://chibakoudai.com/contents/tetsuka/ensyu14.png)
Redo実行後
![ensyu13](https://chibakoudai.com/contents/tetsuka/ensyu13.png)
* 以上の画像の様にUndoで戻り、Redoでやり直しができた。
* Undo,Redoの実行が不可能の時、各ボタンを無効化しユーザーにわかりやすくした。
* 以下の時に実行された
  * Undoボタンを押した時
  * Redoボタンを押した時
  * Cmd + Z を押した時
  * Cmd + Y を押した時


### どの方向にでも描けるようにする
Box.java, Circle.java
```java
if (w >= 0 && h >= 0) {
    g.fillRect(x, y, w, h);
} else if (w < 0 && h >= 0) {
    g.fillRect(x + w, y, -w, h);
} else if (w >= 0) {
    g.fillRect(x, y + h, w, -h);
} else{
    g.fillRect(x+w,y+h,-w,-h);
}
```
4方向に分けて処理する。

#### 動作
![ensyu15](https://chibakoudai.com/contents/tetsuka/ensyu15.png)
* 以下の場合に分けて処理を行った
  * 右上
  * 右下
  * 左下
  * 左上

### リサイズ
Paint.java
```java
this.addComponentListener(new ComponentAdapter() {
    @Override
    public void componentResized(ComponentEvent e) {
        setDesign();
    }
});
```
ShowCtrl.java
```java
public static void setDesign() {

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

    for (Checkbox checkbox : cgCheckBoxList) {

        int x = (int) (_w - checkbox_w);
        int y = (int) _h;
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

    for (Checkbox checkbox : colorList) {

        int x = (int) (_w - checkbox_w);
        int y = (int) __h;
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

    for (Component component : otherList) {

        int x = (int) (_w - checkbox_w);
        int y = (int) _h;
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

    for (Button button : buttonList) {

        int x = (int) (_w - button_w);
        int y = (int) _h;
        int w = (int) button_w;
        int h = (int) button_h;

        button.setBounds(x, y, w, h);

//			更新
        _h += checkbox_h + interval;
    }
}
```

#### 動作
起動時
![ensyu3](https://chibakoudai.com/contents/tetsuka/ensyu3.png)
Windowをリサイズ
![ensyu2](https://chibakoudai.com/contents/tetsuka/ensyu2.png)
* 画面のサイズを変更すると右側のツールバーも移動する

### 全削除
Paint.java
```java
else if (key == 'r' || key == 'R') {

    if (isDrawLock)
        return;
    coordsList = new ArrayList<>();
    coords = new LinkedList<>();
    historyAdd();
}
```
全削除できるようにショートカットキーを追加した。また、戻せる様に履歴に追加した。
#### 動作
実行前
![ensyu12](https://chibakoudai.com/contents/tetsuka/ensyu12.png)
実行後
![ensyu11](https://chibakoudai.com/contents/tetsuka/ensyu11.png)
* 以下の時に実行された
    * Cmd + Rで閉じようとした時
    * Cmd + Shift + Rで閉じようとした時

### 保存と読み込み
Paint.java
```java

public boolean save() {
    boolean result = fileSave();
    if (result)
        isSaved = true;
    return result;
}

public boolean saveNew() {
    boolean result = fileSave(1);
    if (result)
        isSaved = true;
    return result;
}

public void load() {
    load(null);
}

public void load(String str) {

    ArrayList<LinkedList<Figure>> _cooArrayList = new ArrayList<>(coordsList);
    LinkedList<Figure> _coords = new LinkedList<>(coords);
    coordsList = new ArrayList<>();
    coords = new LinkedList<>();

    boolean result = fileLoad(str);

    if (result) {
        offScreenImage = null;
        repaint();
    } else {
        coordsList = _cooArrayList;
        coords = _coords;
    }

    offScreenImage = null;
    repaint();
}
```
FileCtrl.java
```java
/**
 * Yuki Tetsuka
 * <p>
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 * <p>
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023.util;

import static enshuReport2_2023.Paint.*;
import static enshuReport2_2023.util.ShowCtrl.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import enshuReport2_2023.Figure;

public class FileCtrl {

    public static boolean fileSave() {

        if (loadingFile != null) {
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
    public static boolean fileSave(int mode) {

        try {

            String selectedFilePath;

            if (mode == 0) {
                selectedFilePath = loadingFile;
            } else {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (loadingFile != null) {
                    fileChooser.setCurrentDirectory(new File(loadingFile));
                }

                int result = fileChooser.showSaveDialog(mainFrame);

                if (result == JFileChooser.APPROVE_OPTION) {

                    selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                    mainFrame.setTitle(selectedFilePath);
                    loadingFile = selectedFilePath;
                } else return false;
            }

            if (selectedFilePath == null) return false;

            nowLoading();

            FileOutputStream fos = new FileOutputStream(selectedFilePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(coordsList);
            oos.close();
            fos.close();

            closeNowLoading();

            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    public static boolean fileLoad() {
        return fileLoad(null);
    }

    @SuppressWarnings("unchecked")
    public static boolean fileLoad(String path) {

        try {
            String selectedFilePath;

            if (path == null) {

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
                if (typeResult == 0) {

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    if (loadingFile != null) {
                        fileChooser.setCurrentDirectory(new File(loadingFile));
                    }

                    int result = fileChooser.showOpenDialog(mainFrame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        mainFrame.setTitle(selectedFilePath);
                        loadingFile = selectedFilePath;
                    } else {
                        return false;
                    }
                }

//                新規
                else if (typeResult == 1) {
                    mainFrame.setTitle("新規ファイル");
                    return true;
                }
//                キャンセル
                else {
                    return false;
                }
            } else {
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
            closeNowLoading();
        }

        return false;
    }
}
```
* ファイルが存在するか確認して存在しない場合は`新規保存`、する場合は`上書き`または`新規保存`を促す。
* 以下の時に確認が表示された
  * Cmd + Wで閉じようとした時
  * Cmd + Qで閉じようとした時
  * xを押して閉じようとした時
  * 終了ボタンを押して閉じようとした時
  * Cmd + Sで保存しようとした時
  * Cmd + Shift + Sで保存しようとした時(`新規保存`)
* 以下の分岐を確認できた
  * ファイルが存在する
    * 新規保存
      * システム終了
    * 上書き保存
      * システム終了
    * キャンセル
      * 確認ダイアログを閉じる
  * ファイルが存在しない
    * 新規保存
      * システム終了
    * キャンセル
      * 確認ダイアログを閉じる
  
## 動作確認手法・環境
以下の実行環境で行った
* Eclipse上で制御文字が使えるようにASCII制御文字をオンにした。
* 手順は以下のURLの通り、`設定`->`実行/デバック`->`コンソール`->`ASCII制御文字を解釈する`->`復帰文字(\r)を制御文字として解釈する` をオンにする
* https://eclipse.dev/eclipse/news/4.14/platform.php#control-character-console
- ①環境
  * JRE -> JavaSE-17
  * OS  -> Windows 10 Pro
  * プロセッサ -> Intel(R) Core(TM) i5-7500T CPU @ 2.70GHz   2.71 GHz
  * RAM -> 8.00 GB
- ②環境
  * JRE -> JavaSE-17
  * OS  -> macOS Canalia
  * プロセッサ -> 1.6 GHz デュアルコアIntel Core i5
  * RAM -> 8 GB 2133 MHz LPDDR3

## 考察
### 画面のリサイズ時のチラつき
* 画面のリサイズを行った際に、右側に設置しているチェックボックスやボタンがちらつくことがある。
  * `public void componentResized(ComponentEvent e)`が呼び出されるごとにボタンの座標を計算、配置を行っているためラグが発生していると考える。
  * また端末ごとにチラつき具合が変わるため、PCのスペック問題とも考えられる。
### チェックボックス・ボタン設置した際にキー入力ができない
* チェックボックス・ボタンを設置をした際にショートカットキーの処理などのキー入力ができない
  * フォーカスがチェックボックス・ボタンに向いている可能性があったため、チェックボックス・ボタンに対して`checkbox.addKeyListener(this);`でイベントをりっすんをした　

## まとめ
`前回のお絵描きソフトよりも進化したお絵描きソフトを作る。`のためにさまざまな機能を追加できた。特にUIにこだわることで、より使えるお絵描きソフトとなった。

## 感想など
全ての機能を実装するまでに、所々躓くことがあったが、難なく制作に取り組むことができた。

## 主な機能

* 筆
    * 以下の種類の描画を行うことができます。
        * 自由線
            * 自由に線を描けます。点ごとをポリラインで結びスムーズな線を描きます。
        * 円
        * 四角
        * 線
    * 塗り直し
        * 塗り直しの有無を設定できます。
          * 色
    * デフォルト10色
    * ダイヤログで自由な色を指定できます。(実質無限化)
* 編集履歴
    * 編集履歴を保存しているため、「戻る」「やり直す」が可能です。
* データの出力
    * 読み込み
        * データファイルを読み込んでキャンパスを復元・編集ができます。
            * 保存せず読み込みを行おうとすると、保存を促すダイヤログを表示させます。
    * 保存
        * 上書き
            * 既に存在するファイルに対して上書きして保存をできます。
            * 新規ファイルを生成してデータを保存します。
    * ダイヤログ
        * 読み込み/保存をダイヤログで行えます。
* オフスクリーン
    * オフスクリーンを生成することで、画面のチラつきを無くしました。
* ガイド
    * 左上に情報を表示しています。
        * 右クリック・左クリックの説明
        * スクロール
        * ナンバーキー番号
          *コンソール
        * オブジェクト数
        * マウスの座標
        * 生成中の幅・高さ
        * 色
        * 描画モード
* キーボード
    * ショートカットキー
        * [詳細はこちら](#%E3%82%B7%E3%83%A7%E3%83%BC%E3%83%88%E3%82%AB%E3%83%83%E3%83%88)
    * 数字キー
        * 対象の数字キーを押すことで筆の色を変更できます。
* スクロール
    * スクロールすることで自由線の太さを変更できます。
* マウス
    * 右クリック
        * 消しゴム
    * 左クリック
        * 選択中の描画方法
    * 中クリック
        * キャンパスを全体的に移動させます。
* 読み込み中
    * 以下のタイミングで読み込み中のダイヤログが表示されます。
        * 起動時
        * ファイル読み込み時
        * ファイル書き込み中
* 描画ロック
    * 描画したキャンパスを閲覧する用の機能です。
    * キャンパスに対して描画等ができなくなります。
* 画面リサイズ
    * ウィンドウのサイズを変更すると、右側に表示されているボックスなどの位置も動的に変更されます。
* チェックボックス/ボタン
  * 以下の機能を実装
    * 色
    * 描画種類
    * コントール
    * アクション
      * **Undo,Redoは実行不可の場合、ボタンは無効化される**
  * 動的に配置しているため、急遽ボタンを増やしてもデザインが崩れない。
* 終了
    * 終了時に保存していなかった場合、保存確認のダイヤログが表示されます。
        * キャンセル/保存しない/保存から選択できます。
* Git
    * [GitHubと連携済み](https://github.com/ponstream24/DrawJava)

## 描画の種類

描画は以下の種類で行えます。

### 自由線

ペンの様に自由に線を描けます。

* 塗り直し : 不可
* 色 : 可
* 太さ : 可

### 円

楕円の大きさを指定して配置できます。

* 塗り直し : 可
* 色 : 可
* 太さ : 不可

### 四角

四角形の大きさを指定して配置できます。

* 塗り直し : 不可
* 色 : 可
* 太さ : 不可

### 線

直線の2点を指定して配置できます。

* 塗り直し : 不可
* 色 : 可
* 太さ : 可

### 消しゴム

ペンの様に自由に線を消せます。

* 塗り直し : 不可
* 色 : 不
* 太さ : 可

## ショートカット

* Undo
    * 戻る
        * Ctrl + Z
        * Ctrl + 左
        * Cmd + Z
        * Cmd + 左
* Redo
    * やり直し
        * Ctrl + Shift + Z
        * Ctrl + 右
        * Cmd + Shift + Z
        * Cmd + 右
* All Delete
    * 全削除
        * R
        * Shift + R
* Color
    * 色
        * 1(黒)
        * 2(赤)
        * 3(青)
        * 4(シアン)
        * 5(ピンク)
        * 6(灰)
        * 7(緑)
        * 8(黄)
        * 9(マゼンタ)
* Save
    * 上書き保存
        * Ctrl + S
        * Cmd + S
    * 新規保存
        * Ctrl + Shift + S
        * Cmd + Shift + S
* Close
    * 閉じる
        * Ctrl + Q
        * Cmd + Q
        * Ctrl + W
        * Cmd + W
* Open
    * 開く
        * Ctrl + N
        * Cmd + N