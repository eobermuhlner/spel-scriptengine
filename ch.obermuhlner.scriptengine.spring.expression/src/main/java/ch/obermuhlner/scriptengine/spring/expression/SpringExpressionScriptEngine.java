package ch.obermuhlner.scriptengine.spring.expression;

import org.springframework.expression.*;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.script.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpringExpressionScriptEngine implements ScriptEngine, Compilable {
    public static final String ROOT = "_root";

    private final ExpressionParser parser = new SpelExpressionParser();

    private ScriptContext context = new SimpleScriptContext();

    @Override
    public ScriptContext getContext() {
        return context;
    }

    @Override
    public void setContext(ScriptContext context) {
        Objects.requireNonNull(context);
        this.context = context;
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public Bindings getBindings(int scope) {
        return context.getBindings(scope);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        context.setBindings(bindings, scope);
    }

    @Override
    public void put(String key, Object value) {
        getBindings(ScriptContext.ENGINE_SCOPE).put(key, value);
    }

    @Override
    public Object get(String key) {
        return getBindings(ScriptContext.ENGINE_SCOPE).get(key);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return new SpringExpressionScriptEngineFactory();
    }

    @Override
    public Object eval(Reader reader) throws ScriptException {
        return eval(readScript(reader));
    }

    @Override
    public Object eval(String script) throws ScriptException {
        return eval(script, context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        return eval(readScript(reader), context);
    }

    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval(script, context.getBindings(ScriptContext.ENGINE_SCOPE));
    }

    @Override
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        return eval(readScript(reader), bindings);
    }

    @Override
    public Object eval(String script, Bindings bindings) throws ScriptException {
        CompiledScript compile = compile(script);

        return compile.eval(bindings);
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        try {
            Expression exp = parser.parseExpression(script);

            return new SpringExpressionCompiledScript(this, exp);
        } catch (Exception ex) {
            throw new ScriptException(ex);
        }
    }

    @Override
    public CompiledScript compile(Reader reader) throws ScriptException {

        return compile(readScript(reader));
    }

    private String readScript(Reader reader) throws ScriptException {
        try {
            StringBuilder s = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                s.append(line);
                s.append("\n");
            }
            return s.toString();
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }
}
