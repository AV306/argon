package me.av306.argon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public class MouseEvents
{
    public static final Event<ScrollEvent> SCROLL_END = EventFactory.createArrayBacked(
            ScrollEvent.class,
            (windowHandle, horizontalScroll, verticalScroll) -> ActionResult.PASS,
            (listeners) -> (windowHandle, horizontalScroll, verticalScroll) ->
            {
                ActionResult result = ActionResult.PASS;
                for ( ScrollEvent listener : listeners )
                {
                    ActionResult intermediate =
                            listener.onScroll( windowHandle, horizontalScroll, verticalScroll );
                    result = intermediate;
                    if ( intermediate != ActionResult.PASS ) break;
                }

                return result;
            }
    );

    public interface ScrollEvent
    {
        ActionResult onScroll( long windowHandle, double horizontalScroll, double verticalScroll );
    }

}
