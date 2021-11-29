package morningsage.mekanismaddons.mixin.mekanism;

import java.util.ArrayList;
import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

import mekanism.api.text.APILang;
import morningsage.mekanismaddons.utils.EnumNames;

// Based on code found in this comment: https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556
@Unique
@Mixin(value = APILang.class, remap = false)
public abstract class APILangMixin {

    @Shadow
	@Final
	@Mutable
	private static APILang[] $VALUES;

    private static final APILang UPGRADE_LUCK = APILangMixin.addVariant(EnumNames.UPGRADE_LUCK, "upgrade", "luck");
    private static final APILang UPGRADE_LUCK_DESCRIPTION = APILangMixin.addVariant(EnumNames.UPGRADE_LUCK_DESCRIPTION, "upgrade", "luck.description");


    @Invoker("<init>")
    public static APILang invokeInit(String internalName, int internalId, String type, String path) {
        throw new Error("Failed to invoke APILang init");
    }

    private static APILang addVariant(String internalName, String type, String path) {
		ArrayList<APILang> variants = new ArrayList<APILang>(Arrays.asList(APILangMixin.$VALUES));
		APILang materialType = invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, type, path);
		variants.add(materialType);
		APILangMixin.$VALUES = variants.toArray(new APILang[0]);
		return materialType;
	}

}
