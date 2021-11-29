package morningsage.mekanismaddons.recipes;

import javax.annotation.Nonnull;

import mekanism.api.Upgrade;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.base.TileEntityMekanism;
import morningsage.mekanismaddons.config.AddonConfig;
import morningsage.mekanismaddons.mixin.mekanism.OutputHelperAccessor;
import morningsage.mekanismaddons.utils.EnumNames;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SmartItemStackOutputHandler implements IOutputHandler<ItemStack> {
    private final TileEntityMekanism tileEntity;
    private final IInventorySlot inventorySlot;

    public SmartItemStackOutputHandler(TileEntityMekanism tileEntity, final IInventorySlot inventorySlot) {
        this.tileEntity = tileEntity;
        this.inventorySlot = inventorySlot;
    }

    @Override
    public void handleOutput(@Nonnull ItemStack toOutput, int operations) {
        World level = tileEntity.getLevel();

        double effect = Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), getInstalledCount(Upgrade.valueOf(EnumNames.LUCK.toUpperCase())) / (float) Upgrade.valueOf(EnumNames.LUCK.toUpperCase()).getMax());

        if (level != null && level.random.nextFloat() < effect / 500.0F) operations <<= 1;

        OutputHelperAccessor.handleOutput(inventorySlot, toOutput, operations);
    }

    @Override
    public int operationsRoomFor(@Nonnull ItemStack toOutput, int currentMax) {
        return OutputHelperAccessor.operationsRoomFor(inventorySlot, toOutput, currentMax);
    }

    private int getInstalledCount(Upgrade upgrade) {
        return tileEntity.getComponent().getUpgrades(upgrade);
    }

    public static IOutputHandler<ItemStack> getOutputHandler(IOutputHandler<ItemStack> defaultValue, TileEntityMekanism tileEntity, final IInventorySlot inventorySlot) {
        if (!AddonConfig.general.luckUpgradeEnabled.get()) return defaultValue;
        return new SmartItemStackOutputHandler(tileEntity, inventorySlot);
    }
}
