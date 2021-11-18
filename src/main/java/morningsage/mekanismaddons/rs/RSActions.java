package morningsage.mekanismaddons.rs;

import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.apiimpl.network.item.WirelessGridNetworkItem;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import morningsage.mekanismaddons.items.modules.ModuleDigitalStorageUnit;
import morningsage.mekanismaddons.plugins.IDigitalStorage;
import morningsage.mekanismaddons.plugins.RSPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public final class RSActions implements IDigitalStorage {

    public static final RSActions INSTANCE = new RSActions();

    @Override
    public String getEncryptionKey(ItemStack item) {
        return "";
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        // No-Op
    }

    @Override
    public ActionResultType useCustomTerminalOn(ItemUseContext context) {
        return RSItems.WIRELESS_GRID.get().useOn(context);
    }

    @Override
    public boolean canHandle(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> useCustomTerminal(World world, PlayerEntity entity, ItemStack stack, Hand hand) {
        return RSItems.WIRELESS_GRID.get().use(world, entity, hand);
    }

    public static INetworkItem provide(INetworkItemManager iNetworkItemManager, PlayerEntity playerEntity, ItemStack itemStack, PlayerSlot slot) {
        return new WirelessGridNetworkItem(iNetworkItemManager, playerEntity, itemStack, slot);
    }

    public static CompoundNBT getTag(ItemStack stack) {
        return stack.getOrCreateTagElement(ModuleDigitalStorageUnit.StorageMode.RS.getNbtKey());
    }

    public static void setTag(ItemStack stack, CompoundNBT tag) {
        stack.addTagElement(ModuleDigitalStorageUnit.StorageMode.RS.getNbtKey(), tag);
    }

    @Override
    public String getModId() {
        return RSPlugin.MOD_ID;
    }
}
