package me.av306.argon.features.chat;

import me.av306.argon.Argon;
import me.av306.argon.feature.IFeature;
import me.av306.argon.util.text.TextFactory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class ShareLocationFeature extends IFeature
{
	public ShareLocationFeature() { super( "ShareLocation" ); }


	@Override
	protected void onEnable()
	{
		// get the player's current position
		assert Argon.INSTANCE.client.player != null;
		Vec3d currentPos = Argon.INSTANCE.client.player.getPos();

		// get the player's current dimension
		//assert Xenon.INSTANCE.client.world != null;
		//String dim = Xenon.INSTANCE.client.world.getChunk( Xenon.INSTANCE.client.player.getBlockPos()).bi

		// round and display
		String loc = String.format("[%d %d %d](%s)",
				Math.round( currentPos.getX() ),
				Math.round( currentPos.getY() ),
				Math.round( currentPos.getZ() ),
				RegistryKeys.toDimensionKey(Argon.INSTANCE.client.world.getRegistryKey() ).getValue().toString()
		);

		Argon.INSTANCE.client.getNetworkHandler().sendChatMessage( loc );
	}
}