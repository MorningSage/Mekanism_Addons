package morningsage.mekanismaddons.mixin.ae2;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import mekanism.common.content.gear.Modules;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.ae2.AE2Actions;
import morningsage.mekanismaddons.items.modules.AddonModules;
import morningsage.mekanismaddons.items.modules.ModuleDigitalStorageUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemMekaTool.class)
public class ItemMekaToolMixin implements IWirelessTermHandler {

    @Override
    public String getEncryptionKey(ItemStack item) {
        ModuleDigitalStorageUnit module = Modules.load(item, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) return module.getStorageMode().getEncryptionKey(item);
        return "";
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        ModuleDigitalStorageUnit module = Modules.load(item, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) module.getStorageMode().setEncryptionKey(item, encKey, name);
    }

    @Override
    public boolean canHandle(ItemStack itemStack) {
        ModuleDigitalStorageUnit module = Modules.load(itemStack, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) return module.getStorageMode().canHandle(itemStack);
        return false;
    }

    @Override
    public boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        ModuleDigitalStorageUnit module = Modules.load(itemStack, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) return module.getStorageMode().usePower(playerEntity, v, itemStack);
        return false;
    }

    @Override
    public boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        ModuleDigitalStorageUnit module = Modules.load(itemStack, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) return module.getStorageMode().hasPower(playerEntity, v, itemStack);
        return false;
    }

    @Override
    public IConfigManager getConfigManager(ItemStack itemStack) {
        return AE2Actions.getConfigManager(itemStack);
    }

}
