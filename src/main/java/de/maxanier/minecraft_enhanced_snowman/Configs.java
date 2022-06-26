package de.maxanier.minecraft_enhanced_snowman;


import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Configs {

    public static final Common COMMON;
    static final ForgeConfigSpec confSpec;

    static {
        final Pair<Configs.Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Configs.Common::new);
        confSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }


    public static class Common {
        public final ForgeConfigSpec.DoubleValue snowballDamage;
        public final ForgeConfigSpec.BooleanValue onlyHostile;
        public final ForgeConfigSpec.BooleanValue playersDealDamage;
        public final ForgeConfigSpec.BooleanValue convert;
        public final ForgeConfigSpec.DoubleValue convert_chance;
        public final ForgeConfigSpec.BooleanValue slowness;
        public final ForgeConfigSpec.BooleanValue prevent_snow_trail;
        public final ForgeConfigSpec.BooleanValue disable_heat_damage;
        public final ForgeConfigSpec.BooleanValue disable_water_damage;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Snowman common settings");
            builder.push("common");

            snowballDamage = builder.comment("Damage dealt by one snowgolem snowball").defineInRange("snowballDamage", 1D, 0, 100);
            onlyHostile = builder.comment("Only deal damage to hostile creatures").define("onlyHostile", true);
            playersDealDamage = builder.comment("Allow players to deal damage with snowballs").define("playersDealDamage", false);
            convert = builder.comment("Convert creatures killed by snowgolem to a snowgolem").define("convert", false);
            convert_chance = builder.comment("Chance of converting killed creature to a snowgolem if enabled").defineInRange("convert_chance", 1D, 0, 1);
            slowness = builder.comment("Add a short freeze/slowness to a creature hit by a snowball").define("slowness", true);
            prevent_snow_trail = builder.comment("Prevent snowgolems from leaving a snow trail").define("prevent_snow_trail", false);
            disable_heat_damage = builder.comment("Prevent snowgolems from taking damage in hot biomes").define("disable_heat_damage", false);
            disable_water_damage = builder.comment("Prevent snowgolems from taking damage in rain (or in water)").define("disable_water_damage", false);
            builder.pop();

        }

    }
}
