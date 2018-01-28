package de.maxanier.minecraft_enhanced_snowman.vampirism;

import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class EntityVampireSnowman extends EntitySnowman implements IConvertedCreature<EntitySnowman> {


    private boolean sunDamageCache = false;

    public EntityVampireSnowman(World worldIn) {
        super(worldIn);
    }

    //TODO use API
    private static boolean gettingSundamge(EntityLivingBase entity) {
        entity.getEntityWorld().profiler.startSection("vampirism_checkSundamage");
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSpectator()) return false;
        if (VampirismAPI.sundamageRegistry().getSundamageInDim(entity.getEntityWorld().provider.getDimension())) {
            if (!entity.getEntityWorld().isRaining()) {
                float angle = entity.getEntityWorld().getCelestialAngle(1.0F);
                //TODO maybe use this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)
                if (angle > 0.78 || angle < 0.24) {
                    BlockPos pos = new BlockPos(entity.posX, entity.posY + MathHelper.clamp(entity.height / 2.0F, 0F, 2F), entity.posZ);

                    if (canBlockSeeSun(entity.getEntityWorld(), pos)) {
                        try {
                            Biome biome = entity.getEntityWorld().getBiome(pos);
                            if (VampirismAPI.sundamageRegistry().getSundamageInBiome(biome)) {
                                entity.getEntityWorld().profiler.endSection();
                                return true;
                            }
                        } catch (NullPointerException e) {
                            //Strange thing which happen in 1.7.10, not sure about 1.8
                        }

                    }
                }

            }
        }
        entity.getEntityWorld().profiler.endSection();

        return false;
    }

    //TODO use API
    private static boolean canBlockSeeSun(World world, BlockPos pos) {
        if (pos.getY() >= world.getSeaLevel()) {
            return world.canSeeSky(pos);
        } else {
            BlockPos blockpos = new BlockPos(pos.getX(), world.getSeaLevel(), pos.getZ());

            if (!world.canSeeSky(blockpos)) {
                return false;
            } else {
                int liquidBlocks = 0;
                for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
                    IBlockState iblockstate = world.getBlockState(blockpos);
                    if (iblockstate.getBlock().getLightOpacity(iblockstate, world, blockpos) > 0) {
                        if (iblockstate.getMaterial().isLiquid()) {
                            liquidBlocks++;
                            if (liquidBlocks >= 4) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }

                }

                return true;
            }
        }
    }

    @Override
    public boolean wantsBlood() {
        return false;
    }

    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return true;
    }

    @Override
    public void drinkBlood(int amt, float saturationMod) {
        this.addPotionEffect(new PotionEffect(MobEffects.SPEED));
    }

    @Override
    public EnumStrength isGettingGarlicDamage() {
        return isGettingGarlicDamage(false);
    }

    @Override
    public EnumStrength isGettingGarlicDamage(boolean forcerefresh) {
        return EnumStrength.NONE;
    }

    @Override
    public boolean isGettingSundamage(boolean forcerefresh) {
        if (forcerefresh) {
            sunDamageCache = isGettingSundamage();
        }
        return sunDamageCache;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted % 5 == 0) {
            sunDamageCache = this.isGettingSundamage(true);
        }
    }

    @Override
    public boolean isGettingSundamage() {
        return isGettingSundamage(false);
    }

    @Override
    public boolean isIgnoringSundamage() {
        return false;
    }

    @Override
    public IFaction getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public EntityLivingBase getRepresentingEntity() {
        return this;
    }
}
