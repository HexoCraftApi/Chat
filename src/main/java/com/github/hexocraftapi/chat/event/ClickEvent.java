package com.github.hexocraftapi.chat.event;

/*
 * Copyright 2015 hexosse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

public final class ClickEvent
{
	public enum Action
	{
		/**
		 * Open a url at the path given by
		 * {@link ClickEvent#value}
		 */
		OPEN_URL,
		/**
		 * Open a file at the path given by
		 * {@link ClickEvent#value}
		 */
		OPEN_FILE,
		/**
		 * Run the command given by
		 * {@link ClickEvent#value}
		 */
		RUN_COMMAND,
		/**
		 * Inserts the string given by
		 * {@link ClickEvent#value} into the players
		 * text box
		 */
		SUGGEST_COMMAND,
		/**
		 * Change to the page number given by
		 * {@link ClickEvent#value} in a book
		 */
		CHANGE_PAGE
	}

	/**
     * The type of action to perform on click
     */
    private final Action action;

    /**
     * Depends on action
     *
     * @see Action
     */
    private final String value;


	/**
	 * Creates a ClickEvent.
	 */
	public ClickEvent(ClickEvent.Action action, String value) {
		this.action = action;
		this.value = value;
	}

	/**
	 * Creates a ClickEvent.
	 */
	public ClickEvent(ClickEvent event) {
		this.action = event.getAction();
		this.value = event.getValue();
	}

	public ClickEvent.Action getAction() {
		return this.action;
	}

	public String getValue() {
		return this.value;
	}
}
