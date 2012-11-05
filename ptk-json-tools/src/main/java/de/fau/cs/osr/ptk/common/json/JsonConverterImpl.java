/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fau.cs.osr.ptk.common.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeListImpl;
import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.AstText;
import de.fau.cs.osr.ptk.common.ast.AstLocation;
import de.fau.cs.osr.utils.NameAbbrevService;

public class JsonConverterImpl<T extends AstNode<T>>
{
	private static final int MAX_ENTRIES = 128;
	
	// =========================================================================
	
	private final Map<PropKey, PropSetter> propTypeCache;
	
	private final NameAbbrevService abbrev;
	
	private final boolean saveLocation;
	
	private final Class<? extends T> nodeClass;
	
	private final Class<? extends T> listClass;
	
	private final Class<? extends T> textClass;
	
	// =========================================================================
	
	JsonConverterImpl(
			NameAbbrevService abbrev,
			boolean saveLocation,
			Class<? extends T> nodeClass,
			Class<? extends T> listClass,
			Class<? extends T> textClass)
	{
		this.saveLocation = saveLocation;
		this.nodeClass = nodeClass;
		this.listClass = listClass;
		this.textClass = textClass;
		
		if (!nodeClass.isAssignableFrom(listClass))
			throw new IllegalArgumentException("An instance of listClass must be assignable to nodeClass");
		if (!nodeClass.isAssignableFrom(textClass))
			throw new IllegalArgumentException("An instance of textClass must be assignable to nodeClass");
		
		if (!AstNodeListImpl.class.isAssignableFrom(listClass))
			throw new IllegalArgumentException("An instance of listClass must be assignable to type " + AstNodeListImpl.class.getName());
		if (!AstText.class.isAssignableFrom(textClass))
			throw new IllegalArgumentException("An instance of textClass must be assignable to type " + AstText.class.getName());
		
		if (abbrev == null)
			abbrev = new NameAbbrevService(false);
		this.abbrev = abbrev;
		
		propTypeCache = new LinkedHashMap<PropKey, PropSetter>()
		{
			private static final long serialVersionUID = 1L;
			
			protected boolean removeEldestEntry(
					Map.Entry<PropKey, PropSetter> eldest)
			{
				return size() > MAX_ENTRIES;
			}
		};
	}
	
	// =========================================================================
	
	public JsonElement serializeAstNode(
			T src,
			Type typeOfSrc,
			JsonSerializationContext context)
	{
		JsonObject node = new JsonObject();
		node.add("!type", new JsonPrimitive(abbrev(src.getClass())));
		
		if (saveLocation && src.getNativeLocation() != null)
			node.add("!location", new JsonPrimitive(locationToStr(src)));
		
		if (!src.getAttributes().isEmpty())
		{
			for (Entry<String, Object> e : src.getAttributes().entrySet())
			{
				String name = "@" + e.getKey();
				Object value = e.getValue();
				
				JsonElement jsonValue;
				if (value != null)
				{
					JsonObject attr = new JsonObject();
					attr.add(
							"type",
							new JsonPrimitive(abbrev(value.getClass())));
					attr.add(
							"value",
							context.serialize(value));
					jsonValue = attr;
				}
				else
				{
					jsonValue = null;
				}
				
				node.add(name, jsonValue);
			}
		}
		
		if (src.getPropertyCount() > 0)
		{
			AstNodePropertyIterator i = src.propertyIterator();
			while (i.next())
				node.add(i.getName(), context.serialize(i.getValue()));
		}
		
		if (!src.isEmpty())
		{
			if (src.isList())
			{
				JsonArray list = new JsonArray();
				node.add("!list", list);
				
				for (Object child : src)
					list.add(context.serialize(child));
			}
			else
			{
				int i = 0;
				for (String name : src.getChildNames())
					node.add(name, context.serialize(src.get(i++)));
			}
		}
		
		return node;
	}
	
	public T deserializeAstNode(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		if (json.isJsonPrimitive())
		{
			return deserializeText(json, typeOfT, context);
		}
		else if (json.isJsonArray())
		{
			return deserializeNodeList(json, typeOfT, context);
		}
		else
		{
			JsonObject jo = json.getAsJsonObject();
			
			T n = instantiateNode(jo);
			
			JsonElement location = jo.get("!location");
			if (location != null)
				n.setNativeLocation(strToLocation(location));
			
			if (n.isList())
			{
				JsonElement list = jo.get("!list");
				if (list != null)
				{
					for (JsonElement i : list.getAsJsonArray())
						n.add(checkNodeType(context.<Object> deserialize(i, nodeClass)));
				}
			}
			
			String[] childNames = n.getChildNames();
			
			for (Entry<String, JsonElement> field : jo.entrySet())
			{
				String name = field.getKey();
				if (name.length() >= 1 && name.charAt(0) == '!')
					continue;
				
				if (name.charAt(0) == '@')
				{
					loadAttribute(context, field, n);
				}
				else
				{
					int i = 0;
					for (; i < childNames.length; ++i)
					{
						if (name.equals(childNames[i]))
							break;
					}
					
					if (i == childNames.length)
					{
						loadProperty(context, field, n);
					}
					else
					{
						loadChild(context, field, n, i);
					}
				}
			}
			
			return n;
		}
	}
	
