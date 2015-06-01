package nova.core.deps;

import nova.core.util.exception.NovaException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * @author rx14
 */
public class MavenDependency {

	final String repoURL;
	final String groupID;
	final String artifactID;
	final String version;
	final String classifier;
	final String ext;

	public MavenDependency(String mavenRepo,
	                       String groupId,
	                       String artifactId,
	                       String version,
	                       String classifier,
	                       String ext) {
		this.repoURL = mavenRepo.isEmpty() ? "http://maven.novaapi.net/" : mavenRepo;

		this.groupID = groupId;
		this.artifactID = artifactId;
		this.version = version;

		this.classifier = classifier;
		this.ext = ext;
	}

	public MavenDependency(Dependency annotation) {
		this.repoURL = annotation.mavenRepo();

		this.groupID = annotation.groupId();
		this.artifactID = annotation.artifactId();
		this.version = annotation.version();

		this.classifier = annotation.classifier();
		this.ext = annotation.ext();
	}

	public String getDir() {
		return this.groupID.replaceAll(".", "/") + "/" + this.artifactID + "/" + this.version;
	}

	public String getPath() {
		return MessageFormat.format("{0}/{1}-{2}{3}.{4}",
		                    /*{0}*/ getDir(),
							/*{1}*/ this.artifactID,
		                    /*{2}*/ this.version,
		                    /*{3}*/ this.classifier.isEmpty() ? "" : "-" + this.classifier,
		                    /*{4}*/ ext);
	}

	public URL getDownloadURL() {
		try {
			return new URL(this.repoURL + getPath());
		} catch (MalformedURLException e) {
			throw new NovaException(e);
		}
	}
}
