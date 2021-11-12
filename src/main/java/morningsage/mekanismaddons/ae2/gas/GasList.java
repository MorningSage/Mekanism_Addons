package morningsage.mekanismaddons.ae2.gas;

import appeng.api.config.FuzzyMode;
import appeng.api.storage.data.IItemList;
import appeng.fluids.util.MeaningfulFluidIterator;

import java.util.*;

public class GasList  implements IItemList<IAEGasStack> {
    private final Map<IAEGasStack, IAEGasStack> records = new HashMap<>();

    public GasList() {
    }

    public void add(IAEGasStack option) {
        if (option != null) {
            IAEGasStack st = this.getGasRecord(option);
            if (st != null) {
                st.add(option);
            } else {
                IAEGasStack opt = option.copy();
                this.putGasRecord(opt);
            }
        }
    }

    public IAEGasStack findPrecise(IAEGasStack gasStack) {
        return gasStack == null ? null : this.getGasRecord(gasStack);
    }

    public Collection<IAEGasStack> findFuzzy(IAEGasStack filter, FuzzyMode fuzzy) {
        return filter == null ? Collections.emptyList() : Collections.singletonList(this.findPrecise(filter));
    }

    public boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    public void addStorage(IAEGasStack option) {
        if (option != null) {
            IAEGasStack st = this.getGasRecord(option);
            if (st != null) {
                st.incStackSize(option.getStackSize());
            } else {
                IAEGasStack opt = option.copy();
                this.putGasRecord(opt);
            }
        }
    }

    public void addCrafting(IAEGasStack option) {
        if (option != null) {
            IAEGasStack st = this.getGasRecord(option);
            if (st != null) {
                st.setCraftable(true);
            } else {
                IAEGasStack opt = option.copy();
                opt.setStackSize(0L);
                opt.setCraftable(true);
                this.putGasRecord(opt);
            }
        }
    }

    public void addRequestable(IAEGasStack option) {
        if (option != null) {
            IAEGasStack st = this.getGasRecord(option);
            if (st != null) {
                st.setCountRequestable(st.getCountRequestable() + option.getCountRequestable());
            } else {
                IAEGasStack opt = option.copy();
                opt.setStackSize(0L);
                opt.setCraftable(false);
                opt.setCountRequestable(option.getCountRequestable());
                this.putGasRecord(opt);
            }
        }
    }

    public IAEGasStack getFirstItem() {
        Iterator<IAEGasStack> var1 = this.iterator();
        if (var1.hasNext()) {
            return var1.next();
        } else {
            return null;
        }
    }

    public int size() {
        return this.records.values().size();
    }

    public Iterator<IAEGasStack> iterator() {
        return new MeaningfulFluidIterator<>(this.records.values().iterator());
    }

    public void resetStatus() {
        for (IAEGasStack iaeGasStack : this) {
            iaeGasStack.reset();
        }
    }

    private IAEGasStack getGasRecord(IAEGasStack gas) {
        return this.records.get(gas);
    }

    private IAEGasStack putGasRecord(IAEGasStack gas) {
        return this.records.put(gas, gas);
    }
}
