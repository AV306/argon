package me.av306.argon.features.chat;

import me.av306.argon.Argon;
import me.av306.argon.config.feature.chat.QuickChatGroup;
import me.av306.argon.feature.IFeature;

public class QuickChatFeature extends IFeature
{
	public QuickChatFeature()
	{
		super( "QuickChat" );
  }
	
    @Override
    public void onEnable()
    {
        assert Argon.INSTANCE.client.player != null;

        // Send it as a command if the message starts with a "/"
        if ( QuickChatGroup.message.startsWith( "/" ) )
            Argon.INSTANCE.client.getNetworkHandler().sendChatCommand( QuickChatGroup.message.replace( "/", "" ) );
        else Argon.INSTANCE.client.getNetworkHandler().sendChatMessage( QuickChatGroup.message );
    }

    @Override
    public boolean onRequestConfigChange( String config, String value )
    {
        boolean result = config.contains( "message" ) || config.contains( "msg" );

        if ( result ) QuickChatGroup.message = value;

        return result;
    }
}
