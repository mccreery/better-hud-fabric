package mccreery.betterhud.internal.element.vanilla;

import mccreery.betterhud.api.geometry.Rectangle;
import mccreery.betterhud.api.property.BooleanProperty;

public class PotionBar extends OverlayElement {
    public static final ResourceLocation INVENTORY = new ResourceLocation("textures/gui/container/inventory.png");

    private SettingPosition position;
    private BooleanProperty showDuration;

    public PotionBar() {
        super("potionBar");

        position = new SettingPosition("position");
        position.setDirectionOptions(DirectionOptions.X);
        position.setContentOptions(DirectionOptions.CORNERS);
        addSetting(position);

        showDuration = new BooleanProperty("duration");
        showDuration.setValuePrefix(BooleanProperty.VISIBLE);
        addSetting(showDuration);
    }

    @Override
    public boolean shouldRender(HudRenderContext context) {
        return !MC.player.getActivePotionEffects().isEmpty();
    }

    @Override
    public Rectangle render(HudRenderContext context) {
        Boxed grid = getGrid();

        Rectangle bounds = new Rectangle(grid.getPreferredSize());
        if(position.isDirection(Direction.CENTER)) {
            bounds = bounds.align(MANAGER.getScreen().getAnchor(Direction.CENTER).add(SPACER, SPACER), Direction.NORTH_WEST);
        } else {
            bounds = position.applyTo(bounds);
        }
        grid.setBounds(bounds).render();
        MC.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

        return bounds;
    }

    private void populateEffects(List<EffectInstance> helpful, List<EffectInstance> harmful) {
        Iterable<EffectInstance> activeEffects =  MC.player.getActivePotionEffects();

        for (EffectInstance effect : activeEffects) {
            if (!effect.doesShowParticles() || !effect.getPotion().shouldRenderHUD(effect)) {
                continue;
            }

            if (effect.getPotion().isBeneficial()) {
                helpful.add(effect);
            } else {
                harmful.add(effect);
            }
        }
        helpful.sort(Collections.reverseOrder());
        harmful.sort(Collections.reverseOrder());
    }

    private void fillRow(Grid<? super PotionIcon> grid, int row, List<EffectInstance> effects) {
        for(int i = 0; i < effects.size(); i++) {
            grid.setCell(new Point(i, row), new PotionIcon(effects.get(i), showDuration.get()));
        }
    }

    private Boxed getGrid() {
        List<EffectInstance> helpful = new ArrayList<>(), harmful = new ArrayList<>();
        populateEffects(helpful, harmful);

        int rows = 0;
        if(!helpful.isEmpty()) ++rows;
        if(!harmful.isEmpty()) ++rows;

        Grid<PotionIcon> grid = new Grid<>(new Point(Math.max(helpful.size(), harmful.size()), rows));
        grid.setAlignment(position.getContentAlignment());
        grid.setGutter(new Point(1, 2));

        int row = 0;
        if(!helpful.isEmpty()) fillRow(grid, row++, helpful);
        if(!harmful.isEmpty()) fillRow(grid, row++, harmful);

        return grid;
    }
}
