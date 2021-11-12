package morningsage.mekanismaddons.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import mekanism.common.content.gear.Modules;
import morningsage.mekanismaddons.items.modules.AddonModules;
import morningsage.mekanismaddons.items.modules.ModuleAOEUnit;
import morningsage.mekanismaddons.mixin.minecraft.PlayerControllerAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.model.data.EmptyModelData;

@OnlyIn(Dist.CLIENT)
public final class ClientUtils {

    public static void onBlockHover(DrawHighlightEvent.HighlightBlock event) {
        if (event.isCanceled()) return;

        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;

        ModuleAOEUnit module = Modules.load(player.getMainHandItem(), AddonModules.AOE_UNIT);
        if (module == null) return;

        if (!AOEUtils.canPlayerAOE(player, module, Dist.CLIENT)) return;

        Iterable<BlockPos> area = AOEUtils.getAOEArea(player, event.getTarget().getBlockPos(), module, Dist.CLIENT);
        int destroyProgress = getDestroyProgress();
        MatrixStack matrix = event.getMatrix();

        RenderTypeBuffers renderTypeBuffers = Minecraft.getInstance().renderBuffers();
        IVertexBuilder vertexLines = renderTypeBuffers.outlineBufferSource().getBuffer(RenderType.lines());
        IVertexBuilder vertexBreaking = renderTypeBuffers.crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(destroyProgress));

        Vector3d vec3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        matrix.pushPose();
        for (BlockPos blockPos : area) {
            VoxelShape shape = verifyAndGetShape(player.level, blockPos, event.getTarget().getBlockPos());

            if (shape == null || shape.isEmpty()) continue;

            WorldRenderer.renderLineBox(matrix, vertexLines, shape.bounds().move(
                blockPos.getX() - vec3d.x(),
                blockPos.getY() - vec3d.y(),
                blockPos.getZ() - vec3d.z()
            ), 0, 0, 0, 0.35F);

            if (destroyProgress > 0) {
                matrix.pushPose();
                matrix.translate(
                    blockPos.getX() - vec3d.x(),
                    blockPos.getY() - vec3d.y(),
                    blockPos.getZ() - vec3d.z()
                );
                MatrixStack.Entry matrixEntry = matrix.last();
                IVertexBuilder matrixBuilder = new MatrixApplyingVertexBuilder(vertexBreaking, matrixEntry.pose(), matrixEntry.normal());
                Minecraft.getInstance().getBlockRenderer().renderBlockDamage(player.level.getBlockState(blockPos), blockPos, player.level, matrix, matrixBuilder, EmptyModelData.INSTANCE);
                matrix.popPose();
            }
        }
        matrix.popPose();
    }

    private static VoxelShape verifyAndGetShape(World world, BlockPos queryPos, BlockPos centerPos) {
        BlockState queryState = world.getBlockState(queryPos);
        BlockState centerState = world.getBlockState(centerPos);
        VoxelShape shape = queryState.getShape(world, queryPos);

        boolean isBlockValid = AOEUtils.canBlockAOE(world, queryPos, queryState, shape, centerPos, centerState) && !queryPos.equals(centerPos);

        return isBlockValid ? shape : null;
    }

    private static int getDestroyProgress() {
        PlayerController controller = Minecraft.getInstance().gameMode;
        boolean isDestroying = controller != null && controller.isDestroying();
        int progress = isDestroying ? (int) (((PlayerControllerAccessor) controller).getDestroyProgress() * 10.0F) - 1 : 0;
        if (!isDestroying) return 0;
        return (progress < 0 || progress > 10) ? 0 : Math.min(progress + 1, 9);
    }
}