	private String locationToStr(T src)
	{
		return src.getNativeLocation().toString();
	}
	
	private AstLocation strToLocation(JsonElement location)
	{
		String s = location.getAsString();
		if (s == null)
			return null;
		return AstLocation.valueOf(s);
	}
	
	private T instantiateNode(JsonObject jo)
	{
		JsonElement typeElem = jo.get("!type");
		if (typeElem == null)
			throw new JsonParseException("Missing `type' field on AST node");
		
		String typeSuffix = typeElem.getAsString();
		
		Class<?> clazz;
		try
		{
			clazz = resolve(typeSuffix);
		}
		catch (ClassNotFoundException e)
		{
			throw new JsonParseException("Cannot create AST node for name `" + typeSuffix + "'", e);
		}
		
		return instantiateNode(clazz);
	}
	
	private T instantiateNode(Class<?> clazz)
	{
		Exception e = null;
		try
		{
			Constructor<?> ctor = clazz.getDeclaredConstructor();
			ctor.setAccessible(true);
			return checkNodeType(ctor.newInstance());
		}
		catch (InstantiationException e_)
		{
			e = e_;
		}
		catch (IllegalAccessException e_)
		{
			e = e_;
		}
		catch (SecurityException e_)
		{
			e = e_;
		}
		catch (NoSuchMethodException e_)
		{
			e = e_;
		}
		catch (IllegalArgumentException e_)
		{
			e = e_;
		}
		catch (InvocationTargetException e_)
		{
			e = e_;
		}
		
		throw new JsonParseException("Cannot create AST node `" + clazz.getName() + "'", e);
	}
	
	private void loadAttribute(
			JsonDeserializationContext context,
			Entry<String, JsonElement> field,
			T n)
	{
		String name = field.getKey().substring(1);
		
		Exception e;
		try
		{
			Object value = null;
			if (!field.getValue().isJsonNull())
			{
				JsonObject jsonValue = field.getValue().getAsJsonObject();
				
				String type = jsonValue.get("type").getAsString();
				
				value = context.<Object> deserialize(
						jsonValue.get("value"),
						resolve(type));
			}
			
			n.setAttribute(name, value);
			return;
		}
		catch (JsonParseException e_)
		{
			e = e_;
		}
		catch (ClassNotFoundException e_)
		{
			e = e_;
		}
		
		throw new JsonParseException("Failed to deserialize attribute `" + name + "'", e);
	}
	
	private void loadProperty(
			JsonDeserializationContext context,
			Entry<String, JsonElement> field,
			T n)
	{
		PropSetter setter = getPropertyType(
				n.getClass(),
				field.getKey());
		
		setter.set(
				n,
				context.deserialize(
						field.getValue(),
						setter.type));
	}
	
	private void loadChild(
			JsonDeserializationContext context,
			Entry<String, JsonElement> field,
			T n,
			int i)
	{
		T value =
				context.deserialize(
						field.getValue(),
						nodeClass);
		
		n.set(i, value);
	}
	
	// =========================================================================
	
	JsonElement serializeNodeList(
			T src,
			Type typeOfSrc,
			JsonSerializationContext context)
	{
		if (saveLocation && src.getNativeLocation() != null)
			return serializeAstNode(src, typeOfSrc, context);
		
		JsonArray array = new JsonArray();
		for (AstNode<T> c : src)
			array.add(context.serialize(c));
		return array;
	}
	
	T deserializeNodeList(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context)
	{
		if (json.isJsonObject())
			return (T) deserializeAstNode(json, typeOfT, context);
		
		T l = instantiateNode(listClass);
		for (JsonElement i : json.getAsJsonArray())
			l.add(checkNodeType(context.deserialize(i, nodeClass)));
		return l;
	}
	
	// =========================================================================
	
	JsonElement serializeText(
			T src,
			Type typeOfSrc,
			JsonSerializationContext context)
	{
		if (saveLocation && src.getNativeLocation() != null)
			return serializeAstNode(src, typeOfSrc, context);
		
		return new JsonPrimitive(((AstText<T>) src).getContent());
	}
	
