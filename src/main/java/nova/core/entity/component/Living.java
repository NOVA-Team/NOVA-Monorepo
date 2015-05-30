package nova.core.entity.component;

import nova.core.component.Component;
import nova.core.util.transform.vector.Vector3d;

import java.util.function.Supplier;

/**
 * For entities that are alive.
 * @author Calclavia
 */
public class Living extends Component {

	/**
	 * The displacement to the entity's face.
	 */
	public Supplier<Vector3d> faceDisplacement = () -> Vector3d.zero;
}
