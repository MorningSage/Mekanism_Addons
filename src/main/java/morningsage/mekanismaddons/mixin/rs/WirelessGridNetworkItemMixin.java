package morningsage.mekanismaddons.mixin.rs;

import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.apiimpl.network.item.WirelessGridNetworkItem;
import mekanism.common.item.gear.ItemMekaTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WirelessGridNetworkItem.class)
public abstract class WirelessGridNetworkItemMixin {

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        ),
        method = {"onOpen", "drainEnergy"}
    )
    public Item onOpen(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ItemMekaTool) return RSItems.WIRELESS_GRID.get();

        return item;
    }

}
