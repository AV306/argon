package me.av306.argon.commands;

import me.av306.argon.Argon;
import me.av306.argon.command.Command;

public class RegisterCommand extends Command
{
    public RegisterCommand() { super( "register", "reg" ); }

    @Override
    public void execute( String[] args )
    {
        try
        {
            Class<?> c = Class.forName( args[0] );

            c.getDeclaredConstructor().newInstance();
        }
        catch ( NullPointerException npe )
        {
            this.sendErrorMessage( "text.argon.command.notenoughargs", args.length, 1 );
        }
        catch ( ClassNotFoundException cnfe )
        {
            this.sendErrorMessage( "text.argon.command.register.classnotfound", args[0] );
        }
        catch ( Exception e )
        {
            this.sendErrorMessage( "text.argon.register.nosuchmethod", args[0] );
        }
    }
}
