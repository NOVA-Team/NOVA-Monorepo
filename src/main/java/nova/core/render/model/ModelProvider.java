package nova.core.render.model;

import nova.core.render.Asset;

import java.io.InputStream;

/**
 * All models must be included in /assets/domain/models/
 *
 * @author Calclavia
 */
public abstract class ModelProvider extends Asset {

	/**
	 * Creates new ModelProvider
	 *
	 * @param domain dolain of the assets.
	 * @param name name of the model.
	 */
	public ModelProvider(String domain, String name) {
		super(domain, name);
	}

	/**
	 * Loads the model with a input stream.
	 *
	 * @param stream The {@link InputStream}
	 */
	public abstract void load(InputStream stream);

	public abstract Model getModel();

	public abstract String getType();
}
