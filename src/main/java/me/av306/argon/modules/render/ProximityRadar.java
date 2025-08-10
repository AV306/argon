package me.av306.argon.modules.render;

import me.av306.argon.Argon;
import me.av306.argon.module.AbstractToggleableModule;
import me.av306.argon.util.render.RenderHelper;
import me.av306.argon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class ProximityRadar extends AbstractToggleableModule
{
    private static final double MAX_POSSIBLE_DISTANCE = 1024; // FIXME: Math.max( Math.max( ProximityRadarGroup.playerRange, ProximityRadarGroup.hostileRange ), ProximityRadarGroup.itemRange );
    public static final StatusEffectInstance GLOW_EFFECT = new StatusEffectInstance( StatusEffects.GLOWING );
    private double closestDistance = MAX_POSSIBLE_DISTANCE;
    private EntityScanResult entityScanResult = EntityScanResult.NONE;

    public ProximityRadar()
    {
        super( "ProximityRadar", "proxradar", "pr" );

        // Perform entity scanning in the render thread
        // because they're gonna be rendered somewhere before this anyway,
        // and we're rendering stuff too
        /*GameRenderEvents.RENDER_WORLD.register(
                (tickDelta, limitTime, matrices) -> this.scanEntities( matrices )
        );*/
        WorldRenderEvents.BEFORE_ENTITIES.register( this::scanEntities );
    }

    @Override
    public void onEnable() {}


    @Override
    public void onDisable() {}

    private ActionResult scanEntities( WorldRenderContext context )
    {
        RenderHelper.drawTracer( context.matrixStack(), context.tickCounter().getDynamicDeltaTicks(),
                new Vec3d( 5, 5, 5 ), 0xFFFFFFFF, false );


        // Only run the scan if the feature is enabled and essential stuff isn't null
        if (
                this.isEnabled
                && Argon.INSTANCE.client.world != null
                && Argon.INSTANCE.client.player != null
        )
        {
            // Scan each entity
            Argon.INSTANCE.client.world.getEntities()
                    .forEach( (entity) -> this.handleEntity( context, entity ) );

            // Process the results and send the appropriate message
            Text text;
            switch ( entityScanResult )
            {
                case HOSTILE ->
                {
                    text = TextFactory.createTranslatable(
                            "text.argon.proximityradar.hostile",
                            Double.toString( closestDistance ).substring( 0, 3 )
                    ).formatted( Formatting.RED, Formatting.BOLD );

                    Argon.INSTANCE.client.player.sendMessage( text,  true );
                }

                case PLAYER ->
                {
                    text = TextFactory.createTranslatable(
                            "text.argon.proximityradar.player",
                            Double.toString( closestDistance ).substring( 0, 3 )
                    ).formatted( Formatting.RED, Formatting.BOLD );

                    Argon.INSTANCE.client.player.sendMessage( text,  true );
                }

                //case ITEM -> // Items don
            }

            // Reset the closest detected distance and entityScanResult
            this.entityScanResult = EntityScanResult.NONE;
            this.closestDistance = MAX_POSSIBLE_DISTANCE;
        }

        return ActionResult.PASS;
    }


    /**
     * Scans an entity to check its entityScanResult and distance from the player,
     * and render the box and tracer.
     * @param context: World render context
     * @param entity: Entity to scan
     */
    private void handleEntity( WorldRenderContext context, Entity entity )
    {
        // Calculate distance to entity
        Vec3d entityPos = entity.getPos();
        Vec3d clientPos = Argon.INSTANCE.client.player.getPos();
        double distance = entityPos.distanceTo( clientPos );

        if ( /*ProximityRadarGroup.detectPlayers &&*/ entity instanceof PlayerEntity
                && entity != Argon.INSTANCE.client.player
                /*&& distance < ProximityRadarGroup.playerRange*/
        )
        {
            // Player entity, not self

            if ( distance < this.closestDistance )
            {
                this.entityScanResult = EntityScanResult.PLAYER;
                this.closestDistance = distance;
            }

            //this.highlightEntity( entity );
        }
        else if ( /*ProximityRadarGroup.detectHostiles
                &&*/ (entity instanceof HostileEntity || entity instanceof EnderDragonEntity
                || entity instanceof EnderDragonPart || (entity instanceof Hoglin /*&& ProximityRadarGroup.detectHoglins*/))
                /*&& distance < ProximityRadarGroup.hostileRange*/
        )
        {
            ServerWorld serverWorld = Objects.requireNonNullElse( Argon.INSTANCE.client.player.getServer(), Argon.INSTANCE.client.getServer() )
                    .getWorld( Argon.INSTANCE.client.world.getRegistryKey() );



            // Skip neutral zombified piglins
            // FIXME: can't get this to work properly...
            /*if ( entity instanceof ZombifiedPiglinEntity zombifiedPiglin
                    //&& !zombifiedPiglin.isAngryAt( serverWorld, Argon.INSTANCE.client.player )
                    //&& zombifiedPiglin.getAngryAt() != Argon.INSTANCE.client.player.getUuid()
                    //&& Objects.requireNonNull( zombifiedPiglin.getTarget() ).getUuid() == Argon.INSTANCE.client.player.getUuid()
            )
            {
                if ( zombifiedPiglin.getTarget() != null )
                {
                    Argon.INSTANCE.LOGGER.info( "{} <=> {};", zombifiedPiglin.getTarget().getUuid(),
                            Argon.INSTANCE.client.player.getUuid()
                    );
                    if ( zombifiedPiglin.getTarget().getUuid() == Argon.INSTANCE.client.player.getUuid() )
                        return;
                }
                else Argon.INSTANCE.LOGGER.info( "target=null" );
            }*/

            if ( distance < this.closestDistance )
            {
                this.entityScanResult = EntityScanResult.HOSTILE;
                this.closestDistance = distance;
            }

            //this.highlightEntity( entity );
        }
        else if ( /*ProximityRadarGroup.detectItems &&*/ entity instanceof ItemEntity
                /*&& distance < ProximityRadarGroup.itemRange*/ )
        {
            // TODO

        }
        else if ( /*ProximityRadarGroup.detectProjectiles &&*/ entity instanceof ProjectileEntity
                /*&& distance < ProximityRadarGroup.projectileRange*/ )
        {
            // TODO
            //this.highlightEntity( entity );
        }
        //else this.deHighlightEntity( entity );
    }

    private void highlightEntity( Entity entity )
    {

        /*entity.setGlowing( true );
        if ( entity instanceof LivingEntity livingEntity )
        {
            //livingEntity.setGlowing( true );
            livingEntity.addStatusEffect( GLOW_EFFECT );
        }*/
        //Argon.INSTANCE.LOGGER.info( "{}", entity.isGlowing() || entity.isGlowingLocal() );
    }

    private void deHighlightEntity( Entity entity )
    {
        //if ( entity.isGlowing() || entity.isGlowingLocal() )
    }

    private enum EntityScanResult
    {
        HOSTILE,
        //NEUTRAL,
        //FRIENDLY,
        PLAYER,
        ITEM,
        NONE;
    }
}