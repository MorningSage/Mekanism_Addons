package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.events.mekatool.MekaToolDestroySpeedEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(
        at = @At("HEAD"),
        method = "getDestroyProgress",
        cancellable = true
    )
    public void getDestroyProgress(BlockState pState, PlayerEntity pPlayer, IBlockReader pLevel, BlockPos pPos, CallbackInfoReturnable<Float> callbackInfo) {
        ItemStack stack = pPlayer.getMainHandItem();
        if (!(stack.getItem() instanceof ItemMekaTool)) return;

        float defaultValue = pState.getDestroySpeed(pLevel, pPos);
        float f = MekaToolDestroySpeedEvent.invoke(stack, pPos, pPlayer, defaultValue);

        if (f == -1.0F) {
            callbackInfo.setReturnValue(0.0F);
        } else {
            float i = ForgeHooks.canHarvestBlock(pState, pPlayer, pLevel, pPos) ? 30.0F : 100.0F;
            callbackInfo.setReturnValue(pPlayer.getDigSpeed(pState, pPos) / f / i);
        }
    }

}
