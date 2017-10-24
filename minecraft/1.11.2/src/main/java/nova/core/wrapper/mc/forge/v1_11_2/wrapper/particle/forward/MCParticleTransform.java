/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.forward;

import net.minecraft.client.particle.Particle;
import nova.core.component.transform.EntityTransform;
import nova.core.util.math.RotationUtil;
import nova.core.util.math.Vector3DUtil;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Wraps Transform3d used in particle
 * @author ExE Boss
 */
public class MCParticleTransform extends EntityTransform {
	public final net.minecraft.client.particle.Particle wrapper;

	public MCParticleTransform(Particle wrapper) {
		this.wrapper = wrapper;
		this.setPivot(Vector3D.ZERO);
	}

	@Override
	public World world() {
		return WorldConverter.instance().toNova(wrapper.world);
	}

	@Override
	public void setWorld(World world) {

	}

	@Override
	public Vector3D position() {
		return new Vector3D(wrapper.posX, wrapper.posY, wrapper.posZ);
	}

	@Override
	public void setPosition(Vector3D position) {
		wrapper.setPosition(position.getX(), position.getY(), position.getZ());
	}

	@Override
	public void setScale(Vector3D scale) {
		// MC Particles only have one scale.
		wrapper.particleScale = (float) ((scale.getX() + scale.getY() + scale.getZ()) / 3);
	}

	@Override
	public Vector3D scale() {
		return new Vector3D(wrapper.particleScale, wrapper.particleScale, wrapper.particleScale);
	}

	@Override
	public Rotation rotation() {
		// TODO: Calculate rotation so that it is always facing the camera.
		return new Rotation(RotationUtil.DEFAULT_ORDER, 0, 0, 0);
	}

	@Override
	public void setRotation(Rotation rotation) {
		// Particles can’t be rotated.
	}
}
