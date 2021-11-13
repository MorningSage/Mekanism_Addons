package morningsage.mekanismaddons.config;


import mekanism.common.config.MekanismConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class AddonConfig {
    public static final GeneralConfig general = new GeneralConfig();

    private AddonConfig() {
    }

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        MekanismConfigHelper.registerConfig(modContainer, general);
    }
}
