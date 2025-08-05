package me.av306.xenon.mixin;

import me.av306.xenon.event.ClientWorldEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientWorld.class )
public class ClientWorldMixin
{
	@Inject(
			at = @At( "HEAD" ),
			method = "disconnect(Lnet/minecraft/text/Text)V"
	)
	private void onDisconnect( Text reasonText, CallbackInfo ci )
	{
		// MinecraftClient#disconnect() is less reliable for this
		// FIXME: update event to receive reason text?
		ClientWorldEvents.DISCONNECT.invoker().onDisconnect();
	}
}
