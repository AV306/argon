package me.av306.argon.mixins;

import me.av306.argon.events.RenderTickEvents;
import net.minecraft.client.render.RenderTickCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( RenderTickCounter.Dynamic.class )
public class DynamicRenderTickCounterMixin
{
    @Shadow
    private float dynamicDeltaTicks;

    @Inject(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            ),
            method = "beginRenderTick(J)I"
    )
    public void onBeginRenderTick( long timeMillis, CallbackInfoReturnable<Integer> cir )
    {
        this.dynamicDeltaTicks *= RenderTickEvents.BEGIN_RENDER_TICK.invoker().interact( timeMillis );
    }
}
