package mccreery.betterhud.internal.element;

import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.layout.Grid;
import mccreery.betterhud.api.layout.LabelOptions;
import mccreery.betterhud.internal.render.Color;
import mccreery.betterhud.internal.render.DrawingContext;
import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SignReader extends HudElement {
    private static final Rectangle SIGN_UV = new Rectangle(2, 2, 24, 12);
    private static final Point SIGN_TEXTURE_SIZE = new Point(32, 16);

    @Override
    public Rectangle render(HudRenderContext context) {
        BlockPos targetBlock = getTargetBlock(context.getClient());

        if (targetBlock != null) {
            SignType signType = getSignType(context.getClient().world, targetBlock);
            SignBlockEntity signEntity = getSignEntity(context.getClient().world, targetBlock);

            if (signType != null && signEntity != null) {
                return render(context, signType, signEntity);
            }
        }
        return null;
    }

    private Rectangle render(HudRenderContext context, SignType signType, SignBlockEntity signEntity) {
        DrawingContext drawingContext = new DrawingContext(context.getMatrixStack(), Tessellator.getInstance().getBuffer());

        LabelOptions labelOptions = LabelOptions.DEFAULT.withColor(Color.BLACK).withShadow(false);
        Grid grid = Grid.ofLabels(context, Anchor.CENTER, labelOptions,
                signEntity.getTextOnRow(0),
                signEntity.getTextOnRow(1),
                signEntity.getTextOnRow(2),
                signEntity.getTextOnRow(3));

        Rectangle bounds = context.calculateBounds(SIGN_UV.getSize().scale(4));
        Rectangle textBounds = new Rectangle(Point.ZERO, grid.getPreferredSize()).align(bounds, Anchor.CENTER);

        context.getClient().getTextureManager().bindTexture(getSignTexture(signType));
        drawingContext.drawTexturedRectangle(bounds, SIGN_UV, SIGN_TEXTURE_SIZE);

        grid.applyLayout(textBounds);
        grid.render();
        return bounds;
    }

    private static BlockPos getTargetBlock(MinecraftClient client) {
        HitResult hitResult = client.getCameraEntity().raycast(200, 1, false);

        if (hitResult instanceof BlockHitResult) {
            return ((BlockHitResult)hitResult).getBlockPos();
        } else {
            return null;
        }
    }

    private static SignType getSignType(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();

        if (block instanceof SignBlock) {
            return ((SignBlock)block).getSignType();
        } else {
            return null;
        }
    }

    private static SignBlockEntity getSignEntity(World world, BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity instanceof SignBlockEntity) {
            return (SignBlockEntity)blockEntity;
        } else {
            return null;
        }
    }

    private static Identifier getSignTexture(SignType signType) {
        return new Identifier("textures/entity/signs/" + signType.getName() + ".png");
    }
}