	T deserializeText(
			JsonElement json,
			Type typeOfT,
			JsonDeserializationContext context)
	{
		if (json.isJsonObject())
			return (T) deserializeAstNode(json, typeOfT, context);
		
		T t = instantiateNode(textClass);
		((AstText<T>) t).setContent(json.getAsString());
		return (T) t;
	}
	
	// =========================================================================
	
	private String abbrev(Class<?> clazz)
	{
		return this.abbrev.abbrev(clazz)[0];
	}
	
	private Class<?> resolve(String typeSuffix) throws ClassNotFoundException
	{
		return this.abbrev.resolve(typeSuffix);
	}
	
	private T checkNodeType(Object o)
	{
		if (nodeClass.isInstance(o))
		{
			@SuppressWarnings("unchecked")
			T node = (T) o;
			return node;
		}
		else
		{
			throw new ClassCastException("Cannot cast " + o.getClass().getName() + " to " + nodeClass.getName());
		}
	}
	
	// =========================================================================
	
	private PropSetter getPropertyType(
			Class<?> nodeClass,
			String propertyName)
	{
		PropKey key = new PropKey(nodeClass, propertyName);
		PropSetter setter = propTypeCache.get(key);
		if (setter != null)
			return setter;
		
		String head = ("" + propertyName.charAt(0)).toUpperCase();
		String tail = propertyName.substring(1);
		String getterName = "get" + head + tail;
		String setterName = "set" + head + tail;
		
		Method getterMethod;
		try
		{
			try 
			{
				getterMethod = nodeClass.getMethod(getterName);
			} 
			catch (NoSuchMethodException nse) {
				getterName = "is" + head + tail;
				getterMethod = nodeClass.getMethod(getterName);
			}
		}
		catch (NoSuchMethodException e)
		{
			// We also checked if it's the name of a child
			throw new JsonParseException("The field `" + propertyName
					+ "' is not a child nor a property of AST node of type `"
					+ nodeClass.getName() + "'", e);
		}
		catch (SecurityException e)
		{
			throw new JsonParseException("Cannot deduce type of property `"
					+ propertyName + "' in AST node of type `"
					+ nodeClass.getName() + "'. ", e);
		}
		
		Class<?> propType = getterMethod.getReturnType();

		
		Method setterMethod;
		try
		{
			setterMethod = nodeClass.getMethod(setterName, propType);
		}
		catch (Exception e)
		{
			throw new JsonParseException("Cannot set property `"
					+ propertyName + "' in AST node of type `"
					+ nodeClass.getName() + "'. ", e);
		}

		try
		{
			if (propType.isInterface() && !propType.getPackage().getName().equals("java.util"))
			try {
				propType = Class.forName(propType.getName() + "$" + propType.getSimpleName() + "Impl");
			}
			catch (ClassNotFoundException nfe)
			{
				propType = Class.forName(propType.getName() + "Impl");
			}
		}
		catch (Exception e)
		{
			throw new JsonParseException("Cannot set property `"
					+ propertyName + "' in AST node of type `"
					+ nodeClass.getName() + "' which is an interface without known implementation. ", e);
		}
		
		setter = new PropSetter(propType, setterMethod);
		propTypeCache.put(key, setter);
		return setter;
	}
	
	// =========================================================================
	
	private static final class PropKey
	{
		public final Class<?> nodeClazz;
		
		public final String propName;
		
		// =====================================================================
		
		public PropKey(
				Class<?> nodeClazz,
				String propName)
		{
			this.nodeClazz = nodeClazz;
			this.propName = propName;
		}
		
		// =====================================================================
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((nodeClazz == null) ? 0 : nodeClazz.hashCode());
			result = prime * result + ((propName == null) ? 0 : propName.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PropKey other = (PropKey) obj;
			if (nodeClazz == null)
			{
				if (other.nodeClazz != null)
					return false;
			}
			else if (!nodeClazz.equals(other.nodeClazz))
				return false;
			if (propName == null)
			{
				if (other.propName != null)
					return false;
			}
			else if (!propName.equals(other.propName))
				return false;
			return true;
		}
	}
	
	// =========================================================================
	
	private static final class PropSetter
	{
		public final Class<?> type;
		
		public final Method setter;
		
		// =====================================================================
		
		public PropSetter(Class<?> type, Method setter)
		{
			this.type = type;
			this.setter = setter;
		}
		
		// =====================================================================
		
		public void set(Object n, Object value)
		{
			try
			{
				setter.invoke(n, value);
			}
			catch (Exception e)
			{
				throw new JsonParseException("Cannot invoke property setter `"
						+ setter.getName() + "' in AST node of type `"
						+ n.getClass().getName() + "'. ");
			}
		}
	}
}
