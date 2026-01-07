package de.maxanier.minecraft_enhanced_snowman;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ModEnhancedSnowman.MOD_ID, dist= Dist.CLIENT)
public class ModEnhancedSnowmanClient {
    public ModEnhancedSnowmanClient(IEventBus modBus, ModContainer modContainer) {
        // This will use NeoForge's ConfigurationScreen to display this mod's configs
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
