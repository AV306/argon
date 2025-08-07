package me.av306.argon;

import java.util.ArrayList;
import java.util.HashMap;

//#region Imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.av306.argon.module.Module;
import me.av306.argon.module.ToggleableModule;
import net.minecraft.util.Formatting;

//#endregion

// FIXME: not sure about the suitability of the enum singleton pattern
public enum Argon
{
    INSTANCE;

    public final String MODID = "argon";
	
    public final boolean debug = true;
	
    public final Logger LOGGER = LoggerFactory.getLogger( this.MODID );

    public MinecraftClient client;

    //public MinecraftClientAccessor clientAccessor;

    public final Formatting SUCCESS_FORMAT = Formatting.GREEN;
    public final Formatting MESSAGE_FORMAT = Formatting.AQUA;
    public final Formatting WARNING_FORMAT = Formatting.YELLOW;
    public final Formatting ERROR_FORMAT = Formatting.RED;

    // This is most likely going to be used to resolve a feature
    // by its name (e.g. CommandProcessor),
    // so I put it in this order.
    public final HashMap<String, Module> featureRegistry = new HashMap<>();
    public final ArrayList<ToggleableModule> enabledFeatures = new ArrayList<>();

    //public HashMap<String, Command> commandRegistry = new HashMap<>();


    public void preLaunchInit()
    {
        System.out.println( "Hello from Argon prelaunch init!" );
    }

    public void clientInit()
    {
        System.out.println( "Hello from Argon client init!" );
    }
}
