package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.Upgrade;
import mekanism.common.util.EnumUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnumUtils.class)
public interface EnumUtilsAccessor {

    @Final @Accessor("UPGRADES")
    static void setUpgrades(Upgrade[] upgrades) {
        throw new Error("Failed to access `UPGRADES`");
    }

}
