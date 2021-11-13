package morningsage.mekanismaddons.items.modules;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.content.gear.mekasuit.ModuleMekaSuit;
import mekanism.common.registries.MekanismGases;
import morningsage.mekanismaddons.config.AddonConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ModuleIgnitionRetardationUnit extends ModuleMekaSuit {

    public static final Set<DamageSource> IMMUNE_TO_SOURCES = new HashSet<>(
        Arrays.asList(
            DamageSource.HOT_FLOOR, DamageSource.IN_FIRE,
            DamageSource.LAVA, DamageSource.ON_FIRE,
            DamageSource.LIGHTNING_BOLT
        )
    );

    public ModuleIgnitionRetardationUnit() { }

    public boolean handleDamageSource(LivingEntity entity, DamageSource damageSource) {
        if (AddonConfig.general.ignitionRetardationUnitEnabled.get() && isEnabled() && IMMUNE_TO_SOURCES.contains(damageSource) && canFunction(entity)) {
            int usage = AddonConfig.general.mekaSuitSodiumUsageRate.get() / getInstalledCount();

            Optional<IGasHandler> capability = this.getContainer().getCapability(Capabilities.GAS_HANDLER_CAPABILITY).resolve();

            if (capability.isPresent()) {
                IGasHandler gasHandler = capability.get();

                GasStack sodium = gasHandler.extractChemical(MekanismGases.SODIUM.getStack(usage), Action.EXECUTE);
                GasStack shsodium = gasHandler.insertChemical(MekanismGases.SUPERHEATED_SODIUM.getStack(sodium.getAmount()), Action.EXECUTE);

                if (!sodium.isEmpty() && shsodium.isEmpty()) {
                    entity.clearFire();
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    public boolean canFunction(LivingEntity entity) {
        if (!AddonConfig.general.ignitionRetardationUnitEnabled.get() || !entity.isOnFire()) return false;

        int usage = AddonConfig.general.mekaSuitSodiumUsageRate.get() / getInstalledCount();
        Optional<IGasHandler> capability = this.getContainer().getCapability(Capabilities.GAS_HANDLER_CAPABILITY).resolve();

        if (capability.isPresent()) {
            IGasHandler gasHandler = capability.get();
            return gasHandler.extractChemical(MekanismGases.SODIUM.getStack(usage), Action.SIMULATE).getAmount() == usage;
        }

        return false;
    }
}
