package morningsage.mekanismaddons.items;

import mekanism.api.Upgrade;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.ItemModule;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemDeferredRegister;
import morningsage.mekanismaddons.items.modules.AddonModules;
import morningsage.mekanismaddons.items.upgrades.AddonUpgrades;

public final class AddonItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister("mekanismaddons");

    static {
        for (Modules.ModuleData<?> moduleData : AddonModules.ADDON_MODULES_BY_NAME.values()) {
            AddonModules.ADDON_MODULES_BY_DATA.put(moduleData, ITEMS.register("module_" + moduleData.getName(), (properties) ->
                new ItemModule(moduleData, properties)
            ));
        }

        for (Upgrade upgrade : AddonUpgrades.UPGRADES_BY_NAME.values()) {
            AddonUpgrades.UPGRADES_BY_DATA.put(upgrade, ITEMS.register("upgrade_" + upgrade.getRawName(), (properties) ->
                new ItemUpgrade(upgrade, properties)
            ));
        }
    }
}
