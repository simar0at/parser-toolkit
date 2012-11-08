package de.fau.cs.osr.ptk.common.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.RtData;

public class RtDataGsonTypeDeserializer 
		implements 
			JsonDeserializer<RtData>
{	
	private final JsonConverterImpl<? extends AstNode<?>> impl;

	// =========================================================================
	
	public RtDataGsonTypeDeserializer(JsonConverterImpl<? extends AstNode<?>> impl)
	{
		this.impl = impl;
	}
	
	@Override
	public RtData deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		Object result = null;
		JsonObject fields = json.getAsJsonObject();
		JsonArray ja = fields.get("fields").getAsJsonArray();
		ArrayList<Object[]> glue = new ArrayList<Object[]>(ja.size());
		for (JsonElement i : ja)
		{
			JsonArray inner = i.getAsJsonArray();
			if (inner.size() > 0 && inner.get(0).isJsonObject())
				glue.add(new Object[]{impl.deserializeAstNode(inner.get(0), typeOfT, context), context.deserialize(inner.get(1), String.class)});
			else
				glue.add((Object[]) context.deserialize(i, Object[].class));
		}
		int rtDataSize = glue.size();
		int glue2Size = 0;
		for (Object[] o : glue)
			glue2Size = glue2Size + o.length + 1; // separator
		glue2Size--; // there is no end separator;
		Object[] glue2 = new Object[glue2Size];
		int glueI = 0;
		for (Object[] o : glue)
		{
			int i = 0;
			for (; i < o.length; i++)
				glue2[glueI + i] = o[i];
			glueI = glueI + i + 1;
			if (glueI < glue2.length)
				glue2[glueI - 1] = RtData.SEP;
		}
		try {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Constructor constr = ((Class)typeOfT).getConstructor(int.class, Object[].class);
			result = constr.newInstance(rtDataSize, glue2);
		} catch (NoSuchMethodException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		} catch (SecurityException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		} catch (InstantiationException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		} catch (IllegalAccessException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		} catch (IllegalArgumentException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		} catch (InvocationTargetException e) {
			throw new JsonParseException("Can't create " + typeOfT.toString() + "!", e);
		}
		
		return (RtData) result;
	}

}
