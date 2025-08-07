package me.av306.argon.config.feature;

import me.av306.argon.config.XenonConfigGroup;
import me.av306.argon.util.render.ScreenPosition;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class FeatureListGroup implements XenonConfigGroup
{
	@ConfigEntry.Boolean
    public static boolean reEnableOnWorldEnter = true;
	
	public static boolean showVersion = true;

	public static boolean shadow = false;

	@ConfigEntry.Dropdown
	public static ScreenPosition position = ScreenPosition.TOP_RIGHT;
}