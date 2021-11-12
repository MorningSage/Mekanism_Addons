package morningsage.mekanismaddons.items.modules;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.text.ILangEntry;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.chemical.item.RateLimitMultiTankGasHandler;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.Module;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.Modules.ModuleData;
import mekanism.common.item.ItemModule;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismGases;
import mekanism.common.registries.MekanismItems;
import morningsage.mekanismaddons.MekanismAddonsLang;
import morningsage.mekanismaddons.events.*;
import morningsage.mekanismaddons.events.mekasuit.MekaSuitArmorInit;
import morningsage.mekanismaddons.events.mekasuit.MekaSuitArmorTick;
import morningsage.mekanismaddons.events.mekatool.MekaToolBlockBreakEvent;
import morningsage.mekanismaddons.events.mekatool.MekaToolDestroySpeedEvent;
import morningsage.mekanismaddons.mixin.mekanism.ModuleDataAccessor;
import morningsage.mekanismaddons.mixin.mekanism.ModulesAccessor;
import morningsage.mekanismaddons.utils.AOEUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class AddonModules {
    public static final Map<String, Modules.ModuleData<?>> ADDON_MODULES_BY_NAME = new Object2ObjectOpenHashMap<>();
    public static final Map<ModuleData<?>, ItemRegistryObject<? extends ItemModule>> ADDON_MODULES_BY_DATA = new LinkedHashMap<>();

    public static ModuleData<ModuleDihydrogenMonoxideAvidityUnit> DIHYDROGEN_MONOXIDE_AVIDITY_UNIT;
    public static ModuleData<ModuleAtmosphericAvidityUnit> ATMOSPHERIC_AVIDITY_UNIT;
    public static ModuleData<ModuleCapturingUnit> CAPTURING_UNIT;
    public static ModuleData<ModuleAOEUnit> AOE_UNIT;
    public static ModuleData<ModuleAmbulationAccelerationUnit> AMBULATION_ACCELERATION_UNIT;
    public static ModuleData<ModuleIgnitionRetardationUnit> IGNITION_RETARDATION_UNIT;

    static {
        DIHYDROGEN_MONOXIDE_AVIDITY_UNIT = register(
            "dihydrogen_monoxide_avidity_unit",
            MekanismAddonsLang.MODULE_DIHYDROGEN_MONOXIDE_AVIDITY_UNIT,
            MekanismAddonsLang.DESCRIPTION_DIHYDROGEN_MONOXIDE_AVIDITY_UNIT,
            ModuleDihydrogenMonoxideAvidityUnit::new
        ).rarity(Rarity.UNCOMMON);

        ATMOSPHERIC_AVIDITY_UNIT = register(
            "atmospheric_avidity_unit",
            MekanismAddonsLang.MODULE_ATMOSPHERIC_AVIDITY_UNIT,
            MekanismAddonsLang.DESCRIPTION_ATMOSPHERIC_AVIDITY_UNIT,
            ModuleAtmosphericAvidityUnit::new
        ).rarity(Rarity.UNCOMMON);

        CAPTURING_UNIT = register(
            "capturing_unit",
            MekanismAddonsLang.MODULE_CAPTURING_UNIT,
            MekanismAddonsLang.DESCRIPTION_CAPTURING_UNIT,
            ModuleCapturingUnit::new,
            5
        ).rarity(Rarity.RARE);

        AOE_UNIT = register(
            "aoe_unit",
            MekanismAddonsLang.MODULE_AOE_UNIT,
            MekanismAddonsLang.DESCRIPTION_AOE_UNIT,
            ModuleAOEUnit::new,
            ModuleAOEUnit.AOERange.values().length - 1
        ).rarity(Rarity.RARE).setExclusive().setRendersHUD().setDisabledByDefault().setHandlesModeChange();

        AMBULATION_ACCELERATION_UNIT = register(
            "ambulation_acceleration_unit",
            MekanismAddonsLang.MODULE_AMBULATION_ACCELERATION_UNIT,
            MekanismAddonsLang.DESCRIPTION_AMBULATION_ACCELERATION_UNIT,
            ModuleAmbulationAccelerationUnit::new,
            4
        ).rarity(Rarity.RARE);

        IGNITION_RETARDATION_UNIT = register(
            "ignition_retardation_unit",
            MekanismAddonsLang.MODULE_IGNITION_RETARDATION_UNIT,
            MekanismAddonsLang.DESCRIPTION_IGNITION_RETARDATION_UNIT,
            ModuleIgnitionRetardationUnit::new,
            4
        ).rarity(Rarity.RARE);

        Modules.VEIN_MINING_UNIT.setExclusive();

        ((ModuleDataAccessor) Modules.NUTRITIONAL_INJECTION_UNIT).setMaxStackSize(2);

        MinecraftForge.EVENT_BUS.addListener(AddonModules::onMekaToolBreakBlock);
        MinecraftForge.EVENT_BUS.addListener(AddonModules::onMekaToolDestroySpeed);
        MinecraftForge.EVENT_BUS.addListener(AddonModules::onGetBreakSpeed);
        MinecraftForge.EVENT_BUS.addListener(AddonModules::onLivingEntityDrops);
        MinecraftForge.EVENT_BUS.addListener(AddonModules::onMekaSuitArmorTick);
        MinecraftForge.EVENT_BUS.addListener(AddonModules::onPlayerBurn);

        CustomEventBus.EVENT_BUS.addListener(AddonModules::onMekaSuitInit);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(AddonModules::setupModules);

    }

    private static <M extends Module> Modules.ModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier) {
        return register(name, langEntry, description, moduleSupplier, 1);
    }
    private static <M extends Module> Modules.ModuleData<M> register(String name, ILangEntry langEntry, ILangEntry description, Supplier<M> moduleSupplier, int maxStackSize) {
        Modules.ModuleData<M> module = ModuleDataAccessor.createNewInstance(name, langEntry, description, moduleSupplier, maxStackSize);
        ADDON_MODULES_BY_NAME.put(name, module);
        return module;
    }

    public static void onMekaToolBreakBlock(final MekaToolBlockBreakEvent event) {
        if (event.isCanceled() || !(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ModuleAOEUnit module = Modules.load(event.getStack(), AddonModules.AOE_UNIT);
        if (module == null) return;
        module.doBreakAOE((ServerPlayerEntity) event.getPlayer(), AOEUtils.getAOEArea(event.getPlayer(), event.getPos(), module, Dist.DEDICATED_SERVER), event.getPos(), event::breakBlock);
    }
    public static void onMekaToolDestroySpeed(final MekaToolDestroySpeedEvent event) {
        if (event.isCanceled() || event.getDestroySpeed() <= 0 || event.getPos() == null) return;

        ModuleAOEUnit module = Modules.load(event.getStack(), AddonModules.AOE_UNIT);
        if (module == null || !AOEUtils.canPlayerAOE(event.getPlayer(), module, null)) return;

        Iterable<BlockPos> area = AOEUtils.getAOEArea(event.getPlayer(), event.getPos(), module, null);
        PlayerEntity player = event.getPlayer();
        float hardestHardness = 0;

        BlockState centerState = player.level.getBlockState(event.getPos());

        for (BlockPos blockPos : area) {
            BlockState state = player.level.getBlockState(blockPos);
            VoxelShape shape = state.getShape(player.level, blockPos);

            if (!AOEUtils.canBlockAOE(player.level, blockPos, state, shape, event.getPos(), centerState)) continue;

            float hardness = state.getDestroySpeed(player.level, blockPos);

            if (hardness > hardestHardness) hardestHardness = hardness;
        }

        event.setDestroySpeed(hardestHardness);
    }

    public static void setupModules(final FMLCommonSetupEvent event) {
        Modules.setSupported(MekanismItems.MEKASUIT_HELMET.getItem(), AddonModules.DIHYDROGEN_MONOXIDE_AVIDITY_UNIT, AddonModules.ATMOSPHERIC_AVIDITY_UNIT);
        Modules.setSupported(MekanismItems.MEKA_TOOL.getItem(), AddonModules.CAPTURING_UNIT, AddonModules.AOE_UNIT);
        Modules.setSupported(MekanismItems.MEKASUIT_PANTS.getItem(), AddonModules.AMBULATION_ACCELERATION_UNIT, AddonModules.IGNITION_RETARDATION_UNIT);
        Modules.resetSupportedContainers();
        Modules.processSupportedContainers();

        for (Modules.ModuleData<?> module : AddonModules.ADDON_MODULES_BY_NAME.values()) {
            ModulesAccessor.getModules().put(module.getName(), module);
        }
    }

    public static void onGetBreakSpeed(final PlayerEvent.BreakSpeed event) {
        LivingEntity entity = event.getEntityLiving();
        ItemStack container = entity.getItemBySlot(EquipmentSlotType.HEAD);

        ModuleDihydrogenMonoxideAvidityUnit moduleWater = Modules.load(container, AddonModules.DIHYDROGEN_MONOXIDE_AVIDITY_UNIT);
        if (moduleWater != null) moduleWater.adjustMiningSpeed(event);

        ModuleAtmosphericAvidityUnit moduleAir = Modules.load(container, AddonModules.ATMOSPHERIC_AVIDITY_UNIT);
        if (moduleAir != null) moduleAir.adjustMiningSpeed(event);
    }

    public static void onLivingEntityDrops(final LivingDropsEvent event) {
        Entity killer = event.getSource().getEntity();
        if (!(killer instanceof LivingEntity)) return;

        ItemStack container = ((LivingEntity) killer).getMainHandItem();

        ModuleCapturingUnit moduleCapturing = Modules.load(container, AddonModules.CAPTURING_UNIT);
        if (moduleCapturing != null) moduleCapturing.dropLoot(event);
    }

    public static void onMekaSuitInit(final MekaSuitArmorInit event) {
        if (event.getArmor().getSlot() == EquipmentSlotType.LEGS) {
            event.addGasTank(
                new RateLimitMultiTankGasHandler.GasTankSpec(
                    MekanismConfig.gear.mekaSuitNutritionalTransferRate,
                    MekanismConfig.gear.mekaSuitNutritionalMaxStorage,
                    (gas, automationType) -> automationType != AutomationType.EXTERNAL,
                    (gas, automationType) -> true,
                    (gas) -> gas == MekanismGases.SODIUM.get()
                )
            );

            event.addGasTank(
                new RateLimitMultiTankGasHandler.GasTankSpec(
                    MekanismConfig.gear.mekaSuitNutritionalTransferRate,
                    MekanismConfig.gear.mekaSuitNutritionalMaxStorage,
                    (gas, automationType) -> automationType != AutomationType.EXTERNAL,
                    (gas, automationType) -> automationType != AutomationType.EXTERNAL,
                    gas -> gas == MekanismGases.SUPERHEATED_SODIUM.get()
                )
            );
        }
    }

    public static void onPlayerBurn(final LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();

        ItemStack container = entity.getItemBySlot(EquipmentSlotType.LEGS);
        ModuleIgnitionRetardationUnit moduleIgnition = Modules.load(container, AddonModules.IGNITION_RETARDATION_UNIT);

        if (moduleIgnition != null) {
            if (moduleIgnition.handleDamageSource(entity, event.getSource())) {
                event.setCanceled(true);
            }
        }
    }

    public static void onMekaSuitArmorTick(final MekaSuitArmorTick.Server event) {
        Optional<IGasHandler> capability = event.getStack().getCapability(Capabilities.GAS_HANDLER_CAPABILITY).resolve();
        FloatingLong amount = MekanismConfig.gear.mekaSuitEnergyUsageSprintBoost.get().divide(1.5);

        if (capability.isPresent()) {
            IGasHandler gasHandler = capability.get();

            GasStack sodium = gasHandler.extractChemical(MekanismGases.SUPERHEATED_SODIUM.getStack(amount.getValue()), Action.EXECUTE);
            gasHandler.insertChemical(MekanismGases.SODIUM.getStack(sodium.getAmount()), Action.EXECUTE);
        }
    }
}