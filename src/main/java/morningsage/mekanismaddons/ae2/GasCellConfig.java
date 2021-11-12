package morningsage.mekanismaddons.ae2;

import appeng.items.contents.CellConfig;
import morningsage.mekanismaddons.ae2.gas.GasDummyItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GasCellConfig extends CellConfig {
    public GasCellConfig(ItemStack is) {
        super(is);
    }

    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || stack.getItem() instanceof GasDummyItem) {
            super.insertItem(slot, stack, simulate);
        }

        //Optional<GasStack> fluidOpt = FluidUtil.getFluidContained(stack);
        //if (fluidOpt.isPresent() && Api.instance().definitions().items().dummyFluidItem().maybeStack(1).isPresent()) {
        //    FluidStack fluid = fluidOpt.orElse(null);
        //    fluid.setAmount(1000);
        //    ItemStack is = Api.instance().definitions().items().dummyFluidItem().maybeStack(1).get();
        //    FluidDummyItem item = (FluidDummyItem)is.getItem();
        //    item.setFluidStack(is, fluid);
        //    return super.insertItem(slot, is, simulate);
        //} else {
            return stack;
        //}
    }

    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof GasDummyItem) {
            super.setStackInSlot(slot, stack);
        }

        //Optional<FluidStack> fluidOpt = FluidUtil.getFluidContained(stack);
        //if (fluidOpt.isPresent() && Api.instance().definitions().items().dummyFluidItem().maybeStack(1).isPresent()) {
        //    FluidStack fluid = fluidOpt.orElse((Object)null);
        //    fluid.setAmount(1000);
        //    ItemStack is = (ItemStack)Api.instance().definitions().items().dummyFluidItem().maybeStack(1).get();
        //    FluidDummyItem item = (FluidDummyItem)is.getItem();
        //    item.setFluidStack(is, fluid);
        //    super.setStackInSlot(slot, is);
        //}
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof GasDummyItem) {
            super.isItemValid(slot, stack);
        }

        //Optional<FluidStack> fluidOpt = FluidUtil.getFluidContained(stack);
        //if (fluidOpt.isPresent() && Api.instance().definitions().items().dummyFluidItem().maybeStack(1).isPresent()) {
        //    FluidStack fluid = (FluidStack)fluidOpt.orElse((Object)null);
        //    fluid.setAmount(1000);
        //    ItemStack is = (ItemStack)Api.instance().definitions().items().dummyFluidItem().maybeStack(1).get();
        //    FluidDummyItem item = (FluidDummyItem)is.getItem();
        //    item.setFluidStack(is, fluid);
        //    return super.isItemValid(slot, is);
        //} else {
            return false;
        //}
    }
}
