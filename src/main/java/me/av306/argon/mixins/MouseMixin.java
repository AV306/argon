package me.av306.argon.mixins;

import me.av306.argon.events.MouseEvents;

import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Mouse.class )
public class MouseMixin
{
    @Inject(
        at = @At( "TAIL" ),
        method = "onMouseScroll(JDD)V",
        cancellable = true
    )
    private void onOnMouseScroll( long windowHandle, double horizontal,
            double vertical, CallbackInfo ci )
    {
        ActionResult result = MouseEvents.SCROLL_END.invoker().onScroll( windowHandle, horizontal, vertical );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }
}
