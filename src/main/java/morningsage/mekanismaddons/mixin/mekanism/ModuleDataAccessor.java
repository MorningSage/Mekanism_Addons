package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.api.text.ILangEntry;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(value = Modules.ModuleData.class, remap = false)
public interface ModuleDataAccessor {

    @Invoker("<init>")
    static <MODULE extends Module> Modules.ModuleData<MODULE> createNewInstance(String name, ILangEntry langEntry, ILangEntry description, Supplier<MODULE> supplier, int maxStackSize) {
        throw new Error("Failed to invoke ModuleData init");
    }

    @Final @Accessor("maxStackSize")
    void setMaxStackSize(int maxStackSize);

}
