package nova.core.wrapper.mc.forge.v17.wrapper.identifier;

import net.minecraft.util.ResourceLocation;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.id.NamespacedStringIdentifier;

/**
 * @author soniex2
 */
public class IdentifierConverter implements NativeConverter<NamespacedStringIdentifier, ResourceLocation> {

	@Override
	public Class<NamespacedStringIdentifier> getNovaSide() {
		return NamespacedStringIdentifier.class;
	}

	@Override
	public Class<ResourceLocation> getNativeSide() {
		return ResourceLocation.class;
	}

	@Override
	public NamespacedStringIdentifier toNova(ResourceLocation nativeObj) {
		return new NamespacedStringIdentifier(nativeObj.toString());
	}

	@Override
	public ResourceLocation toNative(NamespacedStringIdentifier novaObj) {
		NamespacedStringIdentifier.NamespacedString ns = novaObj.asNamespacedString();
		return new ResourceLocation(ns.getNamespace(), ns.getName());
	}
}
