package me.av306.argon;

import net.fabricmc.api.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer
{
    private boolean initialized = false;

    @Override
    public void onInitializeClient()
    {
        Argon.INSTANCE.LOGGER.info( "Hello Fabric world!" );

        if ( initialized )
            throw new RuntimeException( "Oh no! Xenon tried to initialise twice and this is very bad!!!" );
        else Argon.INSTANCE.initialise();

        this.initialized = true;
    }
}
