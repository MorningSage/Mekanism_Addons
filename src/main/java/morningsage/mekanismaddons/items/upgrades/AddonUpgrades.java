package morningsage.mekanismaddons.items.upgrades;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tier.FactoryTier;
import morningsage.mekanismaddons.utils.EnumBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddonUpgrades {

    public static final Map<String, Upgrade> UPGRADES_BY_NAME = new Object2ObjectOpenHashMap<>();
    public static final Map<Upgrade, ItemRegistryObject<? extends ItemUpgrade>> UPGRADES_BY_DATA = new LinkedHashMap<>();

    private static final EnumBuilder<APILang> langEnumBuilder = EnumBuilder.of(APILang.class);
    private static final EnumBuilder<Upgrade> upgradeEnumBuilder = EnumBuilder.of(Upgrade.class);

    public static final APILang UPGRADE_LUCK = langEnumBuilder.create("UPGRADE_LUCK").init("upgrade", "luck");
    public static final APILang UPGRADE_LUCK_DESCRIPTION = langEnumBuilder.create("UPGRADE_LUCK_DESCRIPTION").init("upgrade", "luck.description");
    public static final Upgrade LUCK_UPGRADE;

    static {
        LUCK_UPGRADE = registerUpgrade("luck", UPGRADE_LUCK, UPGRADE_LUCK_DESCRIPTION, 4, EnumColor.ORANGE);

        registerSupportedUpgrades(MekanismBlockTypes.ENERGIZED_SMELTER, LUCK_UPGRADE);
        registerSupportedUpgrades(MekanismBlockTypes.ENRICHMENT_CHAMBER, LUCK_UPGRADE);

        registerSupportedUpgrades(FactoryType.SMELTING, LUCK_UPGRADE);
        registerSupportedUpgrades(FactoryType.ENRICHING, LUCK_UPGRADE);
    }

    private static Upgrade registerUpgrade(String name, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
        Upgrade upgrade = upgradeEnumBuilder.create(name).init(name, langKey, descLangKey, maxStack, color);
        UPGRADES_BY_NAME.put(name, upgrade);
        return upgrade;
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
