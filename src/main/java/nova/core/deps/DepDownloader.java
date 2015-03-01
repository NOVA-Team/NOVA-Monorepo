package nova.core.deps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author Mitchellbrine
 */
public abstract class DepDownloader {

	public File locationName;

	public DepDownloader(File location) {
		this.locationName = location;
	}

	public DepDownloader(String location) {
		this.locationName = new File(location);
	}

	public void downloadDepdency(URL versionURL, String locationToDownloadTo) {
		try {

			File dependencyLocation = new File(locationToDownloadTo);

			/**
			 * This should make sure that if we already have
			 */
			if (dependencyLocation.exists()) { return; }

			InputStream stream = versionURL.openStream();

			byte[] buffer = new byte[4096];
			int n = -1;

			OutputStream output = new FileOutputStream(dependencyLocation);
			while ((n = stream.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}
			output.close();
			stream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
