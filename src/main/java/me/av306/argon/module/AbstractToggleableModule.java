package me.av306.argon.module;

import me.av306.argon.Argon;
import me.av306.argon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;

public abstract class AbstractToggleableModule extends AbstractModule
{
	/**
 	* Text to display when the feature is enabled. Should not change after initialisation,
  	*/
    protected final Text enabledText;
	
	/**
 	 * Text to display when the feature is disabled. Should not change after initialisation,
     */
    protected final Text disabledText;

	/**
 	 * Whether the feature is enabled.
     */
    protected boolean isEnabled = false;

    /**
	 * Whether this feature should be disabled on exit
	 */
	@Deprecated protected boolean persistent = false;
	@Deprecated public void setPersistent( boolean persistent ) { this.persistent = persistent; }
	@Deprecated public boolean isPersistent() { return this.persistent; }

    /**
     * Re-enable module when joining a world?
     * Replaces {@link #persistent} so that all features can be disabled
     * when not in a world
     */
    protected boolean reEnableOnWorldEnter = false;
	public boolean shouldReEnableOnWorldEnter()
	{
		return this.reEnableOnWorldEnter;
	}

    //protected int key = GLFW.GLFW_KEY_UNKNOWN;

    //protected static IToggleableFeature instance;

	protected AbstractToggleableModule( String name )
    {
        this( name, GLFW.GLFW_KEY_UNKNOWN );
    }

    protected AbstractToggleableModule( String name, String... aliases )
    {
        this( name, GLFW.GLFW_KEY_UNKNOWN, aliases );
    }

    protected AbstractToggleableModule( String name, int key )
    {
        super( name, key );
		
		this.enabledText = TextFactory.createTranslatable( "text.argon.toggleablemodule.enabled", name )
                .formatted( Argon.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.argon.toggleablemodule.disabled", name )
                .formatted( Argon.ERROR_FORMAT );

        this.commandBuilder.then( literal( "disable" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "d" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "toggle" ).executes( context -> { this.toggle(); return 1; } ) );
        this.commandBuilder.then( literal( "t" ).executes( context -> { this.toggle(); return 1; } ) );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register( this.commandBuilder )
        );
    }

    protected AbstractToggleableModule( String name, int key, String... aliases )
    {
        super( name, key, aliases );

		this.enabledText = TextFactory.createTranslatable( "text.argon.itoggleablefeature.enabled", name )
                .formatted( Argon.SUCCESS_FORMAT );
        this.disabledText = TextFactory.createTranslatable( "text.argon.itoggleablefeature.disabled", name )
                .formatted( Argon.SUCCESS_FORMAT );

        // We're registering the things like thrice, but it works, so I'm not complaining
        // FIXME: the time has come. I must complain
        this.commandBuilder.then( literal( "disable" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "d" ).executes( context -> { this.disable(); return 1; } ) );
        this.commandBuilder.then( literal( "toggle" ).executes( context -> { this.toggle(); return 1; } ) );
        this.commandBuilder.then( literal( "t" ).executes( context -> { this.toggle(); return 1; } ) );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register( this.commandBuilder )
        );
    }

    /*@Override
    protected ActionResult onKeyboardKey( long window, int key, int scanCode, int action, int modifiers )
    {
        if ( action == GLFW.GLFW_PRESS && key == this.key )
            toggle();

        return ActionResult.PASS;
    }*/

    @Override
    protected void keyEvent()
    {
        while ( this.keyBinding.wasPressed() && !Argon.getInstance().modifierKey.isPressed() )
        {
            if ( this.forceDisabled )
            {
                Argon.getInstance().LOGGER.warn( "Force-disabled feature enabled: {}", this.name );
                Argon.getInstance().sendErrorMessage( "text.argon.ifeature.blocked", this.name );
            }
        
            else this.toggle();
        }
    }

    @Override
    public void enable()
    {
        this.enable( Optional.empty() );
    }

    public void enable( Optional<ListIterator<AbstractToggleableModule>> optionalListIterator )
    {
        if ( this.isEnabled ) return; // safety

        this.isEnabled = true;

        Argon.LOGGER.info( "{} enabled!", this.getName() );

        // If an iterator is available, i.e. the caller is iterating through some list
        // containing ATMs, modify that iterator to prevent concurrent modification
        // Otherwise add it to the enabled modules list normallt

        optionalListIterator.ifPresentOrElse(
                iterator -> iterator.add( this ),
                () -> Argon.getInstance().enabledModules.add( this )
        );

        try
        {
            Argon.getInstance().client.player.sendMessage( this.enabledText, true );
        }
        catch ( NullPointerException ignored ) {} // Features that start enabled will try to send this message and fail

        onEnable();
    }

    public void disable()
    {
        this.disable( Optional.empty() );
    }

    public void disable( Optional<ListIterator<AbstractToggleableModule>> listIterator )
    {
        if ( !this.isEnabled ) return; // safety catch

        this.isEnabled = false;

        Argon.LOGGER.info( "{} disabled!", this.getName() );

        listIterator.ifPresentOrElse(
            iterator -> iterator.remove(),
            () -> Argon.getInstance().enabledModules.remove( this )
        );

        try
        {
            Argon.getInstance().client.player.sendMessage( this.disabledText, true );
        }
        catch ( NullPointerException ignored ) {}

        onDisable();
    }
    
    protected abstract void onDisable();

    public void toggle()
    {
        if ( this.isEnabled ) disable();
        else enable();
    }
}
