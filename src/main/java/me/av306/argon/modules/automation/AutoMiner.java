package me.av306.argon.modules.automation;

import java.util.stream.StreamSupport;

import me.av306.argon.Argon;
import me.av306.argon.module.AbstractToggleableModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class AutoMiner extends AbstractToggleableModule
{
    private static final int INVENTORY_CHECK_COOLDOWN_TICKS = 5 * 20;
    private int inventoryCheckCooldownCounter = 0;

    public AutoMiner()
    {
        super( "BaritoneMiner", "autominer" );

        ClientTickEvents.END_CLIENT_TICK.register( this::tick );

        // TODO: hook tool damage event so that we can get baritone to stop when all tools break
    }

    private static boolean isWaste( ItemStack itemStack )
    {
        Item item = itemStack.getItem();
        return (itemStack.isIn( ItemTags.STONE_TOOL_MATERIALS ) // Cobbled stuff + blackstone
                || itemStack.isIn( ItemTags.DIRT )
                || item == Items.DIORITE
                || item == Items.ANDESITE
                || item == Items.GRANITE
                || item == Items.TUFF);
                //&& itemStack.getCount() > 32; // More than half a stack of it
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
            //var interactionManager = client.interactionManager;

             // Only carries 0, 1, or 2; roughly tracks how many stacks of
             // mining waste we have so that we can keep one
            boolean hasSpareBlocks = false;
            for ( ItemStack itemStack : inventory )
            {
                if ( isWaste( itemStack ) )
                {
                    if ( hasSpareBlocks )
                    {
                        // Throw away
                        // FIXME: This seems to ignore blocks in the hotbar... which may actually be useful
                        //Argon.LOGGER.info( "discarding {}", itemStack.getName().toString() );
                        this.sendInfoMessage( Text.literal( "Clearing inventory" ) );
                        this.sendInfoMessage( Text.literal( "Discarding " + itemStack.getName().toString() ) );
                        client.interactionManager.clickSlot(
                                client.player.playerScreenHandler.syncId, // = 0
                                inventory.getSlotWithStack( itemStack ),
                                1,
                                SlotActionType.THROW,
                                client.player
                        );
                        //byproductsLeft--;
                        // Don't need to update the counter past 2 stacks
                    }
                    else hasSpareBlocks = true;
                }
            }

            // StreamSupport.stream( inventory.spliterator(), true )
            //         .filter( this::wasteStoneFilter )
            //         .forEach( itemStack ->
            //         {
            //             Argon.LOGGER.info( "Dropping {}", itemStack.getName().toString() );
            //             //client.player.dropItem( itemStack, false );

            //             inventory.setSelectedStack( itemStack );
            //             inventory.dropSelectedItem( true );
            //         } );

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
