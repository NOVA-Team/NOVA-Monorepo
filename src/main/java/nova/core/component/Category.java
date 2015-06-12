package nova.core.component;

import nova.core.item.Item;
import nova.core.util.Identifiable;

/**
 * For object that belong specific categories.
 * Used by blocks and items to sort into manageable categories for the game.
 * @author Calclavia
 */
public class Category extends Component implements Identifiable {

	public final String name;
	public final Item item;

	public Category(String name, Item item) {
		this.name = name;
		this.item = item;
	}

	public Category(String name) {
		this(name, null);
	}

	@Override
	public String getID() {
		return name;
	}
}
