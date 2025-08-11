package me.av306.argon.modules;

import me.av306.argon.Argon;
import me.av306.argon.config.feature.FeatureListGroup;
import me.av306.argon.event.RenderInGameHudEvent;
import me.av306.argon.event.MinecraftClientEvents;
import me.av306.argon.feature.IFeature;
import me.av306.argon.feature.IToggleableFeature;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.util.render.ScreenPosition;
import me.av306.argon.util.color.ColorUtil;
import me.av306.argon.util.text.TextFactory;
import me.av306.argon.util.text.TextUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class ArgonHud extends AbstractToggleableModule
{
    private final Text versionText;

    public ArgonHud()
    {
        // Set name
        super( "ArgonHud" );

        // Hide FL from the list and don't disable it on exit
        this.setShouldHide( true );
        this.setPersistent( true );

        // Start enabled by default
        this.enable();

        // Version data should already be initialised at this time
        this.versionText = TextFactory.createTranslatable(
                "text.argon.version", Argon.INSTANCE.getVersionString()
        );

        // register render listener
        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );

        .JOIN_WORLD.register( world ->
        {
            if ( FeatureListGroup.reEnableOnWorldEnter )
                this.enable();
            return ActionResult.PASS;
        } );
    }

    private ActionResult onInGameHudRender( DrawContext drawContext, float tickDelta )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        final boolean shouldShowVersion = FeatureListGroup.showVersion;
        final ScreenPosition position = FeatureListGroup.position;

        // Initialise an empty AL that will contain the feature names
        ArrayList<Text> nameTexts = new ArrayList<>();

        // Now, begin drawing text

        // should we draw the version name?
        if ( shouldShowVersion )
            TextUtil.drawPositionedText(
                    drawContext,
                    this.versionText,
                    position,
                    0, 0,
                    FeatureListGroup.shadow,
                    Argon.INSTANCE.SUCCESS_FORMAT
            );

        // Place the feature names to be drawn in an AL
        // then convert it into a normal array.
        // THis is done because the logic for drawing text on
        // multiple lines at a given ScreenPosition
        // is hidden in TextUtil.drawPositionedMultiLineText,
        // and we don't need to reinvent the motor car here.
        for ( IFeature feature : Argon.INSTANCE.enabledFeatures )
        {
            // hide FeatureList itself and Debbuger
            if ( !feature.getShouldHide() )
            {
                Text nameText = TextFactory.createLiteral( feature.getName() );
                nameTexts.add( nameText );
            }
        }

        // Begin drawing the feature names from the array.
        // remember to leave space for the version text!
        TextUtil.drawPositionedMultiLineText(
                drawContext,
                nameTexts.toArray( Text[]::new ),
                position,
                0, 12,
                FeatureListGroup.shadow,
                ColorUtil.WHITE
        );

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
}
