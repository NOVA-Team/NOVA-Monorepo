package nova.core.util;

import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class Ray {
	public final Vector3D origin;
	public final Vector3D dir;
	public final Vector3D invDir;

	public final boolean ignoreX;
	public final boolean ignoreY;
	public final boolean ignoreZ;

	public final boolean signDirX;
	public final boolean signDirY;
	public final boolean signDirZ;

	/**
	 * @param origin The ray's beginning
	 * @param dir The ray's direction (unit vector)
	 */
	public Ray(Vector3D origin, Vector3D dir) {
		this.origin = origin;
		this.dir = dir;
		this.invDir = Vector3DUtil.reciprocal(dir);
		this.signDirX = invDir.getX() < 0;
		this.signDirY = invDir.getY() < 0;
		this.signDirZ = invDir.getZ() < 0;

		this.ignoreX = Math.abs(dir.getX()) < 0.0000001;
		this.ignoreY = Math.abs(dir.getY()) < 0.0000001;
		this.ignoreZ = Math.abs(dir.getZ()) < 0.0000001;
	}

	public static Ray fromInterval(Vector3D start, Vector3D end) {
		return new Ray(start, end.subtract(start).normalize());
	}
}
