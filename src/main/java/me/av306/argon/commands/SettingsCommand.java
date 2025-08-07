package me.av306.argon.commands;

import me.av306.argon.Argon;
import me.av306.argon.command.Command;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;

public class SettingsCommand extends Command
{
    public SettingsCommand()
    {
        super( "settings" );
    }
    
    @Override
    public void execute( String[] args )
    {
        /*Xenon.INSTANCE.client.setScreen(
            new OptionsScreen( Xenon.INSTANCE.client.currentScreen, Xenon.INSTANCE.client.options )
        );*/
        //Util.pause( "Pause" );
    }
}
