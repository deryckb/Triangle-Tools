package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Terminals.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Terminals.IntegerLiteral;

public interface LiteralVisitor<TArg, TResult> {

	TResult visitCharacterLiteral(CharacterLiteral ast, TArg arg);

	TResult visitIntegerLiteral(IntegerLiteral ast, TArg arg);

}
