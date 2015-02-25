package nova.core.deps;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mitchellbrine on 2015.
 */
public class URLDependency implements Dependency {

	private String id, version, url;

	public URLDependency(String identifier,String version, String url) {
		this.id = identifier;
		this.version = version;
		this.url = url;
	}


	@Override
	public String getDependencyTypeID() {
		return "URL";
	}

	@Override
	public String getDependencyID() {
		return this.id;
	}

	@Override
	public String getVersionID() {
		return this.version;
	}

	@Override
	public URL getDownloadURL() throws MalformedURLException {
		return new URL(url);
	}
}
