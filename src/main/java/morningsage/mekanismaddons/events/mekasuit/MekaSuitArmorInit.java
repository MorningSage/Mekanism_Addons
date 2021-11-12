package morningsage.mekanismaddons.events.mekasuit;

import mekanism.common.capabilities.chemical.item.RateLimitMultiTankGasHandler;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import morningsage.mekanismaddons.events.CustomEventBus;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Function;

public class MekaSuitArmorInit extends Event {
    private final ItemMekaSuitArmor armor;
    private final Function<RateLimitMultiTankGasHandler.GasTankSpec, Boolean> gasTankSpecFunction;

    private MekaSuitArmorInit(ItemMekaSuitArmor armor, Function<RateLimitMultiTankGasHandler.GasTankSpec, Boolean> gasTankSpecFunction) {
        this.armor = armor;
        this.gasTankSpecFunction = gasTankSpecFunction;
    }

    public ItemMekaSuitArmor getArmor() {
        return this.armor;
    }

    public boolean addGasTank(RateLimitMultiTankGasHandler.GasTankSpec gasTankSpec) {
        return gasTankSpecFunction.apply(gasTankSpec);
    }

    public static void invoke(ItemMekaSuitArmor armor, Function<RateLimitMultiTankGasHandler.GasTankSpec, Boolean> gasTankSpecFunction) {
        CustomEventBus.EVENT_BUS.post(new MekaSuitArmorInit(armor, gasTankSpecFunction));
    }
}
