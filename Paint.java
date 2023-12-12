/**
 * Yuki Tetsuka
 *
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 *
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

// https://eclipse.dev/eclipse/news/4.14/platform.php#control-character-console

package enshuReport2_2023;

import enshuReport2_2023.util.HistoryCtrl;

import javax.swing.*;

import static enshuReport2_2023.util.ColorCtrl.*;
import static enshuReport2_2023.util.FileCtrl.fileLoad;
import static enshuReport2_2023.util.FileCtrl.fileSave;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

// MouseListener -> マウスクリック等監視
// MouseMotionListener -> マウス動き等監視
// KeyListener -> キー監視
// MouseWheelListener -> マウスホイール監視
public class Paint extends Frame
		implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener, ActionListener {

	//	初期化
	public static Frame mainFrame;
	public static String loadingFile = null;
	public int x, y;
	public boolean isUndoRedoLastTime = false;
	public boolean isFill = false;
	public boolean isSaved = true;
	public Color color = Color.BLACK; // 色
	public int size = 1; // 太さ
	public Figure c; // 点
	public LinkedList<Figure> coords = new LinkedList<>();// 線分
	public static ArrayList<LinkedList<Figure>> coordsList = new ArrayList<>(); //　線分リスト

	public HistoryCtrl historyCtrl = new HistoryCtrl();

	public CheckboxGroup cbg;
	public ArrayList<Checkbox> cgCheckBoxList = new ArrayList<>();
	public ArrayList<Button> buttonList = new ArrayList<>();
	public ArrayList<Component> otherList = new ArrayList<>();

	/**
	 *  0:通常描画
	 *  1:大きさ変更して描画
	 *  2:全体移動
	 *  3:線
	 *  4:消しゴム
	 */
	int mode = 0;

	//	psvm
	public static void main(String[] args) {

		//		インスタンス
		Paint f = new Paint();
		Paint.mainFrame = f;
		f.setTitle("2232103");
		f.setVisible(true);
		if (args.length >= 1)
			f.load(args[0]);
	}

	//	コンストラクター
	public Paint() {
		c = null;

		this.setSize(640, 480);

		//		各イベント監視開始
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if( isSaved || showSaveConfirmDialog() ){
					dispose();
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setDesign();
			}
		});

		setLayout(null);

		cbg = new CheckboxGroup();

		String[] labels = {"自由線", "円", "四角", "線"};

		for (String label : labels) {
			Checkbox checkbox;
			checkbox = new Checkbox(label, cbg, false);
			add(checkbox);
			checkbox.addKeyListener(this);
			cgCheckBoxList.add(checkbox);
		}

		cgCheckBoxList.get(0).setState(true);

		String[] otherLabels = {"塗り直し", "描画ロック"};

		for (String otherLabel : otherLabels) {
			Checkbox checkbox;
			checkbox = new Checkbox(otherLabel);
			add(checkbox);
			checkbox.addKeyListener(this);
			otherList.add(checkbox);
		}

		cgCheckBoxList.get(0).setState(true);

		String[] buttonLabels = {"色を選択", "Undo", "Redo", "終了"};

		for (int i = 0; i < buttonLabels.length; i++) {
			Button b = new Button(buttonLabels[i]);
			b.setBounds(560, 300 + i *30, 120, 30);
			add(b);
			b.addActionListener(this);
			b.addKeyListener(this);
			buttonList.add(b);
		}

		setDesign();

		historyAdd();
	}

	/**
	 * 画面ちかちか防止のため、一旦オフスクリーンに描画して、ちかちかを防止する
	 */

	//	オフスクリーン
	Image offScreenImage;
	Graphics offScreenGraphics;

	@Override
	public void update(Graphics g) {

		if (offScreenImage == null) {

			// オフスクリーンイメージをフルスクリーンサイズで作成
			offScreenImage = createImage(1980, 1080);

			// オフスクリーンイメージに描画するための Graphics オブジェクト
			offScreenGraphics = offScreenImage.getGraphics();
		}

		// offScreenGraphicsにペイントする
		paint(offScreenGraphics);

		// イメージをスクリーンに書き込む
		g.drawImage(offScreenImage, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {

		updateHistoryButton();

		//		2Dにキャスト
		Graphics2D g2 = (Graphics2D) g;

		//		線分リストをループ
		if (coordsList.size() > 0) {

			//			線分をループ
			for (LinkedList<Figure> coords : coordsList) {

				//				線分が１
				if (coords.size() == 1) {
					coords.get(0).paint(g);
				} else if (coords.size() > 0) {

					Coord lastCoord = null;

					//					各点を描画
					for (Coord _c : coords) {

						if (lastCoord == null) {
							_c.paint(g);
						}

						lastCoord = _c;
					}

					//					線分内の点で配列を形成
					int n = coords.size();
					int size = coords.get(0).w;
					int[] xp = new int[n];
					int[] yp = new int[n];
					for (int i = 0; i < n; i++) {

						xp[i] = coords.get(i).x;
						yp[i] = coords.get(i).y;
					}

					//					ポリラインで線分を描画
					BasicStroke bs = new BasicStroke(size);
					g2.setStroke(bs);
					g2.drawPolyline(xp, yp, n);
				}
			}
		}

		//		描画中の線分あり
		if (coords.size() > 0) {

			Coord lastCoord = null;

			//			各点を描画
			for (Coord _c : coords) {

				if (lastCoord == null) {
					_c.paint(g);
				}

				lastCoord = _c;
			}

			//			線分内の点で配列を形成
			int n = coords.size();
			int size = coords.get(0).w;
			int[] xp = new int[n];
			int[] yp = new int[n];
			for (int i = 0; i < n; i++) {

				xp[i] = coords.get(i).x;
				yp[i] = coords.get(i).y;
			}

			//			ポリラインで線分を描画
			BasicStroke bs = new BasicStroke(size);
			g2.setStroke(bs);
			g2.drawPolyline(xp, yp, n);
		}

		//		描画中の点あり　なら　描画
		if (c != null)
		{
//			if( mode == 1 ) {
//				c.paintCursor(g);
//			}
//			else {
				c.paint(g);
//			}
		}

		//		info用
		ArrayList<String> info = new ArrayList<>();

		//		説明を追加
		info.add("~How to Use~");
		info.add("・Left: " + getStringColor(color) + " Cicle");
		info.add("・Right: While Circle");
		info.add("・Center: Move");
		info.add("・Scroll: Size(" + this.size + ")");
		info.add("");
		info.add("1 : 黒");
		info.add("2 : 赤");
		info.add("3 : 青");
		info.add("4 : シアン");
		info.add("5 : ピンク");
		info.add("6 : 灰色");
		info.add("7 : 緑");
		info.add("8 : 黄色");
		info.add("9 : マゼンタ");
		info.add("0 : オレンジ");

		//		文字色は黒
		g.setColor(Color.BLACK);

		//		説明を記述
		for (int i = 0; i < info.size(); i++) {
			g.drawString(info.get(i), 10, 50 + (i * 15));
		}

		//		カウント情報等を表示
		showCount();
	}

	@Override
	public void mousePressed(MouseEvent e) {

		if(otherList.get(0) instanceof Checkbox isFillCheckbox){

			isFill = isFillCheckbox.getState();
		}

		Checkbox cb = cbg.getSelectedCheckbox();

		if (cb == cgCheckBoxList.get(0)) {
			c = new Dot();
			mode = 0;
		} else if (cb == cgCheckBoxList.get(1)) {
			c = new Circle();
			mode = 1;
		} else if (cb == cgCheckBoxList.get(2)) {
			c = new Box();
			mode = 1;
		} else if (cb == cgCheckBoxList.get(3)) {
			c = new Line(this.size);
			mode = 3;
		} else {
			c = new Dot();
			mode = 0;
		}

		// 中央
		if (e.getButton() == MouseEvent.BUTTON2) {

			//			null => 全体移動用
			mode = 2;
		}

		// 右
		else if (e.getButton() == MouseEvent.BUTTON3){

			//			消しゴム用四角
			c = new Box(true);
			mode = 4;
		}

		//		描画中の点があるなら
		if (c != null) {

			//			点を移動させ、座標を書き換え
			c.moveto(e.getX(), e.getY());
			c.setWH(size, size);
			c.color = this.color;
			c.isFill = this.isFill;
		}

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (mode != 2 && c != null) {

			//			点を深層クローン
			Figure _Coord = c.clone();

			//			線分に追加
			coords.add(_Coord);

			//			サイズを引き継ぎ
			c.setWH(size, size);

			//			色を引き継ぎ
			c.color = this.color;

			//			線分リストに線分を追加
			coordsList.add(coords);
			historyAdd();
		}
		
		
		if( c != null && (mode == 1 || mode == 3)) {
			c.moveto(e.getX(), e.getY());

			if( mode == 3 ){
				c.width = this.size;
				c.setWH(1, 1);
			}
		}

		//		線分を初期化
		coords = new LinkedList<>();

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		// 全体移動用なら
		if (mode == 2) {

			//			動いた量
			int moveX = e.getX() - this.x;
			int moveY = e.getY() - this.y;

			//			線分リストがあるなら
			if (coordsList.size() > 0) {

				//				線分リストをループ
				for (LinkedList<Figure> coords : coordsList) {

					//					線分があるなら
					if (coords.size() > 0) {

						//						線分をループ
						for (Coord _c : coords) {

							//							点を移動
							_c.move(moveX, moveY);
						}
					}
				}
			}

			//			線分があるなら
			if (coords.size() > 0) {

				//				線分をループ
				for (Coord _c : coords) {

					//					点を移動
					_c.move(moveX, moveY);
				}
			}

			if (c != null) {
				//			マウスが動いた分移動させる
				c.move(e.getX() - x, e.getY() - y);
			}
		}

		else if (mode == 1 || mode == 3) {
			c.setWH(x -= c.x, y - c.y);
		}

		//		描画中の点があるなら
		else if (c != null) {

			//			点を深層クローン
			Figure _Coord = c.clone();

			//			線分に追加
			coords.add(_Coord);

			//			サイズを引き継ぎ
			c.setWH(size, size);

			//			色を引き継ぎ
			c.color = this.color;

			//			マウスが動いた分移動させる
			c.move(e.getX() - x, e.getY() - y);
		}

		//		座標を更新
		x = e.getX();
		y = e.getY();

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		// 全体移動用なら
		if (mode == 2) {

			//			サークルをインスタンス
			c = new Circle();

			//			移動、現在の設定を適応
			c.moveto(e.getX(), e.getY());

			//			サイズを引き継ぎ
			c.setWH(size, size);

			//			色を引き継ぎ
			c.color = this.color;
		}

		//		描画中の点があるなら
		else if (c != null) {

			if (mode == 1) {
				c.moveto(e.getX(), e.getY());
			}

			else {
				//			点を移動
				c.move(e.getX() - x, e.getY() - y);
			}
		}

		else {

			//			サークルをインスタンス
			c = new Circle();

			//			移動、現在の設定を適応
			c.moveto(e.getX(), e.getY());

			//			サイズを引き継ぎ
			c.setWH(size, size);

			//			色を引き継ぎ
			c.color = this.color;
		}

		//		座標を更新
		x = e.getX();
		y = e.getY();

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	/**
	 * コンソールに情報を表示
	 */
	public void showCount() {

		//		カウント初期化
		int count = 0;

		//		線分リストをループ
		for (LinkedList<Figure> coords : coordsList) {

			//			線分内の点の数を足す
			count += coords.size();
		}

		//		描画中の線分の点の数を足す
		count += coords.size();

		//		コンソールの現在の行の最初の文字まで戻る
		clearConsole();

		//		出力
		System.out.print("Count : " + count);
		System.out.print(", X : " + x);
		System.out.print(", Y : " + y);
		System.out.print(", W : " + size);
		System.out.print(", H : " + size);
		System.out.print(", Color : " + getStringColor(color));
		System.out.print(", Mode : " + mode);
		System.out.print("     ");
	}

	/**
	 * コンソールの現在の行の最初の文字まで戻る
	 */
	public void clearConsole() {
		System.out.print("\r");
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {

		//		押されたキーを取得
		int key = e.getKeyChar();

		//		もしキーが〇なら◇色にcolorを設定
		if (key == '1') color = Color.BLACK;
		else if (key == '2') color = Color.RED;
		else if (key == '3') color = Color.BLUE;
		else if (key == '4') color = Color.CYAN;
		else if (key == '5') color = Color.PINK;
		else if (key == '6') color = Color.GRAY;
		else if (key == '7') color = Color.GREEN;
		else if (key == '8') color = Color.YELLOW;
		else if (key == '9') color = Color.MAGENTA;
		else if (key == '0') color = Color.ORANGE;
		else if (key == 'r' || key == 'R') {
			coordsList = new ArrayList<>();
			coords = new LinkedList<>();
			historyAdd();
		}

//		Mac : Command + Z + Shift
//		Win : Ctrl + Z + Shift(じゃないの？)  Ctrl + Z
		else if (
					(( (key == 'z' || key == 'Z') || e.getKeyCode() == 39 ) &&
					( ( e.isMetaDown() || e.isControlDown() ) ) &&
					e.isShiftDown()
				) ||
				( (key == 'y' || key == 'Y') && e.isControlDown() )
		) redo();

//		Mac : Command + (Z + <-)
//		Win : Ctrl + (Z + <-)
		else if (( (key == 'z' || key == 'Z') || e.getKeyCode() == 37 ) && ( e.isMetaDown() || e.isControlDown() )) undo();

//		Mac : Command + Shift + S
//		Win : Ctrl + Shift + s
		else if (( key == 's' || key == 'S') && ( e.isMetaDown() || e.isControlDown() ) && e.isShiftDown()){
			if(saveNew()){
				JOptionPane.showMessageDialog(this, "新規保存しました。");
			}
			else{
				JOptionPane.showMessageDialog(this, "保存に失敗しました。");
			}
		}

//		Mac : Command + S
//		Win : Ctrl + s
		else if (( key == 's' || key == 'S') && ( e.isMetaDown() || e.isControlDown() )){
			if(save()){
				JOptionPane.showMessageDialog(this, "上書き保存しました。");
			}
			else{
				JOptionPane.showMessageDialog(this, "保存に失敗しました。");
			}
		}

		else return;

//		色を変えるごとに保存　-> 需要ない
//		if( coords.size() > 0 ) historyAdd();

		//		線分を線分リストに追加
		coordsList.add(coords);

		//		線分を初期化
		coords = new LinkedList<>();

		//		色をセット
		c.color = this.color;

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		// TODO 自動生成されたメソッド・スタブ

		//		マウスが動いた分　＊　マウスが動いた向き　をsizeに足す
		this.size += e.getScrollAmount() * e.getWheelRotation();

		//		サイズが1以下なら
		if (this.size <= 1) {

			//			サイズを1にする」
			this.size = 1;
		}

		//		描画中の点があるなら
		if (c != null) {

			//			サイズを更新する　
			if( mode == 3 ) c.width = this.size;

			else c.setWH(size, size);

			//		色をセット
			c.color = this.color;
		}

		//		線分を線分リストに追加
		coordsList.add(coords);

//		historyAdd();

		//		線分を初期化
		coords = new LinkedList<>();

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( e.getActionCommand().equalsIgnoreCase("色を選択") ){

			this.color = JColorChooser.showDialog(this, "色の選択", Color.white);

			if( this.color == null  ) this.color = Color.BLACK;

			//		線分を線分リストに追加
			coordsList.add(coords);
//			historyAdd();

			//		線分を初期化
			coords = new LinkedList<>();

			//		色をセット
			c.color = this.color;
			buttonList.get(0).setBackground(this.color);

			//		オフスクリーンをリセット
			offScreenImage = null;

			//		再描画をリクエスト
			repaint();
		}

		else if( e.getActionCommand().equalsIgnoreCase("Undo") ){
			undo();
		}
		else if( e.getActionCommand().equalsIgnoreCase("Redo") ){
			redo();
		}
		else{

			if( isSaved || showSaveConfirmDialog() ){
				dispose();
			}
		}

		updateHistoryButton();
	}

	public boolean save() {
		boolean result = fileSave();
		if( result ) isSaved = true;
		return result;
	}
	public boolean saveNew() {
		boolean result = fileSave(1);
		if( result ) isSaved = true;
		return result;
	}

	public void load() {
		load(null);
		isSaved = true;
	}

	public void load(String str) {
		fileLoad(str);
		repaint();
	}

	public void updateHistoryButton(){

		buttonList.get(1).setEnabled(historyCtrl.getNextUndo() != null);

		buttonList.get(2).setEnabled(historyCtrl.getNextRedo() != null);
	}

	private void historyAdd(){

		isSaved = false;

		if( isUndoRedoLastTime ) {

//				履歴に追加
			ArrayList<LinkedList<Figure>> cloneCoordsList = new ArrayList<>(coordsList);
			cloneCoordsList.remove(cloneCoordsList.size() - 1);
			historyCtrl.add(cloneCoordsList);
			historyCtrl.add(coordsList);
		}

		else {

//				履歴に追加
			historyCtrl.add(coordsList);
		}

		isUndoRedoLastTime = false;
	}

	private void undo(){

		if( historyCtrl.getNextUndo() == null ) historyCtrl.undo();

		if( coordsList.size() == historyCtrl.getNextUndo().size() ) historyCtrl.undo();

		ArrayList<LinkedList<Figure>> _list = historyCtrl.undo();
		isUndoRedoLastTime = true;

		if( _list == null ){
			coordsList = new ArrayList<>();

			//		オフスクリーンをリセット
			offScreenImage = null;

			//		再描画をリクエスト
			repaint();
			return;
		}
		else{
			coordsList = _list;
		}

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	private void redo(){

		if( historyCtrl.getNextRedo() == null ) return;

		if( coordsList.size() == historyCtrl.getNextRedo().size() ) historyCtrl.redo();

		ArrayList<LinkedList<Figure>> _list = historyCtrl.redo();
		isUndoRedoLastTime = true;

		if( _list == null ) return;

		coordsList = _list;

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	private boolean showSaveConfirmDialog(){

		int choice = JOptionPane.showConfirmDialog(this, "保存しますか?", "確認", JOptionPane.YES_NO_CANCEL_OPTION);

		if (choice == JOptionPane.YES_OPTION) {

			if(save()){
				isSaved = true;
				System.out.println("保存しました。");
				return true;
			}
			else{
				return false;
			}
		} else if (choice == JOptionPane.NO_OPTION) {
			System.out.println("保存しませんでした。");
			return true;
		} else {
			return false;
		}
	}

	private void setDesign(){

		int margin_top = 50;
		int margin_right = 20;
		int interval = 10;

//		画面のサイズ
		double widthMax = this.getSize().getWidth();
		double heightMax = this.getSize().getHeight();

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
		_h += 20;

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
