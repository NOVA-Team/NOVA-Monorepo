package nova.wrapper.mc1710.render;

import com.google.common.base.Charsets;
import net.minecraft.client.resources.FileResourcePack;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NovaResourcePack extends FileResourcePack {
	private final String modid;
	private final String[] domains;

	public NovaResourcePack(File p_i1290_1_, String modid, String[] domains) {
		super(p_i1290_1_);
		this.modid = modid;
		this.domains = domains;
	}

	@Override
	public Set getResourceDomains() {
		HashSet<String> domains = new HashSet<>();
		domains.add(modid);
		domains.addAll(Arrays.asList(this.domains));
		return domains;
	}

	private String transform(String path) {
		return path.replaceFirst("assets[/\\\\]minecraft", modid);
	}

	@Override
	public boolean hasResourceName(String path) {
		return super.hasResourceName(transform(path));
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		try {
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
