// $ANTLR : "hql-sql.g" -> "HqlSqlBaseWalker.java"$

package org.hibernate.hql.internal.antlr;

import java.util.Stack;

import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


/**
 * Hibernate Query Language to SQL Tree Transform.<br>
 * This is a tree grammar that transforms an HQL AST into a intermediate SQL AST
 * with bindings to Hibernate interfaces (Queryable, etc.).  The Hibernate specific methods
 * are all implemented in the HqlSqlWalker subclass, allowing the ANTLR-generated class
 * to have only the minimum dependencies on the Hibernate code base.   This will also allow
 * the sub-class to be easily edited using an IDE (most IDE's don't support ANTLR).
 * <br>
 * <i>NOTE:</i> The java class is generated from hql-sql.g by ANTLR.
 * <i>DO NOT EDIT THE GENERATED JAVA SOURCE CODE.</i>
 * @author Joshua Davis (joshua@hibernate.org)
 */
public class HqlSqlBaseWalker extends antlr.TreeParser       implements HqlSqlTokenTypes
 {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, HqlSqlBaseWalker.class.getName());

	private int level = 0;

	private boolean inSelect = false;
	private boolean inFunctionCall = false;
	private boolean inCase = false;
	private boolean inFrom = false;
	private boolean inCount = false;
	private boolean inCountDistinct = false;

	private int statementType;
	private String statementTypeName;
	// Note: currentClauseType tracks the current clause within the current
	// statement, regardless of level; currentTopLevelClauseType, on the other
	// hand, tracks the current clause within the top (or primary) statement.
	// Thus, currentTopLevelClauseType ignores the clauses from any subqueries.
	private int currentClauseType;
	private int currentTopLevelClauseType;
	private int currentStatementType;
	private Stack<Integer> parentClauses = new Stack<Integer>();

	public final boolean isSubQuery() {
		return level > 1;
	}

	public final boolean isInFrom() {
		return inFrom;
	}

	public final boolean isInFunctionCall() {
		return inFunctionCall;
	}
	
	public final boolean isInSelect() {
		return inSelect;
	}

	public final boolean isInCase() {
		return inCase;
	}

    public final boolean isInCount() {
        return inCount;
    }

    public final boolean isInCountDistinct() {
        return inCountDistinct;
    }

	public final int getStatementType() {
		return statementType;
	}

	public final int getCurrentClauseType() {
		return currentClauseType;
	}

	public final int getCurrentTopLevelClauseType() {
		return currentTopLevelClauseType;
	}

	public final int getCurrentStatementType() {
		return currentStatementType;
	}

	public final boolean isComparativeExpressionClause() {
		// Note: once we add support for "JOIN ... ON ...",
		// the ON clause needs to get included here
	    return getCurrentClauseType() == WHERE ||
	            getCurrentClauseType() == WITH ||
	            isInCase();
	}

	public final boolean isSelectStatement() {
		return statementType == SELECT;
	}

	private void beforeStatement(String statementName, int statementType) {
		inFunctionCall = false;
		level++;
		if ( level == 1 ) {
			this.statementTypeName = statementName;
			this.statementType = statementType;
		}
		currentStatementType = statementType;
		LOG.debugf("%s << begin [level=%s, statement=%s]", statementName, level, this.statementTypeName);
	}

	private void beforeStatementCompletion(String statementName) {
        LOG.debugf("%s : finishing up [level=%s, statement=%s]", statementName, level, this.statementTypeName);
	}

	private void afterStatementCompletion(String statementName) {
        LOG.debugf("%s >> end [level=%s, statement=%s]", statementName, level, this.statementTypeName);
		level--;
	}

	private void handleClauseStart(int clauseType) {
		parentClauses.push(currentClauseType);
		currentClauseType = clauseType;
		if ( level == 1 ) {
			currentTopLevelClauseType = clauseType;
		}
	}

	private void handleClauseEnd() {
		currentClauseType = parentClauses.pop();
	}

	///////////////////////////////////////////////////////////////////////////
	// NOTE: The real implementations for the following are in the subclass.

	protected void evaluateAssignment(AST eq) throws SemanticException { }
	
	/** Pre-process the from clause input tree. **/
	protected void prepareFromClauseInputTree(AST fromClauseInput) {}

	/** Sets the current 'FROM' context. **/
	protected void pushFromClause(AST fromClause,AST inputFromNode) {}

	protected AST createFromElement(String path,AST alias,AST propertyFetch) throws SemanticException {
		return null;
	}

	protected void createFromJoinElement(AST path,AST alias,int joinType,AST fetch,AST propertyFetch,AST with) throws SemanticException {}

	protected AST createFromFilterElement(AST filterEntity,AST alias) throws SemanticException	{
		return null;
	}

	protected void processQuery(AST select,AST query) throws SemanticException { }

	protected void postProcessUpdate(AST update) throws SemanticException { }

	protected void postProcessDelete(AST delete) throws SemanticException { }

	protected void postProcessInsert(AST insert) throws SemanticException { }

	protected void beforeSelectClause() throws SemanticException { }

	protected void processIndex(AST indexOp) throws SemanticException { }

	protected void processConstant(AST constant) throws SemanticException { }

	protected void processBoolean(AST constant) throws SemanticException { }

	protected void processNumericLiteral(AST literal) throws SemanticException { }

	protected void resolve(AST node) throws SemanticException { }

	protected void resolveSelectExpression(AST dotNode) throws SemanticException { }

	protected void processFunction(AST functionCall,boolean inSelect) throws SemanticException { }

	protected void processCastFunction(AST functionCall,boolean inSelect) throws SemanticException { }

	protected void processAggregation(AST node, boolean inSelect) throws SemanticException { }

	protected void processConstructor(AST constructor) throws SemanticException { }

	protected AST generateNamedParameter(AST delimiterNode, AST nameNode) throws SemanticException {
		return (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(NAMED_PARAM,nameNode.getText())));
	}

	protected AST generatePositionalParameter(AST inputNode) throws SemanticException {
		return (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(PARAM,"?")));
	}

	protected void lookupAlias(AST ident) throws SemanticException { }

	protected void setAlias(AST selectExpr, AST ident) { }

	protected boolean isOrderExpressionResultVariableRef(AST ident) throws SemanticException {
		return false;
	}

	protected void handleResultVariableRef(AST resultVariableRef) throws SemanticException {
	}

	protected AST lookupProperty(AST dot,boolean root,boolean inSelect) throws SemanticException {
		return dot;
	}

	protected boolean isNonQualifiedPropertyRef(AST ident) { return false; }

	protected AST lookupNonQualifiedProperty(AST property) throws SemanticException { return property; }

	protected void setImpliedJoinType(int joinType) { }

	protected AST createIntoClause(String path, AST propertySpec) throws SemanticException {
		return null;
	};

	protected void prepareVersioned(AST updateNode, AST versionedNode) throws SemanticException {}

	protected void prepareLogicOperator(AST operator) throws SemanticException { }

	protected void prepareArithmeticOperator(AST operator) throws SemanticException { }

    protected void processMapComponentReference(AST node) throws SemanticException { }

	protected void validateMapPropertyExpression(AST node) throws SemanticException { }
	protected void finishFromClause (AST fromClause) throws SemanticException { }

