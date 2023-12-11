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

import java.awt.Color;

public class ColorCtrl {

	public static String getStringColor(Color color) {

		if (Color.BLACK == color)
			return "BLACK";
		if (Color.RED == color)
			return "RED";
		if (Color.BLUE == color)
			return "BLUE";
		if (Color.CYAN == color)
			return "CYAN";
		if (Color.DARK_GRAY == color)
			return "DARK_GRAY";
		if (Color.GRAY == color)
			return "GRAY";
		if (Color.GREEN == color)
			return "GREEN";
		if (Color.LIGHT_GRAY == color)
			return "LIGHT_GRAY";
		if (Color.MAGENTA == color)
			return "MAGENTA";
		if (Color.ORANGE == color)
			return "ORANGE";
		if (Color.PINK == color)
			return "PINK";
		if (Color.WHITE == color)
			return "WHITE";
		if (Color.YELLOW == color)
			return "YELLOW";

		if( color != null ) return String.valueOf(color.getRGB());

		return "NULL";
	}
}