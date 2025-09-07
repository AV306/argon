package me.av306.argon.util.text;

import java.text.DecimalFormat;

// Why doesn't Java have this?
public class DecimalFormats
{
    public static final DecimalFormat TWO_DP = new DecimalFormat( "#.##" );
    public static final DecimalFormat THREE_DP = new DecimalFormat( "#.###" );
}
