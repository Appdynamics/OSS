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
package com.unboundid.util.ssl.cert;



import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;



/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.util.ssl.cert package, which correspond to messages in the
 * unboundid-ldapsdk-cert.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum CertMessages
{
  /**
   * Unable to parse the provided X.509 certificate extension {0} as an authority key identifier extension:  {1}
   */
  ERR_AUTHORITY_KEY_ID_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as an authority key identifier extension:  {1}"),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as a basic constraints extension:  {1}
   */
  ERR_BASIC_CONSTRAINTS_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as a basic constraints extension:  {1}"),



  /**
   * An error occurred while trying to compute a {0} fingerprint of the certificate:  {1}
   */
  ERR_CERT_CANNOT_COMPUTE_FINGERPRINT("An error occurred while trying to compute a {0} fingerprint of the certificate:  {1}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the set of certificate extensions:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_EXTENSION("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the set of certificate extensions:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the issuer DN:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_ISSUER_DN("Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the issuer DN:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the issuer unique ID element:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_ISSUER_UNIQUE_ID("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the issuer unique ID element:  {0}"),



  /**
   * An error occurred while trying to parse RDN element {0,number,0} in the name sequence:  {1}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_NAME_SEQUENCE_ELEMENT("An error occurred while trying to parse RDN element {0,number,0} in the name sequence:  {1}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the subject public key info element:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_PUBLIC_KEY_INFO("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the subject public key info element:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the serial number as an integer:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_SERIAL_NUMBER("Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the serial number as an integer:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the signature algorithm:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_SIG_ALG("Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the signature algorithm:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the signature value:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_SIG_VALUE("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the signature value:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the subject DN:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_SUBJECT_DN("Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the subject DN:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the subject unique ID element:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_SUBJECT_UNIQUE_ID("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the subject unique ID element:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the X.509 certificate version as an integer:  {0}
   */
  ERR_CERT_DECODE_CANNOT_PARSE_VERSION("Unable to decode the provided byte array as an X.509 certificate because an error was encountered while trying to parse the X.509 certificate version as an integer:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the validity sequence:  {0}
   */
  ERR_CERT_DECODE_COULD_NOT_PARSE_VALIDITY("Unable to decode the provided byte array as an X.509 certificate because an error occurred while trying to parse the validity sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because the first element of the DER sequence (expected to be the tbsCertificate element) could not itself be parsed as a DER sequence:  {0}
   */
  ERR_CERT_DECODE_FIRST_ELEMENT_NOT_SEQUENCE("Unable to decode the provided byte array as an X.509 certificate because the first element of the DER sequence (expected to be the tbsCertificate element) could not itself be parsed as a DER sequence:  {0}"),



  /**
   * An error occurred while trying to parse the name as an RDN sequence:  {0}
   */
  ERR_CERT_DECODE_NAME_NOT_SEQUENCE("An error occurred while trying to parse the name as an RDN sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because the notAfter element had an unexpected BER type of {0}, which does not match the universal BER type for either a UTC time ({1}) or generalized time ({2}) element.
   */
  ERR_CERT_DECODE_NOT_AFTER_UNEXPECTED_TYPE("Unable to decode the provided byte array as an X.509 certificate because the notAfter element had an unexpected BER type of {0}, which does not match the universal BER type for either a UTC time ({1}) or generalized time ({2}) element."),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because the notBefore element had an unexpected BER type of {0}, which does not match the universal BER type for either a UTC time ({1}) or generalized time ({2}) element.
   */
  ERR_CERT_DECODE_NOT_BEFORE_UNEXPECTED_TYPE("Unable to decode the provided byte array as an X.509 certificate because the notBefore element had an unexpected BER type of {0}, which does not match the universal BER type for either a UTC time ({1}) or generalized time ({2}) element."),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because the contents of the array could not be parsed as a DER sequence:  {0}
   */
  ERR_CERT_DECODE_NOT_SEQUENCE("Unable to decode the provided byte array as an X.509 certificate because the contents of the array could not be parsed as a DER sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because there is a mismatch between the signature algorithm contained in the tbsCertificate sequence ({0}) and the signature algorithm contained in the outer certificate sequence ({1}).  These signature algorithms must match.
   */
  ERR_CERT_DECODE_SIG_ALG_MISMATCH("Unable to decode the provided byte array as an X.509 certificate because there is a mismatch between the signature algorithm contained in the tbsCertificate sequence ({0}) and the signature algorithm contained in the outer certificate sequence ({1}).  These signature algorithms must match."),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because the DER sequence contained {0,number,0}, which is different from the three elements (tbsCertificate, signatureAlgorithm, and signatureValue) that were expected.
   */
  ERR_CERT_DECODE_UNEXPECTED_SEQUENCE_ELEMENT_COUNT("Unable to decode the provided byte array as an X.509 certificate because the DER sequence contained {0,number,0}, which is different from the three elements (tbsCertificate, signatureAlgorithm, and signatureValue) that were expected."),



  /**
   * Unable to decode the provided byte array as an X.509 certificate because it appears to have a version number of {0,number,0}, which not a supported version.  Only versions 1, 2, and 3 are supported.
   */
  ERR_CERT_DECODE_UNSUPPORTED_VERSION("Unable to decode the provided byte array as an X.509 certificate because it appears to have a version number of {0,number,0}, which not a supported version.  Only versions 1, 2, and 3 are supported."),



  /**
   * An error occurred while attempting to encode X.509 certificate {0} using the provided information:  {1}
   */
  ERR_CERT_ENCODE_ERROR("An error occurred while attempting to encode X.509 certificate {0} using the provided information:  {1}"),



  /**
   * Unable to encoded DN ''{0}'' for inclusion in an encoded X.509 certificate because an error occurred while trying to get the default standard schema:  {1}
   */
  ERR_CERT_ENCODE_NAME_CANNOT_GET_SCHEMA("Unable to encoded DN ''{0}'' for inclusion in an encoded X.509 certificate because an error occurred while trying to get the default standard schema:  {1}"),



  /**
   * Unable to encode DN ''{0}'' for inclusion in an encoded X.509 certificate:  {1}
   */
  ERR_CERT_ENCODE_NAME_ERROR("Unable to encode DN ''{0}'' for inclusion in an encoded X.509 certificate:  {1}"),



  /**
   * Unable to encode DN ''{0}'' for inclusion in an encoded X.509 certificate because it includes attribute ''{1}'' that is not defined in the default standard schema.
   */
  ERR_CERT_ENCODE_NAME_UNKNOWN_ATTR_TYPE("Unable to encode DN ''{0}'' for inclusion in an encoded X.509 certificate because it includes attribute ''{1}'' that is not defined in the default standard schema."),



  /**
   * An error occurred while attempting to generate a subject key identifier for the certificate:  {0}
   */
  ERR_CERT_GEN_ISSUER_SIGNED_CANNOT_GENERATE_KEY_ID("An error occurred while attempting to generate a subject key identifier for the certificate:  {0}"),



  /**
   * An error occurred while attempting to generate the {0,number,0}-bit {1} key pair for the certificate:  {2}
   */
  ERR_CERT_GEN_SELF_SIGNED_CANNOT_GENERATE_KEY_PAIR("An error occurred while attempting to generate the {0,number,0}-bit {1} key pair for the certificate:  {2}"),



  /**
   * Unable to get a key generator instance for the ''{0}'' public key algorithm:  {1}
   */
  ERR_CERT_GEN_SELF_SIGNED_CANNOT_GET_KEY_GENERATOR("Unable to get a key generator instance for the ''{0}'' public key algorithm:  {1}"),



  /**
   * An error occurred while attempting to parse the key pair to get the public key elements and construct a subject key identifier:  {0}
   */
  ERR_CERT_GEN_SELF_SIGNED_CANNOT_PARSE_KEY_PAIR("An error occurred while attempting to parse the key pair to get the public key elements and construct a subject key identifier:  {0}"),



  /**
   * Unable to use a key size of {0,number,0} bits with the ''{1}'' key algorithm:  {2}
   */
  ERR_CERT_GEN_SELF_SIGNED_INVALID_KEY_SIZE("Unable to use a key size of {0,number,0} bits with the ''{1}'' key algorithm:  {2}"),



  /**
   * An error occurred while attempting to compute the ''{0}'' signature for the certificate:  {1}
   */
  ERR_CERT_GEN_SIGNATURE_CANNOT_COMPUTE("An error occurred while attempting to compute the ''{0}'' signature for the certificate:  {1}"),



  /**
   * Unable to get a signature generator for the ''{0}'' signature algorithm:  {1}
   */
  ERR_CERT_GEN_SIGNATURE_CANNOT_GET_SIGNATURE_GENERATOR("Unable to get a signature generator for the ''{0}'' signature algorithm:  {1}"),



  /**
   * Unable to initialize the ''{0}'' signature generator with the provided private key:  {1}
   */
  ERR_CERT_GEN_SIGNATURE_CANNOT_INIT_SIGNATURE_GENERATOR("Unable to initialize the ''{0}'' signature generator with the provided private key:  {1}"),



  /**
   * ERROR:  Unable to verify the certificate signature because an error occurred while attempting to get the public key from the issuer certificate:  {0}
   */
  ERR_CERT_VERIFY_SIGNATURE_CANNOT_GET_PUBLIC_KEY("ERROR:  Unable to verify the certificate signature because an error occurred while attempting to get the public key from the issuer certificate:  {0}"),



  /**
   * Unable verify the certificate signature because an error occurred while trying to get a signature verifier for the ''{0}'' signature algorithm:  {1}
   */
  ERR_CERT_VERIFY_SIGNATURE_CANNOT_GET_SIGNATURE_VERIFIER("Unable verify the certificate signature because an error occurred while trying to get a signature verifier for the ''{0}'' signature algorithm:  {1}"),



  /**
   * Unable to initialize the ''{0}'' signature verifier with the issuer certificate''s public key:  {1}
   */
  ERR_CERT_VERIFY_SIGNATURE_CANNOT_INIT_SIGNATURE_VERIFIER("Unable to initialize the ''{0}'' signature verifier with the issuer certificate''s public key:  {1}"),



  /**
   * ERROR:  An error occurred while attempting to verify the signature for certificate ''{0}'':  {1}
   */
  ERR_CERT_VERIFY_SIGNATURE_ERROR("ERROR:  An error occurred while attempting to verify the signature for certificate ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to verify the certificate signature because the certificate is not self-signed and no issuer certificate was provided.
   */
  ERR_CERT_VERIFY_SIGNATURE_ISSUER_CERT_NOT_PROVIDED("ERROR:  Unable to verify the certificate signature because the certificate is not self-signed and no issuer certificate was provided."),



  /**
   * ERROR:  Certificate ''{0}'' has an invalid signature.
   */
  ERR_CERT_VERIFY_SIGNATURE_NOT_VALID("ERROR:  Certificate ''{0}'' has an invalid signature."),



  /**
   * Unable to decode the provided ASN.1 element as a CRL distribution point:  {0}
   */
  ERR_CRL_DP_CANNOT_DECODE("Unable to decode the provided ASN.1 element as a CRL distribution point:  {0}"),



  /**
   * Unable to encode CRL distribution point {0} because an error occurred while trying to get the default standard schema for use in encoding nameRelativeToCRLIssuer value ''{1}'':  {2}
   */
  ERR_CRL_DP_ENCODE_CANNOT_GET_SCHEMA("Unable to encode CRL distribution point {0} because an error occurred while trying to get the default standard schema for use in encoding nameRelativeToCRLIssuer value ''{1}'':  {2}"),



  /**
   * Unable to encode CRL distribution point {0} because an error occurred while trying to encode nameRelativeToCRLIssuer value ''{1}'':  {2}
   */
  ERR_CRL_DP_ENCODE_ERROR("Unable to encode CRL distribution point {0} because an error occurred while trying to encode nameRelativeToCRLIssuer value ''{1}'':  {2}"),



  /**
   * Unable to encode CRL distribution point {0} because nameRelativeToCRLIssuer value ''{1}'' includes attribute type ''{2}'' that is not defined in the default standard schema.
   */
  ERR_CRL_DP_ENCODE_UNKNOWN_ATTR_TYPE("Unable to encode CRL distribution point {0} because nameRelativeToCRLIssuer value ''{1}'' includes attribute type ''{2}'' that is not defined in the default standard schema."),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as a CRL distribution points extension:  {1}
   */
  ERR_CRL_DP_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as a CRL distribution points extension:  {1}"),



  /**
   * Unable to decode the provided ASN.1 element as a CRL distribution point because the distributionPoint element had a nested element with an unexpected DER type of {0}.
   */
  ERR_CRL_DP_UNRECOGNIZED_NAME_ELEMENT_TYPE("Unable to decode the provided ASN.1 element as a CRL distribution point because the distributionPoint element had a nested element with an unexpected DER type of {0}."),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the request attributes:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_ATTRS("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the request attributes:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse a request attribute with OID {0} as an X.509 certificate extension:  {1}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_EXT_ATTR("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse a request attribute with OID {0} as an X.509 certificate extension:  {1}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the subject public key info element:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_PUBLIC_KEY_INFO("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the subject public key info element:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the signature algorithm:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_SIG_ALG("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the signature algorithm:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the signature value:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_SIG_VALUE("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error occurred while trying to parse the signature value:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the subject DN:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_SUBJECT_DN("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the subject DN:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the version element as an integer:  {0}
   */
  ERR_CSR_DECODE_CANNOT_PARSE_VERSION("Unable to decode the provided byte array as a PKCS #10 certificate signing request because an error was encountered while trying to parse the version element as an integer:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because the first element of the DER sequence (expected to be the CertificationRequestInfo element) could not itself be parsed as a DER sequence:  {0}
   */
  ERR_CSR_DECODE_FIRST_ELEMENT_NOT_SEQUENCE("Unable to decode the provided byte array as a PKCS #10 certificate signing request because the first element of the DER sequence (expected to be the CertificationRequestInfo element) could not itself be parsed as a DER sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because the contents of the array could not be parsed as a DER sequence:  {0}
   */
  ERR_CSR_DECODE_NOT_SEQUENCE("Unable to decode the provided byte array as a PKCS #10 certificate signing request because the contents of the array could not be parsed as a DER sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because the DER sequence contained {0,number,0}, which is different from the three elements (CertificationRequestInfo, SignatureAlgorithm, and Signature) that were expected.
   */
  ERR_CSR_DECODE_UNEXPECTED_SEQUENCE_ELEMENT_COUNT("Unable to decode the provided byte array as a PKCS #10 certificate signing request because the DER sequence contained {0,number,0}, which is different from the three elements (CertificationRequestInfo, SignatureAlgorithm, and Signature) that were expected."),



  /**
   * Unable to decode the provided byte array as a PKCS #10 certificate signing request because it appears to have a version number of {0,number,0}, which not a supported version.  Only versions 1 is supported.
   */
  ERR_CSR_DECODE_UNSUPPORTED_VERSION("Unable to decode the provided byte array as a PKCS #10 certificate signing request because it appears to have a version number of {0,number,0}, which not a supported version.  Only versions 1 is supported."),



  /**
   * An error occurred while attempting to encode PKCS #10 certificate signing request {0} using the provided information:  {1}
   */
  ERR_CSR_ENCODE_ERROR("An error occurred while attempting to encode PKCS #10 certificate signing request {0} using the provided information:  {1}"),



  /**
   * An error occurred while attempting to parse the generated key pair to get the public key elements:  {0}
   */
  ERR_CSR_GEN_CANNOT_PARSE_KEY_PAIR("An error occurred while attempting to parse the generated key pair to get the public key elements:  {0}"),



  /**
   * An error occurred while attempting to compute the ''{0}'' signature for the certificate signing request:  {1}
   */
  ERR_CSR_GEN_SIGNATURE_CANNOT_COMPUTE("An error occurred while attempting to compute the ''{0}'' signature for the certificate signing request:  {1}"),



  /**
   * Unable to get a signature generator for the ''{0}'' signature algorithm:  {1}
   */
  ERR_CSR_GEN_SIGNATURE_CANNOT_GET_SIGNATURE_GENERATOR("Unable to get a signature generator for the ''{0}'' signature algorithm:  {1}"),



  /**
   * Unable to initialize the ''{0}'' signature generator with the provided private key:  {1}
   */
  ERR_CSR_GEN_SIGNATURE_CANNOT_INIT_SIGNATURE_GENERATOR("Unable to initialize the ''{0}'' signature generator with the provided private key:  {1}"),



  /**
   * Unable to verify the certificate signing request signature because an error occurred while attempting to parse the request''s public key:  {0}
   */
  ERR_CSR_VERIFY_SIGNATURE_CANNOT_GET_PUBLIC_KEY("Unable to verify the certificate signing request signature because an error occurred while attempting to parse the request''s public key:  {0}"),



  /**
   * Unable to verify the certificate signing request signature because an error occurred while attempting to get a signature verifier for the ''{0}'' signature algorithm:  {1}
   */
  ERR_CSR_VERIFY_SIGNATURE_CANNOT_GET_SIGNATURE_VERIFIER("Unable to verify the certificate signing request signature because an error occurred while attempting to get a signature verifier for the ''{0}'' signature algorithm:  {1}"),



  /**
   * Unable to verify the certificate signing request signature because an error occurred while attempting to initialize the ''{0}'' signature verifier with the request''s public key:  {1}
   */
  ERR_CSR_VERIFY_SIGNATURE_CANNOT_INIT_SIGNATURE_VERIFIER("Unable to verify the certificate signing request signature because an error occurred while attempting to initialize the ''{0}'' signature verifier with the request''s public key:  {1}"),



  /**
   * ERROR:  An error occurred while attempting to verify the signature for the certificate signing request with subject ''{0}'':  {1}
   */
  ERR_CSR_VERIFY_SIGNATURE_ERROR("ERROR:  An error occurred while attempting to verify the signature for the certificate signing request with subject ''{0}'':  {1}"),



  /**
   * ERROR:  The certificate signing request with subject ''{0}'' has an invalid signature.
   */
  ERR_CSR_VERIFY_SIGNATURE_NOT_VALID("ERROR:  The certificate signing request with subject ''{0}'' has an invalid signature."),



  /**
   * Unable to decode the PKCS #8 private key as an elliptic curve private key:  {0}
   */
  ERR_EC_PRIVATE_KEY_CANNOT_DECODE("Unable to decode the PKCS #8 private key as an elliptic curve private key:  {0}"),



  /**
   * An error occurred while trying to encode elliptic curve private key {0}:  {1}
   */
  ERR_EC_PRIVATE_KEY_CANNOT_ENCODE("An error occurred while trying to encode elliptic curve private key {0}:  {1}"),



  /**
   * Unable to decode the PKCS #8 private key as an elliptic curve private key because it has an unsupported version of {0,number,0}.  Only version 1 is supported.
   */
  ERR_EC_PRIVATE_KEY_UNSUPPORTED_VERSION("Unable to decode the PKCS #8 private key as an elliptic curve private key because it has an unsupported version of {0,number,0}.  Only version 1 is supported."),



  /**
   * Unable to encode elliptic curve public key {0} because the x coordinate value requires {1,number,0} bytes to encode, which is larger than the maximum allowed size of 32 bytes.
   */
  ERR_EC_PUBLIC_KEY_ENCODE_X_TOO_LARGE("Unable to encode elliptic curve public key {0} because the x coordinate value requires {1,number,0} bytes to encode, which is larger than the maximum allowed size of 32 bytes."),



  /**
   * Unable to encode elliptic curve public key {0} because the y coordinate value requires {1,number,0} bytes to encode, which is larger than the maximum allowed size of 32 bytes.
   */
  ERR_EC_PUBLIC_KEY_ENCODE_Y_TOO_LARGE("Unable to encode elliptic curve public key {0} because the y coordinate value requires {1,number,0} bytes to encode, which is larger than the maximum allowed size of 32 bytes."),



  /**
   * Unable to decode the X.509 certificate public key as an elliptic curve public key:  {0}
   */
  ERR_EC_PUBLIC_KEY_PARSE_ERROR("Unable to decode the X.509 certificate public key as an elliptic curve public key:  {0}"),



  /**
   * Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, indicating that it uses the compressed form of the point, but the value of the first byte is {1} rather than the expected value of 02 (to indicate that the y coordinate is even) or 0x03 (to indicate that the y coordinate is odd).
   */
  ERR_EC_PUBLIC_KEY_PARSE_UNEXPECTED_COMPRESSED_FIRST_BYTE("Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, indicating that it uses the compressed form of the point, but the value of the first byte is {1} rather than the expected value of 02 (to indicate that the y coordinate is even) or 0x03 (to indicate that the y coordinate is odd)."),



  /**
   * Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, which does not match the expected size for a 256-bit or 384-bit compressed or uncompressed elliptic curve public key.
   */
  ERR_EC_PUBLIC_KEY_PARSE_UNEXPECTED_SIZE("Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, which does not match the expected size for a 256-bit or 384-bit compressed or uncompressed elliptic curve public key."),



  /**
   * Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, indicating that it uses the uncompressed form of the point, but the value of the first byte is {1} rather than the expected value of 04.
   */
  ERR_EC_PUBLIC_KEY_PARSE_UNEXPECTED_UNCOMPRESSED_FIRST_BYTE("Unable to decode the X.509 certificate public key as an elliptic curve public key because the public key has a size of {0,number,0} bytes, indicating that it uses the uncompressed form of the point, but the value of the first byte is {1} rather than the expected value of 04."),



  /**
   * An error occurred while trying to encode the value of an extended key usage extension with key usage IDs {0}:  {1}
   */
  ERR_EXTENDED_KEY_USAGE_EXTENSION_CANNOT_ENCODE("An error occurred while trying to encode the value of an extended key usage extension with key usage IDs {0}:  {1}"),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as an extended key usage extension:  {1}
   */
  ERR_EXTENDED_KEY_USAGE_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as an extended key usage extension:  {1}"),



  /**
   * An error occurred while trying to decode an ASN.1 element as an X.509 certificate extension:  {0}
   */
  ERR_EXTENSION_DECODE_ERROR("An error occurred while trying to decode an ASN.1 element as an X.509 certificate extension:  {0}"),



  /**
   * Unable to encode X.509 certificate extension {0}:  {1}
   */
  ERR_EXTENSION_ENCODE_ERROR("Unable to encode X.509 certificate extension {0}:  {1}"),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as an extension of type {1}:  {2}
   */
  ERR_GENERAL_ALT_NAME_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as an extension of type {1}:  {2}"),



  /**
   * An error occurred while trying to encode general names element {0}:  {1}
   */
  ERR_GENERAL_NAMES_CANNOT_ENCODE("An error occurred while trying to encode general names element {0}:  {1}"),



  /**
   * Unable to parse the provided element as a general names element:  {0}
   */
  ERR_GENERAL_NAMES_CANNOT_PARSE("Unable to parse the provided element as a general names element:  {0}"),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as key usage extension:  {1}
   */
  ERR_KEY_USAGE_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as key usage extension:  {1}"),



  /**
   * ERROR:  Unable to instantiate a key store of type ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_CANNOT_INSTANTIATE_KS_TYPE("ERROR:  Unable to instantiate a key store of type ''{0}'':  {1}"),



  /**
   * ERROR:  An error occurred while trying to invoke the ''{0}'' command:
   */
  ERR_MANAGE_CERTS_CANNOT_INVOKE_COMMAND("ERROR:  An error occurred while trying to invoke the ''{0}'' command:"),



  /**
   * ERROR:  Unable to load the contents of key store file ''{0}'' because the correct key store password was not provided.  Please provide the correct key store password.
   */
  ERR_MANAGE_CERTS_CANNOT_LOAD_KS_WRONG_PW("ERROR:  Unable to load the contents of key store file ''{0}'' because the correct key store password was not provided.  Please provide the correct key store password."),



  /**
   * ERROR:  Unable to open key store file ''{0}'' for reading:  {1}
   */
  ERR_MANAGE_CERTS_CANNOT_OPEN_KS_FILE_FOR_READING("ERROR:  Unable to open key store file ''{0}'' for reading:  {1}"),



  /**
   * ERROR:  Unable to establish a connection to {0}.
   */
  ERR_MANAGE_CERTS_CERT_COLLECTOR_CONNECT_FAILED("ERROR:  Unable to establish a connection to {0}."),



  /**
   * ERROR:  Unable to parse the certificate chain received from the server {0} as a set of X.509 certificates.
   */
  ERR_MANAGE_CERTS_CERT_COLLECTOR_ERROR_PARSING_CERT_CHAIN("ERROR:  Unable to parse the certificate chain received from the server {0} as a set of X.509 certificates."),



  /**
   * An error occurred while trying to start TLS negotiation.
   */
  ERR_MANAGE_CERTS_CERT_COLLECTOR_ERROR_STARTING_TLS_NEGOTIATION("An error occurred while trying to start TLS negotiation."),



  /**
   * ERROR:  Did not receive the certificate chain from {0} after waiting for up to 60 seconds.
   */
  ERR_MANAGE_CERTS_CERT_COLLECTOR_NO_CERT_CHAIN_RECEIVED("ERROR:  Did not receive the certificate chain from {0} after waiting for up to 60 seconds."),



  /**
   * ERROR:  The server rejected the StartTLS request.
   */
  ERR_MANAGE_CERTS_CERT_COLLECTOR_START_TLS_FAILED("ERROR:  The server rejected the StartTLS request."),



  /**
   * ERROR:  An error occurred while attempting to retrieve the contents of the existing entry with alias ''{0}''
   */
  ERR_MANAGE_CERTS_CHANGE_ALIAS_CANNOT_GET_EXISTING_ENTRY("ERROR:  An error occurred while attempting to retrieve the contents of the existing entry with alias ''{0}''"),



  /**
   * ERROR:  An error occurred while attempting to update the key store to set the new alias:
   */
  ERR_MANAGE_CERTS_CHANGE_ALIAS_CANNOT_UPDATE_KEYSTORE("ERROR:  An error occurred while attempting to update the key store to set the new alias:"),



  /**
   * ERROR:  The key store already has an entry with alias ''{0}''.
   */
  ERR_MANAGE_CERTS_CHANGE_ALIAS_NEW_ALIAS_IN_USE("ERROR:  The key store already has an entry with alias ''{0}''."),



  /**
   * ERROR:  The key store does not have an existing entry with alias ''{0}''.
   */
  ERR_MANAGE_CERTS_CHANGE_ALIAS_NO_SUCH_ALIAS("ERROR:  The key store does not have an existing entry with alias ''{0}''."),



  /**
   * ERROR:  Alias ''{0}'' references a certificate entry for which there is no private key.  You can only change the private key password for entries that have a private key.
   */
  ERR_MANAGE_CERTS_CHANGE_PK_PW_ALIAS_IS_CERT("ERROR:  Alias ''{0}'' references a certificate entry for which there is no private key.  You can only change the private key password for entries that have a private key."),



  /**
   * ERROR:  An error occurred while attempting to retrieve the private key stored in alias ''{0}'':
   */
  ERR_MANAGE_CERTS_CHANGE_PK_PW_CANNOT_GET_PK("ERROR:  An error occurred while attempting to retrieve the private key stored in alias ''{0}'':"),



  /**
   * ERROR:  An error occurred while attempting to update the key store:
   */
  ERR_MANAGE_CERTS_CHANGE_PK_PW_CANNOT_UPDATE_KS("ERROR:  An error occurred while attempting to update the key store:"),



  /**
   * ERROR:  Alias ''{0}'' does not exist in the key store.
   */
  ERR_MANAGE_CERTS_CHANGE_PK_PW_NO_SUCH_ALIAS("ERROR:  Alias ''{0}'' does not exist in the key store."),



  /**
   * ERROR:  Unable to retrieve the private key stored in alias ''{0}''.  The most likely reason is that the provided current private key password is incorrect.
   */
  ERR_MANAGE_CERTS_CHANGE_PK_PW_WRONG_PK_PW("ERROR:  Unable to retrieve the private key stored in alias ''{0}''.  The most likely reason is that the provided current private key password is incorrect."),



  /**
   * ERROR:  An error occurred while retrieving the certificate chain contained in the key entry with alias ''{0}'':
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_CANNOT_GET_CHAIN("ERROR:  An error occurred while retrieving the certificate chain contained in the key entry with alias ''{0}'':"),



  /**
   * ERROR:  The certificate chain stored in alias ''{0}'' is not valid because the certificates in it do not constitute a single continuous change.  The certificate with subject ''{1}'' is not the issuer certificate for the certificate with subject ''{2}'' that immediately precedes it in the chain:  {3}
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_CHAIN_ISSUER_MISMATCH("ERROR:  The certificate chain stored in alias ''{0}'' is not valid because the certificates in it do not constitute a single continuous change.  The certificate with subject ''{1}'' is not the issuer certificate for the certificate with subject ''{2}'' that immediately precedes it in the chain:  {3}"),



  /**
   * ERROR:  Certificate ''{0}'' at the head of the chain includes an extended key usage extension, but that extension does not include the ''serverAuth'' usage.  Clients that check this extension will not accept the certificate as a TLS server certificate.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_END_CERT_BAD_EKU("ERROR:  Certificate ''{0}'' at the head of the chain includes an extended key usage extension, but that extension does not include the ''serverAuth'' usage.  Clients that check this extension will not accept the certificate as a TLS server certificate."),



  /**
   * ERROR:  Certificate ''{0}'' expired at {1}.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_END_CERT_EXPIRED("ERROR:  Certificate ''{0}'' expired at {1}."),



  /**
   * ERROR:  Certificate ''{0}'' is not yet valid.  It will not be valid until {1}.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_END_CERT_NOT_YET_VALID("ERROR:  Certificate ''{0}'' is not yet valid.  It will not be valid until {1}."),



  /**
   * ERROR:  The certificate chain stored in alias ''{0}'' is not complete because it does not end with a self-signed certificate.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_END_OF_CHAIN_NOT_SELF_SIGNED("ERROR:  The certificate chain stored in alias ''{0}'' is not complete because it does not end with a self-signed certificate."),



  /**
   * ERROR:  Issuer certificate ''{0}'' includes a basic constraints extension that indicates the certificate is not permitted to act as a certification authority.  Clients that check this extension will not accept the certificate chain.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_BAD_BC_CA("ERROR:  Issuer certificate ''{0}'' includes a basic constraints extension that indicates the certificate is not permitted to act as a certification authority.  Clients that check this extension will not accept the certificate chain."),



  /**
   * ERROR:  Issuer certificate ''{0}'' includes a basic constraints extension with a path length value of {1,number,0}, which means that there must not be more than {1,number,0} intermediate certificate(s) between that certificate and the subject certificate.  However, the number of intermediate certificates between subject certificate ''{2}'' and issuer certificate ''{0}'' is {3,number,0}.  Clients that check this extension will likely not accept the certificate chain.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_BAD_BC_LENGTH("ERROR:  Issuer certificate ''{0}'' includes a basic constraints extension with a path length value of {1,number,0}, which means that there must not be more than {1,number,0} intermediate certificate(s) between that certificate and the subject certificate.  However, the number of intermediate certificates between subject certificate ''{2}'' and issuer certificate ''{0}'' is {3,number,0}.  Clients that check this extension will likely not accept the certificate chain."),



  /**
   * ERROR:  Issuer certificate ''{0}'' expired at {1}.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_EXPIRED("ERROR:  Issuer certificate ''{0}'' expired at {1}."),



  /**
   * ERROR:  Issuer certificate ''{0}'' is not yet valid.  It will not be valid until {1}.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_NOT_YET_VALID("ERROR:  Issuer certificate ''{0}'' is not yet valid.  It will not be valid until {1}."),



  /**
   * ERROR:  Issuer certificate ''{0}'' includes a key usage extension, but that extension does not have the keyCertSign usage flag set to true.  Clients that check this extension will not trust it to sign other certificates.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ISSUER_NO_CERT_SIGN_KU("ERROR:  Issuer certificate ''{0}'' includes a key usage extension, but that extension does not have the keyCertSign usage flag set to true.  Clients that check this extension will not trust it to sign other certificates."),



  /**
   * {0,number,0} usability errors were identified while validating the certificate chain.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_MULTIPLE_ERRORS("{0,number,0} usability errors were identified while validating the certificate chain."),



  /**
   * No usability errors were identified while validating the certificate chain, but {0,number,0} usability warnings were identified.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_MULTIPLE_WARNINGS("No usability errors were identified while validating the certificate chain, but {0,number,0} usability warnings were identified."),



  /**
   * ERROR:  Alias ''{0}'' contains only a certificate entry with no corresponding private key.  A server certificate must have both a private key and a certificate chain.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_NO_PRIVATE_KEY("ERROR:  Alias ''{0}'' contains only a certificate entry with no corresponding private key.  A server certificate must have both a private key and a certificate chain."),



  /**
   * ERROR:  Alias ''{0}'' does not exist in the key store.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_NO_SUCH_ALIAS("ERROR:  Alias ''{0}'' does not exist in the key store."),



  /**
   * 1 usability error was identified while validating the certificate chain.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ONE_ERROR("1 usability error was identified while validating the certificate chain."),



  /**
   * No usability errors were identified while validating the certificate chain, but 1 usability warning was identified.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_ONE_WARNING("No usability errors were identified while validating the certificate chain, but 1 usability warning was identified."),



  /**
   * ERROR:  Certificate ''{0}'' has a {1,number,0}-bit RSA public key, which is considered weak.  RSA keys should have a size of at least 2048 bits.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_WEAK_RSA_MODULUS("ERROR:  Certificate ''{0}'' has a {1,number,0}-bit RSA public key, which is considered weak.  RSA keys should have a size of at least 2048 bits."),



  /**
   * ERROR:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is considered weak.  Some clients may not accept certificates with this signature algorithm.
   */
  ERR_MANAGE_CERTS_CHECK_USABILITY_WEAK_SIG_ALG("ERROR:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is considered weak.  Some clients may not accept certificates with this signature algorithm."),



  /**
   * The delete operation was canceled and the key store was not updated.
   */
  ERR_MANAGE_CERTS_DELETE_CERT_CANCELED("The delete operation was canceled and the key store was not updated."),



  /**
   * ERROR:  An error occurred when trying to delete the ''{0}'' entry from the key store:
   */
  ERR_MANAGE_CERTS_DELETE_CERT_DELETE_ERROR("ERROR:  An error occurred when trying to delete the ''{0}'' entry from the key store:"),



  /**
   * ERROR:  There is no certificate or key entry with alias ''{0}'' in the key store.
   */
  ERR_MANAGE_CERTS_DELETE_CERT_ERROR_ALIAS_NOT_CERT_OR_KEY("ERROR:  There is no certificate or key entry with alias ''{0}'' in the key store."),



  /**
   * ERROR:  An error occurred while attempting to retrieve the certificate stored in alias ''{0}'':
   */
  ERR_MANAGE_CERTS_DELETE_CERT_ERROR_GETTING_CERT("ERROR:  An error occurred while attempting to retrieve the certificate stored in alias ''{0}'':"),



  /**
   * ERROR:  An error occurred while attempting to retrieve the certificate chain associated with the private key stored in alias ''{0}'':
   */
  ERR_MANAGE_CERTS_DELETE_CERT_ERROR_GETTING_CHAIN("ERROR:  An error occurred while attempting to retrieve the certificate chain associated with the private key stored in alias ''{0}'':"),



  /**
   * ERROR:  An error occurred while trying to load the contents of the key store from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_ERROR_CANNOT_LOAD_KS("ERROR:  An error occurred while trying to load the contents of the key store from file ''{0}'':  {1}"),



  /**
   * ERROR:  An error occurred while waiting for the ''{0}'' command to complete:
   */
  ERR_MANAGE_CERTS_ERROR_WAITING_FOR_COMMAND("ERROR:  An error occurred while waiting for the ''{0}'' command to complete:"),



  /**
   * ERROR:  An error occurred while trying to obtain the certificate with alias ''{0}'' from key store ''{1}'':
   */
  ERR_MANAGE_CERTS_EXPORT_CERT_ERROR_GETTING_CERT("ERROR:  An error occurred while trying to obtain the certificate with alias ''{0}'' from key store ''{1}'':"),



  /**
   * ERROR:  An error occurred while opening output file ''{0}'' for writing:
   */
  ERR_MANAGE_CERTS_EXPORT_CERT_ERROR_OPENING_OUTPUT("ERROR:  An error occurred while opening output file ''{0}'' for writing:"),



  /**
   * ERROR:  An error occurred while attempting to export the certificate with alias ''{0}'' and subject ''{1}'':
   */
  ERR_MANAGE_CERTS_EXPORT_CERT_ERROR_WRITING_CERT("ERROR:  An error occurred while attempting to export the certificate with alias ''{0}'' and subject ''{1}'':"),



  /**
   * ERROR:  There is no certificate with alias ''{0}'' in key store ''{1}''.
   */
  ERR_MANAGE_CERTS_EXPORT_CERT_NO_CERT_WITH_ALIAS("ERROR:  There is no certificate with alias ''{0}'' in key store ''{1}''."),



  /**
   * ERROR:  An output file must be specified when exporting certificates in the binary DER format.
   */
  ERR_MANAGE_CERTS_EXPORT_CERT_NO_FILE_WITH_DER("ERROR:  An output file must be specified when exporting certificates in the binary DER format."),



  /**
   * ERROR:  An error occurred while trying to obtain the private key with alias ''{0}'' from key store ''{1}'':
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_ERROR_GETTING_KEY("ERROR:  An error occurred while trying to obtain the private key with alias ''{0}'' from key store ''{1}'':"),



  /**
   * ERROR:  An error occurred while opening output file ''{0}'' for writing:
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_ERROR_OPENING_OUTPUT("ERROR:  An error occurred while opening output file ''{0}'' for writing:"),



  /**
   * ERROR:  An error occurred while attempting to export the private with alias ''{0}'':
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_ERROR_WRITING_KEY("ERROR:  An error occurred while attempting to export the private with alias ''{0}'':"),



  /**
   * ERROR:  An output file must be specified when exporting a private key in the binary DER format.
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_NO_FILE_WITH_DER("ERROR:  An output file must be specified when exporting a private key in the binary DER format."),



  /**
   * ERROR:  There is no private key with alias ''{0}'' in key store ''{1}''.
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_NO_KEY_WITH_ALIAS("ERROR:  There is no private key with alias ''{0}'' in key store ''{1}''."),



  /**
   * ERROR:  Unable to retrieve the private key with alias ''{0}'' from key store ''{1}'' because the wrong password was used to try to access the key.  Please use one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments to supply the correct password for the private key.
   */
  ERR_MANAGE_CERTS_EXPORT_KEY_WRONG_KEY_PW("ERROR:  Unable to retrieve the private key with alias ''{0}'' from key store ''{1}'' because the wrong password was used to try to access the key.  Please use one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments to supply the correct password for the private key."),



  /**
   * ERROR:  Alias ''{0}'' is already in use in the key store.  The specified alias must not exist unless the --replace-existing-certificate argument is also provided.
   */
  ERR_MANAGE_CERTS_GEN_CERT_ALIAS_EXISTS_WITHOUT_REPLACE("ERROR:  Alias ''{0}'' is already in use in the key store.  The specified alias must not exist unless the --replace-existing-certificate argument is also provided."),



  /**
   * ERROR:  The --basic-constraints-path-length argument cannot be used unless the --basic-constraints-is-ca argument is also provided with a value of ''true''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_BC_PATH_LENGTH_WITHOUT_CA("ERROR:  The --basic-constraints-path-length argument cannot be used unless the --basic-constraints-is-ca argument is also provided with a value of ''true''."),



  /**
   * ERROR:  An error occurred while trying to generate a self-signed certificate with the provided settings:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_GENERATING_CERT("ERROR:  An error occurred while trying to generate a self-signed certificate with the provided settings:"),



  /**
   * ERROR:  An error occurred while trying to generate a certificate signing request with the provided settings:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_GENERATING_CSR("ERROR:  An error occurred while trying to generate a certificate signing request with the provided settings:"),



  /**
   * ERROR:  An error occurred while trying to generate a signed certificate with the provided settings:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_SIGNING_CERT("ERROR:  An error occurred while trying to generate a signed certificate with the provided settings:"),



  /**
   * ERROR:  An error occurred while attempting to write the updated key store:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_UPDATING_KEYSTORE("ERROR:  An error occurred while attempting to write the updated key store:"),



  /**
   * ERROR:  An error occurred while trying to write the generated certificate signing request:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_WRITING_CSR("ERROR:  An error occurred while trying to write the generated certificate signing request:"),



  /**
   * ERROR:  An error occurred while trying to write the signed certificate:
   */
  ERR_MANAGE_CERTS_GEN_CERT_ERROR_WRITING_SIGNED_CERT("ERROR:  An error occurred while trying to write the signed certificate:"),



  /**
   * ERROR:  Unable to create an extended key usage extension with the provided values:
   */
  ERR_MANAGE_CERTS_GEN_CERT_EXTENDED_KEY_USAGE_ERROR("ERROR:  Unable to create an extended key usage extension with the provided values:"),



  /**
   * ERROR:  Unable to create an extension from value ''{0}'' because the criticality value ''{1}'' could not be parsed as a Boolean value.  The criticality should be either ''true'' or ''false'' (without the single quotes).
   */
  ERR_MANAGE_CERTS_GEN_CERT_EXT_INVALID_CRITICALITY("ERROR:  Unable to create an extension from value ''{0}'' because the criticality value ''{1}'' could not be parsed as a Boolean value.  The criticality should be either ''true'' or ''false'' (without the single quotes)."),



  /**
   * ERROR:  Unable to create an extension from value ''{0}'' because the value portion could not be parsed as a valid hexadecimal string with an even number of characters.
   */
  ERR_MANAGE_CERTS_GEN_CERT_EXT_INVALID_VALUE("ERROR:  Unable to create an extension from value ''{0}'' because the value portion could not be parsed as a valid hexadecimal string with an even number of characters."),



  /**
   * ERROR:  Unable to create an extension from value ''{0}'' because that value could not be parsed in the form ''oid:criticality:value'', where oid is the object identifier for the extension, criticality is either ''true'' or ''false'', and value is the hexadecimal representation of the bytes to include in the extension value.
   */
  ERR_MANAGE_CERTS_GEN_CERT_EXT_MALFORMED("ERROR:  Unable to create an extension from value ''{0}'' because that value could not be parsed in the form ''oid:criticality:value'', where oid is the object identifier for the extension, criticality is either ''true'' or ''false'', and value is the hexadecimal representation of the bytes to include in the extension value."),



  /**
   * ERROR:  Unable to create an extension from value ''{0}'' because it has a malformed OID ''{1}'' that is not a strictly valid object identifier.
   */
  ERR_MANAGE_CERTS_GEN_CERT_EXT_MALFORMED_OID("ERROR:  Unable to create an extension from value ''{0}'' because it has a malformed OID ''{1}'' that is not a strictly valid object identifier."),



  /**
   * ERROR:  Invalid value ''{0}'' provided for the --extended-key-usage argument.  Allowed values are:  ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.
   */
  ERR_MANAGE_CERTS_GEN_CERT_INVALID_EXTENDED_KEY_USAGE("ERROR:  Invalid value ''{0}'' provided for the --extended-key-usage argument.  Allowed values are:  ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier."),



  /**
   * ERROR:  Invalid value ''{0}'' provided for the --key-usage argument.  Allowed values are:  ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_INVALID_KEY_USAGE("ERROR:  Invalid value ''{0}'' provided for the --key-usage argument.  Allowed values are:  ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''."),



  /**
   * ERROR:  An output file must be specified when using the binary DER output format.
   */
  ERR_MANAGE_CERTS_GEN_CERT_NO_FILE_WITH_DER("ERROR:  An output file must be specified when using the binary DER output format."),



  /**
   * ERROR:  If the --key-algorithm argument is used to specify a key algorithm other than ''RSA'', then the --key-size-bits argument must also be provided to specify the key size.
   */
  ERR_MANAGE_CERTS_GEN_CERT_NO_KEY_SIZE_FOR_NON_RSA_KEY("ERROR:  If the --key-algorithm argument is used to specify a key algorithm other than ''RSA'', then the --key-size-bits argument must also be provided to specify the key size."),



  /**
   * ERROR:  If the --key-algorithm argument is used to specify a key algorithm other than ''RSA'', then the --signature-algorithm argument must also be provided to specify the signature algorithm.
   */
  ERR_MANAGE_CERTS_GEN_CERT_NO_SIG_ALG_FOR_NON_RSA_KEY("ERROR:  If the --key-algorithm argument is used to specify a key algorithm other than ''RSA'', then the --signature-algorithm argument must also be provided to specify the signature algorithm."),



  /**
   * ERROR:  The --subject-dn argument must be provided unless the --replace-existing-certificate argument is provided.
   */
  ERR_MANAGE_CERTS_GEN_CERT_NO_SUBJECT_DN_WITHOUT_REPLACE("ERROR:  The --subject-dn argument must be provided unless the --replace-existing-certificate argument is provided."),



  /**
   * ERROR:  Alias ''{0}'' in key store ''{1}'' is associated with a certificate entry that does not include a private key.  The --replace-existing-certificate argument can only be used to replace a certificate that has a corresponding private key.
   */
  ERR_MANAGE_CERTS_GEN_CERT_REPLACE_ALIAS_IS_CERT("ERROR:  Alias ''{0}'' in key store ''{1}'' is associated with a certificate entry that does not include a private key.  The --replace-existing-certificate argument can only be used to replace a certificate that has a corresponding private key."),



  /**
   * ERROR:  An error occurred while attempting to retrieve the certificate and corresponding key pair at the head of the chain stored in alias ''{0}'':
   */
  ERR_MANAGE_CERTS_GEN_CERT_REPLACE_COULD_NOT_GET_CERT("ERROR:  An error occurred while attempting to retrieve the certificate and corresponding key pair at the head of the chain stored in alias ''{0}'':"),



  /**
   * ERROR:  Alias ''{0}'' does not exist in key store ''{1}''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_REPLACE_NO_SUCH_ALIAS("ERROR:  Alias ''{0}'' does not exist in key store ''{1}''."),



  /**
   * ERROR:  If the --replace-existing-certificate argument is provided, then the key store file must already exist.
   */
  ERR_MANAGE_CERTS_GEN_CERT_REPLACE_WITHOUT_KS("ERROR:  If the --replace-existing-certificate argument is provided, then the key store file must already exist."),



  /**
   * ERROR:  Alias ''{0}'' in key store ''{1}'' is associated with a certificate entry that does not include a private key.  The signing certificate must have a private key.
   */
  ERR_MANAGE_CERTS_GEN_CERT_SIGN_ALIAS_IS_CERT("ERROR:  Alias ''{0}'' in key store ''{1}'' is associated with a certificate entry that does not include a private key.  The signing certificate must have a private key."),



  /**
   * The operation was canceled and the certificate signing request was not signed.
   */
  ERR_MANAGE_CERTS_GEN_CERT_SIGN_CANCELED("The operation was canceled and the certificate signing request was not signed."),



  /**
   * ERROR:  An error occurred while attempting to retrieve the signing certificate and its corresponding key pair with alias ''{0}'' from the key store:
   */
  ERR_MANAGE_CERTS_GEN_CERT_SIGN_CANNOT_GET_SIGNING_CERT("ERROR:  An error occurred while attempting to retrieve the signing certificate and its corresponding key pair with alias ''{0}'' from the key store:"),



  /**
   * ERROR:  Alias ''{0}'' does not exist in key store ''{1}''.  The signing certificate must exist and must have a private key.
   */
  ERR_MANAGE_CERTS_GEN_CERT_SIGN_NO_SUCH_ALIAS("ERROR:  Alias ''{0}'' does not exist in key store ''{1}''.  The signing certificate must exist and must have a private key."),



  /**
   * ERROR:  Unrecognized public key algorithm ''{0}''.  Suggested key algorithm names include ''RSA'' and ''EC''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_UNKNOWN_KEY_ALG("ERROR:  Unrecognized public key algorithm ''{0}''.  Suggested key algorithm names include ''RSA'' and ''EC''."),



  /**
   * ERROR:  Unrecognized signature algorithm ''{0}''.  Suggested signature algorithm names include ''SHA256withRSA'', ''SHA384withRSA'', ''SHA512withRSA'', ''SHA256withECDSA'', ''SHA384withECDSA'', and ''SHA512withECDSA''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_UNKNOWN_SIG_ALG("ERROR:  Unrecognized signature algorithm ''{0}''.  Suggested signature algorithm names include ''SHA256withRSA'', ''SHA384withRSA'', ''SHA512withRSA'', ''SHA256withECDSA'', ''SHA384withECDSA'', and ''SHA512withECDSA''."),



  /**
   * ERROR:  The existing certificate uses an unrecognized signature algorithm with OID ''{0}''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_UNKNOWN_SIG_ALG_IN_CERT("ERROR:  The existing certificate uses an unrecognized signature algorithm with OID ''{0}''."),



  /**
   * ERROR:  The existing certificate signing request uses an unrecognized signature algorithm with OID ''{0}''.
   */
  ERR_MANAGE_CERTS_GEN_CERT_UNKNOWN_SIG_ALG_IN_CSR("ERROR:  The existing certificate signing request uses an unrecognized signature algorithm with OID ''{0}''."),



  /**
   * ERROR:  An error occurred while trying to retrieve the certificate chain for alias ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_GET_CHAIN_ERROR("ERROR:  An error occurred while trying to retrieve the certificate chain for alias ''{0}'':  {1}"),



  /**
   * ERROR:  An error occurred while trying to retrieve the issuer certificate with subject ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_GET_ISSUER_ERROR("ERROR:  An error occurred while trying to retrieve the issuer certificate with subject ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to read the key store password from file ''{0}'' because the file is empty.  The file must have exactly one line, consisting only of the clear-text key store password.
   */
  ERR_MANAGE_CERTS_GET_KS_PW_EMPTY_FILE("ERROR:  Unable to read the key store password from file ''{0}'' because the file is empty.  The file must have exactly one line, consisting only of the clear-text key store password."),



  /**
   * ERROR:  An error occurred while attempting to read the key store password from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_GET_KS_PW_ERROR_READING_FILE("ERROR:  An error occurred while attempting to read the key store password from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to read the key store password from file ''{0}'' because the file has multiple lines.  The file must have exactly one line, consisting only of the clear-text key store password.
   */
  ERR_MANAGE_CERTS_GET_KS_PW_MULTI_LINE_FILE("ERROR:  Unable to read the key store password from file ''{0}'' because the file has multiple lines.  The file must have exactly one line, consisting only of the clear-text key store password."),



  /**
   * ERROR:  The specified key store password is too short.  The password must contain at least six characters.
   */
  ERR_MANAGE_CERTS_GET_KS_PW_TOO_SHORT("ERROR:  The specified key store password is too short.  The password must contain at least six characters."),



  /**
   * ERROR:  Unable to read the private key password from file ''{0}'' because the file is empty.  The file must have exactly one line, consisting only of the clear-text private key password.
   */
  ERR_MANAGE_CERTS_GET_PK_PW_EMPTY_FILE("ERROR:  Unable to read the private key password from file ''{0}'' because the file is empty.  The file must have exactly one line, consisting only of the clear-text private key password."),



  /**
   * ERROR:  An error occurred while attempting to read the private key password from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_GET_PK_PW_ERROR_READING_FILE("ERROR:  An error occurred while attempting to read the private key password from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to read the private key password from file ''{0}'' because the file has multiple lines.  The file must have exactly one line, consisting only of the clear-text private key password.
   */
  ERR_MANAGE_CERTS_GET_PK_PW_MULTI_LINE_FILE("ERROR:  Unable to read the private key password from file ''{0}'' because the file has multiple lines.  The file must have exactly one line, consisting only of the clear-text private key password."),



  /**
   * ERROR:  An error occurred while attempting to prompt for the private key password for alias ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_GET_PK_PW_PROMPT_ERROR("ERROR:  An error occurred while attempting to prompt for the private key password for alias ''{0}'':  {1}"),



  /**
   * ERROR:  The provided passwords do not match.
   */
  ERR_MANAGE_CERTS_GET_PK_PW_PROMPT_MISMATCH("ERROR:  The provided passwords do not match."),



  /**
   * ERROR:  The specified private key password is too short.  The password must contain at least six characters.
   */
  ERR_MANAGE_CERTS_GET_PK_PW_TOO_SHORT("ERROR:  The specified private key password is too short.  The password must contain at least six characters."),



  /**
   * The import operation was canceled and the key store was not updated.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_CANCELED("The import operation was canceled and the key store was not updated."),



  /**
   * ERROR:  An error occurred while trying to retrieve issuer certificate ''{0}'' from the key store or the JVM''s default set of trusted issuers to complete the certificate chain:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_CANNOT_GET_ISSUER("ERROR:  An error occurred while trying to retrieve issuer certificate ''{0}'' from the key store or the JVM''s default set of trusted issuers to complete the certificate chain:"),



  /**
   * ERROR:  Unable to convert the X.509 certificate with subject ''{0}'' into a Java certificate object suitable for importing into the key store:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_ERROR_CONVERTING_CERT("ERROR:  Unable to convert the X.509 certificate with subject ''{0}'' into a Java certificate object suitable for importing into the key store:"),



  /**
   * ERROR:  Unable to convert the PKCS #8 key read from file ''{0}'' into a Java PrivateKey object suitable for importing into the key store:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_ERROR_CONVERTING_KEY("ERROR:  Unable to convert the PKCS #8 key read from file ''{0}'' into a Java PrivateKey object suitable for importing into the key store:"),



  /**
   * ERROR:  An error occurred while attempting to add certificate ''{0}'' with alias ''{1}'' to the key store:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_ERROR_UPDATING_KS_WITH_CERT("ERROR:  An error occurred while attempting to add certificate ''{0}'' with alias ''{1}'' to the key store:"),



  /**
   * ERROR:  An error occurred while attempting to set the key entry for alias ''{0}'' with the private key and certificate chain:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_ERROR_UPDATING_KS_WITH_CHAIN("ERROR:  An error occurred while attempting to set the key entry for alias ''{0}'' with the private key and certificate chain:"),



  /**
   * ERROR:  The key store already contains a key with alias ''{0}'', but an error was encountered while attempting to retrieve that key and its associated certificate chain:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_INTO_KEY_ALIAS_CANNOT_GET_KEY("ERROR:  The key store already contains a key with alias ''{0}'', but an error was encountered while attempting to retrieve that key and its associated certificate chain:"),



  /**
   * ERROR:  The key store already contains a key pair and certificate chain with alias ''{0}'', and that key pair uses a different public key than the certificate to import.  A certificate can only be imported into an alias with an existing key pair if the certificate uses the same public key.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_INTO_KEY_ALIAS_KEY_MISMATCH("ERROR:  The key store already contains a key pair and certificate chain with alias ''{0}'', and that key pair uses a different public key than the certificate to import.  A certificate can only be imported into an alias with an existing key pair if the certificate uses the same public key."),



  /**
   * ERROR:  There are multiple certificates to import, but they do not form a valid certificate chain.  {0}
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_NEXT_NOT_ISSUER_OF_PREV("ERROR:  There are multiple certificates to import, but they do not form a valid certificate chain.  {0}"),



  /**
   * ERROR:  Certificate file ''{0}'' does not contain any certificates to import.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_NO_CERTS_IN_FILE("ERROR:  Certificate file ''{0}'' does not contain any certificates to import."),



  /**
   * ERROR:  The certificate with subject ''{0}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When importing a private key, or when importing a signed certificate into an alias with an existing private key, the entire certificate chain must be available.  Please locate this issuer certificate, and any other certificates higher up the issuer chain, so that the complete chain can be imported.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_NO_ISSUER_NO_AKI("ERROR:  The certificate with subject ''{0}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When importing a private key, or when importing a signed certificate into an alias with an existing private key, the entire certificate chain must be available.  Please locate this issuer certificate, and any other certificates higher up the issuer chain, so that the complete chain can be imported."),



  /**
   * ERROR:  The certificate with subject ''{0}'' and subject key identifier ''{1}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When importing a private key, or when importing a signed certificate into an alias with an existing private key, the entire certificate chain must be available.  Please locate this issuer certificate, and any other certificates higher up the issuer chain, so that the complete chain can be imported.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_NO_ISSUER_WITH_AKI("ERROR:  The certificate with subject ''{0}'' and subject key identifier ''{1}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When importing a private key, or when importing a signed certificate into an alias with an existing private key, the entire certificate chain must be available.  Please locate this issuer certificate, and any other certificates higher up the issuer chain, so that the complete chain can be imported."),



  /**
   * ERROR:  There are multiple certificates to import, but they do not form a valid certificate chain.  The certificate with subject DN ''{0}'' is self-signed, but it is not the last certificate in the set of certificates to import.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_SELF_SIGNED_NOT_LAST("ERROR:  There are multiple certificates to import, but they do not form a valid certificate chain.  The certificate with subject DN ''{0}'' is self-signed, but it is not the last certificate in the set of certificates to import."),



  /**
   * ERROR:  The key store already has a certificate with alias ''{0}''.  Please choose a different alias for the certificate to import.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_WITH_CONFLICTING_CERT_ALIAS("ERROR:  The key store already has a certificate with alias ''{0}''.  Please choose a different alias for the certificate to import."),



  /**
   * ERROR:  The import process would have resulted in issuer certificate ''{0}'' being assigned an alias of ''{1}'', which is already in use by another certificate or key in the key store.  Please choose a different alias to use as the base of the certificate chain, or import the issuer certificates manually with aliases that do not conflict.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_WITH_CONFLICTING_ISSUER_ALIAS("ERROR:  The import process would have resulted in issuer certificate ''{0}'' being assigned an alias of ''{1}'', which is already in use by another certificate or key in the key store.  Please choose a different alias to use as the base of the certificate chain, or import the issuer certificates manually with aliases that do not conflict."),



  /**
   * ERROR:  An error occurred while trying to check for an existing key or certificate with alias ''{0}'' in the key store:
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_WITH_PK_ALIAS_CONFLICT_ERROR("ERROR:  An error occurred while trying to check for an existing key or certificate with alias ''{0}'' in the key store:"),



  /**
   * ERROR:  Unable to import the private key and certificate chain into alias ''{0}'' because that alias is already associated with another certificate in the key store.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_WITH_PK_CERT_ALIAS_CONFLICT("ERROR:  Unable to import the private key and certificate chain into alias ''{0}'' because that alias is already associated with another certificate in the key store."),



  /**
   * ERROR:  Unable to import the private key and certificate chain into alias ''{0}'' because that alias is already associated with another key in the key store.
   */
  ERR_MANAGE_CERTS_IMPORT_CERT_WITH_PK_KEY_ALIAS_CONFLICT("ERROR:  Unable to import the private key and certificate chain into alias ''{0}'' because that alias is already associated with another key in the key store."),



  /**
   * ERROR:  Unable to infer the key store type for the key store held in file ''{0}'' because that file is empty and cannot represent either a valid JKS key store or PKCS #12 file.
   */
  ERR_MANAGE_CERTS_INFER_KS_TYPE_EMPTY_FILE("ERROR:  Unable to infer the key store type for the key store held in file ''{0}'' because that file is empty and cannot represent either a valid JKS key store or PKCS #12 file."),



  /**
   * ERROR:  Unable to infer the type for file ''{0}'' because an error occurred while reading from the file:  {1}
   */
  ERR_MANAGE_CERTS_INFER_KS_TYPE_ERROR_READING_FILE("ERROR:  Unable to infer the type for file ''{0}'' because an error occurred while reading from the file:  {1}"),



  /**
   * ERROR:  Unable to infer the key store type for the key store held in file ''{0}'' because the file had a first byte of {1}, which is not the expected first byte of either a JKS key store or a PKCS #12 file.
   */
  ERR_MANAGE_CERTS_INFER_KS_TYPE_UNEXPECTED_FIRST_BYTE("ERROR:  Unable to infer the key store type for the key store held in file ''{0}'' because the file had a first byte of {1}, which is not the expected first byte of either a JKS key store or a PKCS #12 file."),



  /**
   * ERROR:  The provided passwords do not match.
   */
  ERR_MANAGE_CERTS_KEY_KS_PW_PROMPT_MISMATCH("ERROR:  The provided passwords do not match."),



  /**
   * ERROR:  Unable to obtain a list of the aliases in key store ''{0}'':
   */
  ERR_MANAGE_CERTS_LIST_CERTS_CANNOT_GET_ALIASES("ERROR:  Unable to obtain a list of the aliases in key store ''{0}'':"),



  /**
   * ERROR:  An error occurred while attempting to retrieve the certificate with alias ''{0}'' from the key store:  {1}
   */
  ERR_MANAGE_CERTS_LIST_CERTS_ERROR_GETTING_CERT("ERROR:  An error occurred while attempting to retrieve the certificate with alias ''{0}'' from the key store:  {1}"),



  /**
   * WARNING:  Unable to verify the signature for this certificate because issuer certificate ''{0}'' could not be located in either the specified key store or the default set of JVM trusted issuers.
   */
  ERR_MANAGE_CERTS_LIST_CERTS_VERIFY_SIGNATURE_NO_ISSUER("WARNING:  Unable to verify the signature for this certificate because issuer certificate ''{0}'' could not be located in either the specified key store or the default set of JVM trusted issuers."),



  /**
   * ERROR:  No subcommand was selected.
   */
  ERR_MANAGE_CERTS_NO_SUBCOMMAND("ERROR:  No subcommand was selected."),



  /**
   * ERROR:  The password must not be empty.
   */
  ERR_MANAGE_CERTS_PROMPT_FOR_PW_EMPTY_PW("ERROR:  The password must not be empty."),



  /**
   * ERROR:  Your response must be either ''yes'' or ''no''.
   */
  ERR_MANAGE_CERTS_PROMPT_FOR_YES_NO_INVALID_RESPONSE("ERROR:  Your response must be either ''yes'' or ''no''."),



  /**
   * ERROR:  An error occurred while trying to read the response from standard input:  {0}
   */
  ERR_MANAGE_CERTS_PROMPT_FOR_YES_NO_READ_ERROR("ERROR:  An error occurred while trying to read the response from standard input:  {0}"),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer.
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_DATA_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer."),



  /**
   * ERROR:  Unable to read an ASN.1 DER element from certificate file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_DER_NOT_VALID_ASN1("ERROR:  Unable to read an ASN.1 DER element from certificate file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to decode a DER element read from certificate file ''{0}'' as an X.509 certificate:  {1}
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_DER_NOT_VALID_CERT("ERROR:  Unable to decode a DER element read from certificate file ''{0}'' as an X.509 certificate:  {1}"),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains an ''END CERTIFICATE'' footer without a corresponding ''BEGIN CERTIFICATE'' header.
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_END_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains an ''END CERTIFICATE'' footer without a corresponding ''BEGIN CERTIFICATE'' header."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the end of the file was reached before finding an ''END CERTIFICATE'' footer to mark the end of the current certificate.
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_EOF_WITHOUT_END("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the end of the file was reached before finding an ''END CERTIFICATE'' footer to mark the end of the current certificate."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains multiple ''BEGIN CERTIFICATE'' headers without an ''END CERTIFICATE'' footer between them.
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_MULTIPLE_BEGIN("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the file contains multiple ''BEGIN CERTIFICATE'' headers without an ''END CERTIFICATE'' footer between them."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the data read between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer is not valid base64-encoded data:  {0}
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_PEM_CERT_NOT_BASE64("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the data read between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer is not valid base64-encoded data:  {0}"),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the data read between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer could not be parsed as a valid X.509 certificate:  {1}
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_PEM_CERT_NOT_CERT("ERROR:  Unable to read a PEM-encoded certificate from file ''{0}'' because the data read between a ''BEGIN CERTIFICATE'' header and an ''END CERTIFICATE'' footer could not be parsed as a valid X.509 certificate:  {1}"),



  /**
   * ERROR:  An error occurred while attempting to read the certificates from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_CERTS_FROM_FILE_READ_ERROR("ERROR:  An error occurred while attempting to read the certificates from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a begin header and an end footer.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_DATA_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a begin header and an end footer."),



  /**
   * ERROR:  Unable to read an ASN.1 DER element from certificate signing request file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_DER_NOT_VALID_ASN1("ERROR:  Unable to read an ASN.1 DER element from certificate signing request file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to decode a DER element read from certificate signing request file ''{0}'' as a PKCS #10 certificate signing request:  {1}
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_DER_NOT_VALID_CSR("ERROR:  Unable to decode a DER element read from certificate signing request file ''{0}'' as a PKCS #10 certificate signing request:  {1}"),



  /**
   * ERROR:  Unable to read a certificate signing request from file ''{0}'' because the file is empty or does not contain a request.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_EMPTY_FILE("ERROR:  Unable to read a certificate signing request from file ''{0}'' because the file is empty or does not contain a request."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains an end footer without a corresponding begin header.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_END_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains an end footer without a corresponding begin header."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the end of the file was reached before finding an end footer to mark the end of the request.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_EOF_WITHOUT_END("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the end of the file was reached before finding an end footer to mark the end of the request."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains multiple begin headers.  A certificate signing request file may only contain a single request.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_MULTIPLE_BEGIN("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the file contains multiple begin headers.  A certificate signing request file may only contain a single request."),



  /**
   * ERROR:  Unable to read a certificate signing request from file ''{0}'' because that file contains multiple requests.  The certificate signing request file is only allowed to have a single request.
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_MULTIPLE_CSRS("ERROR:  Unable to read a certificate signing request from file ''{0}'' because that file contains multiple requests.  The certificate signing request file is only allowed to have a single request."),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the data read between a begin header and an end footer is not valid base64-encoded data:  {0}
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_PEM_CSR_NOT_BASE64("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the data read between a begin header and an end footer is not valid base64-encoded data:  {0}"),



  /**
   * ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the data read between the begin header and end footer could not be parsed as a valid PKCS #10 certificate signing request:  {1}
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_PEM_CSR_NOT_CSR("ERROR:  Unable to read a PEM-encoded certificate signing request from file ''{0}'' because the data read between the begin header and end footer could not be parsed as a valid PKCS #10 certificate signing request:  {1}"),



  /**
   * ERROR:  An error occurred while trying to read a certificate signing request from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_CSR_FROM_FILE_READ_ERROR("ERROR:  An error occurred while trying to read a certificate signing request from file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a ''BEGIN PRIVATE KEY'' header and an ''END PRIVATE KEY'' footer.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_DATA_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains a non-empty, non-comment line that does not appear between a ''BEGIN PRIVATE KEY'' header and an ''END PRIVATE KEY'' footer."),



  /**
   * ERROR:  Unable to read an ASN.1 DER element from private key file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_DER_NOT_VALID_ASN1("ERROR:  Unable to read an ASN.1 DER element from private key file ''{0}'':  {1}"),



  /**
   * ERROR:  Unable to decode a DER element read from certificate file ''{0}'' as an PKCS #8 private key:  {1}
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_DER_NOT_VALID_PK("ERROR:  Unable to decode a DER element read from certificate file ''{0}'' as an PKCS #8 private key:  {1}"),



  /**
   * ERROR:  Unable to read a private key from file ''{0}'' because the file is empty or does not contain a private key.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_EMPTY_FILE("ERROR:  Unable to read a private key from file ''{0}'' because the file is empty or does not contain a private key."),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains an ''END PRIVATE KEY'' footer without a corresponding ''BEGIN PRIVATE KEY'' header.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_END_WITHOUT_BEGIN("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains an ''END PRIVATE KEY'' footer without a corresponding ''BEGIN PRIVATE KEY'' header."),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the end of the file was reached before finding an ''END PRIVATE KEY'' footer to mark the end of the key.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_EOF_WITHOUT_END("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the end of the file was reached before finding an ''END PRIVATE KEY'' footer to mark the end of the key."),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains multiple ''BEGIN PRIVATE KEY'' headers.  A private key file may only contain a single private key.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_MULTIPLE_BEGIN("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the file contains multiple ''BEGIN PRIVATE KEY'' headers.  A private key file may only contain a single private key."),



  /**
   * ERROR:  Unable to read a private key from file ''{0}'' because that file contains multiple keys.  The private key file is only allowed to have a single private key.
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_MULTIPLE_KEYS("ERROR:  Unable to read a private key from file ''{0}'' because that file contains multiple keys.  The private key file is only allowed to have a single private key."),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the data read between a ''BEGIN PRIVATE KEY'' header and an ''END PRIVATE KEY'' footer is not valid base64-encoded data:  {0}
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_PEM_PK_NOT_BASE64("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the data read between a ''BEGIN PRIVATE KEY'' header and an ''END PRIVATE KEY'' footer is not valid base64-encoded data:  {0}"),



  /**
   * ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the data read between the ''BEGIN PRIVATE KEY'' header and ''END PRIVATE KEY'' footer could not be parsed as a valid PKCS #8 private key:  {1}
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_PEM_PK_NOT_PK("ERROR:  Unable to read a PEM-encoded private key from file ''{0}'' because the data read between the ''BEGIN PRIVATE KEY'' header and ''END PRIVATE KEY'' footer could not be parsed as a valid PKCS #8 private key:  {1}"),



  /**
   * ERROR:  An error occurred while trying to read a private key from file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_READ_PK_FROM_FILE_READ_ERROR("ERROR:  An error occurred while trying to read a private key from file ''{0}'':  {1}"),



  /**
   * An error occurred while attempting to write certificate information to file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_RETRIEVE_CERT_CANNOT_WRITE_TO_FILE("An error occurred while attempting to write certificate information to file ''{0}'':  {1}"),



  /**
   * ERROR:  Received an empty certificate chain from the server.
   */
  ERR_MANAGE_CERTS_RETRIEVE_CERT_EMPTY_CHAIN("ERROR:  Received an empty certificate chain from the server."),



  /**
   * ERROR:  Did not receive the certificate chain from {0} after waiting for up to 90 seconds.
   */
  ERR_MANAGE_CERTS_RETRIEVE_CERT_NO_CERT_CHAIN_RECEIVED("ERROR:  Did not receive the certificate chain from {0} after waiting for up to 90 seconds."),



  /**
   * VM exit is not allowed.
   */
  ERR_MANAGE_CERTS_SECURITY_MANAGER_EXIT_NOT_ALLOWED("VM exit is not allowed."),



  /**
   * ERROR:  Alias ''{0}'' is already in use in the key store.
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_ALIAS_IN_USE("ERROR:  Alias ''{0}'' is already in use in the key store."),



  /**
   * ERROR:  Unable to read the response to the prompt:
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_CANNOT_READ_PROMPT_RESPONSE("ERROR:  Unable to read the response to the prompt:"),



  /**
   * The server certificate chain was rejected and the key store has not been updated.
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_CHAIN_REJECTED("The server certificate chain was rejected and the key store has not been updated."),



  /**
   * ERROR:  An error occurred while trying to add certificate ''{0}'' to the key store:
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_ERROR_ADDING_CERT_TO_KS("ERROR:  An error occurred while trying to add certificate ''{0}'' to the key store:"),



  /**
   * ERROR:  You must enter either ''yes'' to trust the certificate chain and add it into the key store, or ''no'' to reject it and exit.
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_INVALID_PROMPT_RESPONSE("ERROR:  You must enter either ''yes'' to trust the certificate chain and add it into the key store, or ''no'' to reject it and exit."),



  /**
   * ERROR:  Did not receive the certificate chain from {0} after waiting for up to 90 seconds.
   */
  ERR_MANAGE_CERTS_TRUST_SERVER_NO_CERT_CHAIN_RECEIVED("ERROR:  Did not receive the certificate chain from {0} after waiting for up to 90 seconds."),



  /**
   * ERROR:  Unrecognized subcommand ''{0}''.
   */
  ERR_MANAGE_CERTS_UNKNOWN_SUBCOMMAND("ERROR:  Unrecognized subcommand ''{0}''."),



  /**
   * ERROR:  Unable to write a backup copy of existing key store ''{0}'' to file ''{1}'':  {2}
   */
  ERR_MANAGE_CERTS_WRITE_KS_ERROR_COPYING_EXISTING_KS("ERROR:  Unable to write a backup copy of existing key store ''{0}'' to file ''{1}'':  {2}"),



  /**
   * ERROR:  Unable to delete the temporary backup ''{0}'' of key store ''{1}'':  {2}
   */
  ERR_MANAGE_CERTS_WRITE_KS_ERROR_DELETING_KS_BACKUP("ERROR:  Unable to delete the temporary backup ''{0}'' of key store ''{1}'':  {2}"),



  /**
   * ERROR:  Unable to write updates to key store file ''{0}'':  {1}.  A backup copy of the previous version of the key store is available as ''{2}''.
   */
  ERR_MANAGE_CERTS_WRITE_KS_ERROR_OVERWRITING_KS("ERROR:  Unable to write updates to key store file ''{0}'':  {1}.  A backup copy of the previous version of the key store is available as ''{2}''."),



  /**
   * ERROR:  Unable to write key store file ''{0}'':  {1}
   */
  ERR_MANAGE_CERTS_WRITE_KS_ERROR_WRITING_NEW_KS("ERROR:  Unable to write key store file ''{0}'':  {1}"),



  /**
   * Unable to base64-decode the PEM-encoded private key data.
   */
  ERR_PKCS8_PEM_READER_CANNOT_BASE64_DECODE("Unable to base64-decode the PEM-encoded private key data."),



  /**
   * Unable to read a PEM-encoded PKCS #8 private key because unexpected data was found before encountering a ''{0}'' header.
   */
  ERR_PKCS8_PEM_READER_DATA_WITHOUT_BEGIN("Unable to read a PEM-encoded PKCS #8 private key because unexpected data was found before encountering a ''{0}'' header."),



  /**
   * Unable to read a PEM-encoded PKCS #8 private key because an unexpected ''{0}'' footer was found without a preceding ''{1}'' header.
   */
  ERR_PKCS8_PEM_READER_END_WITHOUT_BEGIN("Unable to read a PEM-encoded PKCS #8 private key because an unexpected ''{0}'' footer was found without a preceding ''{1}'' header."),



  /**
   * Unable to read a PEM-encoded PKCS #8 private key because the ''{0}'' footer was encountered immediately after the ''{1}'' header and without reading any base64-encoded private key data.
   */
  ERR_PKCS8_PEM_READER_END_WITHOUT_DATA("Unable to read a PEM-encoded PKCS #8 private key because the ''{0}'' footer was encountered immediately after the ''{1}'' header and without reading any base64-encoded private key data."),



  /**
   * Unable to read a PEM-encoded PKCS #8 private key because the end of the data was encountered without finding an expected ''{0}'' footer that corresponds to the ''{1}'' header.
   */
  ERR_PKCS8_PEM_READER_EOF_WITHOUT_END("Unable to read a PEM-encoded PKCS #8 private key because the end of the data was encountered without finding an expected ''{0}'' footer that corresponds to the ''{1}'' header."),



  /**
   * Unable to read a PEM-encoded PKCS #8 private key because an unexpected ''{0}'' header was found after having already begun the process of decoding a private key.
   */
  ERR_PKCS8_PEM_READER_REPEATED_BEGIN("Unable to read a PEM-encoded PKCS #8 private key because an unexpected ''{0}'' header was found after having already begun the process of decoding a private key."),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the private key algorithm:  {0}
   */
  ERR_PRIVATE_KEY_DECODE_CANNOT_PARSE_ALGORITHM("Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the private key algorithm:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the public key:  {0}
   */
  ERR_PRIVATE_KEY_DECODE_CANNOT_PARSE_PUBLIC_KEY("Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the public key:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the private key version:  {0}
   */
  ERR_PRIVATE_KEY_DECODE_CANNOT_PARSE_VERSION("Unable to decode the provided byte array as a PKCS #8 private key because an error occurred while trying to parse the private key version:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because the private key sequence only had {0,number,0} elements, while the sequence should have a minimum of three elements.
   */
  ERR_PRIVATE_KEY_DECODE_NOT_ENOUGH_ELEMENTS("Unable to decode the provided byte array as a PKCS #8 private key because the private key sequence only had {0,number,0} elements, while the sequence should have a minimum of three elements."),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because the contents of the array could not be parsed as a DER sequence:  {0}
   */
  ERR_PRIVATE_KEY_DECODE_NOT_SEQUENCE("Unable to decode the provided byte array as a PKCS #8 private key because the contents of the array could not be parsed as a DER sequence:  {0}"),



  /**
   * Unable to decode the provided byte array as a PKCS #8 private key because it appears to have a version number of {0,number,0}, which is not a supported version.  Only versions 1 and 2 are supported.
   */
  ERR_PRIVATE_KEY_DECODE_UNSUPPORTED_VERSION("Unable to decode the provided byte array as a PKCS #8 private key because it appears to have a version number of {0,number,0}, which is not a supported version.  Only versions 1 and 2 are supported."),



  /**
   * An error occurred while trying to encode PKCS #8 private key {0}:  {1}
   */
  ERR_PRIVATE_KEY_ENCODE_ERROR("An error occurred while trying to encode PKCS #8 private key {0}:  {1}"),



  /**
   * An error occurred while trying to wrap an RSA private key in a PKCS #8 private key envelope:  {0}
   */
  ERR_PRIVATE_KEY_WRAP_RSA_KEY_ERROR("An error occurred while trying to wrap an RSA private key in a PKCS #8 private key envelope:  {0}"),



  /**
   * Unable to decode the PKCS #8 private key as an RSA private key:  {0}
   */
  ERR_RSA_PRIVATE_KEY_CANNOT_DECODE("Unable to decode the PKCS #8 private key as an RSA private key:  {0}"),



  /**
   * Unable to decode the PKCS #8 private key as an RSA private key because it has an unsupported version of {0,number,0}.  Only versions 0 and 1 are supported.
   */
  ERR_RSA_PRIVATE_KEY_UNSUPPORTED_VERSION("Unable to decode the PKCS #8 private key as an RSA private key because it has an unsupported version of {0,number,0}.  Only versions 0 and 1 are supported."),



  /**
   * Unable to decode the X.509 certificate public key as an RSA public key:  {0}
   */
  ERR_RSA_PUBLIC_KEY_CANNOT_DECODE("Unable to decode the X.509 certificate public key as an RSA public key:  {0}"),



  /**
   * Unable to parse the provided X.509 certificate extension {0} as a subject key identifier extension:  {1}
   */
  ERR_SUBJECT_KEY_ID_EXTENSION_CANNOT_PARSE("Unable to parse the provided X.509 certificate extension {0} as a subject key identifier extension:  {1}"),



  /**
   * Unable to base64-decode the PEM-encoded certificate data.
   */
  ERR_X509_PEM_READER_CANNOT_BASE64_DECODE("Unable to base64-decode the PEM-encoded certificate data."),



  /**
   * Unable to read a PEM-encoded X.509 certificate because unexpected data was found before encountering a ''{0}'' header.
   */
  ERR_X509_PEM_READER_DATA_WITHOUT_BEGIN("Unable to read a PEM-encoded X.509 certificate because unexpected data was found before encountering a ''{0}'' header."),



  /**
   * Unable to read a PEM-encoded X.509 certificate because an unexpected ''{0}'' footer was found without a preceding ''{1}'' header.
   */
  ERR_X509_PEM_READER_END_WITHOUT_BEGIN("Unable to read a PEM-encoded X.509 certificate because an unexpected ''{0}'' footer was found without a preceding ''{1}'' header."),



  /**
   * Unable to read a PEM-encoded X.509 certificate because the ''{0}'' footer was encountered immediately after the ''{1}'' header and without reading any base64-encoded certificate data.
   */
  ERR_X509_PEM_READER_END_WITHOUT_DATA("Unable to read a PEM-encoded X.509 certificate because the ''{0}'' footer was encountered immediately after the ''{1}'' header and without reading any base64-encoded certificate data."),



  /**
   * Unable to read a PEM-encoded X.509 certificate because the end of the data was encountered without finding an expected ''{0}'' footer that corresponds to the ''{1}'' header.
   */
  ERR_X509_PEM_READER_EOF_WITHOUT_END("Unable to read a PEM-encoded X.509 certificate because the end of the data was encountered without finding an expected ''{0}'' footer that corresponds to the ''{1}'' header."),



  /**
   * Unable to read a PEM-encoded X.509 certificate because an unexpected ''{0}'' header was found after having already begun the process of decoding a certificate.
   */
  ERR_X509_PEM_READER_REPEATED_BEGIN("Unable to read a PEM-encoded X.509 certificate because an unexpected ''{0}'' header was found after having already begun the process of decoding a certificate."),



  /**
   * Authority Key Identifier
   */
  INFO_AUTHORITY_KEY_ID_EXTENSION_NAME("Authority Key Identifier"),



  /**
   * Basic Constraints
   */
  INFO_BASIC_CONSTRAINTS_EXTENSION_NAME("Basic Constraints"),



  /**
   * The certificate with subject DN ''{0}'' is not the issuer for certificate with subject DN ''{1}'' because that certificate has an issuer DN of ''{2}''.
   */
  INFO_CERT_IS_ISSUER_FOR_DN_MISMATCH("The certificate with subject DN ''{0}'' is not the issuer for certificate with subject DN ''{1}'' because that certificate has an issuer DN of ''{2}''."),



  /**
   * The certificate with subject DN ''{0}'' is not the issuer for certificate with subject DN ''{1}'' because the authority key identifier for certificate ''{1}'' does not match the subject key identifier for certificate ''{0}''.
   */
  INFO_CERT_IS_ISSUER_FOR_KEY_ID_MISMATCH("The certificate with subject DN ''{0}'' is not the issuer for certificate with subject DN ''{1}'' because the authority key identifier for certificate ''{1}'' does not match the subject key identifier for certificate ''{0}''."),



  /**
   * CRL Distribution Points
   */
  INFO_CRL_DP_EXTENSION_NAME("CRL Distribution Points"),



  /**
   * Extended Key Usage
   */
  INFO_EXTENDED_KEY_USAGE_EXTENSION_NAME("Extended Key Usage"),



  /**
   * Code Signing
   */
  INFO_EXTENDED_KEY_USAGE_ID_CODE_SIGNING("Code Signing"),



  /**
   * Email Protection
   */
  INFO_EXTENDED_KEY_USAGE_ID_EMAIL_PROTECTION("Email Protection"),



  /**
   * OCSP Signing
   */
  INFO_EXTENDED_KEY_USAGE_ID_OCSP_SIGNING("OCSP Signing"),



  /**
   * Time Stamping
   */
  INFO_EXTENDED_KEY_USAGE_ID_TIME_STAMPING("Time Stamping"),



  /**
   * TLS Client Authentication
   */
  INFO_EXTENDED_KEY_USAGE_ID_TLS_CLIENT_AUTHENTICATION("TLS Client Authentication"),



  /**
   * TLS Server Authentication
   */
  INFO_EXTENDED_KEY_USAGE_ID_TLS_SERVER_AUTHENTICATION("TLS Server Authentication"),



  /**
   * Issuer Alternative Name
   */
  INFO_ISSUER_ALT_NAME_EXTENSION_NAME("Issuer Alternative Name"),



  /**
   * Key Usage
   */
  INFO_KEY_USAGE_EXTENSION_NAME("Key Usage"),



  /**
   * # Approximately equivalent keytool command:
   */
  INFO_MANAGE_CERTS_APPROXIMATE_KEYTOOL_COMMAND("# Approximately equivalent keytool command:"),



  /**
   * Beginning TLS negotiation on the connection ...
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_BEGINNING_TLS_NEGOTIATION("Beginning TLS negotiation on the connection ..."),



  /**
   * Connected successfully.
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_CONNECTED("Connected successfully."),



  /**
   * Connecting to {0} ...
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_CONNECTING("Connecting to {0} ..."),



  /**
   * This connection was only established for the purpose of obtaining the server''s certificate chain.  That certificate chain has been acquired, so this connection is no longer needed and TLS negotiation can be aborted.
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_CONNECTION_DONE("This connection was only established for the purpose of obtaining the server''s certificate chain.  That certificate chain has been acquired, so this connection is no longer needed and TLS negotiation can be aborted."),



  /**
   * Successfully retrieved the server certificate chain.
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_GOT_CERT_CHAIN("Successfully retrieved the server certificate chain."),



  /**
   * Negotiated TLS protocol:  {0}
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_NEGOTIATED_TLS_PROTOCOL("Negotiated TLS protocol:  {0}"),



  /**
   * Negotiated TLS cipher suite:  {0}
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_NEGOTIATED_TLS_SUITE("Negotiated TLS cipher suite:  {0}"),



  /**
   * Sending an LDAP StartTLS extended request to the server ...
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_SENDING_START_TLS("Sending an LDAP StartTLS extended request to the server ..."),



  /**
   * The server accepted the StartTLS request.
   */
  INFO_MANAGE_CERTS_CERT_COLLECTOR_START_TLS_SUCCESSFUL("The server accepted the StartTLS request."),



  /**
   * Successfully changed the alias from ''{0}'' to ''{1}''.
   */
  INFO_MANAGE_CERTS_CHANGE_ALIAS_SUCCESSFUL("Successfully changed the alias from ''{0}'' to ''{1}''."),



  /**
   * Successfully changed the password for key store ''{0}''.
   */
  INFO_MANAGE_CERTS_CHANGE_KS_PW_SUCCESSFUL("Successfully changed the password for key store ''{0}''."),



  /**
   * Successfully changed the private key password for alias ''{0}''.
   */
  INFO_MANAGE_CERTS_CHANGE_PK_PW_SUCCESSFUL("Successfully changed the private key password for alias ''{0}''."),



  /**
   * NOTICE:  CA certificate ''{0}'' was not found in the JVM''s default set of trusted certificates.  Clients will likely need special configuration to trust this certificate chain.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_CA_NOT_IN_JVM_DEFAULT_TS("NOTICE:  CA certificate ''{0}'' was not found in the JVM''s default set of trusted certificates.  Clients will likely need special configuration to trust this certificate chain."),



  /**
   * OK:  CA certificate ''{0}'' was found in the JVM''s default set of trusted certificates.  Most clients will likely trust this issuer.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_CA_TRUSTED_OK("OK:  CA certificate ''{0}'' was found in the JVM''s default set of trusted certificates.  Most clients will likely trust this issuer."),



  /**
   * OK:  Certificate ''{0}'' has a valid signature.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_CERT_SIGNATURE_VALID("OK:  Certificate ''{0}'' has a valid signature."),



  /**
   * OK:  The certificate chain is complete.  Each subsequent certificate is the issuer for the previous certificate in the chain, and the chain ends with a self-signed certificate.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_CHAIN_COMPLETE("OK:  The certificate chain is complete.  Each subsequent certificate is the issuer for the previous certificate in the chain, and the chain ends with a self-signed certificate."),



  /**
   * OK:  Certificate ''{0}'' at the head of the chain includes an extended key usage extension, and that extension includes the ''serverAuth'' usage.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_END_CERT_GOOD_EKU("OK:  Certificate ''{0}'' at the head of the chain includes an extended key usage extension, and that extension includes the ''serverAuth'' usage."),



  /**
   * OK:  Certificate ''{0}'' will expire at {1}, which is not in the near future.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_END_CERT_VALIDITY_OK("OK:  Certificate ''{0}'' will expire at {1}, which is not in the near future."),



  /**
   * Successfully retrieved the certificate chain for alias ''{0}'':
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_GOT_CHAIN("Successfully retrieved the certificate chain for alias ''{0}'':"),



  /**
   * OK:  Issuer certificate ''{0}'' includes a basic constraints extension, and the certificate chain satisfies those constraints.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_GOOD_BC("OK:  Issuer certificate ''{0}'' includes a basic constraints extension, and the certificate chain satisfies those constraints."),



  /**
   * OK:  Issuer certificate ''{0}'' will expire at {1}, which is not in the near future.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_VALIDITY_OK("OK:  Issuer certificate ''{0}'' will expire at {1}, which is not in the near future."),



  /**
   * OK:  Issuer certificate ''{0}'' includes a key usage extension with the keyCertSign usage flag set to true.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_ISSUER_GOOD_KU("OK:  Issuer certificate ''{0}'' includes a key usage extension with the keyCertSign usage flag set to true."),



  /**
   * No usability errors or warnings were identified while validating the certificate chain.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_NO_ERRORS_OR_WARNINGS("No usability errors or warnings were identified while validating the certificate chain."),



  /**
   * OK:  Certificate ''{0}'' has a {1,number,0}-bit RSA public key, which is considered strong.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_RSA_MODULUS_OK("OK:  Certificate ''{0}'' has a {1,number,0}-bit RSA public key, which is considered strong."),



  /**
   * OK:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is considered strong.
   */
  INFO_MANAGE_CERTS_CHECK_USABILITY_SIG_ALG_OK("OK:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is considered strong."),



  /**
   * The following certificate will be deleted from the key store:
   */
  INFO_MANAGE_CERTS_DELETE_CERT_CONFIRM_DELETE_CERT("The following certificate will be deleted from the key store:"),



  /**
   * The following certificate chain will be deleted from the key store, along with its corresponding private key:
   */
  INFO_MANAGE_CERTS_DELETE_CERT_CONFIRM_DELETE_CHAIN("The following certificate chain will be deleted from the key store, along with its corresponding private key:"),



  /**
   * Successfully deleted the certificate from the key store.
   */
  INFO_MANAGE_CERTS_DELETE_CERT_DELETED_CERT("Successfully deleted the certificate from the key store."),



  /**
   * Successfully deleted the certificate chain and its associated private key from the key store.
   */
  INFO_MANAGE_CERTS_DELETE_CERT_DELETED_CHAIN("Successfully deleted the certificate chain and its associated private key from the key store."),



  /**
   * Do you really want to delete this entry from the key store?
   */
  INFO_MANAGE_CERTS_DELETE_CERT_PROMPT_DELETE("Do you really want to delete this entry from the key store?"),



  /**
   * There are no certificates in file ''{0}''.
   */
  INFO_MANAGE_CERTS_DISPLAY_CERT_NO_CERTS("There are no certificates in file ''{0}''."),



  /**
   * Updates the ''{0}'' key store to change the alias of the ''server-cert'' certificate to be ''server-certificate''.  Also, display a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_CHANGE_ALIAS_1("Updates the ''{0}'' key store to change the alias of the ''server-cert'' certificate to be ''server-certificate''.  Also, display a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Examines the ''server-cert'' certificate in the ''{0}'' key store to determine whether that certificate is suitable for use as a TLS server certificate.
   */
  INFO_MANAGE_CERTS_EXAMPLE_CHECK_USABILITY_1("Examines the ''server-cert'' certificate in the ''{0}'' key store to determine whether that certificate is suitable for use as a TLS server certificate."),



  /**
   * Deletes the certificate stored in the ''server-cert'' alias in the ''{0}'' key store.
   */
  INFO_MANAGE_CERTS_EXAMPLE_DELETE_1("Deletes the certificate stored in the ''server-cert'' alias in the ''{0}'' key store."),



  /**
   * Displays verbose information about all of the certificates contained in file ''{0}'', along with a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_DISPLAY_CERT_1("Displays verbose information about all of the certificates contained in file ''{0}'', along with a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Displays information about the certificate signing request contained in file ''{0}'', along with a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_DISPLAY_CSR_1("Displays information about the certificate signing request contained in file ''{0}'', along with a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Exports a PEM-formatted representation of the certificate contained in the ''server-cert'' alias in the ''{0}'' key store and writes it to the ''{1}'' output file.  Also, display a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_EXPORT_CERT_1("Exports a PEM-formatted representation of the certificate contained in the ''server-cert'' alias in the ''{0}'' key store and writes it to the ''{1}'' output file.  Also, display a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Exports a PEM-formatted representation of the private key contained in the ''server-cert'' alias in the ''{0}'' key store and writes it to the ''{1}'' output file.
   */
  INFO_MANAGE_CERTS_EXAMPLE_EXPORT_KEY_1("Exports a PEM-formatted representation of the private key contained in the ''server-cert'' alias in the ''{0}'' key store and writes it to the ''{1}'' output file."),



  /**
   * Generates a self-signed certificate with alias ''ca-cert'' in the ''{0}'' key store.  If the key store does not already exist, then it will be created using the standard PKCS #12 format.  The certificate will have a subject DN of ''CN=Example Authority,O=Example Corporation,C=US'', a 4096-bit RSA key, and a signature generated using the SHA256withRSA algorithm.  The certificate will be valid for 7300 days starting at midnight local time on January 1, 2017.  It will include a basic constraints extension that indicates the certificate can act as a certification authority, and a key usage extension that indicates that the key can be used for signing certificates and CRLs.  Also, display a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_GEN_CERT_1("Generates a self-signed certificate with alias ''ca-cert'' in the ''{0}'' key store.  If the key store does not already exist, then it will be created using the standard PKCS #12 format.  The certificate will have a subject DN of ''CN=Example Authority,O=Example Corporation,C=US'', a 4096-bit RSA key, and a signature generated using the SHA256withRSA algorithm.  The certificate will be valid for 7300 days starting at midnight local time on January 1, 2017.  It will include a basic constraints extension that indicates the certificate can act as a certification authority, and a key usage extension that indicates that the key can be used for signing certificates and CRLs.  Also, display a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Generates a certificate signing request for a new certificate with subject ''CN=ldap.example.com,O=Example Corporation,C=US'' that will be stored in the ''server-cert'' alias in the ''{0}'' key store.  A new 256-bit elliptic curve key pair will be created, the request will be signed with the SHA256withECDSA signature algorithm, and the request will include a subject alternative name extension with alternate DNS names of ''ldap1.example.com'' and ''ldap2.example.com'', and an extended key usage extension to indicate that the certificate should be usable for either TLS server authentication or TLS client authentication.  The certificate signing request will be written in PEM format to output file ''{1}''.
   */
  INFO_MANAGE_CERTS_EXAMPLE_GEN_CSR_1("Generates a certificate signing request for a new certificate with subject ''CN=ldap.example.com,O=Example Corporation,C=US'' that will be stored in the ''server-cert'' alias in the ''{0}'' key store.  A new 256-bit elliptic curve key pair will be created, the request will be signed with the SHA256withECDSA signature algorithm, and the request will include a subject alternative name extension with alternate DNS names of ''ldap1.example.com'' and ''ldap2.example.com'', and an extended key usage extension to indicate that the certificate should be usable for either TLS server authentication or TLS client authentication.  The certificate signing request will be written in PEM format to output file ''{1}''."),



  /**
   * Generates a certificate signing request intended to renew the existing certificate stored in alias ''server-cert'' in the ''{0}'' key store.  The request will use the same subject DN and set of extensions as the certificate currently stored in that alias, and it will be written to standard output in PEM format.
   */
  INFO_MANAGE_CERTS_EXAMPLE_GEN_CSR_2("Generates a certificate signing request intended to renew the existing certificate stored in alias ''server-cert'' in the ''{0}'' key store.  The request will use the same subject DN and set of extensions as the certificate currently stored in that alias, and it will be written to standard output in PEM format."),



  /**
   * Displays a list of the subcommands available for use with this tool.
   */
  INFO_MANAGE_CERTS_EXAMPLE_HELP_SUBCOMMANDS_1("Displays a list of the subcommands available for use with this tool."),



  /**
   * Imports a certificate chain read from file ''{0}'' and the corresponding private key read from file ''{1}'' into the ''server-cert'' alias in the ''{2}'' key store.  If the key store does not already exist, then it will be created using the JKS key store format.
   */
  INFO_MANAGE_CERTS_EXAMPLE_IMPORT_1("Imports a certificate chain read from file ''{0}'' and the corresponding private key read from file ''{1}'' into the ''server-cert'' alias in the ''{2}'' key store.  If the key store does not already exist, then it will be created using the JKS key store format."),



  /**
   * List verbose information about each of the certificates in key store file ''{0}''.  Also, display a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_LIST_1("List verbose information about each of the certificates in key store file ''{0}''.  Also, display a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Uses the ''ca-cert'' certificate in key store ''{0}'' to sign the certificate signing request (CSR) contained in file ''{1}'' and writes the signed certificate to PEM-formatted output file ''{2}''.  The signed certificate will use the subject DN and set of extensions included in the request, and the resulting certificate will be valid for 730 days, starting immediately.  Also, display a command that can be used to obtain a similar result with the Java keytool utility.
   */
  INFO_MANAGE_CERTS_EXAMPLE_SIGN_CERT_1("Uses the ''ca-cert'' certificate in key store ''{0}'' to sign the certificate signing request (CSR) contained in file ''{1}'' and writes the signed certificate to PEM-formatted output file ''{2}''.  The signed certificate will use the subject DN and set of extensions included in the request, and the resulting certificate will be valid for 730 days, starting immediately.  Also, display a command that can be used to obtain a similar result with the Java keytool utility."),



  /**
   * Connects to the ldap.example.com server on port 636 to retrieve the certificate chain that the server presents during TLS negotiation.  That certificate chain will be added to the ''{0}'' key store with a base alias of ''ldap.example.com:636'' after interactively confirming that the certificate chain should be trusted.
   */
  INFO_MANAGE_CERTS_EXAMPLE_TRUST_SERVER_1("Connects to the ldap.example.com server on port 636 to retrieve the certificate chain that the server presents during TLS negotiation.  That certificate chain will be added to the ''{0}'' key store with a base alias of ''ldap.example.com:636'' after interactively confirming that the certificate chain should be trusted."),



  /**
   * Successfully exported the following certificate to ''{0}'':
   */
  INFO_MANAGE_CERTS_EXPORT_CERT_EXPORT_SUCCESSFUL("Successfully exported the following certificate to ''{0}'':"),



  /**
   * Successfully exported the private key.
   */
  INFO_MANAGE_CERTS_EXPORT_KEY_EXPORT_SUCCESSFUL("Successfully exported the private key."),



  /**
   * {0} at {1} ({2} from now)
   */
  INFO_MANAGE_CERTS_FORMAT_DATE_AND_TIME_IN_FUTURE("{0} at {1} ({2} from now)"),



  /**
   * {0} at {1} ({2} ago)
   */
  INFO_MANAGE_CERTS_FORMAT_DATE_AND_TIME_IN_PAST("{0} at {1} ({2} ago)"),



  /**
   * Directory Name:  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_DIRECTORY_NAME("Directory Name:  {0}"),



  /**
   * DNS Name:  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_DNS("DNS Name:  {0}"),



  /**
   * EDI Party Name Count:  {0,number,0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_EDI_PARTY_NAME_COUNT("EDI Party Name Count:  {0,number,0}"),



  /**
   * IP Address:  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_IP("IP Address:  {0}"),



  /**
   * Other Name Count:  {0,number,0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_OTHER_NAME_COUNT("Other Name Count:  {0,number,0}"),



  /**
   * Registered ID:  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_REGISTERED_ID("Registered ID:  {0}"),



  /**
   * RFC 822 Name (Email Address):  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_RFC_822_NAME("RFC 822 Name (Email Address):  {0}"),



  /**
   * URI:  {0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_URI("URI:  {0}"),



  /**
   * X.400 Address Count:  {0,number,0}
   */
  INFO_MANAGE_CERTS_GENERAL_NAMES_LABEL_X400_ADDR_COUNT("X.400 Address Count:  {0,number,0}"),



  /**
   * Successfully created a new {0} key store.
   */
  INFO_MANAGE_CERTS_GEN_CERT_CERT_CREATED_KEYSTORE("Successfully created a new {0} key store."),



  /**
   * Do you really want to sign this request?
   */
  INFO_MANAGE_CERTS_GEN_CERT_PROMPT_SIGN("Do you really want to sign this request?"),



  /**
   * Read the following certificate signing request:
   */
  INFO_MANAGE_CERTS_GEN_CERT_SIGN_CONFIRM("Read the following certificate signing request:"),



  /**
   * Successfully wrote the certificate signing request to file ''{0}''.
   */
  INFO_MANAGE_CERTS_GEN_CERT_SUCCESSFULLY_GENERATED_CSR("Successfully wrote the certificate signing request to file ''{0}''."),



  /**
   * Successfully generated the key pair to use for the certificate signing request.
   */
  INFO_MANAGE_CERTS_GEN_CERT_SUCCESSFULLY_GENERATED_KEYPAIR("Successfully generated the key pair to use for the certificate signing request."),



  /**
   * Successfully generated the following self-signed certificate:
   */
  INFO_MANAGE_CERTS_GEN_CERT_SUCCESSFULLY_GENERATED_SELF_CERT("Successfully generated the following self-signed certificate:"),



  /**
   * Successfully wrote the signed certificate to file ''{0}''.
   */
  INFO_MANAGE_CERTS_GEN_CERT_SUCCESSFULLY_SIGNED_CERT("Successfully wrote the signed certificate to file ''{0}''."),



  /**
   * Please enter the current private key password for alias ''{0}'':
   */
  INFO_MANAGE_CERTS_GET_PK_PW_CURRENT_PROMPT("Please enter the current private key password for alias ''{0}'':"),



  /**
   * Please enter the password used to encrypt the private key for alias ''{0}'':
   */
  INFO_MANAGE_CERTS_GET_PK_PW_EXISTING_PROMPT("Please enter the password used to encrypt the private key for alias ''{0}'':"),



  /**
   * Please enter the new private key password:
   */
  INFO_MANAGE_CERTS_GET_PK_PW_NEW_PROMPT("Please enter the new private key password:"),



  /**
   * Please enter the password to use to protect the private key for alias ''{0}'':
   */
  INFO_MANAGE_CERTS_GET_PK_PW_NEW_PROMPT_1("Please enter the password to use to protect the private key for alias ''{0}'':"),



  /**
   * Confirm the new private key password:
   */
  INFO_MANAGE_CERTS_GET_PK_PW_NEW_PROMPT_2("Confirm the new private key password:"),



  /**
   * {0,number,0}-bit
   */
  INFO_MANAGE_CERTS_GET_PK_SUMMARY_RSA_MODULUS_SIZE("{0,number,0}-bit"),



  /**
   * The following certificate chain will be imported into the key store into alias ''{0}'', preserving the existing private key associated with that alias:
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_CONFIRM_IMPORT_CHAIN_EXISTING_KEY("The following certificate chain will be imported into the key store into alias ''{0}'', preserving the existing private key associated with that alias:"),



  /**
   * The following certificate chain will be imported into the key store, along with a private key, into alias ''{0}'':
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_CONFIRM_IMPORT_CHAIN_NEW_KEY("The following certificate chain will be imported into the key store, along with a private key, into alias ''{0}'':"),



  /**
   * The following certificate chain will be imported into the key store:
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_CONFIRM_IMPORT_CHAIN_NO_KEY("The following certificate chain will be imported into the key store:"),



  /**
   * Successfully created a new {0} key store.
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_CREATED_KEYSTORE("Successfully created a new {0} key store."),



  /**
   * Successfully imported the certificate chain.
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_IMPORTED_CHAIN_WITHOUT_PK("Successfully imported the certificate chain."),



  /**
   * Successfully imported the certificate chain and its associated private key.
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_IMPORTED_CHAIN_WITH_PK("Successfully imported the certificate chain and its associated private key."),



  /**
   * Alias:  {0}
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_LABEL_ALIAS("Alias:  {0}"),



  /**
   * Do you want to import this certificate chain into the key store?
   */
  INFO_MANAGE_CERTS_IMPORT_CERT_PROMPT_IMPORT_CHAIN("Do you want to import this certificate chain into the key store?"),



  /**
   * Please enter the current password needed to access key store ''{0}'':
   */
  INFO_MANAGE_CERTS_KEY_KS_PW_EXISTING_CURRENT_PROMPT("Please enter the current password needed to access key store ''{0}'':"),



  /**
   * Please enter the new password for the key store:
   */
  INFO_MANAGE_CERTS_KEY_KS_PW_EXISTING_NEW_PROMPT("Please enter the new password for the key store:"),



  /**
   * Please enter the password needed to access key store ''{0}'':
   */
  INFO_MANAGE_CERTS_KEY_KS_PW_EXISTING_PROMPT("Please enter the password needed to access key store ''{0}'':"),



  /**
   * Please enter the password to use to protect the contents of key store ''{0}'':
   */
  INFO_MANAGE_CERTS_KEY_KS_PW_NEW_PROMPT_1("Please enter the password to use to protect the contents of key store ''{0}'':"),



  /**
   * Confirm the key store password:
   */
  INFO_MANAGE_CERTS_KEY_KS_PW_NEW_PROMPT_2("Confirm the key store password:"),



  /**
   * Alias:  {0}
   */
  INFO_MANAGE_CERTS_LIST_CERTS_LABEL_ALIAS_WITHOUT_CHAIN("Alias:  {0}"),



  /**
   * Alias:  {0} (Certificate {1,number,0} of {2,number,0} in a chain)
   */
  INFO_MANAGE_CERTS_LIST_CERTS_LABEL_ALIAS_WITH_CHAIN("Alias:  {0} (Certificate {1,number,0} of {2,number,0} in a chain)"),



  /**
   * Private Key Available:  No
   */
  INFO_MANAGE_CERTS_LIST_CERTS_LABEL_HAS_PK_NO("Private Key Available:  No"),



  /**
   * Private Key Available:  Yes
   */
  INFO_MANAGE_CERTS_LIST_CERTS_LABEL_HAS_PK_YES("Private Key Available:  Yes"),



  /**
   * PEM-Encoded Certificate:
   */
  INFO_MANAGE_CERTS_LIST_CERTS_LABEL_PEM("PEM-Encoded Certificate:"),



  /**
   * No certificates or keys were found in the key store.  This may be because the key store is empty, or it may be that the key store requires a password in order to access its contents.  If you believe the key store may be non-empty, then try again with one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments supply a key store password.
   */
  INFO_MANAGE_CERTS_LIST_CERTS_NO_CERTS_OR_KEYS_WITHOUT_PW("No certificates or keys were found in the key store.  This may be because the key store is empty, or it may be that the key store requires a password in order to access its contents.  If you believe the key store may be non-empty, then try again with one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments supply a key store password."),



  /**
   * No certificates or keys were found in the key store.
   */
  INFO_MANAGE_CERTS_LIST_CERTS_NO_CERTS_OR_KEYS_WITH_PW("No certificates or keys were found in the key store."),



  /**
   * The certificate has a valid signature.
   */
  INFO_MANAGE_CERTS_LIST_CERTS_SIGNATURE_VALID("The certificate has a valid signature."),



  /**
   * '{'alias'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_ALIAS("'{'alias'}'"),



  /**
   * '{'bits'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_BITS("'{'bits'}'"),



  /**
   * '{'format'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_FORMAT("'{'format'}'"),



  /**
   * '{'host'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_HOST("'{'host'}'"),



  /**
   * '{'ipAddress'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_IP("'{'ipAddress'}'"),



  /**
   * '{'name'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_NAME("'{'name'}'"),



  /**
   * '{'oid'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_OID("'{'oid'}'"),



  /**
   * '{'password'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_PASSWORD("'{'password'}'"),



  /**
   * '{'port'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_PORT("'{'port'}'"),



  /**
   * '{'YYYYMMDDhhmmss'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_TIMESTAMP("'{'YYYYMMDDhhmmss'}'"),



  /**
   * '{'type'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_TYPE("'{'type'}'"),



  /**
   * '{'uri'}'
   */
  INFO_MANAGE_CERTS_PLACEHOLDER_URI("'{'uri'}'"),



  /**
   * Elliptic Curve Named Curve:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EC_CURVE("Elliptic Curve Named Curve:  {0}"),



  /**
   * Elliptic Curve Public Key Is Compressed:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EC_IS_COMPRESSED("Elliptic Curve Public Key Is Compressed:  {0}"),



  /**
   * Elliptic Curve X-Coordinate:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EC_X("Elliptic Curve X-Coordinate:  {0}"),



  /**
   * Elliptic Curve Y-Coordinate:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EC_Y("Elliptic Curve Y-Coordinate:  {0}"),



  /**
   * Elliptic Curve Y-Coordinate Is Even:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EC_Y_IS_EVEN("Elliptic Curve Y-Coordinate Is Even:  {0}"),



  /**
   * Encoded Public Key:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_ENCODED_PK("Encoded Public Key:"),



  /**
   * Certificate Extensions:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXTENSIONS("Certificate Extensions:"),



  /**
   * Authority Key Identifier Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_AUTH_KEY_ID_EXT("Authority Key Identifier Extension:"),



  /**
   * Key Identifier:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_AUTH_KEY_ID_ID("Key Identifier:"),



  /**
   * Authority Certificate Issuer:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_AUTH_KEY_ID_ISSUER("Authority Certificate Issuer:"),



  /**
   * Authority Certificate Serial Number:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_AUTH_KEY_ID_SERIAL("Authority Certificate Serial Number:  {0}"),



  /**
   * Basic Constraints Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_BASIC_CONST_EXT("Basic Constraints Extension:"),



  /**
   * Is CA:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_BASIC_CONST_IS_CA("Is CA:  {0}"),



  /**
   * Path Length Constraint:  {0,number,0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_BASIC_CONST_LENGTH("Path Length Constraint:  {0,number,0}"),



  /**
   * CRL Issuer:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_CRL_ISSUER("CRL Issuer:"),



  /**
   * CRL Distribution Points Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_EXT("CRL Distribution Points Extension:"),



  /**
   * Full Name:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_FULL_NAME("Full Name:"),



  /**
   * CRL Distribution Point:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_HEADER("CRL Distribution Point:"),



  /**
   * Potential Revocation Reasons:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_REASON("Potential Revocation Reasons:"),



  /**
   * Name Relative to CRL Issuer:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_CRL_DP_REL_NAME("Name Relative to CRL Issuer:  {0}"),



  /**
   * Extended Key Usage Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_EKU_EXT("Extended Key Usage Extension:"),



  /**
   * Key Purpose ID:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_EKU_ID("Key Purpose ID:  {0}"),



  /**
   * Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_GENERIC("Extension:"),



  /**
   * Issuer Alternative Name Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_IAN_EXT("Issuer Alternative Name Extension:"),



  /**
   * Is Critical:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_IS_CRITICAL("Is Critical:  {0}"),



  /**
   * CRL Sign
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_CRL_SIGN("CRL Sign"),



  /**
   * Data Encipherment
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_DE("Data Encipherment"),



  /**
   * Decipher Only
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_DO("Decipher Only"),



  /**
   * Digital Signature
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_DS("Digital Signature"),



  /**
   * Encipher Only
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_EO("Encipher Only"),



  /**
   * Key Usage Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_EXT("Key Usage Extension:"),



  /**
   * Key Agreement
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_KA("Key Agreement"),



  /**
   * Key Cert Sign
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_KCS("Key Cert Sign"),



  /**
   * Key Encipherment
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_KE("Key Encipherment"),



  /**
   * Non-Repudiation
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_NR("Non-Repudiation"),



  /**
   * Key Usages:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_KU_USAGES("Key Usages:"),



  /**
   * OID:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_OID("OID:  {0}"),



  /**
   * Subject Alternative Name Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_SAN_EXT("Subject Alternative Name Extension:"),



  /**
   * Subject Key Identifier Extension:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_SKI_EXT("Subject Key Identifier Extension:"),



  /**
   * Key Identifier:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_SKI_ID("Key Identifier:"),



  /**
   * Extension Value:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_EXT_VALUE("Extension Value:"),



  /**
   * {0} Fingerprint:  {1}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_FINGERPRINT("{0} Fingerprint:  {1}"),



  /**
   * Issuer DN:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_ISSUER_DN("Issuer DN:  {0}"),



  /**
   * Issuer Unique Identifier:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_ISSUER_UNIQUE_ID("Issuer Unique Identifier:"),



  /**
   * Public Key Algorithm:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_PK_ALG("Public Key Algorithm:  {0}"),



  /**
   * RSA Public Exponent:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_RSA_EXPONENT("RSA Public Exponent:  {0}"),



  /**
   * RSA Key Size:  {0,number,0} bits
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_RSA_KEY_SIZE("RSA Key Size:  {0,number,0} bits"),



  /**
   * {0,number,0}-bit RSA Modulus:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_RSA_MODULUS("{0,number,0}-bit RSA Modulus:"),



  /**
   * Serial Number:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_SERIAL_NUMBER("Serial Number:  {0}"),



  /**
   * Signature Algorithm:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_SIG_ALG("Signature Algorithm:  {0}"),



  /**
   * Signature Value:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_SIG_VALUE("Signature Value:"),



  /**
   * Subject DN:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_SUBJECT_DN("Subject DN:  {0}"),



  /**
   * Subject Unique Identifier:
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_SUBJECT_UNIQUE_ID("Subject Unique Identifier:"),



  /**
   * Validity End Time:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VALIDITY_END("Validity End Time:  {0}"),



  /**
   * Validity Start Time:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VALIDITY_START("Validity Start Time:  {0}"),



  /**
   * Validity State:  The certificate is expired.
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VALIDITY_STATE_EXPIRED("Validity State:  The certificate is expired."),



  /**
   * Validity State:  The certificate is not yet valid.
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VALIDITY_STATE_NOT_YET_VALID("Validity State:  The certificate is not yet valid."),



  /**
   * Validity State:  The certificate is currently within the validity window.
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VALIDITY_STATE_VALID("Validity State:  The certificate is currently within the validity window."),



  /**
   * X.509 Certificate Version:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CERT_LABEL_VERSION("X.509 Certificate Version:  {0}"),



  /**
   * PKCS #10 Certificate Signing Request Version:  {0}
   */
  INFO_MANAGE_CERTS_PRINT_CSR_LABEL_VERSION("PKCS #10 Certificate Signing Request Version:  {0}"),



  /**
   * Certificate {0,number,0} of {1,number,0} in the chain:
   */
  INFO_MANAGE_CERTS_RETRIEVE_CERT_DISPLAY_HEADER("Certificate {0,number,0} of {1,number,0} in the chain:"),



  /**
   * The current alias for the key store entry to rename.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_CURRENT_ALIAS_DESC("The current alias for the key store entry to rename.  This is required, and it may only be provided once."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to the key store file containing the alias to rename.  This is required, and the keystore file must exist.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_KS_DESC("The path to the key store file containing the alias to rename.  This is required, and the keystore file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when changing a certificate alias, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when changing a certificate alias, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * The new alias to assign to the target entry in the key store.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_NEW_ALIAS_DESC("The new alias to assign to the target entry in the key store.  This is required, and it may only be provided once."),



  /**
   * The password (also called a passphrase or PIN) used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided."),



  /**
   * The path to a file containing the password used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_PK_PW_FILE_DESC("The path to a file containing the password used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Interactively prompt for the key store password.  A key store password is required so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Interactively prompt for the private key password.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the private key password.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if the target alias includes a private key, and that private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided."),



  /**
   * Changes the alias of a certificate in a key store.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_DESC("Changes the alias of a certificate in a key store."),



  /**
   * Changes the alias of the existing ''server-cert'' certificate to be ''server-certificate''.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_ALIAS_EXAMPLE_1("Changes the alias of the existing ''server-cert'' certificate to be ''server-certificate''."),



  /**
   * The current password for the key store.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_CURRENT_PW_DESC("The current password for the key store.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the current password for the key store.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_CURRENT_PW_FILE_DESC("The path to a file containing the current password for the key store.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to the key store file for which to change the password.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_KS_DESC("The path to the key store file for which to change the password.  This is required, and the key store file must exist."),



  /**
   * The new password for the key store.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_NEW_PW_DESC("The new password for the key store.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the new password for the key store.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_NEW_PW_FILE_DESC("The path to a file containing the new password for the key store.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * Interactively prompt for the current key store password.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_PROMPT_FOR_CURRENT_PW_DESC("Interactively prompt for the current key store password.  The current password is required, so one of the --current-keystore-password, --current-keystore-password-file, or --prompt-for-current-keystore-password arguments must be provided."),



  /**
   * Interactively prompt for the new key store password.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_ARG_PROMPT_FOR_NEW_PW_DESC("Interactively prompt for the new key store password.  The new password is required, so one of the --new-keystore-password, --new-keystore-password-file, or --prompt-for-new-keystore-password arguments must be provided."),



  /**
   * Changes the password used to protect the contents of a key store.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_DESC("Changes the password used to protect the contents of a key store."),



  /**
   * Changes the password for the ''{0}'' key store from the current password contained in file ''{1}'' to the new password contained in file ''{2}''.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_KS_PW_EXAMPLE_1("Changes the password for the ''{0}'' key store from the current password contained in file ''{1}'' to the new password contained in file ''{2}''."),



  /**
   * The alias (also called a nickname) of the private key entry for which to change the password.  This is required.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_ALIAS_DESC("The alias (also called a nickname) of the private key entry for which to change the password.  This is required."),



  /**
   * The current password used to encrypt the private key.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_CURRENT_PW_DESC("The current password used to encrypt the private key.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required."),



  /**
   * The path to a file containing the current password used to encrypt the private key.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_CURRENT_PW_FILE_DESC("The path to a file containing the current password used to encrypt the private key.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to the key store file containing the private key entry for which to change the password.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_KS_DESC("The path to the key store file containing the private key entry for which to change the password.  This is required, and the key store file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when changing private key passwords, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when changing private key passwords, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * The new password to use to encrypt the private key.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_NEW_PW_DESC("The new password to use to encrypt the private key.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required."),



  /**
   * The path to a file containing the new password to use to encrypt the private key.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_NEW_PW_FILE_DESC("The path to a file containing the new password to use to encrypt the private key.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Interactively prompt for the current private key password.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_PROMPT_FOR_CURRENT_PW_DESC("Interactively prompt for the current private key password.  The current private key password is required, so one of the --current-private-key-password, --current-private-key-password-file, and --prompt-for-current-private-key-password arguments is required."),



  /**
   * Interactively prompt for the key store password.  A key store password is required so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Interactively prompt for the new private key password.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_ARG_PROMPT_FOR_NEW_PW_DESC("Interactively prompt for the new private key password.  The new private key password is required, so one of the --new-private-key-password, --new-private-key-password-file, and --prompt-for-new-private-key-password arguments is required."),



  /**
   * Changes the password used to protect a specified private key.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_DESC("Changes the password used to protect a specified private key."),



  /**
   * Changes the password for the ''server-cert'' private key entry in the ''{0}'' key store from the current password contained in file ''{1}'' to the new password contained in file ''{2}''.
   */
  INFO_MANAGE_CERTS_SC_CHANGE_PK_PW_EXAMPLE_1("Changes the password for the ''server-cert'' private key entry in the ''{0}'' key store from the current password contained in file ''{1}'' to the new password contained in file ''{2}''."),



  /**
   * The alias (also called a nickname) of the certificate to examine.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_ALIAS_DESC("The alias (also called a nickname) of the certificate to examine.  This is required, and it may only be provided once."),



  /**
   * The path to the key store file containing the certificate to check.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_KS_DESC("The path to the key store file containing the certificate to check.  This is required, and the key store file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when checking certificate usability, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when checking certificate usability, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * Interactively prompt for the key store password.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Examines a key store to determine how suitable a specified certificate is for use as a server certificate.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_DESC("Examines a key store to determine how suitable a specified certificate is for use as a server certificate."),



  /**
   * Check the ''server-cert'' certificate in the ''{0}'' key store to determine how suitable it is for use as a server certificate.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_EXAMPLE_1("Check the ''server-cert'' certificate in the ''{0}'' key store to determine how suitable it is for use as a server certificate."),



  /**
   * Do not fail the validation check merely because an issuer certificate contains a signature based on the SHA-1 digest algorithm.  The SHA-1 algorithm is considered weak, and some clients may reject a certificate chain that includes a certificate with a SHA-1-based signature, but because some commercial authorities still use SHA-1-based root certificates, this argument makes it possible to ignore this warning for issuer certificates.
   */
  INFO_MANAGE_CERTS_SC_CHECK_USABILITY_IGNORE_SHA1_WARNING_DESC("Do not fail the validation check merely because an issuer certificate contains a signature based on the SHA-1 digest algorithm.  The SHA-1 algorithm is considered weak, and some clients may reject a certificate chain that includes a certificate with a SHA-1-based signature, but because some commercial authorities still use SHA-1-based root certificates, this argument makes it possible to ignore this warning for issuer certificates."),



  /**
   * The alias (also called a nickname) of the certificate to delete.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_ALIAS_DESC("The alias (also called a nickname) of the certificate to delete.  This is required, and it may only be provided once."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to the key store file containing the certificate to remove.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_KS_DESC("The path to the key store file containing the certificate to remove.  This is required, and the key store file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when deleting certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when deleting certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * Delete the certificate without prompting the end user.  By default, the target certificate will be displayed and the user will be interactively prompted about whether to delete it.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_NO_PROMPT_DESC("Delete the certificate without prompting the end user.  By default, the target certificate will be displayed and the user will be interactively prompted about whether to delete it."),



  /**
   * Interactively prompt for the key store password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Removes a certificate from a key store.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_DESC("Removes a certificate from a key store."),



  /**
   * Remove the ''server-cert'' certificate from the ''{0}'' key store.
   */
  INFO_MANAGE_CERTS_SC_DELETE_CERT_EXAMPLE_1("Remove the ''server-cert'' certificate from the ''{0}'' key store."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to a file containing the certificates to be printed.  The certificates may be formatted in either the text-based PEM format or the binary DER format.  If the certificates are in PEM format, then each certificate must include the begin header and end footer, and blank lines and lines that start with the octothorpe character (#) will be ignored.  If the certificates are in DER format, then there must not be any delimiter between the certificates.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_ARG_FILE_DESC("The path to a file containing the certificates to be printed.  The certificates may be formatted in either the text-based PEM format or the binary DER format.  If the certificates are in PEM format, then each certificate must include the begin header and end footer, and blank lines and lines that start with the octothorpe character (#) will be ignored.  If the certificates are in DER format, then there must not be any delimiter between the certificates."),



  /**
   * Display verbose information about each of the certificates.  If this argument is not provided, then the listing will only include basic summary information for each certificate, including its subject and issuer DNs, validity start and end times, and fingerprints.  If this argument is provided, then additional information, including the X.509 certificate version, serial number, signature algorithm and value, public key algorithm and content, and extensions, will also be included.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_ARG_VERBOSE_DESC("Display verbose information about each of the certificates.  If this argument is not provided, then the listing will only include basic summary information for each certificate, including its subject and issuer DNs, validity start and end times, and fingerprints.  If this argument is provided, then additional information, including the X.509 certificate version, serial number, signature algorithm and value, public key algorithm and content, and extensions, will also be included."),



  /**
   * Displays information about all of the certificates contained in a file.  The certificates may be formatted in either the text-based PEM or the binary DER format, and if the file multiple certificates, then all certificates must use the same format.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_DESC("Displays information about all of the certificates contained in a file.  The certificates may be formatted in either the text-based PEM or the binary DER format, and if the file multiple certificates, then all certificates must use the same format."),



  /**
   * Display basic information about each of the certificates in file ''{0}''.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_EXAMPLE_1("Display basic information about each of the certificates in file ''{0}''."),



  /**
   * Display verbose information about each of the certificates in file ''{0}''.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CERT_EXAMPLE_2("Display verbose information about each of the certificates in file ''{0}''.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CSR_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to a file containing the certificate signing request (CSR) to be printed.  The CSR may be formatted in either the text-based PEM format or the binary DER format.  If the request is in PEM format, it must include the begin header and end footer, and blank lines and lines that start with the octothorpe character (#) will be ignored.  The file must contain only a single certificate signing request.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CSR_ARG_FILE_DESC("The path to a file containing the certificate signing request (CSR) to be printed.  The CSR may be formatted in either the text-based PEM format or the binary DER format.  If the request is in PEM format, it must include the begin header and end footer, and blank lines and lines that start with the octothorpe character (#) will be ignored.  The file must contain only a single certificate signing request."),



  /**
   * Display verbose information about the certificate signing request.  If this argument is not provided, then the listing will only include basic summary information for the request, including its subject DN, signature algorithm, and public key algorithm.  If this argument is provided, then additional information about the signature, public key, and extensions will be included.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CSR_ARG_VERBOSE_DESC("Display verbose information about the certificate signing request.  If this argument is not provided, then the listing will only include basic summary information for the request, including its subject DN, signature algorithm, and public key algorithm.  If this argument is provided, then additional information about the signature, public key, and extensions will be included."),



  /**
   * Displays information about a certificate signing request (CSR) contained in a file.  The CSR may be formatted in either the text-based PEM or the binary DER format.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CSR_DESC("Displays information about a certificate signing request (CSR) contained in a file.  The CSR may be formatted in either the text-based PEM or the binary DER format."),



  /**
   * Display information about the certificate signing request in file ''{0}'', as well as a command that can be used to accomplish a similar result using the Java keytool utility.
   */
  INFO_MANAGE_CERTS_SC_DISPLAY_CSR_EXAMPLE_1("Display information about the certificate signing request in file ''{0}'', as well as a command that can be used to accomplish a similar result using the Java keytool utility."),



  /**
   * Export certificates from the JVM-default trust store ({0}).
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERTS_ARG_JVM_DEFAULT_DESC("Export certificates from the JVM-default trust store ({0})."),



  /**
   * The alias (also called a nickname) of the certificate to export.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_ALIAS_DESC("The alias (also called a nickname) of the certificate to export.  This is required, and it may only be provided once."),



  /**
   * Indicates that the entire certificate chain (the target certificate and all of the certificates in its issuer chain) should be exported rather than just the specified target certificate.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_CHAIN_DESC("Indicates that the entire certificate chain (the target certificate and all of the certificates in its issuer chain) should be exported rather than just the specified target certificate."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to the output file to which the exported certificate should be written.  An output file is optional when using the PEM format, but required when using the DER format.  If no output file is provided, then the exported certificate will be written to standard output.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_FILE_DESC("The path to the output file to which the exported certificate should be written.  An output file is optional when using the PEM format, but required when using the DER format.  If no output file is provided, then the exported certificate will be written to standard output."),



  /**
   * The output format to use for the exported certificate.  The value may be either ''PEM'' (to export the certificate in the text-based PEM format), or ''DER'' (to export the certificate in the binary DER format).  If this is not provided, then the PEM output format will be used.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_FORMAT_DESC("The output format to use for the exported certificate.  The value may be either ''PEM'' (to export the certificate in the text-based PEM format), or ''DER'' (to export the certificate in the binary DER format).  If this is not provided, then the PEM output format will be used."),



  /**
   * The path to the key store file containing the certificates to export.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_KS_DESC("The path to the key store file containing the certificates to export.  This is required, and the key store file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when exporting certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when exporting certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * Interactively prompt for the key store password.  A key store password is optional for some key store types, but may be required for others.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is optional for some key store types, but may be required for others."),



  /**
   * Indicates that if multiple certificates are to be exported, then each certificate should be written to a different file rather than concatenating all of them into the same file.  This can only be used if both the --export-certificate-chain and --output-file arguments are also provided.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_ARG_SEPARATE_FILE_DESC("Indicates that if multiple certificates are to be exported, then each certificate should be written to a different file rather than concatenating all of them into the same file.  This can only be used if both the --export-certificate-chain and --output-file arguments are also provided."),



  /**
   * Exports a certificate or certificate chain from a key store.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_DESC("Exports a certificate or certificate chain from a key store."),



  /**
   * Export the ''server-cert'' certificate in PEM format to standard output.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_EXAMPLE_1("Export the ''server-cert'' certificate in PEM format to standard output."),



  /**
   * Export the ''server-cert'' certificate, and all of the certificates in its issuer chain, to the specified output file in the binary DER format.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_CERT_EXAMPLE_2("Export the ''server-cert'' certificate, and all of the certificates in its issuer chain, to the specified output file in the binary DER format.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility."),



  /**
   * The alias (also called a nickname) of the private key to export.  This is required, and it may only be provided once.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_ALIAS_DESC("The alias (also called a nickname) of the private key to export.  This is required, and it may only be provided once."),



  /**
   * The path to the output file to which the exported private key should be written.  An output file is optional when using the PEM format, but required when using the DER format.  If no output file is provided, then the exported private key will be written to standard output.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_FILE_DESC("The path to the output file to which the exported private key should be written.  An output file is optional when using the PEM format, but required when using the DER format.  If no output file is provided, then the exported private key will be written to standard output."),



  /**
   * The output format to use for the exported private key.  The value may be either ''PEM'' (to export the private key in the text-based PEM format), or ''DER'' (to export the key in the binary DER format).  If this is not provided, then the PEM output format will be used.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_FORMAT_DESC("The output format to use for the exported private key.  The value may be either ''PEM'' (to export the private key in the text-based PEM format), or ''DER'' (to export the key in the binary DER format).  If this is not provided, then the PEM output format will be used."),



  /**
   * The path to the key store file containing the private key to export.  This is required, and the key store file must exist.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_KS_DESC("The path to the key store file containing the private key to export.  This is required, and the key store file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when exporting private keys, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when exporting private keys, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * The password (also called a passphrase or PIN) used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided."),



  /**
   * The path to a file containing the password used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_PK_PW_FILE_DESC("The path to a file containing the password used to protect the private key.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Interactively prompt for the key store password.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required when exporting a private key, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Interactively prompt for the private key password.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the private key password.  In many cases, the private key password will be the same as the password used to protect the key store itself, and in such instances, the private key password can be omitted and the key store password will be used.  However, if a private key is protected with a different password than the key store itself, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments must be provided."),



  /**
   * Exports a private key from a key store.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_DESC("Exports a private key from a key store."),



  /**
   * Export the private key for the ''server-cert'' certificate to standard output in PEM format.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_EXAMPLE_1("Export the private key for the ''server-cert'' certificate to standard output in PEM format."),



  /**
   * Export the private key for the ''server-cert'' certificate to the specified output file in the binary DER format.
   */
  INFO_MANAGE_CERTS_SC_EXPORT_KEY_EXAMPLE_2("Export the private key for the ''server-cert'' certificate to the specified output file in the binary DER format."),



  /**
   * The alias (also called a nickname) to use for the newly-generated certificate.  If the --replace-existing-certificate argument is provided, then this must be the alias of the private key for which to replace the certificate chain with the self-signed certificate.  If the --replace-existing-certificate argument is not provided, then the alias must not already exist in the key store.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_ALIAS_DESC("The alias (also called a nickname) to use for the newly-generated certificate.  If the --replace-existing-certificate argument is provided, then this must be the alias of the private key for which to replace the certificate chain with the self-signed certificate.  If the --replace-existing-certificate argument is not provided, then the alias must not already exist in the key store."),



  /**
   * Indicates that the certificate should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_BC_IS_CA_DESC("Indicates that the certificate should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''."),



  /**
   * Indicates that the certificate should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that issuer certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_BC_PATH_LENGTH_DESC("Indicates that the certificate should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that issuer certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''."),



  /**
   * The number of days that the certificate should be considered valid.  If this argument is not provided, then a default value of 365 days will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_DAYS_VALID_DESC("The number of days that the certificate should be considered valid.  If this argument is not provided, then a default value of 365 days will be used."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * Indicates that the certificate should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_EKU_DESC("Indicates that the certificate should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values."),



  /**
   * Indicates that the certificate should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff).
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_EXT_DESC("Indicates that the certificate should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff)."),



  /**
   * This argument can only be used in conjunction with the --replace-existing-certificate argument, and it indicates that the new certificate should inherit all of the same extension values as the certificate being replaced (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded).  If the --replace-existing-certificate argument is provided without the --inherit-extensions argument, then the new certificate will only have the extensions that are explicitly specified using other arguments.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_INHERIT_EXT_DESC("This argument can only be used in conjunction with the --replace-existing-certificate argument, and it indicates that the new certificate should inherit all of the same extension values as the certificate being replaced (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded).  If the --replace-existing-certificate argument is provided without the --inherit-extensions argument, then the new certificate will only have the extensions that are explicitly specified using other arguments."),



  /**
   * The name of the key algorithm to use to generate the key pair.  If present, the value will typically be ''RSA'' or ''EC'' (for elliptic curve).  This argument must not be provided if the --replace-existing-certificate argument is used.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key algorithm of ''RSA'' will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KEY_ALGORITHM_DESC("The name of the key algorithm to use to generate the key pair.  If present, the value will typically be ''RSA'' or ''EC'' (for elliptic curve).  This argument must not be provided if the --replace-existing-certificate argument is used.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key algorithm of ''RSA'' will be used."),



  /**
   * The size of the key to generate, in bits.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key size of 2048 bits will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KEY_SIZE_BITS_DESC("The size of the key to generate, in bits.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key size of 2048 bits will be used."),



  /**
   * The path to the key store file in which the self-signed certificate will be created.  This is required, but if the file does not exist, then it will be created.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KS_DESC("The path to the key store file in which the self-signed certificate will be created.  This is required, but if the file does not exist, then it will be created."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KS_PW_FILE_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters."),



  /**
   * The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KS_TYPE_DESC("The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores."),



  /**
   * Indicates that the certificate should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_KU_DESC("Indicates that the certificate should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values."),



  /**
   * The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters."),



  /**
   * The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_PK_PW_FILE_DESC("The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.  The password must contain at least six characters."),



  /**
   * Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters."),



  /**
   * Indicates that the new self-signed certificate should replace the certificate chain associated with an existing private key that is identified by the --alias argument, reusing the existing key pair.  If this argument is not provided, then a new key pair will be generated.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_REPLACE_DESC("Indicates that the new self-signed certificate should replace the certificate chain associated with an existing private key that is identified by the --alias argument, reusing the existing key pair.  If this argument is not provided, then a new key pair will be generated."),



  /**
   * Indicates that the certificate should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SAN_DNS_DESC("Indicates that the certificate should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the certificate should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SAN_EMAIL_DESC("Indicates that the certificate should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the certificate should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SAN_IP_DESC("Indicates that the certificate should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses."),



  /**
   * Indicates that the certificate should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SAN_OID_DESC("Indicates that the certificate should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier."),



  /**
   * Indicates that the certificate should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SAN_URI_DESC("Indicates that the certificate should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987."),



  /**
   * The name of the algorithm to use to sign the certificate.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default signature algorithm of ''SHA256withRSA'' will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SIG_ALG_DESC("The name of the algorithm to use to sign the certificate.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default signature algorithm of ''SHA256withRSA'' will be used."),



  /**
   * The subject DN for the new certificate.  This must be provided unless then --replace-existing-certificate argument is given.  If the --replace-existing-certificate argument is provided, then the --subject-dn argument may be omitted if you want to reuse the same subject as the existing certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department or organizational unit name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_SUBJECT_DN_DESC("The subject DN for the new certificate.  This must be provided unless then --replace-existing-certificate argument is given.  If the --replace-existing-certificate argument is provided, then the --subject-dn argument may be omitted if you want to reuse the same subject as the existing certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department or organizational unit name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''."),



  /**
   * The time that the certificate''s validity window should start (that is, the ''notBefore'' value).  If this is not provided, then the current time will be used.  If a value is given, it should be in the form ''YYYYMMDDhhmmss'' (for example, ''{0}'').  Timestamp values are assumed to be in the local time zone.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_ARG_VALIDITY_START_TIME_DESC("The time that the certificate''s validity window should start (that is, the ''notBefore'' value).  If this is not provided, then the current time will be used.  If a value is given, it should be in the form ''YYYYMMDDhhmmss'' (for example, ''{0}'').  Timestamp values are assumed to be in the local time zone."),



  /**
   * Generates a self-signed certificate in a key store.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_DESC("Generates a self-signed certificate in a key store."),



  /**
   * Generates a self-signed certificate with an alias of ''server-cert'' and subject DN of ''CN=ldap.example.com,O=Example Corp,C=US''.  The certificate will use a 2048-bit RSA key, a signature algorithm of SHA256withRSA, and a validity of 365 days, starting immediately.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_EXAMPLE_1("Generates a self-signed certificate with an alias of ''server-cert'' and subject DN of ''CN=ldap.example.com,O=Example Corp,C=US''.  The certificate will use a 2048-bit RSA key, a signature algorithm of SHA256withRSA, and a validity of 365 days, starting immediately."),



  /**
   * Generates a self-signed certificate to replace the existing certificate with the ''server-cert'' alias.  The new certificate will include the same subject, key and signature algorithms, and set of extensions as the existing certificate, and it will have a validity of 365 days, starting immediately.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_EXAMPLE_2("Generates a self-signed certificate to replace the existing certificate with the ''server-cert'' alias.  The new certificate will include the same subject, key and signature algorithms, and set of extensions as the existing certificate, and it will have a validity of 365 days, starting immediately."),



  /**
   * Generates a self-signed server certificate with an alias of ''server-cert'', a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US'', a 4096-bit RSA key, a signature algorithm of SHA256withRSA, a subject alternate name extension with DNS names of ''ldap1.example.com'' and ''ldap2.example.com'' and IP addresses of 1.2.3.4 and 1.2.3.5, and an extended key usage extension with the server-auth and client-auth usages.  The certificate will have a validity of 3650 days, starting at midnight on January 1, 2017 in the local time zone.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_EXAMPLE_3("Generates a self-signed server certificate with an alias of ''server-cert'', a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US'', a 4096-bit RSA key, a signature algorithm of SHA256withRSA, a subject alternate name extension with DNS names of ''ldap1.example.com'' and ''ldap2.example.com'' and IP addresses of 1.2.3.4 and 1.2.3.5, and an extended key usage extension with the server-auth and client-auth usages.  The certificate will have a validity of 3650 days, starting at midnight on January 1, 2017 in the local time zone."),



  /**
   * Generates a self-signed certification authority certificate with an alias of ''ca-cert'', a subject DN of ''CN=Example Certification Authority,O=Example Corp,C=US'', a 256-bit elliptic curve key, a signature algorithm of SHA256withECDSA, a basic constraints extension that indicates the certificate is a certification authority, and a key usage extension with the key-cert-sign and crl-sign values.  The certificate will have a validity of 7300 days, starting at midnight on January 1, 2017 in the local time zone.
   */
  INFO_MANAGE_CERTS_SC_GEN_CERT_EXAMPLE_4("Generates a self-signed certification authority certificate with an alias of ''ca-cert'', a subject DN of ''CN=Example Certification Authority,O=Example Corp,C=US'', a 256-bit elliptic curve key, a signature algorithm of SHA256withECDSA, a basic constraints extension that indicates the certificate is a certification authority, and a key usage extension with the key-cert-sign and crl-sign values.  The certificate will have a validity of 7300 days, starting at midnight on January 1, 2017 in the local time zone."),



  /**
   * The alias (also called a nickname) of the private key to use to generate the certificate signing request.  If the --replace-existing-certificate argument is provided, then this must be the alias of an existing private key.  If the --replace-existing-certificate argument is not provided, then the alias must not already exist in the key store, and a corresponding key pair will be created in that alias.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_ALIAS_DESC("The alias (also called a nickname) of the private key to use to generate the certificate signing request.  If the --replace-existing-certificate argument is provided, then this must be the alias of an existing private key.  If the --replace-existing-certificate argument is not provided, then the alias must not already exist in the key store, and a corresponding key pair will be created in that alias."),



  /**
   * Indicates that the certificate signing request should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_BC_IS_CA_DESC("Indicates that the certificate signing request should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''."),



  /**
   * Indicates that the certificate signing request should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_BC_PATH_LENGTH_DESC("Indicates that the certificate signing request should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * Indicates that the certificate signing request should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_EKU_DESC("Indicates that the certificate signing request should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values."),



  /**
   * Indicates that the certificate signing request should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff).
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_EXT_DESC("Indicates that the certificate signing request should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff)."),



  /**
   * The output format to use for the generated certificate signing request.  The value may be either ''PEM'' (to export the request in the text-based PEM format), or ''DER'' (to export the request in the binary DER format).  If this is not provided, then the PEM output format will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_FORMAT_DESC("The output format to use for the generated certificate signing request.  The value may be either ''PEM'' (to export the request in the text-based PEM format), or ''DER'' (to export the request in the binary DER format).  If this is not provided, then the PEM output format will be used."),



  /**
   * This argument can only be used in conjunction with the --replace-existing-certificate argument, and it indicates that the requested certificate should inherit all of the same extension values as the certificate being replaced (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded).  If the --replace-existing-certificate argument is provided without the --inherit-extensions argument, then the new certificate will only have the extensions that are explicitly specified using other arguments.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_INHERIT_EXT_DESC("This argument can only be used in conjunction with the --replace-existing-certificate argument, and it indicates that the requested certificate should inherit all of the same extension values as the certificate being replaced (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded).  If the --replace-existing-certificate argument is provided without the --inherit-extensions argument, then the new certificate will only have the extensions that are explicitly specified using other arguments."),



  /**
   * The name of the key algorithm to use to generate the key pair.  If present, the value will typically be ''RSA'' or ''EC'' (for elliptic curve).  This argument must not be provided if the --replace-existing-certificate argument is used.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key algorithm of ''RSA'' will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KEY_ALGORITHM_DESC("The name of the key algorithm to use to generate the key pair.  If present, the value will typically be ''RSA'' or ''EC'' (for elliptic curve).  This argument must not be provided if the --replace-existing-certificate argument is used.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key algorithm of ''RSA'' will be used."),



  /**
   * The size of the key to generate, in bits.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key size of 2048 bits will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KEY_SIZE_BITS_DESC("The size of the key to generate, in bits.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default key size of 2048 bits will be used."),



  /**
   * The path to the key store file that contains the key (or in which the key will be generated) to use for the certificate signing request.  This is required, but if the file does not exist, then it will be created.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KS_DESC("The path to the key store file that contains the key (or in which the key will be generated) to use for the certificate signing request.  This is required, but if the file does not exist, then it will be created."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KS_PW_FILE_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters."),



  /**
   * The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KS_TYPE_DESC("The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores."),



  /**
   * Indicates that the certificate signing request should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_KU_DESC("Indicates that the certificate signing request should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values."),



  /**
   * The path to the output file to which the certificate signing request should be written.  If this is not provided, then the certificate signing request will be written to standard output.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_OUTPUT_FILE_DESC("The path to the output file to which the certificate signing request should be written.  If this is not provided, then the certificate signing request will be written to standard output."),



  /**
   * The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters."),



  /**
   * The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_PK_PW_FILE_DESC("The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.  The password must contain at least six characters."),



  /**
   * Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when generating certificate signing requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  The password must contain at least six characters."),



  /**
   * Indicates that the certificate signing request should use an existing key pair in the key store, identified by the specified alias.  If this argument is not provided, then a new key pair will be generated for the certificate signing request and stored in the key store.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_REPLACE_DESC("Indicates that the certificate signing request should use an existing key pair in the key store, identified by the specified alias.  If this argument is not provided, then a new key pair will be generated for the certificate signing request and stored in the key store."),



  /**
   * Indicates that the certificate signing request should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SAN_DNS_DESC("Indicates that the certificate signing request should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the certificate signing request should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SAN_EMAIL_DESC("Indicates that the certificate signing request should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the certificate signing request should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SAN_IP_DESC("Indicates that the certificate signing request should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses."),



  /**
   * Indicates that the certificate signing request should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SAN_OID_DESC("Indicates that the certificate signing request should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier."),



  /**
   * Indicates that the certificate signing request should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SAN_URI_DESC("Indicates that the certificate signing request should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987."),



  /**
   * The name of the algorithm to use to sign the certificate.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default signature algorithm of ''SHA256withRSA'' will be used.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SIG_ALG_DESC("The name of the algorithm to use to sign the certificate.  This argument must not be provided if the --replace-existing-certificate argument is used.  This argument must be provided if the --key-algorithm argument is used to specify an algorithm other than ''RSA''.  If neither this argument nor the --replace-existing-certificate argument is provided, then a default signature algorithm of ''SHA256withRSA'' will be used."),



  /**
   * The subject DN for the certificate signing request.  This must be provided unless then --replace-existing-certificate argument is given.  If the --replace-existing-certificate argument is provided, then the --subject-dn argument may be omitted if you want to reuse the same subject as the existing certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_ARG_SUBJECT_DN_DESC("The subject DN for the certificate signing request.  This must be provided unless then --replace-existing-certificate argument is given.  If the --replace-existing-certificate argument is provided, then the --subject-dn argument may be omitted if you want to reuse the same subject as the existing certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''."),



  /**
   * Generates a certificate signing request (CSR) for a private key in a key store, optionally generating the private key in the process.  The certificate signing request may be either written to standard output or to a specified output file.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_DESC("Generates a certificate signing request (CSR) for a private key in a key store, optionally generating the private key in the process.  The certificate signing request may be either written to standard output or to a specified output file."),



  /**
   * Generates a certificate signing request for a certificate with a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US''.  The request will generate a new key pair in the ''server-cert'' alias with a 2048-bit RSA key and a signature algorithm of SHA256withRSA.  The generated certificate signing request will be sent to standard output.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_EXAMPLE_1("Generates a certificate signing request for a certificate with a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US''.  The request will generate a new key pair in the ''server-cert'' alias with a 2048-bit RSA key and a signature algorithm of SHA256withRSA.  The generated certificate signing request will be sent to standard output."),



  /**
   * Generates a certificate signing request to replace the existing certificate with the ''server-cert'' alias.  The new certificate will include the same subject, key and signature algorithms, and set of extensions as the existing certificate, and the request will be written to the server-cert.csr output file.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_EXAMPLE_2("Generates a certificate signing request to replace the existing certificate with the ''server-cert'' alias.  The new certificate will include the same subject, key and signature algorithms, and set of extensions as the existing certificate, and the request will be written to the server-cert.csr output file."),



  /**
   * Generates a certificate signing request for a certificate with a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US'', a subject alternate name extension with DNS names of ''ldap1.example.com'' and ''ldap2.example.com'' and IP addresses of 1.2.3.4 and 1.2.3.5, and an extended key usage extension with the server-auth and client-auth usages.  The certificate will use a newly-generated key pair with a 256-bit elliptic curve key and a signature algorithm of SHA256withECDSA.  The request will be written to the server-cert.csr output file.
   */
  INFO_MANAGE_CERTS_SC_GEN_CSR_EXAMPLE_3("Generates a certificate signing request for a certificate with a subject DN of ''CN=ldap.example.com,O=Example Corp,C=US'', a subject alternate name extension with DNS names of ''ldap1.example.com'' and ''ldap2.example.com'' and IP addresses of 1.2.3.4 and 1.2.3.5, and an extended key usage extension with the server-auth and client-auth usages.  The certificate will use a newly-generated key pair with a 256-bit elliptic curve key and a signature algorithm of SHA256withECDSA.  The request will be written to the server-cert.csr output file."),



  /**
   * The alias (also called a nickname) to use for the imported certificate.  If multiple certificates are to be imported, then the behavior depends on whether a private key will also be imported.  When importing a certificate chain along with a private key, then the alias must not already exist in the key store, and the private key and the entire certificate chain will be stored under this alias.  When importing a certificate for which the corresponding private key already exists in the key store (for example, if you used the generate-certificate-request subcommand to create a certificate signing request and are now importing the signed certificate), then you should use the same alias that is used for the existing private key, and you should provide the complete certificate chain.  When importing a certificate for which the corresponding private key does not already exist in the key store and for which the private key is not being provided by the --private-key-file argument, then the alias must not already exist in the key store, and any provided issuer certificates (which you should provide if they do not already exist in the key store and are not in the JVM''s default set of trusted issuer certificates) will be imported with aliases that are generated from the provided alias.  If there is only one issuer certificate to be imported, then it will be stored with an alias that is the provided alias with ''-issuer'' appended onto it.  If there are multiple issuer certificates to be imported, then their aliases will be the provided alias with ''-issuer-#'' appended onto it, where ''#'' will be ''1'' for the first issuer certificate, ''2'' for the second, and so on.  This is a required argument.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_ALIAS_DESC("The alias (also called a nickname) to use for the imported certificate.  If multiple certificates are to be imported, then the behavior depends on whether a private key will also be imported.  When importing a certificate chain along with a private key, then the alias must not already exist in the key store, and the private key and the entire certificate chain will be stored under this alias.  When importing a certificate for which the corresponding private key already exists in the key store (for example, if you used the generate-certificate-request subcommand to create a certificate signing request and are now importing the signed certificate), then you should use the same alias that is used for the existing private key, and you should provide the complete certificate chain.  When importing a certificate for which the corresponding private key does not already exist in the key store and for which the private key is not being provided by the --private-key-file argument, then the alias must not already exist in the key store, and any provided issuer certificates (which you should provide if they do not already exist in the key store and are not in the JVM''s default set of trusted issuer certificates) will be imported with aliases that are generated from the provided alias.  If there is only one issuer certificate to be imported, then it will be stored with an alias that is the provided alias with ''-issuer'' appended onto it.  If there are multiple issuer certificates to be imported, then their aliases will be the provided alias with ''-issuer-#'' appended onto it, where ''#'' will be ''1'' for the first issuer certificate, ''2'' for the second, and so on.  This is a required argument."),



  /**
   * The path to a file containing a certificate or certificate chain to import.  The certificates in the file may be stored either in the text-based PEM or the binary DER format, but if the file contains multiple certificates, then they must all be in the same format.  You may also provide this argument multiple times to specify multiple files containing certificates to import.  However, if multiple certificates are provided, then they must all form a certificate chain in which each subsequent certificate is the issuer certificate for the previous certificate.  When importing a non-self-signed certificate, you should ensure that its issuer certificate is also being imported, is already in the key store under a different alias, or is in the JVM''s default set of trusted certificates.  At least one certificate file is required.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_CERT_FILE_DESC("The path to a file containing a certificate or certificate chain to import.  The certificates in the file may be stored either in the text-based PEM or the binary DER format, but if the file contains multiple certificates, then they must all be in the same format.  You may also provide this argument multiple times to specify multiple files containing certificates to import.  However, if multiple certificates are provided, then they must all form a certificate chain in which each subsequent certificate is the issuer certificate for the previous certificate.  When importing a non-self-signed certificate, you should ensure that its issuer certificate is also being imported, is already in the key store under a different alias, or is in the JVM''s default set of trusted certificates.  At least one certificate file is required."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * The path to a file containing the private key for the end certificate in the chain to be imported.  It may be stored in either the text-based PEM or the binary DER format.  This is an optional argument, and at most one private key file may be specified, and that file may contain only a single private key.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_KEY_FILE_DESC("The path to a file containing the private key for the end certificate in the chain to be imported.  It may be stored in either the text-based PEM or the binary DER format.  This is an optional argument, and at most one private key file may be specified, and that file may contain only a single private key."),



  /**
   * The path to the key store file into which the certificates and key should be imported.  This is required, but if the file does not exist, then it will be created.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_KS_DESC("The path to the key store file into which the certificates and key should be imported.  This is required, but if the file does not exist, then it will be created."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_KS_PW_FILE_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters."),



  /**
   * The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_KS_TYPE_DESC("The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores."),



  /**
   * Import the certificates without prompting the end user.  By default, the certificates will be displayed and the user will be interactively prompted about whether to import them.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_NO_PROMPT_DESC("Import the certificates without prompting the end user.  By default, the certificates will be displayed and the user will be interactively prompted about whether to import them."),



  /**
   * The password (also called a passphrase or PIN) to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided."),



  /**
   * The path to a file containing the password to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_PK_PW_FILE_DESC("The path to a file containing the password to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * Interactively prompt for the password to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the password to use to protect the private key.  This is only needed when importing a private key along with a certificate chain, or when importing a certificate chain into an alias with an existing private key.  In many cases, the private key password will be the same as the key store password and in such instances, the private key password can be omitted and the key store password will be used as the private key password.  However, if you are importing a private key and wish to protect it with a password that does not match the key store password, or if you are importing a new certificate chain for an existing private key that uses a password that does not match the key store password, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be provided."),



  /**
   * Imports a certificate or certificate chain, and optionally a private key, into a key store.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_DESC("Imports a certificate or certificate chain, and optionally a private key, into a key store."),



  /**
   * Import the certificates in the ''{0}'' file into the specified key store using an alias of ''server-cert''.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_EXAMPLE_1("Import the certificates in the ''{0}'' file into the specified key store using an alias of ''server-cert''."),



  /**
   * Import a certificate chain, including a private key, from the set of provided files into the specified key store using an alias of ''server-cert''.
   */
  INFO_MANAGE_CERTS_SC_IMPORT_CERT_EXAMPLE_2("Import a certificate chain, including a private key, from the set of provided files into the specified key store using an alias of ''server-cert''."),



  /**
   * The alias (also called a nickname) of a certificate to include in the output.  This argument may be provided multiple times to identify multiple certificates to include.  If this argument is provided, then only the listed certificates will be displayed.  If this argument is omitted, then all certificates will be listed.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_ALIAS_DESC("The alias (also called a nickname) of a certificate to include in the output.  This argument may be provided multiple times to identify multiple certificates to include.  If this argument is provided, then only the listed certificates will be displayed.  If this argument is omitted, then all certificates will be listed."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * Include a PEM-encoded representation of each certificate in the output.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_DISPLAY_PEM_DESC("Include a PEM-encoded representation of each certificate in the output."),



  /**
   * List certificates from the JVM-default trust store ({0}).
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_JVM_DEFAULT_DESC("List certificates from the JVM-default trust store ({0})."),



  /**
   * The path to the key store file containing the certificates to list.  Either this argument or the --useJVMDefaultTrustStore argument must be provided, and if this argument is given, then the specified file must exist.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_KS_DESC("The path to the key store file containing the certificates to list.  Either this argument or the --useJVMDefaultTrustStore argument must be provided, and if this argument is given, then the specified file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others."),



  /**
   * The path to a file containing the password needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_KS_PW_FILE_DESC("The path to a file containing the password needed to access the contents of the key store.  A key store password is optional for some key store types, but may be required for others.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when listing certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when listing certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * Interactively prompt for the key store password.  A key store password is optional for some key store types, but may be required for others.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is optional for some key store types, but may be required for others."),



  /**
   * Display verbose information about each of the certificates.  If this argument is not provided, then the listing will only include basic summary information for each certificate, including its subject and issuer DNs, validity start and end times, and fingerprints.  If this argument is provided, then additional information, including the X.509 certificate version, serial number, signature algorithm and value, public key algorithm and content, and extensions, will also be included.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_ARG_VERBOSE_DESC("Display verbose information about each of the certificates.  If this argument is not provided, then the listing will only include basic summary information for each certificate, including its subject and issuer DNs, validity start and end times, and fingerprints.  If this argument is provided, then additional information, including the X.509 certificate version, serial number, signature algorithm and value, public key algorithm and content, and extensions, will also be included."),



  /**
   * Displays a list of some or all of the certificates in a key store.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_DESC("Displays a list of some or all of the certificates in a key store."),



  /**
   * Display basic information about all of the certificates in the ''{0}'' key store file.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_EXAMPLE_1("Display basic information about all of the certificates in the ''{0}'' key store file."),



  /**
   * Display verbose information about the ''server-cert'' certificate in the ''{0}'' key store file, whose contents are protected by a password contained in the ''{1}'' file.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility, along with a PEM-encoded representation of the certificate.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_EXAMPLE_2("Display verbose information about the ''server-cert'' certificate in the ''{0}'' key store file, whose contents are protected by a password contained in the ''{1}'' file.  It will also display a command that can be used to accomplish a similar result using the Java keytool utility, along with a PEM-encoded representation of the certificate."),



  /**
   * Display basic information about all of the certificates in the JVM''s default trust store file.
   */
  INFO_MANAGE_CERTS_SC_LIST_CERTS_EXAMPLE_3("Display basic information about all of the certificates in the JVM''s default trust store file."),



  /**
   * Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_ENABLE_SSL_DEBUGGING_DESC("Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''."),



  /**
   * The path to the output file to which the retrieved certificates should be written.  If this argument is provided, then a PEM or DER representation of all certificates in the chain will be written to the specified file.  If this argument is not provided, then information about the certificate will only be written to standard output.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_FILE_DESC("The path to the output file to which the retrieved certificates should be written.  If this argument is provided, then a PEM or DER representation of all certificates in the chain will be written to the specified file.  If this argument is not provided, then information about the certificate will only be written to standard output."),



  /**
   * The format in which the certificates should be written to the specified output file.  The value may be either ''PEM'' (to use the text-based PEM format), or ''DER'' (to use the binary DER format).  This argument may only be provided if the --output-file argument is also given.  If this is not provided, then the PEM output format will be used.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_FORMAT_DESC("The format in which the certificates should be written to the specified output file.  The value may be either ''PEM'' (to use the text-based PEM format), or ''DER'' (to use the binary DER format).  This argument may only be provided if the --output-file argument is also given.  If this is not provided, then the PEM output format will be used."),



  /**
   * The hostname or IP address of the server to which the connection should be established.  This must be provided.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_HOSTNAME_DESC("The hostname or IP address of the server to which the connection should be established.  This must be provided."),



  /**
   * Only provide information about the peer certificate.  If this argument is not provided, then the tool will provide information about all certificates in the presented chain.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_ONLY_PEER_DESC("Only provide information about the peer certificate.  If this argument is not provided, then the tool will provide information about all certificates in the presented chain."),



  /**
   * The TCP port number of the server to which the connection should be established.  Unless the --use-ldap-start-tls argument is provided, the port number must be one on which the server expects to accept TLS-based connections.  If the --use-ldap-start-tls argument is provided, then the specified port must be one on which an LDAP server is listening for non-secure connections but on which clients may use the StartTLS extended operation to transition to using secure communication.  Standard secure port numbers include 636 for LDAPS and 443 for HTTPS, and the standard non-secure port for LDAP is 389.  This must be provided.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_PORT_DESC("The TCP port number of the server to which the connection should be established.  Unless the --use-ldap-start-tls argument is provided, the port number must be one on which the server expects to accept TLS-based connections.  If the --use-ldap-start-tls argument is provided, then the specified port must be one on which an LDAP server is listening for non-secure connections but on which clients may use the StartTLS extended operation to transition to using secure communication.  Standard secure port numbers include 636 for LDAPS and 443 for HTTPS, and the standard non-secure port for LDAP is 389.  This must be provided."),



  /**
   * Indicates that the tool should initially establish a non-secure connection to an LDAP server, and then use the StartTLS extended operation to transition to using secure communication.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_USE_START_TLS_DESC("Indicates that the tool should initially establish a non-secure connection to an LDAP server, and then use the StartTLS extended operation to transition to using secure communication."),



  /**
   * Display verbose information about the certificates that are retrieved.  This will only affect what is written to standard output and will not alter what may be written to the output file (if one was requested).
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_ARG_VERBOSE_DESC("Display verbose information about the certificates that are retrieved.  This will only affect what is written to standard output and will not alter what may be written to the output file (if one was requested)."),



  /**
   * Initiates a secure connection to a server to get that server''s certificate chain, and then displays that certificate and optionally writes it to a file.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_DESC("Initiates a secure connection to a server to get that server''s certificate chain, and then displays that certificate and optionally writes it to a file."),



  /**
   * Establish a secure connection to ds.example.com on port 636 and display basic information about the server''s certificate chain.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_EXAMPLE_1("Establish a secure connection to ds.example.com on port 636 and display basic information about the server''s certificate chain."),



  /**
   * Establish an initially insecure connection to ds.example.com on port 389 and then invoke an LDAP StartTLS extended operation to establish a secure communication channel.  Display verbose information about the peer certificate (ignoring any issuer certificates), and write a PEM representation of that certificate to the ''ds-cert.pem'' file.
   */
  INFO_MANAGE_CERTS_SC_RETRIEVE_CERT_EXAMPLE_2("Establish an initially insecure connection to ds.example.com on port 389 and then invoke an LDAP StartTLS extended operation to establish a secure communication channel.  Display verbose information about the peer certificate (ignoring any issuer certificates), and write a PEM representation of that certificate to the ''ds-cert.pem'' file."),



  /**
   * The alias (also called a nickname) of the certificate to use to sign the request.  This alias must exist in the key store, and it must reference a private key with a certificate chain.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_ALIAS_DESC("The alias (also called a nickname) of the certificate to use to sign the request.  This alias must exist in the key store, and it must reference a private key with a certificate chain."),



  /**
   * Indicates that the signed certificate should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_BC_IS_CA_DESC("Indicates that the signed certificate should include a basic constraints extension that indicates whether the certificate should be considered a certification authority.  If present, the value must be either ''true'' or ''false''."),



  /**
   * Indicates that the signed certificate should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that issuer certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_BC_PATH_LENGTH_DESC("Indicates that the signed certificate should include a basic constraints extension that specifies that there must not be more than the specified number of intermediate certificates between that issuer certificate and the subject certificate in a certificate chain.  This argument can only be provided in conjunction with a --basic-constraints-is-ca value of ''true''."),



  /**
   * The number of days that the signed certificate should be considered valid.  If this argument is not provided, then a default value of 365 days will be used.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_DAYS_VALID_DESC("The number of days that the signed certificate should be considered valid.  If this argument is not provided, then a default value of 365 days will be used."),



  /**
   * Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_DISPLAY_COMMAND_DESC("Display a command that can be invoked to achieve a similar result with the Java keytool utility.  Note that this may just be an approximation, since the manage-certificates and keytool utilities do not provide exactly the same sets of functionality."),



  /**
   * Indicates that the signed certificate should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_EKU_DESC("Indicates that the signed certificate should include an extended key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''server-auth'', ''client-auth'', ''code-signing'', ''email-protection'', ''time-stamping'', and ''ocsp-signing'', or the string representation of any valid object identifier.  This argument can be provided multiple times to specify multiple extended key usage values."),



  /**
   * Indicates that the signed certificate should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff).
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_EXT_DESC("Indicates that the signed certificate should include an extension with the specified content.  The value must be in the form oid:criticality:value, where oid is the OID that identifies the type of extension, criticality is a value of either ''true'' or ''false'', and value is the hexadecimal representation of the extension value (for example, --ext 2.5.29.19:true:30030101ff)."),



  /**
   * The output format to use for the signed certificate.  The value may be either ''PEM'' (to write the certificate in the text-based PEM format), or ''DER'' (to write the certificate in the binary DER format).  If this is not provided, then the PEM output format will be used.  If an output format of ''DER'' is specified, then the --certificate-output-file argument must also be provided.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_FORMAT_DESC("The output format to use for the signed certificate.  The value may be either ''PEM'' (to write the certificate in the text-based PEM format), or ''DER'' (to write the certificate in the binary DER format).  If this is not provided, then the PEM output format will be used.  If an output format of ''DER'' is specified, then the --certificate-output-file argument must also be provided."),



  /**
   * Indicates that the signed certificate should include an issuer alternative name extension with the specified DNS hostname.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_IAN_DNS_DESC("Indicates that the signed certificate should include an issuer alternative name extension with the specified DNS hostname.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the signed certificate should include an issuer alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_IAN_EMAIL_DESC("Indicates that the signed certificate should include an issuer alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the signed certificate should include an issuer alternative name extension with the specified IP address.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_IAN_IP_DESC("Indicates that the signed certificate should include an issuer alternative name extension with the specified IP address.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses."),



  /**
   * Indicates that the signed certificate should include an issuer alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_IAN_OID_DESC("Indicates that the signed certificate should include an issuer alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier."),



  /**
   * Indicates that the signed certificate should include an issuer alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_IAN_URI_DESC("Indicates that the signed certificate should include an issuer alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987."),



  /**
   * Indicates that the signed certificate should include all of the extensions requested in the certificate signing request (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded), and the requested extensions will be included in addition to any other extensions requested via command-line arguments.  If this is not provided, then only the extensions requested via command-line arguments will be included in the signed certificate.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_INCLUDE_EXT_DESC("Indicates that the signed certificate should include all of the extensions requested in the certificate signing request (although extensions known to apply to the certificate''s issuer, like authority key identifier and issuer alternative name, may be excluded), and the requested extensions will be included in addition to any other extensions requested via command-line arguments.  If this is not provided, then only the extensions requested via command-line arguments will be included in the signed certificate."),



  /**
   * The path to the input file containing the certificate signing request to process.  This must be provided, and the specified file must exist.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_INPUT_FILE_DESC("The path to the input file containing the certificate signing request to process.  This must be provided, and the specified file must exist."),



  /**
   * The path to the key store file that contains the certificate that will be used to sign the requested certificate.  This must be provided, and the specified file must exist.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_KS_DESC("The path to the key store file that contains the certificate that will be used to sign the requested certificate.  This must be provided, and the specified file must exist."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store containing the signing certificate.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store containing the signing certificate.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store containing the signing certificate.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_KS_PW_FILE_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store containing the signing certificate.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password."),



  /**
   * The key store type for the key store.  This is usually not necessary when signing certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_KS_TYPE_DESC("The key store type for the key store.  This is usually not necessary when signing certificates, but it may be required in some cases (for example, when listing the certificates in a BCFKS key store when not operating in FIPS mode).  The value must be one of ''JKS'' (for the Java Key Store format), ''PKCS12'' (for the standard PKCS #12 format), or ''BCFKS'' (for the Bouncy Castle FIPS 140-2-compliant key store type).  If this is not provided, then an attempt will be made to automatically infer the correct key store type."),



  /**
   * Indicates that the signed certificate should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_KU_DESC("Indicates that the signed certificate should include a key usage extension that indicates that the certificate can be used for a specified purpose.  Allowed values for this argument are ''digital-signature'', ''non-repudiation'', ''key-encipherment'', ''data-encipherment'', ''key-agreement'', ''key-cert-sign'', ''crl-sign'', ''encipher-only'', and ''decipher-only''.  This argument can be provided multiple times to specify multiple key usage values."),



  /**
   * Sign the request without prompting the end user.  By default, the certificate signing request will be displayed and the user will be interactively prompted about whether to sign it.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_NO_PROMPT_DESC("Sign the request without prompting the end user.  By default, the certificate signing request will be displayed and the user will be interactively prompted about whether to sign it."),



  /**
   * The path to the output file to which the signed certificate should be written.  If this is not provided, then the certificate will be written to standard output.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_OUTPUT_FILE_DESC("The path to the output file to which the signed certificate should be written.  If this is not provided, then the certificate will be written to standard output."),



  /**
   * The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_PK_PW_DESC("The password (also called a passphrase or PIN) to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password."),



  /**
   * The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_PK_PW_FILE_DESC("The path to a file containing the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.  If a private key password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text private key password."),



  /**
   * Interactively prompt for the key store password.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  A key store password is required when signing certificate requests, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided."),



  /**
   * Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_PROMPT_FOR_PK_PW_DESC("Interactively prompt for the password to use to protect the private key.  Although in many cases, private keys will be protected with the same password as the key store itself, it is possible to use a different password for the private key.  If an alternate private key password is needed, then one of the --private-key-password, --private-key-password-file, or --prompt-for-private-key-password arguments should be used to provide that private key password.  If none of these arguments is given, then the key store password will be used as the private key password."),



  /**
   * Indicates that the signed certificate should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SAN_DNS_DESC("Indicates that the signed certificate should include a subject alternative name extension with the specified DNS hostname.  This can be used to help clients trust a server certificate if they connect to the server using a different hostname than is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple alternate hostnames, and hostnames can have an asterisk as their leftmost component (for example, ''*.example.com'' or ''*.east.example.com'') to match any value in that component.  Each value must contain only ASCII characters, so internationalized domain names must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the signed certificate should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SAN_EMAIL_DESC("Indicates that the signed certificate should include a subject alternative name extension with the specified email address (technically, RFC 822 name) value.  This can be provided multiple times to specify multiple email addresses.  Each value must contain only ASCII characters, so internationalized email addresses must use the ASCII-Compatible Encoding (ACE) described in RFC 5890."),



  /**
   * Indicates that the signed certificate should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SAN_IP_DESC("Indicates that the signed certificate should include a subject alternative name extension with the specified IP address.  This can be used to help clients trust a server certificate if they connect to the server using an IP address rather than the hostname that is included in the CN attribute of the certificate subject.  This can be provided multiple times to specify multiple IP addresses, and each value must be a valid IPv4 or IPv6 address.  There is no support for wildcards, CIDR, other mechanisms for specifying a range of addresses."),



  /**
   * Indicates that the signed certificate should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SAN_OID_DESC("Indicates that the signed certificate should include a subject alternative name extension with the specified OID as a resource identifier.  This can be provided multiple times to specify multiple OIDs, and each value must be the string representation of a valid object identifier."),



  /**
   * Indicates that the signed certificate should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SAN_URI_DESC("Indicates that the signed certificate should include a subject alternative name extension with the specified URI value.  This can be provided multiple times to specify multiple URIs.  Each value must contain only ASCII characters, so internationalized resource identifiers must be mapped to URIs as described in RFC 3987."),



  /**
   * The name of the algorithm to use to sign the certificate.  If this is not provided, then the signature algorithm from the certificate signing request will be used.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SIG_ALG_DESC("The name of the algorithm to use to sign the certificate.  If this is not provided, then the signature algorithm from the certificate signing request will be used."),



  /**
   * The subject DN for the signed certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department or organizational unit name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''.  This argument is optional, and if it is not provided, then the subject DN from the certificate signing request will be used.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_SUBJECT_DN_DESC("The subject DN for the signed certificate.  A subject DN typically includes at least a ''CN'' attribute (which in a server certificate should be the hostname that clients are expected to use when connecting to the server, and in other certificates indicates the purpose of that certificate), and may also include additional attributes like ''OU'' (the associated department or organizational unit name), ''O'' (the company or organization name), ''L'' (the city or locality name), ''ST'' (the full name -- NOT the two-letter abbreviation -- of the state or province), ''C'' (the two-letter country code -- NOT the full country name).  For example:  ''CN=ldap.example.com,OU=Directory Services,O=Example Corporation,L=Austin,ST=Texas,C=US''.  This argument is optional, and if it is not provided, then the subject DN from the certificate signing request will be used."),



  /**
   * The time that the signed certificate''s validity window should start (that is, the ''notBefore'' value).  If this is not provided, then the current time will be used.  If a value is given, it should be in the form ''YYYYMMDDhhmmss'' (for example, ''{0}'').  Timestamp values are assumed to be in the local time zone.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_ARG_VALIDITY_START_TIME_DESC("The time that the signed certificate''s validity window should start (that is, the ''notBefore'' value).  If this is not provided, then the current time will be used.  If a value is given, it should be in the form ''YYYYMMDDhhmmss'' (for example, ''{0}'').  Timestamp values are assumed to be in the local time zone."),



  /**
   * Signs a certificate signing request (CSR) provided in a specified input file using a certificate contained in a specified key store.  The signed certificate may be written to either standard output or to a specified file.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_DESC("Signs a certificate signing request (CSR) provided in a specified input file using a certificate contained in a specified key store.  The signed certificate may be written to either standard output or to a specified file."),



  /**
   * Signs the certificate signing request contained in file ''server-cert.csr'' using the ''ca-cert'' certificate contained in the ''{0}'' key store.  The subject DN, signature algorithm, and extensions from the provided certificate signing request will be used to generate the corresponding values in the signed certificate, and the certificate will be valid for 365 days, starting immediately.  The signed certificate will be written to standard output in PEM format.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_EXAMPLE_1("Signs the certificate signing request contained in file ''server-cert.csr'' using the ''ca-cert'' certificate contained in the ''{0}'' key store.  The subject DN, signature algorithm, and extensions from the provided certificate signing request will be used to generate the corresponding values in the signed certificate, and the certificate will be valid for 365 days, starting immediately.  The signed certificate will be written to standard output in PEM format."),



  /**
   * Signs the certificate signing request contained in file ''server-cert.csr'' using the ''ca-cert'' certificate contained in the ''{0}'' key store.  The subject DN, signature algorithm, and extensions from the provided certificate signing request will be used to generate the corresponding values in the signed certificate, and the certificate will also include an issuer alternative name extension with an email address of ''ca@example.com''.  The signed certificate will be valid for 730 days starting at midnight on January 1, 2017 in the local timezone.  The signed certificate will be written to the file ''server-cert.der'' file in the binary DER format.
   */
  INFO_MANAGE_CERTS_SC_SIGN_CSR_EXAMPLE_2("Signs the certificate signing request contained in file ''server-cert.csr'' using the ''ca-cert'' certificate contained in the ''{0}'' key store.  The subject DN, signature algorithm, and extensions from the provided certificate signing request will be used to generate the corresponding values in the signed certificate, and the certificate will also include an issuer alternative name extension with an email address of ''ca@example.com''.  The signed certificate will be valid for 730 days starting at midnight on January 1, 2017 in the local timezone.  The signed certificate will be written to the file ''server-cert.der'' file in the binary DER format."),



  /**
   * The alias (also called a nickname) to use for the first certificate to add to the key store.  This alias must not already be in use in the key store.  If multiple certificates are to be imported, then the first certificate imported will use this alias, and subsequent certificates will have either ''-issuer'' (if there is only one issuer certificate) or ''-issuer-#'' (if there are multiple issuers, where # will be replaced with an incrementing number for each subsequent issuer).  If this is omitted, then a default alias will be constructed from the hostname and port number.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_ALIAS_DESC("The alias (also called a nickname) to use for the first certificate to add to the key store.  This alias must not already be in use in the key store.  If multiple certificates are to be imported, then the first certificate imported will use this alias, and subsequent certificates will have either ''-issuer'' (if there is only one issuer certificate) or ''-issuer-#'' (if there are multiple issuers, where # will be replaced with an incrementing number for each subsequent issuer).  If this is omitted, then a default alias will be constructed from the hostname and port number."),



  /**
   * Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_ENABLE_SSL_DEBUGGING_DESC("Enable Java''s low-level support for debugging SSL/TLS communication.  This is equivalent to setting the ''javax.net.debug'' property to ''all''."),



  /**
   * The hostname or IP address of the server to which the connection should be established.  This must be provided.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_HOSTNAME_DESC("The hostname or IP address of the server to which the connection should be established.  This must be provided."),



  /**
   * Indicates that the tool should only update the key store to include the issuer certificates for the target server, but omit the server certificate at the head of the chain.  This may be useful in environments in which all servers are signed by a common issuer and it is sufficient to trust just the issuer certificates.  This argument will not have any effect for self-signed certificates in which a certificate is its own issuer.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_ISSUERS_ONLY_DESC("Indicates that the tool should only update the key store to include the issuer certificates for the target server, but omit the server certificate at the head of the chain.  This may be useful in environments in which all servers are signed by a common issuer and it is sufficient to trust just the issuer certificates.  This argument will not have any effect for self-signed certificates in which a certificate is its own issuer."),



  /**
   * The path to the key store file to which the certificates should be added.  This is required, but if the file does not exist, then it will be created.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_KS_DESC("The path to the key store file to which the certificates should be added.  This is required, but if the file does not exist, then it will be created."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_KS_PW_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_KS_PW_FILE_DESC("The password (also called a passphrase or PIN) needed to access the contents of the key store.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  If a key store password file is supplied, then the file must exist, must contain only one line, and that line must consist only of the clear-text key store password.  The password must contain at least six characters."),



  /**
   * The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_KS_TYPE_DESC("The key store type for the key store to create.  This argument should only be provided when creating a new key store, and it will be ignored if the key store already exists.  The value must be either ''JKS'' for the non-standard Java Key Store format, or ''PKCS12'' for the standard PKCS #12 format.  If this is not provided, then a default key store type of ''JKS'' will be used for newly-created key stores."),



  /**
   * Trust the server certificates without prompting the end user.  By default, the server certificate chain will be displayed and the user will be interactively prompted about whether to trust the certificate.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_NO_PROMPT_DESC("Trust the server certificates without prompting the end user.  By default, the server certificate chain will be displayed and the user will be interactively prompted about whether to trust the certificate."),



  /**
   * The TCP port number of the server to which the connection should be established.  Unless the --use-ldap-start-tls argument is provided, the port number must be one on which the server expects to accept TLS-based connections.  If the --use-ldap-start-tls argument is provided, then the specified port must be one on which an LDAP server is listening for non-secure connections but on which clients may use the StartTLS extended operation to transition to using secure communication.  Standard secure port numbers include 636 for LDAPS and 443 for HTTPS, and the standard non-secure port for LDAP is 389.  This must be provided.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_PORT_DESC("The TCP port number of the server to which the connection should be established.  Unless the --use-ldap-start-tls argument is provided, the port number must be one on which the server expects to accept TLS-based connections.  If the --use-ldap-start-tls argument is provided, then the specified port must be one on which an LDAP server is listening for non-secure connections but on which clients may use the StartTLS extended operation to transition to using secure communication.  Standard secure port numbers include 636 for LDAPS and 443 for HTTPS, and the standard non-secure port for LDAP is 389.  This must be provided."),



  /**
   * Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_PROMPT_FOR_KS_PW_DESC("Interactively prompt for the key store password.  If the key store does not exist, then it will be created with this password.  A key store password is required when importing certificates, so one of the --keystore-password, --keystore-password-file, or --prompt-for-keystore-password arguments must be provided.  The password must contain at least six characters."),



  /**
   * Indicates that the tool should initially establish a non-secure connection to an LDAP server, and then use the StartTLS extended operation to transition to using secure communication.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_USE_START_TLS_DESC("Indicates that the tool should initially establish a non-secure connection to an LDAP server, and then use the StartTLS extended operation to transition to using secure communication."),



  /**
   * Display verbose information about the certificates in the server''s certificate chain.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_ARG_VERBOSE_DESC("Display verbose information about the certificates in the server''s certificate chain."),



  /**
   * Initiates a secure connection to a server to get that server''s certificate chain, and then adds those certificates to a key store so that it can be used as a trust store for that server.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_DESC("Initiates a secure connection to a server to get that server''s certificate chain, and then adds those certificates to a key store so that it can be used as a trust store for that server."),



  /**
   * Establishes a secure connection to the server ds.example.com on port 636 and adds that server''s certificate chain to the ''{0}'' key store with a base alias of ''ds.example.com:636''.  The tool will display verbose information about the certificate chain presented by the server, and will interactively prompt about whether to trust that chain.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_EXAMPLE_1("Establishes a secure connection to the server ds.example.com on port 636 and adds that server''s certificate chain to the ''{0}'' key store with a base alias of ''ds.example.com:636''.  The tool will display verbose information about the certificate chain presented by the server, and will interactively prompt about whether to trust that chain."),



  /**
   * Establishes a non-secure connection to ds.example.com on port 389, and then uses the LDAP StartTLS extended operation to transition to a secure connection.  It will then add the server''s issuer certificates to the ''{0}'' key store with a base alias of ''ds-start-tls-cert''.  The tool will trust the certificate chain without any confirmation from the user.
   */
  INFO_MANAGE_CERTS_SC_TRUST_SERVER_EXAMPLE_2("Establishes a non-secure connection to ds.example.com on port 389, and then uses the LDAP StartTLS extended operation to transition to a secure connection.  It will then add the server''s issuer certificates to the ''{0}'' key store with a base alias of ''ds-start-tls-cert''.  The tool will trust the certificate chain without any confirmation from the user."),



  /**
   * Manage certificates and private keys in a JKS or PKCS #12 key store.
   */
  INFO_MANAGE_CERTS_TOOL_DESC("Manage certificates and private keys in a JKS or PKCS #12 key store."),



  /**
   * Successfully added {0,number,0} certificates to the key store.
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_ADDED_CERTS_TO_KS("Successfully added {0,number,0} certificates to the key store."),



  /**
   * Successfully added 1 certificate to the key store.
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_ADDED_CERT_TO_KS("Successfully added 1 certificate to the key store."),



  /**
   * Successfully created a new {0} key store.
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_CERT_CREATED_KEYSTORE("Successfully created a new {0} key store."),



  /**
   * NOTE:  The following certificate will not be added to the key store because the --issuers-only argument was provided:
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_NOTE_OMITTED("NOTE:  The following certificate will not be added to the key store because the --issuers-only argument was provided:"),



  /**
   * Do you wish to trust this certificate chain and add the certificates into the trust store?
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_PROMPT_TRUST("Do you wish to trust this certificate chain and add the certificates into the trust store?"),



  /**
   * Retrieved the following certificate chain from {0}:
   */
  INFO_MANAGE_CERTS_TRUST_SERVER_RETRIEVED_CHAIN("Retrieved the following certificate chain from {0}:"),



  /**
   * Subject Alternative Name
   */
  INFO_SUBJECT_ALT_NAME_EXTENSION_NAME("Subject Alternative Name"),



  /**
   * Subject Key Identifier
   */
  INFO_SUBJECT_KEY_IDENTIFIER_EXTENSION_NAME("Subject Key Identifier"),



  /**
   * WARNING:  Certificate ''{0}'' is self-signed.  While this is valid and will yield encryption that is just as strong as if the certificate had been signed by another certificate, self-signed certificates can encourage bad behavior among clients because rather than configuring the client with explicit knowledge of the server certificate, the client might be configured to blindly trust any certificate that is presented to it, which leaves that communication vulnerable to man-in-the-middle attacks.  It is recommended that server certificates be signed by a common issuer so that clients can be configured to trust that issuer certificate, and they can automatically trust any certificate signed by that issuer.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_CERT_IS_SELF_SIGNED("WARNING:  Certificate ''{0}'' is self-signed.  While this is valid and will yield encryption that is just as strong as if the certificate had been signed by another certificate, self-signed certificates can encourage bad behavior among clients because rather than configuring the client with explicit knowledge of the server certificate, the client might be configured to blindly trust any certificate that is presented to it, which leaves that communication vulnerable to man-in-the-middle attacks.  It is recommended that server certificates be signed by a common issuer so that clients can be configured to trust that issuer certificate, and they can automatically trust any certificate signed by that issuer."),



  /**
   * WARNING:  An error occurred while attempting to determine whether CA certificate ''{0}'' is contained in the JVM-default trust store:  {1}
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_CHECK_CA_IN_TS_ERROR("WARNING:  An error occurred while attempting to determine whether CA certificate ''{0}'' is contained in the JVM-default trust store:  {1}"),



  /**
   * WARNING:  Certificate ''{0}'' will expire at {1}.  To ensure seamless operation, you will need to renew the certificate before that time.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_END_CERT_NEAR_EXPIRATION("WARNING:  Certificate ''{0}'' will expire at {1}.  To ensure seamless operation, you will need to renew the certificate before that time."),



  /**
   * WARNING:  Issuer certificate ''{0}'' will expire at {1}.  Clients will stop trusting a certificate once its issuer has expired.  To ensure seamless operation, you will need to renew the certificate before that time and ensure that the new certificate is signed by an issuer that will not expire in the near future.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_ISSUER_CERT_NEAR_EXPIRATION("WARNING:  Issuer certificate ''{0}'' will expire at {1}.  Clients will stop trusting a certificate once its issuer has expired.  To ensure seamless operation, you will need to renew the certificate before that time and ensure that the new certificate is signed by an issuer that will not expire in the near future."),



  /**
   * WARNING:  Issuer certificate ''{0}'' uses a signature algorithm of ''{1}'', which uses the weak SHA-1 message digest.  Many clients will not accept server certificates with this signature algorithm, and some may not accept issuer certificates with this algorithm.  However, this is not considered an error because the ''{2}'' argument was provided.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_ISSUER_WITH_SHA1_SIG("WARNING:  Issuer certificate ''{0}'' uses a signature algorithm of ''{1}'', which uses the weak SHA-1 message digest.  Many clients will not accept server certificates with this signature algorithm, and some may not accept issuer certificates with this algorithm.  However, this is not considered an error because the ''{2}'' argument was provided."),



  /**
   * WARNING:  Issuer certificate ''{0}'' does not have a basic constraints extension.  It is generally recommended that all issuer certificates have this extension to indicate that they are permitted to act as a certification authority.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_NO_BC("WARNING:  Issuer certificate ''{0}'' does not have a basic constraints extension.  It is generally recommended that all issuer certificates have this extension to indicate that they are permitted to act as a certification authority."),



  /**
   * WARNING:  Certificate ''{0}'' does not have an extended key usage extension.  It is generally recommended that TLS server certificates have an extended key usage extension with at least the serverAuth usage ID.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_NO_EKU("WARNING:  Certificate ''{0}'' does not have an extended key usage extension.  It is generally recommended that TLS server certificates have an extended key usage extension with at least the serverAuth usage ID."),



  /**
   * WARNING:  Issuer certificate ''{0}'' does not have a key usage extension.  It is generally recommended that all issuer certificates have this extension with at least the keyCertSign usage to indicate that they are allowed to sign other certificates.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_NO_KU("WARNING:  Issuer certificate ''{0}'' does not have a key usage extension.  It is generally recommended that all issuer certificates have this extension with at least the keyCertSign usage to indicate that they are allowed to sign other certificates."),



  /**
   * WARNING:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is not a recognized algorithm.  Unable to determine the strength of the signature algorithm.
   */
  WARN_MANAGE_CERTS_CHECK_USABILITY_UNKNOWN_SIG_ALG("WARNING:  Certificate ''{0}'' uses a signature algorithm of ''{1}'', which is not a recognized algorithm.  Unable to determine the strength of the signature algorithm."),



  /**
   * WARNING:  Unable to locate issuer certificate with subject DN ''{0}'' in either key store ''{1}'' or in the JVM''s default set of trusted certificates.  The certificate chain is incomplete.
   */
  WARN_MANAGE_CERTS_EXPORT_CERT_MISSING_CERT_IN_CHAIN("WARNING:  Unable to locate issuer certificate with subject DN ''{0}'' in either key store ''{1}'' or in the JVM''s default set of trusted certificates.  The certificate chain is incomplete."),



  /**
   * WARNING:  The certificate with subject ''{0}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When validating a certificate chain, many clients expect to be able to find all certificates in the chain.  Although the import will continue, you are strongly encouraged to find this issuer certificate, and any other certificates higher up the issuer chain, and import those certificates as well.
   */
  WARN_MANAGE_CERTS_IMPORT_CERT_NO_ISSUER_NO_AKI("WARNING:  The certificate with subject ''{0}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When validating a certificate chain, many clients expect to be able to find all certificates in the chain.  Although the import will continue, you are strongly encouraged to find this issuer certificate, and any other certificates higher up the issuer chain, and import those certificates as well."),



  /**
   * WARNING:  The certificate with subject ''{0}'' and subject key identifier ''{1}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When validating a certificate chain, many clients expect to be able to find all certificates in the chain.  Although the import will continue, you are strongly encouraged to find this issuer certificate, and any other certificates higher up the issuer chain, and import those certificates as well.
   */
  WARN_MANAGE_CERTS_IMPORT_CERT_NO_ISSUER_WITH_AKI("WARNING:  The certificate with subject ''{0}'' and subject key identifier ''{1}'' was not included in the set of certificates to import, is not already present in the key store, and is not included in the JVM''s default set of trusted issuers.  When validating a certificate chain, many clients expect to be able to find all certificates in the chain.  Although the import will continue, you are strongly encouraged to find this issuer certificate, and any other certificates higher up the issuer chain, and import those certificates as well."),



  /**
   * WARNING:  Alias ''{0}'' was requested for inclusion in the list of certificates, but there is no certificate with that alias in key store ''{1}''.
   */
  WARN_MANAGE_CERTS_LIST_CERTS_ALIAS_NOT_IN_KS("WARNING:  Alias ''{0}'' was requested for inclusion in the list of certificates, but there is no certificate with that alias in key store ''{1}''.");



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
      rb = ResourceBundle.getBundle("unboundid-ldapsdk-cert");
    } catch (final Exception e) {}
    RESOURCE_BUNDLE = rb;
  }



  /**
   * The map that will be used to hold the unformatted message strings, indexed by property name.
   */
  private static final ConcurrentHashMap<CertMessages,String> MESSAGE_STRINGS = new ConcurrentHashMap<>(100);



  /**
   * The map that will be used to hold the message format objects, indexed by property name.
   */
  private static final ConcurrentHashMap<CertMessages,MessageFormat> MESSAGES = new ConcurrentHashMap<>(100);



  // The default text for this message
  private final String defaultText;



  /**
   * Creates a new message key.
   */
  private CertMessages(final String defaultText)
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

