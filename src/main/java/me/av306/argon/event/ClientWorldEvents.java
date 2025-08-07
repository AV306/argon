package me.av306.argon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ClientWorldEvents
{
    Event<Disconnect> DISCONNECT = EventFactory.createArrayBacked(
            Disconnect.class,
            (listeners) -> () ->
            {
                for ( Disconnect listener : listeners )
                    listener.onDisconnect();
            }
    );

    @FunctionalInterface
    interface Disconnect
    {
        void onDisconnect();
    }
}