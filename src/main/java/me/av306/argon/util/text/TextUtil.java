package me.av306.argon.util.text;

import me.av306.argon.util.render.ScreenPosition;
import me.av306.argon.Argon;

import me.av306.argon.util.color.ColorUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Iterator;

/**
 * Helper class for text-related operations.
 * <p>
 * Contains methods for drawing text at a predefined position,
 * as well as appending lines to text.
 * <p>
 * TODO: implement BOTTOM_* text drawing
 */
public class TextUtil
{
    public static int MARGIN = 2;
    public static int LINE_SPACING = 2;
    public static int ONE_LINE_OFFSET = 12; // textRenderer.fontHeight + LINE_SPACING

    /**
     * Draw a set of alignment text
     */
    public static void drawTestText( DrawContext drawContext )
    {
        TextRenderer textRenderer = Argon.getInstance().client.textRenderer;
        int scaledWidth = Argon.getInstance().client.getWindow().getScaledWidth();
        int scaledHeight = Argon.getInstance().client.getWindow().getScaledHeight();

        // 0, 0
        drawContext.drawText( textRenderer, "(0, 0)", 0, 0, 0xFFFFFFFF, false );

        String centreText = "(" + scaledWidth/2 + ", " + scaledHeight/2 + ")";
        drawContext.drawText( textRenderer, centreText,
                (scaledWidth - textRenderer.getWidth( centreText )) / 2,
                (scaledHeight - textRenderer.fontHeight) / 2,
                0xFFFFFFFF, false );
        
        String bottomText = "(" + scaledWidth + ", " + scaledHeight + ")";
        drawContext.drawText( textRenderer, bottomText, scaledWidth - textRenderer.getWidth( bottomText ),
                scaledHeight - textRenderer.fontHeight, 0xFFFFFFFF, false );
    }

    // TODO: do we want to split the position up into top/centre/bottom, left/centre/right?
	public static void drawPositionedText(
            DrawContext context,
            Text text,
			ScreenPosition position,
            int xOffset, int yOffset,
            boolean shadow,
			int color
    )
	{
		TextRenderer textRenderer = Argon.getInstance().client.textRenderer;

		int scaledWidth = Argon.getInstance().client.getWindow().getScaledWidth();
		int scaledHeight = Argon.getInstance().client.getWindow().getScaledHeight();

		int x = MARGIN + xOffset, y = MARGIN + yOffset;

        // Text is anchored at its top left corner

		switch ( position )
        {
            case TOP_LEFT:
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;

            case TOP_RIGHT:
                x = scaledWidth - textRenderer.getWidth( text ) - MARGIN - xOffset;
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;

            case BOTTOM_LEFT:
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;

            case BOTTOM_RIGHT:
                x = scaledWidth - textRenderer.getWidth( text ) - MARGIN - xOffset;
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;


            case BOTTOM_CENTER:
                x = (scaledWidth - textRenderer.getWidth( text )) / 2;
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;

            case TOP_CENTER:
                x = (scaledWidth - textRenderer.getWidth( text )) / 2;
                context.drawText( textRenderer, text, x, y, color, shadow );
                break;
		}
	}

	public static void drawPositionedText(
			DrawContext context,
			Text text,
			ScreenPosition position,
			int xOffset, int yOffset,
			boolean shadow,
			Formatting formatting
	)
	{
		int color = 0;

		try
		{
			color = formatting.getColorValue() | 0xFF000000;
		}
		catch( NullPointerException npe )
		{
			color = 0xFFFFFFFF;
		}

		TextUtil.drawPositionedText( context, text, position, xOffset, yOffset, shadow, color );
	}

