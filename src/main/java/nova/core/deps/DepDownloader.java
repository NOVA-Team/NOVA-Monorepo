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

	/**
	 * Downloads the URL to the file if the file does not exist.
	 * This provides extremely basic caching based on the assumption
	 * that each version of the file has a unique output path.
	 *
	 * @param in the URL to download from
	 * @param out the file to download to
	 */
	public static void downloadDepdency(URL in, String out) {
		try {

			Path outPath = Paths.get(out);

			/**
			 * Do not overwrite previous files, this effectively is caching
			 */
			if (outPath.toFile().exists()) { return; }

			Files.copy(in.openStream(), outPath, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
