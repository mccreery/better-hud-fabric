package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;
import mccreery.betterhud.api.geometry.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid extends LayoutBox {
    private final List<LayoutBox> cells;

    private final int rowCount;
    private final int columnCount;

    private double rowGutter;
    private double columnGutter;

    private boolean stretch;
    private Point cellAlignment = Rectangle.Node.CENTER.getT();

    public Grid(int rowCount, int columnCount) {
        if (rowCount < 1 || columnCount < 1) {
            throw new IllegalArgumentException("Minimum 1 row and 1 column");
        }

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.cells = new ArrayList<>(Collections.nCopies(rowCount * columnCount, null));
    }

    public void setRowGutter(double rowGutter) {
        this.rowGutter = rowGutter;
    }

    public void setColumnGutter(double columnGutter) {
        this.columnGutter = columnGutter;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    /**
     * Sets the cell alignment.
     * @param t An interpolation parameter.
     */
    public void setCellAlignment(Point t) {
        this.cellAlignment = t;
    }

    public void setCellAlignment(Rectangle.Node node) {
        setCellAlignment(node.getT());
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
        Point cellSize = getCellSize();
        Point gutter = getGutter();
        Point shape = getShape();

        return cellSize.add(gutter).scale(shape).subtract(gutter);
    }

    @Override
    public void render() {
        Point cellSize = getCellSize();
        Point cellStep = cellSize.add(getGutter());
        Rectangle bounds = getBounds();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                LayoutBox cell = getCell(row, column);

                if (cell != null) {
                    Rectangle cellBounds = new Rectangle(
                        bounds.getPosition().add(cellStep.scale(new Point(row, column))),
                        cellSize
                    );
                    cell.setBounds(cellBounds);
                }
            }
        }
    }

    /**
     * Converts the row count and column count to a double-precision point.
     * @return The size of the grid in cells.
     */
    private Point getShape() {
        return new Point(columnCount, rowCount);
    }

    private Point getCellSize() {
        return Point.max(cells.stream().map(LayoutBox::getPreferredSize)::iterator);
    }

    /**
     * Converts the row and column gutters to a point.
     * @return The size of the gutter.
     */
    private Point getGutter() {
        return new Point(columnGutter, rowGutter);
    }
}
