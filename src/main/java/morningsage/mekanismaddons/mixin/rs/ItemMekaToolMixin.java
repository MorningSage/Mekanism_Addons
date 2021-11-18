package morningsage.mekanismaddons.mixin.rs;

import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemProvider;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.rs.RSActions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

/**
 * Requires RS 1.9.16.  In older versions, PlayerSlot is an int.
 * <p>
 * With a bit of work, we could dynamically implement and override
 * with the correct definition, but is it really worth it?  If you
 * are reading this, you are welcome to write it and make a PR. I,
 * however, don't personally fancy the extra work.
 */


@Mixin(ItemMekaTool.class)
public class ItemMekaToolMixin implements INetworkItemProvider {

    @Nonnull
    @Override
    public INetworkItem provide(INetworkItemManager iNetworkItemManager, PlayerEntity playerEntity, ItemStack itemStack, PlayerSlot slot) {
        return RSActions.provide(iNetworkItemManager, playerEntity, itemStack, slot);
    }
}
