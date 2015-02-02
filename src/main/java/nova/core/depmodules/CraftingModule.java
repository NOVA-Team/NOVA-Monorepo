package nova.core.depmodules;

import nova.core.recipes.crafting.CraftingRecipeManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class CraftingModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(CraftingRecipeManager.class).toConstructor();
	}

}
