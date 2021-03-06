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

/* 
 * This file is auto-generated.
 * DO NOT MODIFY MANUALLY!
 * 
 * Generated by AstNodeGenerator.
 * Last generated: 2011-04-13 17:41:42.
 */

package de.fau.cs.osr.ptk.nodegen.parser;

import de.fau.cs.osr.ptk.common.ast.*;




/**
 * <h1>RatsDocGrammar</h1>
 */
public class RatsDocGrammar
        extends InnerNode.InnerNode1
        
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public RatsDocGrammar()
	{
		super(new RatsDoc());

	}
	public RatsDocGrammar(RatsDoc ratsDoc, String moduleName)
	{
		super(ratsDoc);
		setModuleName(moduleName);

	}




	// =========================================================================
	// Properties

	private String moduleName;

	public final String getModuleName()
	{
		return this.moduleName;
	}
	
	public final String setModuleName(String moduleName)
	{
		String old = this.moduleName;
		this.moduleName = moduleName;
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
						return "moduleName";

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
						return RatsDocGrammar.this.getModuleName();

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
						return RatsDocGrammar.this.setModuleName((String) value);

					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}


	// =========================================================================
	// Children

	public final void setRatsDoc(RatsDoc ratsDoc)
	{
		set(0, ratsDoc);
	}
	
	public final RatsDoc getRatsDoc()
	{
		return (RatsDoc) get(0);
	}

	private static final String[] CHILD_NAMES = new String[] {"ratsDoc"};

	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}


	// =========================================================================


}
