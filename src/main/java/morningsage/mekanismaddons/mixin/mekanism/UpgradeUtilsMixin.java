package morningsage.mekanismaddons.mixin.mekanism;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.util.UpgradeUtils;
import morningsage.mekanismaddons.utils.EnumNames;
import net.minecraft.item.ItemStack;

@Mixin(UpgradeUtils.class)
public abstract class UpgradeUtilsMixin {

    @Inject(
        at = @At("RETURN"),
        method = "getStack",
        cancellable = true,
        remap = false
    )
    private static void getStack(Upgrade upgrade, CallbackInfoReturnable<ItemStack> callbackInfo) {
        if (callbackInfo.getReturnValue().isEmpty()) {
            ItemRegistryObject<? extends ItemUpgrade> upgradeItem = EnumNames.UPGRADES_BY_DATA.getOrDefault(upgrade, null);
            if (upgradeItem != null) callbackInfo.setReturnValue(upgradeItem.getItemStack());
        }
    }

}
