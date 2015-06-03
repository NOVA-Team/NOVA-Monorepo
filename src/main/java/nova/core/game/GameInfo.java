package nova.core.game;

/**
 * Provides information about the game being run on.
 * @author Calclavia
 */
public class GameInfo {
	/**
	 * The name of the game.
	 */
	public final String name;

	/**
	 * The version of the game.
	 */
	public final String version;

	//TODO: Add mods loaded

	public GameInfo(String name, String version) {
		this.name = name;
		this.version = version;
	}
}
