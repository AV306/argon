package me.av306.argon;

import java.util.ArrayList;
import java.util.HashMap;

import me.av306.argon.modules.render.ProximityRadar;
import me.av306.argon.util.KeybindUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.av306.argon.module.AbstractModule;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.util.text.TextFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;

//#endregion

// FIXME: not sure about the suitability of the enum singleton pattern
public enum Argon
{
    INSTANCE;

    public final String MODID = "argon";
	
    public final boolean debug = true;
	
    public final Logger LOGGER = LoggerFactory.getLogger( this.MODID );

    public MinecraftClient client;

    public KeyBinding modifierKey;

    //public MinecraftClientAccessor clientAccessor;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    public final Formatting MESSAGE_FORMAT = Formatting.AQUA;
    public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    public final Formatting ERROR_FORMAT = Formatting.RED;

    // This is most likely going to be used to resolve a feature
    // by its name (e.g. CommandProcessor),
    // so I put it in this order.
    public final HashMap<String, AbstractModule> moduleRegistry = new HashMap<>();
    public final ArrayList<AbstractToggleableModule> enabledModules = new ArrayList<>();

    //public HashMap<String, Command> commandRegistry = new HashMap<>();

    public ModContainer modContainer;
    public String versionString;

    public void preLaunchInit()
    {
        System.out.println( "Hello from Argon prelaunch init!" );
    }

    public void clientInit()
    {
        LOGGER.info( "Hello from Argon client init!" );

        this.client = MinecraftClient.getInstance();

        this.modifierKey = KeybindUtil.registerKeybind(
                "modifier",
                GLFW.GLFW_KEY_UNKNOWN,
                "modules"
        );

        new ProximityRadar();
    }

    private final Text namePrefix = TextFactory.createLiteral( "[Xenon] " )
            .formatted( this.MESSAGE_FORMAT );

    public MutableText getNamePrefixCopy() { return namePrefix.copy(); }

    public void disableAllFeatures()
    {
        // FIXME: This feels very inefficient
        Argon.INSTANCE.LOGGER.info( "Exiting world, disabling all features" );

        ArrayList<AbstractToggleableModule> enabledModules_copy = new ArrayList<>( this.enabledModules );
        for ( AbstractToggleableModule module : enabledModules_copy )
            if ( !module.isPersistent() ) module.disable();

        // Remove restrictions
        for ( AbstractModule module : this.moduleRegistry.values() )
            module .setForceDisabled( false );
    }


    // Mod Menu handles update checks for us :)
    private void readVersionData()
    {
        //assert FabricLoader.getInstance().getModContainer( "xenon" ).isPresent();
        this.modContainer = FabricLoader.getInstance().getModContainer( "xenon" ).get();
        // Get version string
        Version ver = modContainer.getMetadata().getVersion();
        this.versionString = ver.getFriendlyString();
    }


    
    public void sendInfoMessage( String key )
    {
        Text finalText = namePrefix.copy()
                .append( TextFactory.createTranslatable( key ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendInfoMessage( String key, Object... args )
    {
        Text finalText = namePrefix.copy()
                .append( TextFactory.createTranslatable( key, args ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendInfoMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append( text );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }*/

    public void sendWarningMessage( String key )
    {
        Text finalText = namePrefix.copy().append(
                TextFactory.createTranslatable( key ).formatted( this.MESSAGE_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendWarningMessage( String key, Object... args )
    {
        Text finalText = namePrefix.copy().append(
                TextFactory.createTranslatable( key, args )
                .formatted( this.WARNING_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendWarningMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append(
                        text.copyContentOnly()
                                .formatted( this.MESSAGE_FORMAT )
                );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }*/

    public void sendErrorMessage( String key )
    {
        Text finalText = namePrefix.copy() .append(
                TextFactory.createTranslatable( key )
                .formatted( this.ERROR_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendErrorMessage( String key, Object... args )
    {
        Text finalText = namePrefix.copy() .append(
                TextFactory.createTranslatable( key, args )
                .formatted( this.ERROR_FORMAT ) );

        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendErrorMessage( Text text )
    {
        Text finalText = namePrefix.copy()
                .append(
                        text.copyContentOnly()
                                .formatted( this.ERROR_FORMAT )
                );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }*/

}
