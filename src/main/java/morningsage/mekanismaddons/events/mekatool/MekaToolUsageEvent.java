package morningsage.mekanismaddons.events.mekatool;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MekaToolUsageEvent extends Event {
    private final LivingEntity entity;
    private final ItemStack stack;

    private MekaToolUsageEvent(LivingEntity entity, ItemStack stack) {
        this.entity = entity;
        this.stack = stack;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getStack() {
        return stack;
    }

    public static class Use extends MekaToolUsageEvent {
        private final World world;
        private final Hand hand;

        private ActionResult<ItemStack> actionResult;

        private Use(World world, LivingEntity entity, @Nonnull Hand hand) {
            super(entity, entity.getItemInHand(hand));

            this.world = world;
            this.hand = hand;

            this.actionResult = null;
        }

        public World getWorld() {
            return world;
        }

        public Hand getHand() {
            return hand;
        }

        public ActionResult<ItemStack> getActionResult() {
            return actionResult;
        }

        public void setActionResult(ActionResult<ItemStack> actionResult) {
            this.actionResult = actionResult;
        }

        @Nullable
        public static ActionResult<ItemStack> invoke(World world, LivingEntity entity, @Nonnull Hand hand) {
            Use event = new Use(world, entity, hand);
            return MinecraftForge.EVENT_BUS.post(event) ? null : event.getActionResult();
        }
    }

    public static class UseOn extends MekaToolUsageEvent {

        private ActionResultType actionResultType;

        private UseOn(ItemUseContext itemUseContext) {
            super(itemUseContext.getPlayer(), itemUseContext.getItemInHand());

            actionResultType = null;
        }

        public ActionResultType getActionResultType() {
            return actionResultType;
        }

        public void setActionResultType(ActionResultType actionResultType) {
            this.actionResultType = actionResultType;
        }

        @Nullable
        public static ActionResultType invoke(ItemUseContext itemUseContext) {
            UseOn event = new UseOn(itemUseContext);
            return MinecraftForge.EVENT_BUS.post(event) ? null : event.getActionResultType();
        }
    }
}
