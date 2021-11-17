package morningsage.mekanismaddons.plugins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface IDigitalStorage {
    String getEncryptionKey(ItemStack item);

    void setEncryptionKey(ItemStack item, String encKey, String name);

    String getModId();

    ActionResult<ItemStack> useCustomTerminal(World world, PlayerEntity player, ItemStack stack, Hand hand);

    ActionResultType useCustomTerminalOn(ItemUseContext context);

    boolean canHandle(ItemStack itemStack);

    boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack);

    boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack);
}
