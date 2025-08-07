package me.av306.argon.features.render;

import me.av306.argon.Argon;
import me.av306.argon.config.feature.render.TakePanoramaGroup;
import me.av306.argon.feature.IFeature;
import me.av306.argon.util.text.TextFactory;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;

public class TakePanoramaFeature extends IFeature
{
	
    //private static int resolution = 512;
    //public static int getResolution() { return resolution; }
    //public static void setResolution( int res ) { resolution = res; }

    public TakePanoramaFeature() { super( "TakePanorama" ); } // Not strictly needed
	
    @Override
    public void onEnable()
    {
        Argon.INSTANCE.LOGGER.info( "pano" );
        File runDir = Argon.INSTANCE.client.runDirectory;
        File panoramaFile = new File( runDir, "screenshots" );

        final int resolution = TakePanoramaGroup.resolution;

        // A little bit of info:
        // `runDirectory` refers to the root of the gamedir,
        // and through a bunch of nested func calls,
        // it saves in the screenshots subdir.
        // So we pass the root gamedir.
        Argon.INSTANCE.client.takePanorama( runDir, resolution, resolution );

        Text linkToPanoramas = TextFactory.createLiteral( panoramaFile.getName() + File.separator + "panorama0.png" )
                .formatted( Formatting.UNDERLINE )
                .styled(
                        style -> style.withClickEvent(
                                new ClickEvent( ClickEvent.Action.OPEN_FILE, panoramaFile.getAbsolutePath() )
                        )
                )
		.formatted(Argon.INSTANCE.SUCCESS_FORMAT );

        this.sendInfoMessage(  "text.argon.takepanorama.success", linkToPanoramas );
    }
}
