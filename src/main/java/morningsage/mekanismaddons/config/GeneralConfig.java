package morningsage.mekanismaddons.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.config.value.CachedLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class GeneralConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;

    public final CachedLongValue mekaSuitSodiumTransferRate;
    public final CachedLongValue mekaSuitSodiumMaxStorage;
    public final CachedIntValue mekaSuitSodiumUsageRate;
    public final CachedFloatingLongValue mekaSuitHeatedSodiumCoolRate;
    public final CachedFloatingLongValue mekaSuitEnergyUsageWalkBoost;
    public final CachedBooleanValue ambulationAccelerationUnitEnabled;
    public final CachedBooleanValue aoeUnitEnabled;
    public final CachedBooleanValue atmosphericAvidityUnitEnabled;
    public final CachedBooleanValue dihydrogenMonoxideAvidityUnitEnabled;
    public final CachedBooleanValue capturingUnitEnabled;
    public final CachedBooleanValue ignitionRetardationUnitEnabled;
    public final CachedBooleanValue luckUpgradeEnabled;

    GeneralConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");
        this.mekaSuitSodiumTransferRate = CachedLongValue.wrap(this, builder.comment("Rate at which Sodium can be transferred into the Ignition Retardation Unit.").defineInRange("mekaSuitSodiumTransferRate", 256L, 1L, 9223372036854775807L));
        this.mekaSuitSodiumMaxStorage = CachedLongValue.wrap(this, builder.comment("Maximum amount of Sodium storable by the Ignition Retardation Unit.").defineInRange("mekaSuitSodiumMaxStorage", 128000L, 1L, 9223372036854775807L));
        this.mekaSuitSodiumUsageRate = CachedIntValue.wrap(this, builder.comment("Sodium usage of the Ignition Retardation Unit when removing fire effects.").define("mekaSuitSodiumUsageRate", 1234));
        this.mekaSuitHeatedSodiumCoolRate = CachedFloatingLongValue.define(this, builder, "Heated Sodium cool rate after being used by the Ignition Retardation Unit.", "mekaSuitHeatedSodiumCoolRate", FloatingLong.createConst(66L));
        this.mekaSuitEnergyUsageWalkBoost = CachedFloatingLongValue.define(this, builder, "Energy usage (Joules) of MekaSuit when adding 0.1 to walk motion.", "mekaSuitEnergyUsageWalkBoost", FloatingLong.createConst(100L));
        builder.comment("Modules").push("modules");
        this.ambulationAccelerationUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Ambulation Acceleration Unit").define("ambulationAccelerationUnitEnabled", true));
        this.aoeUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the AOE Unit").define("aoeUnitEnabled", true));
        this.atmosphericAvidityUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Atmospheric Avidity Unit").define("atmosphericAvidityUnitEnabled", true));
        this.dihydrogenMonoxideAvidityUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Dihydrogen Monoxide Avidity Unit").define("dihydrogenMonoxideAvidityUnitEnabled", true));
        this.capturingUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Capturing Unit").define("capturingUnitEnabled", true));
        this.ignitionRetardationUnitEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Ignition Retardation Unit").define("ignitionRetardationUnitEnabled", true));
        builder.pop();
        builder.comment("Upgrades").push("upgrades");
        this.luckUpgradeEnabled = CachedBooleanValue.wrap(this, builder.comment("Whether to apply the effects of the Luck Upgrade").define("luckUpgradeEnabled", true));
        builder.pop();
        builder.pop();
        this.configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "addons";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return this.configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
