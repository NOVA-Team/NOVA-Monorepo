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

package nova.core.wrapper.mc.forge.v1_8.util;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import nova.core.event.LanguageEvent;
import nova.core.event.bus.EventBus;
import nova.core.language.LanguageManager;
import nova.core.wrapper.mc.forge.v1_8.launcher.ForgeLoadable;
import nova.core.wrapper.mc.forge.v1_8.launcher.NovaMinecraft;
import nova.internal.core.Game;

/**
 * @author Calclavia
 */
public class MCLanguageManager extends LanguageManager implements ForgeLoadable {

	@Override
	public void register(String language, String key, String value) {
		super.register(language, key, value);
	}

	public MCLanguageManager() {
		NovaMinecraft.registerWrapper(this);
	}

	@Override
	public String getCurrentLanguage() {
		return FMLCommonHandler.instance().getCurrentLanguage().replace('_', '-');
	}

	@Override
	public String translate(String key) {
		String value = super.translate(key);
		if (value.equals(key))
			value = StatCollector.translateToLocal(key);
		return value;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void preInit(FMLPreInitializationEvent evt) {
		this.languageMap.forEach((language, map) -> {
			String lang = language.replace('-', '_');
			map.forEach((key, value) -> LanguageRegistry.instance().addStringLocalization(key, lang, value));
		});

		Game.events().on(LanguageEvent.RegisterTranslation.class).withPriority(EventBus.PRIORITY_LOW).bind(this::register);
	}

	@SuppressWarnings("deprecation")
	public void register(LanguageEvent.RegisterTranslation evt) {
		LanguageRegistry.instance().addStringLocalization(evt.key, evt.language.replace('-', '_'), evt.value);
	}
}
