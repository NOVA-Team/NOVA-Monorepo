package nova.core.depmodules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import nova.core.entity.EntityManager;

class EntityModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EntityManager.class).in(Singleton.class);
	}

}
