package morningsage.mekanismaddons.ae2.gas;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.IStorageChannel;
import appeng.core.Api;
import appeng.util.item.AEStack;
import com.google.common.base.Preconditions;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import morningsage.mekanismaddons.ae2.channels.IGasStorageChannel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public final class AEGasStack extends AEStack<IAEGasStack> implements IAEGasStack {
    private static final String NBT_STACKSIZE = "cnt";
    private static final String NBT_REQUESTABLE = "req";
    private static final String NBT_CRAFTABLE = "craft";
    private static final String NBT_GAS_ID = "g";

    private final Gas gas;

    private AEGasStack(AEGasStack gasStack) {
        this.gas = gasStack.gas;
        this.setStackSize(gasStack.getStackSize());
        this.setCraftable(gasStack.isCraftable());
        this.setCountRequestable(gasStack.getCountRequestable());
    }


    private AEGasStack(Gas gas, long amount) {
        if (gas == MekanismAPI.EMPTY_GAS) System.out.println();

        this.gas = Preconditions.checkNotNull(gas);
        this.setStackSize(amount);
        this.setCraftable(false);
        this.setCountRequestable(0L);
    }

    public static IAEGasStack fromGasStack(GasStack input) {
        return input.isEmpty() ? null : new AEGasStack(input.getType(), input.getAmount());
    }

    @Override
    protected boolean hasTagCompound() {
        return false;
    }

    @Override
    public GasStack getGasStack() {
        return new GasStack(this.gas, Math.min(2147483647L, this.getStackSize()));
    }

    @Override
    public void add(IAEGasStack option) {
        if (option != null) {
            this.incStackSize(option.getStackSize());
            this.setCountRequestable(this.getCountRequestable() + option.getCountRequestable());
            this.setCraftable(this.isCraftable() || option.isCraftable());
        }
    }

    @Override
    public void writeToNBT(CompoundNBT data) {
        if (this.gas.getRegistryName() != null) {
            data.putString(NBT_GAS_ID, this.gas.getRegistryName().toString());
            data.putLong(NBT_STACKSIZE, this.getStackSize());
            data.putLong(NBT_REQUESTABLE, this.getCountRequestable());
            data.putBoolean(NBT_CRAFTABLE, this.isCraftable());
        }
    }

    @Override
    public boolean fuzzyComparison(IAEGasStack other, FuzzyMode fuzzyMode) {
        return this.gas == other.getGas();
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {
        buffer.writeBoolean(this.isCraftable());
        buffer.writeRegistryIdUnsafe(MekanismAPI.gasRegistry(), this.gas);
        buffer.writeVarLong(this.getStackSize());
        buffer.writeVarLong(this.getCountRequestable());
    }

    @Override
    public IAEGasStack copy() {
        return new AEGasStack(this);
    }

    @Override
    public Gas getGas() {
        return this.gas;
    }

    @Override
    public IStorageChannel<IAEGasStack> getChannel() {
        return Api.instance().storage().getStorageChannel(IGasStorageChannel.class);
    }

    @Override
    public ItemStack asItemStackRepresentation() {
        ItemStack is = Api.instance().definitions().items().dummyFluidItem().maybeStack(1).orElse(ItemStack.EMPTY);
        if (!is.isEmpty()) {
            GasDummyItem item = (GasDummyItem)is.getItem();
            item.setGasStack(is, this.getGasStack());
            return is;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static IAEGasStack fromPacket(PacketBuffer buffer) {
        boolean isCraftable = buffer.readBoolean();
        Gas gas = buffer.readRegistryIdUnsafe(MekanismAPI.gasRegistry());
        long stackSize = buffer.readVarLong();
        long countRequestable = buffer.readVarLong();
        AEGasStack stack = new AEGasStack(gas, stackSize);
        stack.setCountRequestable(countRequestable);
        stack.setCraftable(isCraftable);
        return stack;
    }

    public static IAEGasStack fromNBT(CompoundNBT data) {
        ResourceLocation gasId = new ResourceLocation(data.getString(NBT_GAS_ID));
        Gas gas = MekanismAPI.gasRegistry().getValue(gasId);
        if (gas != null && gas != MekanismAPI.EMPTY_GAS) {
            long amount = data.getLong(NBT_STACKSIZE);
            AEGasStack gasStack = new AEGasStack(gas, amount);
            gasStack.setCountRequestable(data.getLong(NBT_REQUESTABLE));
            gasStack.setCraftable(data.getBoolean(NBT_CRAFTABLE));
            return gasStack;
        } else {
            return null;
        }
    }
}
