package mccreery.betterhud.internal.element;

import com.mojang.blaze3d.systems.RenderSystem;
import mccreery.betterhud.api.HudElement;
import mccreery.betterhud.api.HudRenderContext;
import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;
import mccreery.betterhud.internal.BetterHud;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.List;

/**
 * @see BetterHud#onBlockBreak(net.minecraftforge.event.world.BlockEvent.BreakEvent)
 */
public class BlockViewer extends HudElement {
    private final BooleanProperty showBlock;
    private final BooleanProperty showIds;
    private final BooleanProperty invNames;

    private BlockRayTraceResult trace;
    private BlockState state;
    private ItemStack stack;

    public BlockViewer() {
        showBlock = new BooleanProperty("showItem", true);
        addProperty(showBlock);

        showIds = new BooleanProperty("showIds", false);
        addProperty(showIds);

        invNames = new BooleanProperty("invNames", true);
        addProperty(invNames);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        RayTraceResult traceResult = MC.getRenderViewEntity().pick(HudElements.GLOBAL.getBillboardDistance(), 1f, false);

        if (traceResult != null && traceResult.getType() == RayTraceResult.Type.BLOCK) {
            trace = (BlockRayTraceResult)traceResult;
            state = MC.world.getBlockState(trace.getPos());
            stack = getDisplayStack(trace, state);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected List<String> getText() {
        ITextComponent text = getBlockName(trace, state, stack);
        String textString = text.getFormattedText();

        if (showIds.get()) {
            textString += " " + getIdString(state);
        }

        return Arrays.asList(textString);
    }

    @Override
    protected Rectangle getPadding() {
        int vPad = 20 - MC.fontRenderer.FONT_HEIGHT;
        int bottom = vPad / 2;
        Rectangle bounds = Rectangle.createPadding(5, vPad - bottom, 5, bottom);

        if (stack != null && showBlock.get()) {
            if (position.getContentAlignment() == Direction.EAST) {
                bounds = bounds.withRight(bounds.getRight() + 21);
            } else {
                bounds = bounds.withLeft(bounds.getLeft() - 21);
            }
        }
        return bounds;
    }

    @Override
    protected void drawBorder(Rectangle bounds, Rectangle padding, Rectangle margin) {
        GlUtil.drawTooltipBox(bounds);
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        RenderSystem.disableDepthTest();
        return super.render(context);
    }

    @Override
    protected void drawExtras(Rectangle bounds) {
        if (stack != null && showBlock.get()) {
            Rectangle stackRect = new Rectangle(16, 16).anchor(bounds.grow(-5, -2, -5, -2), position.getContentAlignment());
            GlUtil.renderSingleItem(stack, stackRect.getPosition());
        }
    }

    @Override
    protected Rectangle moveRect(Rectangle bounds) {
        if (position.isDirection(Direction.CENTER)) {
            return bounds.align(MANAGER.getScreen().getAnchor(Direction.CENTER).add(0, -SPACER), Direction.SOUTH);
        } else {
            return super.moveRect(bounds);
        }
    }

    /**
     * Creates the most representative item stack for the given result.<br>
     * If the block has no {@link net.minecraft.item.ItemBlock}, it is impossible to create a stack.
     *
     * @see net.minecraftforge.common.ForgeHooks#onPickBlock(RayTraceResult, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World)
     */
    private ItemStack getDisplayStack(BlockRayTraceResult trace, BlockState state) {
        ItemStack stack = state.getBlock().getPickBlock(state, trace, MC.world, trace.getPos(), MC.player);

        if (stack.isEmpty()) {
            // Pick block is disabled, however we can grab the information directly
            return new ItemStack(state.getBlock());
        }
        return stack;
    }

    /**
     * Chooses the best name for the given result and its related stack.
     *
     * @param stack The direct result of {@link #getDisplayStack(RayTraceResult, BlockState)}. May be {@code null}
     * @see ItemStack#getDisplayName()
     * @see TileEntity#getDisplayName()
     * @see net.minecraft.item.ItemBlock#getUnlocalizedName(ItemStack)
     */
    private ITextComponent getBlockName(BlockRayTraceResult trace, BlockState state, ItemStack stack) {
        if (state.getBlock() == Blocks.END_PORTAL) {
            return new TranslationTextComponent("tile.endPortal.name");
        }

        if (invNames.get() && state.getBlock().hasTileEntity(state)) {
            TileEntity tileEntity = MC.world.getTileEntity(trace.getPos());

            if (tileEntity instanceof INameable) {
                ITextComponent invName = ensureInvName(trace.getPos());

                if (invName != null) {
                    return invName;
                }
            }
        }

        return isStackEmpty(stack) ? state.getBlock().getNameTextComponent() : stack.getDisplayName();
    }

    /**
     * @return Information about the block's related IDs
     */
    private String getIdString(BlockState state) {
        String name = ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString();
        int id = ((ForgeRegistry<Block>)ForgeRegistries.BLOCKS).getID(state.getBlock());

        return String.format("%s(%s/#%04d)", ChatFormatting.YELLOW, name, id);
    }

    public static final Map<BlockPos, ITextComponent> nameCache = new HashMap<BlockPos, ITextComponent>();

    public void onNameReceived(BlockPos pos, ITextComponent name) {
        nameCache.put(pos, name);
    }

    public void onChangeWorld() {
        nameCache.clear();
    }

    /**
     * If the client doesn't know the name of an inventory, this method
     * asks the server, then until the response is given, a generic
     * container name will be returned. When the client finds out the actual name
     * of the inventory, it will return that value
     */
    private static ITextComponent ensureInvName(BlockPos pos) {
        if (!nameCache.containsKey(pos)) {
            BetterHud.NET_WRAPPER.sendToServer(new InventoryNameQuery.Request(pos));
            nameCache.put(pos, null);
        }
        ITextComponent name = nameCache.get(pos);

        if (name != null) {
            return name;
        } else {
            TileEntity tileEntity = MC.world.getTileEntity(pos);
            return tileEntity instanceof INameable ? ((INameable)tileEntity).getDisplayName() : null;
        }
    }

    /**
     * Considers {@code null} to be empty
     *
     * @see ItemStack#isEmpty()
     */
    private static boolean isStackEmpty(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
