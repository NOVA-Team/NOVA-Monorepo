package nova.internal.core.di;

import se.jbee.inject.bind.BinderModule;

import java.util.function.Supplier;

public class DICoreModule extends BinderModule {


	@Override
	protected void declare() {

		//install(BuildinBundle.PROVIDER); //To allow injection of providers
		starbind(Supplier.class).to(new SupplierSupplier());
	}

}
