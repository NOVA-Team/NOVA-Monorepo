package nova.core.util;

import nova.core.util.transform.vector.Vector3d;

/**
 * @author Calclavia
 */
public class Ray {
	public final Vector3d origin;
	public final Vector3d dir;
	public final Vector3d invDir;

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
	public Ray(Vector3d origin, Vector3d dir) {
		this.origin = origin;
		this.dir = dir;
		this.invDir = dir.reciprocal();
		this.signDirX = invDir.x < 0;
		this.signDirY = invDir.y < 0;
		this.signDirZ = invDir.z < 0;

		this.ignoreX = Math.abs(dir.x) < 0.0000001;
		this.ignoreY = Math.abs(dir.y) < 0.0000001;
		this.ignoreZ = Math.abs(dir.z) < 0.0000001;
	}

	public static Ray fromInterval(Vector3d start, Vector3d end) {
		return new Ray(start, end.subtract(start).normalize());
	}
}
