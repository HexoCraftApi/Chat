package com.github.hexocraftapi.chat.event;

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

final public class HoverEvent
{
    public enum Action
    {
        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
        SHOW_ITEM,
        SHOW_ENTITY
    }

    /**
     * The type of action to perform on hover
     */
    private final Action action;

    /**
     * The BaseComponent
     */
    private final BaseComponent[] value;


	/**
	 * Creates a HoverEvent.
	 *
	 * @param action {@link Action}
	 * @param value String depending on {@code action}, see {@link BaseComponent}
	 */
	public HoverEvent(HoverEvent.Action action, BaseComponent[] value) {
		this.action = action;
		this.value = value;
	}

	/**
	 * Creates a HoverEvent from an existing event
	 *
	 * @param event {@code HoverEvent} to copy
	 */
	public HoverEvent(HoverEvent event) {
		this.action = event.getAction();
		this.value = event.getValue();
	}

	public HoverEvent.Action getAction() {
        return this.action;
    }

    public BaseComponent[] getValue() {
        return this.value;
    }

}
