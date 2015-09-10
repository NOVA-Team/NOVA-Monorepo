/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.internal.core.bootstrap;

import com.google.common.collect.Sets;
import nova.internal.core.Game;
import nova.internal.core.depmodules.CoreBundle;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Optional;
import java.util.Set;

public class DependencyInjectionEntryPoint {

	private State state = State.PREINIT;
	private Optional<Injector> injector = Optional.empty();

	private Set<Class<? extends Bundle>> bundles = Sets.newHashSet();

	public DependencyInjectionEntryPoint() {

		install(CoreBundle.class);
	}

	/**
	 * @return current injector instance.
	 */
	public Optional<Injector> getInjector() {
		return injector;
	}

	/**
	 * @return current state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Installs bundle in core Injector. Works until, finalization later throws
	 * {@link IllegalStateException}.
	 *
	 * @param bundle Bundle
	 */
	public void install(Class<? extends Bundle> bundle) {
		if (state != State.PREINIT) {
			throw new IllegalStateException("This function may only be used before DependencyInjectionEntryPoint initialization.");
		}
		bundles.add(bundle);
	}

	/**
	 * Removes bundle from core Injector. Works until finalization, later throws
	 * {@link IllegalStateException}.
	 *
	 * @param bundle Bundle
	 * @return whether module being uninstalled was installed.
	 */
	public boolean uninstall(Class<? extends Bundle> bundle) {
		if (state != State.PREINIT) {
			throw new IllegalStateException("This function may only be used before DependencyInjectionEntryPoint initialization.");
		}
		return bundles.remove(bundle);
	}

	/**
	 * In this method modules added to DependencyInjectionEntryPoint are being
	 * installed in core injector. Alternating module composition in core
	 * injector after initialization is not possible.
	 *
	 * @return Game instance {@link Game}. Use it for future injections and
	 *         general management.
	 */
	public Game init() {
		if (state != State.PREINIT) {
			throw new IllegalStateException("EntryPoint#postInit() has to be only once.");
		}

		DIEPBundle.bundles = bundles;

		injector = Optional.of(Bootstrap.injector(DIEPBundle.class));
		state = State.POSTINIT;
		return injector.map(injector -> injector.resolve(Dependency.dependency(Game.class))).orElseThrow(IllegalStateException::new);
	}

	private enum State {
		PREINIT, POSTINIT
	}

	private static final class DIEPBundle extends BootstrapperBundle {

		private static Set<Class<? extends Bundle>> bundles;

		@Override
		protected void bootstrap() {
			bundles.stream().forEach(this::install);
		}

	}

}
