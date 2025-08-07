package me.av306.argon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ChatOutputEvent
{
    Event<ChatOutputEvent> EVENT = EventFactory.createArrayBacked(
            ChatOutputEvent.class,
            (listeners) -> (message) ->
            {
                for ( ChatOutputEvent listener : listeners )
                {
                    ActionResult result = listener.interact( message );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( String message );
}
