package me.av306.argon;


import me.av306.argon.modules.SimpleAutoTool;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.av306.argon.events.MinecraftClientEvents;
import me.av306.argon.module.AbstractModule;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.modules.ModuleList;
import me.av306.argon.modules.movement.Timer;
import me.av306.argon.modules.render.ProximityRadar;
import me.av306.argon.util.KeybindUtil;
import me.av306.argon.util.text.TextFactory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class Argon implements ClientModInitializer, PreLaunchEntrypoint
{
    private static final Argon INSTANCE = new Argon();
    public static Argon getInstance()
    {
        return INSTANCE;
    }

    public static final String MOD_ID = "argon";
    public static final boolean debug = true;

    public static final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    public static final Formatting MESSAGE_FORMAT = Formatting.AQUA;
    public static final Formatting WARNING_FORMAT = Formatting.YELLOW;
    public static final Formatting ERROR_FORMAT = Formatting.RED;
    
    private static ModContainer MOD_CONTAINER;
    public static String VERSION_STRING;
    public static String getVersionString()
    {
        return VERSION_STRING;
    }

    private static final Text NAME_PREFIX =
            TextFactory.createLiteral( "[Argon] " ).formatted( MESSAGE_FORMAT );
    public static MutableText getNamePrefixCopy() { return NAME_PREFIX.copy(); }
    
    public static final Logger LOGGER = LoggerFactory.getLogger( MOD_ID );

    public KeyBinding modifierKey;

    public MinecraftClient client;
    //public MinecraftClientAccessor clientAccessor;

    public final HashMap<String, AbstractModule> moduleRegistry = new HashMap<>();
    public final ArrayList<AbstractToggleableModule> enabledModules = new ArrayList<>();

    //public HashMap<String, Command> commandRegistry = new HashMap<>();

    // The instance that the init methods are called on isn't guaranteed to be
    // the same, which is super annoying because I have instance stuff going on
    // I want to try something other than the enum singleton method, though
    // The init methods just call the singleton's methods

    @Override
    public void onPreLaunch()
    {
        INSTANCE.initPrelaunch();
        readVersionData();
    }

    @Override
    public void onInitializeClient()
    {

        // Call the singleton's init method
        INSTANCE.initClient();
    }


    private void initPrelaunch()
    {
        LOGGER.info( "Hello from Argon prelaunch init!" );
    }

    private void initClient()
    {
        LOGGER.info( "Hello from Argon client init!" );

        readVersionData();

        this.client = MinecraftClient.getInstance();

        this.modifierKey = KeybindUtil.registerKeybind(
                "modifier",
                GLFW.GLFW_KEY_UNKNOWN,
                "modules"
        );

        MinecraftClientEvents.DISCONNECT.register(
                (screen, transferring) -> this.disableAllFeatures() );

        // TODO: It just occured to me that these could be GC'ed
        // Although I've never seen it happen.
        // Hmm, maybe the event stuff keeps them alive...
        new ModuleList();
        new ProximityRadar();
        new Timer();
        //new SimpleAutoTool();
    }


    public void disableAllFeatures()
    {
        // FIXME: don't disable all when transferring?
        Argon.LOGGER.info( "Disconnected, disabling all features" );

        ListIterator<AbstractToggleableModule> enabledModulesIterator = this.enabledModules.listIterator();
        while ( enabledModulesIterator.hasNext() )
        {
            var module = enabledModulesIterator.next();
            //if ( module.)
            module.disable( java.util.Optional.of( enabledModulesIterator ) );
            //enabledModulesIterator.remove();
        }
    }


    // Mod Menu handles update checks for us :)
    private static void readVersionData()
    {
        MOD_CONTAINER = FabricLoader.getInstance().getModContainer( MOD_ID ).get();
        Version ver = MOD_CONTAINER.getMetadata().getVersion();
        VERSION_STRING = ver.getFriendlyString();
    }


    public static Identifier argonIdentifier( String path )
    {
        return Identifier.of( MOD_ID, path );
    }

    // #region Message-sending
    
    public void sendInfoMessage( String key )
    {
        Text finalText = NAME_PREFIX.copy()
                .append( TextFactory.createTranslatable( key ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendInfoMessage( String key, Object... args )
    {
        Text finalText = NAME_PREFIX.copy()
                .append( TextFactory.createTranslatable( key, args ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendInfoMessage( Text text )
    {
        Text finalText = NAME_PREFIX.copy()
                .append( text );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }*/

    public void sendWarningMessage( String key )
    {
        Text finalText = NAME_PREFIX.copy().append(
                TextFactory.createTranslatable( key ).formatted( WARNING_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendWarningMessage( String key, Object... args )
    {
        Text finalText = NAME_PREFIX.copy().append(
                TextFactory.createTranslatable( key, args )
                .formatted( WARNING_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendWarningMessage( Text text )
    {
        Text finalText = NAME_PREFIX.copy()
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
        Text finalText = NAME_PREFIX.copy() .append(
                TextFactory.createTranslatable( key )
                .formatted( ERROR_FORMAT ) );
        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    public void sendErrorMessage( String key, Object... args )
    {
        Text finalText = NAME_PREFIX.copy() .append(
                TextFactory.createTranslatable( key, args )
                .formatted( ERROR_FORMAT ) );

        try
        {
            this.client.player.sendMessage( finalText, false );
        }
        catch ( NullPointerException ignored ) {}
    }

    /*public void sendErrorMessage( Text text )
    {
        Text finalText = NAME_PREFIX.copy()
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

    // #endregion
}
