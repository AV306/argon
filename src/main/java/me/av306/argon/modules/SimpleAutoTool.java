package me.av306.argon.modules;

import com.jcraft.jorbis.Block;
import me.av306.argon.Argon;
import me.av306.argon.events.HandledScreenEvents;
import me.av306.argon.module.AbstractModule;
import me.av306.argon.module.AbstractToggleableModule;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SimpleAutoTool extends AbstractModule
{

    public SimpleAutoTool()
    {
        super( "SimpleAutoTool", "autotool", "tool", "at" );
    }

    @Override
    protected void onEnable()
    {
        /*if ( this.inventoryDirty )
        {
            // Build inventory graph
            this.toolItemStackReferences = S
            this.inventoryDirty = false;
        }*/

        MinecraftClient client = Argon.getInstance().client;
        PlayerInventory inventory = client.player.getInventory();
        Stream<ItemStack> items = StreamSupport.stream(
                        inventory.spliterator(), true )
                .filter( itemStack -> itemStack.getComponents().contains( DataComponentTypes.TOOL ) );

        HitResult hitResult = Argon.getInstance().client.crosshairTarget;
        switch ( hitResult.getType() )
        {
            case BLOCK ->
            {
                BlockHitResult blockHit = (BlockHitResult) hitResult;
                BlockState blockState = client.player.getWorld()
                        .getBlockState( blockHit.getBlockPos() );

                // TODO: isCorrectForDrops uses no instance fields whatsoever... why is it an instance method???

                // Filter correct tools
                items = items.filter(
                        itemStack ->  itemStack.getItem().isCorrectForDrops( itemStack, blockState ) );

                // Filter for fastest-mining tool
                ItemStack fastestTool = items.max( (itemStack1, itemStack2) ->
                {
                    float speed1 = itemStack1.getItem().getMiningSpeed( itemStack1, blockState );
                    float speed2 = itemStack2.getItem().getMiningSpeed( itemStack2, blockState );
                    Argon.LOGGER.info( "{}, {}", speed1, speed2 );
                    float diff = speed1 - speed2;

                    if ( diff < 0 ) return -1;
                    if ( diff > 0 ) return 1;
                    else return 0;
                } ).orElseGet( inventory::getSelectedStack );

                int currentSlot = inventory.getSelectedSlot();
                int sourceSlot = inventory.getSlotWithStack( fastestTool ); // May be -1
                if ( sourceSlot == -1 ) sourceSlot = currentSlot;

                client.interactionManager.clickSlot(
                        client.player.playerScreenHandler.syncId,
                        sourceSlot,
                        currentSlot,
                        SlotActionType.SWAP,
                        client.player
                );

                Argon.LOGGER.info( "src: {}; dst: {}, tool: {}", sourceSlot, currentSlot, fastestTool.getName().toString() );
            }

            case ENTITY ->
            {

            }

            case null, default -> {}
        }
    }
}
