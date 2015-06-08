package nova.internal.core.di;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bootstrap.SuppliedBy;

import static se.jbee.inject.util.ToString.describe;

final class SupplierSupplier implements Supplier<java.util.function.Supplier<?>> {

	@Override
	public java.util.function.Supplier<?> supply( Dependency<? super java.util.function.Supplier<?>> dependency, Injector injector ) {
		Dependency<?> providedType = dependency.onTypeParameter();
		if ( !dependency.getName().isDefault() ) {
			providedType = providedType.named( dependency.getName() );
		}
		final Dependency<?> finalProvidedType = providedType;
		return () -> SuppliedBy.lazyProvider(finalProvidedType.uninject().ignoredExpiry(), injector).provide();
	}

	@Override
	public String toString() {
		return describe( "supplies", java.util.function.Supplier.class );
	}
}
