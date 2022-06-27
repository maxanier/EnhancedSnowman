package de.maxanier.minecraft_enhanced_snowman;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

@Mod(ModEnhancedSnowman.MOD_ID)
public class ModEnhancedSnowman {

    public static final String MOD_ID = "enhanced_snowman";



    public static ModEnhancedSnowman INSTANCE;

    public ModEnhancedSnowman() {
        INSTANCE = this;
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configs.confSpec);
        modEventBus.register(Configs.class);

        MinecraftForge.EVENT_BUS.register(this);

        Optional<? extends ModContainer> opt = ModList.get().getModContainerById("enhanced_snowman");
        if (opt.isPresent()) {

            org.apache.logging.log4j.LogManager.getLogger().info("Preparing Enhanced Snowman {}", opt.get().getModInfo().getVersion());
        } else {
            org.apache.logging.log4j.LogManager.getLogger().error("Somehow Enhanced Snowman could not be found");
        }


    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDeath(LivingDeathEvent event) {
        if (Configs.COMMON.convert.get() && event.getSource().getEntity() instanceof SnowGolem) {
            if (Configs.COMMON.convert_chance.get() > Math.random()) {
                SnowGolem snowman = new SnowGolem(EntityType.SNOW_GOLEM, event.getEntityLiving().getCommandSenderWorld());
                snowman.copyPosition(event.getEntityLiving());
                event.getEntityLiving().getCommandSenderWorld().addFreshEntity(snowman);
                event.getEntityLiving().deathTime = 19;
            }
        }
    }

    @SubscribeEvent
    public void onLivingBaseAttack(LivingAttackEvent event) {
        if (event.getAmount() == 0.0F && event.getSource().getDirectEntity() instanceof Snowball) {
            if (event.getEntityLiving().getCommandSenderWorld().isClientSide) return;
            if (event.getSource().getEntity() instanceof SnowGolem || (Configs.COMMON.playersDealDamage.get() && event.getSource().getEntity() instanceof Player)) {
                if (event.getEntityLiving() instanceof Enemy || !Configs.COMMON.onlyHostile.get()) {
                    Snowball ball = (Snowball) event.getSource().getDirectEntity();
                    if (!ball.getPersistentData().contains("dealt_damage")) {
                        ball.getPersistentData().putBoolean("dealt_damage", true);
                        event.getEntityLiving()
                                .hurt(
                                        new IndirectEntityDamageSource("thrown", event.getSource().getDirectEntity(), event.getSource().getEntity()), Configs.COMMON.snowballDamage.get().floatValue());
                        if (Configs.COMMON.slowness.get()) {
                            event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                        }
                    }
                }
            }
        }
        if(Configs.COMMON.disable_heat_damage.get() && event.getSource() == DamageSource.ON_FIRE && event.getEntityLiving().getType() == EntityType.SNOW_GOLEM){
            event.setCanceled(true);
        }
        if(Configs.COMMON.disable_water_damage.get() && event.getSource() == DamageSource.DROWN && event.getEntityLiving().getType() == EntityType.SNOW_GOLEM){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMobGriefing(EntityMobGriefingEvent event){
        if(Configs.COMMON.prevent_snow_trail.get() && event.getEntity() instanceof SnowGolem){
            event.setResult(Event.Result.DENY);
        }
    }
}
