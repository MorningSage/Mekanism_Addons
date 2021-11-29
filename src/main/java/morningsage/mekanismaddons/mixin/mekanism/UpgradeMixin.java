package morningsage.mekanismaddons.mixin.mekanism;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.api.Upgrade;
import mekanism.api.text.APILang;
import mekanism.api.text.EnumColor;
import mekanism.common.util.EnumUtils;
import morningsage.mekanismaddons.utils.EnumNames;

// Based on code found in this comment: https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556
@Unique
@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeMixin {

    @Shadow
	@Final
	@Mutable
	private static Upgrade[] $VALUES;

    @Shadow
	@Final
	@Mutable
	private static Upgrade[] UPGRADES;

    private static Upgrade LUCK_UPGRADE;

    private static boolean firstLoad = true;

    @Inject(method = "values", at = @At("HEAD"))
    private static void init(CallbackInfoReturnable<Upgrade[]> callbackInfo) {
        if (LUCK_UPGRADE == null && firstLoad) {
            firstLoad = false;
            LUCK_UPGRADE = registerUpgrade(EnumNames.LUCK, APILang.valueOf(EnumNames.UPGRADE_LUCK), APILang.valueOf(EnumNames.UPGRADE_LUCK_DESCRIPTION), 4, EnumColor.ORANGE);
        }
    }

    private static Upgrade registerUpgrade(String name, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
        //Upgrade upgrade = upgradeEnumBuilder.create(name.toUpperCase()).init(name, langKey, descLangKey, maxStack, color);
        Upgrade upgrade = UpgradeMixin.addVariant(name.toUpperCase(), name, langKey, descLangKey, maxStack, color);
        EnumNames.UPGRADES_BY_NAME.put(name, upgrade);

        ArrayList<Upgrade> values;
        if (EnumUtils.UPGRADES == null) {
            values = new ArrayList<>(Arrays.asList(UpgradeMixin.$VALUES));
        } else {
            values = new ArrayList<>(Arrays.asList(EnumUtils.UPGRADES));
        }
        if (!values.contains(upgrade)) {
            values.add(upgrade);
        }
        EnumUtilsAccessor.setUpgrades(values.toArray((Upgrade[]) Array.newInstance(Upgrade.class, 0)));

        return upgrade;
    }

    @Invoker("<init>")
    public static Upgrade invokeInit(String internalName, int internalId, String name, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
        throw new Error("Failed to invoke Upgrade init");
    }

    private static Upgrade addVariant(String internalName, String name, APILang langKey, APILang descLangKey, int maxStack, EnumColor color) {
		ArrayList<Upgrade> variants = new ArrayList<Upgrade>(Arrays.asList(UpgradeMixin.$VALUES));
		Upgrade upgrade = invokeInit(internalName, variants.get(variants.size() - 1).ordinal() + 1, name, langKey, descLangKey, maxStack, color);
		variants.add(upgrade);
		UpgradeMixin.$VALUES = variants.toArray(new Upgrade[0]);
        UpgradeMixin.UPGRADES = UpgradeMixin.$VALUES;
		return upgrade;
	}

}
