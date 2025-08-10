package me.av306.argon.util.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ScissorState;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import org.joml.Vector4f;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class SimpleRenderLayer extends RenderLayer
{
    private final RenderPipeline renderPipeline;

    public SimpleRenderLayer( String name, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload,
                              RenderPipeline renderPipeline )
    {
        super( name, bufferSize, affectsCrumbling, sortOnUpload, () -> {}, () -> {} );
        this.renderPipeline = renderPipeline;
    }

    public static SimpleRenderLayer create( String name, int bufferSize, RenderPipeline renderPipeline )
    {
        return new SimpleRenderLayer( name, bufferSize, false, false, renderPipeline );
    }

    @Override
    public VertexFormat getVertexFormat()
    {
        return this.renderPipeline.getVertexFormat();
    }

    @Override
    public VertexFormat.DrawMode getDrawMode()
    {
        return this.renderPipeline.getVertexFormatMode();
    }

    @Override
    public void draw( final BuiltBuffer builtBuffer )
    {
        this.startDrawing();

        try
        {
            GpuBuffer vertexBuffer = this.renderPipeline.getVertexFormat()
                    .uploadImmediateVertexBuffer( builtBuffer.getBuffer() );
            GpuBuffer indexBuffer;
            VertexFormat.IndexType indexType;

            if ( builtBuffer.getSortedBuffer() == null )
            {
                RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(
                        builtBuffer.getDrawParameters().mode() );
                indexBuffer = shapeIndexBuffer.getIndexBuffer( builtBuffer.getDrawParameters().indexCount() );
                indexType = shapeIndexBuffer.getIndexType();
            }
            else
            {
                indexBuffer = this.renderPipeline.getVertexFormat()
                         .uploadImmediateIndexBuffer( builtBuffer.getSortedBuffer());
                indexType = builtBuffer.getDrawParameters().indexType();
            }

            Framebuffer renderTarget = RenderLayer.MAIN_TARGET.get(); // Single phase
            GpuTextureView colorTextureTarget = RenderSystem.outputColorTextureOverride != null
                    ? RenderSystem.outputColorTextureOverride
                    : renderTarget.getColorAttachmentView();

            GpuTextureView depthTextureTarget = renderTarget.useDepthAttachment
                    ? (RenderSystem.outputDepthTextureOverride != null
                            ? RenderSystem.outputDepthTextureOverride
                            : renderTarget.getDepthAttachmentView())
                    : null;

            GpuBufferSlice dynamicTransformsUbo = RenderSystem.getDynamicUniforms()
                    .write(
                            RenderSystem.getModelViewMatrix(),
                            new Vector4f( 1.0F, 1.0F, 1.0F, 1.0F ),
                            RenderSystem.getModelOffset(),
                            RenderSystem.getTextureMatrix(),
                            RenderSystem.getShaderLineWidth()
                    );

            try ( RenderPass renderPass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass( () -> "Immediate draw for " + this.getName(), colorTextureTarget,
                            OptionalInt.empty(), depthTextureTarget, OptionalDouble.empty() )
            )
            {
                renderPass.setPipeline( this.renderPipeline );
                ScissorState scissorState = RenderSystem.getScissorStateForRenderTypeDraws();
                if ( scissorState.method_72091() ) // Getter for `enabled`
                {
                    // This is ridiculous. TODO: submit mappings to Fabric
                    renderPass.enableScissor( scissorState.method_72092(), scissorState.method_72093(),
                            scissorState.method_72094(), scissorState.method_72095() );
                }

                RenderSystem.bindDefaultUniforms( renderPass );
                renderPass.setUniform( "DynamicTransforms", dynamicTransformsUbo );
                renderPass.setVertexBuffer( 0, vertexBuffer );

                for ( int i = 0; i < 12; ++i )
                {
                    GpuTextureView texture = RenderSystem.getShaderTexture( i );
                    if ( texture != null )
                        renderPass.bindSampler( "Sampler" + i, texture );
                }

                renderPass.setIndexBuffer( indexBuffer, indexType );
                renderPass.drawIndexed( 0, 0, builtBuffer.getDrawParameters().indexCount(),
                        1 );
            }
        } catch ( Throwable e )
        {
            try
            {
                builtBuffer.close();
            }
            catch ( Throwable e2 )
            {
                e.addSuppressed(e2);
            }
            throw e;
        }

        builtBuffer.close();

        this.endDrawing();
    }
}
