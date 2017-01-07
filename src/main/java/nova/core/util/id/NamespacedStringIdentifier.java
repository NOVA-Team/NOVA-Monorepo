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

	public NamespacedStringIdentifier(String id) {
		super(id.substring(id.indexOf(':') + 1));
		this.domain = (id.contains(":") ? id.substring(0, id.indexOf(':')) : "nova");
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
		return domain + ':' + id;
	}
}
