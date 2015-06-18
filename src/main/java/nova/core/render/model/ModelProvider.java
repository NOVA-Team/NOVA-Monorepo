package nova.core.render.model;

import java.io.InputStream;

/**
 * All models must be included in /assets/domain/models/
 *
 * @author Calclavia
 */
public abstract class ModelProvider {
	//The domain of the assets
	public final String domain;
	//The name of the file
	public final String name;

	/**
	 * Creates new ModelProvider
	 * @param domain dolain of the assets.
	 * @param name name of the model.
	 */
	public ModelProvider(String domain, String name) {
		this.domain = domain;
		this.name = name;
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
