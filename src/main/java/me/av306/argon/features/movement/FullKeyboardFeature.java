package me.av306.argon.features.movement;

import me.av306.argon.Argon;
import me.av306.argon.config.feature.movement.FullKeyboardGroup;
import me.av306.argon.feature.IToggleableFeature;
import me.av306.argon.mixin.MouseAccessor;
import me.av306.argon.mixinterface.IMouse;
import me.av306.argon.util.KeybindUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import org.lwjgl.glfw.GLFW;

public final class FullKeyboardFeature extends IToggleableFeature
{
    private final KeyBinding upKey;
    private final KeyBinding downKey;
    private final KeyBinding leftKey;
    private final KeyBinding rightKey;
    private final KeyBinding virtualLeftMouseKey;
    private final KeyBinding virtualRightMouseKey;

    public FullKeyboardFeature()
    {
        super( "FullKeyboard", "fullkey", "fullkb", "fkb" );

        // Register extra keys
        this.virtualLeftMouseKey = KeybindUtil.registerKeybind(
            "fullkeyboard.virtualLeftMouseKey",
            GLFW.GLFW_KEY_COMMA,
            this.category
        );

        this.virtualRightMouseKey = KeybindUtil.registerKeybind(
            "fullkeyboard.virtualRightMouseKey",
            GLFW.GLFW_KEY_PERIOD,
            this.category
        );

        this.upKey = KeybindUtil.registerKeybind(
            "fullkeyboard.upKey",
            GLFW.GLFW_KEY_UP,
            this.category
        );

        this.downKey = KeybindUtil.registerKeybind(
            "fullkeyboard.downKey",
            GLFW.GLFW_KEY_DOWN,
            this.category
        );

        this.leftKey = KeybindUtil.registerKeybind(
            "fullkeyboard.leftKey",
            GLFW.GLFW_KEY_LEFT,
            this.category
        );

        this.rightKey = KeybindUtil.registerKeybind(
            "fullkeyboard.rightKey",
            GLFW.GLFW_KEY_RIGHT,
            this.category
        );
        
        // Register double-bindings
        ClientTickEvents.START_CLIENT_TICK.register( (client) -> this.onEndTick() );
    }

    private void onEndTick()
    {
        if ( !this.isEnabled ) return;

        // Left mouse stuff
        boolean leftMousePressed = this.virtualLeftMouseKey.isPressed();
        // Arm swing

        // Attack
        if ( leftMousePressed )
        {
            if ( Argon.INSTANCE.client.crosshairTarget.getType() == HitResult.Type.ENTITY )
            {
                Entity e = ((EntityHitResult) Argon.INSTANCE.client.crosshairTarget).getEntity();
                //Xenon.INSTANCE.client.player.attack( entityHitResult.getEntity() );
                Argon.INSTANCE.client.interactionManager.attackEntity(Argon.INSTANCE.client.player, e );
            }

            Argon.INSTANCE.client.player.swingHand( Hand.MAIN_HAND );
        }

        Argon.INSTANCE.client.options.attackKey.setPressed( this.virtualLeftMouseKey.isPressed() );
        Argon.INSTANCE.client.options.useKey.setPressed( this.virtualRightMouseKey.isPressed() );
        
        double fac = Argon.INSTANCE.client.options.getMouseSensitivity().getValue() * FullKeyboardGroup.sensitivity;

        // The cursor will be unlocked when in a GUI or chat, so when this happens
        // we need to modify the cursor position directly
        // Movement stuff
        if ( Argon.INSTANCE.client.mouse.isCursorLocked() )
            // Gameplay; use deltas
            this.modifyMouseDelta( fac, FullKeyboardGroup.acceleration );
        else
            // This works, but doesn't update the actual cursor position
            this.modifyMousePos( fac );
    }

    /**
     * Modify the mouse delta, so that the arrow keys control look angle.
     * Note: Mouse deltas are only used when the cursor is locked; i.e. when "actually playing"
     * @param f: Delta of the delta
     * @param accelerate: Make the delta change at an increasing rate. Not sure if this actually does anything
     */
    private void modifyMouseDelta( double f, boolean accelerate )
    {
        if ( Argon.INSTANCE.client.options.sprintKey.isPressed() )
        {
            // View flick
            assert Argon.INSTANCE.client.player != null;

            if ( this.upKey.wasPressed() ) Argon.INSTANCE.client.player.setPitch(Argon.INSTANCE.client.player.getPitch() - 90 );
            if ( this.downKey.wasPressed() ) Argon.INSTANCE.client.player.setPitch(Argon.INSTANCE.client.player.getPitch() + 90 );
            if ( this.leftKey.wasPressed() ) Argon.INSTANCE.client.player.setYaw(Argon.INSTANCE.client.player.getYaw() - 90 );
            if ( this.rightKey.wasPressed() ) Argon.INSTANCE.client.player.setYaw(Argon.INSTANCE.client.player.getYaw() + 90 );
        }
        else if ( !accelerate )
        {
            // Linear movement
            if ( this.upKey.isPressed() ) ((MouseAccessor) Argon.INSTANCE.client.mouse).setCursorDeltaY( -f );
            if ( this.downKey.isPressed() ) ((MouseAccessor) Argon.INSTANCE.client.mouse).setCursorDeltaY( f );
            if ( this.leftKey.isPressed() ) ((MouseAccessor) Argon.INSTANCE.client.mouse).setCursorDeltaX( -f );
            if ( this.rightKey.isPressed() ) ((MouseAccessor) Argon.INSTANCE.client.mouse).setCursorDeltaX( f );
        }
        else
        {
            if ( this.upKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).accelerateDeltaY( -f );
            if ( this.downKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).accelerateDeltaY( f );
            if ( this.leftKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).accelerateDeltaX( -f );
            if ( this.rightKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).accelerateDeltaX( f );
        }
    }

    /**
     * Modify the actual mouse position.
     * Note: Only used when the cursor is unlocked; i.e. anytime you can see the cursor, like in GUIs and chat
     * @param f: Delta delta
     */
    private void modifyMousePos( double f )
    {
        // Linear movement
        if ( this.upKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).changeY( -f );
        if ( this.downKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).changeY( f );
        if ( this.leftKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).changeX( -f );
        if ( this.rightKey.isPressed() ) ((IMouse) Argon.INSTANCE.client.mouse).changeX( f );
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