package me.av306.argon.modules.movement;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.av306.argon.events.RenderTickEvents;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.Argon;
//import me.av306.argon.config.feature.movement.TimerGroup;
//import me.av306.argon.events.ScrollInHotbarEvent;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.util.ActionResult;

import java.util.Optional;
import java.util.stream.Stream;

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
        if ( this.isEnabled && Argon.getInstance().modifierKey.isPressed() && this.keyBinding.isPressed() )
        {
            //this.timerFactor += ((int) amount) * TimerGroup.adjustmentInterval;
            //if ( this.timerFactor < 0 ) this.timerFactor = 0; // Negative values break everything
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    @Override
    protected Optional<Stream<? extends ArgumentBuilder<FabricClientCommandSource, ? extends Object>>> getConfigCommandArguments()
    {
        return Optional.of( Stream.of(
                ClientCommandManager.literal( "speed" ).then(
                        ClientCommandManager.argument( "speed", FloatArgumentType.floatArg( 0.1F ) )
                                .executes( context ->
                                {
                                    this.timerFactor = FloatArgumentType.getFloat( context, "speed" );
                                    return 1;
                                } )
                )

        ) );
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
            RenderTickCounter renderTickCounter = ((MinecraftClientAccessor) Argon.getInstance().client).getRenderTickCounter();

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
