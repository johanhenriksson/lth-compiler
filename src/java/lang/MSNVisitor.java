package lang;

import lang.ast.*;

public class MSNVisitor extends TraversingVisitor {
    int max_depth = 0;

    public static int result(ASTNode n) {
        MSNVisitor v = new MSNVisitor();
        n.accept(v, 0);
        return v.max_depth;
    }

	public Object visit(Block node, Object data) { 
        int depth = (int)data + 1;
        this.max_depth = Math.max(max_depth, depth);
        return visitChildren(node, depth); 
    }
}
