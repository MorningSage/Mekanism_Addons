package morningsage.mekanismaddons.ae2.parts;

import appeng.api.config.RedstoneMode;
import appeng.api.config.Upgrades;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.util.AECableType;
import appeng.container.ContainerLocator;
import appeng.container.ContainerOpener;
import appeng.core.Api;
import appeng.fluids.helper.IConfigurableFluidInventory;
import appeng.fluids.util.AEFluidInventory;
import appeng.fluids.util.IAEFluidTank;
import appeng.me.GridAccessException;
import appeng.parts.automation.UpgradeablePart;
import morningsage.mekanismaddons.ae2.channels.IGasStorageChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class SharedGasBusPart extends UpgradeablePart implements IGridTickable, IConfigurableFluidInventory {
    private final AEFluidInventory config = new AEFluidInventory(null, 9);
    private boolean lastRedstone;

    public SharedGasBusPart(ItemStack is) {
        super(is);
    }

    public void upgradesChanged() {
        this.updateState();
    }

    public void onNeighborChanged(IBlockReader w, BlockPos pos, BlockPos neighbor) {
        this.updateState();
        if (this.lastRedstone != this.getHost().hasRedstone(this.getSide())) {
            this.lastRedstone = !this.lastRedstone;
            if (this.lastRedstone && this.getRSMode() == RedstoneMode.SIGNAL_PULSE) {
                this.doBusWork();
            }
        }

    }

    private void updateState() {
        try {
            if (!this.isSleeping()) {
                this.getProxy().getTick().wakeDevice(this.getProxy().getNode());
            } else {
                this.getProxy().getTick().sleepDevice(this.getProxy().getNode());
            }
        } catch (GridAccessException var2) {
        }

    }

    @Override
    public boolean onPartActivate(PlayerEntity player, Hand hand, Vector3d pos) {
        if (!this.isRemote()) {
            ContainerOpener.openContainer(this.getContainerType(), player, ContainerLocator.forPart(this));
        }

        return true;
    }

    protected abstract ContainerType<?> getContainerType();

    @Override
    public void getBoxes(IPartCollisionHelper bch) {
        bch.addBox(6.0D, 6.0D, 11.0D, 10.0D, 10.0D, 13.0D);
        bch.addBox(5.0D, 5.0D, 13.0D, 11.0D, 11.0D, 14.0D);
        bch.addBox(4.0D, 4.0D, 14.0D, 12.0D, 12.0D, 16.0D);
    }

    protected TileEntity getConnectedTE() {
        TileEntity self = this.getHost().getTile();
        return this.getTileEntity(self, self.getBlockPos().relative(this.getSide().getFacing()));
    }

    private TileEntity getTileEntity(TileEntity self, BlockPos pos) {
        World w = self.getLevel();
        return w.getChunkSource().isTickingChunk(pos) ? w.getBlockEntity(pos) : null;
    }

    protected int calculateAmountToSend() {
        double amount = this.getChannel().transferFactor();
        switch(this.getInstalledUpgrades(Upgrades.SPEED)) {
            case 4:
                amount *= 1.5D;
            case 3:
                amount *= 2.0D;
            case 2:
                amount *= 4.0D;
            case 1:
                amount *= 8.0D;
            case 0:
            default:
                return MathHelper.floor(amount);
        }
    }

    @Override
    public void readFromNBT(CompoundNBT extra) {
        super.readFromNBT(extra);
        this.config.readFromNBT(extra, "config");
    }

    @Override
    public void writeToNBT(CompoundNBT extra) {
        super.writeToNBT(extra);
        this.config.writeToNBT(extra, "config");
    }

    public IAEFluidTank getConfig() {
        return this.config;
    }

    @Override
    public IFluidHandler getFluidInventoryByName(String name) {
        return name.equals("config") ? this.config : null;
    }

    protected IGasStorageChannel getChannel() {
        return Api.instance().storage().getStorageChannel(IGasStorageChannel.class);
    }

    @Override
    public float getCableConnectionLength(AECableType cable) {
        return 5.0F;
    }

    protected abstract TickRateModulation doBusWork();

    protected abstract boolean canDoBusWork();
}

