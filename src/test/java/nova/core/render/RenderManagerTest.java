/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.render;

import nova.core.render.model.Model;
import nova.core.render.model.ModelProvider;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class RenderManagerTest {

	public RenderManagerTest() {
	}

	private RenderManager renderManager;

	@Before
	public void setUp() {
		renderManager = new RenderManager();
	}

	@Test
	public void testRegisterBlockTexture() {
		BlockTexture texture1 = renderManager.registerTexture(new BlockTexture("nova", "test"));
		BlockTexture texture2 = renderManager.registerTexture(new BlockTexture("nova", "test"));
		assertThat(texture1).is(new Condition<>(tex -> tex == texture2, "%s (same instance)", texture2));
	}

	@Test
	public void testRegisterEntityTexture() {
		EntityTexture texture1 = renderManager.registerTexture(new EntityTexture("nova", "test"));
		EntityTexture texture2 = renderManager.registerTexture(new EntityTexture("nova", "test"));
		assertThat(texture1).is(new Condition<>(tex -> tex == texture2, "%s (same instance)", texture2));
	}

	@Test
	public void testRegisterItemTexture() {
		ItemTexture texture1 = renderManager.registerTexture(new ItemTexture("nova", "test"));
		ItemTexture texture2 = renderManager.registerTexture(new ItemTexture("nova", "test"));
		assertThat(texture1).is(new Condition<>(tex -> tex == texture2, "%s (same instance)", texture2));
	}

	@Test
	public void testGetMissingTexture() {
		assertThat(renderManager.getMissingTexture()).isEqualTo(new Texture("nova", "null"));
	}

	@Test
	public void testRegisterModel() {
		ItemTexture texture1 = renderManager.registerTexture(new ItemTexture("nova", "test"));
		ItemTexture texture2 = renderManager.registerTexture(new ItemTexture("nova", "test"));
		assertThat(texture1).is(new Condition<>(tex -> tex == texture2, "%s (same instance)", texture2));
	}

	public class FakeModelProvider extends ModelProvider {

		public FakeModelProvider(String domain, String name) {
			super(domain, name);
		}

		@Override
		public void load(InputStream stream) {}

		@Override
		public Model getModel() {
			return renderManager.getMissingModel();
		}

		@Override
		public String getType() {
			return "test";
		}
	}
}
