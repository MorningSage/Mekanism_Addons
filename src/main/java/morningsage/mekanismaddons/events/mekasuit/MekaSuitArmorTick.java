package morningsage.mekanismaddons.events.mekasuit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public abstract class MekaSuitArmorTick extends Event {
    private final ItemStack stack;
    private final World world;
    private final PlayerEntity player;

    private MekaSuitArmorTick(ItemStack stack, World world, PlayerEntity player) {
        this.stack = stack;
        this.world = world;
        this.player = player;
    }

    public ItemStack getStack() {
        return this.stack;
    }
    public World getWorld() {
        return this.world;
    }
    public PlayerEntity getPlayer() {
        return this.player;
    }

    public static class Client extends MekaSuitArmorTick {
        private Client(ItemStack stack, World world, PlayerEntity player) {
            super(stack, world, player);
        }

        public static void invoke(ItemStack stack, World world, PlayerEntity player) {
            MinecraftForge.EVENT_BUS.post(new Client(stack, world, player));
        }
    }


    public static class Server extends MekaSuitArmorTick {
        private Server(ItemStack stack, World world, PlayerEntity player) {
            super(stack, world, player);
        }

        public static void invoke(ItemStack stack, World world, PlayerEntity player) {
            MinecraftForge.EVENT_BUS.post(new Server(stack, world, player));
        }
    }
}
