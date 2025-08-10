package me.av306.argon.util.render;

import me.av306.argon.Argon;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class RenderHelper
{
    public static VertexConsumerProvider.Immediate getEntityVertexConsumers()
    {
        return Argon.INSTANCE.client.getBufferBuilders().getEntityVertexConsumers();
    }

    public static void drawTracer( MatrixStack matrices, float partialTicks,
                                   Vec3d end, int color, boolean depthTest )
    {
        VertexConsumerProvider.Immediate vertexConsumers = getEntityVertexConsumers();
        RenderLayer layer = CustomRenderLayers.OVERLAY_LINES;
        VertexConsumer buffer = vertexConsumers.getBuffer( layer );

        Vec3d start = new Vec3d( 0, 0, 1 );

        buffer.vertex( (float) start.x, (float) start.y, (float) start.z ).color( 0xFFFFFFFF );
        buffer.vertex( (float) end.x, (float) end.y, (float) end.z ).color( 0xFFFFFFFF );

        vertexConsumers.draw( layer );
    }
}
