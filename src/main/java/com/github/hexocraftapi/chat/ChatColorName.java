package com.github.hexocraftapi.chat;

/*
 * Copyright 2016 hexosse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <b>Hexosse</b> (<a href="https://github.com/hexosse">on GitHub</a>))
 */
public enum ChatColorName
{
	BLACK(ChatColor.BLACK, "black"),
	DARK_BLUE(ChatColor.DARK_BLUE, "dark_blue"),
	DARK_GREEN(ChatColor.DARK_GREEN, "dark_green"),
	DARK_AQUA(ChatColor.DARK_AQUA, "dark_aqua"),
	DARK_RED(ChatColor.DARK_RED, "dark_red"),
	DARK_PURPLE(ChatColor.DARK_PURPLE, "dark_purple"),
	GOLD(ChatColor.GOLD, "gold"),
	GRAY(ChatColor.GRAY, "gray"),
	DARK_GRAY(ChatColor.DARK_GRAY, "dark_gray"),
	BLUE(ChatColor.BLUE, "blue"),
	GREEN(ChatColor.GREEN, "green"),
	AQUA(ChatColor.AQUA, "aqua"),
	RED(ChatColor.RED, "red"),
	LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "light_purple"),
	YELLOW(ChatColor.YELLOW, "yellow"),
	WHITE(ChatColor.WHITE, "white"),
	MAGIC(ChatColor.MAGIC, "obfuscated"),
	BOLD(ChatColor.BOLD, "bold"),
	STRIKETHROUGH(ChatColor.STRIKETHROUGH, "strikethrough"),
	UNDERLINE(ChatColor.UNDERLINE, "underline"),
	ITALIC(ChatColor.ITALIC, "italic"),
	RESET(ChatColor.RESET, "reset");

	private final ChatColor color;
	private final String name;

	private static final Map<ChatColor, String> BY_COLOR = new HashMap<ChatColor, String>();

	static
	{
		for ( ChatColorName namedColor : values() )
			BY_COLOR.put( namedColor.color, namedColor.name);
	}


	ChatColorName(ChatColor color, String name)
	{
		this.color = color;
		this.name = name;
	}

	public static String getName(ChatColor color)
	{
		return BY_COLOR.get( color );
	}
}
