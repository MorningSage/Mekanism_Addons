package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.energy.IEnergyContainer;
import mekanism.common.content.gear.IModuleContainerItem;
import mekanism.common.item.gear.ItemMekaTool;
import morningsage.mekanismaddons.events.mekatool.MekaToolBlockBreakEvent;
import morningsage.mekanismaddons.events.mekatool.MekaToolUsageEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(ItemMekaTool.class)
public abstract class ItemMekaToolMixin implements IModuleContainerItem {

    @Shadow
    protected abstract boolean breakBlock(ItemStack stack, World world, BlockPos pos, ServerPlayerEntity player, IEnergyContainer energyContainer, boolean silk);

    @Inject(
        at = @At("HEAD"),
        method = "onBlockStartBreak",
        remap = false
    )
    public void onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfo) {
        MekaToolBlockBreakEvent.invoke(stack, pos, player, this::breakBlock);
    }

    @Inject(
        at = @At("HEAD"),
        method = "use",
        cancellable = true
    )
    public void use(World world, PlayerEntity player, @Nonnull Hand hand, CallbackInfoReturnable<ActionResult<ItemStack>> callbackInfo) {
        ActionResult<ItemStack> result = MekaToolUsageEvent.Use.invoke(world, player, hand);
        if (result != null) callbackInfo.setReturnValue(result);
    }

    @Inject(
        at = @At("HEAD"),
        method = "useOn",
        cancellable = true
    )
    public void useOn(ItemUseContext context, CallbackInfoReturnable<ActionResultType> callbackInfo) {
        ActionResultType result = MekaToolUsageEvent.UseOn.invoke(context);
        if (result != null) callbackInfo.setReturnValue(result);
    }

}
