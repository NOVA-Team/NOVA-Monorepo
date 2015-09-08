package nova.core.component.fluid;

import nova.core.component.Component;
import nova.core.util.Direction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Calclavia
 */
public class FluidHandler extends Component {

	public Set<Tank> tanks = new HashSet<>();
	public Function<Direction, Set<Tank>> sidedTanks = direction -> tanks;

	public FluidHandler() {
	}

	public FluidHandler(Tank... tanks) {
		this.tanks.addAll(Arrays.asList(tanks));
	}

}
