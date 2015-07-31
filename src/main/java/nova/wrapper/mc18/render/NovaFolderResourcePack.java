package nova.wrapper.mc18.render;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NovaFolderResourcePack extends FolderResourcePack {
	private final String modid;
	private final String[] domains;

	public NovaFolderResourcePack(File file, String modid, String[] domains) {
		super(file);
		this.modid = modid;
		this.domains = domains;
	}

	@Override
	public Set<String> getResourceDomains() {
		Set<String> domains = new HashSet<>();
		domains.add(modid);
		domains.addAll(Arrays.asList(this.domains));
		return domains;
	}

	private String transform(String path) {
		return path;
	}

	@Override
	protected InputStream getInputStreamByName(String path) throws IOException {
		try {
			return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, transform(path))));
		} catch (IOException e) {
			if (path.endsWith("sound.json")) {
				JsonObject fakeSoundJSON = new JsonObject();

				JsonObject sound = new JsonObject();
				sound.addProperty("category", "ambient");
				JsonArray sounds = new JsonArray();
				sound.add("sounds", sounds);
				fakeSoundJSON.add("soundName", sound);

				return new ByteArrayInputStream(fakeSoundJSON.toString().getBytes(Charsets.UTF_8));
			} else if (path.equals("pack.mcmeta")) {
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
	public boolean hasResourceName(String path) {
		return super.hasResourceName(transform(path));
	}

	@Override
	public boolean resourceExists(ResourceLocation rl) {
		//Hack Sounds
		if (rl.getResourcePath().endsWith("sounds.json")) {
			return true;
		}

		return new File(resourcePackFile, "assets/" + rl.getResourceDomain() + "/" + rl.getResourcePath()).isFile() || new File(resourcePackFile, rl.getResourceDomain() + "/" + rl.getResourcePath().replace(".mcmeta", "")).isFile();
	}
}
