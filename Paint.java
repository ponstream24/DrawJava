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

import javax.swing.*;

import static enshuReport2_2023.util.ColorCtrl.*;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

// MouseListener -> マウスクリック等監視
// MouseMotionListener -> マウス動き等監視
// KeyListener -> キー監視
// MouseWheelListener -> マウスホイール監視
public class Paint extends Frame
		implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener, ActionListener {

	//	初期化
	int x, y;
	Color color = Color.BLACK; // 色
	int size = 1; // 太さ
	Figure c; // 点
	LinkedList<Figure> coords = new LinkedList<>();// 線分
	ArrayList<LinkedList<Figure>> coordsList = new ArrayList<LinkedList<Figure>>(); //　線分リスト

	CheckboxGroup cbg;
	Checkbox c1, c2, c3, c4;
	Button end, undo, redo, colorSelect;

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
		f.setSize(640, 480);
		f.setTitle("2232103");
		f.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setVisible(true);
		if (args.length == 1)
			f.load("paint.dat");
	}

	//	コンストラクター
	public Paint() {
		c = null;

		//		各イベント監視開始
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);

		setLayout(null);
		cbg = new CheckboxGroup();

		c1 = new Checkbox("丸", cbg, true);
		c1.setBounds(560, 30, 60, 30);
		add(c1);
		c1.addKeyListener(this);

		c2 = new Checkbox("円", cbg, false);
		c2.setBounds(560, 60, 60, 30);
		add(c2);
		c2.addKeyListener(this);

		c3 = new Checkbox("四角", cbg, false);
		c3.setBounds(560, 90, 60, 30);
		add(c3);
		c3.addKeyListener(this);

		c4 = new Checkbox("線", cbg, false);
		c4.setBounds(560, 120, 60, 30);
		add(c4);
		c4.addKeyListener(this);

		colorSelect = new Button("色を選択");
		colorSelect.setBounds(560, 300, 120, 30);
		add(colorSelect);

		undo = new Button("Undo");
		undo.setBounds(560, 330, 120, 30);
		add(undo);

		redo = new Button("Redo");
		redo.setBounds(560, 360, 120, 30);
		add(redo);

		end = new Button("終了");
		end.setBounds(560, 360, 60, 30);
		add(end);

		end.addActionListener(this);
		colorSelect.addActionListener(this);

		end.addKeyListener(this);
		colorSelect.addKeyListener(this);
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

		Checkbox cb = cbg.getSelectedCheckbox();

		if (cb == c1) {
			c = new Dot();
			mode = 0;
		} else if (cb == c2) {
			c = new Circle();
			mode = 1;
		} else if (cb == c3) {
			c = new Box();
			mode = 1;
		} else if (cb == c4) {
			c = new Line(this.size);
			mode = 3;
		} else {
			c = new Dot();
			mode = 0;
		}

		// 左
		if (e.getButton() == MouseEvent.BUTTON1) {

		}

		// 中央
		else if (e.getButton() == MouseEvent.BUTTON2) {

			//			null => 全体移動用
			mode = 2;
		}

		// 右
		else {

			//			消しゴム用四角
			mode = 4;
		}

		//		描画中の点があるなら
		if (c != null) {

			//			点を移動させ、座標を書き換え
			c.moveto(e.getX(), e.getY());
			c.setWH(size, size);
			c.color = this.color;
		}

		//		オフスクリーンをリセット
		offScreenImage = null;

		//		再描画をリクエスト
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// 全体移動用なら
		if (mode == 2) {

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

			//			線分リストに線分を追加
			coordsList.add(coords);
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

		Checkbox cb = cbg.getSelectedCheckbox();

		//		if( cb == c4 ) {
		////			オフスクリーンをリセット
		//			offScreenImage = null;
		//
		//			//		再描画をリクエスト
		//			repaint();
		//
		//			//		座標を更新
		//			x = e.getX();
		//			y = e.getY();
		//		}

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
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyPressed(KeyEvent e) {

		//		押されたキーを取得
		int key = e.getKeyChar();

		//		もしキーが〇なら◇色にcolorを設定
		if (key == '1') {
			color = Color.BLACK;
		} else if (key == '2') {
			color = Color.RED;
		} else if (key == '3') {
			color = Color.BLUE;
		} else if (key == '4') {
			color = Color.CYAN;
		} else if (key == '5') {
			color = Color.PINK;
		} else if (key == '6') {
			color = Color.GRAY;
		} else if (key == '7') {
			color = Color.GREEN;
		} else if (key == '8') {
			color = Color.YELLOW;
		} else if (key == '9') {
			color = Color.MAGENTA;
		} else if (key == '0') {
			color = Color.ORANGE;
		} else if (key == 'r' || key == 'R') {
			coordsList = new ArrayList<>();
			coords = new LinkedList<>();
		} else {
			//			数字以外
			return;
		}

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

			//		線分を初期化
			coords = new LinkedList<>();

			//		色をセット
			c.color = this.color;
			colorSelect.setBackground(this.color);

			//		オフスクリーンをリセット
			offScreenImage = null;

			//		再描画をリクエスト
			repaint();
		}

		else if( e.getActionCommand().equalsIgnoreCase("Undo") ){

		}
		else if( e.getActionCommand().equalsIgnoreCase("Redo") ){

		}
		else{

			save("paint.dat");
			System.exit(0);
		}
	}

	public void save(String fname) {
		try {
			FileOutputStream fos = new FileOutputStream(fname);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(coordsList);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("unchecked")
	public void load(String fname) {
		try {
			FileInputStream fis = new FileInputStream(fname);
			ObjectInputStream ois = new ObjectInputStream(fis);
			coordsList = (ArrayList<LinkedList<Figure>>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException ignored) {
		}

		repaint();
	}
}
