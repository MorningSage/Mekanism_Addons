package morningsage.mekanismaddons.ae2.gas;

import appeng.api.storage.data.IAEStack;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;

public interface IAEGasStack extends IAEStack<IAEGasStack> {
    GasStack getGasStack();

    void add(IAEGasStack var1);

    IAEGasStack copy();

    Gas getGas();
}
