package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.common.content.gear.Modules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Modules.class)
public interface ModulesAccessor {

    @Final @Accessor("MODULES")
    static Map<String, Modules.ModuleData<?>> getModules() {
        throw new Error("Failed to access `MODULES`");
    }

}
