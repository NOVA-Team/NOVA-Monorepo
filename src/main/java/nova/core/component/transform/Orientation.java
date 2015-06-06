package nova.core.component.transform;

import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.event.Event;
import nova.core.event.EventBus;
import nova.core.game.Game;
import nova.core.network.Packet;
import nova.core.network.Syncable;
import nova.core.network.Sync;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.Direction;
import nova.core.util.RayTracer;
import nova.core.util.transform.vector.Vector3d;

import java.util.Optional;

/**
 * A component that is applied to providers with discrete orientations.
 *
 * @author Calclavia
 */
public class Orientation extends Component implements Storable, Stateful, Syncable {

	public final ComponentProvider provider;

	public final EventBus<Event> onOrientationChange = new EventBus<>();

	/**
	 * The allowed rotation directions the block can face.
	 */
	public int rotationMask = 0x3C;
	public boolean isFlip = false;
	/**
	 * The direction the block is facing.
	 */
	@Sync
	@Stored
	protected Direction orientation = Direction.UNKNOWN;

	public Orientation(ComponentProvider provider) {
		this.provider = provider;
	}

	public Orientation hookBlockEvents() {
		if (provider instanceof Block) {
			((Block) provider).placeEvent.add(
				evt ->
				{
					if (Game.network().isServer()) {
						setOrientation(calculateDirection(evt.placer));
					}
				}
			);
			((Block) provider).rightClickEvent.add(
				evt ->
				{
					if (Game.network().isServer()) {
						rotate(evt.side.ordinal(), evt.position);
					}
				}
			);
		}
		return this;
	}

	public Direction orientation() {
		return orientation;
	}

	public Orientation setOrientation(Direction orientation) {
		this.orientation = orientation;
		onOrientationChange.publish(new Event());
		return this;
	}

	public Orientation setMask(int mask) {
		this.rotationMask = mask;
		return this;
	}

	public Orientation flipPlacement(boolean flip) {
		isFlip = flip;
		return this;
	}

	public Direction calculateDirection(Entity entity) {
		if (provider instanceof Block) {
			Optional<RayTracer.RayTraceBlockResult> hit = new RayTracer(entity)
				.setDistance(7)
				.rayTraceBlocks(entity.world())
				.filter(res -> res.block != provider)
				.findFirst();

			if (hit.isPresent()) {
				return (isFlip) ? hit.get().side.opposite() : hit.get().side;
			}
		}

		return Direction.UNKNOWN;
	}

	public boolean canRotate(int side) {
		return (rotationMask & (1 << side)) != 0;
	}

	public boolean canRotate(Direction side) {
		return canRotate(side.ordinal());
	}

	/**
	 * Rotatable Block
	 */
	public boolean rotate(int side, Vector3d hit) {
		int result = getSideToRotate(side, hit);

		if (result != -1) {
			setOrientation(Direction.fromOrdinal(result));
			onOrientationChange.publish(new Event());
			return true;
		}

		return false;
	}

	/**
	 * Determines the side to rotate based on the hit vector on the block.
	 */
	public int getSideToRotate(int hitSide, Vector3d hit) {
		int tBack = hitSide ^ 1;

		switch (hitSide) {
			case 0:
			case 1:
				if (hit.x < 0.25) {
					if (hit.z < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.z > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(4)) {
						return 4;
					}
				}
				if (hit.x > 0.75) {
					if (hit.z < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.z > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(5)) {
						return 5;
					}
				}
				if (hit.z < 0.25) {
					if (canRotate(2)) {
						return 2;
					}
				}
				if (hit.z > 0.75) {
					if (canRotate(3)) {
						return 3;
					}
				}
				if (canRotate(hitSide)) {
					return hitSide;
				}
				break;
			case 2:
			case 3:
				if (hit.x < 0.25) {
					if (hit.y < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.y > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(4)) {
						return 4;
					}
				}
				if (hit.x > 0.75) {
					if (hit.y < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.y > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(5)) {
						return 5;
					}
				}
				if (hit.y < 0.25) {
					if (canRotate(0)) {
						return 0;
					}
				}
				if (hit.y > 0.75) {
					if (canRotate(1)) {
						return 1;
					}
				}
				if (canRotate(hitSide)) {
					return hitSide;
				}
				break;
			case 4:
			case 5:
				if (hit.z < 0.25) {
					if (hit.y < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.y > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(2)) {
						return 2;
					}
				}
				if (hit.z > 0.75) {
					if (hit.y < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.y > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(3)) {
						return 3;
					}
				}
				if (hit.y < 0.25) {
					if (canRotate(0)) {
						return 0;
					}
				}
				if (hit.y > 0.75) {
					if (canRotate(1)) {
						return 1;
					}
				}
				if (canRotate(hitSide)) {
					return hitSide;
				}
				break;
		}
		return -1;
	}

	@Override
	public void save(Data data) {
		data.put("orientation", orientation.ordinal());
	}

	@Override
	public void load(Data data) {
		orientation = Direction.fromOrdinal(data.get("orientation"));
	}

	@Override
	public void read(Packet packet) {
		setOrientation(Direction.fromOrdinal(packet.readInt()));
	}

	@Override
	public void write(Packet packet) {
		packet.writeInt(orientation.ordinal());
	}
}
