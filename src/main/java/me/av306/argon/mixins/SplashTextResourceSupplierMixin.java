package me.av306.argon.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( SplashTextResourceSupplier.class )
public class SplashTextResourceSupplierMixin
{
    @Inject( at = @At( "HEAD" ), method = "get", cancellable = true )
    public void onGet( CallbackInfoReturnable<SplashTextRenderer> cir )
    {
        cir.setReturnValue( new SplashTextRenderer( "ARGON-40" ) );
    }
}
