package nova.core.wrapper.mc18.util;

import nova.core.event.BlockEvent;
import nova.core.event.bus.GlobalEvents;
import nova.core.util.Direction;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Events for wrappers to hook into
 * @author Calclavia
 */
public class WrapperEvent {

	public static class RedstoneConnect extends BlockEvent {
		public final Direction direction;
		public boolean canConnect;

		public RedstoneConnect(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class StrongRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public StrongRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class WeakRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public WeakRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}
}
