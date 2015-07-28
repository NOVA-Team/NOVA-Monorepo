package nova.core.render;

import nova.core.util.Identifiable;

/**
 * @author Calclavia
 */
//TODO: Texture should extend Asset
public abstract class Asset implements Identifiable {
	//The domain of the assets
	public final String domain;
	//The name of the file
	public final String name;

	public Asset(String domain, String name) {
		this.domain = domain;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Asset other = (Asset) obj;
			return other.getID().equals(getID());
		}

		return false;
	}

	@Override
	public final String getID() {
		return domain + ":" + name;
	}
}
