package morningsage.mekanismaddons.mixin.ae2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import appeng.api.features.AEFeature;
import appeng.items.materials.MaterialType;
import morningsage.mekanismaddons.utils.EnumNames;
import net.minecraft.util.Tuple;

// Based on code found in this comment: https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556
@Mixin(value = MaterialType.class, remap = false)
public abstract class MaterialTypeMixin {

    @Shadow
	@Final
	@Mutable
	private static MaterialType[] $VALUES;

    private static final MaterialType GAS_1K_CELL_COMPONENT;
    private static final MaterialType GAS_4K_CELL_COMPONENT;
    private static final MaterialType GAS_16K_CELL_COMPONENT;
    private static final MaterialType GAS_64K_CELL_COMPONENT;

    static {
        GAS_1K_CELL_COMPONENT = register(1);
        GAS_4K_CELL_COMPONENT = register( 4);
        GAS_16K_CELL_COMPONENT = register(16);
        GAS_64K_CELL_COMPONENT = register(64);
    }

    private static MaterialType register(int size) {
        String enumName = "GAS_" + size + "K_CELL_COMPONENT";
        String enumID = size + "k_gas_cell_component";

        MaterialType type = MaterialTypeMixin.addVariant(enumName, enumID, EnumSet.of(AEFeature.STORAGE_CELLS));

        EnumNames.DATA_BY_TYPE.put(type, new Tuple<>(size << 3, Math.log(size) / Math.log(4) * 0.5D + 0.5D));

        return type;
    }


    @Invoker("<init>")
    public static MaterialType invokeInit(String internalName, int internalId, String id, final Set<AEFeature> features) {
        throw new Error("Failed to invoke MaterialType init");
    }

    private static MaterialType addVariant(String internalName, String id, Set<AEFeature> features) {
		ArrayList<MaterialType> variants = new ArrayList<MaterialType>(Arrays.asList(MaterialTypeMixin.$VALUES));
		MaterialType materialType = invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, id, features);
		variants.add(materialType);
		MaterialTypeMixin.$VALUES = variants.toArray(new MaterialType[0]);
		return materialType;
	}

}
