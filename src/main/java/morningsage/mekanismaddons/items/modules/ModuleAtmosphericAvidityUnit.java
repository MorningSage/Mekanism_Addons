package morningsage.mekanismaddons.items.modules;

import mekanism.common.content.gear.mekasuit.ModuleMekaSuit;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ModuleAtmosphericAvidityUnit extends ModuleMekaSuit {

    public void adjustMiningSpeed(PlayerEvent.BreakSpeed event) {
        if (!isEnabled()) return;

        if (!event.getEntityLiving().isOnGround()) {
            if (event.getOriginalSpeed() < event.getNewSpeed() * 5) event.setNewSpeed(event.getNewSpeed() * 5F);
        }
    }

}