	public static void drawPositionedMultiLineText(
			DrawContext context,
			Text[] texts,
			ScreenPosition position,
			int xOffset, int yOffset,
			boolean shadow,
			int color
	)
	{
		// REMINDER:
		// Y is UP-DOWN offset
		// X is LEFT-RIGHT offset
		// I'm dumb

		TextRenderer textRenderer = Argon.getInstance().client.textRenderer;

		int scaledWidth = Argon.getInstance().client.getWindow().getScaledWidth();
		int scaledHeight = Argon.getInstance().client.getWindow().getScaledHeight();

		int x = MARGIN + xOffset, y = MARGIN + yOffset;

        switch ( position )
        {
            case TOP_LEFT:
                for ( Text text : texts )
                {
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y += textRenderer.fontHeight + LINE_SPACING;
                }
                break;

            case TOP_RIGHT:
                for ( Text text : texts )
                {
                    x = scaledWidth - textRenderer.getWidth( text ) - MARGIN - xOffset;
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y += textRenderer.fontHeight + LINE_SPACING;
                }
                break;

            case BOTTOM_LEFT:
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                for ( Text text : texts )
                {
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y -= textRenderer.fontHeight + LINE_SPACING;
                }
                break;

            case BOTTOM_RIGHT:
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                for ( Text text : texts )
                {
                    x = scaledWidth - textRenderer.getWidth( text ) - MARGIN - xOffset;
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y -= textRenderer.fontHeight + LINE_SPACING;
                }
                break;


            case BOTTOM_CENTER:
                y = scaledHeight - textRenderer.fontHeight - MARGIN - yOffset;
                for ( Text text : texts )
                {
                    x = (scaledWidth - textRenderer.getWidth( text )) / 2;
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y -= textRenderer.fontHeight + LINE_SPACING;
                }
                break;

            case TOP_CENTER:
                for ( Text text : texts )
                {
                    x = (scaledWidth - textRenderer.getWidth( text )) / 2;
                    context.drawText( textRenderer, text, x, y, color, shadow );
                    y += textRenderer.fontHeight + LINE_SPACING;
                }
                break;
        }
	}

	public static void drawPositionedMultiLineText(
			DrawContext context,
			Text[] texts,
			ScreenPosition position,
			int xOffset, int yOffset,
			boolean shadow,
			Formatting formatting
	)
	{
		int color = 0;

		try
		{
			color = formatting.getColorValue();
		}
		catch( NullPointerException npe )
		{
			color = ColorUtil.WHITE;
		}

		TextUtil.drawPositionedMultiLineText(
				context,
				texts,
				position,
				xOffset, yOffset,
				shadow,
				color
		);
	}

    public static void drawPositionedTexts(
        DrawContext drawContext,
        Iterator<String> texts,
        ScreenPosition position,
        int xOffset, int yOffset,
        boolean shadow,
        int color
    )
    {
        TextRenderer textRenderer = Argon.getInstance().client.textRenderer;
        int x = switch ( position )
        {
            case TOP_LEFT, BOTTOM_LEFT -> (MARGIN + xOffset);
            case TOP_RIGHT, BOTTOM_RIGHT -> (Argon.getInstance().client.getWindow().getScaledWidth() - MARGIN - xOffset);
            default -> 0;
        };

        int y = switch ( position )
        {
            case TOP_LEFT, TOP_RIGHT -> (MARGIN + yOffset);
            case BOTTOM_LEFT, BOTTOM_RIGHT -> (Argon.getInstance().client.getWindow().getScaledHeight() - MARGIN - yOffset);
            default -> 0;
        };
                
        while ( texts.hasNext() )
        {
            switch ( position )
            {
                case TOP_LEFT ->
                {
                    //Argon.LOGGER.info( "text at = ({}, {})", x, y );
                    drawContext.drawText( textRenderer, texts.next(), x, y,
                            color, shadow );
                    
                    y += textRenderer.fontHeight + LINE_SPACING;
                }

                case TOP_RIGHT ->
                {
                    String text = texts.next();
                    int startX = x - textRenderer.getWidth( text );
                    //Argon.LOGGER.info( "text at = ({}, {})", x, y );
                    drawContext.drawText( textRenderer, text, startX, y,
                            color, shadow );
                    y += textRenderer.fontHeight + LINE_SPACING;
                    
                }

                case BOTTOM_LEFT ->
                {
                    //Argon.LOGGER.info( "text at = ({}, {})", x, y );
                    drawContext.drawText( textRenderer, texts.next(), x, y,
                            color, shadow );
                    
                    y -= textRenderer.fontHeight + LINE_SPACING;
                }

                case BOTTOM_RIGHT ->
                {
                    String text = texts.next();
                    int startX = x - textRenderer.getWidth( text );
                    //Argon.LOGGER.info( "text at = ({}, {})", x, y );
                    drawContext.drawText( textRenderer, texts.next(), startX, y,
                            color, shadow );
                    y -= textRenderer.fontHeight + LINE_SPACING;
                }

                default -> Argon.LOGGER.error( "TextUtil.drawPositionedTexts() called with position " + position.toString() + " that is not implemented yet!" );
            }
        }
    }
}