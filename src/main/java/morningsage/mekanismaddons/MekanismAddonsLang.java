package morningsage.mekanismaddons;

import mekanism.api.text.ILangEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public enum MekanismAddonsLang implements ILangEntry {
    MODULE_DIHYDROGEN_MONOXIDE_AVIDITY_UNIT("module", "dihydrogen_monoxide_avidity_unit"),
    DESCRIPTION_DIHYDROGEN_MONOXIDE_AVIDITY_UNIT("description", "dihydrogen_monoxide_avidity_unit"),
    MODULE_ATMOSPHERIC_AVIDITY_UNIT("module", "atmospheric_avidity_unit"),
    DESCRIPTION_ATMOSPHERIC_AVIDITY_UNIT("description", "atmospheric_avidity_unit"),
    MODULE_CAPTURING_UNIT("module", "capturing_unit"),
    DESCRIPTION_CAPTURING_UNIT("description", "capturing_unit"),
    MODULE_AOE_UNIT("module", "aoe_unit"),
    DESCRIPTION_AOE_UNIT("description", "aoe_unit"),
    MODULE_AOE("module", "aoe"),
    MODULE_AOE_MODE("module", "aoe_mode"),
    MODULE_AMBULATION_ACCELERATION_UNIT("module", "ambulation_acceleration_unit"),
    DESCRIPTION_AMBULATION_ACCELERATION_UNIT("description", "ambulation_acceleration_unit"),
    MODULE_IGNITION_RETARDATION_UNIT("module", "ignition_retardation_unit"),
    DESCRIPTION_IGNITION_RETARDATION_UNIT("description", "ignition_retardation_unit"),
    MODULE_WALK_BOOST("module", "walk_boost"),
    MODULE_DIGITAL_STORAGE_UNIT("module", "digital_storage_unit"),
    DESCRIPTION_DIGITAL_STORAGE_UNIT("description", "digital_storage_unit"),
    MODULE_STORAGE_MODE("module", "storage_mode"),
    HUD_STORAGE_MODE("hud", "storage_mode"),
    ;

    private final String key;

    MekanismAddonsLang(String type, String path) {
        this(Util.makeDescriptionId(type, new ResourceLocation(MekanismAddons.MOD_ID, path)));
    }

    MekanismAddonsLang(String key) {
        this.key = key;
    }

    public String getTranslationKey() { return this.key; }
}
