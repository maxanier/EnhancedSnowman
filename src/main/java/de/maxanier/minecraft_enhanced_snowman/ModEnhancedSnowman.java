package de.maxanier.minecraft_enhanced_snowman;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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
        if (Configs.COMMON.convert.get() && event.getSource().getEntity() instanceof SnowGolemEntity) {
            if (Configs.COMMON.convert_chance.get() > Math.random()) {
                SnowGolemEntity snowman = new SnowGolemEntity(EntityType.SNOW_GOLEM, event.getEntityLiving().getCommandSenderWorld());
                snowman.copyPosition(event.getEntityLiving());
                event.getEntityLiving().getCommandSenderWorld().addFreshEntity(snowman);
            }
        }
    }

    @SubscribeEvent
    public void onLivingBaseAttack(LivingAttackEvent event) {
        if (event.getAmount() == 0.0F && event.getSource().getDirectEntity() instanceof SnowballEntity) {
            if (event.getEntityLiving().getCommandSenderWorld().isClientSide) return;
            if (event.getSource().getEntity() instanceof SnowGolemEntity || (Configs.COMMON.playersDealDamage.get() && event.getSource().getEntity() instanceof PlayerEntity)) {
                if (event.getEntityLiving() instanceof IMob || !Configs.COMMON.onlyHostile.get()) {
                    SnowballEntity ball = (SnowballEntity) event.getSource().getDirectEntity();
                    if (!ball.getPersistentData().contains("dealt_damage")) {
                        ball.getPersistentData().putBoolean("dealt_damage", true);
                        event.getEntityLiving()
                                .hurt(
                                        new IndirectEntityDamageSource("thrown", event.getSource().getDirectEntity(), event.getSource().getEntity()), Configs.COMMON.snowballDamage.get().floatValue());
                        if (Configs.COMMON.slowness.get()) {
                            event.getEntityLiving().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 1));
                        }
                    }
                }
            }
        }
    }
}
