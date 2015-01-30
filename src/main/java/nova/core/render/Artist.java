package nova.core.render;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist is capable of storing render information for game engine rendering.
 *
 * @author Calclavia
 */
public abstract class Artist
{
	protected final List<Canvas> canvases = new ArrayList<>();

	public void drawCanvas(Canvas canvas)
	{
		canvases.add(canvas);
	}
}
