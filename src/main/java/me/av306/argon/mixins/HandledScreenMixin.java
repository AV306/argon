package me.av306.argon.mixins;

import me.av306.argon.events.HandledScreenEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( HandledScreen.class )
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen
        implements ScreenHandlerProvider<T>
{
    protected HandledScreenMixin( Text title )
    {
        super( title );
    }

    @Inject( at = @At( "RETURN" ), method = "close" )
    public void onClose( CallbackInfo ci )
    {
        HandledScreenEvents.CLOSE.invoker().onClose();
    }
}
