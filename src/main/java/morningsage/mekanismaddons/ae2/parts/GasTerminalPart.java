package morningsage.mekanismaddons.ae2.parts;

import appeng.api.parts.IPartModel;
import appeng.container.me.fluids.FluidTerminalContainer;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractTerminalPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GasTerminalPart extends AbstractTerminalPart {
    @PartModels
    public static final ResourceLocation MODEL_OFF = new ResourceLocation("mekanismaddons", "part/gas_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = new ResourceLocation("mekanismaddons", "part/gas_terminal_on");
    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

    public GasTerminalPart(ItemStack is) {
        super(is);
    }

    public ContainerType<?> getContainerType(PlayerEntity player) {
        return FluidTerminalContainer.TYPE;
    }

    @Override @Nonnull
    public IPartModel getStaticModels() {
        return this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
    }
}

