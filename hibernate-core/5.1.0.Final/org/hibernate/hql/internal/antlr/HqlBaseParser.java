// $ANTLR : "hql.g" -> "HqlBaseParser.java"$

package org.hibernate.hql.internal.antlr;

import org.hibernate.hql.internal.ast.*;
import org.hibernate.hql.internal.ast.util.*;


import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 * Hibernate Query Language Grammar
 * <br>
 * This grammar parses the query language for Hibernate (an Open Source, Object-Relational
 * mapping library).  A partial BNF grammar description is available for reference here:
 * http://www.hibernate.org/Documentation/HQLBNF
 *
 * Text from the original reference BNF is prefixed with '//##'.
 * @author Joshua Davis (pgmjsd@sourceforge.net)
 */
public class HqlBaseParser extends antlr.LLkParser       implements HqlTokenTypes
 {

    /** True if this is a filter query (allow no FROM clause). **/
	private boolean filter = false;

	/**
	 * Sets the filter flag.
	 * @param f True for a filter query, false for a normal query.
	 */
	public void setFilter(boolean f) {
		filter = f;
	}

	/**
	 * Returns true if this is a filter query, false if not.
	 * @return true if this is a filter query, false if not.
	 */
	public boolean isFilter() {
		return filter;
	}

	/**
	 * This method is overriden in the sub class in order to provide the
	 * 'keyword as identifier' hack.
	 * @param token The token to retry as an identifier.
	 * @param ex The exception to throw if it cannot be retried as an identifier.
	 */
	public AST handleIdentifierError(Token token,RecognitionException ex) throws RecognitionException, TokenStreamException {
		// Base implementation: Just re-throw the exception.
		throw ex;
	}

    /**
     * This method looks ahead and converts . <token> into . IDENT when
     * appropriate.
     */
    public void handleDotIdent() throws TokenStreamException {
    }

	/**
	 * Returns the negated equivalent of the expression.
	 * @param x The expression to negate.
	 */
	public AST negateNode(AST x) {
		// Just create a 'not' parent for the default behavior.
		return ASTUtil.createParent(astFactory, NOT, "not", x);
	}

	/**
	 * Returns the 'cleaned up' version of a comparison operator sub-tree.
	 * @param x The comparison operator to clean up.
	 */
	public AST processEqualityExpression(AST x) throws RecognitionException {
		return x;
	}

	public void weakKeywords() throws TokenStreamException {
	}

	/**
	 * Called after we have recognized ':'.  The expectation is to handle converting
	 * any non-IDENT token where possibleID == true into an IDENT
	 */
	public void expectNamedParameterName() throws TokenStreamException {
	}

	public void processMemberOf(Token n,AST p,ASTPair currentAST) {
	}

	protected boolean validateSoftKeyword(String text) throws TokenStreamException {
		return validateLookAheadText(1, text);
	}

	protected boolean validateLookAheadText(int lookAheadPosition, String text) throws TokenStreamException {
		String text2Validate = retrieveLookAheadText( lookAheadPosition );
		return text2Validate == null ? false : text2Validate.equalsIgnoreCase( text );
	}

	protected String retrieveLookAheadText(int lookAheadPosition) throws TokenStreamException {
		Token token = LT(lookAheadPosition);
		return token == null ? null : token.getText();
	}

    protected String unquote(String text) {
        return text.substring( 1, text.length() - 1 );
    }

    protected void registerTreat(AST pathToTreat, AST treatAs) {
    }

protected HqlBaseParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public HqlBaseParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected HqlBaseParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public HqlBaseParser(TokenStream lexer) {
  this(lexer,3);
}

public HqlBaseParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case UPDATE:
			{
				updateStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case DELETE:
			{
				deleteStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case SELECT:
			case WHERE:
			{
				selectStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case INSERT:
			{
				insertStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			statement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = statement_AST;
	}
	
	public final void updateStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST updateStatement_AST = null;
		
		try {      // for error handling
			AST tmp1_AST = null;
			tmp1_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(UPDATE);
			{
			switch ( LA(1)) {
			case VERSIONED:
			{
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(VERSIONED);
				break;
			}
			case FROM:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			optionalFromTokenFromClause();
			astFactory.addASTChild(currentAST, returnAST);
			setClause();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			updateStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = updateStatement_AST;
	}
	
	public final void deleteStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST deleteStatement_AST = null;
		
		try {      // for error handling
			AST tmp3_AST = null;
			tmp3_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp3_AST);
			match(DELETE);
			{
			optionalFromTokenFromClause();
			astFactory.addASTChild(currentAST, returnAST);
			}
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			deleteStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = deleteStatement_AST;
	}
	
	public final void selectStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectStatement_AST = null;
		
		try {      // for error handling
			queryRule();
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement_AST = (AST)currentAST.root;
			
					selectStatement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(QUERY,"query")).add(selectStatement_AST));
				
			currentAST.root = selectStatement_AST;
			currentAST.child = selectStatement_AST!=null &&selectStatement_AST.getFirstChild()!=null ?
				selectStatement_AST.getFirstChild() : selectStatement_AST;
			currentAST.advanceChildToEnd();
			selectStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = selectStatement_AST;
	}
	
	public final void insertStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertStatement_AST = null;
		
		try {      // for error handling
			AST tmp4_AST = null;
			tmp4_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp4_AST);
			match(INSERT);
			intoClause();
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement();
			astFactory.addASTChild(currentAST, returnAST);
			insertStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = insertStatement_AST;
	}
	
	public final void optionalFromTokenFromClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST optionalFromTokenFromClause_AST = null;
		AST f_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FROM:
			{
				match(FROM);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			path();
			f_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				a_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case SET:
			case WHERE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			optionalFromTokenFromClause_AST = (AST)currentAST.root;
			
					AST range = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(RANGE,"RANGE")).add(f_AST).add(a_AST));
					optionalFromTokenFromClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FROM,"FROM")).add(range));
				
			currentAST.root = optionalFromTokenFromClause_AST;
			currentAST.child = optionalFromTokenFromClause_AST!=null &&optionalFromTokenFromClause_AST.getFirstChild()!=null ?
				optionalFromTokenFromClause_AST.getFirstChild() : optionalFromTokenFromClause_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = optionalFromTokenFromClause_AST;
	}
	
	public final void setClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST setClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp6_AST = null;
			tmp6_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp6_AST);
			match(SET);
			assignment();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop93:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					assignment();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop93;
				}
				
			} while (true);
			}
			}
			setClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = setClause_AST;
	}
	
	public final void whereClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST whereClause_AST = null;
		
		try {      // for error handling
			AST tmp8_AST = null;
			tmp8_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp8_AST);
			match(WHERE);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			whereClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = whereClause_AST;
	}
	
	public final void assignment() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignment_AST = null;
		
		try {      // for error handling
			stateField();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp9_AST);
			match(EQ);
			newValue();
			astFactory.addASTChild(currentAST, returnAST);
			assignment_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = assignment_AST;
	}
	
	public final void stateField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stateField_AST = null;
		
		try {      // for error handling
			path();
			astFactory.addASTChild(currentAST, returnAST);
			stateField_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		returnAST = stateField_AST;
	}
	
	public final void newValue() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newValue_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			newValue_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = newValue_AST;
	}
	
	public final void path() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST path_AST = null;
		
		try {      // for error handling
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop289:
			do {
				if ((LA(1)==DOT)) {
					AST tmp10_AST = null;
					tmp10_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp10_AST);
					match(DOT);
					weakKeywords();
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop289;
				}
				
			} while (true);
			}
			path_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		returnAST = path_AST;
	}
	
	public final void concatenation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST concatenation_AST = null;
		Token  c = null;
		AST c_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case CONCAT:
			{
				c = LT(1);
				c_AST = astFactory.create(c);
				astFactory.makeASTRoot(currentAST, c_AST);
				match(CONCAT);
				c_AST.setType(EXPR_LIST); c_AST.setText("concatList");
				additiveExpression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop206:
				do {
					if ((LA(1)==CONCAT)) {
						match(CONCAT);
						additiveExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop206;
					}
					
				} while (true);
				}
				concatenation_AST = (AST)currentAST.root;
				concatenation_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(METHOD_CALL,"||")).add((AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(IDENT,"concat")))).add(c_AST));
				currentAST.root = concatenation_AST;
				currentAST.child = concatenation_AST!=null &&concatenation_AST.getFirstChild()!=null ?
					concatenation_AST.getFirstChild() : concatenation_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case BETWEEN:
			case DESCENDING:
			case ESCAPE:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case IN:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case LIKE:
			case NOT:
			case OR:
			case ORDER:
			case RIGHT:
			case WHERE:
			case NULLS:
			case THEN:
			case MEMBER:
			case COMMA:
			case EQ:
			case CLOSE:
			case IDENT:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			concatenation_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = concatenation_AST;
	}
	
	public final void asAlias() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST asAlias_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case AS:
			{
				match(AS);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			alias();
			astFactory.addASTChild(currentAST, returnAST);
			asAlias_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_8);
		}
		returnAST = asAlias_AST;
	}
	
	public final void queryRule() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST queryRule_AST = null;
		
		try {      // for error handling
			selectFrom();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case GROUP:
			case ORDER:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case GROUP:
			{
				groupByClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case ORDER:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ORDER:
			{
				orderByClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			queryRule_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = queryRule_AST;
	}
	
	public final void intoClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST intoClause_AST = null;
		
		try {      // for error handling
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp13_AST);
			match(INTO);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			weakKeywords();
			insertablePropertySpec();
			astFactory.addASTChild(currentAST, returnAST);
			intoClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		returnAST = intoClause_AST;
	}
	
	public final void insertablePropertySpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertablePropertySpec_AST = null;
		
		try {      // for error handling
			match(OPEN);
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop108:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					primaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop108;
				}
				
			} while (true);
			}
			match(CLOSE);
			insertablePropertySpec_AST = (AST)currentAST.root;
			
					// Just need *something* to distinguish this on the hql-sql.g side
					insertablePropertySpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(RANGE,"column-spec")).add(insertablePropertySpec_AST));
				
			currentAST.root = insertablePropertySpec_AST;
			currentAST.child = insertablePropertySpec_AST!=null &&insertablePropertySpec_AST.getFirstChild()!=null ?
				insertablePropertySpec_AST.getFirstChild() : insertablePropertySpec_AST;
			currentAST.advanceChildToEnd();
			insertablePropertySpec_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		returnAST = insertablePropertySpec_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case FALSE:
			case NULL:
			case TRUE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case QUOTED_STRING:
			case NUM_INT:
			{
				constant();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case COLON:
			case PARAM:
			{
				parameter();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case OPEN:
			{
				match(OPEN);
				{
				switch ( LA(1)) {
				case ALL:
				case ANY:
				case AVG:
				case COUNT:
				case ELEMENTS:
				case EXISTS:
				case FALSE:
				case INDICES:
				case MAX:
				case MIN:
				case NOT:
				case NULL:
				case SOME:
				case SUM:
				case TRUE:
				case CASE:
				case EMPTY:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_BIG_INTEGER:
				case NUM_BIG_DECIMAL:
				case OPEN:
				case IDENT:
				case PLUS:
				case MINUS:
				case QUOTED_STRING:
				case COLON:
				case PARAM:
				case NUM_INT:
				{
					expressionOrVector();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case FROM:
				case GROUP:
				case ORDER:
				case SELECT:
				case WHERE:
				case CLOSE:
				{
					subQuery();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(CLOSE);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
				if (((LA(1)==IDENT) && (LA(2)==OPEN) && (LA(3)==QUOTED_STRING))&&( validateSoftKeyword("function") && LA(2) == OPEN && LA(3) == QUOTED_STRING )) {
					jpaFunctionSyntax();
					astFactory.addASTChild(currentAST, returnAST);
					primaryExpression_AST = (AST)currentAST.root;
				}
				else if (((LA(1)==IDENT) && (LA(2)==OPEN) && (_tokenSet_11.member(LA(3))))&&( validateSoftKeyword("cast") && LA(2) == OPEN )) {
					castFunction();
					astFactory.addASTChild(currentAST, returnAST);
					primaryExpression_AST = (AST)currentAST.root;
				}
				else if ((_tokenSet_12.member(LA(1))) && (_tokenSet_13.member(LA(2))) && (_tokenSet_14.member(LA(3)))) {
					identPrimary();
					astFactory.addASTChild(currentAST, returnAST);
					{
					if ((LA(1)==DOT) && (LA(2)==CLASS)) {
						AST tmp19_AST = null;
						tmp19_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp19_AST);
						match(DOT);
						AST tmp20_AST = null;
						tmp20_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp20_AST);
						match(CLASS);
					}
					else if ((_tokenSet_15.member(LA(1))) && (_tokenSet_16.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					primaryExpression_AST = (AST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = primaryExpression_AST;
	}
	
	public final void selectFrom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectFrom_AST = null;
		AST s_AST = null;
		AST f_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SELECT:
			{
				selectClause();
				s_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case WHERE:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FROM:
			{
				fromClause();
				f_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case GROUP:
			case ORDER:
			case WHERE:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			selectFrom_AST = (AST)currentAST.root;
			
					// If there was no FROM clause and this is a filter query, create a from clause.  Otherwise, throw
					// an exception because non-filter queries must have a FROM clause.
					if (f_AST == null) {
						if (filter) {
							f_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(FROM,"{filter-implied FROM}")));
						}
						else
							throw new SemanticException("FROM expected (non-filter queries must contain a FROM clause)");
					}
						
					// Create an artificial token so the 'FROM' can be placed
					// before the SELECT in the tree to make tree processing
					// simpler.
					selectFrom_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(SELECT_FROM,"SELECT_FROM")).add(f_AST).add(s_AST));
				
			currentAST.root = selectFrom_AST;
			currentAST.child = selectFrom_AST!=null &&selectFrom_AST.getFirstChild()!=null ?
				selectFrom_AST.getFirstChild() : selectFrom_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = selectFrom_AST;
	}
	
	public final void groupByClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST groupByClause_AST = null;
		
		try {      // for error handling
			AST tmp21_AST = null;
			tmp21_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp21_AST);
			match(GROUP);
			match(LITERAL_by);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop150:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop150;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case HAVING:
			{
				havingClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case ORDER:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			groupByClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = groupByClause_AST;
	}
	
	public final void orderByClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderByClause_AST = null;
		
		try {      // for error handling
			AST tmp24_AST = null;
			tmp24_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp24_AST);
			match(ORDER);
			match(LITERAL_by);
			orderElement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop154:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					orderElement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop154;
				}
				
			} while (true);
			}
			orderByClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = orderByClause_AST;
	}
	
	public final void selectClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectClause_AST = null;
		
		try {      // for error handling
			AST tmp27_AST = null;
			tmp27_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp27_AST);
			match(SELECT);
			weakKeywords();
			{
			switch ( LA(1)) {
			case DISTINCT:
			{
				AST tmp28_AST = null;
				tmp28_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp28_AST);
				match(DISTINCT);
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NEW:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case OBJECT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case IDENT:
			case PLUS:
			case MINUS:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case IDENT:
			case PLUS:
			case MINUS:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				selectedPropertiesList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NEW:
			{
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case OBJECT:
			{
				selectObject();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			selectClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = selectClause_AST;
	}
	
	public final void fromClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClause_AST = null;
		
		try {      // for error handling
			AST tmp29_AST = null;
			tmp29_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp29_AST);
			match(FROM);
			weakKeywords();
			fromRange();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop124:
			do {
				switch ( LA(1)) {
				case FULL:
				case INNER:
				case JOIN:
				case LEFT:
				case RIGHT:
				{
					fromJoin();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case COMMA:
				{
					match(COMMA);
					weakKeywords();
					fromRange();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					break _loop124;
				}
				}
			} while (true);
			}
			fromClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_17);
		}
		returnAST = fromClause_AST;
	}
	
	public final void selectedPropertiesList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectedPropertiesList_AST = null;
		
		try {      // for error handling
			aliasedExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop167:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					aliasedExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop167;
				}
				
			} while (true);
			}
			selectedPropertiesList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = selectedPropertiesList_AST;
	}
	
	public final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;
		Token  op = null;
		AST op_AST = null;
		
		try {      // for error handling
			{
			match(NEW);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			}
			op = LT(1);
			op_AST = astFactory.create(op);
			astFactory.makeASTRoot(currentAST, op_AST);
			match(OPEN);
			op_AST.setType(CONSTRUCTOR);
			selectedPropertiesList();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			newExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = newExpression_AST;
	}
	
	public final void selectObject() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectObject_AST = null;
		
		try {      // for error handling
			AST tmp34_AST = null;
			tmp34_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp34_AST);
			match(OBJECT);
			match(OPEN);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			selectObject_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = selectObject_AST;
	}
	
	public final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;
		
		try {      // for error handling
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(IDENT);
			identifier_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			
					identifier_AST = handleIdentifierError(LT(1),ex);
				
		}
		returnAST = identifier_AST;
	}
	
	public final void fromRange() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromRange_AST = null;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (_tokenSet_20.member(LA(2)))) {
				fromClassOrOuterQueryPath();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (LA(2)==IN) && (LA(3)==CLASS)) {
				inClassDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IN)) {
				inCollectionDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (LA(2)==IN) && (LA(3)==ELEMENTS)) {
				inCollectionElementsDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = fromRange_AST;
	}
	
	public final void fromJoin() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromJoin_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LEFT:
			case RIGHT:
			{
				{
				{
				switch ( LA(1)) {
				case LEFT:
				{
					AST tmp38_AST = null;
					tmp38_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp38_AST);
					match(LEFT);
					break;
				}
				case RIGHT:
				{
					AST tmp39_AST = null;
					tmp39_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp39_AST);
					match(RIGHT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case OUTER:
				{
					AST tmp40_AST = null;
					tmp40_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp40_AST);
					match(OUTER);
					break;
				}
				case JOIN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				break;
			}
			case FULL:
			{
				AST tmp41_AST = null;
				tmp41_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp41_AST);
				match(FULL);
				break;
			}
			case INNER:
			{
				AST tmp42_AST = null;
				tmp42_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(INNER);
				break;
			}
			case JOIN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			AST tmp43_AST = null;
			tmp43_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp43_AST);
			match(JOIN);
			{
			switch ( LA(1)) {
			case FETCH:
			{
				AST tmp44_AST = null;
				tmp44_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(FETCH);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			joinPath();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FETCH:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case WHERE:
			case ON:
			case WITH:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FETCH:
			{
				propertyFetch();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case WHERE:
			case ON:
			case WITH:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ON:
			case WITH:
			{
				withClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			fromJoin_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = fromJoin_AST;
	}
	
	public final void joinPath() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST joinPath_AST = null;
		
		try {      // for error handling
			if (((LA(1)==IDENT) && (LA(2)==OPEN))&&( validateSoftKeyword("treat") && LA(2) == OPEN )) {
				castedJoinPath();
				astFactory.addASTChild(currentAST, returnAST);
				joinPath_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (_tokenSet_22.member(LA(2)))) {
				path();
				astFactory.addASTChild(currentAST, returnAST);
				joinPath_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_23);
		}
		returnAST = joinPath_AST;
	}
	
	public final void propertyFetch() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST propertyFetch_AST = null;
		
		try {      // for error handling
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp45_AST);
			match(FETCH);
			match(ALL);
			match(PROPERTIES);
			propertyFetch_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_24);
		}
		returnAST = propertyFetch_AST;
	}
	
	public final void withClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST withClause_AST = null;
		AST le_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case WITH:
			{
				AST tmp48_AST = null;
				tmp48_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp48_AST);
				match(WITH);
				logicalExpression();
				astFactory.addASTChild(currentAST, returnAST);
				withClause_AST = (AST)currentAST.root;
				break;
			}
			case ON:
			{
				match(ON);
				logicalExpression();
				le_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				withClause_AST = (AST)currentAST.root;
				
					    // it's really just a WITH clause, so treat it as such...
					    withClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(WITH,"with")).add(le_AST));
					
				currentAST.root = withClause_AST;
				currentAST.child = withClause_AST!=null &&withClause_AST.getFirstChild()!=null ?
					withClause_AST.getFirstChild() : withClause_AST;
				currentAST.advanceChildToEnd();
				withClause_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = withClause_AST;
	}
	
