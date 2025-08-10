package me.av306.argon.util.render;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderPhase;

import java.util.OptionalDouble;

public class CustomRenderLayers
{
    /**
     * Render layer for lines, main target, position/colour
     */
    public static final RenderLayer.MultiPhase OVERLAY_LINES = RenderLayer.MultiPhase.of(
            "argon:overlay_lines",
            1536,
            CustomRenderPipelines.OVERLAY_LINES,
            RenderLayer.MultiPhaseParameters.builder()
                    .lineWidth( new RenderPhase.LineWidth( OptionalDouble.of( 2 ) ) )
                    .layering( RenderPhase.VIEW_OFFSET_Z_LAYERING )
                    .target( RenderPhase.MAIN_TARGET )
                    .build( false )
    );
}