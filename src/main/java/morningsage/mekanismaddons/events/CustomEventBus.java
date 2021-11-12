package morningsage.mekanismaddons.events;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;

public class CustomEventBus {
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();
}
