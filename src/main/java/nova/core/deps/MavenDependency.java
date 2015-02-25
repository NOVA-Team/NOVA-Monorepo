package nova.core.deps;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mitchellbrine on 2015.
 */
public class MavenDependency implements Dependency{

	private String repo, dependencyID, groupID, artifactID, version;

	public MavenDependency(String mavenRepo, String groupId, String artifactId, String version) {
		this.repo = mavenRepo;
		this.dependencyID = groupId + "." + artifactID;
		this.groupID = groupId;
		this.artifactID = artifactId;
		this.version = version;
	}

	@Override
	public String getDependencyTypeID() {
		return "maven";
	}

	@Override
	public String getDependencyID() {
		return this.dependencyID;
	}

	@Override
	public String getVersionID() {
		return this.version;
	}

	@Override
	public URL getDownloadURL() throws MalformedURLException{
		return new URL(this.repo + this.groupID.replaceAll(".","/") + "/" + this.artifactID + "-" + this.version + ".jar");
	}
}
