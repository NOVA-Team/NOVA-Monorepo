package nova.core.deps;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Mitchellbrine
 */
public class DepDownloader {

	public static void downloadDepdency(URL downloadURL, String locationToDownloadTo) {
		try {

			Path dependencyLocation = Paths.get(locationToDownloadTo);

			/**
			 * Do not overwrite previous files, this effectively is caching
			 */
			if (dependencyLocation.toFile().exists()) { return; }

			Files.copy(downloadURL.openStream(), dependencyLocation, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
