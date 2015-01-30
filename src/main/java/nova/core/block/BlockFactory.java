package nova.core.block;

import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3i;

/**
 *
 * @author Stan Hebben
 */
public interface BlockFactory extends Identifiable {
	
	public Block getDummyBlock();
	
	public Block makeBlock(BlockAccess blockAccess, Vector3i position);
}
