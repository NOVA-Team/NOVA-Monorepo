package nova.wrapper.mc1710.wrapper.block;

import nova.core.block.Block;
import nova.core.nativewrapper.NativeConverter;
import nova.wrapper.mc1710.backward.block.BWBlock;
import nova.wrapper.mc1710.forward.block.BlockWrapperRegistry;

public class BlockConverter implements NativeConverter {

	@Override
	public Class<?> getNovaSide() {
		return Block.class;
	}

	@Override
	public Class<?> getNativeSide() {
		return net.minecraft.block.Block.class;
	}

	@Override
	public Object toNova(Object nativeObj) {
		return new BWBlock((net.minecraft.block.Block) nativeObj);
	}

	@Override
	public Object toNative(Object novaObj) {
		return BlockWrapperRegistry.instance.getMCBlock((Block) novaObj);
	}
}