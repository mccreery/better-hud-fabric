package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.ScreenRenderContext;
import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}
