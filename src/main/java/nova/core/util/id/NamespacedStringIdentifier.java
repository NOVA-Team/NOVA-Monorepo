/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

/**
 * A Namespaced String Identifier.
 *
 * @author ExE Boss
 */
public class NamespacedStringIdentifier extends StringIdentifier {

	protected final String domain;

	public NamespacedStringIdentifier(final String id) {
		super(id == null ? null : id.substring(id.indexOf(':') + 1));
		if (id != null)
			this.domain = (id.contains(":") ? id.substring(0, id.indexOf(':')) : null);
		else
			this.domain = null;
	}

	public NamespacedStringIdentifier(String domain, String id) {
		super(id);
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public String getId() {
		return id;
	}

	@Override
	public String asString() {
		return (domain == null ? id : (domain + ':' + id));
	}
}
