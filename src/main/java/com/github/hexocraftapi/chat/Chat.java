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

import com.github.hexocraftapi.chat.Serializer.ComponentSerializer;
import com.github.hexocraftapi.chat.component.BaseComponent;
import com.github.hexocraftapi.reflection.minecraft.Minecraft;
import com.github.hexocraftapi.reflection.resolver.ConstructorResolver;
import com.github.hexocraftapi.reflection.resolver.FieldResolver;
import com.github.hexocraftapi.reflection.resolver.MethodResolver;
import com.github.hexocraftapi.reflection.resolver.ResolverQuery;
import com.github.hexocraftapi.reflection.resolver.minecraft.NMSClassResolver;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import static com.github.hexocraftapi.chat.Chat.Reflection.sendPacket;

/**
 * @author <b>Hexosse</b> (<a href="https://github.com/hexosse">on GitHub</a>))
 */
public class Chat
{
	public static int NO_WRAP_CHAT_PAGE_WIDTH = 55;

	public static void sendMessage(Player player, BaseComponent message)
	{
		sendMessage(ChatMessageType.CHAT, player, new BaseComponent[]{message});
	}

	public static void sendMessage(Player player, BaseComponent... messages)
	{
		sendMessage(ChatMessageType.CHAT, player, messages);
	}

	public static void sendMessage(ChatMessageType position, Player player, BaseComponent message)
	{
		sendMessage(position, player, new BaseComponent[]{message});
	}

	public static void sendMessage(ChatMessageType position, Player player, BaseComponent[] messages)
	{
		try
		{
			if(Reflection.getPlayerConnection(player) == null) return;

			Object packet = Reflection.PacketChatConstructorResolver
								.resolve(new Class[] {Reflection.IChatBaseComponent, byte.class})
								.newInstance(new Object[] { Reflection.getSerializedMessage(messages), (byte)position.ordinal() });

			sendPacket(packet, player);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Breaks a raw string up into a series of lines. Words are wrapped using
	 * spaces as decimeters and the newline character is respected.
	 *
	 * @param rawString The raw string to break.
	 * @param lineLength The length of a line of text.
	 * @return An array of word-wrapped lines.
	 */
	public static String[] wordWrap(String rawString, int lineLength) {
		// A null string is a single line
		if (rawString == null) {
			return new String[] {""};
		}

		// A string shorter than the lineWidth is a single line
		if (rawString.length() <= lineLength && !rawString.contains("\n")) {
			return new String[] {rawString};
		}

		char[] rawChars = (rawString + ' ').toCharArray(); // add a trailing space to trigger pagination
		StringBuilder word = new StringBuilder();
		StringBuilder line = new StringBuilder();
		List<String> lines = new LinkedList<String>();
		int lineColorChars = 0;

		for (int i = 0; i < rawChars.length; i++) {
			char c = rawChars[i];

			// skip chat color modifiers
			if (c == ChatColor.COLOR_CHAR) {
				word.append(ChatColor.getByChar(rawChars[i + 1]));
				lineColorChars += 2;
				i++; // Eat the next character as we have already processed it
				continue;
			}

			if (c == ' ' || c == '\n') {
				if (line.length() == 0 && word.length() > lineLength) { // special case: extremely long word begins a line
					for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
						lines.add(partialWord);
					}
				} else if (line.length() + 1 + word.length() - lineColorChars == lineLength) { // Line exactly the correct length...newline
					line.append(' ');
					line.append(word);
					lines.add(line.toString());
					line = new StringBuilder();
					lineColorChars = 0;
				} else if (line.length() + 1 + word.length() - lineColorChars > lineLength) { // Line too long...break the line
					/*for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
						lines.add(line.toString());
						line = new StringBuilder(partialWord);
					}*/
					lines.add(line.toString());
					line = new StringBuilder(word);
					lineColorChars = 0;
				} else {
					if (line.length() > 0) {
						line.append(' ');
					}
					line.append(word);
				}
				word = new StringBuilder();

				if (c == '\n') { // Newline forces the line to flush
					lines.add(line.toString());
					line = new StringBuilder();
				}
			} else {
				word.append(c);
			}
		}

		if(line.length() > 0) { // Only add the last line if there is anything to add
			lines.add(line.toString());
		}

		// Iterate over the wrapped lines, applying the last color from one line to the beginning of the next
		if (lines.get(0).length() == 0 || lines.get(0).charAt(0) != ChatColor.COLOR_CHAR) {
			lines.set(0, ChatColor.WHITE + lines.get(0));
		}
		for (int i = 1; i < lines.size(); i++) {
			final String pLine = lines.get(i-1);
			final String subLine = lines.get(i);

			char color = pLine.charAt(pLine.lastIndexOf(ChatColor.COLOR_CHAR) + 1);
			if (subLine.length() == 0 || subLine.charAt(0) != ChatColor.COLOR_CHAR) {
				lines.set(i, ChatColor.getByChar(color) + subLine);
			}
		}

		return lines.toArray(new String[lines.size()]);
	}

	static class Reflection {

		private static final NMSClassResolver NMS_CLASS_RESOLVER = new NMSClassResolver();
		//
		private static final Class<?> PacketPlayOutChat = NMS_CLASS_RESOLVER.resolveSilent("PacketPlayOutChat");
		private static final Class<?> IChatBaseComponent = NMS_CLASS_RESOLVER.resolveSilent("IChatBaseComponent");
		//
		private static final ConstructorResolver PacketChatConstructorResolver = new ConstructorResolver(PacketPlayOutChat);
		//Packets
		private static FieldResolver EntityPlayerFieldResolver;
		private static MethodResolver PlayerConnectionMethodResolver;
		private static MethodResolver ChatSerializerMethodResolver;

		protected static Object getPlayerConnection(Player p) throws ClassNotFoundException
		{
			// Get fields of class EntityPlayer
			if(EntityPlayerFieldResolver == null)
				EntityPlayerFieldResolver = new FieldResolver(NMS_CLASS_RESOLVER.resolve("EntityPlayer"));

			try
			{
				Object handle = Minecraft.getHandle(p);
				return EntityPlayerFieldResolver.resolve("playerConnection").get(handle);
			}
			catch(ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		protected static Object getSerializedMessage(BaseComponent[] messages) throws ClassNotFoundException
		{
			if(ChatSerializerMethodResolver == null)
				ChatSerializerMethodResolver = new MethodResolver(NMS_CLASS_RESOLVER.resolve("ChatSerializer", "IChatBaseComponent$ChatSerializer"));

			try
			{
				return ChatSerializerMethodResolver
							.resolve(new ResolverQuery("a", String.class))
							.invoke(null, new Object[] { ComponentSerializer.toString(messages) });
			}
			catch(ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}

		protected static void sendPacket(Object packet, Player p) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException
		{
			// Get methods of class PlayerConnection
			if(PlayerConnectionMethodResolver == null)
				PlayerConnectionMethodResolver = new MethodResolver(NMS_CLASS_RESOLVER.resolve("PlayerConnection"));

			try
			{
				final Object playerConnection = getPlayerConnection(p);
				PlayerConnectionMethodResolver
							.resolve("sendPacket")
							.invoke(playerConnection, packet);
			}
			catch(ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
