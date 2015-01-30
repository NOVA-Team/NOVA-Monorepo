package nova.core.render;

import nova.core.util.transform.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist is capable of storing render information for game engine rendering.
 *
 * @author Calclavia
 */
public abstract class Artist
{
	public Vector3d translation = new Vector3d();
	public Vector3d rotation = new Vector3d();
	public Vector3d scale = new Vector3d();
	private List<Vector3d> verticies = new ArrayList<>();

	/**
	 * Binds a specific texture to this artist.
	 */
	public void bindTexture()
	{

	}

	public void drawVertex(Vector3d pos)
	{
		verticies.add(pos);
	}

	public void drawQuad(Vector3d start)
	{

	}
}
