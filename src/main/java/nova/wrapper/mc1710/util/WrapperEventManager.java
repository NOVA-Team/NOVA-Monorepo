package nova.wrapper.mc1710.util;

import nova.core.event.EventBus;
import nova.core.event.GlobalEvents;
import nova.core.util.Direction;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class WrapperEventManager {

	//Called when requesting strong power of a block
	public final EventBus<RedstoneEvent> onStrongPower = new EventBus<>();

	//Called when requesting weak power of a block
	public final EventBus<RedstoneEvent> onWeakPower = new EventBus<>();

	//Called when asked if a block can connect to Redstone
	public final EventBus<RedstoneConnectEvent> onCanConnect = new EventBus<>();

	public static class RedstoneConnectEvent extends GlobalEvents.BlockEvent {
		public final Direction direction;
		public boolean canConnect;

		public RedstoneConnectEvent(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class RedstoneEvent extends GlobalEvents.BlockEvent {
		public final Direction direction;
		public int power;

		public RedstoneEvent(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}
}
