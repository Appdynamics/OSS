/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

/*
 * @(#)SearchSequence.java	1.14 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.mail.imap.protocol;

import java.util.*;
import java.io.IOException;

import javax.mail.*;
import javax.mail.search.*;
import com.sun.mail.iap.*;

/**
 * This class traverses a search-tree and generates the 
 * corresponding IMAP search sequence. 
 *
 * @author	John Mani
 */
class SearchSequence {

    /**
     * Generate the IMAP search sequence for the given search expression. 
     */
    static Argument generateSequence(SearchTerm term, String charset) 
		throws SearchException, IOException {
	/* Call the appropriate handler depending on the type of
	 * the search-term ...
	 */
	if (term instanceof AndTerm) 		// AND
	    return and((AndTerm)term, charset);
	else if (term instanceof OrTerm) 	// OR
	    return or((OrTerm)term, charset);
	else if (term instanceof NotTerm) 	// NOT
	    return not((NotTerm)term, charset);
	else if (term instanceof HeaderTerm) 	// HEADER
	    return header((HeaderTerm)term, charset);
	else if (term instanceof FlagTerm) 	// FLAG
	    return flag((FlagTerm)term);
	else if (term instanceof FromTerm) {	// FROM
	    FromTerm fterm = (FromTerm)term;
	    return from(fterm.getAddress().toString(), charset);
	}
	else if (term instanceof FromStringTerm) { // FROM
	    FromStringTerm fterm = (FromStringTerm)term;
	    return from(fterm.getPattern(), charset);
	}
	else if (term instanceof RecipientTerm)	{ // RECIPIENT
	    RecipientTerm rterm = (RecipientTerm)term;
	    return recipient(rterm.getRecipientType(), 
			     rterm.getAddress().toString(),
			     charset);
	}
	else if (term instanceof RecipientStringTerm) { // RECIPIENT
	    RecipientStringTerm rterm = (RecipientStringTerm)term;
	    return recipient(rterm.getRecipientType(),
			     rterm.getPattern(),
			     charset);
	}
	else if (term instanceof SubjectTerm)	// SUBJECT
	    return subject((SubjectTerm)term, charset);
	else if (term instanceof BodyTerm)	// BODY
	    return body((BodyTerm)term, charset);
	else if (term instanceof SizeTerm)	// SIZE
	    return size((SizeTerm)term);
	else if (term instanceof SentDateTerm)	// SENTDATE
	    return sentdate((SentDateTerm)term);
	else if (term instanceof ReceivedDateTerm) // INTERNALDATE
	    return receiveddate((ReceivedDateTerm)term);
	else if (term instanceof MessageIDTerm) // MessageID
	    return messageid((MessageIDTerm)term, charset);
	else
	    throw new SearchException("Search too complex");
    }

    /* 
     * Check if the "text" terms in the given SearchTerm contain
     * non US-ASCII characters.
     */
    static boolean isAscii(SearchTerm term) {
	if (term instanceof AndTerm || term instanceof OrTerm) {
	    SearchTerm[] terms;
	    if (term instanceof AndTerm)
		terms = ((AndTerm)term).getTerms();
	    else
		terms = ((OrTerm)term).getTerms();

	    for (int i = 0; i < terms.length; i++)
		if (!isAscii(terms[i])) // outta here !
		    return false;
	} else if (term instanceof NotTerm)
	    return isAscii(((NotTerm)term).getTerm());
	else if (term instanceof StringTerm)
	    return isAscii(((StringTerm)term).getPattern());
	else if (term instanceof AddressTerm)
	    return isAscii(((AddressTerm)term).getAddress().toString());
	
	// Any other term returns true.
	return true;
    }

    private static boolean isAscii(String s) {
	int l = s.length();

	for (int i=0; i < l; i++) {
	    if ((int)s.charAt(i) > 0177) // non-ascii
		return false;
	}
	return true;
    }

    private static Argument and(AndTerm term, String charset) 
			throws SearchException, IOException {
	// Combine the sequences for both terms
	SearchTerm[] terms = term.getTerms();
	// Generate the search sequence for the first term
	Argument result = generateSequence(terms[0], charset);
	// Append other terms
	for (int i = 1; i < terms.length; i++)
	    result.append(generateSequence(terms[i], charset));
	return result;
    }

