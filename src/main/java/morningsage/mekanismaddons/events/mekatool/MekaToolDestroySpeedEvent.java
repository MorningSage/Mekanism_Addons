package morningsage.mekanismaddons.events.mekatool;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

public class MekaToolDestroySpeedEvent extends Event {
    private final ItemStack stack;
    @Nullable private final BlockPos pos;
    private final PlayerEntity player;
    private final float defaultValue;
    private float destroySpeed;

    private MekaToolDestroySpeedEvent(ItemStack stack, @Nullable BlockPos pos, PlayerEntity player, float defaultValue) {
        this.stack = stack;
        this.pos = pos;
        this.player = player;
        this.defaultValue = defaultValue;
        this.destroySpeed = defaultValue;
    }

    @Nullable
    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getDestroySpeed() {
        return destroySpeed;
    }

    public void setDestroySpeed(float destroySpeed) {
        this.destroySpeed = destroySpeed;
    }

    public static float invoke(ItemStack stack, BlockPos pos, PlayerEntity player, float defaultValue) {
        MekaToolDestroySpeedEvent event = new MekaToolDestroySpeedEvent(stack, pos, player, defaultValue);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDestroySpeed();
    }
}
