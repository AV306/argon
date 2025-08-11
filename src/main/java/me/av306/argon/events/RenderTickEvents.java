package me.av306.argon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;


public class RenderTickEvents
{
    public static final Event<BeginRenderTickEvent> BEGIN_RENDER_TICK = EventFactory.createArrayBacked(
            BeginRenderTickEvent.class,
            (timeMillis) -> 1,
            (listeners) -> (timeMillis) ->
            {
                float accumulatedScale = 1;
                for ( BeginRenderTickEvent listener : listeners )
                {
                    accumulatedScale *= listener.interact( timeMillis );
                }

                return accumulatedScale;
            }
    );

    public interface BeginRenderTickEvent
    {
        float interact( long timeMillis );
    }
}
