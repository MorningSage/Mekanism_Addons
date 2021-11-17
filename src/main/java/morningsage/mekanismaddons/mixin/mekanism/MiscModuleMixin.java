package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.mekatool.ModuleMekaTool;
import mekanism.common.content.gear.mekatool.ModuleVeinMiningUnit;
import morningsage.mekanismaddons.items.modules.AddonModules;
import morningsage.mekanismaddons.items.modules.IHasSmartEnable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mixin({ModuleVeinMiningUnit.class, ModuleMekaTool.ModuleTeleportationUnit.class})
public abstract class MiscModuleMixin extends Module implements IHasSmartEnable {

    private static final Set<Modules.ModuleData<?>> EMPTY = new HashSet<>();

    private static final Set<Modules.ModuleData<?>> VEINMINING_INCOMPATIBLE = new HashSet<>(
        Collections.singletonList(
            AddonModules.AOE_UNIT
        )
    );

    private static final Set<Modules.ModuleData<?>> TELEPORTATION_INCOMPATIBLE = new HashSet<>(
        Collections.singletonList(
            AddonModules.DIGITAL_STORAGE_UNIT
        )
    );

    @Override
    public Set<Modules.ModuleData<?>> getIncompatibleModules() {
        Module self = this;

        if (self instanceof ModuleVeinMiningUnit) return VEINMINING_INCOMPATIBLE;
        if (self instanceof ModuleMekaTool.ModuleTeleportationUnit) return TELEPORTATION_INCOMPATIBLE;

        return EMPTY;
    }

    @Override
    public ItemStack getContainer() {
        return super.getContainer();
    }
}
