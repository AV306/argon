package me.av306.argon.util.render;

import java.util.OptionalDouble;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;

public class CustomRenderLayers
{
    /**
     * Render layer for lines, main target, position/colour
     */
    public static final RenderLayer.MultiPhase OVERLAY_LINES = RenderLayer.MultiPhase.of(
            "argon:layer/overlay_lines",
            1536,
            CustomRenderPipelines.OVERLAY_LINES_PIPELINE,
            RenderLayer.MultiPhaseParameters.builder()
                    .lineWidth( new RenderPhase.LineWidth( OptionalDouble.of( 2 ) ) )
                    .layering( RenderPhase.VIEW_OFFSET_Z_LAYERING )
                    .target( RenderPhase.MAIN_TARGET )
                    .build( false )
    );
    
    public static final RenderLayer.MultiPhase OVERLAY_LINE_STRIP = RenderLayer.MultiPhase.of(
            "argon:layer/overlay_line_strip",
            1536,
            CustomRenderPipelines.OVERLAY_LINE_STRIP_PIPELINE,
            RenderLayer.MultiPhaseParameters.builder()
                    .lineWidth( new RenderPhase.LineWidth( OptionalDouble.of( 2 ) ) )
                    .layering( RenderPhase.VIEW_OFFSET_Z_LAYERING )
                    .target( RenderPhase.MAIN_TARGET )
                    .build( false )
    );
}