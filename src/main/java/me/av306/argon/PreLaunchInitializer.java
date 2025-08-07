package me.av306.argon;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunchInitializer implements PreLaunchEntrypoint
{
    private boolean initialized = false;
    @Override
    public void onPreLaunch()
    {
        //Argon.INSTANCE.LOGGER.info( "Beginning pre-launch initialisation!" );
        if ( initialized )
            throw new RuntimeException(
                "Oh no! Argon pre-launch tried to initialise twice and this is very bad!!!" );
        //else Argon.INSTANCE.preLaunchInitialise();
        else Argon.INSTANCE.preLaunchInit();

        this.initialized = true;
    }
}
