package com.github.hexocraftapi.chat.component;

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

import com.github.hexocraftapi.chat.event.ClickEvent;
import com.github.hexocraftapi.chat.event.HoverEvent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseComponent
{
    BaseComponent parent;

    /**
     * The color of this component and any child components (unless overridden)
     */
    private ChatColor color;
    /**
     * Whether this component and any child components (unless overridden) is
     * bold
     */
    private Boolean bold;
    /**
     * Whether this component and any child components (unless overridden) is
     * italic
     */
    private Boolean italic;
    /**
     * Whether this component and any child components (unless overridden) is
     * underlined
     */
    private Boolean underlined;
    /**
     * Whether this component and any child components (unless overridden) is
     * strikethrough
     */
    private Boolean strikethrough;
    /**
     * Whether this component and any child components (unless overridden) is
     * obfuscated
     */
    private Boolean obfuscated;
    /**
     * The text to insert into the chat when this component (and child
     * components) are clicked while pressing the shift key
     */
    private String insertion;
    /**
     * Appended components that inherit this component's formatting and events
     */
    private List<BaseComponent> extra;
    /**
     * The action to preform when this component (and child components) are
     * clicked
     */
    private ClickEvent clickEvent;
    /**
     * The action to preform when this component (and child components) are
     * hovered over
     */
    private HoverEvent hoverEvent;

	/**
	 * Creates a BaseComponent.
	 */
	public BaseComponent() {
	}

	/**
	 * Creates a BaseComponent.
	 */
    BaseComponent(BaseComponent old)
    {
        setColor( old.getColorRaw() );
        setBold( old.isBoldRaw() );
        setItalic( old.isItalicRaw() );
        setUnderlined( old.isUnderlinedRaw() );
        setStrikethrough( old.isStrikethroughRaw() );
        setObfuscated( old.isObfuscatedRaw() );
        setInsertion( old.getInsertion() );
        setClickEvent( old.getClickEvent() );
        setHoverEvent( old.getHoverEvent() );
        if ( old.getExtra() != null )
        {
            for ( BaseComponent component : old.getExtra() )
            {
                addExtra( component.duplicate() );
            }
        }
    }

    /**
     * Clones the BaseComponent and returns the clone.
     *
     * @return The duplicate of this BaseComponent
     */
    public abstract BaseComponent duplicate();

    /**
     * Converts the components to a string that uses the old formatting codes
     * ({@link ChatColor#COLOR_CHAR}
     *
     * @param components the components to convert
     * @return the string in the old format
     */
    public static String toLegacyText(BaseComponent... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseComponent msg : components )
        {
            builder.append( msg.toLegacyText() );
        }
        return builder.toString();
    }

    /**
     * Converts the components into a string without any formatting
     *
     * @param components the components to convert
     * @return the string as plain text
     */
    public static String toPlainText(BaseComponent... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseComponent msg : components )
        {
            builder.append( msg.toPlainText() );
        }
        return builder.toString();
    }

    /**
     * Returns the color of this component. This uses the parent's color if this
     * component doesn't have one. {@link ChatColor#WHITE}
     * is returned if no color is found.
     *
     * @return the color of this component
     */
    public ChatColor getColor()
    {
        if(color!=null)
            return color;

        if(parent!=null)
            return parent.getColor();

        return null;
    }
	public void setColor(ChatColor color) {
		this.color = color;
	}

    /**
     * Returns the color of this component without checking the parents color.
     * May return null
     *
     * @return the color of this component
     */
    public ChatColor getColorRaw()
    {
        return color;
    }

    /**
     * Returns whether this component is bold. This uses the parent's setting if
     * this component hasn't been set. false is returned if none of the parent
     * chain has been set.
     *
     * @return whether the component is bold
     */
    public boolean isBold()
    {
        if ( bold == null )
        {
            return parent != null && parent.isBold();
        }
        return bold;
    }
	public void setBold(Boolean bold) {
		this.bold = bold;
	}

    /**
     * Returns whether this component is bold without checking the parents
     * setting. May return null
     *
     * @return whether the component is bold
     */
    public Boolean isBoldRaw()
    {
        return bold;
    }

    /**
     * Returns whether this component is italic. This uses the parent's setting
     * if this component hasn't been set. false is returned if none of the
     * parent chain has been set.
     *
     * @return whether the component is italic
     */
    public boolean isItalic()
    {
        if ( italic == null )
        {
            return parent != null && parent.isItalic();
        }
        return italic;
    }
	public void setItalic(Boolean italic) {
		this.italic = italic;
	}

    /**
     * Returns whether this component is italic without checking the parents
     * setting. May return null
     *
     * @return whether the component is italic
     */
    public Boolean isItalicRaw()
    {
        return italic;
    }

    /**
     * Returns whether this component is underlined. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is underlined
     */
    public boolean isUnderlined()
    {
        if ( underlined == null )
        {
            return parent != null && parent.isUnderlined();
        }
        return underlined;
    }

    /**
     * Returns whether this component is underlined without checking the parents
     * setting. May return null
     *
     * @return whether the component is underlined
     */
    public Boolean isUnderlinedRaw()
    {
        return underlined;
    }
	public void setUnderlined(Boolean underlined) {
		this.underlined = underlined;
	}

    /**
     * Returns whether this component is strikethrough. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is strikethrough
     */
    public boolean isStrikethrough()
    {
        if ( strikethrough == null )
        {
            return parent != null && parent.isStrikethrough();
        }
        return strikethrough;
    }

    /**
     * Returns whether this component is strikethrough without checking the
     * parents setting. May return null
     *
     * @return whether the component is strikethrough
     */
    public Boolean isStrikethroughRaw()
    {
        return strikethrough;
    }
	public void setStrikethrough(Boolean strikethrough) {
		this.strikethrough = strikethrough;
	}

    /**
     * Returns whether this component is obfuscated. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is obfuscated
     */
    public boolean isObfuscated()
    {
        if ( obfuscated == null )
        {
            return parent != null && parent.isObfuscated();
        }
        return obfuscated;
    }

    /**
     * Returns whether this component is obfuscated without checking the parents
     * setting. May return null
     *
     * @return whether the component is obfuscated
     */
    public Boolean isObfuscatedRaw()
    {
        return obfuscated;
    }
	public void setObfuscated(Boolean obfuscated) {
		this.obfuscated = obfuscated;
	}

	public String getInsertion() {
		return this.insertion;
	}
	public void setInsertion(String insertion) {
		this.insertion = insertion;
	}

	public List<BaseComponent> getExtra() {
		return this.extra;
	}
	public void setExtra(List<BaseComponent> components)
    {
        for ( BaseComponent component : components )
        {
            component.parent = this;
        }
        extra = components;
    }

    /**
     * Appends a text element to the component. The text will inherit this
     * component's formatting
     *
     * @param text the text to append
     */
    public void addExtra(String text)
    {
        addExtra( new TextComponent( text ) );
    }

    /**
     * Appends a component to the component. The text will inherit this
     * component's formatting
     *
     * @param component the component to append
     */
    public void addExtra(BaseComponent component)
    {
        if ( extra == null )
        {
            extra = new ArrayList<BaseComponent>();
        }
        component.parent = this;
        extra.add( component );
    }

	public void setClickEvent(ClickEvent clickEvent) {
		this.clickEvent = clickEvent;
	}
	public ClickEvent getClickEvent() {
		return this.clickEvent;
	}

	public void setHoverEvent(HoverEvent hoverEvent) {
		this.hoverEvent = hoverEvent;
	}
	public HoverEvent getHoverEvent() {
		return this.hoverEvent;
	}

	/**
     * Returns whether the message has any formatting or events applied to it
     *
     * @return Whether any formatting or events are applied
     */
    public boolean hasFormatting()
    {
        return color != null || bold != null
                || italic != null || underlined != null
                || strikethrough != null || obfuscated != null
                || hoverEvent != null || clickEvent != null;
    }

    /**
     * Converts the component into a string without any formatting
     *
     * @return the string as plain text
     */
    public String toPlainText()
    {
        StringBuilder builder = new StringBuilder();
        toPlainText( builder );
        return builder.toString();
    }

    void toPlainText(StringBuilder builder)
    {
        if ( extra != null )
        {
            for ( BaseComponent e : extra )
            {
                e.toPlainText( builder );
            }
        }
    }

    /**
     * Converts the component to a string that uses the old formatting codes
     * ({@link ChatColor#COLOR_CHAR}
     *
     * @return the string in the old format
     */
    public String toLegacyText()
    {
        StringBuilder builder = new StringBuilder();
        toLegacyText( builder );
        return builder.toString();
    }

    void toLegacyText(StringBuilder builder)
    {
        if ( extra != null )
        {
            for ( BaseComponent e : extra )
            {
                e.toLegacyText( builder );
            }
        }
    }
}
