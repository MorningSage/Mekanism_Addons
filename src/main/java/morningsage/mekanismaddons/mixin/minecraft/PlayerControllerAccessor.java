package morningsage.mekanismaddons.mixin.minecraft;

import net.minecraft.client.multiplayer.PlayerController;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(PlayerController.class)
public interface PlayerControllerAccessor {

    @Accessor("destroyProgress")
    float getDestroyProgress();

}
