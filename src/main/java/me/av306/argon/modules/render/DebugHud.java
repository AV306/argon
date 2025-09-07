package me.av306.argon.modules.render;

import me.av306.argon.Argon;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.util.render.AnchorPoint;
import me.av306.argon.util.text.DecimalFormats;
import me.av306.argon.util.text.TextUtil;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DebugHud extends AbstractToggleableModule
{
    public DebugHud()
    {
        super( "DebugHud", "hud" );

        HudElementRegistry.addLast( Argon.argonIdentifier( "debug_hud" ), this::renderHudElement );
    }

    private void renderHudElement( DrawContext drawContext, RenderTickCounter renderTickCounter )
    {
        if ( !this.isEnabled ) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        // FIXME: the velocity values are off by quite a lot, somehow
        TextUtil.drawPositionedText(
                drawContext,
                Text.translatable( "text.argon.infohud.player_velocity",
                        DecimalFormats.THREE_DP.format( player.getVelocity().horizontalLength() ),
                        DecimalFormats.THREE_DP.format( player.getVelocity().length() ) ),
                AnchorPoint.LEFT, AnchorPoint.CENTER,
                0, 0,
                false,
                0xFFFFFFFF
        );

        if ( player.hasVehicle() )
        {
            TextUtil.drawPositionedText(
                    drawContext,
                    Text.translatable( "text.argon.infohud.vehicle_velocity",
                            DecimalFormats.THREE_DP.format( player.getVehicle().getVelocity().horizontalLength() ),
                            DecimalFormats.THREE_DP.format( player.getVehicle().getVelocity().length() ) ),
                    AnchorPoint.LEFT, AnchorPoint.CENTER,
                    0, TextUtil.MARGIN + MinecraftClient.getInstance().textRenderer.fontHeight,
                    false,
                    0xFFFFFFFF
            );
        }
    }

    @Override
    protected void onEnable()
    {

    }

    @Override
    protected void onDisable()
    {

    }

}
