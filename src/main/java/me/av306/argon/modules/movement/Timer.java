package me.av306.argon.modules.movement;

import me.av306.argon.events.RenderTickEvents;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.Argon;
//import me.av306.argon.config.feature.movement.TimerGroup;
//import me.av306.argon.events.ScrollInHotbarEvent;
import net.minecraft.util.ActionResult;

public class Timer extends AbstractToggleableModule
{
    // FIXME: temporary config until I get YACL in
    public float timerFactor = 2;

    public Timer()
    {
        super( "Timer" );

        RenderTickEvents.BEGIN_RENDER_TICK.register( this::onBeginRenderTick );

        //ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        if ( this.isEnabled && Argon.INSTANCE.modifierKey.isPressed() && this.keyBinding.isPressed() )
        {
            //this.timerFactor += ((int) amount) * TimerGroup.adjustmentInterval;
            //if ( this.timerFactor < 0 ) this.timerFactor = 0; // Negative values break everything
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent()
    {
        super.keyEvent();
    }

    @Override
    public String getName()
    {
        // when the name is retrieved, update it and return
        this.setName( "Timer (" + this.timerFactor + ")" );

        return this.name;
    }

    private float onBeginRenderTick( long timeMillis )
    {
        /*if ( this.isEnabled )
        {
            // TODO: does below method still work?
            RenderTickCounter renderTickCounter = ((MinecraftClientAccessor) Argon.INSTANCE.client).getRenderTickCounter();

            // Get the duration of the last frame
            float lastFrameDurationLocal = ((RenderTickCounterAccessor) renderTickCounter).getLastFrameDuration();

            // Make it seem longer than it was to force Minecraft to try to run everything faster
            lastFrameDurationLocal *= this.timerFactor;
            ((RenderTickCounterAccessor) renderTickCounter).setLastFrameDuration( lastFrameDurationLocal );
        }*/

        return this.isEnabled ? this.timerFactor : 1;
    }

    @Override
    protected void onEnable()
    {
        this.setName( "Timer (" + this.timerFactor + ")" );
    }

    @Override
    protected void onDisable()
    {

    }

    @Override
    public boolean onRequestConfigChange(String config, String value )
    {
        boolean result = config.contains( "speed" );

        if ( result ) this.timerFactor = Float.parseFloat( value );

        return result;
    }
}
