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

    public static void drawTest( VertexConsumerProvider vertexConsumerProvider, MatrixStack matrices, float partialTicks,
                                   Vec3d pos, int color )
    {
        //VertexConsumerProvider.Immediate vertexConsumerProvider = getEntityVertexConsumers();
        RenderLayer layer = CustomRenderLayers.OVERLAY_LINE_STRIP;
        VertexConsumer buffer = vertexConsumerProvider.getBuffer( layer );

        buffer.vertex( (float) pos.x, (float) pos.y, (float) pos.z ).color( color );
        buffer.vertex( (float) pos.x + 5, (float) pos.y, (float) pos.z ).color( color );
        buffer.vertex( (float) pos.x + 5, (float) pos.y + 5, (float) pos.z ).color( color );
        buffer.vertex( (float) pos.x, (float) pos.y + 5, (float) pos.z ).color( color );
        buffer.vertex( (float) pos.x, (float) pos.y, (float) pos.z ).color( color );

        //vertexConsumerProvider.draw( layer );
    }

    public static void drawTest2( MatrixStack matrices, float partialTicks,
                                   Vec3d pos, int color )
    {
        drawTest( getEntityVertexConsumers(), matrices, partialTicks, pos, color );
    }

}
