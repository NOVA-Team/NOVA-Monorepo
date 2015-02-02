package nova.wrapper.mc1710.render;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
	public Set<String> getResourceDomains() {
		HashSet<String> domains = new HashSet<>();
		domains.add(modid);
		return domains;
	}

	private String transform(String path) {
		return path;
	}

	@Override
	public boolean hasResourceName(String path) {
		return super.hasResourceName(transform(path));
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		try {
			System.out.println("[" + modid + "] Loading " + new File(this.resourcePackFile, transform(path)));
			return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, transform(path))));
		} catch (IOException e) {
			if (path.equals("pack.mcmeta")) {
				return new ByteArrayInputStream(("{\n" +
					" \"pack\": {\n" +
					" \"description\": \"NOVA mod resource pack\",\n" +
					" \"pack_format\": 1\n" +
					"}\n" +
					"}").getBytes(Charsets.UTF_8));
			} else {
				if (path.endsWith(".mcmeta")) {
					return new ByteArrayInputStream("{}".getBytes());
				}
				throw e;
			}
		}
	}

	@Override
	public boolean resourceExists(ResourceLocation rl) {
		return new File(resourcePackFile, "assets/" + rl.getResourceDomain() + "/" + rl.getResourcePath()).isFile() || new File(resourcePackFile, rl.getResourceDomain() + "/" + rl.getResourcePath().replace(".mcmeta", "")).isFile();
	}
}
