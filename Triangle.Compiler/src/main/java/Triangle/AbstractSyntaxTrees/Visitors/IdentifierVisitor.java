package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Terminals.Identifier;

public interface IdentifierVisitor<TArg, TResult> {

	TResult visitIdentifier(Identifier ast, TArg arg);

}
