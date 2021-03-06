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
 * Last generated: 2011-04-13 23:18:40.
 */

package de.fau.cs.osr.ptk.printergen.parser;

import de.fau.cs.osr.ptk.common.ast.*;




/**
 * <h1>CtrlContent</h1>
 */
public class CtrlContent
        extends LeafNode
        implements Instruction
{
	private static final long serialVersionUID = 1L;

	// =========================================================================

	public CtrlContent()
	{
		super();

	}




	// =========================================================================
	// Properties

	private String indent;

	public final String getIndent()
	{
		return this.indent;
	}
	
	public final String setIndent(String indent)
	{
		String old = this.indent;
		this.indent = indent;
		return old;
	}
	private Boolean indented;

	public final Boolean getIndented()
	{
		return this.indented;
	}
	
	public final Boolean setIndented(Boolean indented)
	{
		Boolean old = this.indented;
		this.indented = indented;
		return old;
	}

	@Override
	public final int getPropertyCount()
	{
		return 2;
	}
	
	@Override
	public final AstNodePropertyIterator propertyIterator()
	{
		return new AstNodePropertyIterator()
		{
			@Override
			protected int getPropertyCount()
			{
				return 2;
			}
			
			@Override
			protected String getName(int index)
			{
				switch (index)
				{
					case 0:
						return "indent";
					case 1:
						return "indented";

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
						return CtrlContent.this.getIndent();
					case 1:
						return CtrlContent.this.getIndented();

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
						return CtrlContent.this.setIndent((String) value);
					case 1:
						return CtrlContent.this.setIndented((Boolean) value);

					default:
						throw new IndexOutOfBoundsException();
				}
			}
		};
	}


	// =========================================================================
	// Children




	// =========================================================================


	@Override
	public Instruction.BlockMode getBlockMode()
	{
	  return Instruction.BlockMode.BlockOpen;
	}
	
}
