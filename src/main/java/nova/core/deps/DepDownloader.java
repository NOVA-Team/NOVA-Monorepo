package nova.core.deps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by Mitchellbrine on 2015.
 */
public abstract class DepDownloader {

	public void downloadDepdency(URL versionURL, String locationToDownloadTo) {
		try {

			InputStream stream = versionURL.openStream();

			File dependencyLocation = new File(locationToDownloadTo);

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
