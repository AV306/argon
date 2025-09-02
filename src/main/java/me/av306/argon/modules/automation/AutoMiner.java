package me.av306.argon.modules.automation;

//import baritone.api.BaritoneAPI;
//import baritone.api.IBaritone;
import me.av306.argon.Argon;
import me.av306.argon.module.AbstractToggleableModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class AutoMiner extends AbstractToggleableModule
{
    private static final int INVENTORY_CHECK_COOLDOWN_TICKS = 5 * 20;
    private int inventoryCheckCooldownCounter = 0;

    private static final int TOOL_DAMAGE_THRESHOLD = 5;

    private final boolean hasBaritone;

    public AutoMiner()
    {
        super( "AutoMiner", "autominer" );

        ClientTickEvents.END_CLIENT_TICK.register( this::tick );

        // TODO: hook tool damage event so that we can get baritone to stop when all tools break

        this.hasBaritone = FabricLoader.getInstance()
                .getModContainer( "baritone" ).isPresent();
    }

    private static boolean shouldDiscard( ItemStack itemStack )
    {
        Item item = itemStack.getItem();
        return ( //itemStack.isIn( ItemTags.STONE_TOOL_MATERIALS ) // Cobbled stuff + blackstone
                //|| itemStack.isIn( ItemTags.DIRT )
                item == Items.COBBLESTONE
                || item == Items.COBBLED_DEEPSLATE
                || item == Items.DIRT
                || item == Items.DIORITE
                || item == Items.ANDESITE
                || item == Items.GRANITE
                || item == Items.GRAVEL
                || item == Items.TUFF
                || item == Items.BONE
                || item == Items.WHEAT_SEEDS
                || item == Items.NETHERRACK
                || item == Items.ROTTEN_FLESH );
                //&& itemStack.getCount() > 32; // More than half a stack of it
    }

    private void tick( MinecraftClient client )
    {
        if ( !this.isEnabled ) return;

        // TODO: use RenderTickCounter? or generally better scheduling
        //Argon.LOGGER.info( "dynamic dticks = {}", client.getRenderTickCounter().getDynamicDeltaTicks() );
        this.inventoryCheckCooldownCounter++;

        // In PlayerInventory, slots are 0-indexed starting from the leftmost hotbar slot,
        // then continuing LTR from the topmost inventory row
        // This is different from the packet indexing which differes between screens
        // (see https://minecraft.wiki/w/Java_Edition_protocol/Inventory)
        if ( this.inventoryCheckCooldownCounter >= INVENTORY_CHECK_COOLDOWN_TICKS )
        {
            PlayerInventory inventory = client.player.getInventory();
            //var interactionManager = client.interactionManager;

            // Clear byproducts
            boolean hasAtLeastOneStack = false;
            for ( ItemStack itemStack : inventory )
            {
                //Argon.LOGGER.info( "checking slot {} ({})", inventory.getSlotWithStack( itemStack ), c++ );
                // Argon.LOGGER.info( "{} in slot {}", itemStack.getName().getString(),
                //         inventory.getSlotWithStack( itemStack ) );
                
                if ( shouldDiscard( itemStack ) )
                {
                    if ( !hasAtLeastOneStack ) hasAtLeastOneStack = true;

                    else
                    {
                        Argon.LOGGER.info( "Discarding {}", itemStack.getItemName().getString() );
                        client.interactionManager.clickSlot(
                                client.player.playerScreenHandler.syncId, // = 0
                                inventory.getSlotWithStack( itemStack ),
                                1,
                                SlotActionType.THROW,
                                client.player
                        );
                    }
                }
            }

            ItemStack currentToolStack = client.player.getMainHandStack();

            if ( currentToolStack.getComponents().contains( DataComponentTypes.TOOL )
                    && currentToolStack.getMaxDamage() - currentToolStack.getDamage() <= TOOL_DAMAGE_THRESHOLD )
            {
                // Signal Baritone to pause, if it's enabled
                client.player.sendMessage(
                        Text.translatable( "text.argon.autominer.criticaldurability" )
                                .formatted( Argon.WARNING_FORMAT ),
                        true
                );

                if ( this.hasBaritone )
                {
                    client.player.networkHandler.sendChatMessage( "#pause" );

                    //Argon.LOGGER.info( "Baritone present" );
                    /*IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

                    if ( baritone.getMineProcess().isActive() )
                    {
                        baritone.getMineProcess().cancel();
                        //baritone.getCommandManager().execute( "pause" );
                        client.player.sendMessage(
                            Text.translatable( "text.argon.autominer.baritone.pausing" )
                                    .formatted( Argon.WARNING_FORMAT ),
                            false );
                    }*/
                }
                // else Argon.LOGGER.info( "Baritone absent" );
            }


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
