package morningsage.mekanismaddons.ae2.gas;

import appeng.items.AEBaseItem;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class GasDummyItem extends AEBaseItem {
    public GasDummyItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    public String getDescriptionId(@Nonnull ItemStack stack) {
        GasStack gasStack = this.getGasStack(stack);
        if (gasStack.isEmpty()) gasStack = GasStack.EMPTY;
        return gasStack.getTranslationKey();
    }

    public GasStack getGasStack(ItemStack is) {
        if (is.hasTag()) {
            CompoundNBT tag = is.getTag();
            return GasStack.readFromNBT(tag);
        } else {
            return GasStack.EMPTY;
        }
    }


    public void setGasStack(ItemStack is, GasStack gs) {
        if (gs.isEmpty()) {
            is.setTag(null);
        } else {
            CompoundNBT tag = new CompoundNBT();
            gs.write(tag);
            is.setTag(tag);
        }

    }

    public void fillItemCategory(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
    }
}
