package com.github.hexocraftapi.chat.Serializer;

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

import com.github.hexocraftapi.chat.component.BaseComponent;
import com.github.hexocraftapi.chat.component.TextComponent;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class TextComponentSerializer extends BaseComponentSerializer implements JsonSerializer<TextComponent>, JsonDeserializer<TextComponent>
{

    @Override
    public TextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        TextComponent component = new TextComponent();
        JsonObject object = json.getAsJsonObject();
        deserialize( object, component, context );
        component.setText( object.get( "text" ).getAsString() );
        return component;
    }

    @Override
    public JsonElement serialize(TextComponent src, Type typeOfSrc, JsonSerializationContext context)
    {
        List<BaseComponent> extra = src.getExtra();
        JsonObject object = new JsonObject();
        if ( src.hasFormatting() || ( extra != null && !extra.isEmpty() ) )
        {
            serialize( object, src, context );
        }
        object.addProperty( "text", src.getText() );
        return object;
    }
}
