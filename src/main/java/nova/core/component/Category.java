package nova.core.component;

import nova.core.util.Identifiable;

/**
 * For object that belong specific categories.
 * Used by blocks and items to sort into manageable categories for the game.
 * @author Calclavia
 */
public class Category extends Component implements Identifiable {

	public final String name;

	public Category(String name) {
		this.name = name;
	}

	@Override
	public String getID() {
		return name;
	}
}
