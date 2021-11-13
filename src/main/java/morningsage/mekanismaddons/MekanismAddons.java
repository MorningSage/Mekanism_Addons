package morningsage.mekanismaddons;

import morningsage.mekanismaddons.config.AddonConfig;
import morningsage.mekanismaddons.items.AddonItems;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MekanismAddons.MOD_ID)
public class MekanismAddons {

    public static final String MOD_ID = "mekanismaddons";

    public MekanismAddons() {
        AddonConfig.registerConfigs(ModLoadingContext.get());

        AddonItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        //if (ModList.get().isLoaded("appliedenergistics2")) {
        //    AddonAE2Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        //}
    }

}
