package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.ModuleConfigItem;
import morningsage.mekanismaddons.items.modules.IHasSmartEnable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ModuleConfigItem.class)
public abstract class ModuleConfigItemMixin<TYPE> {

    @Shadow
    @Final
    private String name;

    @Shadow
    @Final
    private Module module;

    @Inject(
        at = @At("HEAD"),
        method = "set",
        remap = false
    )
    public void set(TYPE type, Consumer<ItemStack> callback, CallbackInfo callbackInfo) {
        if (type == Boolean.TRUE && this.name.equals("enabled") && this.module instanceof IHasSmartEnable) {
            ((IHasSmartEnable) this.module).onSmartEnableToggle();
        }
    }

}