/**
 * Represents the JPA 2.1 TREAT construct when applied to a join.  Hibernate already handles subclass
 * property references implicitly, so we simply "eat" all tokens of the TREAT construct and just return the
 * join path itself.
 *
 * Uses a validating semantic predicate to make sure the text of the matched first IDENT is the TREAT keyword
 */
	public final void castedJoinPath() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST castedJoinPath_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST p_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = astFactory.create(i);
			match(IDENT);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(AS);
			path();
			a_AST = (AST)returnAST;
			match(CLOSE);
			if (!(i.getText().equalsIgnoreCase("treat") ))
			  throw new SemanticException("i.getText().equalsIgnoreCase(\"treat\") ");
			
			registerTreat( p_AST, a_AST );
			
			castedJoinPath_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_23);
		}
		returnAST = castedJoinPath_AST;
	}
	
	public final void logicalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalExpression_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			logicalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_25);
		}
		returnAST = logicalExpression_AST;
	}
	
	public final void fromClassOrOuterQueryPath() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClassOrOuterQueryPath_AST = null;
		AST c_AST = null;
		AST a_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			path();
			c_AST = (AST)returnAST;
			weakKeywords();
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				a_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FETCH:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FETCH:
			{
				propertyFetch();
				p_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			fromClassOrOuterQueryPath_AST = (AST)currentAST.root;
			
					fromClassOrOuterQueryPath_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(RANGE,"RANGE")).add(c_AST).add(a_AST).add(p_AST));
				
			currentAST.root = fromClassOrOuterQueryPath_AST;
			currentAST.child = fromClassOrOuterQueryPath_AST!=null &&fromClassOrOuterQueryPath_AST.getFirstChild()!=null ?
				fromClassOrOuterQueryPath_AST.getFirstChild() : fromClassOrOuterQueryPath_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = fromClassOrOuterQueryPath_AST;
	}
	
	public final void inClassDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inClassDeclaration_AST = null;
		AST a_AST = null;
		AST c_AST = null;
		
		try {      // for error handling
			alias();
			a_AST = (AST)returnAST;
			match(IN);
			match(CLASS);
			path();
			c_AST = (AST)returnAST;
			inClassDeclaration_AST = (AST)currentAST.root;
			
					inClassDeclaration_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(RANGE,"RANGE")).add(c_AST).add(a_AST));
				
			currentAST.root = inClassDeclaration_AST;
			currentAST.child = inClassDeclaration_AST!=null &&inClassDeclaration_AST.getFirstChild()!=null ?
				inClassDeclaration_AST.getFirstChild() : inClassDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = inClassDeclaration_AST;
	}
	
	public final void inCollectionDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inCollectionDeclaration_AST = null;
		AST p_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			match(IN);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			match(CLOSE);
			asAlias();
			a_AST = (AST)returnAST;
			inCollectionDeclaration_AST = (AST)currentAST.root;
			
			inCollectionDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(JOIN,"join")).add(astFactory.create(INNER,"inner")).add(p_AST).add(a_AST));
				
			currentAST.root = inCollectionDeclaration_AST;
			currentAST.child = inCollectionDeclaration_AST!=null &&inCollectionDeclaration_AST.getFirstChild()!=null ?
				inCollectionDeclaration_AST.getFirstChild() : inCollectionDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = inCollectionDeclaration_AST;
	}
	
	public final void inCollectionElementsDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inCollectionElementsDeclaration_AST = null;
		AST a_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			alias();
			a_AST = (AST)returnAST;
			match(IN);
			match(ELEMENTS);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			match(CLOSE);
			inCollectionElementsDeclaration_AST = (AST)currentAST.root;
			
			inCollectionElementsDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(JOIN,"join")).add(astFactory.create(INNER,"inner")).add(p_AST).add(a_AST));
				
			currentAST.root = inCollectionElementsDeclaration_AST;
			currentAST.child = inCollectionElementsDeclaration_AST!=null &&inCollectionElementsDeclaration_AST.getFirstChild()!=null ?
				inCollectionElementsDeclaration_AST.getFirstChild() : inCollectionElementsDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = inCollectionElementsDeclaration_AST;
	}
	
	public final void alias() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST alias_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			identifier();
			a_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			a_AST.setType(ALIAS);
			alias_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_26);
		}
		returnAST = alias_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			expression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_27);
		}
		returnAST = expression_AST;
	}
	
	public final void havingClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST havingClause_AST = null;
		
		try {      // for error handling
			AST tmp62_AST = null;
			tmp62_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp62_AST);
			match(HAVING);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			havingClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = havingClause_AST;
	}
	
	public final void orderElement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderElement_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASCENDING:
			case DESCENDING:
			case LITERAL_ascending:
			case LITERAL_descending:
			{
				ascendingOrDescending();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case NULLS:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case NULLS:
			{
				nullOrdering();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			orderElement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = orderElement_AST;
	}
	
	public final void ascendingOrDescending() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ascendingOrDescending_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ASCENDING:
			case LITERAL_ascending:
			{
				{
				switch ( LA(1)) {
				case ASCENDING:
				{
					AST tmp63_AST = null;
					tmp63_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp63_AST);
					match(ASCENDING);
					break;
				}
				case LITERAL_ascending:
				{
					AST tmp64_AST = null;
					tmp64_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp64_AST);
					match(LITERAL_ascending);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				ascendingOrDescending_AST = (AST)currentAST.root;
				ascendingOrDescending_AST.setType(ASCENDING);
				ascendingOrDescending_AST = (AST)currentAST.root;
				break;
			}
			case DESCENDING:
			case LITERAL_descending:
			{
				{
				switch ( LA(1)) {
				case DESCENDING:
				{
					AST tmp65_AST = null;
					tmp65_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp65_AST);
					match(DESCENDING);
					break;
				}
				case LITERAL_descending:
				{
					AST tmp66_AST = null;
					tmp66_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp66_AST);
					match(LITERAL_descending);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				ascendingOrDescending_AST = (AST)currentAST.root;
				ascendingOrDescending_AST.setType(DESCENDING);
				ascendingOrDescending_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_29);
		}
		returnAST = ascendingOrDescending_AST;
	}
	
	public final void nullOrdering() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nullOrdering_AST = null;
		
		try {      // for error handling
			AST tmp67_AST = null;
			tmp67_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp67_AST);
			match(NULLS);
			nullPrecedence();
			astFactory.addASTChild(currentAST, returnAST);
			nullOrdering_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = nullOrdering_AST;
	}
	
	public final void nullPrecedence() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nullPrecedence_AST = null;
		
		try {      // for error handling
			AST tmp68_AST = null;
			tmp68_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp68_AST);
			match(IDENT);
			nullPrecedence_AST = (AST)currentAST.root;
			
			if ( "first".equalsIgnoreCase( nullPrecedence_AST.getText() ) ) {
			nullPrecedence_AST.setType( FIRST );
			}
			else if ( "last".equalsIgnoreCase( nullPrecedence_AST.getText() ) ) {
			nullPrecedence_AST.setType( LAST );
			}
			else {
			throw new SemanticException( "Expecting 'first' or 'last', but found '" +  nullPrecedence_AST.getText() + "' as null ordering precedence." );
			}
			
			nullPrecedence_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = nullPrecedence_AST;
	}
	
	public final void aliasedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aliasedExpression_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case AS:
			{
				AST tmp69_AST = null;
				tmp69_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp69_AST);
				match(AS);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			aliasedExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_30);
		}
		returnAST = aliasedExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop174:
			do {
				if ((LA(1)==OR)) {
					AST tmp70_AST = null;
					tmp70_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp70_AST);
					match(OR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop174;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_27);
		}
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;
		
		try {      // for error handling
			negatedExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop177:
			do {
				if ((LA(1)==AND)) {
					AST tmp71_AST = null;
					tmp71_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp71_AST);
					match(AND);
					negatedExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop177;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_31);
		}
		returnAST = logicalAndExpression_AST;
	}
	
	public final void negatedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST negatedExpression_AST = null;
		AST x_AST = null;
		AST y_AST = null;
		weakKeywords();
		
		try {      // for error handling
			switch ( LA(1)) {
			case NOT:
			{
				AST tmp72_AST = null;
				tmp72_AST = astFactory.create(LT(1));
				match(NOT);
				negatedExpression();
				x_AST = (AST)returnAST;
				negatedExpression_AST = (AST)currentAST.root;
				negatedExpression_AST = negateNode(x_AST);
				currentAST.root = negatedExpression_AST;
				currentAST.child = negatedExpression_AST!=null &&negatedExpression_AST.getFirstChild()!=null ?
					negatedExpression_AST.getFirstChild() : negatedExpression_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case IDENT:
			case PLUS:
			case MINUS:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				equalityExpression();
				y_AST = (AST)returnAST;
				negatedExpression_AST = (AST)currentAST.root;
				negatedExpression_AST = y_AST;
				currentAST.root = negatedExpression_AST;
				currentAST.child = negatedExpression_AST!=null &&negatedExpression_AST.getFirstChild()!=null ?
					negatedExpression_AST.getFirstChild() : negatedExpression_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_32);
		}
		returnAST = negatedExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;
		AST x_AST = null;
		Token  is = null;
		AST is_AST = null;
		Token  ne = null;
		AST ne_AST = null;
		AST y_AST = null;
		
		try {      // for error handling
			relationalExpression();
			x_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop183:
			do {
				if ((_tokenSet_33.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case EQ:
					{
						AST tmp73_AST = null;
						tmp73_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp73_AST);
						match(EQ);
						break;
					}
					case IS:
					{
						is = LT(1);
						is_AST = astFactory.create(is);
						astFactory.makeASTRoot(currentAST, is_AST);
						match(IS);
						is_AST.setType(EQ);
						{
						switch ( LA(1)) {
						case NOT:
						{
							match(NOT);
							is_AST.setType(NE);
							break;
						}
						case ALL:
						case ANY:
						case AVG:
						case COUNT:
						case ELEMENTS:
						case EXISTS:
						case FALSE:
						case INDICES:
						case MAX:
						case MIN:
						case NULL:
						case SOME:
						case SUM:
						case TRUE:
						case CASE:
						case EMPTY:
						case NUM_DOUBLE:
						case NUM_FLOAT:
						case NUM_LONG:
						case NUM_BIG_INTEGER:
						case NUM_BIG_DECIMAL:
						case OPEN:
						case IDENT:
						case PLUS:
						case MINUS:
						case QUOTED_STRING:
						case COLON:
						case PARAM:
						case NUM_INT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						break;
					}
					case NE:
					{
						AST tmp75_AST = null;
						tmp75_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp75_AST);
						match(NE);
						break;
					}
					case SQL_NE:
					{
						ne = LT(1);
						ne_AST = astFactory.create(ne);
						astFactory.makeASTRoot(currentAST, ne_AST);
						match(SQL_NE);
						ne_AST.setType(NE);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					y_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop183;
				}
				
			} while (true);
			}
			equalityExpression_AST = (AST)currentAST.root;
			
						// Post process the equality expression to clean up 'is null', etc.
						equalityExpression_AST = processEqualityExpression(equalityExpression_AST);
					
			currentAST.root = equalityExpression_AST;
			currentAST.child = equalityExpression_AST!=null &&equalityExpression_AST.getFirstChild()!=null ?
				equalityExpression_AST.getFirstChild() : equalityExpression_AST;
			currentAST.advanceChildToEnd();
			equalityExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_32);
		}
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;
		Token  n = null;
		AST n_AST = null;
		Token  i = null;
		AST i_AST = null;
		Token  b = null;
		AST b_AST = null;
		Token  l = null;
		AST l_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case DESCENDING:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case OR:
			case ORDER:
			case RIGHT:
			case WHERE:
			case NULLS:
			case THEN:
			case COMMA:
			case EQ:
			case CLOSE:
			case IDENT:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case CLOSE_BRACKET:
			{
				{
				{
				_loop189:
				do {
					if (((LA(1) >= LT && LA(1) <= GE))) {
						{
						switch ( LA(1)) {
						case LT:
						{
							AST tmp76_AST = null;
							tmp76_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp76_AST);
							match(LT);
							break;
						}
						case GT:
						{
							AST tmp77_AST = null;
							tmp77_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp77_AST);
							match(GT);
							break;
						}
						case LE:
						{
							AST tmp78_AST = null;
							tmp78_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp78_AST);
							match(LE);
							break;
						}
						case GE:
						{
							AST tmp79_AST = null;
							tmp79_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp79_AST);
							match(GE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						additiveExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop189;
					}
					
				} while (true);
				}
				}
				break;
			}
			case BETWEEN:
			case IN:
			case LIKE:
			case NOT:
			case MEMBER:
			{
				{
				switch ( LA(1)) {
				case NOT:
				{
					n = LT(1);
					n_AST = astFactory.create(n);
					match(NOT);
					break;
				}
				case BETWEEN:
				case IN:
				case LIKE:
				case MEMBER:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case IN:
				{
					{
					i = LT(1);
					i_AST = astFactory.create(i);
					astFactory.makeASTRoot(currentAST, i_AST);
					match(IN);
					
										i_AST.setType( (n == null) ? IN : NOT_IN);
										i_AST.setText( (n == null) ? "in" : "not in");
									
					inList();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case BETWEEN:
				{
					{
					b = LT(1);
					b_AST = astFactory.create(b);
					astFactory.makeASTRoot(currentAST, b_AST);
					match(BETWEEN);
					
										b_AST.setType( (n == null) ? BETWEEN : NOT_BETWEEN);
										b_AST.setText( (n == null) ? "between" : "not between");
									
					betweenList();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case LIKE:
				{
					{
					l = LT(1);
					l_AST = astFactory.create(l);
					astFactory.makeASTRoot(currentAST, l_AST);
					match(LIKE);
					
										l_AST.setType( (n == null) ? LIKE : NOT_LIKE);
										l_AST.setText( (n == null) ? "like" : "not like");
									
					concatenation();
					astFactory.addASTChild(currentAST, returnAST);
					likeEscape();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case MEMBER:
				{
					{
					match(MEMBER);
					{
					switch ( LA(1)) {
					case OF:
					{
						match(OF);
						break;
					}
					case IDENT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					memberOfPath();
					p_AST = (AST)returnAST;
					
									processMemberOf(n,p_AST,currentAST);
								
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			relationalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = relationalExpression_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;
		
		try {      // for error handling
			multiplyExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop210:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						AST tmp82_AST = null;
						tmp82_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp82_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						AST tmp83_AST = null;
						tmp83_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp83_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplyExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop210;
				}
				
			} while (true);
			}
			additiveExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_35);
		}
		returnAST = additiveExpression_AST;
	}
	
	public final void inList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inList_AST = null;
		AST x_AST = null;
		
		try {      // for error handling
			compoundExpr();
			x_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			inList_AST = (AST)currentAST.root;
			inList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(IN_LIST,"inList")).add(inList_AST));
			currentAST.root = inList_AST;
			currentAST.child = inList_AST!=null &&inList_AST.getFirstChild()!=null ?
				inList_AST.getFirstChild() : inList_AST;
			currentAST.advanceChildToEnd();
			inList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = inList_AST;
	}
	
	public final void betweenList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST betweenList_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			match(AND);
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			betweenList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = betweenList_AST;
	}
	
	public final void likeEscape() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST likeEscape_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ESCAPE:
			{
				AST tmp85_AST = null;
				tmp85_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp85_AST);
				match(ESCAPE);
				concatenation();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case DESCENDING:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case OR:
			case ORDER:
			case RIGHT:
			case WHERE:
			case NULLS:
			case THEN:
			case COMMA:
			case EQ:
			case CLOSE:
			case IDENT:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			likeEscape_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = likeEscape_AST;
	}
	
	public final void memberOfPath() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST memberOfPath_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST p_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			if (((LA(1)==IDENT) && (LA(2)==OPEN))&&( validateSoftKeyword("treat") && LA(2) == OPEN )) {
				i = LT(1);
				i_AST = astFactory.create(i);
				match(IDENT);
				match(OPEN);
				path();
				p_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				match(AS);
				path();
				a_AST = (AST)returnAST;
				match(CLOSE);
				{
				AST tmp89_AST = null;
				tmp89_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp89_AST);
				match(DOT);
				path();
				astFactory.addASTChild(currentAST, returnAST);
				}
				memberOfPath_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (_tokenSet_36.member(LA(2)))) {
				path();
				astFactory.addASTChild(currentAST, returnAST);
				memberOfPath_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = memberOfPath_AST;
	}
	
	public final void compoundExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundExpr_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			case IDENT:
			{
				path();
				astFactory.addASTChild(currentAST, returnAST);
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			case COLON:
			case PARAM:
			{
				parameter();
				astFactory.addASTChild(currentAST, returnAST);
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
				if (((LA(1)==OPEN) && (LA(2)==CLOSE) && (_tokenSet_34.member(LA(3))))&&( LA(1) == OPEN && LA(2) == CLOSE )) {
					match(OPEN);
					match(CLOSE);
					compoundExpr_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==OPEN) && (_tokenSet_37.member(LA(2))) && (_tokenSet_38.member(LA(3)))) {
					{
					match(OPEN);
					{
					switch ( LA(1)) {
					case ALL:
					case ANY:
					case AVG:
					case COUNT:
					case ELEMENTS:
					case EXISTS:
					case FALSE:
					case INDICES:
					case MAX:
					case MIN:
					case NOT:
					case NULL:
					case SOME:
					case SUM:
					case TRUE:
					case CASE:
					case EMPTY:
					case NUM_DOUBLE:
					case NUM_FLOAT:
					case NUM_LONG:
					case NUM_BIG_INTEGER:
					case NUM_BIG_DECIMAL:
					case OPEN:
					case IDENT:
					case PLUS:
					case MINUS:
					case QUOTED_STRING:
					case COLON:
					case PARAM:
					case NUM_INT:
					{
						{
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						{
						_loop278:
						do {
							if ((LA(1)==COMMA)) {
								match(COMMA);
								expression();
								astFactory.addASTChild(currentAST, returnAST);
							}
							else {
								break _loop278;
							}
							
						} while (true);
						}
						}
						break;
					}
					case FROM:
					case GROUP:
					case ORDER:
					case SELECT:
					case WHERE:
					case CLOSE:
					{
						subQuery();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(CLOSE);
					}
					compoundExpr_AST = (AST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = compoundExpr_AST;
	}
	
	public final void multiplyExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplyExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop214:
			do {
				if (((LA(1) >= STAR && LA(1) <= MOD))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp95_AST = null;
						tmp95_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp95_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp96_AST = null;
						tmp96_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp96_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp97_AST = null;
						tmp97_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp97_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop214;
				}
				
			} while (true);
			}
			multiplyExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_39);
		}
		returnAST = multiplyExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case MINUS:
			{
				AST tmp98_AST = null;
				tmp98_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp98_AST);
				match(MINUS);
				tmp98_AST.setType(UNARY_MINUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case PLUS:
			{
				AST tmp99_AST = null;
				tmp99_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp99_AST);
				match(PLUS);
				tmp99_AST.setType(UNARY_PLUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			{
				caseExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case ALL:
			case ANY:
			case EXISTS:
			case SOME:
			{
				quantifiedExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case AVG:
			case COUNT:
			case ELEMENTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NULL:
			case SUM:
			case TRUE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case IDENT:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				atom();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void caseExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseExpression_AST = null;
		
		try {      // for error handling
			if ((LA(1)==CASE) && (_tokenSet_41.member(LA(2)))) {
				simpleCaseStatement();
				astFactory.addASTChild(currentAST, returnAST);
				caseExpression_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==CASE) && (LA(2)==WHEN)) {
				searchedCaseStatement();
				astFactory.addASTChild(currentAST, returnAST);
				caseExpression_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = caseExpression_AST;
	}
	
	public final void quantifiedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST quantifiedExpression_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SOME:
			{
				AST tmp100_AST = null;
				tmp100_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp100_AST);
				match(SOME);
				break;
			}
			case EXISTS:
			{
				AST tmp101_AST = null;
				tmp101_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp101_AST);
				match(EXISTS);
				break;
			}
			case ALL:
			{
				AST tmp102_AST = null;
				tmp102_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp102_AST);
				match(ALL);
				break;
			}
			case ANY:
			{
				AST tmp103_AST = null;
				tmp103_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp103_AST);
				match(ANY);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case OPEN:
			{
				{
				match(OPEN);
				{
				subQuery();
				astFactory.addASTChild(currentAST, returnAST);
				}
				match(CLOSE);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			quantifiedExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = quantifiedExpression_AST;
	}
	
	public final void atom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST atom_AST = null;
		Token  op = null;
		AST op_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		try {      // for error handling
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop240:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					AST tmp106_AST = null;
					tmp106_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp106_AST);
					match(DOT);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
					{
					switch ( LA(1)) {
					case OPEN:
					{
						{
						op = LT(1);
						op_AST = astFactory.create(op);
						astFactory.makeASTRoot(currentAST, op_AST);
						match(OPEN);
						op_AST.setType(METHOD_CALL);
						exprList();
						astFactory.addASTChild(currentAST, returnAST);
						match(CLOSE);
						}
						break;
					}
					case EOF:
					case AND:
					case AS:
					case ASCENDING:
					case BETWEEN:
					case DESCENDING:
					case DOT:
					case ESCAPE:
					case FROM:
					case FULL:
					case GROUP:
					case HAVING:
					case IN:
					case INNER:
					case IS:
					case JOIN:
					case LEFT:
					case LIKE:
					case NOT:
					case OR:
					case ORDER:
					case RIGHT:
					case WHERE:
					case NULLS:
					case END:
					case ELSE:
					case THEN:
					case WHEN:
					case MEMBER:
					case COMMA:
					case EQ:
					case CLOSE:
					case IDENT:
					case LITERAL_ascending:
					case LITERAL_descending:
					case NE:
					case SQL_NE:
					case LT:
					case GT:
					case LE:
					case GE:
					case CONCAT:
					case PLUS:
					case MINUS:
					case STAR:
					case DIV:
					case MOD:
					case OPEN_BRACKET:
					case CLOSE_BRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case OPEN_BRACKET:
				{
					lb = LT(1);
					lb_AST = astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(OPEN_BRACKET);
					lb_AST.setType(INDEX_OP);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					match(CLOSE_BRACKET);
					break;
				}
				default:
				{
					break _loop240;
				}
				}
			} while (true);
			}
			atom_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = atom_AST;
	}
	
	public final void simpleCaseStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleCaseStatement_AST = null;
		
		try {      // for error handling
			AST tmp109_AST = null;
			tmp109_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp109_AST);
			match(CASE);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			int _cnt219=0;
			_loop219:
			do {
				if ((LA(1)==WHEN)) {
					simpleCaseWhenClause();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt219>=1 ) { break _loop219; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt219++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case ELSE:
			{
				elseClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case END:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(END);
			simpleCaseStatement_AST = (AST)currentAST.root;
			
					simpleCaseStatement_AST.setType(CASE2);
				
			simpleCaseStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = simpleCaseStatement_AST;
	}
	
	public final void searchedCaseStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST searchedCaseStatement_AST = null;
		
		try {      // for error handling
			AST tmp111_AST = null;
			tmp111_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp111_AST);
			match(CASE);
			{
			int _cnt227=0;
			_loop227:
			do {
				if ((LA(1)==WHEN)) {
					searchedCaseWhenClause();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt227>=1 ) { break _loop227; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt227++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case ELSE:
			{
				elseClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case END:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(END);
			searchedCaseStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_40);
		}
		returnAST = searchedCaseStatement_AST;
	}
	
	public final void simpleCaseWhenClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleCaseWhenClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp113_AST = null;
			tmp113_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp113_AST);
			match(WHEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			match(THEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			simpleCaseWhenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_42);
		}
		returnAST = simpleCaseWhenClause_AST;
	}
	
	public final void elseClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST elseClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp115_AST = null;
			tmp115_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp115_AST);
			match(ELSE);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			elseClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_43);
		}
		returnAST = elseClause_AST;
	}
	
	public final void searchedCaseWhenClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST searchedCaseWhenClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp116_AST = null;
			tmp116_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp116_AST);
			match(WHEN);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			match(THEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			searchedCaseWhenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_42);
		}
		returnAST = searchedCaseWhenClause_AST;
	}
	
	public final void collectionExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST collectionExpr_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ELEMENTS:
			{
				AST tmp118_AST = null;
				tmp118_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp118_AST);
				match(ELEMENTS);
				break;
			}
			case INDICES:
			{
				AST tmp119_AST = null;
				tmp119_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp119_AST);
				match(INDICES);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(OPEN);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			collectionExpr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = collectionExpr_AST;
	}
	
	public final void subQuery() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subQuery_AST = null;
		
		try {      // for error handling
			queryRule();
			astFactory.addASTChild(currentAST, returnAST);
			subQuery_AST = (AST)currentAST.root;
			subQuery_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(QUERY,"query")).add(subQuery_AST));
			currentAST.root = subQuery_AST;
			currentAST.child = subQuery_AST!=null &&subQuery_AST.getFirstChild()!=null ?
				subQuery_AST.getFirstChild() : subQuery_AST;
			currentAST.advanceChildToEnd();
			subQuery_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_44);
		}
		returnAST = subQuery_AST;
	}
	
	public final void exprList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exprList_AST = null;
		Token  t = null;
		AST t_AST = null;
		Token  l = null;
		AST l_AST = null;
		Token  b = null;
		AST b_AST = null;
		
		AST trimSpec = null;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case TRAILING:
			{
				t = LT(1);
				t_AST = astFactory.create(t);
				astFactory.addASTChild(currentAST, t_AST);
				match(TRAILING);
				trimSpec = t_AST;
				break;
			}
			case LEADING:
			{
				l = LT(1);
				l_AST = astFactory.create(l);
				astFactory.addASTChild(currentAST, l_AST);
				match(LEADING);
				trimSpec = l_AST;
				break;
			}
			case BOTH:
			{
				b = LT(1);
				b_AST = astFactory.create(b);
				astFactory.addASTChild(currentAST, b_AST);
				match(BOTH);
				trimSpec = b_AST;
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case FROM:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case CLOSE:
			case IDENT:
			case PLUS:
			case MINUS:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if(trimSpec != null) trimSpec.setType(IDENT);
			{
			switch ( LA(1)) {
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case OPEN:
			case IDENT:
			case PLUS:
			case MINUS:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					{
					int _cnt285=0;
					_loop285:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							expression();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							if ( _cnt285>=1 ) { break _loop285; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt285++;
					} while (true);
					}
					break;
				}
				case FROM:
				{
					AST tmp123_AST = null;
					tmp123_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp123_AST);
					match(FROM);
					tmp123_AST.setType(IDENT);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case AS:
				{
					match(AS);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case CLOSE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case FROM:
			{
				AST tmp125_AST = null;
				tmp125_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp125_AST);
				match(FROM);
				tmp125_AST.setType(IDENT);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			exprList_AST = (AST)currentAST.root;
			exprList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR_LIST,"exprList")).add(exprList_AST));
			currentAST.root = exprList_AST;
			currentAST.child = exprList_AST!=null &&exprList_AST.getFirstChild()!=null ?
				exprList_AST.getFirstChild() : exprList_AST;
			currentAST.advanceChildToEnd();
			exprList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_44);
		}
		returnAST = exprList_AST;
	}
	
	public final void jpaFunctionSyntax() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST jpaFunctionSyntax_AST = null;
		Token  i = null;
		AST i_AST = null;
		Token  n = null;
		AST n_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = astFactory.create(i);
			match(IDENT);
			AST tmp126_AST = null;
			tmp126_AST = astFactory.create(LT(1));
			match(OPEN);
			n = LT(1);
			n_AST = astFactory.create(n);
			match(QUOTED_STRING);
			AST tmp127_AST = null;
			tmp127_AST = astFactory.create(LT(1));
			match(COMMA);
			exprList();
			a_AST = (AST)returnAST;
			AST tmp128_AST = null;
			tmp128_AST = astFactory.create(LT(1));
			match(CLOSE);
			jpaFunctionSyntax_AST = (AST)currentAST.root;
			
				final String functionName = unquote( n_AST.getText() );
			
				if ( functionName.equalsIgnoreCase( "cast" ) ) {
						i_AST.setType( CAST );
						i_AST.setText( i_AST.getText() + " (" + functionName + ")" );
						final AST expression = a_AST.getFirstChild();
						final AST type = expression.getNextSibling();
					jpaFunctionSyntax_AST = (AST)astFactory.make( (new ASTArray(3)).add(i_AST).add(expression).add(type));
				}
				else {
						i_AST.setType( METHOD_CALL );
						i_AST.setText( i_AST.getText() + " (" + functionName + ")" );
						jpaFunctionSyntax_AST = (AST)astFactory.make( (new ASTArray(3)).add(i_AST).add(astFactory.create(IDENT,unquote(n.getText()))).add(a_AST));
				}
			
			currentAST.root = jpaFunctionSyntax_AST;
			currentAST.child = jpaFunctionSyntax_AST!=null &&jpaFunctionSyntax_AST.getFirstChild()!=null ?
				jpaFunctionSyntax_AST.getFirstChild() : jpaFunctionSyntax_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = jpaFunctionSyntax_AST;
	}
	
	public final void castFunction() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST castFunction_AST = null;
		Token  c = null;
		AST c_AST = null;
		AST e_AST = null;
		AST t_AST = null;
		
		try {      // for error handling
			c = LT(1);
			c_AST = astFactory.create(c);
			match(IDENT);
			AST tmp129_AST = null;
			tmp129_AST = astFactory.create(LT(1));
			match(OPEN);
			expression();
			e_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case AS:
			{
				AST tmp130_AST = null;
				tmp130_AST = astFactory.create(LT(1));
				match(AS);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			castTargetType();
			t_AST = (AST)returnAST;
			AST tmp131_AST = null;
			tmp131_AST = astFactory.create(LT(1));
			match(CLOSE);
			castFunction_AST = (AST)currentAST.root;
			
					c_AST.setType( CAST );
					castFunction_AST = (AST)astFactory.make( (new ASTArray(3)).add(c_AST).add(e_AST).add(t_AST));
				
			currentAST.root = castFunction_AST;
			currentAST.child = castFunction_AST!=null &&castFunction_AST.getFirstChild()!=null ?
				castFunction_AST.getFirstChild() : castFunction_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = castFunction_AST;
	}
	
	public final void identPrimary() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identPrimary_AST = null;
		AST i_AST = null;
		Token  o = null;
		AST o_AST = null;
		Token  op = null;
		AST op_AST = null;
		AST e_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				identPrimaryBase();
				i_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				handleDotIdent();
				{
				_loop260:
				do {
					if ((LA(1)==DOT) && (LA(2)==ELEMENTS||LA(2)==OBJECT||LA(2)==IDENT) && (_tokenSet_13.member(LA(3)))) {
						AST tmp132_AST = null;
						tmp132_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp132_AST);
						match(DOT);
						{
						switch ( LA(1)) {
						case IDENT:
						{
							identifier();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case ELEMENTS:
						{
							AST tmp133_AST = null;
							tmp133_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp133_AST);
							match(ELEMENTS);
							break;
						}
						case OBJECT:
						{
							o = LT(1);
							o_AST = astFactory.create(o);
							astFactory.addASTChild(currentAST, o_AST);
							match(OBJECT);
							o_AST.setType(IDENT);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
					}
					else {
						break _loop260;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case OPEN:
				{
					{
					op = LT(1);
					op_AST = astFactory.create(op);
					astFactory.makeASTRoot(currentAST, op_AST);
					match(OPEN);
					op_AST.setType(METHOD_CALL);
					exprList();
					e_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					match(CLOSE);
					}
					identPrimary_AST = (AST)currentAST.root;
					
									    AST path = e_AST.getFirstChild();
									    if ( i_AST.getText().equalsIgnoreCase( "key" ) ) {
									        identPrimary_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(KEY)).add(path));
									    }
									    else if ( i_AST.getText().equalsIgnoreCase( "value" ) ) {
									        identPrimary_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(VALUE)).add(path));
									    }
									    else if ( i_AST.getText().equalsIgnoreCase( "entry" ) ) {
									        identPrimary_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ENTRY)).add(path));
									    }
									
					currentAST.root = identPrimary_AST;
					currentAST.child = identPrimary_AST!=null &&identPrimary_AST.getFirstChild()!=null ?
						identPrimary_AST.getFirstChild() : identPrimary_AST;
					currentAST.advanceChildToEnd();
					break;
				}
				case EOF:
				case AND:
				case AS:
				case ASCENDING:
				case BETWEEN:
				case DESCENDING:
				case DOT:
				case ESCAPE:
				case FROM:
				case FULL:
				case GROUP:
				case HAVING:
				case IN:
				case INNER:
				case IS:
				case JOIN:
				case LEFT:
				case LIKE:
				case NOT:
				case OR:
				case ORDER:
				case RIGHT:
				case WHERE:
				case NULLS:
				case END:
				case ELSE:
				case THEN:
				case WHEN:
				case MEMBER:
				case COMMA:
				case EQ:
				case CLOSE:
				case IDENT:
				case LITERAL_ascending:
				case LITERAL_descending:
				case NE:
				case SQL_NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case CONCAT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case MOD:
				case OPEN_BRACKET:
				case CLOSE_BRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				identPrimary_AST = (AST)currentAST.root;
				break;
			}
			case AVG:
			case COUNT:
			case ELEMENTS:
			case INDICES:
			case MAX:
			case MIN:
			case SUM:
			{
				aggregate();
				astFactory.addASTChild(currentAST, returnAST);
				identPrimary_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = identPrimary_AST;
	}
	
	public final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				AST tmp135_AST = null;
				tmp135_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp135_AST);
				match(NUM_INT);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				AST tmp136_AST = null;
				tmp136_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp136_AST);
				match(NUM_FLOAT);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_LONG:
			{
				AST tmp137_AST = null;
				tmp137_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp137_AST);
				match(NUM_LONG);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			{
				AST tmp138_AST = null;
				tmp138_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp138_AST);
				match(NUM_DOUBLE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_BIG_INTEGER:
			{
				AST tmp139_AST = null;
				tmp139_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp139_AST);
				match(NUM_BIG_INTEGER);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_BIG_DECIMAL:
			{
				AST tmp140_AST = null;
				tmp140_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp140_AST);
				match(NUM_BIG_DECIMAL);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case QUOTED_STRING:
			{
				AST tmp141_AST = null;
				tmp141_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp141_AST);
				match(QUOTED_STRING);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NULL:
			{
				AST tmp142_AST = null;
				tmp142_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp142_AST);
				match(NULL);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case TRUE:
			{
				AST tmp143_AST = null;
				tmp143_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp143_AST);
				match(TRUE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case FALSE:
			{
				AST tmp144_AST = null;
				tmp144_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp144_AST);
				match(FALSE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case EMPTY:
			{
				AST tmp145_AST = null;
				tmp145_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp145_AST);
				match(EMPTY);
				constant_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = constant_AST;
	}
	
	public final void parameter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameter_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case COLON:
			{
				AST tmp146_AST = null;
				tmp146_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp146_AST);
				match(COLON);
				expectNamedParameterName();
				AST tmp147_AST = null;
				tmp147_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp147_AST);
				match(IDENT);
				parameter_AST = (AST)currentAST.root;
				break;
			}
			case PARAM:
			{
				AST tmp148_AST = null;
				tmp148_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp148_AST);
				match(PARAM);
				{
				switch ( LA(1)) {
				case NUM_INT:
				{
					AST tmp149_AST = null;
					tmp149_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp149_AST);
					match(NUM_INT);
					break;
				}
				case EOF:
				case AND:
				case AS:
				case ASCENDING:
				case BETWEEN:
				case DESCENDING:
				case DOT:
				case ESCAPE:
				case FROM:
				case FULL:
				case GROUP:
				case HAVING:
				case IN:
				case INNER:
				case IS:
				case JOIN:
				case LEFT:
				case LIKE:
				case NOT:
				case OR:
				case ORDER:
				case RIGHT:
				case WHERE:
				case NULLS:
				case END:
				case ELSE:
				case THEN:
				case WHEN:
				case MEMBER:
				case COMMA:
				case EQ:
				case CLOSE:
				case IDENT:
				case LITERAL_ascending:
				case LITERAL_descending:
				case NE:
				case SQL_NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case CONCAT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case MOD:
				case OPEN_BRACKET:
				case CLOSE_BRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				parameter_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = parameter_AST;
	}
	
	public final void expressionOrVector() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionOrVector_AST = null;
		AST e_AST = null;
		AST v_AST = null;
		
		try {      // for error handling
			expression();
			e_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case COMMA:
			{
				vectorExpr();
				v_AST = (AST)returnAST;
				break;
			}
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expressionOrVector_AST = (AST)currentAST.root;
			
					// If this is a vector expression, create a parent node for it.
					if (v_AST != null)
						expressionOrVector_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(VECTOR_EXPR,"{vector}")).add(e_AST).add(v_AST));
					else
						expressionOrVector_AST = e_AST;
				
			currentAST.root = expressionOrVector_AST;
			currentAST.child = expressionOrVector_AST!=null &&expressionOrVector_AST.getFirstChild()!=null ?
				expressionOrVector_AST.getFirstChild() : expressionOrVector_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_44);
		}
		returnAST = expressionOrVector_AST;
	}
	
	public final void castTargetType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST castTargetType_AST = null;
		
		try {      // for error handling
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			handleDotIdent();
			{
			_loop249:
			do {
				if ((LA(1)==DOT)) {
					AST tmp150_AST = null;
					tmp150_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp150_AST);
					match(DOT);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop249;
				}
				
			} while (true);
			}
			castTargetType_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_44);
		}
		returnAST = castTargetType_AST;
	}
	
	public final void vectorExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST vectorExpr_AST = null;
		
		try {      // for error handling
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop256:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop256;
				}
				
			} while (true);
			}
			vectorExpr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_44);
		}
		returnAST = vectorExpr_AST;
	}
	
	public final void identPrimaryBase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identPrimaryBase_AST = null;
		AST i_AST = null;
		
		try {      // for error handling
			if (((LA(1)==IDENT) && (LA(2)==OPEN) && (LA(3)==IDENT))&&( validateSoftKeyword("treat") && LA(2) == OPEN )) {
				castedIdentPrimaryBase();
				astFactory.addASTChild(currentAST, returnAST);
				identPrimaryBase_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (_tokenSet_13.member(LA(2))) && (_tokenSet_45.member(LA(3)))) {
				identifier();
				i_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				identPrimaryBase_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = identPrimaryBase_AST;
	}
	
	public final void aggregate() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregate_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case AVG:
			case MAX:
			case MIN:
			case SUM:
			{
				{
				switch ( LA(1)) {
				case SUM:
				{
					AST tmp153_AST = null;
					tmp153_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp153_AST);
					match(SUM);
					break;
				}
				case AVG:
				{
					AST tmp154_AST = null;
					tmp154_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp154_AST);
					match(AVG);
					break;
				}
				case MAX:
				{
					AST tmp155_AST = null;
					tmp155_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp155_AST);
					match(MAX);
					break;
				}
				case MIN:
				{
					AST tmp156_AST = null;
					tmp156_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp156_AST);
					match(MIN);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(OPEN);
				additiveExpression();
				astFactory.addASTChild(currentAST, returnAST);
				match(CLOSE);
				aggregate_AST = (AST)currentAST.root;
				aggregate_AST.setType(AGGREGATE);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			case COUNT:
			{
				AST tmp159_AST = null;
				tmp159_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp159_AST);
				match(COUNT);
				match(OPEN);
				{
				switch ( LA(1)) {
				case STAR:
				{
					AST tmp161_AST = null;
					tmp161_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp161_AST);
					match(STAR);
					tmp161_AST.setType(ROW_STAR);
					break;
				}
				case ALL:
				case DISTINCT:
				case ELEMENTS:
				case INDICES:
				case CASE:
				case IDENT:
				case NUM_INT:
				{
					{
					{
					switch ( LA(1)) {
					case DISTINCT:
					{
						AST tmp162_AST = null;
						tmp162_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp162_AST);
						match(DISTINCT);
						break;
					}
					case ALL:
					{
						AST tmp163_AST = null;
						tmp163_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp163_AST);
						match(ALL);
						break;
					}
					case ELEMENTS:
					case INDICES:
					case CASE:
					case IDENT:
					case NUM_INT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					{
					switch ( LA(1)) {
					case IDENT:
					{
						path();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case ELEMENTS:
					case INDICES:
					{
						collectionExpr();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case NUM_INT:
					{
						AST tmp164_AST = null;
						tmp164_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp164_AST);
						match(NUM_INT);
						break;
					}
					case CASE:
					{
						caseExpression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(CLOSE);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = aggregate_AST;
	}
	
	public final void castedIdentPrimaryBase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST castedIdentPrimaryBase_AST = null;
		Token  i = null;
		AST i_AST = null;
		AST p_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = astFactory.create(i);
			match(IDENT);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			match(AS);
			path();
			a_AST = (AST)returnAST;
			match(CLOSE);
			if (!( i.getText().equals("treat") ))
			  throw new SemanticException(" i.getText().equals(\"treat\") ");
			
			registerTreat( p_AST, a_AST );
			
			castedIdentPrimaryBase_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = castedIdentPrimaryBase_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"all\"",
		"\"any\"",
		"\"and\"",
		"\"as\"",
		"\"asc\"",
		"\"avg\"",
		"\"between\"",
		"\"class\"",
		"\"count\"",
		"\"delete\"",
		"\"desc\"",
		"DOT",
		"\"distinct\"",
		"\"elements\"",
		"\"escape\"",
		"\"exists\"",
		"\"false\"",
		"\"fetch\"",
		"\"from\"",
		"\"full\"",
		"\"group\"",
		"\"having\"",
		"\"in\"",
		"\"indices\"",
		"\"inner\"",
		"\"insert\"",
		"\"into\"",
		"\"is\"",
		"\"join\"",
		"\"left\"",
		"\"like\"",
		"\"max\"",
		"\"min\"",
		"\"new\"",
		"\"not\"",
		"\"null\"",
		"\"or\"",
		"\"order\"",
		"\"outer\"",
		"\"properties\"",
		"\"right\"",
		"\"select\"",
		"\"set\"",
		"\"some\"",
		"\"sum\"",
		"\"true\"",
		"\"update\"",
		"\"versioned\"",
		"\"where\"",
		"\"nulls\"",
		"FIRST",
		"LAST",
		"\"case\"",
		"\"end\"",
		"\"else\"",
		"\"then\"",
		"\"when\"",
		"\"on\"",
		"\"with\"",
		"\"both\"",
		"\"empty\"",
		"\"leading\"",
		"\"member\"",
		"\"object\"",
		"\"of\"",
		"\"trailing\"",
		"KEY",
		"VALUE",
		"ENTRY",
		"AGGREGATE",
		"ALIAS",
		"CONSTRUCTOR",
		"CASE2",
		"CAST",
		"EXPR_LIST",
		"FILTER_ENTITY",
		"IN_LIST",
		"INDEX_OP",
		"IS_NOT_NULL",
		"IS_NULL",
		"METHOD_CALL",
		"NOT_BETWEEN",
		"NOT_IN",
		"NOT_LIKE",
		"ORDER_ELEMENT",
		"QUERY",
		"RANGE",
		"ROW_STAR",
		"SELECT_FROM",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"VECTOR_EXPR",
		"WEIRD_IDENT",
		"CONSTANT",
		"NUM_DOUBLE",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_BIG_INTEGER",
		"NUM_BIG_DECIMAL",
		"JAVA_CONSTANT",
		"COMMA",
		"EQ",
		"OPEN",
		"CLOSE",
		"IDENT",
		"\"by\"",
		"\"ascending\"",
		"\"descending\"",
		"NE",
		"SQL_NE",
		"LT",
		"GT",
		"LE",
		"GE",
		"CONCAT",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"MOD",
		"OPEN_BRACKET",
		"CLOSE_BRACKET",
		"QUOTED_STRING",
		"COLON",
		"PARAM",
		"NUM_INT",
		"ID_START_LETTER",
		"ID_LETTER",
		"ESCqs",
		"WS",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 4573968371548162L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 4503599627370498L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2199040032770L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 4503599627370498L, 1099511627776L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 0L, 2199023255552L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 7507591853657571778L, 2306932625236819968L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 589992749395363266L, 2323816725792948228L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 6922122800402530306L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 4540983043686402L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 73043684305277488L, -4503577104561995775L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 281578190279168L, 17592186044416L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 2175259818229810626L, 4611649734543671300L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { -6975029226776829966L, -566935683009L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 2175259818229810626L, 4611645336497160196L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 2248342810077878258L, -566935683051L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 4505798667403266L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 2199023255554L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 4505798671597570L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 4523404017303682L, 27487790694400L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 4523404015173634L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 6922052431658385538L, 27487790694400L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 6922052431658352770L, 27487790694400L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 6922052431656255490L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 580984156318597122L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 6922122800469639170L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 589991355611103618L, 2306081603236921344L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 2L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 9007199254740994L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 4505798671597570L, 9895604649984L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 589992455122731394L, 2306081603236921344L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 589992455122731458L, 2306081603236921344L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 2147483648L, 846623953387520L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 589992457270215106L, 2306928227190308864L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 589992749395363266L, 2341831124302430212L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 589992457270247874L, 2306928227190308864L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 77584667348963888L, -4503568308468973567L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 1815992985240459250L, -566935683059L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 589992749395363266L, 2449917515359322116L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 2175259818229777858L, 3458723831890313220L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 73043409427370544L, -4503577104561995775L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 1585267068834414592L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 144115188075855872L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 0L, 8796093022208L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { -6975029226776895502L, -566935683009L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	
	}
