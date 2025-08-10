package me.av306.argon.util.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class CustomRenderPipelines
{
    public static final RenderPipeline.Snippet FOGLESS_LINES_SNIPPET =
            RenderPipeline.builder( RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET )
                    .withVertexShader( Identifier.of( "argon:core/fogless_lines" ) )
                    .withFragmentShader(Identifier.of("argon:core/fogless_lines"))
                    .withBlend( BlendFunction.TRANSLUCENT )
                    .withCull( false )
                    .withVertexFormat( VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.LINES )
                    .buildSnippet();

    /**
     * Render pipeline for lines, main target, position/colour
     */
    public static final RenderPipeline OVERLAY_LINES = RenderPipelines.register(
            RenderPipeline.builder( FOGLESS_LINES_SNIPPET )
                    .withLocation( Identifier.of( "argon:pipeline/overlay_lines" ) )
                    .withDepthTestFunction( DepthTestFunction.NO_DEPTH_TEST )
                    .build() );

    public static final RenderPipeline OVERLAY_LINE_STRIP = RenderPipelines.register(
            RenderPipeline.builder( FOGLESS_LINES_SNIPPET )
                    .withLocation( Identifier.of("argon:pipeline/overlay_line_strip" ) )
                    .withVertexFormat( VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINE_STRIP )
                    .withDepthTestFunction( DepthTestFunction.NO_DEPTH_TEST )
                    .build() );
}
