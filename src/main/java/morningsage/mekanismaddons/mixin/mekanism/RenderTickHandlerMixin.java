package morningsage.mekanismaddons.mixin.mekanism;

import mekanism.client.render.RenderTickHandler;
import morningsage.mekanismaddons.utils.ClientUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(RenderTickHandler.class)
public abstract class RenderTickHandlerMixin {

    @Inject(
        at = @At("HEAD"),
        method = "onBlockHover",
        remap = false
    )
    public void onBlockHover(DrawHighlightEvent.HighlightBlock event, CallbackInfo callbackInfo) {
        ClientUtils.onBlockHover(event);
    }

}
