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

package de.fau.cs.osr.ptk.common.test;

import de.fau.cs.osr.ptk.common.ast.AstNodePropertyIterator;
import de.fau.cs.osr.ptk.common.ast.InnerNode;
import de.fau.cs.osr.ptk.common.ast.NodeList;

public class TestNodeSection
		extends
			InnerNode.InnerNode2

{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TestNodeSection()
	{
		super(new NodeList(), new NodeList());
		
	}
	
	public TestNodeSection(int level, NodeList title, NodeList body)
	{
		super(title, body);
		setLevel(level);
		
	}
	
	@Override
	public int getNodeType()
	{
		return TestNodes.NT_SECTION;
	}
	
	// =========================================================================
	// Properties
	
	private int level;
	
	public final int getLevel()
	{
		return this.level;
	}
	
	public final int setLevel(int level)
	{
		int old = this.level;
		this.level = level;
		return old;
	}
	
	@Override
	public final int getPropertyCount()
	{
		return 1;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
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
						return "level";
						
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
						return TestNodeSection.this.getLevel();
						
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
						return TestNodeSection.this.setLevel((Integer) value);
						
					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}
	
	// =========================================================================
	// Children
	
	public final void setTitle(NodeList title)
	{
		set(0, title);
	}
	
	public final NodeList getTitle()
	{
		return (NodeList) get(0);
	}
	
	public final void setBody(NodeList body)
	{
		set(1, body);
	}
	
	public final NodeList getBody()
	{
		return (NodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "title", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
}
