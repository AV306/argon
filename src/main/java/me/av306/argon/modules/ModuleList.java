package me.av306.argon.modules;

import java.util.Iterator;

import me.av306.argon.Argon;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.util.render.ScreenPosition;
import me.av306.argon.util.text.TextFactory;
import me.av306.argon.util.text.TextUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class ModuleList extends AbstractToggleableModule
{
    private Text versionText;

    public ModuleList()
    {
        // Set name
        super( "ModuleList" );

        // Hide FL from the list and don't disable it on exit
        this.setShouldHide( true );
        //this.setPersistent( true );
        this.reEnableOnWorldEnter = true;

        // Start enabled by default
        this.enable();

        // Version data should already be initialised at this time
        this.versionText = TextFactory.createTranslatable(
                "text.argon.version", Argon.getInstance().getVersionString()
        );

        // register render listener
        //HudRenderCallback.EVENT.register( this::onInGameHudRender );
        HudElementRegistry.addLast( Argon.getInstance().argonIdentifier( "argon_hud" ), this::onInGameHudRender );

        // FIXME: this event may not be good
        ClientPlayConnectionEvents.JOIN.register( (handler, sender, client) ->
        {
            //if ( FeatureListGroup.reEnableOnWorldEnter )
            this.enable();
        } );
    }

    private void onInGameHudRender( DrawContext drawContext, RenderTickCounter tickCounter )
    {
        if ( !this.isEnabled ) return;

        final boolean shouldShowVersion = true; //FeatureListGroup.showVersion;
        final ScreenPosition position = ScreenPosition.TOP_RIGHT; //FeatureListGroup.position;

        // Version text
        TextUtil.drawPositionedText( drawContext, this.versionText, position,
                0, 0, false, Argon.SUCCESS_FORMAT );

        Iterator<String> moduleNames = Argon.getInstance().enabledModules.stream()
                .parallel()
                .filter( module -> module.shouldHideFromModuleList() )
                .map( module -> module.getName() )
                .iterator();

        // Module names
        TextUtil.drawPositionedTexts( drawContext, moduleNames, position,
            0, 0, false, Argon.SUCCESS_FORMAT );
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
