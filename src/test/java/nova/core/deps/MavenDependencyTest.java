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

package nova.core.deps;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

@Dependency(groupId = "test.group.nova",artifactId = "test1", version = "1.0.0")
public class MavenDependencyTest {

	MavenDependency dep;
	@Before
	public void setUp() {
		dep = new MavenDependency(this.getClass().getAnnotation(Dependency.class));
	}

	@Test
	public void testGetDir() throws Exception {
		assertThat(dep.getDir()).isEqualTo("test/group/nova/test1/1.0.0");
	}

	@Test
	public void testGetDownloadURL() throws Exception {
		assertThat(dep.getDownloadURL()).isNotNull();
	}
}