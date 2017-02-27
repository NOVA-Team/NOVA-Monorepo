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
 */package nova.core.block.component;

import nova.core.component.Component;
import nova.core.component.SidedComponent;
import nova.core.event.bus.Event;
import nova.core.event.bus.EventBus;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A component that defines a connection with another.  C is the connector type
 * @author Calclavia
 */
@SidedComponent
public class Connectable<C> extends Component {

	public final EventBus<Event> connectEvent = new EventBus<>();

	/**
	 * Can this connectable component connect to another component?
	 */
	public Function<C, Boolean> canConnect = C -> true;

	public Supplier<Set<C>> connections = Collections::emptySet;

	public Connectable<C> setCanConnect(Function<C, Boolean> canConnect) {
		this.canConnect = canConnect;
		return this;
	}

	public Connectable<C> setConnections(Supplier<Set<C>> connections) {
		this.connections = connections;
		return this;
	}
}
