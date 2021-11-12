package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.content.gear.mekasuit.ModuleMekaSuit;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModuleMekaSuit.ModuleNutritionalInjectionUnit.class)
public abstract class ModuleNutritionalInjectionUnitMixin {

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;canEat(Z)Z"
        ),
        method = "tickServer"
    )
    public boolean canEat(PlayerEntity player, boolean asdf) {
        ModuleMekaSuit.ModuleNutritionalInjectionUnit self = (ModuleMekaSuit.ModuleNutritionalInjectionUnit) (Object) this;
        return player.canEat(asdf) || (self.getInstalledCount() > 1 && player.getFoodData().getSaturationLevel() != 20);
    }

}
