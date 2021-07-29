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

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Snowman common settings");
            builder.push("common");

            snowballDamage = builder.comment("Damage dealt by one snowman snowball").defineInRange("snowballDamage", 1D, 0, 100);
            onlyHostile = builder.comment("Only deal damage to hostile creatures").define("onlyHostile", true);
            playersDealDamage = builder.comment("Allow players to deal damage with snowballs").define("playersDealDamage", false);
            convert = builder.comment("Convert creatures killed by snowmen to a snowman").define("convert", false);
            convert_chance = builder.comment("Chance of converting killed creature to snowman if enabled").defineInRange("convert_chance", 1D, 0, 1);
            slowness = builder.comment("Add a short freeze/slowness to a creature hit by a snowball").define("slowness", true);
            builder.pop();

        }

    }
}
