package morningsage.mekanismaddons.items.modules;

import mekanism.common.content.gear.mekatool.ModuleMekaTool;
import morningsage.mekanismaddons.config.AddonConfig;
import morningsage.mekanismaddons.mixin.minecraft.SpawnEggItemAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class ModuleCapturingUnit extends ModuleMekaTool {

    public void dropLoot(LivingDropsEvent event) {
        if (!isEnabled() || !AddonConfig.general.capturingUnitEnabled.get()) return;

        LivingEntity killed = event.getEntityLiving();

        if (killed.level.random.nextFloat() < getInstalledCount() / 250F) {
            ItemStack egg = new ItemStack(SpawnEggItemAccessor.getEggLookups().get(killed.getType()));
            event.getDrops().add(new ItemEntity(killed.level, killed.getX(), killed.getY(), killed.getZ(), egg));
        }
    }

}
