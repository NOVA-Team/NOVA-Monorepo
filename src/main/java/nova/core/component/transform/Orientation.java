package nova.core.component.transform;

import nova.core.block.Block;
import nova.core.block.Stateful;
import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.network.Sync;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.Direction;
import nova.core.util.transform.vector.Vector3d;

/**
 * A component that is applied to providers with discrete orientations.
 * @author Calclavia
 */
public class Orientation extends Component implements Storable, Stateful {

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
	@Stored
	public Direction orientation = Direction.UNKNOWN;

	public Orientation(ComponentProvider provider) {
		this.provider = provider;
	}

	public Orientation hookBlockEvents() {
		if (provider instanceof Block) {
			((Block) provider).blockPlaceEvent.add(evt -> evt.by.ifPresent(by -> orientation = calculateDirection(by)));
			((Block) provider).rightClickEvent.add(evt -> rotate(evt.side.ordinal(), evt.position));
		}
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
			if (Math.abs(entity.transform.position().x - ((Block) provider).x()) < 2 && Math.abs(entity.transform.position().z - ((Block) provider).z()) < 2) {
				double height = entity.transform.position().y + 1.82D;//- entity.yOffset

				if (canRotate(1) && height - ((Block) provider).y() > 2.0D) {
					return Direction.UP;
				}
				if (canRotate(0) && ((Block) provider).y() - height > 0.0D) {
					return Direction.DOWN;
				}
			}

			int playerSide = (int) Math.floor(entity.transform.rotation().toEuler().x * 4.0F / 360.0F + 0.5D) & 3;
			int returnSide = (playerSide == 0 && canRotate(2)) ? 2 : ((playerSide == 1 && canRotate(5)) ? 5 : playerSide == 2 && canRotate(3) ? 3 : (playerSide == 3 && canRotate(4)) ? 4 : 0);

			if (isFlip) {
				return Direction.fromOrdinal(returnSide).opposite();
			}
			return Direction.fromOrdinal(returnSide);
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
			orientation = Direction.fromOrdinal(result);
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
				break;
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
				break;
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
				break;
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
}
