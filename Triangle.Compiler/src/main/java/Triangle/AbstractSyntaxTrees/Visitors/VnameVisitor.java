package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Vnames.DotVname;
import Triangle.AbstractSyntaxTrees.Vnames.SimpleVname;
import Triangle.AbstractSyntaxTrees.Vnames.SubscriptVname;

public interface VnameVisitor<TArg, TResult> {

	TResult visitDotVname(DotVname ast, TArg arg);

	TResult visitSimpleVname(SimpleVname ast, TArg arg);

	TResult visitSubscriptVname(SubscriptVname ast, TArg arg);

}
