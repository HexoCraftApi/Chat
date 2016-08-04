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
import java.util.HashSet;

public class ComponentSerializer implements JsonDeserializer<BaseComponent>
{

    private final static Gson gson = new GsonBuilder().
            registerTypeAdapter( BaseComponent.class, new ComponentSerializer() ).
            registerTypeAdapter( TextComponent.class, new TextComponentSerializer() ).
            create();

    public final static ThreadLocal<HashSet<BaseComponent>> serializedComponents = new ThreadLocal<HashSet<BaseComponent>>();

    public static BaseComponent[] parse(String json)
    {
        if ( json.startsWith( "[" ) )
        { //Array
            return gson.fromJson( json, BaseComponent[].class );
        }
        return new BaseComponent[]
        {
            gson.fromJson( json, BaseComponent.class )
        };
    }

    public static String toString(BaseComponent component)
    {
        return gson.toJson( component );
    }

    public static String toString(BaseComponent... components)
    {
        return gson.toJson( new TextComponent( components ) );
    }

    @Override
    public BaseComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if ( json.isJsonPrimitive() )
            return new TextComponent( json.getAsString() );

        JsonObject object = json.getAsJsonObject();
        return context.deserialize( json, TextComponent.class );
    }
}
