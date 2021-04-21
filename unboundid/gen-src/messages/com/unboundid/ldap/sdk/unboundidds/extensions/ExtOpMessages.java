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
package com.unboundid.ldap.sdk.unboundidds.extensions;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.ldap.sdk.unboundidds.extensions package, which correspond to messages in the
 * unboundid-ldapsdk-unboundid-extop.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum ExtOpMessages
{
  /**
   * An error was encountered while attempting to decode the all attributes get changelog batch change selection criteria element:  {0}
   */
  ERR_ALL_ATTRS_CHANGE_SELECTION_CRITERIA_DECODE_ERROR("An error was encountered while attempting to decode the all attributes get changelog batch change selection criteria element:  {0}"),



  /**
   * An error was encountered while attempting to decode the any attributes get changelog batch change selection criteria element:  {0}
   */
  ERR_ANY_ATTRS_CHANGE_SELECTION_CRITERIA_DECODE_ERROR("An error was encountered while attempting to decode the any attributes get changelog batch change selection criteria element:  {0}"),



  /**
   * An error occurred while attempting to decode an exact match assured replication poll criteria:  {0}
   */
  ERR_ASSURED_REPLICATION_EQ_POLL_CRITERIA_CANNOT_DECODE("An error occurred while attempting to decode an exact match assured replication poll criteria:  {0}"),



  /**
   * An error occurred while attempting to decode greater-or-equal assured replication poll criteria:  {0}
   */
  ERR_ASSURED_REPLICATION_GE_POLL_CRITERIA_CANNOT_DECODE("An error occurred while attempting to decode greater-or-equal assured replication poll criteria:  {0}"),



  /**
   * Unable to decode an assured replication poll criteria element because it has an unexpected BER type of {0}.
   */
  ERR_ASSURED_REPLICATION_POLL_CRITERIA_UNEXPECTED_TYPE("Unable to decode an assured replication poll criteria element because it has an unexpected BER type of {0}."),



  /**
   * An error occurred while attempting to decode the value of the provided extended request for use as an assured replication poll request:  {0}
   */
  ERR_ASSURED_REPLICATION_POLL_REQUEST_ERROR_DECODING_VALUE("An error occurred while attempting to decode the value of the provided extended request for use as an assured replication poll request:  {0}"),



  /**
   * Unable to decode the provided extended request as an assured replication poll request because it does not have a value.
   */
  ERR_ASSURED_REPLICATION_POLL_REQUEST_NO_VALUE("Unable to decode the provided extended request as an assured replication poll request because it does not have a value."),



  /**
   * Unable to decode an ASN.1 element as a beginning of changelog starting point because it has a nonzero-length value.
   */
  ERR_BEGINNING_OF_CHANGELOG_STARTING_POINT_HAS_VALUE("Unable to decode an ASN.1 element as a beginning of changelog starting point because it has a nonzero-length value."),



  /**
   * An error occurred while attempting to parse the value of the changelog entry intermediate response:  {0}
   */
  ERR_CHANGELOG_ENTRY_IR_ERROR_PARSING_VALUE("An error occurred while attempting to parse the value of the changelog entry intermediate response:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the value sequence had an unexpected number of elements (expected 4, got {0}).
   */
  ERR_CHANGELOG_ENTRY_IR_INVALID_VALUE_COUNT("Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the value sequence had an unexpected number of elements (expected 4, got {0})."),



  /**
   * Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the provided response did not have a value.
   */
  ERR_CHANGELOG_ENTRY_IR_NO_VALUE("Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the provided response did not have a value."),



  /**
   * Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the value could not be parsed as an ASN.1 sequence:  {0}
   */
  ERR_CHANGELOG_ENTRY_IR_VALUE_NOT_SEQUENCE("Unable to decode the provided intermediate response as a changelog batch entry intermediate response because the value could not be parsed as an ASN.1 sequence:  {0}"),



  /**
   * Unable to decode an ASN.1 element as a change time staring point because the value could not be parsed as a generalized time string:  {0}
   */
  ERR_CHANGE_TIME_STARTING_POINT_MALFORMED_VALUE("Unable to decode an ASN.1 element as a change time staring point because the value could not be parsed as a generalized time string:  {0}"),



  /**
   * Unable to decode the get changelog batch change selection criteria because the change selection criteria element value could not be decoded as an ASN.1 element:  {0}
   */
  ERR_CLBATCH_CHANGE_SELECTION_CRITERIA_DECODE_INNER_FAILURE("Unable to decode the get changelog batch change selection criteria because the change selection criteria element value could not be decoded as an ASN.1 element:  {0}"),



  /**
   * Unable to decode the get changelog batch change selection criteria element because it had an unknown BER type of {0}.
   */
  ERR_CLBATCH_CHANGE_SELECTION_CRITERIA_UNKNOWN_TYPE("Unable to decode the get changelog batch change selection criteria element because it had an unknown BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a clear missed notification changes alarm request because the request does not have a value.
   */
  ERR_CLEAR_MISSED_NOTIFICATION_CHANGES_ALARM_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a clear missed notification changes alarm request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a clear missed notification changes alarm request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_CLEAR_MISSED_NOTIFICATION_CHANGES_ALARM_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a clear missed notification changes alarm request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a consume single-use token request:  {0}
   */
  ERR_CONSUME_SINGLE_USE_TOKEN_REQUEST_CANNOT_DECODE("Unable to decode the provided extended request as a consume single-use token request:  {0}"),



  /**
   * Unable to decode the provided extended request as a consume single-use token request because the request does not have a value.
   */
  ERR_CONSUME_SINGLE_USE_TOKEN_REQUEST_NO_VALUE("Unable to decode the provided extended request as a consume single-use token request because the request does not have a value."),



  /**
   * Unable to decode the provided intermediate response as a collect support data archive fragment intermediate response because an unexpected error occurred while attempting to decode the response value:  {0}
   */
  ERR_CSD_FRAGMENT_IR_DECODE_ERROR("Unable to decode the provided intermediate response as a collect support data archive fragment intermediate response because an unexpected error occurred while attempting to decode the response value:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a collect support data archive fragment intermediate response because the provided response does not have a value.
   */
  ERR_CSD_FRAGMENT_IR_DECODE_NO_VALUE("Unable to decode the provided intermediate response as a collect support data archive fragment intermediate response because the provided response does not have a value."),



  /**
   * Unable to decode the provided ASN.1 element as a collect support data log capture window object because the element has an unrecognized BER type of {0}.
   */
  ERR_CSD_LOG_WINDOW_CANNOT_DECODE("Unable to decode the provided ASN.1 element as a collect support data log capture window object because the element has an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided intermediate response as a collect support data output intermediate response because an unexpected error occurred while attempting to decode the response value:  {0}
   */
  ERR_CSD_OUTPUT_IR_DECODE_ERROR("Unable to decode the provided intermediate response as a collect support data output intermediate response because an unexpected error occurred while attempting to decode the response value:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a collect support data output intermediate response because the provided response does not have a value.
   */
  ERR_CSD_OUTPUT_IR_DECODE_NO_VALUE("Unable to decode the provided intermediate response as a collect support data output intermediate response because the provided response does not have a value."),



  /**
   * Unable to decode the provided intermediate response as a collect support data output intermediate response because the value sequence referenced an output stream with an unrecognized integer value of {0,number,0}.
   */
  ERR_CSD_OUTPUT_IR_DECODE_UNRECOGNIZED_OUTPUT_STREAM("Unable to decode the provided intermediate response as a collect support data output intermediate response because the value sequence referenced an output stream with an unrecognized integer value of {0,number,0}."),



  /**
   * Unable to decode the provided extended request as a collect support data extended request because an unexpected error occurred while attempting to decode the request value:  {0}
   */
  ERR_CSD_REQUEST_DECODE_ERROR("Unable to decode the provided extended request as a collect support data extended request because an unexpected error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a collect support data extended request because an error occurred while attempting to decode the request log capture window element int he request value:  {0}
   */
  ERR_CSD_REQUEST_DECODE_LCW_FAILED("Unable to decode the provided extended request as a collect support data extended request because an error occurred while attempting to decode the request log capture window element int he request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a collect support data extended request because the provided request does not have a value.
   */
  ERR_CSD_REQUEST_DECODE_NO_VALUE("Unable to decode the provided extended request as a collect support data extended request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a collect support data extended request because the value sequence referenced a security level with an unrecognized integer value of {0,number,0}.
   */
  ERR_CSD_REQUEST_DECODE_UNSUPPORTED_SECURITY_LEVEL("Unable to decode the provided extended request as a collect support data extended request because the value sequence referenced a security level with an unrecognized integer value of {0,number,0}."),



  /**
   * Unsupported collect support data extended request security level ''{0}''.
   */
  ERR_CSD_REQUEST_UNSUPPORTED_SECURITY_LEVEL("Unsupported collect support data extended request security level ''{0}''."),



  /**
   * Unable to decode the provided extended result as a collect support data extended result because an unexpected error occurred while attempting to decode the result value:  {0}
   */
  ERR_CSD_RESULT_DECODE_ERROR("Unable to decode the provided extended result as a collect support data extended result because an unexpected error occurred while attempting to decode the result value:  {0}"),



  /**
   * Unable to decode the provided extended request as a deliver one-time password request because an error occurred while parsing the request value:  {0}
   */
  ERR_DELIVER_OTP_REQ_ERROR_PARSING_VALUE("Unable to decode the provided extended request as a deliver one-time password request because an error occurred while parsing the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a deliver one-time password request because the value sequence did not include an authentication ID.
   */
  ERR_DELIVER_OTP_REQ_NO_AUTHN_ID("Unable to decode the provided extended request as a deliver one-time password request because the value sequence did not include an authentication ID."),



  /**
   * Unable to decode the provided extended request as a deliver one-time password request because the value sequence did not include a static password.
   */
  ERR_DELIVER_OTP_REQ_NO_PW("Unable to decode the provided extended request as a deliver one-time password request because the value sequence did not include a static password."),



  /**
   * Unable to decode the provided extended request as a deliver one-time password request because it does not have a value.
   */
  ERR_DELIVER_OTP_REQ_NO_VALUE("Unable to decode the provided extended request as a deliver one-time password request because it does not have a value."),



  /**
   * Unable to decode the provided extended request as a deliver one-time password request because the value sequence includes an element with an unexpected BER type of {0}.
   */
  ERR_DELIVER_OTP_REQ_UNEXPECTED_ELEMENT_TYPE("Unable to decode the provided extended request as a deliver one-time password request because the value sequence includes an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided extended result as a deliver one-time password result because an error occurred while parsing the result value:  {0}
   */
  ERR_DELIVER_OTP_RES_ERROR_PARSING_VALUE("Unable to decode the provided extended result as a deliver one-time password result because an error occurred while parsing the result value:  {0}"),



  /**
   * Unable to decode the provided extended result as a deliver one-time password result because the value sequence did not include a delivery mechanism.
   */
  ERR_DELIVER_OTP_RES_NO_MECH("Unable to decode the provided extended result as a deliver one-time password result because the value sequence did not include a delivery mechanism."),



  /**
   * Unable to decode the provided extended result as a deliver one-time password result because the value sequence did not include a recipient DN.
   */
  ERR_DELIVER_OTP_RES_NO_RECIPIENT_DN("Unable to decode the provided extended result as a deliver one-time password result because the value sequence did not include a recipient DN."),



  /**
   * Unable to decode the provided extended result as a deliver one-time password result because the value sequence includes an element with an unexpected BER type of {0}.
   */
  ERR_DELIVER_OTP_RES_UNEXPECTED_ELEMENT_TYPE("Unable to decode the provided extended result as a deliver one-time password result because the value sequence includes an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a deliver password reset token request because an error was encountered while trying to parse the value:  {0}
   */
  ERR_DELIVER_PW_RESET_TOKEN_REQUEST_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a deliver password reset token request because an error was encountered while trying to parse the value:  {0}"),



  /**
   * Unable to decode the provided extended request as a deliver password reset token request because the provided request did not have a value.
   */
  ERR_DELIVER_PW_RESET_TOKEN_REQUEST_NO_VALUE("Unable to decode the provided extended request as a deliver password reset token request because the provided request did not have a value."),



  /**
   * Unable to decode the provided extended request as a deliver password reset token request because the value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_DELIVER_PW_RESET_TOKEN_REQUEST_UNEXPECTED_TYPE("Unable to decode the provided extended request as a deliver password reset token request because the value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended result as a deliver password reset token result because an error was encountered while trying to parse the value:  {0}
   */
  ERR_DELIVER_PW_RESET_TOKEN_RESULT_ERROR_DECODING_VALUE("Unable to decode the provided extended result as a deliver password reset token result because an error was encountered while trying to parse the value:  {0}"),



  /**
   * Unable to decode the provided extended result as a deliver password reset token result because the value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_DELIVER_PW_RESET_TOKEN_RESULT_UNEXPECTED_TYPE("Unable to decode the provided extended result as a deliver password reset token result because the value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a deliver single-use token request:  {0}
   */
  ERR_DELIVER_SINGLE_USE_TOKEN_REQUEST_CANNOT_DECODE("Unable to decode the provided extended request as a deliver single-use token request:  {0}"),



  /**
   * Unable to decode the provided extended request as a deliver single-use token request because the request does not have a value.
   */
  ERR_DELIVER_SINGLE_USE_TOKEN_REQUEST_NO_VALUE("Unable to decode the provided extended request as a deliver single-use token request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a deliver single-use token request because the request value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_DELIVER_SINGLE_USE_TOKEN_REQUEST_UNKNOWN_ELEMENT("Unable to decode the provided extended request as a deliver single-use token request because the request value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended result as a deliver single-use token result because an error was encountered while trying to parse the value:  {0}
   */
  ERR_DELIVER_SINGLE_USE_TOKEN_RESULT_ERROR_DECODING_VALUE("Unable to decode the provided extended result as a deliver single-use token result because an error was encountered while trying to parse the value:  {0}"),



  /**
   * Unable to decode the provided extended result as a deliver single-use token result because the value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_DELIVER_SINGLE_USE_TOKEN_RESULT_UNEXPECTED_TYPE("Unable to decode the provided extended result as a deliver single-use token result because the value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a delete notification destination request because the request does not have a value.
   */
  ERR_DEL_NOTIFICATION_DEST_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a delete notification destination request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a delete notification destination request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_DEL_NOTIFICATION_DEST_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a delete notification destination request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a delete notification subscription request because the request does not have a value.
   */
  ERR_DEL_NOTIFICATION_SUB_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a delete notification subscription request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a delete notification subscription request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_DEL_NOTIFICATION_SUB_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a delete notification subscription request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a deregister YubiKey OTP device request because an error was encountered while attempting to decode the value:  {0}
   */
  ERR_DEREGISTER_YUBIKEY_OTP_REQUEST_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a deregister YubiKey OTP device request because an error was encountered while attempting to decode the value:  {0}"),



  /**
   * Unable to decode the provided extended request as a deregister YubiKey OTP device request because the provided request does not have a value.
   */
  ERR_DEREGISTER_YUBIKEY_OTP_REQUEST_NO_VALUE("Unable to decode the provided extended request as a deregister YubiKey OTP device request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a deregister YubiKey OTP device request because the value sequence included an element with an unrecognized BER type of ''{0}''.
   */
  ERR_DEREGISTER_YUBIKEY_OTP_REQUEST_UNRECOGNIZED_TYPE("Unable to decode the provided extended request as a deregister YubiKey OTP device request because the value sequence included an element with an unrecognized BER type of ''{0}''."),



  /**
   * Unable to decode the provided ASN.1 element as a duration collect support data log capture window object because the element could not be decoded as an integer:  {0}
   */
  ERR_DURATION_CSD_LOG_WINDOW_CANNOT_DECODE("Unable to decode the provided ASN.1 element as a duration collect support data log capture window object because the element could not be decoded as an integer:  {0}"),



  /**
   * The end administrative session extended request had a value, but none was expected.
   */
  ERR_END_ADMIN_SESSION_REQUEST_HAS_VALUE("The end administrative session extended request had a value, but none was expected."),



  /**
   * The provided extended request cannot be decoded as an end interactive transaction request:  {0}
   */
  ERR_END_INT_TXN_REQUEST_CANNOT_DECODE("The provided extended request cannot be decoded as an end interactive transaction request:  {0}"),



  /**
   * The provided extended request cannot be decoded as an end interactive transaction request because the value contains an element with an invalid BER type of {0}.
   */
  ERR_END_INT_TXN_REQUEST_INVALID_TYPE("The provided extended request cannot be decoded as an end interactive transaction request because the value contains an element with an invalid BER type of {0}."),



  /**
   * The provided extended request cannot be decoded as an end interactive transaction request because the value sequence did not include a transaction ID.
   */
  ERR_END_INT_TXN_REQUEST_NO_TXN_ID("The provided extended request cannot be decoded as an end interactive transaction request because the value sequence did not include a transaction ID."),



  /**
   * The provided extended request cannot be decoded as an end interactive transaction request because it does not have a value.
   */
  ERR_END_INT_TXN_REQUEST_NO_VALUE("The provided extended request cannot be decoded as an end interactive transaction request because it does not have a value."),



  /**
   * Unable to decode an ASN.1 element as an end of changelog starting point because it has a nonzero-length value.
   */
  ERR_END_OF_CHANGELOG_STARTING_POINT_HAS_VALUE("Unable to decode an ASN.1 element as an end of changelog starting point because it has a nonzero-length value."),



  /**
   * The provided extended request cannot be decoded as an end transaction request because an error occurred while attempting to parse the value:  {0}
   */
  ERR_END_TXN_REQUEST_CANNOT_DECODE("The provided extended request cannot be decoded as an end transaction request because an error occurred while attempting to parse the value:  {0}"),



  /**
   * The provided extended request cannot be decoded as an end transaction request because it does not have a value.
   */
  ERR_END_TXN_REQUEST_NO_VALUE("The provided extended request cannot be decoded as an end transaction request because it does not have a value."),



  /**
   * Unable to decode the message ID from the end transaction value sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CANNOT_DECODE_MSGID("Unable to decode the message ID from the end transaction value sequence:  {0}"),



  /**
   * Unable to decode the controls element of an updateControls sequence as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROLS_ELEMENT_NOT_SEQUENCE("Unable to decode the controls element of an updateControls sequence as an ASN.1 sequence:  {0}"),



  /**
   * Unable to decode the updatesControls element in the end transaction value sequence as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROLS_NOT_SEQUENCE("Unable to decode the updatesControls element in the end transaction value sequence as an ASN.1 sequence:  {0}"),



  /**
   * Invalid number of elements in an updateControls element sequence (expected 2, got {0,number,0}).
   */
  ERR_END_TXN_RESPONSE_CONTROL_INVALID_ELEMENT_COUNT("Invalid number of elements in an updateControls element sequence (expected 2, got {0,number,0})."),



  /**
   * Unable to decode the message ID element of an updateControls sequence as an integer:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROL_MSGID_NOT_INT("Unable to decode the message ID element of an updateControls sequence as an integer:  {0}"),



  /**
   * Unable to decode an updateControls sequence element in the end transaction value as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_CONTROL_NOT_SEQUENCE("Unable to decode an updateControls sequence element in the end transaction value as an ASN.1 sequence:  {0}"),



  /**
   * Too many elements in the end transaction value sequence (expected 1 or 2, got {0,number,0}).
   */
  ERR_END_TXN_RESPONSE_INVALID_ELEMENT_COUNT("Too many elements in the end transaction value sequence (expected 1 or 2, got {0,number,0})."),



  /**
   * Unexpected element type {0} encountered in the end transaction value sequence.
   */
  ERR_END_TXN_RESPONSE_INVALID_TYPE("Unexpected element type {0} encountered in the end transaction value sequence."),



  /**
   * Cannot decode the end transaction value as an ASN.1 sequence:  {0}
   */
  ERR_END_TXN_RESPONSE_VALUE_NOT_SEQUENCE("Cannot decode the end transaction value as an ASN.1 sequence:  {0}"),



  /**
   * An unexpected error occurred while attempting to decode a generated password returned by the server:  {0}
   */
  ERR_GENERATED_PASSWORD_DECODING_ERROR("An unexpected error occurred while attempting to decode a generated password returned by the server:  {0}"),



  /**
   * An unexpected error occurred while attempting to decode the value of the provided extended request as a generate password request value:  {0}
   */
  ERR_GENERATE_PASSWORD_REQUEST_DECODING_ERROR("An unexpected error occurred while attempting to decode the value of the provided extended request as a generate password request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a generate password request because the value included invalid value {0,number,0} for the number of validation attempts to make.  The value must be greater than or equal to zero.
   */
  ERR_GENERATE_PASSWORD_REQUEST_INVALID_NUM_ATTEMPTS("Unable to decode the provided extended request as a generate password request because the value included invalid value {0,number,0} for the number of validation attempts to make.  The value must be greater than or equal to zero."),



  /**
   * Unable to decode the provided extended request as a generate password request because the value included invalid value {0,number,0} for the number of passwords to generate.  The value must be greater than or equal to one.
   */
  ERR_GENERATE_PASSWORD_REQUEST_INVALID_NUM_PASSWORDS("Unable to decode the provided extended request as a generate password request because the value included invalid value {0,number,0} for the number of passwords to generate.  The value must be greater than or equal to one."),



  /**
   * Unable to decode the provided extended request as a generate password request because the value included an unrecognized password policy selection type of {0}.
   */
  ERR_GENERATE_PASSWORD_REQUEST_UNSUPPORTED_SELECTION_TYPE("Unable to decode the provided extended request as a generate password request because the value included an unrecognized password policy selection type of {0}."),



  /**
   * Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was processed successfully, but the response value did not include any generated passwords.
   */
  ERR_GENERATE_PASSWORD_RESULT_DECODE_NO_PASSWORDS("Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was processed successfully, but the response value did not include any generated passwords."),



  /**
   * An unexpected error occurred while trying to decode the value of the provided extended operation as a generate password value:  {0}
   */
  ERR_GENERATE_PASSWORD_RESULT_DECODING_ERROR("An unexpected error occurred while trying to decode the value of the provided extended operation as a generate password value:  {0}"),



  /**
   * Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was not processed successfully, but the response included a value.
   */
  ERR_GENERATE_PASSWORD_RESULT_NON_SUCCESS_WITH_VALUE("Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was not processed successfully, but the response included a value."),



  /**
   * Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was processed successfully, but the response did not have a value.
   */
  ERR_GENERATE_PASSWORD_RESULT_SUCCESS_MISSING_VALUE("Unable to decode the provided extended result as a generate password result because the result code indicated that the operation was processed successfully, but the response did not have a value."),



  /**
   * Unable to decode the provided extended request as a generate TOTP shared secret request because an unexpected error was encountered while attempting to decode the value:  {0}
   */
  ERR_GEN_TOTP_SECRET_REQUEST_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a generate TOTP shared secret request because an unexpected error was encountered while attempting to decode the value:  {0}"),



  /**
   * Unable to decode the provided extended request as a generate TOTP shared secret request because the provided request had neither an authentication ID nor a static password.  At least one of these elements must be present.
   */
  ERR_GEN_TOTP_SECRET_REQUEST_NEITHER_AUTHN_ID_NOR_PW("Unable to decode the provided extended request as a generate TOTP shared secret request because the provided request had neither an authentication ID nor a static password.  At least one of these elements must be present."),



  /**
   * Unable to decode the provided extended request as a generate TOTP shared secret request because the provided request does not have a value.
   */
  ERR_GEN_TOTP_SECRET_REQUEST_NO_VALUE("Unable to decode the provided extended request as a generate TOTP shared secret request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a generate TOTP shared secret request because the value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_GEN_TOTP_SECRET_REQUEST_UNRECOGNIZED_TYPE("Unable to decode the provided extended request as a generate TOTP shared secret request because the value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended result as a generate TOTP shared secret result because an unexpected error was encountered while attempting to decode the value:  {0}
   */
  ERR_GEN_TOTP_SECRET_RESULT_ERROR_DECODING_VALUE("Unable to decode the provided extended result as a generate TOTP shared secret result because an unexpected error was encountered while attempting to decode the value:  {0}"),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended request as a get backup compatibility descriptor request value:  {0}
   */
  ERR_GET_BACKUP_COMPAT_REQUEST_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended request as a get backup compatibility descriptor request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a get backup compatibility descriptor request because the provided request does not have a value.
   */
  ERR_GET_BACKUP_COMPAT_REQUEST_NO_VALUE("Unable to decode the provided extended request as a get backup compatibility descriptor request because the provided request does not have a value."),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended result as a get backup compatibility descriptor result value:  {0}
   */
  ERR_GET_BACKUP_COMPAT_RESULT_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended result as a get backup compatibility descriptor result value:  {0}"),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because an error occurred while trying to decode the value sequence:  {0}
   */
  ERR_GET_CHANGELOG_BATCH_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a get changelog batch request because an error occurred while trying to decode the value sequence:  {0}"),



  /**
   * The get changelog batch extended request is not allowed to have a general-purpose intermediate response listener.  If an intermediate response listener is required, then a ChangelogBatchEntryListener should be used.
   */
  ERR_GET_CHANGELOG_BATCH_REQ_IR_LISTENER_NOT_ALLOWED("The get changelog batch extended request is not allowed to have a general-purpose intermediate response listener.  If an intermediate response listener is required, then a ChangelogBatchEntryListener should be used."),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because it does not have a value.
   */
  ERR_GET_CHANGELOG_BATCH_REQ_NO_VALUE("Unable to decode the provided extended request as a get changelog batch request because it does not have a value."),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because the value sequence had too few elements to form a valid request.
   */
  ERR_GET_CHANGELOG_BATCH_REQ_TOO_FEW_ELEMENTS("Unable to decode the provided extended request as a get changelog batch request because the value sequence had too few elements to form a valid request."),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because the value could not be parsed as an ASN.1 sequence:  {0}
   */
  ERR_GET_CHANGELOG_BATCH_REQ_VALUE_NOT_SEQUENCE("Unable to decode the provided extended request as a get changelog batch request because the value could not be parsed as an ASN.1 sequence:  {0}"),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because the value sequence included an unrecognized change type of {0}.
   */
  ERR_GET_CHANGELOG_BATCH_REQ_VALUE_UNRECOGNIZED_CT("Unable to decode the provided extended request as a get changelog batch request because the value sequence included an unrecognized change type of {0}."),



  /**
   * Unable to decode the provided extended request as a get changelog batch request because the value sequence included an element with an unrecognized type of {0}.
   */
  ERR_GET_CHANGELOG_BATCH_REQ_VALUE_UNRECOGNIZED_TYPE("Unable to decode the provided extended request as a get changelog batch request because the value sequence included an element with an unrecognized type of {0}."),



  /**
   * An error occurred while attempting to decode the provided extended result as a get changelog batch extended result:  {0}
   */
  ERR_GET_CHANGELOG_BATCH_RES_ERROR_PARSING_VALUE("An error occurred while attempting to decode the provided extended result as a get changelog batch extended result:  {0}"),



  /**
   * Unable to decode the provided extended result as a get changelog batch extended result because the value sequence did not include an element indicating whether there may be more changes which are immediately available.
   */
  ERR_GET_CHANGELOG_BATCH_RES_MISSING_MORE("Unable to decode the provided extended result as a get changelog batch extended result because the value sequence did not include an element indicating whether there may be more changes which are immediately available."),



  /**
   * Unable to decode the provided extended result as a get changelog batch extended result because the value sequence contained an ASN.1 element with an unexpected type of {0}.
   */
  ERR_GET_CHANGELOG_BATCH_RES_UNEXPECTED_VALUE_ELEMENT("Unable to decode the provided extended result as a get changelog batch extended result because the value sequence contained an ASN.1 element with an unexpected type of {0}."),



  /**
   * Unable to decode the provided extended result as a get changelog batch extended result because the value could not be parsed as an ASN.1 sequence:  {0}
   */
  ERR_GET_CHANGELOG_BATCH_RES_VALUE_NOT_SEQUENCE("Unable to decode the provided extended result as a get changelog batch extended result because the value could not be parsed as an ASN.1 sequence:  {0}"),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended request as a get configuration request value:  {0}
   */
  ERR_GET_CONFIG_REQUEST_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended request as a get configuration request value:  {0}"),



  /**
   * The encoded request value does not include the required archived configuration file name element.
   */
  ERR_GET_CONFIG_REQUEST_NO_ARCHIVED_FILE_NAME("The encoded request value does not include the required archived configuration file name element."),



  /**
   * The encoded request value does not include the required configuration type element.
   */
  ERR_GET_CONFIG_REQUEST_NO_CONFIG_TYPE("The encoded request value does not include the required configuration type element."),



  /**
   * Unable to decode the provided extended request as a get configuration request because the provided request does not have a value.
   */
  ERR_GET_CONFIG_REQUEST_NO_VALUE("Unable to decode the provided extended request as a get configuration request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a get configuration request because the value sequence includes an element with an unexpected configuration type BER value of {0}.
   */
  ERR_GET_CONFIG_REQUEST_UNEXPECTED_CONFIG_TYPE("Unable to decode the provided extended request as a get configuration request because the value sequence includes an element with an unexpected configuration type BER value of {0}."),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended result as a get configuration result value:  {0}
   */
  ERR_GET_CONFIG_RESULT_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended result as a get configuration result value:  {0}"),



  /**
   * Unable to decode the provided extended result as a get configuration result because the value sequence referenced an unrecognized configuration type value of {0}.
   */
  ERR_GET_CONFIG_RESULT_INVALID_CONFIG_TYPE("Unable to decode the provided extended result as a get configuration result because the value sequence referenced an unrecognized configuration type value of {0}."),



  /**
   * The provided extended cannot request be decoded as a get connection ID request because it has a value.
   */
  ERR_GET_CONN_ID_REQUEST_HAS_VALUE("The provided extended cannot request be decoded as a get connection ID request because it has a value."),



  /**
   * Unable to decode the provided extended result as a get connection ID result because the value could not be decoded as an integer.
   */
  ERR_GET_CONN_ID_RESPONSE_VALUE_NOT_INT("Unable to decode the provided extended result as a get connection ID result because the value could not be decoded as an integer."),



  /**
   * An error was encountered while attempting to decode an extended request as a get password quality requirements request:  {0}
   */
  ERR_GET_PW_QUALITY_REQS_REQUEST_CANNOT_DECODE("An error was encountered while attempting to decode an extended request as a get password quality requirements request:  {0}"),



  /**
   * Unable to decoded the provided extended request as a get password quality request because the extended request does not have a value.
   */
  ERR_GET_PW_QUALITY_REQS_REQUEST_NO_VALUE("Unable to decoded the provided extended request as a get password quality request because the extended request does not have a value."),



  /**
   * Unable to decode the provided extended request as a get password quality request because the value sequence included an unrecognized target type of {0}.
   */
  ERR_GET_PW_QUALITY_REQS_REQUEST_UNKNOWN_TARGET_TYPE("Unable to decode the provided extended request as a get password quality request because the value sequence included an unrecognized target type of {0}."),



  /**
   * Unable to decode the provided extended result as a get password quality requirements result because an error occurred while attempting to decode the value {0}
   */
  ERR_GET_PW_QUALITY_REQS_RESULT_CANNOT_DECODE("Unable to decode the provided extended result as a get password quality requirements result because an error occurred while attempting to decode the value {0}"),



  /**
   * Unable to decode the provided extended request as a get subtree accessibility request because it has a value.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_REQUEST_HAS_VALUE("Unable to decode the provided extended request as a get subtree accessibility request because it has a value."),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result:  {0}
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_DECODE_ERROR("Unable to decode the provided extended result as a get subtree accessibility result:  {0}"),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required subtree base DN.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_MISSING_BASE("Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required subtree base DN."),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required accessibility state.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_MISSING_STATE("Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required accessibility state."),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required effective time.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_MISSING_TIME("Unable to decode the provided extended result as a get subtree accessibility result because an accessibility restriction definition was missing the required effective time."),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result because a restriction element included an unrecognized accessibility state value of {0}.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_UNEXPECTED_STATE("Unable to decode the provided extended result as a get subtree accessibility result because a restriction element included an unrecognized accessibility state value of {0}."),



  /**
   * Unable to decode the provided extended result as a get subtree accessibility result because the value sequence contained an element with an unexpected BER type of {0}.
   */
  ERR_GET_SUBTREE_ACCESSIBILITY_RESULT_UNEXPECTED_TYPE("Unable to decode the provided extended result as a get subtree accessibility result because the value sequence contained an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a get supported OTP delivery mechanisms request:  {0}
   */
  ERR_GET_SUPPORTED_OTP_MECH_REQUEST_CANNOT_DECODE("Unable to decode the provided extended request as a get supported OTP delivery mechanisms request:  {0}"),



  /**
   * Cannot decode the provided extended request as a get supported OTP delivery mechanisms request because the request does not have a value.
   */
  ERR_GET_SUPPORTED_OTP_MECH_REQUEST_NO_VALUE("Cannot decode the provided extended request as a get supported OTP delivery mechanisms request because the request does not have a value."),



  /**
   * Unable to decode the provided extended result as a get supported OTP delivery mechanisms result:  {0}
   */
  ERR_GET_SUPPORTED_OTP_MECH_RESULT_CANNOT_DECODE("Unable to decode the provided extended result as a get supported OTP delivery mechanisms result:  {0}"),



  /**
   * Unable to decode the provided extended result as a get supported OTP delivery mechanisms result because a value sequence included an element with unexpected BER type ''{0}''.
   */
  ERR_GET_SUPPORTED_OTP_MECH_RESULT_UNKNOWN_ELEMENT("Unable to decode the provided extended result as a get supported OTP delivery mechanisms result because a value sequence included an element with unexpected BER type ''{0}''."),



  /**
   * Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because the sequence contained an element with an unexpected BER type of {0}.
   */
  ERR_HT_SIZE_CSD_LOG_CAPTURE_WINDOW_INVALID_ELEMENT_TYPE("Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because the sequence contained an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because it contained an invalid head size value of {0,number,0}.
   */
  ERR_HT_SIZE_CSD_LOG_CAPTURE_WINDOW_INVALID_HEAD_SIZE("Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because it contained an invalid head size value of {0,number,0}."),



  /**
   * Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because it contained an invalid tail size value of {0,number,0}.
   */
  ERR_HT_SIZE_CSD_LOG_CAPTURE_WINDOW_INVALID_TAIL_SIZE("Unable to decode the provided ASN.1 element as a head and tail size collect support data log capture window because it contained an invalid tail size value of {0,number,0}."),



  /**
   * An unexpected error occurred while attempting to decode the provided ASN.1 element as a head and tail size collect support data log capture window:  {0}
   */
  ERR_HT_SIZE_CSD_LOG_WINDOW_CANNOT_DECODE("An unexpected error occurred while attempting to decode the provided ASN.1 element as a head and tail size collect support data log capture window:  {0}"),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended request as an identify backup compatibility problems request value:  {0}
   */
  ERR_IDENTIFY_BACKUP_COMPAT_PROBLEMS_REQUEST_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended request as an identify backup compatibility problems request value:  {0}"),



  /**
   * Unable to decode the provided extended request as an identify backup compatibility problems request because the provided request does not have a value.
   */
  ERR_IDENTIFY_BACKUP_COMPAT_PROBLEMS_REQUEST_NO_VALUE("Unable to decode the provided extended request as an identify backup compatibility problems request because the provided request does not have a value."),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended result as an identify backup compatibility problems result value:  {0}
   */
  ERR_IDENTIFY_BACKUP_COMPAT_PROBLEMS_RESULT_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended result as an identify backup compatibility problems result value:  {0}"),



  /**
   * Unable to decode the provided extended result as an identify backup compatibility problems result because the value sequence includes an element with an unexpected BER type of {0}.
   */
  ERR_IDENTIFY_BACKUP_COMPAT_PROBLEMS_RESULT_UNEXPECTED_TYPE("Unable to decode the provided extended result as an identify backup compatibility problems result because the value sequence includes an element with an unexpected BER type of {0}."),



  /**
   * An error was encountered while attempting to decode the ignore attributes get changelog batch change selection criteria element:  {0}
   */
  ERR_IGNORE_ATTRS_CHANGE_SELECTION_CRITERIA_DECODE_ERROR("An error was encountered while attempting to decode the ignore attributes get changelog batch change selection criteria element:  {0}"),



  /**
   * Unable to decode the provided extended request as a list configurations request because the provided request includes a value but none was expected.
   */
  ERR_LIST_CONFIGS_REQUEST_HAS_VALUE("Unable to decode the provided extended request as a list configurations request because the provided request includes a value but none was expected."),



  /**
   * An unexpected problem was encountered while trying to parse the value of the provided extended result as a list configurations result value:  {0}
   */
  ERR_LIST_CONFIGS_RESULT_ERROR_PARSING_VALUE("An unexpected problem was encountered while trying to parse the value of the provided extended result as a list configurations result value:  {0}"),



  /**
   * The encoded result value does not include the required active configuration file name element.
   */
  ERR_LIST_CONFIGS_RESULT_NO_ACTIVE_CONFIG("The encoded result value does not include the required active configuration file name element."),



  /**
   * Unable to decode the provided extended result as a list configurations result because the value sequence includes an element with an unexpected BER type of {0}.
   */
  ERR_LIST_CONFIGS_RESULT_UNEXPECTED_ELEMENT_TYPE("Unable to decode the provided extended result as a list configurations result because the value sequence includes an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a list notification subscriptions request because the request does not have a value.
   */
  ERR_LIST_NOTIFICATION_SUBS_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a list notification subscriptions request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a list notification subscriptions request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_LIST_NOTIFICATION_SUBS_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a list notification subscriptions request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended result as a list notification subscriptions result because an error occurred while attempting to decode the result value:  {0}
   */
  ERR_LIST_NOTIFICATION_SUBS_RESULT_CANNOT_DECODE_VALUE("Unable to decode the provided extended result as a list notification subscriptions result because an error occurred while attempting to decode the result value:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a missing changelog batch entries intermediate response because the value sequence included an element with an unexpected type of {0}.
   */
  ERR_MISSING_CHANGELOG_ENTRIES_IR_UNEXPECTED_VALUE_TYPE("Unable to decode the provided intermediate response as a missing changelog batch entries intermediate response because the value sequence included an element with an unexpected type of {0}."),



  /**
   * Unable to decode the provided intermediate response as a missing changelog batch entries intermediate response because the value could not be parsed as an ASN.1 sequence:  {0}
   */
  ERR_MISSING_CHANGELOG_ENTRIES_IR_VALUE_NOT_SEQUENCE("Unable to decode the provided intermediate response as a missing changelog batch entries intermediate response because the value could not be parsed as an ASN.1 sequence:  {0}"),



  /**
   * An error occurred while attempting to decode a generic extended request as a multi-update request:  {0}
   */
  ERR_MULTI_UPDATE_REQUEST_CANNOT_DECODE_VALUE("An error occurred while attempting to decode a generic extended request as a multi-update request:  {0}"),



  /**
   * Unable to decode a generic extended request as a multi-update request because it has an invalid error behavior value of {0}.
   */
  ERR_MULTI_UPDATE_REQUEST_INVALID_ERROR_BEHAVIOR("Unable to decode a generic extended request as a multi-update request because it has an invalid error behavior value of {0}."),



  /**
   * Unable to decode a generic extended request as a multi-update request because the set of operation requests included an invalid operation type of {0}.
   */
  ERR_MULTI_UPDATE_REQUEST_INVALID_OP_TYPE("Unable to decode a generic extended request as a multi-update request because the set of operation requests included an invalid operation type of {0}."),



  /**
   * Unable to create a multi-update extended request with an operation of type {0}.
   */
  ERR_MULTI_UPDATE_REQUEST_INVALID_REQUEST_TYPE("Unable to create a multi-update extended request with an operation of type {0}."),



  /**
   * Unable to decode a generic extended request as a multi-update request because the extended request did not have a value.
   */
  ERR_MULTI_UPDATE_REQUEST_NO_VALUE("Unable to decode a generic extended request as a multi-update request because the extended request did not have a value."),



  /**
   * Unable to decode the provided extended result as a multi-update extended result because an error occurred while attempting to decode the value:  {0}
   */
  ERR_MULTI_UPDATE_RESULT_CANNOT_DECODE_VALUE("Unable to decode the provided extended result as a multi-update extended result because an error occurred while attempting to decode the value:  {0}"),



  /**
   * Unable to decode the provided extended result as a multi-update extended result because the value included result information for unexpected operation type {0}.
   */
  ERR_MULTI_UPDATE_RESULT_DECODE_INVALID_OP_TYPE("Unable to decode the provided extended result as a multi-update extended result because the value included result information for unexpected operation type {0}."),



  /**
   * Unable to decode the provided extended result as a multi-update extended result because it had an invalid changesApplied value of {0}.
   */
  ERR_MULTI_UPDATE_RESULT_INVALID_CHANGES_APPLIED("Unable to decode the provided extended result as a multi-update extended result because it had an invalid changesApplied value of {0}."),



  /**
   * Multi-update extended responses are not allowed to include results for operations of type {0}.
   */
  ERR_MULTI_UPDATE_RESULT_INVALID_OP_TYPE("Multi-update extended responses are not allowed to include results for operations of type {0}."),



  /**
   * An error was encountered while attempting to decode the notification destination get changelog batch change selection criteria element:  {0}
   */
  ERR_NOT_DEST_CHANGE_SELECTION_CRITERIA_DECODE_ERROR("An error was encountered while attempting to decode the notification destination get changelog batch change selection criteria element:  {0}"),



  /**
   * Unable to decode string ''{0}'' as an account usability error:  {1}
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_ERROR_CANNOT_DECODE("Unable to decode string ''{0}'' as an account usability error:  {1}"),



  /**
   * There was no ''code'' element containing the integer value for the error.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_ERROR_NO_CODE("There was no ''code'' element containing the integer value for the error."),



  /**
   * There was no ''name'' element containing the name for the error.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_ERROR_NO_NAME("There was no ''name'' element containing the name for the error."),



  /**
   * Unable to decode string ''{0}'' as an account usability notice:  {1}
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_NOTICE_CANNOT_DECODE("Unable to decode string ''{0}'' as an account usability notice:  {1}"),



  /**
   * There was no ''code'' element containing the integer value for the notice.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_NOTICE_NO_CODE("There was no ''code'' element containing the integer value for the notice."),



  /**
   * There was no ''name'' element containing the name for the notice.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_NOTICE_NO_NAME("There was no ''name'' element containing the name for the notice."),



  /**
   * Unable to decode string ''{0}'' as an account usability warning:  {1}
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_WARNING_CANNOT_DECODE("Unable to decode string ''{0}'' as an account usability warning:  {1}"),



  /**
   * There was no ''code'' element containing the integer value for the warning.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_WARNING_NO_CODE("There was no ''code'' element containing the integer value for the warning."),



  /**
   * There was no ''name'' element containing the name for the warning.
   */
  ERR_PWP_STATE_ACCOUNT_USABILITY_WARNING_NO_NAME("There was no ''name'' element containing the name for the warning."),



  /**
   * Unable to decode the provided ASN.1 element as a password policy state operation because an error occurred while decoding the set of values:  {0}
   */
  ERR_PWP_STATE_CANNOT_DECODE_VALUES("Unable to decode the provided ASN.1 element as a password policy state operation because an error occurred while decoding the set of values:  {0}"),



  /**
   * Unable to decode the provided ASN.1 element as a password policy state operation because it could not be decoded as a sequence:  {0}
   */
  ERR_PWP_STATE_ELEMENT_NOT_SEQUENCE("Unable to decode the provided ASN.1 element as a password policy state operation because it could not be decoded as a sequence:  {0}"),



  /**
   * The password policy state operation had an invalid number of values for a boolean (expected 1, got {0,number,0}).
   */
  ERR_PWP_STATE_INVALID_BOOLEAN_VALUE_COUNT("The password policy state operation had an invalid number of values for a boolean (expected 1, got {0,number,0})."),



  /**
   * Unable to decode the provided ASN.1 element as a password policy state operation because the value sequence had an invalid number of elements (expected 1 or 2, got {0,number,0}).
   */
  ERR_PWP_STATE_INVALID_ELEMENT_COUNT("Unable to decode the provided ASN.1 element as a password policy state operation because the value sequence had an invalid number of elements (expected 1 or 2, got {0,number,0})."),



  /**
   * The password policy state operation did not have any values.
   */
  ERR_PWP_STATE_NO_VALUES("The password policy state operation did not have any values."),



  /**
   * Unable to decode the provided ASN.1 element as a password policy state operation because the op type could not be decoded as an integer:  {0}
   */
  ERR_PWP_STATE_OP_TYPE_NOT_INTEGER("Unable to decode the provided ASN.1 element as a password policy state operation because the op type could not be decoded as an integer:  {0}"),



  /**
   * Unable to decode the provided extended request as a password policy state request because an error occurred while attempting to decode the operations:  {0}
   */
  ERR_PWP_STATE_REQUEST_CANNOT_DECODE_OPS("Unable to decode the provided extended request as a password policy state request because an error occurred while attempting to decode the operations:  {0}"),



  /**
   * Unable to decode the provided extended request as a password policy state request because the value sequence contained an invalid number of elements (expected 1 or 2, but found {0,number,0}).
   */
  ERR_PWP_STATE_REQUEST_INVALID_ELEMENT_COUNT("Unable to decode the provided extended request as a password policy state request because the value sequence contained an invalid number of elements (expected 1 or 2, but found {0,number,0})."),



  /**
   * Unable to decode the provided extended request as a password policy state request because it does not have a value.
   */
  ERR_PWP_STATE_REQUEST_NO_VALUE("Unable to decode the provided extended request as a password policy state request because it does not have a value."),



  /**
   * Unable to decode the provided extended request as a password policy state request because the value element could not be decoded as an ASN.1 sequence:  {0}
   */
  ERR_PWP_STATE_REQUEST_VALUE_NOT_SEQUENCE("Unable to decode the provided extended request as a password policy state request because the value element could not be decoded as an ASN.1 sequence:  {0}"),



  /**
   * Unable to decode the provided extended response as a password policy state response because an error occurred while attempting to decode the operations:  {0}
   */
  ERR_PWP_STATE_RESPONSE_CANNOT_DECODE_OPS("Unable to decode the provided extended response as a password policy state response because an error occurred while attempting to decode the operations:  {0}"),



  /**
   * Unable to decode the provided extended response as a password policy state response because the value sequence contained an invalid number of elements (expected 1 or 2, but found {0,number,0}).
   */
  ERR_PWP_STATE_RESPONSE_INVALID_ELEMENT_COUNT("Unable to decode the provided extended response as a password policy state response because the value sequence contained an invalid number of elements (expected 1 or 2, but found {0,number,0})."),



  /**
   * The specified password policy operation was not included in the response.
   */
  ERR_PWP_STATE_RESPONSE_NO_SUCH_OPERATION("The specified password policy operation was not included in the response."),



  /**
   * Unable to decode the provided extended response as a password policy state response because the value element could not be decoded as an ASN.1 sequence:  {0}
   */
  ERR_PWP_STATE_RESPONSE_VALUE_NOT_SEQUENCE("Unable to decode the provided extended response as a password policy state response because the value element could not be decoded as an ASN.1 sequence:  {0}"),



  /**
   * The value ''{0}'' could not be decoded as a boolean value.
   */
  ERR_PWP_STATE_VALUE_NOT_BOOLEAN("The value ''{0}'' could not be decoded as a boolean value."),



  /**
   * An error occurred while attempting to decode the provided ASN.1 element as a password quality requirement:  {0}
   */
  ERR_PW_QUALITY_REQ_DECODE_ERROR("An error occurred while attempting to decode the provided ASN.1 element as a password quality requirement:  {0}"),



  /**
   * Unable to decode the provided ASN.1 element as a password quality requirement object because the client-side validation info sequence included an element with an unexpected BER type of {0}.
   */
  ERR_PW_QUALITY_REQ_INVALID_CSV_ELEMENT_TYPE("Unable to decode the provided ASN.1 element as a password quality requirement object because the client-side validation info sequence included an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided ASN.1 element as a password quality requirement object because the requirement sequence included an element with an unexpected BER type of {0}.
   */
  ERR_PW_QUALITY_REQ_INVALID_REQ_ELEMENT_TYPE("Unable to decode the provided ASN.1 element as a password quality requirement object because the requirement sequence included an element with an unexpected BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a register YubiKey OTP device request because an error was encountered while attempting to decode the value:  {0}
   */
  ERR_REGISTER_YUBIKEY_OTP_REQUEST_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a register YubiKey OTP device request because an error was encountered while attempting to decode the value:  {0}"),



  /**
   * Unable to decode the provided extended request as a register YubiKey OTP device request because the value sequence was missing the required YubiKey-generated one-time password.
   */
  ERR_REGISTER_YUBIKEY_OTP_REQUEST_MISSING_OTP("Unable to decode the provided extended request as a register YubiKey OTP device request because the value sequence was missing the required YubiKey-generated one-time password."),



  /**
   * Unable to decode the provided extended request as a register YubiKey OTP device request because the provided request does not have a value.
   */
  ERR_REGISTER_YUBIKEY_OTP_REQUEST_NO_VALUE("Unable to decode the provided extended request as a register YubiKey OTP device request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a register YubiKey OTP device request because the value sequence included an element with an unrecognized BER type of ''{0}''.
   */
  ERR_REGISTER_YUBIKEY_OTP_REQUEST_UNRECOGNIZED_TYPE("Unable to decode the provided extended request as a register YubiKey OTP device request because the value sequence included an element with an unrecognized BER type of ''{0}''."),



  /**
   * Unable to decode the provided extended request as a revoke TOTP shared secret request because an unexpected error was encountered while attempting to decode the value:  {0}
   */
  ERR_REVOKE_TOTP_SECRET_REQUEST_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a revoke TOTP shared secret request because an unexpected error was encountered while attempting to decode the value:  {0}"),



  /**
   * Unable to decode the provided extended request as a revoke TOTP shared secret request because the value sequence did not include any of the authentication ID, static password, or TOTP shared secret elements.  At least one of these elements must be present in the request (and if only the authentication ID is provided, then the underlying connection must be authenticated as a user with the password-reset privilege).
   */
  ERR_REVOKE_TOTP_SECRET_REQUEST_NO_AUTHN_ID_OR_PW_OR_SECRET("Unable to decode the provided extended request as a revoke TOTP shared secret request because the value sequence did not include any of the authentication ID, static password, or TOTP shared secret elements.  At least one of these elements must be present in the request (and if only the authentication ID is provided, then the underlying connection must be authenticated as a user with the password-reset privilege)."),



  /**
   * Unable to decode the provided extended request as a revoke TOTP shared secret request because the provided request does not have a value.
   */
  ERR_REVOKE_TOTP_SECRET_REQUEST_NO_VALUE("Unable to decode the provided extended request as a revoke TOTP shared secret request because the provided request does not have a value."),



  /**
   * Unable to decode the provided extended request as a revoke TOTP shared secret request because the value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_REVOKE_TOTP_SECRET_REQUEST_UNRECOGNIZED_TYPE("Unable to decode the provided extended request as a revoke TOTP shared secret request because the value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a set notification destination request because the request does not have a value.
   */
  ERR_SET_NOTIFICATION_DEST_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a set notification destination request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a set notification destination request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_SET_NOTIFICATION_DEST_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a set notification destination request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * Unable to decode the provided extended request as a set notification destination request because the destination details change type element had an invalid integer value of {0}.
   */
  ERR_SET_NOTIFICATION_DEST_REQ_INVALID_CT("Unable to decode the provided extended request as a set notification destination request because the destination details change type element had an invalid integer value of {0}."),



  /**
   * Unable to decode the provided extended request as a set notification destination request because the value sequence had an element with an unrecognized BER type of ''{0}''.
   */
  ERR_SET_NOTIFICATION_DEST_REQ_INVALID_ELEMENT_TYPE("Unable to decode the provided extended request as a set notification destination request because the value sequence had an element with an unrecognized BER type of ''{0}''."),



  /**
   * Unable to decode the provided extended request as a set notification subscription request because the request does not have a value.
   */
  ERR_SET_NOTIFICATION_SUB_REQ_DECODE_NO_VALUE("Unable to decode the provided extended request as a set notification subscription request because the request does not have a value."),



  /**
   * Unable to decode the provided extended request as a set notification subscription request because an error occurred while attempting to decode the request value:  {0}
   */
  ERR_SET_NOTIFICATION_SUB_REQ_ERROR_DECODING_VALUE("Unable to decode the provided extended request as a set notification subscription request because an error occurred while attempting to decode the request value:  {0}"),



  /**
   * An error occurred while trying to decode the value of provided extended request for a set subtree accessibility request:  {0}
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_CANNOT_DECODE("An error occurred while trying to decode the value of provided extended request for a set subtree accessibility request:  {0}"),



  /**
   * Unrecognized subtree accessibility state value {0} contained in the set subtree accessibility extended request.
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_INVALID_ACCESSIBILITY_STATE("Unrecognized subtree accessibility state value {0} contained in the set subtree accessibility extended request."),



  /**
   * Unable to decode the provided extended request as a set subtree accessibility request because the value sequence included an element with an unexpected type of {0}.
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_INVALID_ELEMENT_TYPE("Unable to decode the provided extended request as a set subtree accessibility request because the value sequence included an element with an unexpected type of {0}."),



  /**
   * The set subtree accessibility request was missing a required bypass user DN element for accessibility state {0}.
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_MISSING_BYPASS_DN("The set subtree accessibility request was missing a required bypass user DN element for accessibility state {0}."),



  /**
   * Unable to decode the provided extended request as a set subtree accessibility request because there was no extended request value.
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_NO_VALUE("Unable to decode the provided extended request as a set subtree accessibility request because there was no extended request value."),



  /**
   * The set subtree accessibility request included a bypass user DN element when none was allowed for accessibility state {0}.
   */
  ERR_SET_SUBTREE_ACCESSIBILITY_UNEXPECTED_BYPASS_DN("The set subtree accessibility request included a bypass user DN element when none was allowed for accessibility state {0}."),



  /**
   * An error occurred while attempting to decode the value of the start administrative session extended request:  {0}
   */
  ERR_START_ADMIN_SESSION_REQUEST_ERROR_DECODING_VALUE("An error occurred while attempting to decode the value of the start administrative session extended request:  {0}"),



  /**
   * The start administrative session extended request did not include a value.
   */
  ERR_START_ADMIN_SESSION_REQUEST_NO_VALUE("The start administrative session extended request did not include a value."),



  /**
   * The start administrative session extended request value sequence included an element with an unrecognized BER type of {0}.
   */
  ERR_START_ADMIN_SESSION_REQUEST_UNKNOWN_VALUE_ELEMENT_TYPE("The start administrative session extended request value sequence included an element with an unrecognized BER type of {0}."),



  /**
   * The provided extended request cannot be decoded as a start interactive transaction request because the value sequence contained an element with an invalid BER type of {0}.
   */
  ERR_START_INT_TXN_REQUEST_INVALID_ELEMENT("The provided extended request cannot be decoded as a start interactive transaction request because the value sequence contained an element with an invalid BER type of {0}."),



  /**
   * The provided extended request cannot be decoded as a start interactive transaction request because the request value could not be parsed as a sequence:  {0}
   */
  ERR_START_INT_TXN_REQUEST_VALUE_NOT_SEQUENCE("The provided extended request cannot be decoded as a start interactive transaction request because the request value could not be parsed as a sequence:  {0}"),



  /**
   * The provided extended result cannot be decoded as a start interactive transaction result because the baseDNs element of the result value could not be parsed as a sequence:  {0}
   */
  ERR_START_INT_TXN_RESULT_BASE_DNS_NOT_SEQUENCE("The provided extended result cannot be decoded as a start interactive transaction result because the baseDNs element of the result value could not be parsed as a sequence:  {0}"),



  /**
   * The provided extended result cannot be decoded as a start interactive transaction result because the value sequence contained an element with an invalid BER type of {0}.
   */
  ERR_START_INT_TXN_RESULT_INVALID_ELEMENT("The provided extended result cannot be decoded as a start interactive transaction result because the value sequence contained an element with an invalid BER type of {0}."),



  /**
   * The provided extended result cannot be decoded as a start interactive transaction result because the value did not include a transaction ID.
   */
  ERR_START_INT_TXN_RESULT_NO_TXN_ID("The provided extended result cannot be decoded as a start interactive transaction result because the value did not include a transaction ID."),



  /**
   * The provided extended result cannot be decoded as a start interactive transaction result because the result value could not be parsed as a sequence:  {0}
   */
  ERR_START_INT_TXN_RESULT_VALUE_NOT_SEQUENCE("The provided extended result cannot be decoded as a start interactive transaction result because the result value could not be parsed as a sequence:  {0}"),



  /**
   * The provided extended cannot request be decoded as a start transaction request because it has a value.
   */
  ERR_START_TXN_REQUEST_HAS_VALUE("The provided extended cannot request be decoded as a start transaction request because it has a value."),



  /**
   * An error occurred while attempting to decode a stream directory values extended request:  {0}
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_CANNOT_DECODE("An error occurred while attempting to decode a stream directory values extended request:  {0}"),



  /**
   * Unable to decode the provided extended request as a stream directory values request because the includeDNs sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_INVALID_INCLUDE_DNS_TYPE("Unable to decode the provided extended request as a stream directory values request because the includeDNs sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a stream directory values request because it included an invalid entry DN scope value of {0}.
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_INVALID_SCOPE("Unable to decode the provided extended request as a stream directory values request because it included an invalid entry DN scope value of {0}."),



  /**
   * Unable to decode the provided extended request as a stream directory values request because the request sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_INVALID_SEQUENCE_TYPE("Unable to decode the provided extended request as a stream directory values request because the request sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a stream directory values request because the value sequence did not include a base DN element.
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_NO_BASE_DN("Unable to decode the provided extended request as a stream directory values request because the value sequence did not include a base DN element."),



  /**
   * The stream directory values extended request cannot be parsed because it does not have a value.
   */
  ERR_STREAM_DIRECTORY_VALUES_REQUEST_NO_VALUE("The stream directory values extended request cannot be parsed because it does not have a value."),



  /**
   * An error occurred while attempting to decode a stream directory values intermediate response:  {0}
   */
  ERR_STREAM_DIRECTORY_VALUES_RESPONSE_CANNOT_DECODE("An error occurred while attempting to decode a stream directory values intermediate response:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence had an invalid value {0} for the result element.
   */
  ERR_STREAM_DIRECTORY_VALUES_RESPONSE_INVALID_RESULT("Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence had an invalid value {0} for the result element."),



  /**
   * Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_DIRECTORY_VALUES_RESPONSE_INVALID_SEQUENCE_TYPE("Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence did not include a result element.
   */
  ERR_STREAM_DIRECTORY_VALUES_RESPONSE_NO_RESULT("Unable to decode the provided intermediate response as a stream directory values intermediate response because the response sequence did not include a result element."),



  /**
   * The stream directory values intermediate response cannot be parsed because it does not have a value.
   */
  ERR_STREAM_DIRECTORY_VALUES_RESPONSE_NO_VALUE("The stream directory values intermediate response cannot be parsed because it does not have a value."),



  /**
   * Unable to decode a backend set config element from the stream proxy values extended request:  {0}
   */
  ERR_STREAM_PROXY_VALUES_BACKEND_SET_CANNOT_DECODE("Unable to decode a backend set config element from the stream proxy values extended request:  {0}"),



  /**
   * Unable to decode a backend set value element from the stream proxy values intermediate response:  {0}
   */
  ERR_STREAM_PROXY_VALUES_BACKEND_SET_VALUE_CANNOT_DECODE("Unable to decode a backend set value element from the stream proxy values intermediate response:  {0}"),



  /**
   * An error occurred while attempting to decode a stream proxy values extended request:  {0}
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_CANNOT_DECODE("An error occurred while attempting to decode a stream proxy values extended request:  {0}"),



  /**
   * Unable to decode the provided extended request as a stream proxy values request because the includeDNs sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_INVALID_INCLUDE_DNS_TYPE("Unable to decode the provided extended request as a stream proxy values request because the includeDNs sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a stream proxy values request because it included an invalid entry DN scope value of {0}.
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_INVALID_SCOPE("Unable to decode the provided extended request as a stream proxy values request because it included an invalid entry DN scope value of {0}."),



  /**
   * Unable to decode the provided extended request as a stream proxy values request because the request sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_INVALID_SEQUENCE_TYPE("Unable to decode the provided extended request as a stream proxy values request because the request sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided extended request as a stream proxy values request because the value sequence did not include a base DN element.
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_NO_BASE_DN("Unable to decode the provided extended request as a stream proxy values request because the value sequence did not include a base DN element."),



  /**
   * The stream proxy values extended request cannot be parsed because it does not have a value.
   */
  ERR_STREAM_PROXY_VALUES_REQUEST_NO_VALUE("The stream proxy values extended request cannot be parsed because it does not have a value."),



  /**
   * An error occurred while attempting to decode a stream proxy values intermediate response:  {0}
   */
  ERR_STREAM_PROXY_VALUES_RESPONSE_CANNOT_DECODE("An error occurred while attempting to decode a stream proxy values intermediate response:  {0}"),



  /**
   * Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence had an invalid value {0} for the result element.
   */
  ERR_STREAM_PROXY_VALUES_RESPONSE_INVALID_RESULT("Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence had an invalid value {0} for the result element."),



  /**
   * Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence included element with an invalid BER type of {0}.
   */
  ERR_STREAM_PROXY_VALUES_RESPONSE_INVALID_SEQUENCE_TYPE("Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence included element with an invalid BER type of {0}."),



  /**
   * Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence did not include a result element.
   */
  ERR_STREAM_PROXY_VALUES_RESPONSE_NO_RESULT("Unable to decode the provided intermediate response as a stream proxy values intermediate response because the response sequence did not include a result element."),



  /**
   * The stream proxy values intermediate response cannot be parsed because it does not have a value.
   */
  ERR_STREAM_PROXY_VALUES_RESPONSE_NO_VALUE("The stream proxy values intermediate response cannot be parsed because it does not have a value."),



  /**
   * An unexpected error occurred while trying to decode the provided ASN.1 element as a time window collect support data log capture window object:  {0}
   */
  ERR_TIME_WINDOW_CSD_LOG_WINDOW_CANNOT_DECODE("An unexpected error occurred while trying to decode the provided ASN.1 element as a time window collect support data log capture window object:  {0}"),



  /**
   * Unable to decode the provided ASN.1 element as a time window collect support data log capture window object because the element sequence had an unexpected element count of {0,number,0} instead of the expected count of 1 or 2 elements.
   */
  ERR_TIME_WINDOW_CSD_LOG_WINDOW_INVALID_ELEMENT_COUNT("Unable to decode the provided ASN.1 element as a time window collect support data log capture window object because the element sequence had an unexpected element count of {0,number,0} instead of the expected count of 1 or 2 elements."),



  /**
   * Unable to decode the provided ASN.1 element as a time window collect support data log capture window object because the element sequence included string ''{0}'' that could not be decoded as a valid timestamp in the generalized time format.
   */
  ERR_TIME_WINDOW_CSD_LOG_WINDOW_MALFORMED_GT("Unable to decode the provided ASN.1 element as a time window collect support data log capture window object because the element sequence included string ''{0}'' that could not be decoded as a valid timestamp in the generalized time format."),



  /**
   * Unable to decode the provided ASN.1 element as a tool-default collect support data log capture window object:  {0}
   */
  ERR_TOOL_DEFAULT_CSD_LOG_WINDOW_CANNOT_DECODE("Unable to decode the provided ASN.1 element as a tool-default collect support data log capture window object:  {0}"),



  /**
   * Unable to decode an ASN.1 element as a changelog batch starting point because it has an unrecognized type of {0}.
   */
  ERR_UNKNOWN_CHANGELOG_BATCH_STARTING_POINT_TYPE("Unable to decode an ASN.1 element as a changelog batch starting point because it has an unrecognized type of {0}."),



  /**
   * Unable to decode an extended request as a validate TOTP request because the request had a malformed value:  {0}
   */
  ERR_VALIDATE_TOTP_REQUEST_MALFORMED_VALUE("Unable to decode an extended request as a validate TOTP request because the request had a malformed value:  {0}"),



  /**
   * Unable to decode an extended request as a validate TOTP request because the provided request did not have a value.
   */
  ERR_VALIDATE_TOTP_REQUEST_MISSING_VALUE("Unable to decode an extended request as a validate TOTP request because the provided request did not have a value."),



  /**
   * Changelog Batch Entry Intermediate Response
   */
  INFO_CHANGELOG_ENTRY_IR_NAME("Changelog Batch Entry Intermediate Response"),



  /**
   * Collect Support Data Archive Fragment Intermediate Response
   */
  INFO_COLLECT_SUPPORT_DATA_FRAGMENT_IR_NAME("Collect Support Data Archive Fragment Intermediate Response"),



  /**
   * Collect Support Data Output Intermediate Response
   */
  INFO_COLLECT_SUPPORT_DATA_OUTPUT_IR_NAME("Collect Support Data Output Intermediate Response"),



  /**
   * Collect Support Data Extended Request
   */
  INFO_COLLECT_SUPPORT_DATA_REQUEST_NAME("Collect Support Data Extended Request"),



  /**
   * Collect Support Data Extended Result
   */
  INFO_COLLECT_SUPPORT_DATA_RESULT_NAME("Collect Support Data Extended Result"),



  /**
   * Deliver One-Time Password Extended Request
   */
  INFO_DELIVER_OTP_REQ_NAME("Deliver One-Time Password Extended Request"),



  /**
   * Deliver One-Time Password Extended Result
   */
  INFO_DELIVER_OTP_RES_NAME("Deliver One-Time Password Extended Result"),



  /**
   * Deregister YubiKey OTP Device Extended Request
   */
  INFO_DEREGISTER_YUBIKEY_OTP_REQUEST_NAME("Deregister YubiKey OTP Device Extended Request"),



  /**
   * Assured Replication Poll Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_ASSURED_REPLICATION_POLL("Assured Replication Poll Extended Request"),



  /**
   * Clear Missed Notification Changes Alarm Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_CLEAR_MISSED_NOTIFICATION_CHANGES_ALARM("Clear Missed Notification Changes Alarm Extended Request"),



  /**
   * Consume Single-Use Token Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_CONSUME_SINGLE_USE_TOKEN("Consume Single-Use Token Extended Request"),



  /**
   * Deliver Password Reset Token Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_DELIVER_PW_RESET_TOKEN("Deliver Password Reset Token Extended Request"),



  /**
   * Deliver Single-Use Token Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_DELIVER_SINGLE_USE_TOKEN("Deliver Single-Use Token Extended Request"),



  /**
   * Delete Notification Destination Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_DEL_NOTIFICATION_DEST("Delete Notification Destination Extended Request"),



  /**
   * Delete Notification Subscription Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_DEL_NOTIFICATION_SUB("Delete Notification Subscription Extended Request"),



  /**
   * End Administrative Session Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_END_ADMIN_SESSION("End Administrative Session Extended Request"),



  /**
   * End Batched Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_END_BATCHED_TXN("End Batched Transaction Extended Request"),



  /**
   * End Interactive Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_END_INTERACTIVE_TXN("End Interactive Transaction Extended Request"),



  /**
   * Get Backup Compatibility Descriptor Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_GET_BACKUP_COMPAT("Get Backup Compatibility Descriptor Extended Request"),



  /**
   * Get Configuration Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_GET_CONFIG("Get Configuration Extended Request"),



  /**
   * Get Connection ID Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_GET_CONNECTION_ID("Get Connection ID Extended Request"),



  /**
   * Get Password Quality Requirements Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_GET_PW_QUALITY_REQS("Get Password Quality Requirements Extended Request"),



  /**
   * Get Subtree Accessibility Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_GET_SUBTREE_ACCESSIBILITY("Get Subtree Accessibility Extended Request"),



  /**
   * Identify Backup Compatibility Problems Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_IDENTIFY_BACKUP_COMPAT_PROBLEMS("Identify Backup Compatibility Problems Extended Request"),



  /**
   * List Configurations Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_LIST_CONFIGS("List Configurations Extended Request"),



  /**
   * List Notification Subscriptions Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_LIST_NOTIFICATION_SUBS("List Notification Subscriptions Extended Request"),



  /**
   * Multi-Update Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_MULTI_UPDATE("Multi-Update Extended Request"),



  /**
   * Password Policy State Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_PW_POLICY_STATE("Password Policy State Extended Request"),



  /**
   * Set Notification Destination Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_SET_NOTIFICATION_DEST("Set Notification Destination Extended Request"),



  /**
   * Set Notification Subscription Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_SET_NOTIFICATION_SUB("Set Notification Subscription Extended Request"),



  /**
   * Set Subtree Accessibility Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_SET_SUBTREE_ACCESSIBILITY("Set Subtree Accessibility Extended Request"),



  /**
   * Start Administrative Session Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_START_ADMIN_SESSION("Start Administrative Session Extended Request"),



  /**
   * Start Batched Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_START_BATCHED_TXN("Start Batched Transaction Extended Request"),



  /**
   * Start Interactive Transaction Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_START_INTERACTIVE_TXN("Start Interactive Transaction Extended Request"),



  /**
   * Stream Directory Values Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_STREAM_DIRECTORY_VALUES("Stream Directory Values Extended Request"),



  /**
   * Stream Proxy Values Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_STREAM_PROXY_VALUES("Stream Proxy Values Extended Request"),



  /**
   * Validate TOTP Password Extended Request
   */
  INFO_EXTENDED_REQUEST_NAME_VALIDATE_TOTP("Validate TOTP Password Extended Request"),



  /**
   * Deliver Password Reset Token Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_DELIVER_PW_RESET_TOKEN("Deliver Password Reset Token Extended Result"),



  /**
   * Deliver Single-Use Token Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_DELIVER_SINGLE_USE_TOKEN("Deliver Single-Use Token Extended Result"),



  /**
   * End Batched Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_END_BATCHED_TXN("End Batched Transaction Extended Result"),



  /**
   * Get Backup Compatibility Descriptor Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_GET_BACKUP_COMPAT("Get Backup Compatibility Descriptor Extended Result"),



  /**
   * Get Configuration Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_GET_CONFIG("Get Configuration Extended Result"),



  /**
   * Get Connection ID Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_GET_CONNECTION_ID("Get Connection ID Extended Result"),



  /**
   * Get Password Quality Requirements Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_GET_PW_QUALITY_REQS("Get Password Quality Requirements Extended Result"),



  /**
   * Get Subtree Accessibility Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_GET_SUBTREE_ACCESSIBILITY("Get Subtree Accessibility Extended Result"),



  /**
   * Identify Backup Compatibility Problems Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_IDENTIFY_BACKUP_COMPAT_PROBLEMS("Identify Backup Compatibility Problems Extended Result"),



  /**
   * Interactive Transaction Aborted Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_INTERACTIVE_TXN_ABORTED("Interactive Transaction Aborted Extended Result"),



  /**
   * List Configurations Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_LIST_CONFIGS("List Configurations Extended Result"),



  /**
   * List Notification Subscriptions Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_LIST_NOTIFICATION_SUBS("List Notification Subscriptions Extended Result"),



  /**
   * Multi-Update Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_MULTI_UPDATE("Multi-Update Extended Result"),



  /**
   * Password Policy State Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_PW_POLICY_STATE("Password Policy State Extended Result"),



  /**
   * Start Batched Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_START_BATCHED_TXN("Start Batched Transaction Extended Result"),



  /**
   * Start Interactive Transaction Extended Result
   */
  INFO_EXTENDED_RESULT_NAME_START_INTERACTIVE_TXN("Start Interactive Transaction Extended Result"),



  /**
   * Generate Password Extended Request
   */
  INFO_GENERATE_PASSWORD_REQUEST_NAME("Generate Password Extended Request"),



  /**
   * Generate Password Extended Result
   */
  INFO_GENERATE_PASSWORD_RESULT_NAME("Generate Password Extended Result"),



  /**
   * Generate TOTP Shared Secret Extended Request
   */
  INFO_GEN_TOTP_SECRET_REQUEST_NAME("Generate TOTP Shared Secret Extended Request"),



  /**
   * Generate TOTP Shared Secret Extended Result
   */
  INFO_GEN_TOTP_SECRET_RESULT_NAME("Generate TOTP Shared Secret Extended Result"),



  /**
   * Get Changelog Batch Request
   */
  INFO_GET_CHANGELOG_BATCH_REQ_NAME("Get Changelog Batch Request"),



  /**
   * Get Changelog Batch Result
   */
  INFO_GET_CHANGELOG_BATCH_RES_NAME("Get Changelog Batch Result"),



  /**
   * Get Supported OTP Delivery Mechanisms Extended Request
   */
  INFO_GET_SUPPORTED_OTP_MECH_REQ_NAME("Get Supported OTP Delivery Mechanisms Extended Request"),



  /**
   * Get Supported OTP Delivery Mechanisms Extended Result
   */
  INFO_GET_SUPPORTED_OTP_MECH_RES_NAME("Get Supported OTP Delivery Mechanisms Extended Result"),



  /**
   * Stream Directory Values Intermediate Response
   */
  INFO_INTERMEDIATE_RESPONSE_NAME_STREAM_DIRECTORY_VALUES("Stream Directory Values Intermediate Response"),



  /**
   * Stream Proxy Values Intermediate Response
   */
  INFO_INTERMEDIATE_RESPONSE_NAME_STREAM_PROXY_VALUES("Stream Proxy Values Intermediate Response"),



  /**
   * Missing Changelog Batch Entries Intermediate Response
   */
  INFO_MISSING_CHANGELOG_ENTRIES_IR_NAME("Missing Changelog Batch Entries Intermediate Response"),



  /**
   * Register YubiKey OTP Device Extended Request
   */
  INFO_REGISTER_YUBIKEY_OTP_REQUEST_NAME("Register YubiKey OTP Device Extended Request"),



  /**
   * Revoke TOTP Shared Secret Extended Request
   */
  INFO_REVOKE_TOTP_SECRET_REQUEST_NAME("Revoke TOTP Shared Secret Extended Request");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-unboundid-extop");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<ExtOpMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<ExtOpMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private ExtOpMessages(final String defaultText)
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

