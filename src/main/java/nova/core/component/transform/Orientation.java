package nova.core.component.transform;

import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.entity.component.Living;
import nova.core.event.Event;
import nova.core.network.Packet;
import nova.core.network.Sync;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.Direction;
import nova.core.util.RayTracer;
import nova.core.util.math.RotationUtil;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

import java.util.Optional;

/**
 * A component that is applied to providers with discrete orientations.
 * @author Calclavia
 */
public class Orientation extends Component implements Storable, Stateful, Syncable {

	public final ComponentProvider provider;

	/**
	 * The allowed rotation directions the block can face.
	 */
	public int rotationMask = 0x3C;
	public boolean isFlip = false;
	/**
	 * The direction the block is facing.
	 */
	@Sync
	@Store
	protected Direction orientation = Direction.UNKNOWN;

	/**
	 * @param provider The provider to apply discrete orientations to
	 */
	public Orientation(ComponentProvider provider) {
		this.provider = provider;
	}

	/**
	 * Hooks the needed events intro the provider to rotate based on the side that is hit when placing the block
	 *
	 * @return The Orientation instance
	 */
	public Orientation hookBasedOnHitSide() {
		if (provider instanceof Block) {
			((Block) provider).events.on(Block.PlaceEvent.class).bind(
				evt ->
				{
					if (Game.network().isServer()) {
						setOrientation(calculateDirection(evt.placer));
					}
				}
			);
			((Block) provider).events.on(Block.RightClickEvent.class).bind(
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

	/**
	 * Hooks the needed events intro the provider to rotate based on the {@link Entity}'s rotation when placing the block
	 *
	 * @return The Orientation instance
	 */
	public Orientation hookBasedOnEntity() {
		if (provider instanceof Block) {
			((Block) provider).events.on(Block.PlaceEvent.class).bind(
				event ->
				{
					if (Game.network().isServer()) {
						setOrientation(calculateDirectionFromEntity(event.placer));
					}
				}
			);
		}
		return this;
	}

	/**
	 * @return The current orientation
	 */
	public Direction orientation() {
		return orientation;
	}

	/**
	 * Changes the orientation
	 *
	 * @param orientation New orientation
	 * @return The Orientation instance
	 */
	public Orientation setOrientation(Direction orientation) {
		if (this.orientation == orientation)
			return this;
		this.orientation = orientation;
		provider.events.publish(new OrientationChangeEvent());
		return this;
	}

	/**
	 * Set's the rotation mask
	 *
	 * @param mask New rotation mask
	 * @return The Orientation instance
	 */
	public Orientation setMask(int mask) {
		this.rotationMask = mask;
		return this;
	}

	/**
	 * Set to true to use the oposite direction
	 *
	 * @param flip should flip rotation or not
	 * @return The Orientation instance
	 */
	public Orientation flipPlacement(boolean flip) {
		isFlip = flip;
		return this;
	}

	/**
	 * Calculates the direction using raytracing
	 *
	 * @param entity The entity to start raytracing from
	 * @return The side of the block that is hit with the raytracing
	 */
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

	/**
	 * Determines the direction the block is facing based on the entity's facing
	 *
	 * @param entity The entity used to determine rotation
	 * @return The direction the block is facing
	 */

	public Direction calculateDirectionFromEntity(Entity entity) {
		if (provider instanceof Block) {
			Vector3D position = entity.position();
			if (FastMath.abs(position.getX() - ((Block) provider).y()) < 2.0F && FastMath.abs(position.getZ() - ((Block) provider).z()) < 2.0F) {
				double height = position.add(entity.get(Living.class).faceDisplacement.get()).getY();

				if (height - ((Block) provider).y() > 2.0D) {
					return Direction.fromOrdinal(1);
				}

				if (((Block) provider).y() - height > 0.0D) {
					return Direction.fromOrdinal(0);
				}
			}

			int l = (int) FastMath.floor((entity.rotation().getAngles(RotationUtil.DEFAULT_ORDER)[0] * 4.0F / 360.0F) + 0.5D) & 3;
			int dir = l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
			return (isFlip) ? Direction.fromOrdinal(dir).opposite() : Direction.fromOrdinal(dir);
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
	public boolean rotate(int side, Vector3D hit) {
		int result = getSideToRotate(side, hit);

		if (result != -1) {
			setOrientation(Direction.fromOrdinal(result));
			provider.events.publish(new OrientationChangeEvent());
			return true;
		}

		return false;
	}

	/**
	 * Determines the side to rotate based on the hit vector on the block.
	 */
	public int getSideToRotate(int hitSide, Vector3D hit) {
		int tBack = hitSide ^ 1;

		switch (hitSide) {
			case 0:
			case 1:
				if (hit.getX() < 0.25) {
					if (hit.getZ() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getZ() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(4)) {
						return 4;
					}
				}
				if (hit.getX() > 0.75) {
					if (hit.getZ() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getZ() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(5)) {
						return 5;
					}
				}
				if (hit.getZ() < 0.25) {
					if (canRotate(2)) {
						return 2;
					}
				}
				if (hit.getZ() > 0.75) {
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
				if (hit.getX() < 0.25) {
					if (hit.getY() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getY() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(4)) {
						return 4;
					}
				}
				if (hit.getX() > 0.75) {
					if (hit.getY() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getY() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(5)) {
						return 5;
					}
				}
				if (hit.getY() < 0.25) {
					if (canRotate(0)) {
						return 0;
					}
				}
				if (hit.getY() > 0.75) {
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
				if (hit.getZ() < 0.25) {
					if (hit.getY() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getY() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(2)) {
						return 2;
					}
				}
				if (hit.getZ() > 0.75) {
					if (hit.getY() < 0.25) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (hit.getY() > 0.75) {
						if (canRotate(tBack)) {
							return tBack;
						}
					}
					if (canRotate(3)) {
						return 3;
					}
				}
				if (hit.getY() < 0.25) {
					if (canRotate(0)) {
						return 0;
					}
				}
				if (hit.getY() > 0.75) {
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

	public static class OrientationChangeEvent extends Event {

	}
}
