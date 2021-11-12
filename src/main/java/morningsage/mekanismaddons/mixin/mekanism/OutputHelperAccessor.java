package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.inventory.IInventorySlot;
import mekanism.api.recipes.outputs.OutputHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(OutputHelper.class)
public interface OutputHelperAccessor {

    @Invoker("handleOutput")
    static void handleOutput(IInventorySlot inventorySlot, ItemStack toOutput, int operations) {
        throw new Error("Failed to invoke `handleOutput`");
    }

    @Invoker("operationsRoomFor")
    static int operationsRoomFor(IInventorySlot inventorySlot, ItemStack toOutput, int currentMax) {
        throw new Error("Failed to invoke `operationsRoomFor`");
    }

}
