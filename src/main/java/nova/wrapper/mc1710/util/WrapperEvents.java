package nova.wrapper.mc1710.util;

import nova.core.event.GlobalEvents;
import nova.core.util.Direction;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class WrapperEvents {

	public static class RedstoneConnectEvent extends GlobalEvents.BlockEvent {
		public final Direction direction;
		public boolean canConnect;

		public RedstoneConnectEvent(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class StrongRedstoneEvent extends GlobalEvents.BlockEvent {
		public final Direction direction;
		public int power;

		public StrongRedstoneEvent(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class WeakRedstoneEvent extends GlobalEvents.BlockEvent {
		public final Direction direction;
		public int power;

		public WeakRedstoneEvent(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}
}
