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

package de.fau.cs.osr.ptk.common.ast;

import java.io.IOException;

import de.fau.cs.osr.utils.StringUtils;

public abstract class StringContentNode
        extends
            LeafNode
{
	private static final long serialVersionUID = -3995972757553601033L;
	
	public StringContentNode()
	{
	}
	
	public StringContentNode(String content)
	{
		setContent(content);
	}
	
	// =========================================================================
	
	private String content;
	
	public String getContent()
	{
		return content;
	}
	
	public String setContent(String content)
	{
		String old = this.content;
		this.content = content;
		return old;
	}
	
	@Override
	public int getPropertyCount()
	{
		return 1;
	}
	
	@Override
	public AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 1;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "content";
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object getValue(int index)
			{
				switch (index)
				{
					case 0:
						return StringContentNode.this.getContent();
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
			
			@Override
			protected Object setValue(int index, Object value)
			{
				switch (index)
				{
					case 0:
						return StringContentNode.this.setContent((String) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
	// =========================================================================
	
	@Override
	public void toString(Appendable out) throws IOException
	{
		out.append(getClass().getSimpleName());
		out.append('(');
		if (getContent() != null)
		{
			out.append('"');
			out.append(StringUtils.escJava(getContent()));
			out.append('"');
		}
		else
		{
			out.append("null");
		}
		out.append(')');
	}
}