    private static Argument or(OrTerm term, String charset) 
			throws SearchException, IOException {
	SearchTerm[] terms = term.getTerms();

	/* The IMAP OR operator takes only two operands. So if
	 * we have more than 2 operands, group them into 2-operand
	 * OR Terms.
	 */
	if (terms.length > 2) {
	    SearchTerm t = terms[0];

	    // Include rest of the terms
	    for (int i = 1; i < terms.length; i++)
		t = new OrTerm(t, terms[i]);

	    term = (OrTerm)t; 	// set 'term' to the new jumbo OrTerm we
				// just created
	    terms = term.getTerms();
	}

	// 'term' now has only two operands
	Argument result = new Argument();

	// Add the OR search-key
	result.writeAtom("OR");

	/* If this term is an AND expression, we need to enclose it
	 * within paranthesis.
	 *
	 * AND expressions are either AndTerms or FlagTerms 
	 */
	if (terms[0] instanceof AndTerm || terms[0] instanceof FlagTerm)
	    result.writeArgument(generateSequence(terms[0], charset));
	else
	    result.append(generateSequence(terms[0], charset));

	// Repeat the above for the second term
	if (terms[1] instanceof AndTerm || terms[1] instanceof FlagTerm)
	    result.writeArgument(generateSequence(terms[1], charset));
	else
	    result.append(generateSequence(terms[1], charset));

	return result;
    }

    private static Argument not(NotTerm term, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();

	// Add the NOT search-key
	result.writeAtom("NOT");

	/* If this term is an AND expression, we need to enclose it
	 * within paranthesis. 
	 *
	 * AND expressions are either AndTerms or FlagTerms 
	 */
	SearchTerm nterm = term.getTerm();
	if (nterm instanceof AndTerm || nterm instanceof FlagTerm)
	    result.writeArgument(generateSequence(nterm, charset));
	else
	    result.append(generateSequence(nterm, charset));

	return result;
    }

    private static Argument header(HeaderTerm term, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();
	result.writeAtom("HEADER");
	result.writeString(term.getHeaderName());
	result.writeString(term.getPattern(), charset);
	return result;
    }

    private static Argument messageid(MessageIDTerm term, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();
	result.writeAtom("HEADER");
	result.writeString("Message-ID");
	// XXX confirm that charset conversion ought to be done
	result.writeString(term.getPattern(), charset); 
	return result;
    }

    private static Argument flag(FlagTerm term) throws SearchException {
	boolean set = term.getTestSet();

	Argument result = new Argument();

	Flags flags = term.getFlags();
	Flags.Flag[] sf = flags.getSystemFlags();
	String[] uf = flags.getUserFlags();
	if (sf.length == 0 && uf.length == 0)
	    throw new SearchException("Invalid FlagTerm");

	for (int i = 0; i < sf.length; i++) {
	    if (sf[i] == Flags.Flag.DELETED)
		result.writeAtom(set ? "DELETED": "UNDELETED");
	    else if (sf[i] == Flags.Flag.ANSWERED)
		result.writeAtom(set ? "ANSWERED": "UNANSWERED");
	    else if (sf[i] == Flags.Flag.DRAFT)
		result.writeAtom(set ? "DRAFT": "UNDRAFT");
	    else if (sf[i] == Flags.Flag.FLAGGED)
		result.writeAtom(set ? "FLAGGED": "UNFLAGGED");
	    else if (sf[i] == Flags.Flag.RECENT)
		result.writeAtom(set ? "RECENT": "OLD");
	    else if (sf[i] == Flags.Flag.SEEN)
		result.writeAtom(set ? "SEEN": "UNSEEN");
	}

	for (int i = 0; i < uf.length; i++) {
	    result.writeAtom(set ? "KEYWORD" : "UNKEYWORD");
	    result.writeAtom(uf[i]);
	}
	
	return result;
    }

    private static Argument from(String address, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();
	result.writeAtom("FROM");
	result.writeString(address, charset);
	return result;
    }

