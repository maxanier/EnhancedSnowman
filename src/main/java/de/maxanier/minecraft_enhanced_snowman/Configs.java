package de.maxanier.minecraft_enhanced_snowman;

import net.minecraftforge.common.config.Config;

@Config(modid = ModEnhancedSnowman.MOD_ID, type = Config.Type.INSTANCE)
public class Configs {
    @Config.Comment("Damage dealt by one snowman snowball")
    @Config.RangeDouble(min = 0, max = 100)
    public static float snowballDamage = 0.5F;
    @Config.Comment("Only deal damage to hostile creatures")
    public static boolean onlyHostile = true;
    @Config.Comment("Allow players to deal damage with snowballs")
    public static boolean playersDealDamage = false;
    @Config.Comment("Convert creatures killed by snowmen to a snowman")
    public static boolean convert = false;
    @Config.Comment("Chance of converting killed creatures to snowmen if enabled. (0.0-1.0)")
    public static double convert_chance = 1.0;
    @Config.Comment("Add a short freeze/slowness to a creature damaged by a snowball")
    public static boolean slowness = true;
}
