package nova.core.component;

/**
 * An annotation applied to a component when the component requires another component's presence to function.
 * @author Calclavia
 */
public @interface Require {
	Class<? extends Component> value();
}
