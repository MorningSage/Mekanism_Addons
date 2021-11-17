package morningsage.mekanismaddons.mixin.rs;

import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemProvider;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.rs.RSActions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(ItemMekaTool.class)
public class ItemMekaToolMixin implements INetworkItemProvider {

    @Nonnull
    @Override
    public INetworkItem provide(INetworkItemManager iNetworkItemManager, PlayerEntity playerEntity, ItemStack itemStack, int i) {
        return RSActions.provide(iNetworkItemManager, playerEntity, itemStack, i);
    }
}
