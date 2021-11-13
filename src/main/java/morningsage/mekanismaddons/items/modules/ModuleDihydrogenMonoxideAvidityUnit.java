package morningsage.mekanismaddons.items.modules;

import mekanism.common.content.gear.mekasuit.ModuleMekaSuit;
import morningsage.mekanismaddons.config.AddonConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ModuleDihydrogenMonoxideAvidityUnit extends ModuleMekaSuit {

    public void adjustMiningSpeed(PlayerEvent.BreakSpeed event) {
        if (!isEnabled() || !AddonConfig.general.dihydrogenMonoxideAvidityUnitEnabled.get()) return;

        LivingEntity entity = event.getEntityLiving();

        if (entity.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(entity)) {
            if (event.getOriginalSpeed() < event.getNewSpeed() * 5) event.setNewSpeed(event.getNewSpeed() * 5F);
        }
    }

}