    private static Argument recipient(Message.RecipientType type,
				      String address, String charset)
			throws SearchException, IOException {
	Argument result = new Argument();

	if (type == Message.RecipientType.TO)
	    result.writeAtom("TO");
	else if (type == Message.RecipientType.CC)
	    result.writeAtom("CC");
	else if (type == Message.RecipientType.BCC)
	    result.writeAtom("BCC");
	else
	    throw new SearchException("Illegal Recipient type");

	result.writeString(address, charset);
	return result;
    }

    private static Argument subject(SubjectTerm term, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();
	
	result.writeAtom("SUBJECT");
	result.writeString(term.getPattern(), charset);
	return result;
    }

    private static Argument body(BodyTerm term, String charset) 
			throws SearchException, IOException {
	Argument result = new Argument();

	result.writeAtom("BODY");
	result.writeString(term.getPattern(), charset);
	return result;
    }

    private static Argument size(SizeTerm term) 
			throws SearchException {
	Argument result = new Argument();

	switch (term.getComparison()) {
	    case ComparisonTerm.GT:
		result.writeAtom("LARGER");
		break;
	    case ComparisonTerm.LT:
		result.writeAtom("SMALLER");
		break;
	    default:
		// GT and LT is all we get from IMAP for size
	    	throw new SearchException("Cannot handle Comparison");
	}

	result.writeNumber(term.getNumber());
	return result;
    }

    // Date SEARCH stuff ...

    /**
     * Print an IMAP Date string, that is suitable for the Date
     * SEARCH commands.
     *
     * The IMAP Date string is :
     *	date ::= date_day "-" date_month "-" date_year	
     *
     * Note that this format does not contain the TimeZone
     */
    private static String monthTable[] = { 
	  "Jan", "Feb", "Mar", "Apr", "May", "Jun",
	  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    // A GregorianCalendar object in the current timezone
    private static Calendar cal = new GregorianCalendar();

    private static String toIMAPDate(Date date) {
	StringBuffer s = new StringBuffer();
	cal.setTime(date);

	s.append(cal.get(Calendar.DATE)).append("-");
	s.append(monthTable[cal.get(Calendar.MONTH)]).append('-');
	s.append(cal.get(Calendar.YEAR));

	return s.toString();
    }

    private static Argument sentdate(DateTerm term) 
			throws SearchException {
	Argument result = new Argument();
	String date = toIMAPDate(term.getDate());

	switch (term.getComparison()) {
	    case ComparisonTerm.GT:
		result.writeAtom("SENTSINCE " + date);
		break;
	    case ComparisonTerm.EQ:
		result.writeAtom("SENTON " + date);
		break;
	    case ComparisonTerm.LT:
		result.writeAtom("SENTBEFORE " + date);
		break;
	    case ComparisonTerm.GE:
		result.writeAtom("OR SENTSINCE " + date + " SENTON " + date);
		break;
	    case ComparisonTerm.LE:
		result.writeAtom("OR SENTBEFORE " + date + " SENTON " + date);
		break;
	    case ComparisonTerm.NE:
		result.writeAtom("NOT SENTON " + date);
		break;
	    default:
	    	throw new SearchException("Cannot handle Date Comparison");
	}

	return result;
    }

    private static Argument receiveddate(DateTerm term) 
			throws SearchException {
	Argument result = new Argument();
	String date = toIMAPDate(term.getDate());

	switch (term.getComparison()) {
	    case ComparisonTerm.GT:
		result.writeAtom("SINCE " + date);
		break;
	    case ComparisonTerm.EQ:
		result.writeAtom("ON " + date);
		break;
	    case ComparisonTerm.LT:
		result.writeAtom("BEFORE " + date);
		break;
	    case ComparisonTerm.GE:
		result.writeAtom("OR SINCE " + date + " ON " + date);
		break;
	    case ComparisonTerm.LE:
		result.writeAtom("OR BEFORE " + date + " ON " + date);
		break;
	    case ComparisonTerm.NE:
		result.writeAtom("NOT ON " + date);
		break;
	    default:
	    	throw new SearchException("Cannot handle Date Comparison");
	}

	return result;
    }
}
