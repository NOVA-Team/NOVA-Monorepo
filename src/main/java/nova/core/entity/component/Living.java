package nova.core.entity.component;

import nova.core.component.Component;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.function.Supplier;

/**
 * For entities that are alive.
 * @author Calclavia
 */
public class Living extends Component {

	/**
	 * The displacement to the entity's face.
	 */
	public Supplier<Vector3D> faceDisplacement = () -> Vector3D.ZERO;
}
