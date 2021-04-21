/*
 * Copyright 2021 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2020-2021 Ping Identity Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2021 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.schema;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.schema package, which correspond to messages in the
 * unboundid-ldapsdk-schema.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum SchemaMessages
{
  /**
   * Unable to decode the provided string ''{0}'' as an attribute syntax because the closing parenthesis was not at the end of the string.
   */
  ERR_ATTRSYNTAX_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as an attribute syntax because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute syntax because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_ATTRSYNTAX_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as an attribute syntax because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as an attribute syntax.
   */
  ERR_ATTRSYNTAX_DECODE_EMPTY("An empty string cannot be decoded as an attribute syntax."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute syntax because it included multiple DESC elements.
   */
  ERR_ATTRSYNTAX_DECODE_MULTIPLE_DESC("Unable to decode the provided string ''{0}'' as an attribute syntax because it included multiple DESC elements."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute syntax definition because it does not start with an opening parenthesis.
   */
  ERR_ATTRSYNTAX_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as an attribute syntax definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute syntax because it included an unexpected token ''{1}''.
   */
  ERR_ATTRSYNTAX_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as an attribute syntax because it included an unexpected token ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type because the closing parenthesis was not at the end of the string.
   */
  ERR_ATTRTYPE_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as an attribute type because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_ATTRTYPE_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as an attribute type because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as an attribute type.
   */
  ERR_ATTRTYPE_DECODE_EMPTY("An empty string cannot be decoded as an attribute type."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type because it included an invalid attribute usage of ''{1}''.
   */
  ERR_ATTRTYPE_DECODE_INVALID_USAGE("Unable to decode the provided string ''{0}'' as an attribute type because it included an invalid attribute usage of ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type because it included multiple occurrences of the {1} element.
   */
  ERR_ATTRTYPE_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as an attribute type because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type definition because it does not start with an opening parenthesis.
   */
  ERR_ATTRTYPE_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as an attribute type definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as an attribute type because it included an unexpected token ''{1}''.
   */
  ERR_ATTRTYPE_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as an attribute type because it included an unexpected token ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT content rule because the closing parenthesis was not at the end of the string.
   */
  ERR_DCR_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as a DIT content rule because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT content rule because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_DCR_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as a DIT content rule because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as a DIT content rule.
   */
  ERR_DCR_DECODE_EMPTY("An empty string cannot be decoded as a DIT content rule."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT content rule because it included multiple occurrences of the {1} element.
   */
  ERR_DCR_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as a DIT content rule because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT content rule definition because it does not start with an opening parenthesis.
   */
  ERR_DCR_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as a DIT content rule definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because the closing parenthesis was not at the end of the string.
   */
  ERR_DSR_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as a DIT structure rule because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_DSR_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as a DIT structure rule because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as a DIT structure rule.
   */
  ERR_DSR_DECODE_EMPTY("An empty string cannot be decoded as a DIT structure rule."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because it included multiple occurrences of the {1} element.
   */
  ERR_DSR_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as a DIT structure rule because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because it does not include the required FORM element.
   */
  ERR_DSR_DECODE_NO_FORM("Unable to decode the provided string ''{0}'' as a DIT structure rule because it does not include the required FORM element."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule definition because it does not start with an opening parenthesis.
   */
  ERR_DSR_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as a DIT structure rule definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because the rule ID could not be parsed as an integer.
   */
  ERR_DSR_DECODE_RULE_ID_NOT_INT("Unable to decode the provided string ''{0}'' as a DIT structure rule because the rule ID could not be parsed as an integer."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because one of the superior rule IDs could not be parsed as an integer.
   */
  ERR_DSR_DECODE_SUP_ID_NOT_INT("Unable to decode the provided string ''{0}'' as a DIT structure rule because one of the superior rule IDs could not be parsed as an integer."),



  /**
   * Unable to decode the provided string ''{0}'' as a DIT structure rule because it included an unexpected token ''{1}''.
   */
  ERR_DSR_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as a DIT structure rule because it included an unexpected token ''{1}''."),



  /**
   * The entry contains multiple values for attribute {0} which is defined as single-valued in the schema.
   */
  ERR_ENTRY_ATTR_HAS_MULTIPLE_VALUES("The entry contains multiple values for attribute {0} which is defined as single-valued in the schema."),



  /**
   * The entry contains value ''{0}'' for attribute {1} which violates the constraints of the associated attribute syntax:  {2}
   */
  ERR_ENTRY_ATTR_INVALID_SYNTAX("The entry contains value ''{0}'' for attribute {1} which violates the constraints of the associated attribute syntax:  {2}"),



  /**
   * The entry contains attribute {0} which is not allowed by its object classes and/or DIT content rule.
   */
  ERR_ENTRY_ATTR_NOT_ALLOWED("The entry contains attribute {0} which is not allowed by its object classes and/or DIT content rule."),



  /**
   * The entry contains value {0,number,0} for attribute {1} that is larger than the maximum allowed value of {2,number,0} specified by the X-MAX-INT-VALUE extension.
   */
  ERR_ENTRY_ATTR_VALUE_INT_TOO_LARGE("The entry contains value {0,number,0} for attribute {1} that is larger than the maximum allowed value of {2,number,0} specified by the X-MAX-INT-VALUE extension."),



  /**
   * The entry contains value {0,number,0} for attribute {1} that is smaller than the minimum allowed value of {2,number,0} specified by the X-MIN-INT-VALUE extension.
   */
  ERR_ENTRY_ATTR_VALUE_INT_TOO_SMALL("The entry contains value {0,number,0} for attribute {1} that is smaller than the minimum allowed value of {2,number,0} specified by the X-MIN-INT-VALUE extension."),



  /**
   * The entry contains value ''{0}'' for attribute {1} that is longer than the maximum length of {2,number,0} characters specified by the X-MAX-VALUE-LENGTH attribute type extension.
   */
  ERR_ENTRY_ATTR_VALUE_LONGER_THAN_MAX_LENGTH("The entry contains value ''{0}'' for attribute {1} that is longer than the maximum length of {2,number,0} characters specified by the X-MAX-VALUE-LENGTH attribute type extension."),



  /**
   * The entry contains value ''{0}'' for attribute {1} that is not in the set of allowed values defined in the X-ALLOWED-VALUE attribute type extension.
   */
  ERR_ENTRY_ATTR_VALUE_NOT_ALLOWED("The entry contains value ''{0}'' for attribute {1} that is not in the set of allowed values defined in the X-ALLOWED-VALUE attribute type extension."),



  /**
   * The entry contains value ''{0}'' for attribute {1} that does not match a regular expression defined in the X-VALUE-REGEX attribute type extension.
   */
  ERR_ENTRY_ATTR_VALUE_NOT_ALLOWED_BY_REGEX("The entry contains value ''{0}'' for attribute {1} that does not match a regular expression defined in the X-VALUE-REGEX attribute type extension."),



  /**
   * The entry contains value ''{0}'' for attribute {1} that cannot be parsed as an integer, but that is expected to be an integer because the attribute type definition uses the {2} extension.
   */
  ERR_ENTRY_ATTR_VALUE_NOT_INT("The entry contains value ''{0}'' for attribute {1} that cannot be parsed as an integer, but that is expected to be an integer because the attribute type definition uses the {2} extension."),



  /**
   * The entry contains value ''{0}'' for attribute {1} that is shorter than the minimum length of {2,number,0} characters specified by the X-MIN-VALUE-LENGTH attribute type extension.
   */
  ERR_ENTRY_ATTR_VALUE_SHORTER_THAN_MIN_LENGTH("The entry contains value ''{0}'' for attribute {1} that is shorter than the minimum length of {2,number,0} characters specified by the X-MIN-VALUE-LENGTH attribute type extension."),



  /**
   * The entry contains auxiliary object class {0} which is not allowed by the associated DIT content rule.
   */
  ERR_ENTRY_AUX_CLASS_NOT_ALLOWED("The entry contains auxiliary object class {0} which is not allowed by the associated DIT content rule."),



  /**
   * The entry contains abstract object class {0} that is not subclassed by any of the structural or auxiliary object classes included in the entry.
   */
  ERR_ENTRY_INVALID_ABSTRACT_CLASS("The entry contains abstract object class {0} that is not subclassed by any of the structural or auxiliary object classes included in the entry."),



  /**
   * The entry contains a malformed DN:  {0}
   */
  ERR_ENTRY_MALFORMED_DN("The entry contains a malformed DN:  {0}"),



  /**
   * The entry''s RDN contains value ''{0}'' for attribute ''{1}'' that is not present in the set of entry attributes.
   */
  ERR_ENTRY_MISSING_RDN_VALUE("The entry''s RDN contains value ''{0}'' for attribute ''{1}'' that is not present in the set of entry attributes."),



  /**
   * The entry is missing required attribute {0}.
   */
  ERR_ENTRY_MISSING_REQUIRED_ATTR("The entry is missing required attribute {0}."),



  /**
   * The entry is missing object class {0} which is the superior class for the {1} object class.  Many servers will allow this when adding or importing data, but it may cause problems in other servers.
   */
  ERR_ENTRY_MISSING_SUP_OC("The entry is missing object class {0} which is the superior class for the {1} object class.  Many servers will allow this when adding or importing data, but it may cause problems in other servers."),



  /**
   * The entry has more than one structural object class ({0}).
   */
  ERR_ENTRY_MULTIPLE_STRUCTURAL_CLASSES("The entry has more than one structural object class ({0})."),



  /**
   * The entry does not have any object classes.
   */
  ERR_ENTRY_NO_OCS("The entry does not have any object classes."),



  /**
   * The entry does not have a structural object class.
   */
  ERR_ENTRY_NO_STRUCTURAL_CLASS("The entry does not have a structural object class."),



  /**
   * The entry''s RDN contains attribute {0} which is not allowed by the associated name form.
   */
  ERR_ENTRY_RDN_ATTR_NOT_ALLOWED_BY_NF("The entry''s RDN contains attribute {0} which is not allowed by the associated name form."),



  /**
   * The entry''s RDN contains attribute {0} which is not allowed to be included in the entry.
   */
  ERR_ENTRY_RDN_ATTR_NOT_ALLOWED_IN_ENTRY("The entry''s RDN contains attribute {0} which is not allowed to be included in the entry."),



  /**
   * The entry''s RDN contains attribute {0} which is not defined in the schema.
   */
  ERR_ENTRY_RDN_ATTR_NOT_DEFINED("The entry''s RDN contains attribute {0} which is not defined in the schema."),



  /**
   * The entry''s RDN does not contain attribute {0} which is required by the associated name form.
   */
  ERR_ENTRY_RDN_MISSING_REQUIRED_ATTR("The entry''s RDN does not contain attribute {0} which is required by the associated name form."),



  /**
   * The entry contains {0,number,0} values for attribute {1}, which is smaller than the minimum of {2,number,0} specified by the X-MIN-VALUE-COUNT attribute type extension.
   */
  ERR_ENTRY_TOO_FEW_VALUES("The entry contains {0,number,0} values for attribute {1}, which is smaller than the minimum of {2,number,0} specified by the X-MIN-VALUE-COUNT attribute type extension."),



  /**
   * The entry contains {0,number,0} values for attribute {1}, which is larger than the maximum of {2,number,0} specified by the X-MAX-VALUE-COUNT attribute type extension.
   */
  ERR_ENTRY_TOO_MANY_VALUES("The entry contains {0,number,0} values for attribute {1}, which is larger than the maximum of {2,number,0} specified by the X-MAX-VALUE-COUNT attribute type extension."),



  /**
   * The entry contains attribute {0} which is not defined in the schema.
   */
  ERR_ENTRY_UNDEFINED_ATTR("The entry contains attribute {0} which is not defined in the schema."),



  /**
   * The entry contains object class {0} which is not defined in the schema.
   */
  ERR_ENTRY_UNDEFINED_OC("The entry contains object class {0} which is not defined in the schema."),



  /**
   * Object class {0} references superior class {1} which is not defined in the schema.
   */
  ERR_ENTRY_UNDEFINED_SUP_OC("Object class {0} references superior class {1} which is not defined in the schema."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use because the closing parenthesis was not at the end of the string.
   */
  ERR_MRU_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as a matching rule use because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_MRU_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as a matching rule use because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as a matching rule use.
   */
  ERR_MRU_DECODE_EMPTY("An empty string cannot be decoded as a matching rule use."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use because it included multiple occurrences of the {1} element.
   */
  ERR_MRU_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as a matching rule use because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use because it did not include any applicable attribute types.
   */
  ERR_MRU_DECODE_NO_APPLIES("Unable to decode the provided string ''{0}'' as a matching rule use because it did not include any applicable attribute types."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use definition because it does not start with an opening parenthesis.
   */
  ERR_MRU_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as a matching rule use definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule use because it included an unexpected token ''{1}''.
   */
  ERR_MRU_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as a matching rule use because it included an unexpected token ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule because the closing parenthesis was not at the end of the string.
   */
  ERR_MR_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as a matching rule because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_MR_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as a matching rule because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as a matching rule.
   */
  ERR_MR_DECODE_EMPTY("An empty string cannot be decoded as a matching rule."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule because it included multiple occurrences of the {1} element.
   */
  ERR_MR_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as a matching rule because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule definition because it does not start with an opening parenthesis.
   */
  ERR_MR_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as a matching rule definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule because it did not include a syntax OID.
   */
  ERR_MR_DECODE_NO_SYNTAX("Unable to decode the provided string ''{0}'' as a matching rule because it did not include a syntax OID."),



  /**
   * Unable to decode the provided string ''{0}'' as a matching rule because it included an unexpected token ''{1}''.
   */
  ERR_MR_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as a matching rule because it included an unexpected token ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because the closing parenthesis was not at the end of the string.
   */
  ERR_NF_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as a name form because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_NF_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as a name form because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as a name form.
   */
  ERR_NF_DECODE_EMPTY("An empty string cannot be decoded as a name form."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because it included multiple occurrences of the {1} element.
   */
  ERR_NF_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as a name form because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because it did not include any required attributes.
   */
  ERR_NF_DECODE_NO_MUST("Unable to decode the provided string ''{0}'' as a name form because it did not include any required attributes."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because it did not include a structural object class.
   */
  ERR_NF_DECODE_NO_OC("Unable to decode the provided string ''{0}'' as a name form because it did not include a structural object class."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form definition because it does not start with an opening parenthesis.
   */
  ERR_NF_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as a name form definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as a name form because it included an unexpected token ''{1}''.
   */
  ERR_NF_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as a name form because it included an unexpected token ''{1}''."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class because the closing parenthesis was not at the end of the string.
   */
  ERR_OC_DECODE_CLOSE_NOT_AT_END("Unable to decode the provided string ''{0}'' as an object class because the closing parenthesis was not at the end of the string."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class because it included multiple occurrences of the ''{1}'' extension.
   */
  ERR_OC_DECODE_DUP_EXT("Unable to decode the provided string ''{0}'' as an object class because it included multiple occurrences of the ''{1}'' extension."),



  /**
   * An empty string cannot be decoded as an object class.
   */
  ERR_OC_DECODE_EMPTY("An empty string cannot be decoded as an object class."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class because it included multiple occurrences of the {1} element.
   */
  ERR_OC_DECODE_MULTIPLE_ELEMENTS("Unable to decode the provided string ''{0}'' as an object class because it included multiple occurrences of the {1} element."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class because it included multiple object class type elements.
   */
  ERR_OC_DECODE_MULTIPLE_OC_TYPES("Unable to decode the provided string ''{0}'' as an object class because it included multiple object class type elements."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class definition because it does not start with an opening parenthesis.
   */
  ERR_OC_DECODE_NO_OPENING_PAREN("Unable to decode the provided string ''{0}'' as an object class definition because it does not start with an opening parenthesis."),



  /**
   * Unable to decode the provided string ''{0}'' as an object class because it included an unexpected token ''{1}''.
   */
  ERR_OC_DECODE_UNEXPECTED_TOKEN("Unable to decode the provided string ''{0}'' as an object class because it included an unexpected token ''{1}''."),



  /**
   * An error occurred while attempting to load or parse a default set of standard schema elements:  {0}
   */
  ERR_SCHEMA_CANNOT_LOAD_DEFAULT_DEFINITIONS("An error occurred while attempting to load or parse a default set of standard schema elements:  {0}"),



  /**
   * Unable to parse string ''{0}'' as a schema element because an empty string was founded where a schema element name or OID was expected.
   */
  ERR_SCHEMA_ELEM_EMPTY_OID("Unable to parse string ''{0}'' as a schema element because an empty string was founded where a schema element name or OID was expected."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the value of the ''{1}'' component is an empty OID list.
   */
  ERR_SCHEMA_ELEM_EMPTY_OID_LIST("Unable to parse string ''{0}'' as a schema element because the value of the ''{1}'' component is an empty OID list."),



  /**
   * Unable to parse string ''{0}'' as a schema element because it contained an empty quoted string as the value for the ''{1}'' component.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_ELEM_EMPTY_QUOTES("Unable to parse string ''{0}'' as a schema element because it contained an empty quoted string as the value for the ''{1}'' component.  Quoted strings in schema elements must not be empty."),



  /**
   * Unable to parse string ''{0}'' as a schema element because component ''{1}'' contained an empty quoted-string list.
   */
  ERR_SCHEMA_ELEM_EMPTY_STRING_LIST("Unable to parse string ''{0}'' as a schema element because component ''{1}'' contained an empty quoted-string list."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the string ended with a backslash while still parsing the value of the ''{1}'' component.
   */
  ERR_SCHEMA_ELEM_ENDS_WITH_BACKSLASH("Unable to parse string ''{0}'' as a schema element because the string ended with a backslash while still parsing the value of the ''{1}'' component."),



  /**
   * Unable to parse string ''{0}'' as a schema element because either a single quote or a closing parenthesis was expected at position {1,number,0} while parsing the value of the ''{2}'' component.
   */
  ERR_SCHEMA_ELEM_EXPECTED_QUOTE_OR_PAREN("Unable to parse string ''{0}'' as a schema element because either a single quote or a closing parenthesis was expected at position {1,number,0} while parsing the value of the ''{2}'' component."),



  /**
   * Unable to parse string ''{0}'' as a schema element because a single quote was expected at position {1,number,0} while parsing the value of component ''{2}''.
   */
  ERR_SCHEMA_ELEM_EXPECTED_SINGLE_QUOTE("Unable to parse string ''{0}'' as a schema element because a single quote was expected at position {1,number,0} while parsing the value of component ''{2}''."),



  /**
   * Unable to parse string ''{0}'' as a schema element because it contained an invalid hex character ''{1}'' at position {2,number,0} as part of the value of the ''{3}'' component.
   */
  ERR_SCHEMA_ELEM_INVALID_HEX_CHAR("Unable to parse string ''{0}'' as a schema element because it contained an invalid hex character ''{1}'' at position {2,number,0} as part of the value of the ''{3}'' component."),



  /**
   * Unable to parse string ''{0}'' as a schema element because two hex digits were expected after a backslash in the value for the ''{1}'' component,but only one hex digit was found.
   */
  ERR_SCHEMA_ELEM_MISSING_HEX_CHAR("Unable to parse string ''{0}'' as a schema element because two hex digits were expected after a backslash in the value for the ''{1}'' component,but only one hex digit was found."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after a closing single quote following the value for component ''{1}''.
   */
  ERR_SCHEMA_ELEM_NO_CLOSING_PAREN("Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after a closing single quote following the value for component ''{1}''."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the end of the string was reached before finding a space to mark the end of an element name or OID.
   */
  ERR_SCHEMA_ELEM_NO_SPACE_AFTER_OID("Unable to parse string ''{0}'' as a schema element because the end of the string was reached before finding a space to mark the end of an element name or OID."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after the OID list for component ''{1}''.
   */
  ERR_SCHEMA_ELEM_NO_SPACE_AFTER_OID_LIST("Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after the OID list for component ''{1}''."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after a closing single quote following the value for the ''{1}'' component.
   */
  ERR_SCHEMA_ELEM_NO_SPACE_AFTER_QUOTE("Unable to parse string ''{0}'' as a schema element because the end of the string was reached without finding an expected space after a closing single quote following the value for the ''{1}'' component."),



  /**
   * Unable to parse string ''{0}'' as a schema element because the end of the string was reached while skipping over spaces and not finding a closing parenthesis.
   */
  ERR_SCHEMA_ELEM_SKIP_SPACES_NO_CLOSE_PAREN("Unable to parse string ''{0}'' as a schema element because the end of the string was reached while skipping over spaces and not finding a closing parenthesis."),



  /**
   * Unable to parse string ''{0}'' as a schema element because an unexpected character was found at position {1,number,0} while attempting to read an element name or OID.
   */
  ERR_SCHEMA_ELEM_UNEXPECTED_CHAR_IN_OID("Unable to parse string ''{0}'' as a schema element because an unexpected character was found at position {1,number,0} while attempting to read an element name or OID."),



  /**
   * Unable to parse string ''{0}'' as a schema element because it contained an unexpected character at position {1,number,0} in the OID list for the ''{2}'' component.
   */
  ERR_SCHEMA_ELEM_UNEXPECTED_CHAR_IN_OID_LIST("Unable to parse string ''{0}'' as a schema element because it contained an unexpected character at position {1,number,0} in the OID list for the ''{2}'' component."),



  /**
   * Unable to parse {0} value {1} as an attribute syntax definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_AS("Unable to parse {0} value {1} as an attribute syntax definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as an attribute type definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_AT("Unable to parse {0} value {1} as an attribute type definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as a DIT content rule definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_DCR("Unable to parse {0} value {1} as a DIT content rule definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as a DIT structure rule definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_DSR("Unable to parse {0} value {1} as a DIT structure rule definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as a matching rule definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_MR("Unable to parse {0} value {1} as a matching rule definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as a matching rule use definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_MRU("Unable to parse {0} value {1} as a matching rule use definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as a name form definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_NF("Unable to parse {0} value {1} as a name form definition:  {2}"),



  /**
   * Unable to parse {0} value {1} as an object class definition:  {2}
   */
  ERR_SCHEMA_UNPARSABLE_OC("Unable to parse {0} value {1} as an object class definition:  {2}"),



  /**
   * Attribute type definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_AT_ALREADY_DEFINED_WITH_NAME("Attribute type definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * Attribute type definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_AT_ALREADY_DEFINED_WITH_OID("Attribute type definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Attribute type definition {0} read from file ''{1}'' is defined as a collective attribute.  Although this is legal, some servers may not support collective attributes.
   */
  ERR_SCHEMA_VALIDATOR_AT_COLLECTIVE("Attribute type definition {0} read from file ''{1}'' is defined as a collective attribute.  Although this is legal, some servers may not support collective attributes."),



  /**
   * Attribute type definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_AT_EMPTY_DESCRIPTION("Attribute type definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Attribute type definition {0} read from file ''{1}'' has invalid name ''{2}''.  Attribute type names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_AT_INVALID_NAME("Attribute type definition {0} read from file ''{1}'' has invalid name ''{2}''.  Attribute type names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Attribute type definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_AT_INVALID_OID("Attribute type definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Attribute type definition {0} read from file ''{1}'' has invalid syntax OID ''{2}'':  {3}
   */
  ERR_SCHEMA_VALIDATOR_AT_INVALID_SYNTAX_OID("Attribute type definition {0} read from file ''{1}'' has invalid syntax OID ''{2}'':  {3}"),



  /**
   * Schema file ''{0}'' contains attribute type {1} but the schema validator configuration does not permit attribute types to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_AT_NOT_ALLOWED("Schema file ''{0}'' contains attribute type {1} but the schema validator configuration does not permit attribute types to be defined in schema files."),



  /**
   * Attribute type definition {0} read from file ''{1}'' does not specify an equality matching rule and does not have a superior type from which the equality matching rule may be inherited.  Although this is technically legal and the server should fall back to byte-for-byte matching for values of that attribute type (although some servers may assume a different default matching rule on a per-syntax basis), it is often desirable to explicitly specify the equality matching rule.
   */
  ERR_SCHEMA_VALIDATOR_AT_NO_EQ_MR("Attribute type definition {0} read from file ''{1}'' does not specify an equality matching rule and does not have a superior type from which the equality matching rule may be inherited.  Although this is technically legal and the server should fall back to byte-for-byte matching for values of that attribute type (although some servers may assume a different default matching rule on a per-syntax basis), it is often desirable to explicitly specify the equality matching rule."),



  /**
   * Attribute type definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_AT_NO_NAME("Attribute type definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * Attribute type definition {0} read from file ''{1}'' does not specify a syntax OID and does not have a superior type from which the syntax may be inherited.
   */
  ERR_SCHEMA_VALIDATOR_AT_NO_SYNTAX("Attribute type definition {0} read from file ''{1}'' does not specify a syntax OID and does not have a superior type from which the syntax may be inherited."),



  /**
   * Attribute type definition {0} read from file ''{1}'' is declared with the NO-USER-MODIFICATION constraint, but is not declared with an operational usage.  The NO-USER-MODIFICATION constraint may only be used with operational attribute types.
   */
  ERR_SCHEMA_VALIDATOR_AT_NO_USER_MOD_WITHOUT_OP_USAGE("Attribute type definition {0} read from file ''{1}'' is declared with the NO-USER-MODIFICATION constraint, but is not declared with an operational usage.  The NO-USER-MODIFICATION constraint may only be used with operational attribute types."),



  /**
   * Attribute type definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_AT_OBSOLETE("Attribute type definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * Attribute type definition {0} read from file ''{1}'' references an unknown equality matching rule with name or OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_AT_UNDEFINED_EQ_MR("Attribute type definition {0} read from file ''{1}'' references an unknown equality matching rule with name or OID ''{2}''."),



  /**
   * Attribute type definition {0} read from file ''{1}'' references an unknown ordering matching rule with name or OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_AT_UNDEFINED_ORD_MR("Attribute type definition {0} read from file ''{1}'' references an unknown ordering matching rule with name or OID ''{2}''."),



  /**
   * Attribute type definition {0} read from file ''{1}'' references an unknown substring matching rule with name or OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_AT_UNDEFINED_SUB_MR("Attribute type definition {0} read from file ''{1}'' references an unknown substring matching rule with name or OID ''{2}''."),



  /**
   * Attribute type definition {0} read from file ''{1}'' references an unknown superior type with name or OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_AT_UNDEFINED_SUPERIOR("Attribute type definition {0} read from file ''{1}'' references an unknown superior type with name or OID ''{2}''."),



  /**
   * Attribute type definition {0} read from file ''{1}'' references an unknown attribute syntax with OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_AT_UNDEFINED_SYNTAX("Attribute type definition {0} read from file ''{1}'' references an unknown attribute syntax with OID ''{2}''."),



  /**
   * Unable to parse attribute type definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_AT("Unable to parse attribute type definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse DIT content rule definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_DCR("Unable to parse DIT content rule definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse DIT structure rule definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_DSR("Unable to parse DIT structure rule definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse matching rule definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_MR("Unable to parse matching rule definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse matching rule use definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_MRU("Unable to parse matching rule use definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse name form definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_NF("Unable to parse name form definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse object class definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_OC("Unable to parse object class definition {0} read from file ''{1}'':  {2}"),



  /**
   * Unable to parse attribute syntax definition {0} read from file ''{1}'':  {2}
   */
  ERR_SCHEMA_VALIDATOR_CANNOT_PARSE_SYNTAX("Unable to parse attribute syntax definition {0} read from file ''{1}'':  {2}"),



  /**
   * DIT content rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_DCR_ALREADY_DEFINED_WITH_NAME("DIT content rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_DCR_ALREADY_DEFINED_WITH_OID("DIT content rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of optional and prohibited attributes.
   */
  ERR_SCHEMA_VALIDATOR_DCR_ATTR_OPT_AND_NOT("DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of optional and prohibited attributes."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of required and prohibited attributes.
   */
  ERR_SCHEMA_VALIDATOR_DCR_ATTR_REQ_AND_NOT("DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of required and prohibited attributes."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of required and optional attributes.
   */
  ERR_SCHEMA_VALIDATOR_DCR_ATTR_REQ_AND_OPT("DIT content rule definition {0} read from file ''{1}'' includes attribute {2} in both the sets of required and optional attributes."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references auxiliary class {2} that is not defined as auxiliary.
   */
  ERR_SCHEMA_VALIDATOR_DCR_AUX_CLASS_NOT_AUX("DIT content rule definition {0} read from file ''{1}'' references auxiliary class {2} that is not defined as auxiliary."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_DCR_EMPTY_DESCRIPTION("DIT content rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  DIT content rule names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_DCR_INVALID_NAME("DIT content rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  DIT content rule names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_DCR_INVALID_OID("DIT content rule definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Schema file ''{0}'' contains DIT content rule {1} but the schema validator configuration does not permit DIT content rules to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_DCR_NOT_ALLOWED("Schema file ''{0}'' contains DIT content rule {1} but the schema validator configuration does not permit DIT content rules to be defined in schema files."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_DCR_NO_NAME("DIT content rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_DCR_OBSOLETE("DIT content rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references prohibited attribute type ''{2}'' that is required by allowed auxiliary object class ''{3}''.
   */
  ERR_SCHEMA_VALIDATOR_DCR_PROHIBITED_ATTR_REQUIRED_BY_AUX("DIT content rule definition {0} read from file ''{1}'' references prohibited attribute type ''{2}'' that is required by allowed auxiliary object class ''{3}''."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references prohibited attribute type ''{2}'' that is required by the structural object class.
   */
  ERR_SCHEMA_VALIDATOR_DCR_PROHIBITED_ATTR_REQUIRED_BY_STRUCT("DIT content rule definition {0} read from file ''{1}'' references prohibited attribute type ''{2}'' that is required by the structural object class."),



  /**
   * DIT content rule {0} read from file ''{1}'' has OID ''{2}'' that references object class {3}, but that is not a structural object class.
   */
  ERR_SCHEMA_VALIDATOR_DCR_STRUCTURAL_CLASS_NOT_STRUCTURAL("DIT content rule {0} read from file ''{1}'' has OID ''{2}'' that references object class {3}, but that is not a structural object class."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references unknown auxiliary object class ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_DCR_UNDEFINED_AUX_CLASS("DIT content rule definition {0} read from file ''{1}'' references unknown auxiliary object class ''{2}''."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references unknown optional attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_DCR_UNDEFINED_OPTIONAL_ATTR("DIT content rule definition {0} read from file ''{1}'' references unknown optional attribute type ''{2}''."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references unknown prohibited attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_DCR_UNDEFINED_PROHIBITED_ATTR("DIT content rule definition {0} read from file ''{1}'' references unknown prohibited attribute type ''{2}''."),



  /**
   * DIT content rule definition {0} read from file ''{1}'' references unknown required attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_DCR_UNDEFINED_REQUIRED_ATTR("DIT content rule definition {0} read from file ''{1}'' references unknown required attribute type ''{2}''."),



  /**
   * DIT content rule {0} read from file ''{1}'' has OID ''{2}'' that does not match that of any known object class.
   */
  ERR_SCHEMA_VALIDATOR_DCR_UNKNOWN_STRUCTURAL_CLASS("DIT content rule {0} read from file ''{1}'' has OID ''{2}'' that does not match that of any known object class."),



  /**
   * Schema directory ''{0}'' contains subdirectory ''{1}'', but the schema validator is not configured to allow files contained in subdirectories.
   */
  ERR_SCHEMA_VALIDATOR_DIR_CONTAINS_SUBDIR("Schema directory ''{0}'' contains subdirectory ''{1}'', but the schema validator is not configured to allow files contained in subdirectories."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same rule ID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_DSR_ALREADY_DEFINED_WITH_ID("DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same rule ID:  {2}."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_DSR_ALREADY_DEFINED_WITH_NAME("DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defiend with name form ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_DSR_ALREADY_DEFINED_WITH_NF("DIT structure rule definition {0} read from file ''{1}'' conflicts with another definition already defiend with name form ''{2}'':  {3}."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_DSR_EMPTY_DESCRIPTION("DIT structure rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  DIT structure rule names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_DSR_INVALID_NAME("DIT structure rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  DIT structure rule names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Schema file ''{0}'' contains DIT structure rule {1} but the schema validator configuration does not permit DIT structure rules to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_DSR_NOT_ALLOWED("Schema file ''{0}'' contains DIT structure rule {1} but the schema validator configuration does not permit DIT structure rules to be defined in schema files."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_DSR_NO_NAME("DIT structure rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_DSR_OBSOLETE("DIT structure rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' references unknown name form ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_DSR_UNDEFINED_NF("DIT structure rule definition {0} read from file ''{1}'' references unknown name form ''{2}''."),



  /**
   * DIT structure rule definition {0} read from file ''{1}'' references unknown superior rule with rule ID {2,number,0}.
   */
  ERR_SCHEMA_VALIDATOR_DSR_UNDEFINED_SUP("DIT structure rule definition {0} read from file ''{1}'' references unknown superior rule with rule ID {2,number,0}."),



  /**
   * Schema element names must start with a letter.
   */
  ERR_SCHEMA_VALIDATOR_ELEMENT_NAME_DOES_NOT_START_WITH_LETTER("Schema element names must start with a letter."),



  /**
   * Schema element names must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_ELEMENT_NAME_EMPTY("Schema element names must not be empty."),



  /**
   * The name has an invalid character ''{0}'' at position {1,number,0}.
   */
  ERR_SCHEMA_VALIDATOR_ELEMENT_NAME_ILLEGAL_CHARACTER("The name has an invalid character ''{0}'' at position {1,number,0}."),



  /**
   * Entry ''{0}'' read from schema file ''{1}'' does not conform to the schema definitions that have been examined thus far:  {2}
   */
  ERR_SCHEMA_VALIDATOR_ENTRY_NOT_VALID("Entry ''{0}'' read from schema file ''{1}'' does not conform to the schema definitions that have been examined thus far:  {2}"),



  /**
   * An error occurred while attempting to read from schema file ''{0}'':  {1}
   */
  ERR_SCHEMA_VALIDATOR_ERROR_READING_FILE("An error occurred while attempting to read from schema file ''{0}'':  {1}"),



  /**
   * Schema directory ''{0}'' contains file ''{1}'' that does not match the configured schema file name pattern.  This file will still be examined because the schema validator is configured to not ignore files in the schema directory that do not match this pattern.
   */
  ERR_SCHEMA_VALIDATOR_FILE_NAME_DOES_NOT_MATCH_PATTERN("Schema directory ''{0}'' contains file ''{1}'' that does not match the configured schema file name pattern.  This file will still be examined because the schema validator is configured to not ignore files in the schema directory that do not match this pattern."),



  /**
   * Unable to parse data read from file ''{0}'' as a valid LDIF entry:  {1}
   */
  ERR_SCHEMA_VALIDATOR_MALFORMED_LDIF_ENTRY("Unable to parse data read from file ''{0}'' as a valid LDIF entry:  {1}"),



  /**
   * Matching rule use definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_MRU_ALREADY_DEFINED_WITH_NAME("Matching rule use definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_MRU_ALREADY_DEFINED_WITH_OID("Matching rule use definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_MRU_EMPTY_DESCRIPTION("Matching rule use definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' has invalid name ''{2}''.  Matching rule use names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_MRU_INVALID_NAME("Matching rule use definition {0} read from file ''{1}'' has invalid name ''{2}''.  Matching rule use names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_MRU_INVALID_OID("Matching rule use definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Schema file ''{0}'' contains matching rule use {1} but the schema validator configuration does not permit matching rule uses to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_MRU_NOT_ALLOWED("Schema file ''{0}'' contains matching rule use {1} but the schema validator configuration does not permit matching rule uses to be defined in schema files."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_MRU_NO_NAME("Matching rule use definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_MRU_OBSOLETE("Matching rule use definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * Attribute type {0} is defined with equality matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''.
   */
  ERR_SCHEMA_VALIDATOR_MRU_PROHIBITS_AT_EQ("Attribute type {0} is defined with equality matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''."),



  /**
   * Attribute type {0} is defined with ordering matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''.
   */
  ERR_SCHEMA_VALIDATOR_MRU_PROHIBITS_AT_ORD("Attribute type {0} is defined with ordering matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''."),



  /**
   * Attribute type {0} is defined with substring matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''.
   */
  ERR_SCHEMA_VALIDATOR_MRU_PROHIBITS_AT_SUB("Attribute type {0} is defined with substring matching rule ''{1}'', but that attribute type is not allowed to use that matching rule as per matching rule use definition {2} read from schema file ''{3}''."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' references unknown attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_MRU_UNDEFINED_AT("Matching rule use definition {0} read from file ''{1}'' references unknown attribute type ''{2}''."),



  /**
   * Matching rule use definition {0} read from file ''{1}'' references unknown matching rule with OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_MRU_UNDEFINED_MR("Matching rule use definition {0} read from file ''{1}'' references unknown matching rule with OID ''{2}''."),



  /**
   * Matching rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_MR_ALREADY_DEFINED_WITH_NAME("Matching rule definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * Matching rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_MR_ALREADY_DEFINED_WITH_OID("Matching rule definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Matching rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_MR_EMPTY_DESCRIPTION("Matching rule definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Matching rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  Matching rule names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_MR_INVALID_NAME("Matching rule definition {0} read from file ''{1}'' has invalid name ''{2}''.  Matching rule names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Matching rule definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_MR_INVALID_OID("Matching rule definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Matching rule definition {0} read from file ''{1}'' has invalid syntax OID ''{2}'':  {3}
   */
  ERR_SCHEMA_VALIDATOR_MR_INVALID_SYNTAX_OID("Matching rule definition {0} read from file ''{1}'' has invalid syntax OID ''{2}'':  {3}"),



  /**
   * Schema file ''{0}'' contains matching rule {1} but the schema validator configuration does not permit matching rules to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_MR_NOT_ALLOWED("Schema file ''{0}'' contains matching rule {1} but the schema validator configuration does not permit matching rules to be defined in schema files."),



  /**
   * Matching rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_MR_NO_NAME("Matching rule definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * Matching rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_MR_OBSOLETE("Matching rule definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * Matching rule definition {0} read from file ''{1}'' references an unknown attribute syntax with OID ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_MR_UNDEFINED_SYNTAX("Matching rule definition {0} read from file ''{1}'' references an unknown attribute syntax with OID ''{2}''."),



  /**
   * File ''{0}'' contains multiple LDIF entries.  Schema files should only contain a single entry, and only the first entry will be examined.
   */
  ERR_SCHEMA_VALIDATOR_MULTIPLE_ENTRIES_IN_FILE("File ''{0}'' contains multiple LDIF entries.  Schema files should only contain a single entry, and only the first entry will be examined."),



  /**
   * Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_NF_ALREADY_DEFINED_WITH_NAME("Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with structural object class ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_NF_ALREADY_DEFINED_WITH_OC("Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with structural object class ''{2}'':  {3}."),



  /**
   * Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_NF_ALREADY_DEFINED_WITH_OID("Name form definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Name form definition {0} read from file ''{1}'' lists attribute ''{2}'' as both required and optional.  An attribute type must not be declared both required and optional in the same name form definition.
   */
  ERR_SCHEMA_VALIDATOR_NF_ATTR_REQ_AND_OPT("Name form definition {0} read from file ''{1}'' lists attribute ''{2}'' as both required and optional.  An attribute type must not be declared both required and optional in the same name form definition."),



  /**
   * Name form definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_NF_EMPTY_DESCRIPTION("Name form definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Name form definition {0} read from file ''{1}'' has invalid name ''{2}''.  Object class names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_NF_INVALID_NAME("Name form definition {0} read from file ''{1}'' has invalid name ''{2}''.  Object class names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Name form definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_NF_INVALID_OID("Name form definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Schema file ''{0}'' contains name form {1} but the schema validator configuration does not permit name forms to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_NF_NOT_ALLOWED("Schema file ''{0}'' contains name form {1} but the schema validator configuration does not permit name forms to be defined in schema files."),



  /**
   * Name form definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_NF_NO_NAME("Name form definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * Name form definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_NF_OBSOLETE("Name form definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * Name form definition {0} read from file ''{1}'' references structural object class ''{2}'' that is not defined as structural.
   */
  ERR_SCHEMA_VALIDATOR_NF_OC_NOT_STRUCTURAL("Name form definition {0} read from file ''{1}'' references structural object class ''{2}'' that is not defined as structural."),



  /**
   * Name form definition {0} read from file ''{1}'' requires RDN attribute type ''{2}'' that is not permitted by structural object class ''{3}''.
   */
  ERR_SCHEMA_VALIDATOR_NF_REQ_ATTR_NOT_PERMITTED("Name form definition {0} read from file ''{1}'' requires RDN attribute type ''{2}'' that is not permitted by structural object class ''{3}''."),



  /**
   * Name form definition {0} read from file ''{1}'' references unknown structural object class ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_NF_UNDEFINED_OC("Name form definition {0} read from file ''{1}'' references unknown structural object class ''{2}''."),



  /**
   * Name form definition {0} read from file ''{1}'' references unknown optional attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_NF_UNDEFINED_OPT_ATTR("Name form definition {0} read from file ''{1}'' references unknown optional attribute type ''{2}''."),



  /**
   * Name form definition {0} read from file ''{1}'' references unknown required attribute type ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_NF_UNDEFINED_REQ_ATTR("Name form definition {0} read from file ''{1}'' references unknown required attribute type ''{2}''."),



  /**
   * File ''{0}'' did not contain any entry from which to parse schema definitions.
   */
  ERR_SCHEMA_VALIDATOR_NO_ENTRY_IN_FILE("File ''{0}'' did not contain any entry from which to parse schema definitions."),



  /**
   * No schema files were found at path ''{0}'', but the following files were ignored because they did not match the expected schema file name pattern:  {1}.
   */
  ERR_SCHEMA_VALIDATOR_NO_SCHEMA_FILES_MULTIPLE_IGNORED("No schema files were found at path ''{0}'', but the following files were ignored because they did not match the expected schema file name pattern:  {1}."),



  /**
   * No schema files were found at path ''{0}''.
   */
  ERR_SCHEMA_VALIDATOR_NO_SCHEMA_FILES_NONE_IGNORED("No schema files were found at path ''{0}''."),



  /**
   * No schema files were found at path ''{0}'', but file ''{1}'' was ignored because it did not match the expected schema file name pattern.
   */
  ERR_SCHEMA_VALIDATOR_NO_SCHEMA_FILES_ONE_IGNORED("No schema files were found at path ''{0}'', but file ''{1}'' was ignored because it did not match the expected schema file name pattern."),



  /**
   * Unable to validate the schema definitions at path ''{0}'' because that path does not exist.
   */
  ERR_SCHEMA_VALIDATOR_NO_SUCH_PATH("Unable to validate the schema definitions at path ''{0}'' because that path does not exist."),



  /**
   * Abstract object class definition {0} read from file ''{1}'' includes an auxiliary superior class of ''{2}''.  Abstract object classes may only inherit from other abstract classes.
   */
  ERR_SCHEMA_VALIDATOR_OC_ABSTRACT_SUP_OF_AUXILIARY("Abstract object class definition {0} read from file ''{1}'' includes an auxiliary superior class of ''{2}''.  Abstract object classes may only inherit from other abstract classes."),



  /**
   * Abstract object class definition {0} read from file ''{1}'' includes a structural superior class of ''{2}''.  Abstract object classes may only inherit from other abstract classes.
   */
  ERR_SCHEMA_VALIDATOR_OC_ABSTRACT_SUP_OF_STRUCTURAL("Abstract object class definition {0} read from file ''{1}'' includes a structural superior class of ''{2}''.  Abstract object classes may only inherit from other abstract classes."),



  /**
   * Object class definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}.
   */
  ERR_SCHEMA_VALIDATOR_OC_ALREADY_DEFINED_WITH_NAME("Object class definition {0} read from file ''{1}'' conflicts with another definition already defined with name ''{2}'':  {3}."),



  /**
   * Object class definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_OC_ALREADY_DEFINED_WITH_OID("Object class definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Object class definition {0} read from file ''{1}'' includes attribute ''{2}'' as both required and optional.
   */
  ERR_SCHEMA_VALIDATOR_OC_AT_REQ_AND_OPT("Object class definition {0} read from file ''{1}'' includes attribute ''{2}'' as both required and optional."),



  /**
   * Auxiliary object class definition {0} read from file ''{1}'' includes a structural superior class of ''{2}''.  Auxiliary object classes may only inherit from auxiliary and abstract classes.
   */
  ERR_SCHEMA_VALIDATOR_OC_AUXILIARY_SUP_OF_STRUCTURAL("Auxiliary object class definition {0} read from file ''{1}'' includes a structural superior class of ''{2}''.  Auxiliary object classes may only inherit from auxiliary and abstract classes."),



  /**
   * Object class definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_OC_EMPTY_DESCRIPTION("Object class definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Structural object class definition {0} read from file ''{1}'' has an implied type of structural, but inherits from auxiliary class ''{2}''.  Structural object classes may only inherit from structural and abstract classes.
   */
  ERR_SCHEMA_VALIDATOR_OC_IMPLIED_STRUCTURAL_SUP_OF_AUXILIARY("Structural object class definition {0} read from file ''{1}'' has an implied type of structural, but inherits from auxiliary class ''{2}''.  Structural object classes may only inherit from structural and abstract classes."),



  /**
   * Object class definition {0} read from file ''{1}'' has invalid name ''{2}''.  Object class names must start with a letter, and may contain only letters, digits, and hyphens.
   */
  ERR_SCHEMA_VALIDATOR_OC_INVALID_NAME("Object class definition {0} read from file ''{1}'' has invalid name ''{2}''.  Object class names must start with a letter, and may contain only letters, digits, and hyphens."),



  /**
   * Object class definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_OC_INVALID_OID("Object class definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Object class definition {0} read from file ''{1}'' contains multiple superior object classes.  Although this is legal, some servers may not support object classes with multiple superiors.
   */
  ERR_SCHEMA_VALIDATOR_OC_MULTIPLE_SUP("Object class definition {0} read from file ''{1}'' contains multiple superior object classes.  Although this is legal, some servers may not support object classes with multiple superiors."),



  /**
   * Schema file ''{0}'' contains object class {1} but the schema validator configuration does not permit object classes to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_OC_NOT_ALLOWED("Schema file ''{0}'' contains object class {1} but the schema validator configuration does not permit object classes to be defined in schema files."),



  /**
   * Object class definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names.
   */
  ERR_SCHEMA_VALIDATOR_OC_NO_NAME("Object class definition {0} read from file ''{1}'' does not have a name.  Although this is legal, schema elements without names are not as user-friendly as those with names."),



  /**
   * Object class definition {0} read from file ''{1}'' does not declare any superior classes and has an implied type of structural.  Structural object classes should define at least one superior class.  The ''top'' object class may be used as the superior class if no other class is appropriate.
   */
  ERR_SCHEMA_VALIDATOR_OC_NO_SUP_NULL_TYPE("Object class definition {0} read from file ''{1}'' does not declare any superior classes and has an implied type of structural.  Structural object classes should define at least one superior class.  The ''top'' object class may be used as the superior class if no other class is appropriate."),



  /**
   * Object class definition {0} read from file ''{1}'' does not declare any superior classes and is declared as a structural class.  Structural object classes should define at least one superior class.  The ''top'' object class may be used as the superior class if no other class is appropriate.
   */
  ERR_SCHEMA_VALIDATOR_OC_NO_SUP_STRUCTURAL_TYPE("Object class definition {0} read from file ''{1}'' does not declare any superior classes and is declared as a structural class.  Structural object classes should define at least one superior class.  The ''top'' object class may be used as the superior class if no other class is appropriate."),



  /**
   * Object class definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly.
   */
  ERR_SCHEMA_VALIDATOR_OC_OBSOLETE("Object class definition {0} read from file ''{1}'' is declared obsolete.  Although this is legal, some servers may not handle obsolete elements properly."),



  /**
   * Structural object class definition {0} read from file ''{1}'' includes an auxiliary superior class of ''{2}''.  Structural object classes may only inherit from structural and abstract classes.
   */
  ERR_SCHEMA_VALIDATOR_OC_STRUCTURAL_SUP_OF_AUXILIARY("Structural object class definition {0} read from file ''{1}'' includes an auxiliary superior class of ''{2}''.  Structural object classes may only inherit from structural and abstract classes."),



  /**
   * Object class definition {0} read from schema file ''{1}'' references unknown optional attribute ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_OC_UNDEFINED_OPTIONAL_ATTR("Object class definition {0} read from schema file ''{1}'' references unknown optional attribute ''{2}''."),



  /**
   * Object class definition {0} read from schema file ''{1}'' references unknown required attribute ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_OC_UNDEFINED_REQUIRED_ATTR("Object class definition {0} read from schema file ''{1}'' references unknown required attribute ''{2}''."),



  /**
   * Object class definition {0} read from file ''{1}'' references unknown superior class ''{2}''.
   */
  ERR_SCHEMA_VALIDATOR_OC_UNDEFINED_SUP("Object class definition {0} read from file ''{1}'' references unknown superior class ''{2}''."),



  /**
   * Attribute syntax definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}.
   */
  ERR_SCHEMA_VALIDATOR_SYNTAX_ALREADY_DEFINED("Attribute syntax definition {0} read from file ''{1}'' conflicts with another definition already defined with the same OID:  {2}."),



  /**
   * Attribute syntax definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty.
   */
  ERR_SCHEMA_VALIDATOR_SYNTAX_EMPTY_DESCRIPTION("Attribute syntax definition {0} read from file ''{1}'' has an empty description.  Quoted strings in schema elements must not be empty."),



  /**
   * Attribute syntax definition {0} read from file ''{1}'' has an invalid object identifier:  {2}
   */
  ERR_SCHEMA_VALIDATOR_SYNTAX_INVALID_OID("Attribute syntax definition {0} read from file ''{1}'' has an invalid object identifier:  {2}"),



  /**
   * Schema file ''{0}'' contains attribute syntax {1} but the schema validator configuration does not permit attribute syntaxes to be defined in schema files.
   */
  ERR_SCHEMA_VALIDATOR_SYNTAX_NOT_ALLOWED("Schema file ''{0}'' contains attribute syntax {1} but the schema validator configuration does not permit attribute syntaxes to be defined in schema files."),



  /**
   * Although the ''{0}'' argument may be used to indicate that certain types of schema elements are not allowed to be provided in schema files, you cannot prohibit all types of schema elements.
   */
  ERR_VALIDATE_SCHEMA_ALL_ELEMENT_TYPES_PROHIBITED("Although the ''{0}'' argument may be used to indicate that certain types of schema elements are not allowed to be provided in schema files, you cannot prohibit all types of schema elements."),



  /**
   * The following errors were found while validating the schema:
   */
  ERR_VALIDATE_SCHEMA_ERRORS_FOUND("The following errors were found while validating the schema:"),



  /**
   * The following error was found while validating the schema:
   */
  ERR_VALIDATE_SCHEMA_ERROR_FOUND("The following error was found while validating the schema:"),



  /**
   * {0,number,0} errors were found while validating the schema.
   */
  ERR_VALIDATE_SCHEMA_MULTIPLE_ERRORS("{0,number,0} errors were found while validating the schema."),



  /**
   * An invalid value of ''{0}'' was provided for the ''{1}'' argument.  Permitted values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.
   */
  ERR_VALIDATE_SCHEMA_NO_SUCH_ELEMENT_TYPE("An invalid value of ''{0}'' was provided for the ''{1}'' argument.  Permitted values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''."),



  /**
   * One error was found while validating the schema.
   */
  ERR_VALIDATE_SCHEMA_ONE_ERROR("One error was found while validating the schema."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) were found to be invalid.
   */
  INFO_ENTRY_INVALID_ENTRY_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) were found to be invalid."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) had malformed DNs.
   */
  INFO_ENTRY_MALFORMED_DN_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) had malformed DNs."),



  /**
   * {0,number,0} required attributes were missing from entries.
   */
  INFO_ENTRY_MISSING_ATTR_COUNT("{0,number,0} required attributes were missing from entries."),



  /**
   * Required attribute {0} was found to be missing {1,number,0} times.
   */
  INFO_ENTRY_MISSING_ATTR_NAME_COUNT("Required attribute {0} was found to be missing {1,number,0} times."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) had attribute values present in their RDNs that were not present in the set of entry attributes.
   */
  INFO_ENTRY_MISSING_RDN_VALUE_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) had attribute values present in their RDNs that were not present in the set of entry attributes."),



  /**
   * {0,number,0} missing superior object classes were found in entries.
   */
  INFO_ENTRY_MISSING_SUPERIOR_OC_COUNT("{0,number,0} missing superior object classes were found in entries."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) had multiple structural object classes.
   */
  INFO_ENTRY_MULTIPLE_STRUCTURAL_OCS_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) had multiple structural object classes."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) had RDNs which violated the associated name form.
   */
  INFO_ENTRY_NF_VIOLATION_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) had RDNs which violated the associated name form."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) did not have any object classes.
   */
  INFO_ENTRY_NO_OC_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) did not have any object classes."),



  /**
   * {0,number,0} of {1,number,0} entries ({2,number,0} percent) did not have a structural object class.
   */
  INFO_ENTRY_NO_STRUCTURAL_OC_COUNT("{0,number,0} of {1,number,0} entries ({2,number,0} percent) did not have a structural object class."),



  /**
   * {0,number,0} prohibited attributes were found in entries.
   */
  INFO_ENTRY_PROHIBITED_ATTR_COUNT("{0,number,0} prohibited attributes were found in entries."),



  /**
   * Prohibited attribute {0} was encountered {1,number,0} times.
   */
  INFO_ENTRY_PROHIBITED_ATTR_NAME_COUNT("Prohibited attribute {0} was encountered {1,number,0} times."),



  /**
   * {0,number,0} prohibited object classes were found in entries.
   */
  INFO_ENTRY_PROHIBITED_OC_COUNT("{0,number,0} prohibited object classes were found in entries."),



  /**
   * Prohibited object class {0} was encountered {1,number,0} times.
   */
  INFO_ENTRY_PROHIBITED_OC_NAME_COUNT("Prohibited object class {0} was encountered {1,number,0} times."),



  /**
   * {0,number,0} single-valued attributes were found with multiple values.
   */
  INFO_ENTRY_SINGLE_VALUE_VIOLATION_COUNT("{0,number,0} single-valued attributes were found with multiple values."),



  /**
   * Single-valued attribute {0} was found to have multiple values in {1,number,0} entries.
   */
  INFO_ENTRY_SINGLE_VALUE_VIOLATION_NAME_COUNT("Single-valued attribute {0} was found to have multiple values in {1,number,0} entries."),



  /**
   * {0,number,0} attribute values were found which violated the associated attribute syntax.
   */
  INFO_ENTRY_SYNTAX_VIOLATION_COUNT("{0,number,0} attribute values were found which violated the associated attribute syntax."),



  /**
   * Attribute {0} was found to have {1,number,0} invalid values.
   */
  INFO_ENTRY_SYNTAX_VIOLATION_NAME_COUNT("Attribute {0} was found to have {1,number,0} invalid values."),



  /**
   * {0,number,0} undefined attributes were encountered.
   */
  INFO_ENTRY_UNDEFINED_ATTR_COUNT("{0,number,0} undefined attributes were encountered."),



  /**
   * Undefined attribute {0} was encountered {1,number,0} times.
   */
  INFO_ENTRY_UNDEFINED_ATTR_NAME_COUNT("Undefined attribute {0} was encountered {1,number,0} times."),



  /**
   * {0,number,0} undefined object classes were encountered.
   */
  INFO_ENTRY_UNDEFINED_OC_COUNT("{0,number,0} undefined object classes were encountered."),



  /**
   * Undefined object class {0} was encountered {1,number,0} times.
   */
  INFO_ENTRY_UNDEFINED_OC_NAME_COUNT("Undefined object class {0} was encountered {1,number,0} times."),



  /**
   * A type of schema element that is allowed to be defined in schema files.  This may be provided multiple times if multiple specific element types are allowed.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, then all element types will be allowed.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOWED_ELEMENT_TYPE("A type of schema element that is allowed to be defined in schema files.  This may be provided multiple times if multiple specific element types are allowed.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, then all element types will be allowed."),



  /**
   * Allow attribute type definitions that do not directly specify a syntax and do not inherit the syntax of a superior attribute type.  Although this is technically not valid, some servers assume a default syntax for such attribute types.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_AT_WITHOUT_SYNTAX("Allow attribute type definitions that do not directly specify a syntax and do not inherit the syntax of a superior attribute type.  Although this is technically not valid, some servers assume a default syntax for such attribute types."),



  /**
   * Allow schema elements with an empty string as the value for the ''DESC'' element.  LDAP does not allow schema element definitions to contain empty quoted strings, but some servers allow them to have empty descriptions.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_EMPTY_DESC("Allow schema elements with an empty string as the value for the ''DESC'' element.  LDAP does not allow schema element definitions to contain empty quoted strings, but some servers allow them to have empty descriptions."),



  /**
   * Allow structural object classes that do not superior class.  All structural object classes must specify a superior class, and that superior class must be structural or abstract, but some servers may assume a default superior object class of ''top'' for any structural class that does not explicitly specify one.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_MISSING_MISSING_OC_SUP("Allow structural object classes that do not superior class.  All structural object classes must specify a superior class, and that superior class must be structural or abstract, but some servers may assume a default superior object class of ''top'' for any structural class that does not explicitly specify one."),



  /**
   * Allow schema elements to only have OIDs but no names.  Although it is technically valid to have schema elements without names, such elements are not as user-friendly as those with names.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_MISSING_NAME("Allow schema elements to only have OIDs but no names.  Although it is technically valid to have schema elements without names, such elements are not as user-friendly as those with names."),



  /**
   * Allow schema files to contain multiple entries.  By default, each schema file may contain only a single entry.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_MULTIPLE_ENTRIES("Allow schema files to contain multiple entries.  By default, each schema file may contain only a single entry."),



  /**
   * Allow schema elements to have non-numeric object identifiers.  Although this is technically not valid, some servers allow the use of non-numeric OIDs.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_NON_NUMERIC_OID("Allow schema elements to have non-numeric object identifiers.  Although this is technically not valid, some servers allow the use of non-numeric OIDs."),



  /**
   * Allow a schema element to be defined multiple times.  If this is provided, then subsequent definitions of a schema elements will override previous definitions of the same element.  By default, each schema element may only be defined once.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_REDEFINING("Allow a schema element to be defined multiple times.  If this is provided, then subsequent definitions of a schema elements will override previous definitions of the same element.  By default, each schema element may only be defined once."),



  /**
   * Allow searching for schema files in subdirectories of the provided schema path(s).  This only applies to schema paths that reference directories, and if this argument is provided, then subdirectories will also be examined to look for additional schema files.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_SUB_DIRS("Allow searching for schema files in subdirectories of the provided schema path(s).  This only applies to schema paths that reference directories, and if this argument is provided, then subdirectories will also be examined to look for additional schema files."),



  /**
   * A type of schema element that may be referenced by an element read from a schema file but that has not been defined in the schema files being read.  This may be provided multiple times if multiple types of undefined elements should be allowed.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, now undefined element types will be allowed, and any reference to an undefined schema element will be reported as an error.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_ALLOW_UNDEFINED("A type of schema element that may be referenced by an element read from a schema file but that has not been defined in the schema files being read.  This may be provided multiple times if multiple types of undefined elements should be allowed.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, now undefined element types will be allowed, and any reference to an undefined schema element will be reported as an error."),



  /**
   * Use lenient validation for schema element names.  Valid LDAP schema element names must start with a letter, and must only contain letters, digits, and hyphens, although some servers have more lax name requirements.  If this option is provided, then schema element names will also be permitted to have underscores, and will be permitted to start with any allowed character.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_LENIENT_NAMES("Use lenient validation for schema element names.  Valid LDAP schema element names must start with a letter, and must only contain letters, digits, and hyphens, although some servers have more lax name requirements.  If this option is provided, then schema element names will also be permitted to have underscores, and will be permitted to start with any allowed character."),



  /**
   * Use lenient validation for numeric object identifiers.  By default, numeric OIDs will be strictly validated.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_LENIENT_OID("Use lenient validation for numeric object identifiers.  By default, numeric OIDs will be strictly validated."),



  /**
   * A type of schema element that is not permitted to be defined in schema files.  This may be provided multiple times if multiple specific element types are prohibited.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, then no element types will be prohibited.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_PROHIBITED_ELEMENT_TYPE("A type of schema element that is not permitted to be defined in schema files.  This may be provided multiple times if multiple specific element types are prohibited.  Allowed values include:  ''attribute-syntax'', ''matching-rule'', ''attribute-type'', ''object-class'', ''name-form'', ''dit-content-rule'', ''dit-structure-rule'', and ''matching-rule-use''.  If this argument is not provided, then no element types will be prohibited."),



  /**
   * Report an error for each attribute type definitions that does not directly specify an equality matching rule and does not inherit an equality matching rule from a superior attribute type.  Although this is technically valid, servers will fall back to byte-for-byte matching for values of such attributes (although some servers may assume a default equality matching rule based on the syntax), which may not be the desired behavior.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_REJECT_AT_WITHOUT_EQ_MR("Report an error for each attribute type definitions that does not directly specify an equality matching rule and does not inherit an equality matching rule from a superior attribute type.  Although this is technically valid, servers will fall back to byte-for-byte matching for values of such attributes (although some servers may assume a default equality matching rule based on the syntax), which may not be the desired behavior."),



  /**
   * Reject object classes that have multiple superior classes.  Although this is valid, some servers do not support object classes with multiple superiors.
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_REJECT_MULTIPLE_OC_SUP("Reject object classes that have multiple superior classes.  Although this is valid, some servers do not support object classes with multiple superiors."),



  /**
   * The path to the schema definitions to parse.  This may be an LDIF file containing a subschema subentry, or it may be a directory containing one or more schema files (in which case the files will be processed in alphabetic order by file name).  This may be provided multiple times if schema definitions should be read from multiple paths (in which case the paths will be processed in they order they were provided on the command line).
   */
  INFO_VALIDATE_SCHEMA_ARG_DESC_SCHEMA_PATH("The path to the schema definitions to parse.  This may be an LDIF file containing a subschema subentry, or it may be a directory containing one or more schema files (in which case the files will be processed in alphabetic order by file name).  This may be provided multiple times if schema definitions should be read from multiple paths (in which case the paths will be processed in they order they were provided on the command line)."),



  /**
   * Input Arguments
   */
  INFO_VALIDATE_SCHEMA_ARG_GROUP_INPUT("Input Arguments"),



  /**
   * Validation Arguments
   */
  INFO_VALIDATE_SCHEMA_ARG_GROUP_VALIDATION("Validation Arguments"),



  /**
   * '{'elementType'}'
   */
  INFO_VALIDATE_SCHEMA_ARG_PLACEHOLDER_ELEMENT_TYPE("'{'elementType'}'"),



  /**
   * Validate schema definitions in the specified path using the default settings.
   */
  INFO_VALIDATE_SCHEMA_EXAMPLE_1("Validate schema definitions in the specified path using the default settings."),



  /**
   * Validate schema definitions in the specified path using more lenient settings.
   */
  INFO_VALIDATE_SCHEMA_EXAMPLE_2("Validate schema definitions in the specified path using more lenient settings."),



  /**
   * No errors were found while validating the schema.
   */
  INFO_VALIDATE_SCHEMA_NO_ERRORS("No errors were found while validating the schema."),



  /**
   * Validate an LDAP schema read from one or more LDIF files.
   */
  INFO_VALIDATE_SCHEMA_TOOL_DESCRIPTION("Validate an LDAP schema read from one or more LDIF files.");



  /**
   * Indicates whether the unit tests are currently running.
   */
  private static final boolean IS_WITHIN_UNIT_TESTS =
       Boolean.getBoolean("com.unboundid.ldap.sdk.RunningUnitTests") ||
       Boolean.getBoolean("com.unboundid.directory.server.RunningUnitTests");



  /**
   * A pre-allocated array of zero objects to use for messages
   * that do not require any arguments.
   */
  private static final Object[] NO_ARGS = new Object[0];



  /**
   * The resource bundle that will be used to load the properties file.
   */
  private static final ResourceBundle RESOURCE_BUNDLE;
  static
  {
    ResourceBundle rb = null;
    try
    {
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-schema");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<SchemaMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<SchemaMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private SchemaMessages(final String defaultText)
  {
    this.defaultText = defaultText;
  }



  /**
   * Retrieves a localized version of the message.
   * This method should only be used for messages that do not take any
   * arguments.
   *
   * @return  A localized version of the message.
   */
  public String get()
  {
    MessageFormat f = MESSAGES.get(this);
    if (f == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        f = new MessageFormat(defaultText);
      }
      else
      {
        try
        {
          f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
        }
        catch (final Exception e)
        {
          f = new MessageFormat(defaultText);
        }
      }
      MESSAGES.putIfAbsent(this, f);
    }

    final String formattedMessage;
    synchronized (f)
    {
      formattedMessage = f.format(NO_ARGS);
    }

    if (IS_WITHIN_UNIT_TESTS)
    {
      if (formattedMessage.contains("{0}") ||
          formattedMessage.contains("{0,number,0}") ||
          formattedMessage.contains("{1}") ||
          formattedMessage.contains("{1,number,0}") ||
          formattedMessage.contains("{2}") ||
          formattedMessage.contains("{2,number,0}") ||
          formattedMessage.contains("{3}") ||
          formattedMessage.contains("{3,number,0}") ||
          formattedMessage.contains("{4}") ||
          formattedMessage.contains("{4,number,0}") ||
          formattedMessage.contains("{5}") ||
          formattedMessage.contains("{5,number,0}") ||
          formattedMessage.contains("{6}") ||
          formattedMessage.contains("{6,number,0}") ||
          formattedMessage.contains("{7}") ||
          formattedMessage.contains("{7,number,0}") ||
          formattedMessage.contains("{8}") ||
          formattedMessage.contains("{8,number,0}") ||
          formattedMessage.contains("{9}") ||
          formattedMessage.contains("{9,number,0}") ||
          formattedMessage.contains("{10}") ||
          formattedMessage.contains("{10,number,0}"))
      {
        throw new IllegalArgumentException(
             "Message " + getClass().getName() + '.' + name() +
                  " contains an un-replaced token:  " + formattedMessage);
      }
    }

    return formattedMessage;
  }



  /**
   * Retrieves a localized version of the message.
   *
   * @param  args  The arguments to use to format the message.
   *
   * @return  A localized version of the message.
   */
  public String get(final Object... args)
  {
    MessageFormat f = MESSAGES.get(this);
    if (f == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        f = new MessageFormat(defaultText);
      }
      else
      {
        try
        {
          f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
        }
        catch (final Exception e)
        {
          f = new MessageFormat(defaultText);
        }
      }
      MESSAGES.putIfAbsent(this, f);
    }

    final String formattedMessage;
    synchronized (f)
    {
      formattedMessage = f.format(args);
    }

    if (IS_WITHIN_UNIT_TESTS)
    {
      if (formattedMessage.contains("{0}") ||
          formattedMessage.contains("{0,number,0}") ||
          formattedMessage.contains("{1}") ||
          formattedMessage.contains("{1,number,0}") ||
          formattedMessage.contains("{2}") ||
          formattedMessage.contains("{2,number,0}") ||
          formattedMessage.contains("{3}") ||
          formattedMessage.contains("{3,number,0}") ||
          formattedMessage.contains("{4}") ||
          formattedMessage.contains("{4,number,0}") ||
          formattedMessage.contains("{5}") ||
          formattedMessage.contains("{5,number,0}") ||
          formattedMessage.contains("{6}") ||
          formattedMessage.contains("{6,number,0}") ||
          formattedMessage.contains("{7}") ||
          formattedMessage.contains("{7,number,0}") ||
          formattedMessage.contains("{8}") ||
          formattedMessage.contains("{8,number,0}") ||
          formattedMessage.contains("{9}") ||
          formattedMessage.contains("{9,number,0}") ||
          formattedMessage.contains("{10}") ||
          formattedMessage.contains("{10,number,0}"))
      {
        throw new IllegalArgumentException(
             "Message " + getClass().getName() + '.' + name() +
                  " contains an un-replaced token:  " + formattedMessage);
      }
    }

    return formattedMessage;
  }



  /**
   * Retrieves a string representation of this message key.
   *
   * @return  A string representation of this message key.
   */
  @Override()
  public String toString()
  {
    String s = MESSAGE_STRINGS.get(this);
    if (s == null)
    {
      if (RESOURCE_BUNDLE == null)
      {
        s = defaultText;
      }
      else
      {
        try
        {
          s = RESOURCE_BUNDLE.getString(name());
        }
        catch (final Exception e)
        {
          s = defaultText;
        }
        MESSAGE_STRINGS.putIfAbsent(this, s);
      }
    }

    return s;
  }
}

