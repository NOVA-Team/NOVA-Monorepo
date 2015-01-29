package nova.core.block;

import nova.core.block.components.PositionDependent;
import nova.core.util.Identifiable;
import nova.core.util.NotBuildableException;
import nova.core.util.transform.Vector3i;
import nova.internal.dummy.BlockAccessDummy;

import java.lang.reflect.Constructor;
import java.util.function.UnaryOperator;

public class BlockBuilder<T extends Block> implements Identifiable {
	protected final Class<T> blockClass;
	private final Constructor<T> constructor;
	private final T dummyBlock;
	private final UnaryOperator<T> configurer;
	private final boolean positionDependent;

	public BlockBuilder(Class<T> blockClass, UnaryOperator<T> configurer) throws NotBuildableException {
		this.blockClass = blockClass;
		this.configurer = configurer;
		this.positionDependent = PositionDependent.class.isAssignableFrom(blockClass);
		try {
			this.constructor = blockClass.getConstructor(BlockAccess.class, Vector3i.class);
		} catch (Exception e) {
			throw new NotBuildableException();
		}
		this.dummyBlock = createBlock(new BlockAccessDummy(), new Vector3i(0, 0, 0));
	}

	public BlockBuilder(Class<T> blockClass) throws NotBuildableException {
		this(blockClass, block -> block);
	}

	public T createBlock(BlockAccess access, Vector3i position) throws NotBuildableException {
		if (!positionDependent) {
			return dummyBlock;
		} else {
			try {
				T baseBlock = constructor.newInstance(access, position);
				return configurer.apply(baseBlock);
			} catch (Exception e) {
				throw new NotBuildableException();
			}
		}
	}

	public T getDummyBlock() {
		return dummyBlock;
	}

	public Class<T> getBlockClass() {
		return blockClass;
	}

	@Override
	public String getID() {
		return getDummyBlock().getID();
	}
}
