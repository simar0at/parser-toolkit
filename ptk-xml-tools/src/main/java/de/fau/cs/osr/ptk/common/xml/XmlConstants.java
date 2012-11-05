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

package de.fau.cs.osr.ptk.common.xml;

import javax.xml.namespace.QName;

public class XmlConstants
{
	public static final String PTK_NS = "http://sweble.org/doc/site/tooling/parser-toolkit/ptk-xml-tools";
	public static final String PTK = "ptk";
	
	// =========================================================================
	
	static final QName AST_QNAME = new QName(PTK_NS, "ast", PTK);
	
	static final QName LIST_QNAME = new QName(PTK_NS, "l", PTK);
	
	static final QName TEXT_QNAME = new QName(PTK_NS, "t", PTK);
	
	static final QName ATTR_QNAME = new QName(PTK_NS, "a", PTK);
	
	static final QName NULL_QNAME = new QName(PTK_NS, "null", PTK);
	
	// =========================================================================
	
	static final QName TYPE_BYTE = new QName(PTK_NS, "byte", PTK);
	
	static final QName TYPE_SHORT = new QName(PTK_NS, "short", PTK);
	
	static final QName TYPE_INTEGER = new QName(PTK_NS, "int", PTK);
	
	static final QName TYPE_LONG = new QName(PTK_NS, "long", PTK);
	
	static final QName TYPE_FLOAT = new QName(PTK_NS, "flt", PTK);
	
	static final QName TYPE_DOUBLE = new QName(PTK_NS, "dbl", PTK);
	
	static final QName TYPE_BOOLEAN = new QName(PTK_NS, "bool", PTK);
	
	static final QName TYPE_CHARACTER = new QName(PTK_NS, "char", PTK);
	
	static final QName TYPE_VOID = new QName(PTK_NS, "void", PTK);
	
	static final QName TYPE_STRING = new QName(PTK_NS, "str", PTK);
	
	// =========================================================================
	
	static final QName ATTR_NAME_QNAME = new QName(PTK_NS, "name", PTK);
	
	static final QName ATTR_LOCATION_QNAME = new QName(PTK_NS, "location", PTK);
	
	static final QName ATTR_ARRAY_QNAME = new QName(PTK_NS, "array", PTK);
	
	static final QName ATTR_NULL_QNAME = new QName(PTK_NS, "null", PTK);
	
	// =========================================================================
	
	public static String typeNameToTagName(final String name)
	{
		return name.replace('$', '-');
	}
	
	public static String tagNameToClassName(final String x)
	{
		return x.replace('-', '$');
	}
}
