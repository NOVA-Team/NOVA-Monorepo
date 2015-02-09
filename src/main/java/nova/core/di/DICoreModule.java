package nova.core.di;

import java.util.function.Supplier;

import se.jbee.inject.bind.BinderModule;

public class DICoreModule extends BinderModule {

	
	@Override
	protected void declare() {

		//install(BuildinBundle.PROVIDER); //To allow injection of providers
		starbind(Supplier.class).to(new SupplierSupplier());
	}

}
