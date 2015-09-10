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

package nova.core.render.pipeline;

import nova.core.render.model.Model;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A render transmutation transforms a {@link RenderStream}.
 *
 * @author Calclavia
 */
public class RenderStream {

	protected Optional<RenderStream> prev = Optional.empty();

	protected Consumer<Model> consumer = model -> {
	};

	/**
	 * Applies a new RenderStream to the current stream.
	 * This method essentially allow you to switch between render streams in the rendering pipeline.
	 *
	 * @param stream The stream to apply.
	 * @return The new RenderStream
	 */
	public <T extends RenderStream> T apply(T stream) {
		stream.prev = Optional.of(this);
		return stream;
	}

	public Consumer<Model> build() {
		if (prev.isPresent()) {
			return prev.get().build().andThen(consumer);
		}

		return this.consumer;
	}
}
