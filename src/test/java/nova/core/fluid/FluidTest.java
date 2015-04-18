package nova.core.fluid;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by magik6k on 4/18/15.
 */
public class FluidTest {
    @Test
    public void testTypes() {
        Fluid fluid1 = new Fluid("water").setAmount(100);
        Fluid fluid2 = new Fluid("water").setAmount(10);
        Fluid fluid3 = new Fluid("lava").setAmount(100);

        assertThat(fluid1.sameType(fluid2)).isTrue();
        assertThat(fluid1.sameType(fluid3)).isFalse();
    }

    @Test
    public void testEqual() {
        Fluid fluid1 = new Fluid("water").setAmount(100);
        Fluid fluid2 = new Fluid("water").setAmount(100);
        Fluid fluid3 = new Fluid("water").setAmount(10);
        Fluid fluid4 = new Fluid("lava").setAmount(100);

        assertThat(fluid1.equals(null)).isFalse();
        assertThat(fluid1.equals(new Object())).isFalse();
        assertThat(fluid1.equals(fluid2)).isTrue();
        assertThat(fluid1.equals(fluid3)).isFalse();
        assertThat(fluid1.equals(fluid4)).isFalse();
    }

    @Test
    public void testClone() {
        Fluid fluid1 = new Fluid("water").setAmount(100);
        Fluid cloned = fluid1.clone();

        assertThat(fluid1.equals(cloned)).isTrue();
        assertThat(fluid1).isNotSameAs(cloned);
    }

    @Test
    public void testWithAmount() {
        Fluid fluid1 = new Fluid("water").setAmount(100);
        Fluid fluid2 = fluid1.withAmount(10);

        assertThat(fluid1.sameType(fluid2)).isTrue();
        assertThat(fluid1.equals(fluid2)).isFalse();
        assertThat(fluid2.amount()).isEqualTo(10);
    }

    @Test
    public void testAmountSetting() {
        Fluid fluid1 = new Fluid("water").setAmount(100);

        fluid1.setAmount(10);
        assertThat(fluid1.amount()).isEqualTo(10);

        fluid1.setAmount(-10);
        assertThat(fluid1.amount()).isEqualTo(1);
    }

    @Test
    public void testAmountAdding() {
        Fluid fluid1 = new Fluid("water").setAmount(10);

        assertThat(fluid1.add(10)).isEqualTo(10);
        assertThat(fluid1.amount()).isEqualTo(20);

        assertThat(fluid1.add(-30)).isEqualTo(-19);
        assertThat(fluid1.amount()).isEqualTo(1);

        fluid1.setAmount(10);

        assertThat(fluid1.remove(5)).isEqualTo(5);
        assertThat(fluid1.amount()).isEqualTo(5);

        assertThat(fluid1.remove(10)).isEqualTo(4);
        assertThat(fluid1.amount()).isEqualTo(1);
    }
}
