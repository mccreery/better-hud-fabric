package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.ScreenRenderContext;
import mccreery.betterhud.api.geometry.Anchor;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A grid layout with a fixed cell size and number of rows and columns.
 */
public class Grid extends LayoutBox {
    private final List<LayoutBox> cells;

    private final int rowCount;
    private final int columnCount;
    private final Point cellSize;

    private double rowGutter;
    private double columnGutter;

    public Grid(ScreenRenderContext context, int rowCount, int columnCount, Point cellSize) {
        super(context);
        if (rowCount < 1 || columnCount < 1) {
            throw new IllegalArgumentException("Minimum 1 row and 1 column");
        }

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.cells = new ArrayList<>(Collections.nCopies(rowCount * columnCount, null));

        this.cellSize = cellSize;
    }

    public void setRowGutter(double rowGutter) {
        this.rowGutter = rowGutter;
    }

    public void setColumnGutter(double columnGutter) {
        this.columnGutter = columnGutter;
    }

    public LayoutBox getCell(int row, int column) {
        int index = getCellIndex(row, column);
        return cells.get(index);
    }

    public LayoutBox setCell(int row, int column, LayoutBox cell) {
        int index = getCellIndex(row, column);
        return cells.set(index, cell);
    }

    private int getCellIndex(int row, int column) {
        if (row < 0 || row >= rowCount || column < 0 || column >= columnCount) {
            throw new IndexOutOfBoundsException("Invalid cell position");
        }
        return row * columnCount + column;
    }

    @Override
    public Point getPreferredSize() {
        Point gutter = getGutter();
        Point shape = getShape();

        return cellSize.add(gutter).scale(shape).subtract(gutter);
    }

    @Override
    public void applyLayout(Rectangle bounds) {
        Point cellStep = cellSize.add(getGutter());

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                LayoutBox cell = getCell(row, column);

                if (cell != null) {
                    Rectangle cellBounds = new Rectangle(
                            bounds.getPosition().add(cellStep.scale(new Point(row, column))),
                            cellSize
                    );
                    cell.applyLayout(cellBounds);
                }
            }
        }
    }

    @Override
    public void render() {
        for (LayoutBox cell : cells) {
            cell.render();
        }
    }

    /**
     * Converts the row count and column count to a double-precision point.
     * @return The size of the grid in cells.
     */
    private Point getShape() {
        return new Point(columnCount, rowCount);
    }

    /**
     * Converts the row and column gutters to a point.
     * @return The size of the gutter.
     */
    private Point getGutter() {
        return new Point(columnGutter, rowGutter);
    }

    /**
     * Creates a grid for lines of text separated by a small space.
     * @param lines The lines of text.
     * @param alignment The alignment of the lines.
     * @return A grid containing labels of each line.
     */
    public static Grid ofLabels(ScreenRenderContext context, Anchor alignment, String... lines) {
        return ofLabels(context, alignment, Arrays.asList(lines));
    }

    /**
     * Creates a grid for lines of text separated by a small space.
     * @param lines The lines of text.
     * @param alignment The alignment of the lines.
     * @return A grid containing labels of each line.
     */
    public static Grid ofLabels(ScreenRenderContext context, Anchor alignment, Iterable<String> lines) {
        List<Label> labels = new ArrayList<>();

        for (String line : lines) {
            labels.add(new Label(context, line, 0xffffffff, true));
        }
        Point cellSize = getMaxSize(labels);

        Grid grid = new Grid(context, labels.size(), 1, cellSize);
        for (int i = 0; i < labels.size(); i++) {
            grid.setCell(i, 0, new AlignmentBox(context, labels.get(i), alignment));
        }

        return grid;
    }

    private static Point getMaxSize(Collection<? extends LayoutBox> boxes) {
        if (boxes.isEmpty()) {
            throw new IllegalArgumentException("Cannot get max size from empty collection");
        }

        Optional<Point> optional = boxes.stream()
                .map(LayoutBox::getPreferredSize)
                .reduce(Point::max);

        // Since the collection is not empty there must be a value
        assert optional.isPresent();
        return optional.get();
    }
}
