package lang;

import lang.ast.*;

/**
 * Traverses each node, passing the data to the children.
 * Returns the data unchanged.
 * Overriding methods may change the data passed and the data returned.
 */
public class TraversingVisitor implements lang.ast.Visitor {
	protected Object visitChildren(ASTNode node, Object data) {
		for (int i = 0; i < node.getNumChild(); ++i) {
			node.getChild(i).accept(this, data);
		}
        return data;
	}

	public Object visit(List node, Object data) { return visitChildren(node, data); }
	public Object visit(Opt node, Object data) { return visitChildren(node, data); }

	public Object visit(Program node, Object data) { return visitChildren(node, data); }
	public Object visit(Function node, Object data) { return visitChildren(node, data); }
	public Object visit(Argument node, Object data) { return visitChildren(node, data); }
	public Object visit(Block node, Object data) { return visitChildren(node, data); }

	public Object visit(IdDecl node, Object data) { return visitChildren(node, data); }
	public Object visit(IdUse node, Object data) { return visitChildren(node, data); }
	public Object visit(TypeId node, Object data) { return visitChildren(node, data); }
	public Object visit(IdExpr node, Object data) { return visitChildren(node, data); }

	public Object visit(AssignStmt node, Object data) { return visitChildren(node, data); }
	public Object visit(ReturnStmt node, Object data) { return visitChildren(node, data); }
	public Object visit(CallStmt node, Object data) { return visitChildren(node, data); }
	public Object visit(DeclareStmt node, Object data) { return visitChildren(node, data); }
	public Object visit(IfStmt node, Object data) { return visitChildren(node, data); }
	public Object visit(WhileStmt node, Object data) { return visitChildren(node, data); }

	public Object visit(BinExpr node, Object data) { return visitChildren(node, data); }
	public Object visit(EqualsExpr node, Object data) { return visitChildren(node, data); }
	public Object visit(AddExpr node, Object data) { return visitChildren(node, data); }

	public Object visit(StringLiteral node, Object data) { return visitChildren(node, data); }
	public Object visit(FloatLiteral node, Object data) { return visitChildren(node, data); }
	public Object visit(IntLiteral node, Object data) { return visitChildren(node, data); }
	public Object visit(BoolLiteral node, Object data) { return visitChildren(node, data); }
}
