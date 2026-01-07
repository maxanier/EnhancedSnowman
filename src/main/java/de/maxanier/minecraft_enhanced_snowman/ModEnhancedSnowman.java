package de.maxanier.minecraft_enhanced_snowman;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Optional;

@Mod(ModEnhancedSnowman.MOD_ID)
public class ModEnhancedSnowman {

    public static final String MOD_ID = "enhanced_snowman";



    public static ModEnhancedSnowman INSTANCE;

    public ModEnhancedSnowman(IEventBus modEventBus) {
        INSTANCE = this;
//        modEventBus.register(Configs.class);

        NeoForge.EVENT_BUS.register(this);

        Optional<? extends ModContainer> opt = ModList.get().getModContainerById(MOD_ID);
        if (opt.isPresent()) {

            org.apache.logging.log4j.LogManager.getLogger().info("Preparing Enhanced Snowman {}", opt.get().getModInfo().getVersion());
            opt.get().registerConfig(ModConfig.Type.COMMON, Configs.confSpec);
        } else {
            org.apache.logging.log4j.LogManager.getLogger().error("Somehow Enhanced Snowman could not be found");
        }


    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDeath(LivingDeathEvent event) {
        if (Configs.COMMON.convert.get() && event.getSource().getEntity() instanceof SnowGolem) {
            if (Configs.COMMON.convert_chance.get() > Math.random()) {
                SnowGolem snowman = new SnowGolem(EntityType.SNOW_GOLEM, event.getEntity().level());
                snowman.copyPosition(event.getEntity());
                event.getEntity().level().addFreshEntity(snowman);
                event.getEntity().deathTime = 19;
            }
        }
    }

    @SubscribeEvent
    public void onLivingBaseAttack(LivingIncomingDamageEvent event) {
        if (event.getAmount() == 0.0F && event.getSource().getDirectEntity() instanceof Snowball) {
            if (event.getEntity().level().isClientSide()) return;
            if (event.getSource().getEntity() instanceof SnowGolem || (Configs.COMMON.playersDealDamage.get() && event.getSource().getEntity() instanceof Player)) {
                if (event.getEntity() instanceof Enemy || !Configs.COMMON.onlyHostile.get()) {
                    Snowball ball = (Snowball) event.getSource().getDirectEntity();
                    if (!ball.getPersistentData().contains("dealt_damage")) {
                        ball.getPersistentData().putBoolean("dealt_damage", true);
                        Entity indirectEntity = event.getSource().getEntity();
                        LivingEntity indirectEntityLiving = indirectEntity instanceof LivingEntity ? (LivingEntity) indirectEntity : null;
                        event.getEntity().hurt(event.getEntity().level().damageSources().mobProjectile(event.getSource().getDirectEntity(), indirectEntityLiving), Configs.COMMON.snowballDamage.get().floatValue());
                        if (Configs.COMMON.slowness.get()) {
                            event.getEntity().addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 1));
                        }
                    }
                }
            }
        }
        if (Configs.COMMON.disable_heat_damage.get() && event.getSource().is(DamageTypes.ON_FIRE) && event.getEntity().getType() == EntityType.SNOW_GOLEM) {
            event.setCanceled(true);
        }
        if (Configs.COMMON.disable_water_damage.get() && event.getSource().is(DamageTypes.DROWN) && event.getEntity().getType() == EntityType.SNOW_GOLEM) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMobGriefing(EntityMobGriefingEvent event){
        if(Configs.COMMON.prevent_snow_trail.get() && event.getEntity() instanceof SnowGolem){
            event.setCanGrief(false);
        }
    }
}
