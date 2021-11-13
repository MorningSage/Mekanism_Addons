package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.tile.prefab.TileEntityElectricMachine;
import morningsage.mekanismaddons.recipes.SmartItemStackOutputHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityElectricMachine.class)
public abstract class TileEntityElectricMachineMixin {

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lmekanism/api/recipes/outputs/OutputHelper;getOutputHandler(Lmekanism/api/inventory/IInventorySlot;)Lmekanism/api/recipes/outputs/IOutputHandler;"
        ),
        method = "<init>",
        remap = false
    )
    protected IOutputHandler<ItemStack> init(IInventorySlot outputSlot) {
        return SmartItemStackOutputHandler.getOutputHandler(OutputHelper.getOutputHandler(outputSlot), (TileEntityElectricMachine) (Object) this, outputSlot);
    }

}
