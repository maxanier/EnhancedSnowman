package de.maxanier.minecraft_enhanced_snowman;

import de.maxanier.minecraft_enhanced_snowman.vampirism.VampirismIntegration;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(
        modid = ModEnhancedSnowman.MOD_ID,
        name = ModEnhancedSnowman.MOD_NAME,
        version = ModEnhancedSnowman.VERSION,
        acceptedMinecraftVersions = "[1.12.0,1.13)"
)
public class ModEnhancedSnowman {

    public static final String MOD_ID = "enhanced_snowman";
    public static final String MOD_NAME = "EnhancedSnowman";
    public static final String VERSION = "1.0";


    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static ModEnhancedSnowman INSTANCE;


    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (Loader.isModLoaded(VampirismIntegration.ID)) {
            VampirismIntegration.registerIntegration();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    @NetworkCheckHandler
    public boolean checkModLists(Map<String, String> modList, Side side) {
        return true;
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public void onLivingBaseAttack(LivingAttackEvent event) {

        if (event.getAmount() == 0.0F && event.getSource().getImmediateSource() instanceof EntitySnowball) {
            if (event.getEntityLiving().getEntityWorld().isRemote) return;
            if (event.getSource().getTrueSource() instanceof EntitySnowman || (Configs.playersDealDamage && event.getSource().getTrueSource() instanceof EntityPlayer)) {
                if (event.getEntityLiving() instanceof IMob || !Configs.onlyHostile) {
                    EntitySnowball ball = (EntitySnowball) event.getSource().getImmediateSource();
                    if (!ball.getEntityData().hasKey("dealt_damage")) {
                        ball.getEntityData().setBoolean("dealt_damage", true);
                        event.getEntityLiving()
                                .attackEntityFrom(
                                        new EntityDamageSourceIndirect("thrown", event.getSource().getImmediateSource(), event.getSource().getTrueSource()),
                                        Configs.snowballDamage);
                        if (Configs.slowness) {
                            event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 1));
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDeath(LivingDeathEvent event) {
        if (Configs.convert && event.getSource().getTrueSource() instanceof EntitySnowman) {
            EntitySnowman snowman = new EntitySnowman(event.getEntityLiving().getEntityWorld());
            snowman.copyLocationAndAnglesFrom(event.getEntityLiving());
            event.getEntityLiving().getEntityWorld().spawnEntity(snowman);
        }
    }
}
