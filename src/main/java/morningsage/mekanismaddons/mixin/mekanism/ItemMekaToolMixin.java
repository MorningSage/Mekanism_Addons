package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.energy.IEnergyContainer;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.events.mekatool.MekaToolBlockBreakEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemMekaTool.class)
public abstract class ItemMekaToolMixin implements IModuleContainerItem {

    @Shadow protected abstract boolean breakBlock(ItemStack stack, World world, BlockPos pos, ServerPlayerEntity player, IEnergyContainer energyContainer, boolean silk);

    @Inject(
        at = @At("HEAD"),
        method = "onBlockStartBreak",
        remap = false
    )
    public void onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfo) {
        MekaToolBlockBreakEvent.invoke(stack, pos, player, this::breakBlock);
    }

}
