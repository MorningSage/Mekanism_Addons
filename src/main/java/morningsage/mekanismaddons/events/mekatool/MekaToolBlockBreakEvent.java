package morningsage.mekanismaddons.events.mekatool;

import mekanism.api.energy.IEnergyContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class MekaToolBlockBreakEvent extends Event {
    private final ItemStack stack;
    private final BlockPos pos;
    private final PlayerEntity player;
    private final BreakBlockCallback callback;

    private MekaToolBlockBreakEvent(ItemStack stack, BlockPos pos, PlayerEntity player, BreakBlockCallback callback) {
        this.stack = stack;
        this.pos = pos;
        this.player = player;
        this.callback = callback;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public boolean breakBlock(ItemStack stack, World world, BlockPos pos, ServerPlayerEntity player, IEnergyContainer energyContainer, boolean silk) {
        return callback.breakBlock(stack, world, pos, player, energyContainer, silk);
    }

    public static void invoke(ItemStack stack, BlockPos pos, PlayerEntity player, MekaToolBlockBreakEvent.BreakBlockCallback callback) {
        MinecraftForge.EVENT_BUS.post(new MekaToolBlockBreakEvent(stack, pos, player, callback));
    }

    @FunctionalInterface
    public interface BreakBlockCallback {
        boolean breakBlock(ItemStack stack, World world, BlockPos pos, ServerPlayerEntity player, IEnergyContainer energyContainer, boolean silk);
    }
}
