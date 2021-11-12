package morningsage.mekanismaddons.mixin.minecraft;

import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemAccessor {

    @Mutable @Accessor("BY_ID")
    static Map<EntityType<?>, SpawnEggItem> getEggLookups() {
        throw new Error("Failed to access `BY_ID`");
    }

}
