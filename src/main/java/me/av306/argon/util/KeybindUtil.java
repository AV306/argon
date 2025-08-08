package me.av306.argon.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindUtil
{
    public static KeyBinding registerKeybind( String translation, int key, String category )
    {
        KeyBinding kb = new KeyBinding(
                "key.argon." + translation,
                InputUtil.Type.KEYSYM,
                key,
                "category.argon." + category
        );
        KeyBindingHelper.registerKeyBinding( kb );

        return kb;
    }
}