package morningsage.mekanismaddons.items.modules;

import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import net.minecraft.item.ItemStack;

import java.util.Set;

public interface IHasSmartEnable {
    Set<Modules.ModuleData<?>> getIncompatibleModules();

    ItemStack getContainer();

    default void onSmartEnableToggle() {
        for (Modules.ModuleData<?> moduleData : getIncompatibleModules()) {
            Module module = Modules.load(getContainer(), moduleData);
            if (module != null && module.isEnabled()) module.setDisabledForce();
        }
    }
}
