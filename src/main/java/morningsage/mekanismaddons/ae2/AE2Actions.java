package morningsage.mekanismaddons.ae2;

import appeng.api.config.Settings;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import appeng.core.Api;
import appeng.util.ConfigManager;
import mekanism.api.math.FloatingLong;
import mekanism.common.content.gear.Modules;
import mekanism.common.registries.MekanismItems;
import morningsage.mekanismaddons.items.modules.AddonModules;
import morningsage.mekanismaddons.items.modules.ModuleDigitalStorageUnit;
import morningsage.mekanismaddons.items.modules.ModuleDigitalStorageUnit.StorageMode;
import morningsage.mekanismaddons.plugins.AE2Plugin;
import morningsage.mekanismaddons.plugins.IDigitalStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public final class AE2Actions implements IDigitalStorage {
    private static final String TAG_ENCRYPTION_KEY = "encryptionKey";
    public static final AE2Actions INSTANCE = new AE2Actions();

    public static void registerMekaToolTerminal() {
        Api.instance().registries().wireless().registerWirelessHandler((IWirelessTermHandler) MekanismItems.MEKA_TOOL.get());
    }

    @Override
    public ActionResultType useCustomTerminalOn(ItemUseContext context) {
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> useCustomTerminal(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        Api.instance().registries().wireless().openWirelessTerminalGui(stack, world, player, hand);
        return new ActionResult<>(ActionResultType.sidedSuccess(world.isClientSide()), player.getItemInHand(hand));
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        CompoundNBT tag = item.getTagElement(StorageMode.AE2.getNbtKey());
        return tag != null ? tag.getString(TAG_ENCRYPTION_KEY) : "";
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        ModuleDigitalStorageUnit module = Modules.load(item, AddonModules.DIGITAL_STORAGE_UNIT);
        if (module != null) module.setStorageMode(StorageMode.AE2);
        item.getOrCreateTagElement(StorageMode.AE2.getNbtKey()).putString(TAG_ENCRYPTION_KEY, encKey);
    }

    @Override
    public String getModId() {
        return AE2Plugin.MOD_ID;
    }

    @Override
    public boolean canHandle(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        ModuleDigitalStorageUnit module = Modules.load(itemStack, AddonModules.DIGITAL_STORAGE_UNIT);

        if (module != null) {
            return module.useEnergy(playerEntity, FloatingLong.create(v)).doubleValue() == v;
        }

        return false;
    }

    @Override
    public boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        ModuleDigitalStorageUnit module = Modules.load(itemStack, AddonModules.DIGITAL_STORAGE_UNIT);

        if (module != null) {
            return playerEntity.isCreative() || module.canUseEnergy(playerEntity, FloatingLong.create(v));
        }

        return false;
    }

    public static IConfigManager getConfigManager(ItemStack itemStack) {
        ConfigManager out = new ConfigManager((manager, settingName, newValue) -> {
            CompoundNBT data = itemStack.getOrCreateTagElement(StorageMode.AE2.getNbtKey());
            manager.writeToNBT(data);
        });
        out.registerSetting(Settings.SORT_BY, SortOrder.NAME);
        out.registerSetting(Settings.VIEW_MODE, ViewItems.ALL);
        out.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING);
        out.readFromNBT(itemStack.getOrCreateTagElement(StorageMode.AE2.getNbtKey()).copy());
        return out;
    }
}
