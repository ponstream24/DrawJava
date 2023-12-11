/**
 * Yuki Tetsuka
 *
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 *
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023;

public class CircleTest {

	public static void main(String[] args) {

		Circle c = new Circle();

		c.move(100, 100);
		System.out.println("x = " + c.x + " y = " + c.y);

		c.move(100, 100);
		System.out.println("x = " + c.x + " y = " + c.y);

		c.moveto(100, 100);
		System.out.println("x = " + c.x + " y = " + c.y);
	}

}
