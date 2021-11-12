package morningsage.mekanismaddons.ae2;

import appeng.api.features.AEFeature;
import appeng.items.materials.MaterialType;
import morningsage.mekanismaddons.utils.EnumBuilder;
import net.minecraft.util.Tuple;

import java.util.*;

public final class AddonMaterialType {
    private static final EnumBuilder<MaterialType> builder = EnumBuilder.of(MaterialType.class);

    public static final Map<MaterialType, Tuple<Integer, Double>> DATA_BY_TYPE = new LinkedHashMap<>();

    public static final MaterialType GAS_1K_CELL_COMPONENT;
    public static final MaterialType GAS_4K_CELL_COMPONENT;
    public static final MaterialType GAS_16K_CELL_COMPONENT;
    public static final MaterialType GAS_64K_CELL_COMPONENT;

    static {
        GAS_1K_CELL_COMPONENT = register(1);
        GAS_4K_CELL_COMPONENT = register( 4);
        GAS_16K_CELL_COMPONENT = register(16);
        GAS_64K_CELL_COMPONENT = register(64);
    }

    private static MaterialType register(int size) {
        String enumName = "GAS_" + size + "K_CELL_COMPONENT";
        String enumID = size + "k_gas_cell_component";

        MaterialType type = builder.create(enumName).init(enumID, EnumSet.of(AEFeature.STORAGE_CELLS));

        DATA_BY_TYPE.put(type, new Tuple<>(size << 3, Math.log(size) / Math.log(4) * 0.5D + 0.5D));

        return type;
    }
}
