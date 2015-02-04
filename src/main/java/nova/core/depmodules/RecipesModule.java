package nova.core.depmodules;

import nova.core.recipes.RecipeManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class RecipesModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(RecipeManager.class).toConstructor();
	}

}
