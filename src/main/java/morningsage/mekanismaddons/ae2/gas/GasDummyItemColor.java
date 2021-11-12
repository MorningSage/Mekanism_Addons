package morningsage.mekanismaddons.ae2.gas;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GasDummyItemColor implements IItemColor {

    @Override
    public int getColor(ItemStack stack, int p_getColor_2_) {
        Item item = stack.getItem();

        return item instanceof GasDummyItem ? ((GasDummyItem )item).getGasStack(stack).getChemicalTint() : -1;
    }

}
