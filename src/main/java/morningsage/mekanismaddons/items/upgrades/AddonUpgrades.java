package morningsage.mekanismaddons.items.upgrades;

import java.util.Collections;
import java.util.HashSet;

import mekanism.api.Upgrade;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tier.FactoryTier;
import morningsage.mekanismaddons.utils.EnumNames;

public class AddonUpgrades {
    public static void init() {
        registerSupportedUpgrades(MekanismBlockTypes.ENERGIZED_SMELTER, Upgrade.valueOf(EnumNames.LUCK.toUpperCase()));
        registerSupportedUpgrades(MekanismBlockTypes.ENRICHMENT_CHAMBER, Upgrade.valueOf(EnumNames.LUCK.toUpperCase()));

        registerSupportedUpgrades(FactoryType.SMELTING, Upgrade.valueOf(EnumNames.LUCK.toUpperCase()));
        registerSupportedUpgrades(FactoryType.ENRICHING, Upgrade.valueOf(EnumNames.LUCK.toUpperCase()));
    }

    private static void registerSupportedUpgrades(BlockTypeTile<?> blockTypeTile, Upgrade... upgrades) {
        HashSet<Upgrade> newSet = new HashSet<>(blockTypeTile.get(AttributeUpgradeSupport.class).getSupportedUpgrades());
        Collections.addAll(newSet, upgrades);
        blockTypeTile.add(new AttributeUpgradeSupport(newSet));
    }
    private static void registerSupportedUpgrades(FactoryType factoryType, Upgrade... upgrades) {
        for (FactoryTier value : FactoryTier.values()) {
            registerSupportedUpgrades(MekanismBlockTypes.getFactory(value, factoryType), upgrades);
        }
    }

}