public HqlSqlBaseWalker() {
	tokenNames = _tokenNames;
}

	public final void statement(AST _t) throws RecognitionException {
		
		AST statement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QUERY:
			{
				selectStatement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (AST)currentAST.root;
				break;
			}
			case UPDATE:
			{
				updateStatement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (AST)currentAST.root;
				break;
			}
			case DELETE:
			{
				deleteStatement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (AST)currentAST.root;
				break;
			}
			case INSERT:
			{
				insertStatement(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = statement_AST;
		_retTree = _t;
	}
	
	public final void selectStatement(AST _t) throws RecognitionException {
		
		AST selectStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectStatement_AST = null;
		
		try {      // for error handling
			query(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = selectStatement_AST;
		_retTree = _t;
	}
	
	public final void updateStatement(AST _t) throws RecognitionException {
		
		AST updateStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST updateStatement_AST = null;
		AST u = null;
		AST u_AST = null;
		AST v = null;
		AST v_AST = null;
		AST f_AST = null;
		AST f = null;
		AST s_AST = null;
		AST s = null;
		AST w_AST = null;
		AST w = null;
		
		try {      // for error handling
			AST __t361 = _t;
			u = _t==ASTNULL ? null :(AST)_t;
			AST u_AST_in = null;
			u_AST = astFactory.create(u);
			ASTPair __currentAST361 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,UPDATE);
			_t = _t.getFirstChild();
			beforeStatement( "update", UPDATE );
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VERSIONED:
			{
				v = (AST)_t;
				AST v_AST_in = null;
				v_AST = astFactory.create(v);
				match(_t,VERSIONED);
				_t = _t.getNextSibling();
				break;
			}
			case FROM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			f = _t==ASTNULL ? null : (AST)_t;
			fromClause(_t);
			_t = _retTree;
			f_AST = (AST)returnAST;
			s = _t==ASTNULL ? null : (AST)_t;
			setClause(_t);
			_t = _retTree;
			s_AST = (AST)returnAST;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				w = _t==ASTNULL ? null : (AST)_t;
				whereClause(_t);
				_t = _retTree;
				w_AST = (AST)returnAST;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST361;
			_t = __t361;
			_t = _t.getNextSibling();
			updateStatement_AST = (AST)currentAST.root;
			
					updateStatement_AST = (AST)astFactory.make( (new ASTArray(4)).add(u_AST).add(f_AST).add(s_AST).add(w_AST));
					beforeStatementCompletion( "update" );
					prepareVersioned( updateStatement_AST, v_AST );
					postProcessUpdate( updateStatement_AST );
					afterStatementCompletion( "update" );
				
			currentAST.root = updateStatement_AST;
			currentAST.child = updateStatement_AST!=null &&updateStatement_AST.getFirstChild()!=null ?
				updateStatement_AST.getFirstChild() : updateStatement_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = updateStatement_AST;
		_retTree = _t;
	}
	
	public final void deleteStatement(AST _t) throws RecognitionException {
		
		AST deleteStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST deleteStatement_AST = null;
		
		try {      // for error handling
			AST __t365 = _t;
			AST tmp1_AST = null;
			AST tmp1_AST_in = null;
			tmp1_AST = astFactory.create((AST)_t);
			tmp1_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp1_AST);
			ASTPair __currentAST365 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,DELETE);
			_t = _t.getFirstChild();
			beforeStatement( "delete", DELETE );
			fromClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				whereClause(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST365;
			_t = __t365;
			_t = _t.getNextSibling();
			deleteStatement_AST = (AST)currentAST.root;
			
					beforeStatementCompletion( "delete" );
					postProcessDelete( deleteStatement_AST );
					afterStatementCompletion( "delete" );
				
			deleteStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = deleteStatement_AST;
		_retTree = _t;
	}
	
	public final void insertStatement(AST _t) throws RecognitionException {
		
		AST insertStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertStatement_AST = null;
		
		try {      // for error handling
			AST __t368 = _t;
			AST tmp2_AST = null;
			AST tmp2_AST_in = null;
			tmp2_AST = astFactory.create((AST)_t);
			tmp2_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp2_AST);
			ASTPair __currentAST368 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INSERT);
			_t = _t.getFirstChild();
			beforeStatement( "insert", INSERT );
			intoClause(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			query(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST368;
			_t = __t368;
			_t = _t.getNextSibling();
			insertStatement_AST = (AST)currentAST.root;
			
					beforeStatementCompletion( "insert" );
					postProcessInsert( insertStatement_AST );
					afterStatementCompletion( "insert" );
				
			insertStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = insertStatement_AST;
		_retTree = _t;
	}
	
	public final void query(AST _t) throws RecognitionException {
		
		AST query_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST query_AST = null;
		AST f_AST = null;
		AST f = null;
		AST s_AST = null;
		AST s = null;
		AST w_AST = null;
		AST w = null;
		AST g_AST = null;
		AST g = null;
		AST o_AST = null;
		AST o = null;
		
		try {      // for error handling
			AST __t386 = _t;
			AST tmp3_AST = null;
			AST tmp3_AST_in = null;
			tmp3_AST = astFactory.create((AST)_t);
			tmp3_AST_in = (AST)_t;
			ASTPair __currentAST386 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,QUERY);
			_t = _t.getFirstChild();
			beforeStatement( "select", SELECT );
			AST __t387 = _t;
			AST tmp4_AST = null;
			AST tmp4_AST_in = null;
			tmp4_AST = astFactory.create((AST)_t);
			tmp4_AST_in = (AST)_t;
			ASTPair __currentAST387 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SELECT_FROM);
			_t = _t.getFirstChild();
			f = _t==ASTNULL ? null : (AST)_t;
			fromClause(_t);
			_t = _retTree;
			f_AST = (AST)returnAST;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT:
			{
				s = _t==ASTNULL ? null : (AST)_t;
				selectClause(_t);
				_t = _retTree;
				s_AST = (AST)returnAST;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST387;
			_t = __t387;
			_t = _t.getNextSibling();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				w = _t==ASTNULL ? null : (AST)_t;
				whereClause(_t);
				_t = _retTree;
				w_AST = (AST)returnAST;
				break;
			}
			case 3:
			case GROUP:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case GROUP:
			{
				g = _t==ASTNULL ? null : (AST)_t;
				groupClause(_t);
				_t = _retTree;
				g_AST = (AST)returnAST;
				break;
			}
			case 3:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ORDER:
			{
				o = _t==ASTNULL ? null : (AST)_t;
				orderClause(_t);
				_t = _retTree;
				o_AST = (AST)returnAST;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST386;
			_t = __t386;
			_t = _t.getNextSibling();
			query_AST = (AST)currentAST.root;
			
					// Antlr note: #x_in refers to the input AST, #x refers to the output AST
					query_AST = (AST)astFactory.make( (new ASTArray(6)).add(astFactory.create(SELECT,"SELECT")).add(s_AST).add(f_AST).add(w_AST).add(g_AST).add(o_AST));
					beforeStatementCompletion( "select" );
					processQuery( s_AST, query_AST );
					afterStatementCompletion( "select" );
				
			currentAST.root = query_AST;
			currentAST.child = query_AST!=null &&query_AST.getFirstChild()!=null ?
				query_AST.getFirstChild() : query_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = query_AST;
		_retTree = _t;
	}
	
	public final void fromClause(AST _t) throws RecognitionException {
		
		AST fromClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClause_AST = null;
		AST f = null;
		AST f_AST = null;
		
				// NOTE: This references the INPUT AST! (see http://www.antlr.org/doc/trees.html#Action%20Translation)
				// the ouput AST (#fromClause) has not been built yet.
				prepareFromClauseInputTree(fromClause_AST_in);
			
		
		try {      // for error handling
			AST __t429 = _t;
			f = _t==ASTNULL ? null :(AST)_t;
			AST f_AST_in = null;
			f_AST = astFactory.create(f);
			astFactory.addASTChild(currentAST, f_AST);
			ASTPair __currentAST429 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,FROM);
			_t = _t.getFirstChild();
			fromClause_AST = (AST)currentAST.root;
			pushFromClause(fromClause_AST,f); handleClauseStart( FROM );
			fromElementList(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST429;
			_t = __t429;
			_t = _t.getNextSibling();
			
					finishFromClause( f_AST );
					handleClauseEnd();
				
			fromClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = fromClause_AST;
		_retTree = _t;
	}
	
	public final void setClause(AST _t) throws RecognitionException {
		
		AST setClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST setClause_AST = null;
		
		try {      // for error handling
			AST __t377 = _t;
			AST tmp5_AST = null;
			AST tmp5_AST_in = null;
			tmp5_AST = astFactory.create((AST)_t);
			tmp5_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp5_AST);
			ASTPair __currentAST377 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SET);
			_t = _t.getFirstChild();
			handleClauseStart( SET );
			{
			_loop379:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==EQ)) {
					assignment(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop379;
				}
				
			} while (true);
			}
			currentAST = __currentAST377;
			_t = __t377;
			_t = _t.getNextSibling();
			
					handleClauseEnd();
				
			setClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = setClause_AST;
		_retTree = _t;
	}
	
	public final void whereClause(AST _t) throws RecognitionException {
		
		AST whereClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST whereClause_AST = null;
		AST w = null;
		AST w_AST = null;
		AST b_AST = null;
		AST b = null;
		
		try {      // for error handling
			AST __t454 = _t;
			w = _t==ASTNULL ? null :(AST)_t;
			AST w_AST_in = null;
			w_AST = astFactory.create(w);
			astFactory.addASTChild(currentAST, w_AST);
			ASTPair __currentAST454 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,WHERE);
			_t = _t.getFirstChild();
			handleClauseStart( WHERE );
			b = _t==ASTNULL ? null : (AST)_t;
			logicalExpr(_t);
			_t = _retTree;
			b_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST454;
			_t = __t454;
			_t = _t.getNextSibling();
			whereClause_AST = (AST)currentAST.root;
			
					// Use the *output* AST for the boolean expression!
					whereClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(w_AST).add(b_AST));
					handleClauseEnd();
				
			currentAST.root = whereClause_AST;
			currentAST.child = whereClause_AST!=null &&whereClause_AST.getFirstChild()!=null ?
				whereClause_AST.getFirstChild() : whereClause_AST;
			currentAST.advanceChildToEnd();
			whereClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = whereClause_AST;
		_retTree = _t;
	}
	
	public final void intoClause(AST _t) throws RecognitionException {
		
		AST intoClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST intoClause_AST = null;
		AST ps_AST = null;
		AST ps = null;
		
				String p = null;
			
		
		try {      // for error handling
			AST __t370 = _t;
			AST tmp6_AST = null;
			AST tmp6_AST_in = null;
			tmp6_AST = astFactory.create((AST)_t);
			tmp6_AST_in = (AST)_t;
			ASTPair __currentAST370 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,INTO);
			_t = _t.getFirstChild();
			handleClauseStart( INTO );
			{
			p=path(_t);
			_t = _retTree;
			}
			ps = _t==ASTNULL ? null : (AST)_t;
			insertablePropertySpec(_t);
			_t = _retTree;
			ps_AST = (AST)returnAST;
			currentAST = __currentAST370;
			_t = __t370;
			_t = _t.getNextSibling();
			intoClause_AST = (AST)currentAST.root;
			
					intoClause_AST = createIntoClause(p, ps);
					handleClauseEnd();
				
			currentAST.root = intoClause_AST;
			currentAST.child = intoClause_AST!=null &&intoClause_AST.getFirstChild()!=null ?
				intoClause_AST.getFirstChild() : intoClause_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = intoClause_AST;
		_retTree = _t;
	}
	
	public final String  path(AST _t) throws RecognitionException {
		String p;
		
		AST path_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST path_AST = null;
		AST a_AST = null;
		AST a = null;
		AST y_AST = null;
		AST y = null;
		
			p = "???";
			String x = "?x?";
			
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WEIRD_IDENT:
			case IDENT:
			{
				a = _t==ASTNULL ? null : (AST)_t;
				identifier(_t);
				_t = _retTree;
				a_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				p = a.getText();
				path_AST = (AST)currentAST.root;
				break;
			}
			case DOT:
			{
				AST __t449 = _t;
				AST tmp7_AST = null;
				AST tmp7_AST_in = null;
				tmp7_AST = astFactory.create((AST)_t);
				tmp7_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp7_AST);
				ASTPair __currentAST449 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DOT);
				_t = _t.getFirstChild();
				x=path(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				y = _t==ASTNULL ? null : (AST)_t;
				identifier(_t);
				_t = _retTree;
				y_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST449;
				_t = __t449;
				_t = _t.getNextSibling();
				
							StringBuilder buf = new StringBuilder();
							buf.append(x).append(".").append(y.getText());
							p = buf.toString();
						
				path_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = path_AST;
		_retTree = _t;
		return p;
	}
	
	public final void insertablePropertySpec(AST _t) throws RecognitionException {
		
		AST insertablePropertySpec_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertablePropertySpec_AST = null;
		
		try {      // for error handling
			AST __t373 = _t;
			AST tmp8_AST = null;
			AST tmp8_AST_in = null;
			tmp8_AST = astFactory.create((AST)_t);
			tmp8_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp8_AST);
			ASTPair __currentAST373 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,RANGE);
			_t = _t.getFirstChild();
			{
			int _cnt375=0;
			_loop375:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==IDENT)) {
					AST tmp9_AST = null;
					AST tmp9_AST_in = null;
					tmp9_AST = astFactory.create((AST)_t);
					tmp9_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp9_AST);
					match(_t,IDENT);
					_t = _t.getNextSibling();
				}
				else {
					if ( _cnt375>=1 ) { break _loop375; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt375++;
			} while (true);
			}
			currentAST = __currentAST373;
			_t = __t373;
			_t = _t.getNextSibling();
			insertablePropertySpec_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = insertablePropertySpec_AST;
		_retTree = _t;
	}
	
	public final void assignment(AST _t) throws RecognitionException {
		
		AST assignment_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignment_AST = null;
		AST p_AST = null;
		AST p = null;
		
		try {      // for error handling
			AST __t381 = _t;
			AST tmp10_AST = null;
			AST tmp10_AST_in = null;
			tmp10_AST = astFactory.create((AST)_t);
			tmp10_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp10_AST);
			ASTPair __currentAST381 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,EQ);
			_t = _t.getFirstChild();
			{
			p = _t==ASTNULL ? null : (AST)_t;
			propertyRef(_t);
			_t = _retTree;
			p_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			}
			resolve(p_AST);
			{
			newValue(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			}
			currentAST = __currentAST381;
			_t = __t381;
			_t = _t.getNextSibling();
			assignment_AST = (AST)currentAST.root;
			
					evaluateAssignment( assignment_AST );
				
			assignment_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = assignment_AST;
		_retTree = _t;
	}
	
	public final void propertyRef(AST _t) throws RecognitionException {
		
		AST propertyRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST propertyRef_AST = null;
		AST mcr_AST = null;
		AST mcr = null;
		AST d = null;
		AST d_AST = null;
		AST lhs_AST = null;
		AST lhs = null;
		AST rhs_AST = null;
		AST rhs = null;
		AST p_AST = null;
		AST p = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case KEY:
			case VALUE:
			case ENTRY:
			{
				mcr = _t==ASTNULL ? null : (AST)_t;
				mapComponentReference(_t);
				_t = _retTree;
				mcr_AST = (AST)returnAST;
				propertyRef_AST = (AST)currentAST.root;
				
					    resolve( mcr_AST );
					    propertyRef_AST = mcr_AST;
					
				currentAST.root = propertyRef_AST;
				currentAST.child = propertyRef_AST!=null &&propertyRef_AST.getFirstChild()!=null ?
					propertyRef_AST.getFirstChild() : propertyRef_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case DOT:
			{
				AST __t542 = _t;
				d = _t==ASTNULL ? null :(AST)_t;
				AST d_AST_in = null;
				d_AST = astFactory.create(d);
				ASTPair __currentAST542 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DOT);
				_t = _t.getFirstChild();
				lhs = _t==ASTNULL ? null : (AST)_t;
				propertyRefLhs(_t);
				_t = _retTree;
				lhs_AST = (AST)returnAST;
				rhs = _t==ASTNULL ? null : (AST)_t;
				propertyName(_t);
				_t = _retTree;
				rhs_AST = (AST)returnAST;
				currentAST = __currentAST542;
				_t = __t542;
				_t = _t.getNextSibling();
				propertyRef_AST = (AST)currentAST.root;
				
						// This gives lookupProperty() a chance to transform the tree to process collection properties (.elements, etc).
						propertyRef_AST = (AST)astFactory.make( (new ASTArray(3)).add(d_AST).add(lhs_AST).add(rhs_AST));
						propertyRef_AST = lookupProperty(propertyRef_AST,false,true);
					
				currentAST.root = propertyRef_AST;
				currentAST.child = propertyRef_AST!=null &&propertyRef_AST.getFirstChild()!=null ?
					propertyRef_AST.getFirstChild() : propertyRef_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case WEIRD_IDENT:
			case IDENT:
			{
				p = _t==ASTNULL ? null : (AST)_t;
				identifier(_t);
				_t = _retTree;
				p_AST = (AST)returnAST;
				propertyRef_AST = (AST)currentAST.root;
				
						// In many cases, things other than property-refs are recognized
						// by this propertyRef rule.  Some of those I have seen:
						//  1) select-clause from-aliases
						//  2) sql-functions
						if ( isNonQualifiedPropertyRef(p_AST) ) {
							propertyRef_AST = lookupNonQualifiedProperty(p_AST);
						}
						else {
							resolve(p_AST);
							propertyRef_AST = p_AST;
						}
					
				currentAST.root = propertyRef_AST;
				currentAST.child = propertyRef_AST!=null &&propertyRef_AST.getFirstChild()!=null ?
					propertyRef_AST.getFirstChild() : propertyRef_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = propertyRef_AST;
		_retTree = _t;
	}
	
	public final void newValue(AST _t) throws RecognitionException {
		
		AST newValue_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newValue_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				newValue_AST = (AST)currentAST.root;
				break;
			}
			case QUERY:
			{
				query(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				newValue_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = newValue_AST;
		_retTree = _t;
	}
	
	public final void expr(AST _t) throws RecognitionException {
		
		AST expr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expr_AST = null;
		AST ae_AST = null;
		AST ae = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			case KEY:
			case VALUE:
			case ENTRY:
			case INDEX_OP:
			case WEIRD_IDENT:
			case IDENT:
			{
				ae = _t==ASTNULL ? null : (AST)_t;
				addrExpr(_t, true );
				_t = _retTree;
				ae_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				resolve(ae_AST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case VECTOR_EXPR:
			{
				AST __t493 = _t;
				AST tmp11_AST = null;
				AST tmp11_AST_in = null;
				tmp11_AST = astFactory.create((AST)_t);
				tmp11_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp11_AST);
				ASTPair __currentAST493 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,VECTOR_EXPR);
				_t = _t.getFirstChild();
				{
				_loop495:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_0.member(_t.getType()))) {
						expr(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop495;
					}
					
				} while (true);
				}
				currentAST = __currentAST493;
				_t = __t493;
				_t = _t.getNextSibling();
				expr_AST = (AST)currentAST.root;
				break;
			}
			case FALSE:
			case NULL:
			case TRUE:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case QUOTED_STRING:
			case NUM_INT:
			{
				constant(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case AGGREGATE:
			case CAST:
			case METHOD_CALL:
			{
				functionCall(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case COLON:
			case PARAM:
			{
				parameter(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = expr_AST;
		_retTree = _t;
	}
	
	public final void selectClause(AST _t) throws RecognitionException {
		
		AST selectClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectClause_AST = null;
		AST d = null;
		AST d_AST = null;
		AST x_AST = null;
		AST x = null;
		
		try {      // for error handling
			AST __t409 = _t;
			AST tmp12_AST = null;
			AST tmp12_AST_in = null;
			tmp12_AST = astFactory.create((AST)_t);
			tmp12_AST_in = (AST)_t;
			ASTPair __currentAST409 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,SELECT);
			_t = _t.getFirstChild();
			handleClauseStart( SELECT ); beforeSelectClause();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DISTINCT:
			{
				d = (AST)_t;
				AST d_AST_in = null;
				d_AST = astFactory.create(d);
				match(_t,DISTINCT);
				_t = _t.getNextSibling();
				break;
			}
			case ALL:
			case AND:
			case AS:
			case BETWEEN:
			case COUNT:
			case DOT:
			case ELEMENTS:
			case EXISTS:
			case IN:
			case INDICES:
			case LIKE:
			case NOT:
			case OR:
			case CASE:
			case OBJECT:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case CAST:
			case IS_NOT_NULL:
			case IS_NULL:
			case METHOD_CALL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case QUERY:
			case UNARY_MINUS:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case EQ:
			case IDENT:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			x = _t==ASTNULL ? null : (AST)_t;
			selectExprList(_t);
			_t = _retTree;
			x_AST = (AST)returnAST;
			currentAST = __currentAST409;
			_t = __t409;
			_t = _t.getNextSibling();
			selectClause_AST = (AST)currentAST.root;
			
					selectClause_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(SELECT_CLAUSE,"{select clause}")).add(d_AST).add(x_AST));
					handleClauseEnd();
				
			currentAST.root = selectClause_AST;
			currentAST.child = selectClause_AST!=null &&selectClause_AST.getFirstChild()!=null ?
				selectClause_AST.getFirstChild() : selectClause_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = selectClause_AST;
		_retTree = _t;
	}
	
	public final void groupClause(AST _t) throws RecognitionException {
		
		AST groupClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST groupClause_AST = null;
		
		try {      // for error handling
			AST __t403 = _t;
			AST tmp13_AST = null;
			AST tmp13_AST_in = null;
			tmp13_AST = astFactory.create((AST)_t);
			tmp13_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp13_AST);
			ASTPair __currentAST403 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,GROUP);
			_t = _t.getFirstChild();
			handleClauseStart( GROUP );
			{
			int _cnt405=0;
			_loop405:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					expr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt405>=1 ) { break _loop405; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt405++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case HAVING:
			{
				AST __t407 = _t;
				AST tmp14_AST = null;
				AST tmp14_AST_in = null;
				tmp14_AST = astFactory.create((AST)_t);
				tmp14_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp14_AST);
				ASTPair __currentAST407 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,HAVING);
				_t = _t.getFirstChild();
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST407;
				_t = __t407;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST403;
			_t = __t403;
			_t = _t.getNextSibling();
			
					handleClauseEnd();
				
			groupClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = groupClause_AST;
		_retTree = _t;
	}
	
	public final void orderClause(AST _t) throws RecognitionException {
		
		AST orderClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderClause_AST = null;
		
		try {      // for error handling
			AST __t393 = _t;
			AST tmp15_AST = null;
			AST tmp15_AST_in = null;
			tmp15_AST = astFactory.create((AST)_t);
			tmp15_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp15_AST);
			ASTPair __currentAST393 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,ORDER);
			_t = _t.getFirstChild();
			handleClauseStart( ORDER );
			orderExprs(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST393;
			_t = __t393;
			_t = _t.getNextSibling();
			
					handleClauseEnd();
				
			orderClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = orderClause_AST;
		_retTree = _t;
	}
	
	public final void orderExprs(AST _t) throws RecognitionException {
		
		AST orderExprs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderExprs_AST = null;
		
		try {      // for error handling
			orderExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASCENDING:
			{
				AST tmp16_AST = null;
				AST tmp16_AST_in = null;
				tmp16_AST = astFactory.create((AST)_t);
				tmp16_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(_t,ASCENDING);
				_t = _t.getNextSibling();
				break;
			}
			case DESCENDING:
			{
				AST tmp17_AST = null;
				AST tmp17_AST_in = null;
				tmp17_AST = astFactory.create((AST)_t);
				tmp17_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp17_AST);
				match(_t,DESCENDING);
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case NULLS:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NULLS:
			{
				nullOrdering(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				orderExprs(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			orderExprs_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = orderExprs_AST;
		_retTree = _t;
	}
	
	public final void orderExpr(AST _t) throws RecognitionException {
		
		AST orderExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderExpr_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==WEIRD_IDENT||_t.getType()==IDENT))&&( isOrderExpressionResultVariableRef( _t ) )) {
				resultVariableRef(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				orderExpr_AST = (AST)currentAST.root;
			}
			else if ((_tokenSet_0.member(_t.getType()))) {
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				orderExpr_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = orderExpr_AST;
		_retTree = _t;
	}
	
	public final void nullOrdering(AST _t) throws RecognitionException {
		
		AST nullOrdering_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nullOrdering_AST = null;
		
		try {      // for error handling
			AST tmp18_AST = null;
			AST tmp18_AST_in = null;
			tmp18_AST = astFactory.create((AST)_t);
			tmp18_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp18_AST);
			match(_t,NULLS);
			_t = _t.getNextSibling();
			nullPrecedence(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			nullOrdering_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = nullOrdering_AST;
		_retTree = _t;
	}
	
	public final void nullPrecedence(AST _t) throws RecognitionException {
		
		AST nullPrecedence_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST nullPrecedence_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FIRST:
			{
				AST tmp19_AST = null;
				AST tmp19_AST_in = null;
				tmp19_AST = astFactory.create((AST)_t);
				tmp19_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp19_AST);
				match(_t,FIRST);
				_t = _t.getNextSibling();
				nullPrecedence_AST = (AST)currentAST.root;
				break;
			}
			case LAST:
			{
				AST tmp20_AST = null;
				AST tmp20_AST_in = null;
				tmp20_AST = astFactory.create((AST)_t);
				tmp20_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp20_AST);
				match(_t,LAST);
				_t = _t.getNextSibling();
				nullPrecedence_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = nullPrecedence_AST;
		_retTree = _t;
	}
	
	public final void resultVariableRef(AST _t) throws RecognitionException {
		
		AST resultVariableRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resultVariableRef_AST = null;
		AST i_AST = null;
		AST i = null;
		
		try {      // for error handling
			i = _t==ASTNULL ? null : (AST)_t;
			identifier(_t);
			_t = _retTree;
			i_AST = (AST)returnAST;
			resultVariableRef_AST = (AST)currentAST.root;
			
					// Create a RESULT_VARIABLE_REF node instead of an IDENT node.
					resultVariableRef_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(RESULT_VARIABLE_REF,i.getText())));
					handleResultVariableRef(resultVariableRef_AST);
				
			currentAST.root = resultVariableRef_AST;
			currentAST.child = resultVariableRef_AST!=null &&resultVariableRef_AST.getFirstChild()!=null ?
				resultVariableRef_AST.getFirstChild() : resultVariableRef_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = resultVariableRef_AST;
		_retTree = _t;
	}
	
	public final void identifier(AST _t) throws RecognitionException {
		
		AST identifier_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case IDENT:
			{
				AST tmp21_AST = null;
				AST tmp21_AST_in = null;
				tmp21_AST = astFactory.create((AST)_t);
				tmp21_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp21_AST);
				match(_t,IDENT);
				_t = _t.getNextSibling();
				break;
			}
			case WEIRD_IDENT:
			{
				AST tmp22_AST = null;
				AST tmp22_AST_in = null;
				tmp22_AST = astFactory.create((AST)_t);
				tmp22_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp22_AST);
				match(_t,WEIRD_IDENT);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			identifier_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = identifier_AST;
		_retTree = _t;
	}
	
	public final void logicalExpr(AST _t) throws RecognitionException {
		
		AST logicalExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalExpr_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			{
				AST __t456 = _t;
				AST tmp23_AST = null;
				AST tmp23_AST_in = null;
				tmp23_AST = astFactory.create((AST)_t);
				tmp23_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp23_AST);
				ASTPair __currentAST456 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,AND);
				_t = _t.getFirstChild();
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST456;
				_t = __t456;
				_t = _t.getNextSibling();
				logicalExpr_AST = (AST)currentAST.root;
				break;
			}
			case OR:
			{
				AST __t457 = _t;
				AST tmp24_AST = null;
				AST tmp24_AST_in = null;
				tmp24_AST = astFactory.create((AST)_t);
				tmp24_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp24_AST);
				ASTPair __currentAST457 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,OR);
				_t = _t.getFirstChild();
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST457;
				_t = __t457;
				_t = _t.getNextSibling();
				logicalExpr_AST = (AST)currentAST.root;
				break;
			}
			case NOT:
			{
				AST __t458 = _t;
				AST tmp25_AST = null;
				AST tmp25_AST_in = null;
				tmp25_AST = astFactory.create((AST)_t);
				tmp25_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp25_AST);
				ASTPair __currentAST458 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NOT);
				_t = _t.getFirstChild();
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST458;
				_t = __t458;
				_t = _t.getNextSibling();
				logicalExpr_AST = (AST)currentAST.root;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				comparisonExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				logicalExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = logicalExpr_AST;
		_retTree = _t;
	}
	
	public final void selectExprList(AST _t) throws RecognitionException {
		
		AST selectExprList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectExprList_AST = null;
		
				boolean oldInSelect = inSelect;
				inSelect = true;
			
		
		try {      // for error handling
			{
			int _cnt413=0;
			_loop413:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ALL:
				case AND:
				case BETWEEN:
				case COUNT:
				case DOT:
				case ELEMENTS:
				case EXISTS:
				case IN:
				case INDICES:
				case LIKE:
				case NOT:
				case OR:
				case CASE:
				case OBJECT:
				case KEY:
				case VALUE:
				case ENTRY:
				case AGGREGATE:
				case CONSTRUCTOR:
				case CASE2:
				case CAST:
				case IS_NOT_NULL:
				case IS_NULL:
				case METHOD_CALL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case QUERY:
				case UNARY_MINUS:
				case WEIRD_IDENT:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_BIG_INTEGER:
				case NUM_BIG_DECIMAL:
				case EQ:
				case IDENT:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case MOD:
				case QUOTED_STRING:
				case COLON:
				case PARAM:
				case NUM_INT:
				{
					selectExpr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case AS:
				{
					aliasedSelectExpr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					if ( _cnt413>=1 ) { break _loop413; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt413++;
			} while (true);
			}
			
					inSelect = oldInSelect;
				
			selectExprList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = selectExprList_AST;
		_retTree = _t;
	}
	
	public final void selectExpr(AST _t) throws RecognitionException {
		
		AST selectExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectExpr_AST = null;
		AST p_AST = null;
		AST p = null;
		AST ar2_AST = null;
		AST ar2 = null;
		AST ar3_AST = null;
		AST ar3 = null;
		AST con_AST = null;
		AST con = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			case KEY:
			case VALUE:
			case ENTRY:
			case WEIRD_IDENT:
			case IDENT:
			{
				p = _t==ASTNULL ? null : (AST)_t;
				propertyRef(_t);
				_t = _retTree;
				p_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				resolveSelectExpression(p_AST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case ALL:
			{
				AST __t417 = _t;
				AST tmp26_AST = null;
				AST tmp26_AST_in = null;
				tmp26_AST = astFactory.create((AST)_t);
				tmp26_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp26_AST);
				ASTPair __currentAST417 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,ALL);
				_t = _t.getFirstChild();
				ar2 = _t==ASTNULL ? null : (AST)_t;
				aliasRef(_t);
				_t = _retTree;
				ar2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST417;
				_t = __t417;
				_t = _t.getNextSibling();
				selectExpr_AST = (AST)currentAST.root;
				resolveSelectExpression(ar2_AST); selectExpr_AST = ar2_AST;
				currentAST.root = selectExpr_AST;
				currentAST.child = selectExpr_AST!=null &&selectExpr_AST.getFirstChild()!=null ?
					selectExpr_AST.getFirstChild() : selectExpr_AST;
				currentAST.advanceChildToEnd();
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case OBJECT:
			{
				AST __t418 = _t;
				AST tmp27_AST = null;
				AST tmp27_AST_in = null;
				tmp27_AST = astFactory.create((AST)_t);
				tmp27_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp27_AST);
				ASTPair __currentAST418 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,OBJECT);
				_t = _t.getFirstChild();
				ar3 = _t==ASTNULL ? null : (AST)_t;
				aliasRef(_t);
				_t = _retTree;
				ar3_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST418;
				_t = __t418;
				_t = _t.getNextSibling();
				selectExpr_AST = (AST)currentAST.root;
				resolveSelectExpression(ar3_AST); selectExpr_AST = ar3_AST;
				currentAST.root = selectExpr_AST;
				currentAST.child = selectExpr_AST!=null &&selectExpr_AST.getFirstChild()!=null ?
					selectExpr_AST.getFirstChild() : selectExpr_AST;
				currentAST.advanceChildToEnd();
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case CONSTRUCTOR:
			{
				con = _t==ASTNULL ? null : (AST)_t;
				constructor(_t);
				_t = _retTree;
				con_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				processConstructor(con_AST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case AGGREGATE:
			case CAST:
			case METHOD_CALL:
			{
				functionCall(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionFunction(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case QUOTED_STRING:
			case NUM_INT:
			{
				literal(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case AND:
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				logicalExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case COLON:
			case PARAM:
			{
				parameter(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			case QUERY:
			{
				query(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				selectExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = selectExpr_AST;
		_retTree = _t;
	}
	
	public final void aliasedSelectExpr(AST _t) throws RecognitionException {
		
		AST aliasedSelectExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aliasedSelectExpr_AST = null;
		AST se_AST = null;
		AST se = null;
		AST i_AST = null;
		AST i = null;
		
		try {      // for error handling
			AST __t415 = _t;
			AST tmp28_AST = null;
			AST tmp28_AST_in = null;
			tmp28_AST = astFactory.create((AST)_t);
			tmp28_AST_in = (AST)_t;
			ASTPair __currentAST415 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,AS);
			_t = _t.getFirstChild();
			se = _t==ASTNULL ? null : (AST)_t;
			selectExpr(_t);
			_t = _retTree;
			se_AST = (AST)returnAST;
			i = _t==ASTNULL ? null : (AST)_t;
			identifier(_t);
			_t = _retTree;
			i_AST = (AST)returnAST;
			currentAST = __currentAST415;
			_t = __t415;
			_t = _t.getNextSibling();
			aliasedSelectExpr_AST = (AST)currentAST.root;
			
					setAlias(se_AST,i_AST);
					aliasedSelectExpr_AST = se_AST;
				
			currentAST.root = aliasedSelectExpr_AST;
			currentAST.child = aliasedSelectExpr_AST!=null &&aliasedSelectExpr_AST.getFirstChild()!=null ?
				aliasedSelectExpr_AST.getFirstChild() : aliasedSelectExpr_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = aliasedSelectExpr_AST;
		_retTree = _t;
	}
	
	public final void aliasRef(AST _t) throws RecognitionException {
		
		AST aliasRef_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aliasRef_AST = null;
		AST i_AST = null;
		AST i = null;
		
		try {      // for error handling
			i = _t==ASTNULL ? null : (AST)_t;
			identifier(_t);
			_t = _retTree;
			i_AST = (AST)returnAST;
			aliasRef_AST = (AST)currentAST.root;
			
					aliasRef_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(ALIAS_REF,i.getText())));	// Create an ALIAS_REF node instead of an IDENT node.
					lookupAlias(aliasRef_AST);
					
			currentAST.root = aliasRef_AST;
			currentAST.child = aliasRef_AST!=null &&aliasRef_AST.getFirstChild()!=null ?
				aliasRef_AST.getFirstChild() : aliasRef_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = aliasRef_AST;
		_retTree = _t;
	}
	
	public final void constructor(AST _t) throws RecognitionException {
		
		AST constructor_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constructor_AST = null;
		String className = null;
		
		try {      // for error handling
			AST __t424 = _t;
			AST tmp29_AST = null;
			AST tmp29_AST_in = null;
			tmp29_AST = astFactory.create((AST)_t);
			tmp29_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp29_AST);
			ASTPair __currentAST424 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,CONSTRUCTOR);
			_t = _t.getFirstChild();
			className=path(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop426:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ALL:
				case AND:
				case BETWEEN:
				case COUNT:
				case DOT:
				case ELEMENTS:
				case EXISTS:
				case IN:
				case INDICES:
				case LIKE:
				case NOT:
				case OR:
				case CASE:
				case OBJECT:
				case KEY:
				case VALUE:
				case ENTRY:
				case AGGREGATE:
				case CONSTRUCTOR:
				case CASE2:
				case CAST:
				case IS_NOT_NULL:
				case IS_NULL:
				case METHOD_CALL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case QUERY:
				case UNARY_MINUS:
				case WEIRD_IDENT:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_BIG_INTEGER:
				case NUM_BIG_DECIMAL:
				case EQ:
				case IDENT:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case MOD:
				case QUOTED_STRING:
				case COLON:
				case PARAM:
				case NUM_INT:
				{
					selectExpr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case AS:
				{
					aliasedSelectExpr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					break _loop426;
				}
				}
			} while (true);
			}
			currentAST = __currentAST424;
			_t = __t424;
			_t = _t.getNextSibling();
			constructor_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = constructor_AST;
		_retTree = _t;
	}
	
	public final void functionCall(AST _t) throws RecognitionException {
		
		AST functionCall_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST functionCall_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case METHOD_CALL:
			{
				AST __t525 = _t;
				AST tmp30_AST = null;
				AST tmp30_AST_in = null;
				tmp30_AST = astFactory.create((AST)_t);
				tmp30_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp30_AST);
				ASTPair __currentAST525 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,METHOD_CALL);
				_t = _t.getFirstChild();
				inFunctionCall=true;
				pathAsIdent(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case EXPR_LIST:
				{
					AST __t527 = _t;
					AST tmp31_AST = null;
					AST tmp31_AST_in = null;
					tmp31_AST = astFactory.create((AST)_t);
					tmp31_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp31_AST);
					ASTPair __currentAST527 = currentAST.copy();
					currentAST.root = currentAST.child;
					currentAST.child = null;
					match(_t,EXPR_LIST);
					_t = _t.getFirstChild();
					{
					_loop529:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_tokenSet_1.member(_t.getType()))) {
							exprOrSubquery(_t);
							_t = _retTree;
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop529;
						}
						
					} while (true);
					}
					currentAST = __currentAST527;
					_t = __t527;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST525;
				_t = __t525;
				_t = _t.getNextSibling();
				functionCall_AST = (AST)currentAST.root;
				
				processFunction( functionCall_AST, inSelect );
				inFunctionCall=false;
				
				functionCall_AST = (AST)currentAST.root;
				break;
			}
			case CAST:
			{
				AST __t530 = _t;
				AST tmp32_AST = null;
				AST tmp32_AST_in = null;
				tmp32_AST = astFactory.create((AST)_t);
				tmp32_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp32_AST);
				ASTPair __currentAST530 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,CAST);
				_t = _t.getFirstChild();
				inFunctionCall=true;
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				pathAsIdent(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST530;
				_t = __t530;
				_t = _t.getNextSibling();
				functionCall_AST = (AST)currentAST.root;
				
					processCastFunction( functionCall_AST, inSelect );
				inFunctionCall=false;
				
				functionCall_AST = (AST)currentAST.root;
				break;
			}
			case AGGREGATE:
			{
				AST __t531 = _t;
				AST tmp33_AST = null;
				AST tmp33_AST_in = null;
				tmp33_AST = astFactory.create((AST)_t);
				tmp33_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp33_AST);
				ASTPair __currentAST531 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,AGGREGATE);
				_t = _t.getFirstChild();
				aggregateExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST531;
				_t = __t531;
				_t = _t.getNextSibling();
				functionCall_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = functionCall_AST;
		_retTree = _t;
	}
	
	public final void count(AST _t) throws RecognitionException {
		
		AST count_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST count_AST = null;
		
		try {      // for error handling
			AST __t420 = _t;
			AST tmp34_AST = null;
			AST tmp34_AST_in = null;
			tmp34_AST = astFactory.create((AST)_t);
			tmp34_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp34_AST);
			ASTPair __currentAST420 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,COUNT);
			_t = _t.getFirstChild();
			inCount = true;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DISTINCT:
			{
				AST tmp35_AST = null;
				AST tmp35_AST_in = null;
				tmp35_AST = astFactory.create((AST)_t);
				tmp35_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp35_AST);
				match(_t,DISTINCT);
				_t = _t.getNextSibling();
				inCountDistinct = true;
				break;
			}
			case ALL:
			{
				AST tmp36_AST = null;
				AST tmp36_AST_in = null;
				tmp36_AST = astFactory.create((AST)_t);
				tmp36_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp36_AST);
				match(_t,ALL);
				_t = _t.getNextSibling();
				break;
			}
			case COUNT:
			case DOT:
			case ELEMENTS:
			case FALSE:
			case INDICES:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case ROW_STAR:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case ELEMENTS:
			case FALSE:
			case INDICES:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				aggregateExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ROW_STAR:
			{
				AST tmp37_AST = null;
				AST tmp37_AST_in = null;
				tmp37_AST = astFactory.create((AST)_t);
				tmp37_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp37_AST);
				match(_t,ROW_STAR);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST420;
			_t = __t420;
			_t = _t.getNextSibling();
			
			inCount = false;
			inCountDistinct = false;
			
			count_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = count_AST;
		_retTree = _t;
	}
	
	public final void collectionFunction(AST _t) throws RecognitionException {
		
		AST collectionFunction_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST collectionFunction_AST = null;
		AST e = null;
		AST e_AST = null;
		AST p1_AST = null;
		AST p1 = null;
		AST i = null;
		AST i_AST = null;
		AST p2_AST = null;
		AST p2 = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELEMENTS:
			{
				AST __t522 = _t;
				e = _t==ASTNULL ? null :(AST)_t;
				AST e_AST_in = null;
				e_AST = astFactory.create(e);
				astFactory.addASTChild(currentAST, e_AST);
				ASTPair __currentAST522 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,ELEMENTS);
				_t = _t.getFirstChild();
				inFunctionCall=true;
				p1 = _t==ASTNULL ? null : (AST)_t;
				propertyRef(_t);
				_t = _retTree;
				p1_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				resolve(p1_AST);
				currentAST = __currentAST522;
				_t = __t522;
				_t = _t.getNextSibling();
				processFunction(e_AST,inSelect);
				inFunctionCall=false;
				collectionFunction_AST = (AST)currentAST.root;
				break;
			}
			case INDICES:
			{
				AST __t523 = _t;
				i = _t==ASTNULL ? null :(AST)_t;
				AST i_AST_in = null;
				i_AST = astFactory.create(i);
				astFactory.addASTChild(currentAST, i_AST);
				ASTPair __currentAST523 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,INDICES);
				_t = _t.getFirstChild();
				inFunctionCall=true;
				p2 = _t==ASTNULL ? null : (AST)_t;
				propertyRef(_t);
				_t = _retTree;
				p2_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				resolve(p2_AST);
				currentAST = __currentAST523;
				_t = __t523;
				_t = _t.getNextSibling();
				processFunction(i_AST,inSelect);
				inFunctionCall=false;
				collectionFunction_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = collectionFunction_AST;
		_retTree = _t;
	}
	
	public final void literal(AST _t) throws RecognitionException {
		
		AST literal_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST literal_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NUM_INT:
			{
				AST tmp38_AST = null;
				AST tmp38_AST_in = null;
				tmp38_AST = astFactory.create((AST)_t);
				tmp38_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp38_AST);
				match(_t,NUM_INT);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case NUM_LONG:
			{
				AST tmp39_AST = null;
				AST tmp39_AST_in = null;
				tmp39_AST = astFactory.create((AST)_t);
				tmp39_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp39_AST);
				match(_t,NUM_LONG);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				AST tmp40_AST = null;
				AST tmp40_AST_in = null;
				tmp40_AST = astFactory.create((AST)_t);
				tmp40_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp40_AST);
				match(_t,NUM_FLOAT);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			{
				AST tmp41_AST = null;
				AST tmp41_AST_in = null;
				tmp41_AST = astFactory.create((AST)_t);
				tmp41_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp41_AST);
				match(_t,NUM_DOUBLE);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case NUM_BIG_INTEGER:
			{
				AST tmp42_AST = null;
				AST tmp42_AST_in = null;
				tmp42_AST = astFactory.create((AST)_t);
				tmp42_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(_t,NUM_BIG_INTEGER);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case NUM_BIG_DECIMAL:
			{
				AST tmp43_AST = null;
				AST tmp43_AST_in = null;
				tmp43_AST = astFactory.create((AST)_t);
				tmp43_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp43_AST);
				match(_t,NUM_BIG_DECIMAL);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				processNumericLiteral( literal_AST );
				literal_AST = (AST)currentAST.root;
				break;
			}
			case QUOTED_STRING:
			{
				AST tmp44_AST = null;
				AST tmp44_AST_in = null;
				tmp44_AST = astFactory.create((AST)_t);
				tmp44_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(_t,QUOTED_STRING);
				_t = _t.getNextSibling();
				literal_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = literal_AST;
		_retTree = _t;
	}
	
	public final void arithmeticExpr(AST _t) throws RecognitionException {
		
		AST arithmeticExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST arithmeticExpr_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			{
				AST __t497 = _t;
				AST tmp45_AST = null;
				AST tmp45_AST_in = null;
				tmp45_AST = astFactory.create((AST)_t);
				tmp45_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp45_AST);
				ASTPair __currentAST497 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,PLUS);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST497;
				_t = __t497;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case MINUS:
			{
				AST __t498 = _t;
				AST tmp46_AST = null;
				AST tmp46_AST_in = null;
				tmp46_AST = astFactory.create((AST)_t);
				tmp46_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp46_AST);
				ASTPair __currentAST498 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,MINUS);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST498;
				_t = __t498;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case DIV:
			{
				AST __t499 = _t;
				AST tmp47_AST = null;
				AST tmp47_AST_in = null;
				tmp47_AST = astFactory.create((AST)_t);
				tmp47_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp47_AST);
				ASTPair __currentAST499 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DIV);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST499;
				_t = __t499;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case MOD:
			{
				AST __t500 = _t;
				AST tmp48_AST = null;
				AST tmp48_AST_in = null;
				tmp48_AST = astFactory.create((AST)_t);
				tmp48_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp48_AST);
				ASTPair __currentAST500 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,MOD);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST500;
				_t = __t500;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case STAR:
			{
				AST __t501 = _t;
				AST tmp49_AST = null;
				AST tmp49_AST_in = null;
				tmp49_AST = astFactory.create((AST)_t);
				tmp49_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp49_AST);
				ASTPair __currentAST501 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,STAR);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST501;
				_t = __t501;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case UNARY_MINUS:
			{
				AST __t502 = _t;
				AST tmp50_AST = null;
				AST tmp50_AST_in = null;
				tmp50_AST = astFactory.create((AST)_t);
				tmp50_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp50_AST);
				ASTPair __currentAST502 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,UNARY_MINUS);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST502;
				_t = __t502;
				_t = _t.getNextSibling();
				arithmeticExpr_AST = (AST)currentAST.root;
				prepareArithmeticOperator( arithmeticExpr_AST );
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			case CASE2:
			{
				caseExpr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				arithmeticExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = arithmeticExpr_AST;
		_retTree = _t;
	}
	
	public final void parameter(AST _t) throws RecognitionException {
		
		AST parameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST parameter_AST = null;
		AST c = null;
		AST c_AST = null;
		AST a_AST = null;
		AST a = null;
		AST p = null;
		AST p_AST = null;
		AST n = null;
		AST n_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COLON:
			{
				AST __t551 = _t;
				c = _t==ASTNULL ? null :(AST)_t;
				AST c_AST_in = null;
				c_AST = astFactory.create(c);
				ASTPair __currentAST551 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,COLON);
				_t = _t.getFirstChild();
				a = _t==ASTNULL ? null : (AST)_t;
				identifier(_t);
				_t = _retTree;
				a_AST = (AST)returnAST;
				currentAST = __currentAST551;
				_t = __t551;
				_t = _t.getNextSibling();
				parameter_AST = (AST)currentAST.root;
				
							// Create a NAMED_PARAM node instead of (COLON IDENT).
							parameter_AST = generateNamedParameter( c, a );
				//			#parameter = #([NAMED_PARAM,a.getText()]);
				//			namedParameter(#parameter);
						
				currentAST.root = parameter_AST;
				currentAST.child = parameter_AST!=null &&parameter_AST.getFirstChild()!=null ?
					parameter_AST.getFirstChild() : parameter_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case PARAM:
			{
				AST __t552 = _t;
				p = _t==ASTNULL ? null :(AST)_t;
				AST p_AST_in = null;
				p_AST = astFactory.create(p);
				ASTPair __currentAST552 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,PARAM);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NUM_INT:
				{
					n = (AST)_t;
					AST n_AST_in = null;
					n_AST = astFactory.create(n);
					match(_t,NUM_INT);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST552;
				_t = __t552;
				_t = _t.getNextSibling();
				parameter_AST = (AST)currentAST.root;
				
							if ( n != null ) {
								// An ejb3-style "positional parameter", which we handle internally as a named-param
								parameter_AST = generateNamedParameter( p, n );
				//				#parameter = #([NAMED_PARAM,n.getText()]);
				//				namedParameter(#parameter);
							}
							else {
								parameter_AST = generatePositionalParameter( p );
				//				#parameter = #([PARAM,"?"]);
				//				positionalParameter(#parameter);
							}
						
				currentAST.root = parameter_AST;
				currentAST.child = parameter_AST!=null &&parameter_AST.getFirstChild()!=null ?
					parameter_AST.getFirstChild() : parameter_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = parameter_AST;
		_retTree = _t;
	}
	
	public final void aggregateExpr(AST _t) throws RecognitionException {
		
		AST aggregateExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregateExpr_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				aggregateExpr_AST = (AST)currentAST.root;
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionFunction(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				aggregateExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = aggregateExpr_AST;
		_retTree = _t;
	}
	
	public final void fromElementList(AST _t) throws RecognitionException {
		
		AST fromElementList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromElementList_AST = null;
		
				boolean oldInFrom = inFrom;
				inFrom = true;
				
		
		try {      // for error handling
			{
			int _cnt432=0;
			_loop432:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==JOIN||_t.getType()==FILTER_ENTITY||_t.getType()==RANGE)) {
					fromElement(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt432>=1 ) { break _loop432; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt432++;
			} while (true);
			}
			
					inFrom = oldInFrom;
					
			fromElementList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = fromElementList_AST;
		_retTree = _t;
	}
	
	public final void fromElement(AST _t) throws RecognitionException {
		
		AST fromElement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromElement_AST = null;
		AST a = null;
		AST a_AST = null;
		AST pf = null;
		AST pf_AST = null;
		AST je_AST = null;
		AST je = null;
		AST fe = null;
		AST fe_AST = null;
		AST a3 = null;
		AST a3_AST = null;
		
			String p = null;
			
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case RANGE:
			{
				AST __t434 = _t;
				AST tmp51_AST = null;
				AST tmp51_AST_in = null;
				tmp51_AST = astFactory.create((AST)_t);
				tmp51_AST_in = (AST)_t;
				ASTPair __currentAST434 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,RANGE);
				_t = _t.getFirstChild();
				p=path(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ALIAS:
				{
					a = (AST)_t;
					AST a_AST_in = null;
					a_AST = astFactory.create(a);
					match(_t,ALIAS);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				case FETCH:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case FETCH:
				{
					pf = (AST)_t;
					AST pf_AST_in = null;
					pf_AST = astFactory.create(pf);
					match(_t,FETCH);
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST434;
				_t = __t434;
				_t = _t.getNextSibling();
				fromElement_AST = (AST)currentAST.root;
				
						fromElement_AST = createFromElement(p,a, pf);
					
				currentAST.root = fromElement_AST;
				currentAST.child = fromElement_AST!=null &&fromElement_AST.getFirstChild()!=null ?
					fromElement_AST.getFirstChild() : fromElement_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case JOIN:
			{
				je = _t==ASTNULL ? null : (AST)_t;
				joinElement(_t);
				_t = _retTree;
				je_AST = (AST)returnAST;
				fromElement_AST = (AST)currentAST.root;
				
						fromElement_AST = je_AST;
					
				currentAST.root = fromElement_AST;
				currentAST.child = fromElement_AST!=null &&fromElement_AST.getFirstChild()!=null ?
					fromElement_AST.getFirstChild() : fromElement_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case FILTER_ENTITY:
			{
				fe = (AST)_t;
				AST fe_AST_in = null;
				fe_AST = astFactory.create(fe);
				match(_t,FILTER_ENTITY);
				_t = _t.getNextSibling();
				a3 = (AST)_t;
				AST a3_AST_in = null;
				a3_AST = astFactory.create(a3);
				match(_t,ALIAS);
				_t = _t.getNextSibling();
				fromElement_AST = (AST)currentAST.root;
				
						fromElement_AST = createFromFilterElement(fe,a3);
					
				currentAST.root = fromElement_AST;
				currentAST.child = fromElement_AST!=null &&fromElement_AST.getFirstChild()!=null ?
					fromElement_AST.getFirstChild() : fromElement_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = fromElement_AST;
		_retTree = _t;
	}
	
	public final void joinElement(AST _t) throws RecognitionException {
		
		AST joinElement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST joinElement_AST = null;
		AST f = null;
		AST f_AST = null;
		AST ref_AST = null;
		AST ref = null;
		AST a = null;
		AST a_AST = null;
		AST pf = null;
		AST pf_AST = null;
		AST with = null;
		AST with_AST = null;
		
				int j = INNER;
			
		
		try {      // for error handling
			AST __t438 = _t;
			AST tmp52_AST = null;
			AST tmp52_AST_in = null;
			tmp52_AST = astFactory.create((AST)_t);
			tmp52_AST_in = (AST)_t;
			ASTPair __currentAST438 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,JOIN);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FULL:
			case INNER:
			case LEFT:
			case RIGHT:
			{
				j=joinType(_t);
				_t = _retTree;
				setImpliedJoinType(j);
				break;
			}
			case DOT:
			case FETCH:
			case KEY:
			case VALUE:
			case ENTRY:
			case WEIRD_IDENT:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FETCH:
			{
				f = (AST)_t;
				AST f_AST_in = null;
				f_AST = astFactory.create(f);
				match(_t,FETCH);
				_t = _t.getNextSibling();
				break;
			}
			case DOT:
			case KEY:
			case VALUE:
			case ENTRY:
			case WEIRD_IDENT:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			ref = _t==ASTNULL ? null : (AST)_t;
			propertyRef(_t);
			_t = _retTree;
			ref_AST = (AST)returnAST;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALIAS:
			{
				a = (AST)_t;
				AST a_AST_in = null;
				a_AST = astFactory.create(a);
				match(_t,ALIAS);
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case FETCH:
			case WITH:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FETCH:
			{
				pf = (AST)_t;
				AST pf_AST_in = null;
				pf_AST = astFactory.create(pf);
				match(_t,FETCH);
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case WITH:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WITH:
			{
				with = (AST)_t;
				AST with_AST_in = null;
				with_AST = astFactory.create(with);
				match(_t,WITH);
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST438;
			_t = __t438;
			_t = _t.getNextSibling();
			
					//createFromJoinElement(#ref,a,j,f, pf);
					createFromJoinElement(ref_AST,a,j,f, pf, with);
					setImpliedJoinType(INNER);	// Reset the implied join type.
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = joinElement_AST;
		_retTree = _t;
	}
	
	public final int  joinType(AST _t) throws RecognitionException {
		int j;
		
		AST joinType_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST joinType_AST = null;
		AST left = null;
		AST left_AST = null;
		AST right = null;
		AST right_AST = null;
		AST outer = null;
		AST outer_AST = null;
		
			j = INNER;
			
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LEFT:
			case RIGHT:
			{
				{
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LEFT:
				{
					left = (AST)_t;
					AST left_AST_in = null;
					left_AST = astFactory.create(left);
					astFactory.addASTChild(currentAST, left_AST);
					match(_t,LEFT);
					_t = _t.getNextSibling();
					break;
				}
				case RIGHT:
				{
					right = (AST)_t;
					AST right_AST_in = null;
					right_AST = astFactory.create(right);
					astFactory.addASTChild(currentAST, right_AST);
					match(_t,RIGHT);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case OUTER:
				{
					outer = (AST)_t;
					AST outer_AST_in = null;
					outer_AST = astFactory.create(outer);
					astFactory.addASTChild(currentAST, outer_AST);
					match(_t,OUTER);
					_t = _t.getNextSibling();
					break;
				}
				case DOT:
				case FETCH:
				case KEY:
				case VALUE:
				case ENTRY:
				case WEIRD_IDENT:
				case IDENT:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				}
				
						if (left != null)       j = LEFT_OUTER;
						else if (right != null) j = RIGHT_OUTER;
						else if (outer != null) j = RIGHT_OUTER;
					
				joinType_AST = (AST)currentAST.root;
				break;
			}
			case FULL:
			{
				AST tmp53_AST = null;
				AST tmp53_AST_in = null;
				tmp53_AST = astFactory.create((AST)_t);
				tmp53_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp53_AST);
				match(_t,FULL);
				_t = _t.getNextSibling();
				
						j = FULL;
					
				joinType_AST = (AST)currentAST.root;
				break;
			}
			case INNER:
			{
				AST tmp54_AST = null;
				AST tmp54_AST_in = null;
				tmp54_AST = astFactory.create((AST)_t);
				tmp54_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp54_AST);
				match(_t,INNER);
				_t = _t.getNextSibling();
				
						j = INNER;
					
				joinType_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = joinType_AST;
		_retTree = _t;
		return j;
	}
	
	public final void pathAsIdent(AST _t) throws RecognitionException {
		
		AST pathAsIdent_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pathAsIdent_AST = null;
		
		String text = "?text?";
		
		
		try {      // for error handling
			text=path(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			pathAsIdent_AST = (AST)currentAST.root;
			
			pathAsIdent_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(IDENT,text)));
			
			currentAST.root = pathAsIdent_AST;
			currentAST.child = pathAsIdent_AST!=null &&pathAsIdent_AST.getFirstChild()!=null ?
				pathAsIdent_AST.getFirstChild() : pathAsIdent_AST;
			currentAST.advanceChildToEnd();
			pathAsIdent_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = pathAsIdent_AST;
		_retTree = _t;
	}
	
	public final void withClause(AST _t) throws RecognitionException {
		
		AST withClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST withClause_AST = null;
		AST w = null;
		AST w_AST = null;
		AST b_AST = null;
		AST b = null;
		
		try {      // for error handling
			AST __t452 = _t;
			w = _t==ASTNULL ? null :(AST)_t;
			AST w_AST_in = null;
			w_AST = astFactory.create(w);
			astFactory.addASTChild(currentAST, w_AST);
			ASTPair __currentAST452 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,WITH);
			_t = _t.getFirstChild();
			handleClauseStart( WITH );
			b = _t==ASTNULL ? null : (AST)_t;
			logicalExpr(_t);
			_t = _retTree;
			b_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST452;
			_t = __t452;
			_t = _t.getNextSibling();
			withClause_AST = (AST)currentAST.root;
			
					withClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(w_AST).add(b_AST));
					handleClauseEnd();
				
			currentAST.root = withClause_AST;
			currentAST.child = withClause_AST!=null &&withClause_AST.getFirstChild()!=null ?
				withClause_AST.getFirstChild() : withClause_AST;
			currentAST.advanceChildToEnd();
			withClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = withClause_AST;
		_retTree = _t;
	}
	
	public final void comparisonExpr(AST _t) throws RecognitionException {
		
		AST comparisonExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST comparisonExpr_AST = null;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQ:
			{
				AST __t461 = _t;
				AST tmp55_AST = null;
				AST tmp55_AST_in = null;
				tmp55_AST = astFactory.create((AST)_t);
				tmp55_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp55_AST);
				ASTPair __currentAST461 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,EQ);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST461;
				_t = __t461;
				_t = _t.getNextSibling();
				break;
			}
			case NE:
			{
				AST __t462 = _t;
				AST tmp56_AST = null;
				AST tmp56_AST_in = null;
				tmp56_AST = astFactory.create((AST)_t);
				tmp56_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp56_AST);
				ASTPair __currentAST462 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NE);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST462;
				_t = __t462;
				_t = _t.getNextSibling();
				break;
			}
			case LT:
			{
				AST __t463 = _t;
				AST tmp57_AST = null;
				AST tmp57_AST_in = null;
				tmp57_AST = astFactory.create((AST)_t);
				tmp57_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp57_AST);
				ASTPair __currentAST463 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LT);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST463;
				_t = __t463;
				_t = _t.getNextSibling();
				break;
			}
			case GT:
			{
				AST __t464 = _t;
				AST tmp58_AST = null;
				AST tmp58_AST_in = null;
				tmp58_AST = astFactory.create((AST)_t);
				tmp58_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp58_AST);
				ASTPair __currentAST464 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,GT);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST464;
				_t = __t464;
				_t = _t.getNextSibling();
				break;
			}
			case LE:
			{
				AST __t465 = _t;
				AST tmp59_AST = null;
				AST tmp59_AST_in = null;
				tmp59_AST = astFactory.create((AST)_t);
				tmp59_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp59_AST);
				ASTPair __currentAST465 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LE);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST465;
				_t = __t465;
				_t = _t.getNextSibling();
				break;
			}
			case GE:
			{
				AST __t466 = _t;
				AST tmp60_AST = null;
				AST tmp60_AST_in = null;
				tmp60_AST = astFactory.create((AST)_t);
				tmp60_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp60_AST);
				ASTPair __currentAST466 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,GE);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST466;
				_t = __t466;
				_t = _t.getNextSibling();
				break;
			}
			case LIKE:
			{
				AST __t467 = _t;
				AST tmp61_AST = null;
				AST tmp61_AST_in = null;
				tmp61_AST = astFactory.create((AST)_t);
				tmp61_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp61_AST);
				ASTPair __currentAST467 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,LIKE);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ESCAPE:
				{
					AST __t469 = _t;
					AST tmp62_AST = null;
					AST tmp62_AST_in = null;
					tmp62_AST = astFactory.create((AST)_t);
					tmp62_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp62_AST);
					ASTPair __currentAST469 = currentAST.copy();
					currentAST.root = currentAST.child;
					currentAST.child = null;
					match(_t,ESCAPE);
					_t = _t.getFirstChild();
					expr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					currentAST = __currentAST469;
					_t = __t469;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST467;
				_t = __t467;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_LIKE:
			{
				AST __t470 = _t;
				AST tmp63_AST = null;
				AST tmp63_AST_in = null;
				tmp63_AST = astFactory.create((AST)_t);
				tmp63_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp63_AST);
				ASTPair __currentAST470 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NOT_LIKE);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ESCAPE:
				{
					AST __t472 = _t;
					AST tmp64_AST = null;
					AST tmp64_AST_in = null;
					tmp64_AST = astFactory.create((AST)_t);
					tmp64_AST_in = (AST)_t;
					astFactory.addASTChild(currentAST, tmp64_AST);
					ASTPair __currentAST472 = currentAST.copy();
					currentAST.root = currentAST.child;
					currentAST.child = null;
					match(_t,ESCAPE);
					_t = _t.getFirstChild();
					expr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					currentAST = __currentAST472;
					_t = __t472;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST470;
				_t = __t470;
				_t = _t.getNextSibling();
				break;
			}
			case BETWEEN:
			{
				AST __t473 = _t;
				AST tmp65_AST = null;
				AST tmp65_AST_in = null;
				tmp65_AST = astFactory.create((AST)_t);
				tmp65_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp65_AST);
				ASTPair __currentAST473 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,BETWEEN);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST473;
				_t = __t473;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_BETWEEN:
			{
				AST __t474 = _t;
				AST tmp66_AST = null;
				AST tmp66_AST_in = null;
				tmp66_AST = astFactory.create((AST)_t);
				tmp66_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp66_AST);
				ASTPair __currentAST474 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NOT_BETWEEN);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST474;
				_t = __t474;
				_t = _t.getNextSibling();
				break;
			}
			case IN:
			{
				AST __t475 = _t;
				AST tmp67_AST = null;
				AST tmp67_AST_in = null;
				tmp67_AST = astFactory.create((AST)_t);
				tmp67_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp67_AST);
				ASTPair __currentAST475 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,IN);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				inRhs(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST475;
				_t = __t475;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_IN:
			{
				AST __t476 = _t;
				AST tmp68_AST = null;
				AST tmp68_AST_in = null;
				tmp68_AST = astFactory.create((AST)_t);
				tmp68_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp68_AST);
				ASTPair __currentAST476 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,NOT_IN);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				inRhs(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST476;
				_t = __t476;
				_t = _t.getNextSibling();
				break;
			}
			case IS_NULL:
			{
				AST __t477 = _t;
				AST tmp69_AST = null;
				AST tmp69_AST_in = null;
				tmp69_AST = astFactory.create((AST)_t);
				tmp69_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp69_AST);
				ASTPair __currentAST477 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,IS_NULL);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST477;
				_t = __t477;
				_t = _t.getNextSibling();
				break;
			}
			case IS_NOT_NULL:
			{
				AST __t478 = _t;
				AST tmp70_AST = null;
				AST tmp70_AST_in = null;
				tmp70_AST = astFactory.create((AST)_t);
				tmp70_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp70_AST);
				ASTPair __currentAST478 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,IS_NOT_NULL);
				_t = _t.getFirstChild();
				exprOrSubquery(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST478;
				_t = __t478;
				_t = _t.getNextSibling();
				break;
			}
			case EXISTS:
			{
				AST __t479 = _t;
				AST tmp71_AST = null;
				AST tmp71_AST_in = null;
				tmp71_AST = astFactory.create((AST)_t);
				tmp71_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp71_AST);
				ASTPair __currentAST479 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,EXISTS);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case COUNT:
				case DOT:
				case FALSE:
				case NULL:
				case TRUE:
				case CASE:
				case KEY:
				case VALUE:
				case ENTRY:
				case AGGREGATE:
				case CASE2:
				case CAST:
				case INDEX_OP:
				case METHOD_CALL:
				case UNARY_MINUS:
				case VECTOR_EXPR:
				case WEIRD_IDENT:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_BIG_INTEGER:
				case NUM_BIG_DECIMAL:
				case JAVA_CONSTANT:
				case IDENT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case MOD:
				case QUOTED_STRING:
				case COLON:
				case PARAM:
				case NUM_INT:
				{
					expr(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case ELEMENTS:
				case INDICES:
				case QUERY:
				{
					collectionFunctionOrSubselect(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				currentAST = __currentAST479;
				_t = __t479;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			comparisonExpr_AST = (AST)currentAST.root;
			
				    prepareLogicOperator( comparisonExpr_AST );
				
			comparisonExpr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = comparisonExpr_AST;
		_retTree = _t;
	}
	
	public final void exprOrSubquery(AST _t) throws RecognitionException {
		
		AST exprOrSubquery_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exprOrSubquery_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery_AST = (AST)currentAST.root;
				break;
			}
			case QUERY:
			{
				query(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				exprOrSubquery_AST = (AST)currentAST.root;
				break;
			}
			case ANY:
			{
				AST __t488 = _t;
				AST tmp72_AST = null;
				AST tmp72_AST_in = null;
				tmp72_AST = astFactory.create((AST)_t);
				tmp72_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp72_AST);
				ASTPair __currentAST488 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,ANY);
				_t = _t.getFirstChild();
				collectionFunctionOrSubselect(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST488;
				_t = __t488;
				_t = _t.getNextSibling();
				exprOrSubquery_AST = (AST)currentAST.root;
				break;
			}
			case ALL:
			{
				AST __t489 = _t;
				AST tmp73_AST = null;
				AST tmp73_AST_in = null;
				tmp73_AST = astFactory.create((AST)_t);
				tmp73_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp73_AST);
				ASTPair __currentAST489 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,ALL);
				_t = _t.getFirstChild();
				collectionFunctionOrSubselect(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST489;
				_t = __t489;
				_t = _t.getNextSibling();
				exprOrSubquery_AST = (AST)currentAST.root;
				break;
			}
			case SOME:
			{
				AST __t490 = _t;
				AST tmp74_AST = null;
				AST tmp74_AST_in = null;
				tmp74_AST = astFactory.create((AST)_t);
				tmp74_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp74_AST);
				ASTPair __currentAST490 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,SOME);
				_t = _t.getFirstChild();
				collectionFunctionOrSubselect(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST490;
				_t = __t490;
				_t = _t.getNextSibling();
				exprOrSubquery_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = exprOrSubquery_AST;
		_retTree = _t;
	}
	
	public final void inRhs(AST _t) throws RecognitionException {
		
		AST inRhs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inRhs_AST = null;
		
		try {      // for error handling
			AST __t482 = _t;
			AST tmp75_AST = null;
			AST tmp75_AST_in = null;
			tmp75_AST = astFactory.create((AST)_t);
			tmp75_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp75_AST);
			ASTPair __currentAST482 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,IN_LIST);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELEMENTS:
			case INDICES:
			case QUERY:
			{
				collectionFunctionOrSubselect(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				{
				{
				_loop486:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_0.member(_t.getType()))) {
						expr(_t);
						_t = _retTree;
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop486;
					}
					
				} while (true);
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST482;
			_t = __t482;
			_t = _t.getNextSibling();
			inRhs_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = inRhs_AST;
		_retTree = _t;
	}
	
	public final void collectionFunctionOrSubselect(AST _t) throws RecognitionException {
		
		AST collectionFunctionOrSubselect_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST collectionFunctionOrSubselect_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELEMENTS:
			case INDICES:
			{
				collectionFunction(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				collectionFunctionOrSubselect_AST = (AST)currentAST.root;
				break;
			}
			case QUERY:
			{
				query(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				collectionFunctionOrSubselect_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = collectionFunctionOrSubselect_AST;
		_retTree = _t;
	}
	
	public final void addrExpr(AST _t,
		 boolean root 
	) throws RecognitionException {
		
		AST addrExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST addrExpr_AST = null;
		AST d = null;
		AST d_AST = null;
		AST lhs_AST = null;
		AST lhs = null;
		AST rhs_AST = null;
		AST rhs = null;
		AST i = null;
		AST i_AST = null;
		AST lhs2_AST = null;
		AST lhs2 = null;
		AST rhs2_AST = null;
		AST rhs2 = null;
		AST mcr_AST = null;
		AST mcr = null;
		AST p_AST = null;
		AST p = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			{
				AST __t537 = _t;
				d = _t==ASTNULL ? null :(AST)_t;
				AST d_AST_in = null;
				d_AST = astFactory.create(d);
				ASTPair __currentAST537 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,DOT);
				_t = _t.getFirstChild();
				lhs = _t==ASTNULL ? null : (AST)_t;
				addrExprLhs(_t);
				_t = _retTree;
				lhs_AST = (AST)returnAST;
				rhs = _t==ASTNULL ? null : (AST)_t;
				propertyName(_t);
				_t = _retTree;
				rhs_AST = (AST)returnAST;
				currentAST = __currentAST537;
				_t = __t537;
				_t = _t.getNextSibling();
				addrExpr_AST = (AST)currentAST.root;
				
						// This gives lookupProperty() a chance to transform the tree 
						// to process collection properties (.elements, etc).
						addrExpr_AST = (AST)astFactory.make( (new ASTArray(3)).add(d_AST).add(lhs_AST).add(rhs_AST));
						addrExpr_AST = lookupProperty(addrExpr_AST,root,false);
					
				currentAST.root = addrExpr_AST;
				currentAST.child = addrExpr_AST!=null &&addrExpr_AST.getFirstChild()!=null ?
					addrExpr_AST.getFirstChild() : addrExpr_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case INDEX_OP:
			{
				AST __t538 = _t;
				i = _t==ASTNULL ? null :(AST)_t;
				AST i_AST_in = null;
				i_AST = astFactory.create(i);
				ASTPair __currentAST538 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,INDEX_OP);
				_t = _t.getFirstChild();
				lhs2 = _t==ASTNULL ? null : (AST)_t;
				addrExprLhs(_t);
				_t = _retTree;
				lhs2_AST = (AST)returnAST;
				rhs2 = _t==ASTNULL ? null : (AST)_t;
				expr(_t);
				_t = _retTree;
				rhs2_AST = (AST)returnAST;
				currentAST = __currentAST538;
				_t = __t538;
				_t = _t.getNextSibling();
				addrExpr_AST = (AST)currentAST.root;
				
						addrExpr_AST = (AST)astFactory.make( (new ASTArray(3)).add(i_AST).add(lhs2_AST).add(rhs2_AST));
						processIndex(addrExpr_AST);
					
				currentAST.root = addrExpr_AST;
				currentAST.child = addrExpr_AST!=null &&addrExpr_AST.getFirstChild()!=null ?
					addrExpr_AST.getFirstChild() : addrExpr_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case KEY:
			case VALUE:
			case ENTRY:
			{
				mcr = _t==ASTNULL ? null : (AST)_t;
				mapComponentReference(_t);
				_t = _retTree;
				mcr_AST = (AST)returnAST;
				addrExpr_AST = (AST)currentAST.root;
				
					    addrExpr_AST = mcr_AST;
					
				currentAST.root = addrExpr_AST;
				currentAST.child = addrExpr_AST!=null &&addrExpr_AST.getFirstChild()!=null ?
					addrExpr_AST.getFirstChild() : addrExpr_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case WEIRD_IDENT:
			case IDENT:
			{
				p = _t==ASTNULL ? null : (AST)_t;
				identifier(_t);
				_t = _retTree;
				p_AST = (AST)returnAST;
				addrExpr_AST = (AST)currentAST.root;
				
				//		#addrExpr = #p;
				//		resolve(#addrExpr);
						// In many cases, things other than property-refs are recognized
						// by this addrExpr rule.  Some of those I have seen:
						//  1) select-clause from-aliases
						//  2) sql-functions
						if ( isNonQualifiedPropertyRef(p_AST) ) {
							addrExpr_AST = lookupNonQualifiedProperty(p_AST);
						}
						else {
							resolve(p_AST);
							addrExpr_AST = p_AST;
						}
					
				currentAST.root = addrExpr_AST;
				currentAST.child = addrExpr_AST!=null &&addrExpr_AST.getFirstChild()!=null ?
					addrExpr_AST.getFirstChild() : addrExpr_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = addrExpr_AST;
		_retTree = _t;
	}
	
	public final void constant(AST _t) throws RecognitionException {
		
		AST constant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case QUOTED_STRING:
			case NUM_INT:
			{
				literal(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NULL:
			{
				AST tmp76_AST = null;
				AST tmp76_AST_in = null;
				tmp76_AST = astFactory.create((AST)_t);
				tmp76_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(_t,NULL);
				_t = _t.getNextSibling();
				constant_AST = (AST)currentAST.root;
				break;
			}
			case TRUE:
			{
				AST tmp77_AST = null;
				AST tmp77_AST_in = null;
				tmp77_AST = astFactory.create((AST)_t);
				tmp77_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp77_AST);
				match(_t,TRUE);
				_t = _t.getNextSibling();
				constant_AST = (AST)currentAST.root;
				processBoolean(constant_AST);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case FALSE:
			{
				AST tmp78_AST = null;
				AST tmp78_AST_in = null;
				tmp78_AST = astFactory.create((AST)_t);
				tmp78_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(_t,FALSE);
				_t = _t.getNextSibling();
				constant_AST = (AST)currentAST.root;
				processBoolean(constant_AST);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case JAVA_CONSTANT:
			{
				AST tmp79_AST = null;
				AST tmp79_AST_in = null;
				tmp79_AST = astFactory.create((AST)_t);
				tmp79_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp79_AST);
				match(_t,JAVA_CONSTANT);
				_t = _t.getNextSibling();
				constant_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = constant_AST;
		_retTree = _t;
	}
	
	public final void caseExpr(AST _t) throws RecognitionException {
		
		AST caseExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseExpr_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CASE2:
			{
				simpleCaseExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				caseExpr_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			{
				searchedCaseExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				caseExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = caseExpr_AST;
		_retTree = _t;
	}
	
	public final void simpleCaseExpression(AST _t) throws RecognitionException {
		
		AST simpleCaseExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleCaseExpression_AST = null;
		
		try {      // for error handling
			AST __t506 = _t;
			AST tmp80_AST = null;
			AST tmp80_AST_in = null;
			tmp80_AST = astFactory.create((AST)_t);
			tmp80_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp80_AST);
			ASTPair __currentAST506 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,CASE2);
			_t = _t.getFirstChild();
			inCase=true;
			expressionOrSubQuery(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			{
			int _cnt508=0;
			_loop508:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==WHEN)) {
					simpleCaseWhenClause(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt508>=1 ) { break _loop508; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt508++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELSE:
			{
				elseClause(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST506;
			_t = __t506;
			_t = _t.getNextSibling();
			inCase=false;
			simpleCaseExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = simpleCaseExpression_AST;
		_retTree = _t;
	}
	
	public final void searchedCaseExpression(AST _t) throws RecognitionException {
		
		AST searchedCaseExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST searchedCaseExpression_AST = null;
		
		try {      // for error handling
			AST __t515 = _t;
			AST tmp81_AST = null;
			AST tmp81_AST_in = null;
			tmp81_AST = astFactory.create((AST)_t);
			tmp81_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp81_AST);
			ASTPair __currentAST515 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,CASE);
			_t = _t.getFirstChild();
			inCase = true;
			{
			int _cnt517=0;
			_loop517:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==WHEN)) {
					searchedCaseWhenClause(_t);
					_t = _retTree;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt517>=1 ) { break _loop517; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt517++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ELSE:
			{
				elseClause(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			currentAST = __currentAST515;
			_t = __t515;
			_t = _t.getNextSibling();
			inCase = false;
			searchedCaseExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = searchedCaseExpression_AST;
		_retTree = _t;
	}
	
	public final void expressionOrSubQuery(AST _t) throws RecognitionException {
		
		AST expressionOrSubQuery_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionOrSubQuery_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case KEY:
			case VALUE:
			case ENTRY:
			case AGGREGATE:
			case CASE2:
			case CAST:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case WEIRD_IDENT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_BIG_INTEGER:
			case NUM_BIG_DECIMAL:
			case JAVA_CONSTANT:
			case IDENT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case QUOTED_STRING:
			case COLON:
			case PARAM:
			case NUM_INT:
			{
				expr(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expressionOrSubQuery_AST = (AST)currentAST.root;
				break;
			}
			case QUERY:
			{
				query(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				expressionOrSubQuery_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = expressionOrSubQuery_AST;
		_retTree = _t;
	}
	
	public final void simpleCaseWhenClause(AST _t) throws RecognitionException {
		
		AST simpleCaseWhenClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST simpleCaseWhenClause_AST = null;
		
		try {      // for error handling
			AST __t511 = _t;
			AST tmp82_AST = null;
			AST tmp82_AST_in = null;
			tmp82_AST = astFactory.create((AST)_t);
			tmp82_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp82_AST);
			ASTPair __currentAST511 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,WHEN);
			_t = _t.getFirstChild();
			expressionOrSubQuery(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			expressionOrSubQuery(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST511;
			_t = __t511;
			_t = _t.getNextSibling();
			simpleCaseWhenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = simpleCaseWhenClause_AST;
		_retTree = _t;
	}
	
	public final void elseClause(AST _t) throws RecognitionException {
		
		AST elseClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST elseClause_AST = null;
		
		try {      // for error handling
			AST __t513 = _t;
			AST tmp83_AST = null;
			AST tmp83_AST_in = null;
			tmp83_AST = astFactory.create((AST)_t);
			tmp83_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp83_AST);
			ASTPair __currentAST513 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,ELSE);
			_t = _t.getFirstChild();
			expressionOrSubQuery(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST513;
			_t = __t513;
			_t = _t.getNextSibling();
			elseClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = elseClause_AST;
		_retTree = _t;
	}
	
	public final void searchedCaseWhenClause(AST _t) throws RecognitionException {
		
		AST searchedCaseWhenClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST searchedCaseWhenClause_AST = null;
		
		try {      // for error handling
			AST __t520 = _t;
			AST tmp84_AST = null;
			AST tmp84_AST_in = null;
			tmp84_AST = astFactory.create((AST)_t);
			tmp84_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp84_AST);
			ASTPair __currentAST520 = currentAST.copy();
			currentAST.root = currentAST.child;
			currentAST.child = null;
			match(_t,WHEN);
			_t = _t.getFirstChild();
			logicalExpr(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			expressionOrSubQuery(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			currentAST = __currentAST520;
			_t = __t520;
			_t = _t.getNextSibling();
			searchedCaseWhenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = searchedCaseWhenClause_AST;
		_retTree = _t;
	}
	
	public final void addrExprLhs(AST _t) throws RecognitionException {
		
		AST addrExprLhs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST addrExprLhs_AST = null;
		
		try {      // for error handling
			addrExpr(_t, false );
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			addrExprLhs_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = addrExprLhs_AST;
		_retTree = _t;
	}
	
	public final void propertyName(AST _t) throws RecognitionException {
		
		AST propertyName_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST propertyName_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WEIRD_IDENT:
			case IDENT:
			{
				identifier(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				propertyName_AST = (AST)currentAST.root;
				break;
			}
			case CLASS:
			{
				AST tmp85_AST = null;
				AST tmp85_AST_in = null;
				tmp85_AST = astFactory.create((AST)_t);
				tmp85_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp85_AST);
				match(_t,CLASS);
				_t = _t.getNextSibling();
				propertyName_AST = (AST)currentAST.root;
				break;
			}
			case ELEMENTS:
			{
				AST tmp86_AST = null;
				AST tmp86_AST_in = null;
				tmp86_AST = astFactory.create((AST)_t);
				tmp86_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp86_AST);
				match(_t,ELEMENTS);
				_t = _t.getNextSibling();
				propertyName_AST = (AST)currentAST.root;
				break;
			}
			case INDICES:
			{
				AST tmp87_AST = null;
				AST tmp87_AST_in = null;
				tmp87_AST = astFactory.create((AST)_t);
				tmp87_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp87_AST);
				match(_t,INDICES);
				_t = _t.getNextSibling();
				propertyName_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = propertyName_AST;
		_retTree = _t;
	}
	
	public final void mapComponentReference(AST _t) throws RecognitionException {
		
		AST mapComponentReference_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST mapComponentReference_AST = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case KEY:
			{
				AST __t546 = _t;
				AST tmp88_AST = null;
				AST tmp88_AST_in = null;
				tmp88_AST = astFactory.create((AST)_t);
				tmp88_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp88_AST);
				ASTPair __currentAST546 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,KEY);
				_t = _t.getFirstChild();
				mapPropertyExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST546;
				_t = __t546;
				_t = _t.getNextSibling();
				mapComponentReference_AST = (AST)currentAST.root;
				break;
			}
			case VALUE:
			{
				AST __t547 = _t;
				AST tmp89_AST = null;
				AST tmp89_AST_in = null;
				tmp89_AST = astFactory.create((AST)_t);
				tmp89_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp89_AST);
				ASTPair __currentAST547 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,VALUE);
				_t = _t.getFirstChild();
				mapPropertyExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST547;
				_t = __t547;
				_t = _t.getNextSibling();
				mapComponentReference_AST = (AST)currentAST.root;
				break;
			}
			case ENTRY:
			{
				AST __t548 = _t;
				AST tmp90_AST = null;
				AST tmp90_AST_in = null;
				tmp90_AST = astFactory.create((AST)_t);
				tmp90_AST_in = (AST)_t;
				astFactory.addASTChild(currentAST, tmp90_AST);
				ASTPair __currentAST548 = currentAST.copy();
				currentAST.root = currentAST.child;
				currentAST.child = null;
				match(_t,ENTRY);
				_t = _t.getFirstChild();
				mapPropertyExpression(_t);
				_t = _retTree;
				astFactory.addASTChild(currentAST, returnAST);
				currentAST = __currentAST548;
				_t = __t548;
				_t = _t.getNextSibling();
				mapComponentReference_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = mapComponentReference_AST;
		_retTree = _t;
	}
	
	public final void propertyRefLhs(AST _t) throws RecognitionException {
		
		AST propertyRefLhs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST propertyRefLhs_AST = null;
		
		try {      // for error handling
			propertyRef(_t);
			_t = _retTree;
			astFactory.addASTChild(currentAST, returnAST);
			propertyRefLhs_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = propertyRefLhs_AST;
		_retTree = _t;
	}
	
	public final void mapPropertyExpression(AST _t) throws RecognitionException {
		
		AST mapPropertyExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST mapPropertyExpression_AST = null;
		AST e_AST = null;
		AST e = null;
		
		try {      // for error handling
			e = _t==ASTNULL ? null : (AST)_t;
			expr(_t);
			_t = _retTree;
			e_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			
			validateMapPropertyExpression( e_AST );
			
			mapPropertyExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = mapPropertyExpression_AST;
		_retTree = _t;
	}
	
	public final void numericInteger(AST _t) throws RecognitionException {
		
		AST numericInteger_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST numericInteger_AST = null;
		
		try {      // for error handling
			AST tmp91_AST = null;
			AST tmp91_AST_in = null;
			tmp91_AST = astFactory.create((AST)_t);
			tmp91_AST_in = (AST)_t;
			astFactory.addASTChild(currentAST, tmp91_AST);
			match(_t,NUM_INT);
			_t = _t.getNextSibling();
			numericInteger_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			if (_t!=null) {_t = _t.getNextSibling();}
		}
		returnAST = numericInteger_AST;
		_retTree = _t;
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
		"FLOAT_SUFFIX",
		"FROM_FRAGMENT",
		"IMPLIED_FROM",
		"JOIN_FRAGMENT",
		"ENTITY_JOIN",
		"SELECT_CLAUSE",
		"LEFT_OUTER",
		"RIGHT_OUTER",
		"ALIAS_REF",
		"PROPERTY_REF",
		"SQL_TOKEN",
		"SELECT_COLUMNS",
		"SELECT_EXPR",
		"THETA_JOINS",
		"FILTERS",
		"METHOD_NAME",
		"NAMED_PARAM",
		"BOGUS",
		"RESULT_VARIABLE_REF"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 72621093748248576L, -3494774629341187136L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 72761831236603952L, -3494774629307632704L, 3L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	}
	
