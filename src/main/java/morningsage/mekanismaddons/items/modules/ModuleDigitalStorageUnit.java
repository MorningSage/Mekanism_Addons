package morningsage.mekanismaddons.items.modules;

import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.MekanismLang;
import mekanism.common.content.gear.ModuleConfigItem;
import mekanism.common.content.gear.Modules;
import mekanism.common.content.gear.Modules.ModuleData;
import mekanism.common.content.gear.mekatool.ModuleMekaTool;
import morningsage.mekanismaddons.MekanismAddonsLang;
import morningsage.mekanismaddons.ae2.AE2Actions;
import morningsage.mekanismaddons.config.AddonConfig;
import morningsage.mekanismaddons.events.mekatool.MekaToolUsageEvent;
import morningsage.mekanismaddons.plugins.AE2Plugin;
import morningsage.mekanismaddons.plugins.IDigitalStorage;
import morningsage.mekanismaddons.plugins.RSPlugin;
import morningsage.mekanismaddons.rs.RSActions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModuleDigitalStorageUnit extends ModuleMekaTool implements IHasSmartEnable {
    private ModuleConfigItem<StorageMode> storageMode;

    public static final Set<ModuleData<?>> INCOMPATIBLE = new HashSet<>(
        Collections.singletonList(
            Modules.TELEPORTATION_UNIT
        )
    );

    @Override
    public ItemStack getContainer() {
        return super.getContainer();
    }

    @Override
    public Set<ModuleData<?>> getIncompatibleModules() {
        return INCOMPATIBLE;
    }

    @Override
    public void init() {
        super.init();
        this.addConfigItem(
            storageMode = new ModuleConfigItem<>(
                this, "storage_mode", MekanismAddonsLang.MODULE_STORAGE_MODE, new ModuleConfigItem.EnumData<>(StorageMode.class, StorageMode.values().length), StorageMode.OFF
            )
        );
    }

    @Override
    protected void toggleEnabled(PlayerEntity player, ITextComponent modeName) {
        super.toggleEnabled(player, modeName);
    }

    public void onMekaToolUse(MekaToolUsageEvent.Use event) {
        if (!canFunction() || !(event.getEntity() instanceof PlayerEntity)) return;

        ActionResult<ItemStack> result = this.storageMode.get().useCustomTerminal(
            event.getWorld(), (PlayerEntity) event.getEntity(), event.getStack(), event.getHand()
        );

        if (result != null) {
            event.setActionResult(result);
        }
    }

    @Override
    public void addHUDStrings(List<ITextComponent> list) {
        if (AddonConfig.general.digitalStorageUnitEnabled.get() && isEnabled()) {
            list.add(MekanismAddonsLang.HUD_STORAGE_MODE.translateColored(EnumColor.DARK_GRAY, EnumColor.INDIGO, storageMode.get().getTextComponent()));
        }
    }

    @Override
    public void changeMode(@Nonnull PlayerEntity player, @Nonnull ItemStack stack, int shift, boolean displayChangeMessage) {
        if (!isEnabled() || !AddonConfig.general.digitalStorageUnitEnabled.get()) return;

        StorageMode newMode = storageMode.get().adjust(shift);

        if (storageMode.get() != newMode) {
            storageMode.set(newMode, null);

            if (displayChangeMessage) {
                player.sendMessage(MekanismLang.LOG_FORMAT.translateColored(EnumColor.DARK_BLUE, MekanismLang.MEKANISM, EnumColor.GRAY, MekanismLang.MODULE_MODE_CHANGE.translate(MekanismAddonsLang.MODULE_STORAGE_MODE.translate(), EnumColor.INDIGO, storageMode.get().getTextComponent())), Util.NIL_UUID);
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (canFunction()) {
            ResourceLocation rs = context.getLevel().getBlockState(context.getClickedPos()).getBlock().getRegistryName();
            StorageMode storageMode = StorageMode.getByMod(rs);
            if (storageMode != null) {
                storageMode.useCustomTerminalOn(context);
                setStorageMode(storageMode);
            }
            return ActionResultType.PASS;
        }
        return super.onItemUse(context);
    }

    public StorageMode getStorageMode() {
        return this.storageMode.get();
    }

    public void setStorageMode(StorageMode storageMode) {
        this.storageMode.set(storageMode, null);
    }

    public boolean canFunction() {
        return AddonConfig.general.digitalStorageUnitEnabled.get() && isEnabled();
    }

    public enum StorageMode implements IIncrementalEnum<StorageMode>, IHasTextComponent, IDigitalStorage {
        OFF(false, () -> null),
        AE2(AE2Plugin.isLoaded, () -> AE2Actions.INSTANCE),
        RS(RSPlugin.isLoaded, () -> RSActions.INSTANCE);

        private static final StorageMode[] MODES = values();
        private final boolean loadedCheck;
        private final Supplier<IDigitalStorage> digitalStorage;
        private ITextComponent label = null;
        private String nbtKey = null;

        StorageMode(boolean loadedCheck, Supplier<IDigitalStorage> digitalStorage) {
            this.loadedCheck = loadedCheck;
            this.digitalStorage = digitalStorage;
        }

        @Nullable
        public static StorageMode getByMod(@Nullable ResourceLocation resourceLocation) {
            if (resourceLocation != null) {
                for (StorageMode value : values()) {
                    if (!value.loadedCheck) continue;
                    if (value.digitalStorage.get().getModId().equals(resourceLocation.getNamespace())) return value;
                }
            }
            return null;
        }

        @Override
        public ITextComponent getTextComponent() {
            if (label == null) {
                label = TextComponentUtil.getString(this == OFF ? "-" : this.name());
            }

            return label;
        }

        public String getNbtKey() {
            if (nbtKey == null) {
                nbtKey = this == OFF ? "" : "digital" + this.name();
            }

            return nbtKey;
        }

        @Nonnull
        public StorageMode byIndex(int index) {
            return byIndexStatic(index);
        }

        public static StorageMode byIndexStatic(int index) {
            return MathUtils.getByIndexMod(MODES, index);
        }

        @Override
        public String getEncryptionKey(ItemStack item) {
            if (loadedCheck) return digitalStorage.get().getEncryptionKey(item);
            return "";
        }

        @Override
        public void setEncryptionKey(ItemStack item, String encKey, String name) {
            if (loadedCheck) digitalStorage.get().setEncryptionKey(item, encKey, name);
        }

        @Override
        public String getModId() {
            return loadedCheck ? digitalStorage.get().getModId() : "";
        }

        @Override
        public ActionResult<ItemStack> useCustomTerminal(World world, PlayerEntity player, ItemStack stack, Hand hand) {
            if (loadedCheck) {
                return this.digitalStorage.get().useCustomTerminal(world, player, stack, hand);
            }

            return null;
        }

        @Override
        public ActionResultType useCustomTerminalOn(ItemUseContext context) {
            if (loadedCheck) {
                return this.digitalStorage.get().useCustomTerminalOn(context);
            }
            return ActionResultType.PASS;
        }

        @Override
        public boolean canHandle(ItemStack itemStack) {
            if (loadedCheck) {
                return this.digitalStorage.get().canHandle(itemStack);
            }
            return false;
        }

        @Override
        public boolean usePower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
            if (loadedCheck) {
                return this.digitalStorage.get().usePower(playerEntity, v, itemStack);
            }
            return false;
        }

        @Override
        public boolean hasPower(PlayerEntity playerEntity, double v, ItemStack itemStack) {
            if (loadedCheck) {
                return this.digitalStorage.get().hasPower(playerEntity, v, itemStack);
            }
            return false;
        }
    }
}
