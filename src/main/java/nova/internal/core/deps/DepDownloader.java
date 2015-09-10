/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.internal.core.deps;

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
