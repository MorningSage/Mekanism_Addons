package morningsage.mekanismaddons.ae2.channels;

import appeng.api.storage.IStorageHelper;
import appeng.api.storage.data.IItemList;
import com.google.common.base.Preconditions;
import mekanism.api.chemical.gas.GasStack;
import morningsage.mekanismaddons.ae2.gas.AEGasStack;
import morningsage.mekanismaddons.ae2.gas.GasDummyItem;
import morningsage.mekanismaddons.ae2.gas.GasList;
import morningsage.mekanismaddons.ae2.gas.IAEGasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GasStorageChannel implements IGasStorageChannel {
    private GasStorageChannel() { }

    public static void register(IStorageHelper storageHelper) {
        storageHelper.registerStorageChannel(IGasStorageChannel.class, new GasStorageChannel());
    }

    public int transferFactor() {
        return 125;
    }

    public int getUnitsPerByte() {
        return 8000;
    }

    @Nonnull
    @Override
    public IItemList<IAEGasStack> createList() {
        return new GasList();
    }

    @Nullable
    @Override
    public IAEGasStack createStack(@Nonnull Object input) {
        Preconditions.checkNotNull(input);
        if (input instanceof GasStack) {
            return AEGasStack.fromGasStack((GasStack)input);
        } else if (input instanceof ItemStack && ((ItemStack) input).getItem() instanceof GasDummyItem) {
            ItemStack is = (ItemStack)input;
            return AEGasStack.fromGasStack(((GasDummyItem)is.getItem()).getGasStack(is));
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public IAEGasStack readFromPacket(@Nonnull PacketBuffer input) {
        Preconditions.checkNotNull(input);
        return AEGasStack.fromPacket(input);
    }

    @Nullable
    @Override
    public IAEGasStack createFromNBT(@Nonnull CompoundNBT nbt) {
        Preconditions.checkNotNull(nbt);
        return AEGasStack.fromNBT(nbt);
    }
}
