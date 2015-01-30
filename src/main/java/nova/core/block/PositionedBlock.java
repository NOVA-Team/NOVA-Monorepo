package nova.core.block;

import nova.core.util.transform.Vector3i;

/**
 *
 * @author Stan Hebben
 */
public interface PositionedBlock {
	public BlockAccess getBlockAccess();
	
	public Vector3i getPosition();
	
	public default int getX() {
		return getPosition().x;
	}
	
	public default int getY() {
		return getPosition().y;
	}
	
	public default int getZ() {
		return getPosition().z;
	}
}
