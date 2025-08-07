package me.av306.argon.commands;

import me.av306.argon.Argon;
import me.av306.argon.command.Command;
import me.av306.argon.feature.IFeature;
import me.av306.argon.util.KeybindUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.input.Input;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class AliasCommand extends Command
{
    public AliasCommand()
    {
        super( "alias" );
    }

    @Override
	public void execute( String[] args )
	{
		// args[0] is feature name, args[1] is new alias
        try
        {
            IFeature feature = Argon.INSTANCE.featureRegistry.get( args[0] );
            String alias = args[1];

            Argon.INSTANCE.featureRegistry.put( alias, feature );

            this.sendInfoMessage( "text.argon.command.alias.success", args[0] );
        }
        catch ( NullPointerException npe )
        {
            this.sendErrorMessage( "text.argon.command.unresolvable", this.name, args[0] );
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            this.sendErrorMessage( "text.argon.command.notenoughargs", this.name, args.length, 2 );
        }
	}
}
