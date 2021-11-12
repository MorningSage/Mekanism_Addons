package morningsage.mekanismaddons.utils;

import morningsage.mekanismaddons.items.modules.ModuleAOEUnit;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.IFluidBlock;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collections;

public final class AOEUtils {
    private static RayTraceResult rayTraceSimple(LivingEntity living) {
        double reach = getReach(living);

        Vector3d startPosition = living.getEyePosition(0);
        Vector3d viewVector = living.getViewVector(0);
        Vector3d endPosition = startPosition.add(viewVector.x * reach, viewVector.y * reach, viewVector.z * reach);

        return living.level.clip(new RayTraceContext(startPosition, endPosition, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, living));
    }

    private static Pair<BlockPos, BlockPos> getAOECorners(BlockPos pos, Direction facing, ModuleAOEUnit.AOERange range) {
        int radius = range.getRadius();

        BlockPos bottomLeft = pos
            .relative(facing.getAxis() == Direction.Axis.Y ? Direction.SOUTH : Direction.DOWN, radius)
            .relative(facing.getAxis() == Direction.Axis.Y ? Direction.WEST : facing.getCounterClockWise(), radius);
        BlockPos topRight = pos
            .relative(facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP, radius)
            .relative(facing.getAxis() == Direction.Axis.Y ? Direction.EAST : facing.getClockWise(), radius);

        if (facing.getAxis() != Direction.Axis.Y && radius > 0) {
            bottomLeft = bottomLeft.relative(Direction.UP, radius - 1);
            topRight = topRight.relative(Direction.UP, radius - 1);
        }

        return Pair.of(bottomLeft, topRight);
    }

    public static Iterable<BlockPos> getAOEArea(PlayerEntity player, @Nullable BlockPos blockPos, ModuleAOEUnit module, @Nullable Dist dist) {
        if (blockPos == null) return Collections.emptyList();

        BlockState state = player.level.getBlockState(blockPos);
        VoxelShape shape = state.getShape(player.level, blockPos);

        boolean canPlayerAOE = canPlayerAOE(player, module, dist);
        boolean canBlockAOE = canBlockAOE(player.level, blockPos, state, shape, blockPos, state);

        if (!canPlayerAOE || !canBlockAOE) return Collections.emptyList();

        RayTraceResult rayTraceResult = AOEUtils.rayTraceSimple(player);
        if (!(rayTraceResult instanceof BlockRayTraceResult)) return Collections.emptyList();

        Pair<BlockPos, BlockPos> area = getAOECorners(
            blockPos, ((BlockRayTraceResult) rayTraceResult).getDirection(), module.getRange()
        );

        return BlockPos.betweenClosed(area.getLeft(), area.getRight());
    }

    public static boolean canPlayerAOE(PlayerEntity player, ModuleAOEUnit module, @Nullable Dist dist) {
        return !player.isShiftKeyDown() && !player.isCreative()
            && module != null && module.isEnabled()
            && (dist == null || (player.level.isClientSide == dist.isClient()));
    }

    public static boolean canBlockAOE(World world, BlockPos queryPos, BlockState queryState, VoxelShape queryShape, BlockPos centerPos, BlockState centerState) {
        float queryHardness = queryState.getDestroySpeed(world, queryPos);
        float centerHardness = centerState.getDestroySpeed(world, centerPos);

        boolean isBlockValid = !queryState.isAir(world, queryPos)
            && !(queryState.getBlock() instanceof IFluidBlock)
            && !(queryState.getBlock() instanceof FlowingFluidBlock)
            && world.getWorldBorder().isWithinBounds(queryPos)
            && !queryState.hasTileEntity()
            && queryHardness >= 0
            && centerHardness > 0;

        if (queryShape != null) isBlockValid = isBlockValid && !queryShape.isEmpty();

        return isBlockValid;
    }

    public static double getReach(LivingEntity livingEntity) {
        ModifiableAttributeInstance reachAttribute = livingEntity.getAttribute(ForgeMod.REACH_DISTANCE.get());
        return reachAttribute == null ? 0 : reachAttribute.getValue();
    }
}
