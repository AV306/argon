package me.av306.argon.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class HandledScreenEvents
{
    public static final Event<CloseHandledScreenEvent> CLOSE = EventFactory.createArrayBacked(
            CloseHandledScreenEvent.class,
            () -> {},
            (listeners) -> () ->
            {
                for ( CloseHandledScreenEvent listener : listeners )
                    listener.onClose();
            }
    );

    public interface CloseHandledScreenEvent
    {
        void onClose();
    }
}
