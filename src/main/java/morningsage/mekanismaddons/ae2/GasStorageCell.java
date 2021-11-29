package morningsage.mekanismaddons.ae2;

import javax.annotation.Nonnull;

import appeng.api.storage.IStorageChannel;
import appeng.core.Api;
import appeng.items.materials.MaterialType;
import appeng.items.storage.AbstractStorageCell;
import appeng.util.InventoryAdaptor;
import morningsage.mekanismaddons.ae2.channels.IGasStorageChannel;
import morningsage.mekanismaddons.ae2.gas.IAEGasStack;
import morningsage.mekanismaddons.utils.EnumNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.items.IItemHandler;

public class GasStorageCell extends AbstractStorageCell<IAEGasStack> {
    private final int perType;
    private final double idleDrain;

    public GasStorageCell(Properties properties, MaterialType whichCell, int kilobytes) {
        super(properties, whichCell, kilobytes);

        Tuple<Integer, Double> data = EnumNames.DATA_BY_TYPE.getOrDefault(whichCell, new Tuple<>(8, 0.0D));

        perType = data.getA();
        idleDrain = data.getB();
    }

    @Override
    protected void dropEmptyStorageCellCase(InventoryAdaptor inventoryAdaptor, PlayerEntity playerEntity) {
        Api.instance().definitions().materials().emptyStorageCell().maybeStack(1).ifPresent((is) -> {
            ItemStack extraA = inventoryAdaptor.addItems(is);
            if (!extraA.isEmpty()) {
                playerEntity.drop(extraA, false);
            }
        });
    }

    @Override
    public int getBytesPerType(@Nonnull ItemStack itemStack) {
        return perType;
    }

    @Override
    public double getIdleDrain() {
        return idleDrain;
    }

    @Nonnull
    @Override
    public IStorageChannel<IAEGasStack> getChannel() {
        return Api.instance().storage().getStorageChannel(IGasStorageChannel.class);
    }

    public int getTotalTypes(ItemStack cellItem) {
        return 5;
    }

    public IItemHandler getConfigInventory(ItemStack is) {
        return new GasCellConfig(is);
    }

}
