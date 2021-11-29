package morningsage.mekanismaddons.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import appeng.items.materials.MaterialType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.util.Tuple;

public class EnumNames {
    // Upgrade
    public static final Map<String, Upgrade> UPGRADES_BY_NAME = new Object2ObjectOpenHashMap<>();
    public static final Map<Upgrade, ItemRegistryObject<? extends ItemUpgrade>> UPGRADES_BY_DATA = new LinkedHashMap<>();

    public static final String LUCK = "luck";

    // APILang
    public static final String UPGRADE_LUCK = "UPGRADE_LUCK";
    public static final String UPGRADE_LUCK_DESCRIPTION = "UPGRADE_LUCK_DESCRIPTION";

    // MaterialType
    public static final Map<MaterialType, Tuple<Integer, Double>> DATA_BY_TYPE = new LinkedHashMap<>();
}
