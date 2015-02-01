package nova.wrapper.mc1710.render;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FolderResourcePack;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class NovaFolderResourcePack extends FolderResourcePack {
	private final String modid;

	public NovaFolderResourcePack(File file, String modid) {
		super(file);
		this.modid = modid;
	}

	@Override
	public Set getResourceDomains() {
		HashSet<String> domains = new HashSet<>();
		domains.add(modid);
		//		domains.add("minecraft");
		return domains;
	}

	private String transform(String path) {
		return path.replaceFirst("assets/minecraft", modid);
	}

	@Override
	public boolean hasResourceName(String path) {
		return super.hasResourceName(transform(path));
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		try {
			System.out.println("[" + modid + "] Loading " + path);
			return super.getInputStreamByName(transform(path));
		} catch (IOException e) {
			if (path.equals("pack.mcmeta")) {
				return new ByteArrayInputStream(("{\n" +
					" \"pack\": {\n" +
					" \"description\": \"NOVA mod resource pack\",\n" +
					" \"pack_format\": 1\n" +
					"}\n" +
					"}").getBytes(Charsets.UTF_8));
			} else {
				throw e;
			}
		}
	}
}
