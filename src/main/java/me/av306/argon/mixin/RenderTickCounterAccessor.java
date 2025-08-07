package me.av306.argon.mixin;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// confusing interface injection
// fabric's way of ducktyping?
@Mixin( RenderTickCounter.class )
public interface RenderTickCounterAccessor
{
    @Accessor
    float getLastFrameDuration();

    @Accessor( "lastFrameDuration" )
    void setLastFrameDuration( float duration );

    @Accessor
    long getPrevTimeMillis();
    
    @Accessor( "prevTimeMillis" )
    void setPrevTimeMillis( long timeMillis );
}
