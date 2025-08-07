package me.av306.argon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatInputEvent
{
    Event<ChatInputEvent> EVENT = EventFactory.createArrayBacked(
            ChatInputEvent.class,
            (listeners) -> (message) ->
            {
                for ( ChatInputEvent listener : listeners )
                {
                    ActionResult result = listener.interact( message );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( Text message );
}
