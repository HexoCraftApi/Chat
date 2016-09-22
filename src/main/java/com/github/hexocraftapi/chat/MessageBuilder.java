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

import com.github.hexocraftapi.chat.component.BaseComponent;
import com.github.hexocraftapi.chat.component.TextComponent;
import com.github.hexocraftapi.chat.event.ClickEvent;
import com.github.hexocraftapi.chat.event.HoverEvent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * MessageBuilder simplifies creating basic messages by allowing the use of a
 * chainable builder.
 * </p>
 * <pre>
 * new MessageBuilder("Hello ").color(ChatColor.RED).
 * append("World").color(ChatColor.BLUE). append("!").bold(true).create();
 * </pre>
 * <p>
 * All methods (excluding {@link #append(String)} and {@link #create()} work on
 * the last part appended to the builder, so in the example above "Hello " would
 * be {@link ChatColor#RED} and "World" would be
 * {@link ChatColor#BLUE} but "!" would be bold and
 * {@link ChatColor#BLUE} because append copies the previous
 * part's formatting
 * </p>
 */
public class MessageBuilder
{

    private TextComponent current;
    private final List<BaseComponent> parts = new ArrayList<BaseComponent>();

    /**
     * Creates a MessageBuilder from the other given MessageBuilder to clone
     * it.
     *
     * @param original the original for the new MessageBuilder.
     */
    public MessageBuilder(MessageBuilder original)
    {
        current = new TextComponent( original.current );
        for ( BaseComponent baseComponent : original.parts )
        {
            parts.add( baseComponent.duplicate() );
        }
    }

    /**
     * Creates an empty MessageBuilder.
     */
    public MessageBuilder()
    {
        current = new TextComponent("");
    }

    /**
     * Creates a MessageBuilder with the given text as the first part.
     *
     * @param text the first text element
     */
    public MessageBuilder(String text)
    {
        current = new TextComponent( text );
    }

    /**
     * Appends the text to the builder and makes it the current target for
     * formatting. The text will have all the formatting from the previous part.
     *
     * @param text the text to append
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder append(String text)
    {
        return append( text, FormatRetention.ALL );
    }

    /**
     * Appends the text to the builder and makes it the current target for
     * formatting. You can specify the amount of formatting retained.
     *
     * @param text the text to append
     * @param retention the formatting to retain
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder append(String text, FormatRetention retention)
    {
        parts.add( current );

        current = new TextComponent( current );
        current.setText( text );
        retain( retention );

        return this;
    }

    /**
     * Sets the color of the current part.
     *
     * @param color the new color
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder color(ChatColor color)
    {
        current.setColor( color );
        return this;
    }

    /**
     * Sets whether the current part is bold.
     *
     * @param bold whether this part is bold
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder bold(boolean bold)
    {
        current.setBold( bold );
        return this;
    }

    /**
     * Sets whether the current part is italic.
     *
     * @param italic whether this part is italic
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder italic(boolean italic)
    {
        current.setItalic( italic );
        return this;
    }

    /**
     * Sets whether the current part is underlined.
     *
     * @param underlined whether this part is underlined
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder underlined(boolean underlined)
    {
        current.setUnderlined( underlined );
        return this;
    }

    /**
     * Sets whether the current part is strikethrough.
     *
     * @param strikethrough whether this part is strikethrough
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder strikethrough(boolean strikethrough)
    {
        current.setStrikethrough( strikethrough );
        return this;
    }

    /**
     * Sets whether the current part is obfuscated.
     *
     * @param obfuscated whether this part is obfuscated
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder obfuscated(boolean obfuscated)
    {
        current.setObfuscated( obfuscated );
        return this;
    }

    /**
     * Sets the insertion text for the current part.
     *
     * @param insertion the insertion text
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder insertion(String insertion)
    {
        current.setInsertion( insertion );
        return this;
    }

    /**
     * Sets the click event for the current part.
     *
     * @param clickEvent the click event
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder event(ClickEvent clickEvent)
    {
        current.setClickEvent( clickEvent );
        return this;
    }

    /**
     * Sets the hover event for the current part.
     *
     * @param hoverEvent the hover event
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder event(HoverEvent hoverEvent)
    {
        current.setHoverEvent( hoverEvent );
        return this;
    }

    /**
     * Sets the current part back to normal settings. Only text is kept.
     *
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder reset()
    {
        return retain( FormatRetention.NONE );
    }

    /**
     * Retains only the specified formatting. Text is not modified.
     *
     * @param retention the formatting to retain
     * @return this MessageBuilder for chaining
     */
    public MessageBuilder retain(FormatRetention retention)
    {
        BaseComponent previous = current;

        switch ( retention )
        {
            case NONE:
                current = new TextComponent( current.getText() );
                break;
            case ALL:
                // No changes are required
                break;
            case EVENTS:
                current = new TextComponent( current.getText() );
                current.setInsertion( previous.getInsertion() );
                current.setClickEvent( previous.getClickEvent() );
                current.setHoverEvent( previous.getHoverEvent() );
                break;
            case FORMATTING:
                current.setClickEvent( null );
                current.setHoverEvent( null );
                break;
        }
        return this;
    }

    /**
     * Returns the components needed to display the message created by this
     * builder.
     *
     * @return the created components
     */
    public BaseComponent[] create()
    {
        BaseComponent[] result = parts.toArray( new BaseComponent[ parts.size() + 1 ] );
        result[parts.size()] = current;
        return result;
    }

    public static enum FormatRetention
    {

        /**
         * Specify that we do not want to retain anything from the previous
         * component.
         */
        NONE,
        /**
         * Specify that we want the formatting retained from the previous
         * component.
         */
        FORMATTING,
        /**
         * Specify that we want the events retained from the previous component.
         */
        EVENTS,
        /**
         * Specify that we want to retain everything from the previous
         * component.
         */
        ALL
    }
}
