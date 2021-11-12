package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.capabilities.chemical.item.RateLimitMultiTankGasHandler;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import morningsage.mekanismaddons.events.mekasuit.MekaSuitArmorInit;
import morningsage.mekanismaddons.events.mekasuit.MekaSuitArmorTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(ItemMekaSuitArmor.class)
public abstract class ItemMekaSuitArmorMixin {

    @Shadow @Final @Mutable private Set<RateLimitMultiTankGasHandler.GasTankSpec> gasTankSpecs;

    @Inject(
        at = @At("TAIL"),
        method = "<init>",
        remap = false
    )
    public void init(EquipmentSlotType slot, Item.Properties properties, CallbackInfo callbackInfo) {
        // Make the list ordered
        this.gasTankSpecs = new LinkedHashSet<>(this.gasTankSpecs);
        MekaSuitArmorInit.invoke((ItemMekaSuitArmor) (Object) this, this.gasTankSpecs::add);
    }

    @Inject(
        at = @At("TAIL"),
        method = "onArmorTick",
        remap = false
    )
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player, CallbackInfo callbackInfo) {
        if (world.isClientSide()) {
            MekaSuitArmorTick.Client.invoke(stack, world, player);
        } else {
            MekaSuitArmorTick.Server.invoke(stack, world, player);
        }
    }

}
