package me.av306.argon.util.color;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for converting rgb arrays to int values, and vice versa.
 */
public class ColorUtil
{
	// AARRGGBB
	public static final int WHITE = 0xFFFFFFFF;
	public static final int BLACK = 0xFF000000;
	public static final int RED = 0xFFFF0000;
	public static final int GREEN = 0xFF00FF00;
	public static final int BLUE = 0xFF0000FF;
	
	public static int rgbToInt( int r, int g, int b ) { return BLACK | r << 16 | g << 8 | b; }

  	public static int argbToInt( int a, int r, int g, int b )
    {
      return a << 24 | r << 16 | g << 8 | b;
    }

	@Contract(value = "_ -> new", pure = true)
	public static int @NotNull [] intToRgb(int rgb )
	{
		return new int[]{ rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF };
	}

	@Contract(value = "_ -> new", pure = true)
	public static int @NotNull [] intToArgb(int argb )
	{
		return new int[]{ argb >> 24 & 0xFF, argb >> 16 & 0xFF, argb >> 8 & 0xFF, argb & 0xFF };
	}
}