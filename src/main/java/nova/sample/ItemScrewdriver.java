package nova.sample;

import nova.core.item.Item;
import nova.core.render.ItemTexture;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class ItemScrewdriver extends Item {

	@Override
	public Optional<ItemTexture> getTexture() {
		return Optional.of(NovaTest.screwTexture);
	}

	@Override
	public String getID() {
		return "screwdriver";
	}
}
