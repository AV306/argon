package me.av306.argon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public class MouseEvents
{
    public static final Event<ScrollEvent> SCROLL_END = EventFactory.createArrayBacked(
            ScrollEvent.class,
            (windowHandle, horizontalAmount, verticalAmount) -> ActionResult.PASS,
            (listeners) -> (windowHandle, horizontalAmount, verticalAmount) ->
            {
                for ( ScrollEvent listener : listeners )
                {
                    var result = listener.onScroll( windowHandle, horizontalAmount, verticalAmount );
                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    public interface ScrollEvent
    {
        ActionResult onScroll( long windowHandle, double horizontalAmount, double verticalAmount );
    }
}
