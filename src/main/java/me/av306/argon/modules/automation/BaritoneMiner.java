package me.av306.argon.modules.automation;

import me.av306.argon.Argon;
import me.av306.argon.events.HandledScreenEvents;
import me.av306.argon.module.AbstractModule;
import me.av306.argon.module.AbstractToggleableModule;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BaritoneMiner extends AbstractToggleableModule
{
    private static final int INVENTORY_CHECK_COOLDOWN_TICKS = 5 * 20;
    private int inventoryCheckCooldownCounter = 0;

    public BaritoneMiner()
    {
        super( "BaritoneMiner", "autominer" );

        ClientTickEvents.END_CLIENT_TICK.register( this::tick );

        // TODO: hook tool damage event so that we can get baritone to stop when all tools break
    }

    private boolean wasteStoneFilter( ItemStack itemStack )
    {
        Item item = itemStack.getItem();
        return (itemStack.isIn( ItemTags.STONE_TOOL_MATERIALS ) // Cobbled stuff + blackstone
                || itemStack.isIn( ItemTags.DIRT )
                || item == Items.DIORITE
                || item == Items.ANDESITE
                || item == Items.GRANITE
                || item == Items.TUFF)
                && itemStack.getCount() > 32; // More than half a stack of it
    }

    private void tick( MinecraftClient client )
    {
        if ( !this.isEnabled ) return;

        // TODO: use RenderTickCounter?
        this.inventoryCheckCooldownCounter++;

        if ( this.inventoryCheckCooldownCounter >= INVENTORY_CHECK_COOLDOWN_TICKS )
        {
            // Remove all stacks of building materials
            PlayerInventory inventory = client.player.getInventory();
            StreamSupport.stream( inventory.spliterator(), true )
                    .filter( this::wasteStoneFilter )
                    .forEach( itemStack ->
                    {
                        Argon.LOGGER.info( "Dropping {}", itemStack.getName().toString() );
                        //client.player.dropItem( itemStack, false );

                        inventory.setSelectedStack( itemStack );
                        inventory.dropSelectedItem( true );
                    } );

            this.inventoryCheckCooldownCounter -= INVENTORY_CHECK_COOLDOWN_TICKS;
        }
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
