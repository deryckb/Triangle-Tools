package Triangle.AbstractSyntaxTrees.Visitors;

import Triangle.AbstractSyntaxTrees.Actuals.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.Actuals.VarActualParameter;

public interface ActualParameterVisitor<TArg, TResult> {

	TResult visitConstActualParameter(ConstActualParameter ast, TArg arg);

	TResult visitFuncActualParameter(FuncActualParameter ast, TArg arg);

	TResult visitProcActualParameter(ProcActualParameter ast, TArg arg);

	TResult visitVarActualParameter(VarActualParameter ast, TArg arg);

}
