package com.stansvec.dropwizard.auth;

/**
 * The class use MVEL expression contained in Auth type to evaluate the authorization condition.
 * The class performs caching of expressions to achieve better performance.
 *
 * @author Stan Svec
 * Date: 4/10/13
 */
public class MvelExpressionEvaluation<R> {

    private static final String ALWAYS_TRUE_EXP = "";

    /*private final MvelVariableProvider<R> variableProvider;

    private final VariableResolverFactory commonVariables;*/

/*    private final ConcurrentMap<String, Serializable> expressionCache;

    public MvelExpressionEvaluation(MvelVariableProvider<R> variableProvider) {
        this.variableProvider = variableProvider;
        this.commonVariables = variableProvider.createCommonVariables();
        this.expressionCache = new ConcurrentHashMap<String, Serializable>();
    }
*/
    public boolean evaluate(R res, Auth auth/*, HttpContext httpContext*/) {
        if (ALWAYS_TRUE_EXP.equals(auth.check())) {
            return true;
        }
        /*VariableResolverFactory vars = variableProvider.createPerRequestVariables(res, httpContext);
        vars.setNextFactory(commonVariables);
        Serializable check = expressionCache.get(auth.check());
        if (check == null) {
            check = MVEL.compileExpression(auth.check());
            expressionCache.put(auth.check(), check);
        }
        return (Boolean) MVEL.executeExpression(check, vars);*/
        return false;
    }
}
