package nova.core.fluid;

import nova.core.retention.Data;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by magik6k on 4/18/15.
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
        assertThat(tank.getFluid().equals(new Fluid("water").setAmount(100))).isTrue();
    }

}
