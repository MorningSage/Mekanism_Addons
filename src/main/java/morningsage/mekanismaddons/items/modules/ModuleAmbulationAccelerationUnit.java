package morningsage.mekanismaddons.items.modules;

import mekanism.api.IIncrementalEnum;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.MathUtils;
import mekanism.api.text.IHasTextComponent;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.mekasuit.ModuleMekaSuit;
import morningsage.mekanismaddons.MekanismAddonsLang;
import morningsage.mekanismaddons.config.AddonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class ModuleAmbulationAccelerationUnit extends ModuleMekaSuit {
    private ModuleConfigItem<WalkBoost> walkBoost;

    public ModuleAmbulationAccelerationUnit() { }

    public void init() {
        super.init();

        this.addConfigItem(this.walkBoost = new ModuleConfigItem<>(this, "walk_boost", MekanismAddonsLang.MODULE_WALK_BOOST, new ModuleConfigItem.EnumData<>(WalkBoost.class, this.getInstalledCount() + 1), WalkBoost.LOW));
    }

    public void tickServer(PlayerEntity player) {
        super.tickServer(player);
        if (this.canFunction(player)) {
            float boost = this.getBoost();

            if (!player.isOnGround()) boost /= 5.0F;
            if (player.isInWater()) boost /= 5.0F;

            player.moveRelative(boost, new Vector3d(0.0D, 0.0D, 1.0D));
            this.useEnergy(player, AddonConfig.general.mekaSuitEnergyUsageWalkBoost.get().multiply(this.getBoost() / 0.1F));
        }
    }

    public void tickClient(PlayerEntity player) {
        super.tickClient(player);
        if (this.canFunction(player)) {
            float boost = this.getBoost();

            if (!player.isOnGround()) boost /= 5.0F;
            if (player.isInWater()) boost /= 5.0F;

            player.moveRelative(boost, new Vector3d(0.0D, 0.0D, 1.0D));
        }
    }

    public boolean canFunction(PlayerEntity player) {
        if (!AddonConfig.general.ambulationAccelerationUnitEnabled.get()) return false;
        FloatingLong usage = AddonConfig.general.mekaSuitEnergyUsageWalkBoost.get().multiply(this.getBoost() / 0.1F);
        return player.zza > 0 && this.getContainerEnergy().greaterOrEqual(usage);
    }

    public float getBoost() {
        return this.walkBoost.get().getBoost();
    }

    public enum WalkBoost implements IHasTextComponent, IIncrementalEnum<WalkBoost> {
        OFF(0.0F),
        LOW(0.05F),
        MED(0.1F),
        HIGH(0.25F),
        ULTRA(0.5F);

        private static final WalkBoost[] MODES = values();
        private final float boost;
        private final ITextComponent label;

        WalkBoost(float boost) {
            this.boost = boost;
            this.label = new StringTextComponent(Float.toString(boost));
        }

        @Nonnull
        public WalkBoost byIndex(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }

        public ITextComponent getTextComponent() {
            return this.label;
        }

        public float getBoost() {
            return this.boost;
        }
    }
}
