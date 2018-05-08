package test;

import model.Complex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexTest {

    private Complex c1;
    private Complex c2;

    @BeforeEach
    public void before() {
        c1 = new Complex(1, 1);
        c2 = new Complex(2, 5);
    }

    @Test
    public void sumTest() {
        assertEquals(c1, c1.sum(new Complex()), "Base case");
        assertEquals(c1.sum(c2), c2.sum(c1), "Symmetry");
        assertEquals(new Complex(3, 6), c1.sum(c2), "C1 + C2");
    }

    @Test
    public void subTest() {
        assertEquals(c1, c1.sub(new Complex()), "Base case");
        assertEquals(c2.sub(c1).mul(-1), c1.sub(c2),"Inverse Symmetry");
        assertEquals(new Complex(-1, -4), c1.sub(c2), "C1 - C2");
    }

    @Test
    public void mulTest() {
        assertEquals(c1, c1.mul(1), "Base case");
        assertEquals(c1.mul(c2), c2.mul(c1), "Symmetry");
        assertEquals(new Complex(-3, 7), c1.mul(c2), "C1 * C2");
        assertEquals(new Complex(8, 20), c2.mul(4), "Mul by 4");
        assertEquals(new Complex(29), c2.mul(c2.conjugate()), "Mul by conjugate");
    }

    @Test
    public void divTest() {
        assertEquals(c1, c1.div(1), "Base case");
        assertEquals(new Complex(0.24137931, -0.103448275), c1.div(c2), "C1 / C2");
    }

}
