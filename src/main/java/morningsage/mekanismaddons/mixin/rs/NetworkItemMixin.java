package morningsage.mekanismaddons.mixin.rs;

import com.refinedmods.refinedstorage.item.NetworkItem;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.rs.RSActions;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetworkItem.class)
public abstract class NetworkItemMixin {

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundNBT;"
        ),
        method = "*"
    )
    private static CompoundNBT getTagStatic(ItemStack stack) {
        if (stack.getItem() instanceof ItemMekaTool) {
            return RSActions.getTag(stack);
        }

        return stack.getTag();
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getTag()Lnet/minecraft/nbt/CompoundNBT;"
        ),
        method = "*"
    )
    private CompoundNBT getTag(ItemStack stack) {
        if (stack.getItem() instanceof ItemMekaTool) {
            return RSActions.getTag(stack);
        }

        return stack.getTag();
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;setTag(Lnet/minecraft/nbt/CompoundNBT;)V"
        ),
        method = "useOn"
    )
    private void setTag(ItemStack stack, CompoundNBT tag) {
        if (stack.getItem() instanceof ItemMekaTool) RSActions.setTag(stack, tag);
    }
}
