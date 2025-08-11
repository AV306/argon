package me.av306.argon.events;

import me.av306.argon.events.RenderTickEvents.BeginRenderTickEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.gui.screen.Screen;

public class MinecraftClientEvents
{
    public static final Event<JoinWorldEvent> JOIN_WORLD = EventFactory.createArrayBacked(
            JoinWorldEvent.class,
            (clientWorld, entryReason) -> {},
            (listeners) -> (clientWorld, entryReason) ->
            {
                for ( JoinWorldEvent listener : listeners )
                    listener.onJoinWorld( clientWorld, entryReason );
            }
    );

    public static final Event<DisconnectEvent> DISCONNECT = EventFactory.createArrayBacked(
            DisconnectEvent.class,
            (screen, isTransferring) -> {},
            (listeners) -> (screen, isTransferring) ->
            {
                for ( DisconnectEvent listener : listeners )
                    listener.onDisconnect( screen, isTransferring );
            }
    );

    public interface JoinWorldEvent
    {
        void onJoinWorld( ClientWorld world,
                DownloadingTerrainScreen.WorldEntryReason worldEntryReason );
    }

    public interface DisconnectEvent
    {
        void onDisconnect( Screen screen, boolean isTransferring );
    }
}
