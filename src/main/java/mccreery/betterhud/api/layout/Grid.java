package mccreery.betterhud.api.layout;

import mccreery.betterhud.api.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid extends LayoutBox {
    private final List<LayoutBox> cells;

    private final int rowCount;
    private final int columnCount;

    private double rowGutter;
    private double columnGutter;

    private boolean stretch = false;
    private Direction alignment = Direction.NORTH_WEST;
    private Direction cellAlignment = Direction.CENTER;

    public Grid(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.cells = new ArrayList<>(Collections.nCopies(rowCount * columnCount, null)));
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

    public double getRowGutter() {
        return rowGutter;
    }

    public void setRowGutter(double rowGutter) {
        this.rowGutter = rowGutter;
    }

    public double getColumnGutter() {
        return columnGutter;
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

    public Rect getCellBounds(Rect bounds, Point position) {
        Size gutterless = bounds.getSize().sub(shape.getSize().sub(1, 1).scale(gutter));
        Size cellSize = gutterless.scale(1.0f / shape.getWidth(), 1.0f / shape.getHeight());

        Direction flow = alignment.mirror();
        Point offset = new Point((flow.getCol() - 1) * position.getX(), (flow.getRow() - 1) * position.getY()).scale(cellSize.add(gutter));
        return new Rect(cellSize).anchor(bounds, alignment).translate(offset);
    }

    public boolean hasStretch() {
        return stretch;
    }

    public Grid<T> setStretch(boolean stretch) {
        this.stretch = stretch;
        return this;
    }

    public Direction getAlignment() {
        return alignment;
    }

    public Grid<T> setAlignment(Direction alignment) {
        if(!DirectionOptions.CORNERS.isValid(alignment)) {
            throw new IllegalArgumentException("Grid alignment must be a corner");
        }
        this.alignment = alignment;
        return this;
    }

    public Direction getCellAlignment() {
        return cellAlignment;
    }

    public Grid<T> setCellAlignment(Direction cellAlignment) {
        this.cellAlignment = cellAlignment;
        return this;
    }

    @Override
    public Size getPreferredSize() {
        if(shape.isEmpty()) return Size.zero();

        int width = 0, height = 0;

        for(Boxed element : flatten()) {
            if(element == null) continue;
            Size size = element.getPreferredSize();

            if(size.getWidth() > width) width = size.getWidth();
            if(size.getHeight() > height) height = size.getHeight();
        }

        return shape.getSize().scale(width, height)
            .add(shape.getSize().sub(1, 1).scale(gutter));
    }

    @Override
    public void render() {
        int x = 0;

        Size gutterless = bounds.getSize().sub(shape.getSize().sub(1, 1).scale(gutter));
        Size cellSize = gutterless.scale(1.0f / shape.getWidth(), 1.0f / shape.getHeight());

        Rect cellLeft = new Rect(cellSize).anchor(bounds, alignment);
        Rect cell = cellLeft;

        Rect gutterPadding = new Rect(gutter.invert(), new Point(gutter));
        Direction flow = alignment.mirror();

        for(Boxed element : flatten()) {
            if(element != null) {
                Size size = stretch ? element.negotiateSize(cell.getSize()) : element.getPreferredSize();
                element.setBounds(new Rect(size).anchor(cell, cellAlignment)).render();
            }

            if(x >= shape.getWidth() - 1) {
                x = 0;
                cellLeft = cellLeft.anchor(cellLeft.grow(gutterPadding), flow.withCol(1), true);
                cell = cellLeft;
            } else {
                ++x;
                cell = cell.anchor(cell.grow(gutterPadding), flow.withRow(1), true);
            }
        }
    }
}
