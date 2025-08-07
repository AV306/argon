package me.av306.argon.features.accessibility;

import me.av306.argon.event.EntityRendererEvents;
import me.av306.argon.event.MobEntityRendererEvents;
import me.av306.argon.feature.IToggleableFeature;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public class LabelFeature extends IToggleableFeature
{
	public LabelFeature()
	{
		super( "Labels", "label", "lb" );

		EntityRendererEvents.GET_HAS_LABEL.register( this::onEntityRendererGetHasLabel );
		MobEntityRendererEvents.GET_HAS_LABEL.register( this::onEntityRendererGetHasLabel );
	}

	private ActionResult onEntityRendererGetHasLabel( Entity entity )
	{
		return this.isEnabled ? ActionResult.SUCCESS : ActionResult.PASS;
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
