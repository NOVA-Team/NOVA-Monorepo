package nova.core.depmodules;

import nova.core.item.OreDictionary;
import nova.core.recipes.crafting.CraftingRecipeManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class DictionaryModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(OreDictionary.class).toConstructor();
	}

}
