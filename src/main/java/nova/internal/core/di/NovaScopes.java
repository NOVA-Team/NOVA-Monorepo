package nova.internal.core.di;

import se.jbee.inject.Demand;
import se.jbee.inject.Injectable;
import se.jbee.inject.Repository;
import se.jbee.inject.Scope;

public class NovaScopes {
	public static final Scope MULTIPLE_INSTANCES = new InjectionScope();
}

class InjectionScope implements Scope, Repository {

	InjectionScope() {
		// make visible
	}

	@Override
	public Repository init() {
		return this;
	}

	@Override
	public <T> T serve(Demand<T> demand, Injectable<T> injectable) {
		return injectable.instanceFor(demand);
	}

	@Override
	public String toString() {
		return "(default)";
	}
}
