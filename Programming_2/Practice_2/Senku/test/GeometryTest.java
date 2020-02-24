import acm.graphics.GDimension;
import acm.graphics.GPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeometryTest {

    Geometry geometry = new Geometry(
            800,
            300,
            4,
            3,
            0.05,
            0.10);

    @Test
    void getters() {
        assertEquals(3, geometry.getRows());
        assertEquals(4, geometry.getColumns());
    }

    @Test
    void board_dimension() {
        assertEquals(
                new GDimension(720.0, 270.0),
                geometry.boardDimension());
    }

    @Test
    void board_top_left() {
        assertEquals(
                new GPoint(40.0, 15.0),
                geometry.boardTopLeft());
    }

    @Test
    void cell_dimension() {
        assertEquals(
                new GDimension(180.0, 90.0),
                geometry.cellDimension());
    }

    @Test
    void cell_top_left() {
        assertEquals(
                new GPoint(400.0, 105.0),
                geometry.cellTopLeft(2, 1));
    }

    @Test
    void token_dimension() {
        assertEquals(
                new GDimension(144.0, 72.0),
                geometry.tokenDimension());
    }

    @Test
    void token_top_left() {
        assertEquals(
                new GPoint(418.0, 114.0),
                geometry.tokenTopLeft(2, 1));
    }

    @Test
    void xy_to_cell() {
        assertEquals(
                new Position(3, 1),
                geometry.xyToCell(600.0, 110.0));
    }

    @Test
    void center_at() {
        assertEquals(
                new GPoint(310.0, 240.0),
                geometry.centerAt(1, 2));
    }
}