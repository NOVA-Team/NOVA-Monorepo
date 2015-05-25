package nova.wrapper.mc1710.forward.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import nova.core.block.components.DynamicRenderer;
import nova.core.component.Updater;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.entity.EntityWrapper;
import nova.core.util.transform.Transform3d;
import nova.core.util.transform.matrix.MatrixStack;
import nova.core.util.transform.vector.Vector3d;
import nova.wrapper.mc1710.backward.render.BWModel;
import nova.wrapper.mc1710.backward.world.BWWorld;

import java.util.Arrays;

/**
 * A copy of BWEntity that extends EntityFX
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
public class FWEntityFX extends EntityFX implements EntityWrapper {

	public final Entity wrapped;
	public final Transform3d transform;

	public FWEntityFX(World world, EntityFactory factory) {
		super(world, 0, 0, 0);
		this.wrapped = factory.makeEntity(this);
		this.transform = wrapped.transform;
	}

	public FWEntityFX(World world, Entity entity) {
		super(world, 0, 0, 0);
		this.wrapped = entity;
		this.transform = wrapped.transform;
	}

	@Override
	public void renderParticle(Tessellator tess, float x, float y, float z, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
		if (wrapped instanceof DynamicRenderer) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x, y, z).rotate(transform.rotation()).getMatrix();
			((DynamicRenderer) wrapped).renderDynamic(model);
			model.renderWorld(worldObj);
		}
	}

	/**
	 * All methods below here are exactly the same between FWEntity and FWEntityFX.
	 * *****************************************************************************
	 */
	@Override
	protected void entityInit() {
		//MC calls entityInit() before we finish wrapping, so this variable is required to check if wrapped exists.
		if (wrapped != null) {
			wrapped.awake();
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		double deltaTime = 0.05;

		if (wrapped instanceof Updater) {
			((Updater) wrapped).update(deltaTime);
		}

		/**
		 * Update all components in the entity.
		 */
		wrapped.components()
			.stream()
			.filter(component -> component instanceof Updater)
			.forEach(component -> ((Updater) component).update(deltaTime));
	}

	/**
	 * Entity Wrapper Methods
	 * @return
	 */
	@Override
	public nova.core.world.World world() {
		return new BWWorld(worldObj);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(posX, posY, posZ);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		travelToDimension(Arrays
				.stream(DimensionManager.getWorlds())
				.filter(w -> w.getProviderName().equals(world.getID()))
				.findAny()
				.get()
				.provider
				.dimensionId
		);
	}

	@Override
	public void setPosition(Vector3d position) {
		setPosition(position.x, position.y, position.z);
	}

}
