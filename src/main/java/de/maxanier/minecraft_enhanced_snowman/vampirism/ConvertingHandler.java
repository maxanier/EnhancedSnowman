package de.maxanier.minecraft_enhanced_snowman.vampirism;

import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import net.minecraft.entity.monster.EntitySnowman;

public class ConvertingHandler implements IConvertingHandler<EntitySnowman> {
    @Override
    public IConvertedCreature<EntitySnowman> createFrom(EntitySnowman entity) {
        return null;
    }
}
