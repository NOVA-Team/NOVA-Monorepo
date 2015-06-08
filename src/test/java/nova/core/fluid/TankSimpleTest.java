package nova.core.fluid;

import nova.core.fluid.component.TankSimple;
import nova.core.retention.Data;
import org.junit.Test;

import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author magik6k
 */
public class TankSimpleTest {

    @Test
    public void testBasic() {
        TankSimple tank = new TankSimple(150);

        assertThat(tank.hasFluid()).isFalse();
        assertThat(tank.addFluid(new Fluid("water").setAmount(100))).isEqualTo(100);
        assertThat(tank.getFluidAmount()).isEqualTo(100);
        assertThat(tank.addFluid(new Fluid("lava").setAmount(100))).isEqualTo(0);
        assertThat(tank.getFluidAmount()).isEqualTo(100);
        assertThat(tank.addFluid(new Fluid("water").setAmount(100))).isEqualTo(50);
        assertThat(tank.getFluidAmount()).isEqualTo(150);

        assertThat(tank.removeFluid(100)).isEqualTo(Optional.of(new Fluid("water").withAmount(100)));
        assertThat(tank.getFluidAmount()).isEqualTo(50);
        assertThat(tank.removeFluid(100)).isEqualTo(Optional.of(new Fluid("water").withAmount(50)));
        assertThat(tank.getFluidAmount()).isEqualTo(0);

        assertThat(tank.addFluid(new Fluid("lava").setAmount(100))).isEqualTo(100);
        assertThat(tank.getFluidAmount()).isEqualTo(100);
    }

    @Test
    public void storeTest() {
		TankSimple tank = new TankSimple(150);
		assertThat(tank.addFluid(new Fluid("water").setAmount(100))).isEqualTo(100);

        Data tankData = new Data();
        tank.save(tankData);

        //TODO: Should store capacity?
        tank = new TankSimple(150);
        tank.load(tankData);

        assertThat(tank.hasFluid()).isTrue();
        assertThat(tank.hasFluidType(new Fluid("water"))).isTrue();
        assertThat(tank.getFluid().get().equals(new Fluid("water").setAmount(100))).isTrue();
    }

}
