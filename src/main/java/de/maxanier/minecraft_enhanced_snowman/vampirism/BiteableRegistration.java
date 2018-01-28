package de.maxanier.minecraft_enhanced_snowman.vampirism;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Items;

public class BiteableRegistration {

    public static void register() {
        VampirismAPI.biteableRegistry().addConvertible(EntitySnowman.class, "enhanced_snowman:textures/vampire_snowman_overlay.png", new IConvertingHandler.IDefaultHelper() {
            @Override
            public void dropConvertedItems(EntityCreature entity, boolean recentlyHit, int looting) {
                entity.dropItem(Items.STICK, 1);
            }

            @Override
            public double getConvertedDMG(EntityCreature entity) {
                return 0;
            }

            @Override
            public double getConvertedKnockbackResistance(EntityCreature entity) {
                return 0;
            }

            @Override
            public double getConvertedMaxHealth(EntityCreature entity) {
                return 0;
            }

            @Override
            public double getConvertedSpeed(EntityCreature entity) {
                return 0;
            }
        });
    }
}
