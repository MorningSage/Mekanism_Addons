package morningsage.mekanismaddons.items.modules;


import mekanism.api.IIncrementalEnum;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.mekatool.ModuleMekaTool;
import mekanism.common.registries.MekanismItems;
import morningsage.mekanismaddons.MekanismAddonsLang;
import morningsage.mekanismaddons.config.AddonConfig;
import morningsage.mekanismaddons.events.mekatool.MekaToolBlockBreakEvent;
import morningsage.mekanismaddons.utils.AOEUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModuleAOEUnit extends ModuleMekaTool implements IHasSmartEnable {
    private ModuleConfigItem<AOERange> range;

    public static final Set<Modules.ModuleData<?>> INCOMPATIBLE = new HashSet<>(
        Collections.singletonList(
            Modules.VEIN_MINING_UNIT
        )
    );

    @Override
    public ItemStack getContainer() {
        return super.getContainer();
    }

    @Override
    public Set<Modules.ModuleData<?>> getIncompatibleModules() {
        return INCOMPATIBLE;
    }

    @Override
    public void init() {
        super.init();
        this.addConfigItem(
            range = new ModuleConfigItem<>(
                this, "aoe_range", MekanismAddonsLang.MODULE_AOE_UNIT, new ModuleConfigItem.EnumData<>(AOERange.class, getInstalledCount() + 1), ModuleAOEUnit.AOERange.ONE
            )
        );
    }

    @Override
    public void addHUDStrings(List<ITextComponent> list) {
        if (AddonConfig.general.aoeUnitEnabled.get() && isEnabled()) {
            list.add(MekanismAddonsLang.MODULE_AOE.translateColored(EnumColor.DARK_GRAY, EnumColor.INDIGO, getModeString()));
        }
    }

    public String getModeString() {
        if (!isEnabled()) return "";
        int diameter = range.get().getRadius() * 2 + 1;
        return diameter + "x" + diameter;
    }

    @Override
    public void changeMode(@Nonnull PlayerEntity player, @Nonnull ItemStack stack, int shift, boolean displayChangeMessage) {
        if (!isEnabled() || !AddonConfig.general.aoeUnitEnabled.get()) return;

        AOERange newMode = range.get().adjust(shift);

        if (range.get() != newMode) {
            range.set(newMode, null);

            if (displayChangeMessage) {
                player.sendMessage(MekanismLang.LOG_FORMAT.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM, EnumColor.GRAY, MekanismLang.MODULE_MODE_CHANGE.translate(MekanismAddonsLang.MODULE_AOE_MODE.translate(), EnumColor.INDIGO, getModeString())), Util.NIL_UUID);
            }
        }
    }

    public void doBreakAOE(ServerPlayerEntity player, Iterable<BlockPos> area, BlockPos centerPos, MekaToolBlockBreakEvent.BreakBlockCallback callback) {
        if (!AddonConfig.general.aoeUnitEnabled.get()) return;

        IEnergyContainer energyContainer = getEnergyContainer();
        if (energyContainer == null) return;

        ItemStack stack = getContainer();
        World world = player.level;

        BlockState centerState = world.getBlockState(centerPos);
        boolean silk = MekanismItems.MEKA_TOOL.get().isModuleEnabled(stack, Modules.SILK_TOUCH_UNIT);

        for (BlockPos queryPos : area) {
            BlockState queryState = world.getBlockState(queryPos);
            boolean isBlockValid = AOEUtils.canBlockAOE(world, queryPos, queryState, null, centerPos, centerState) && !queryPos.equals(centerPos);

            if (isBlockValid) callback.breakBlock(stack, world, queryPos, player, energyContainer, silk);
        }
    }

    public AOERange getRange() {
        return range.get();
    }

    public enum AOERange implements IIncrementalEnum<AOERange>, IHasTextComponent {
        ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);

        private static final AOERange[] MODES = values();
        private final int radius;
        private final ITextComponent label;

        AOERange(int radius) {
            this.radius = radius;
            this.label = TextComponentUtil.getString(radius == 0 ? "-" : String.valueOf(radius * 2 + 1));
        }

        @Override
        public ITextComponent getTextComponent() {
            return label;
        }

        public int getRadius() {
            return radius;
        }

        @Nonnull
        public AOERange byIndex(int index) {
            return byIndexStatic(index);
        }

        public static AOERange byIndexStatic(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }
    }
}
