package ch.obermuhlner.scriptengine.spring.expression;

import org.junit.Test;

import javax.script.*;

import java.io.Reader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpringExpressionScriptEngineTest {
    @Test
    public void testBindingVariables() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        engine.put("alpha", 2);
        engine.put("beta", 3);
        Object result = engine.eval("#alpha + #beta");

        assertThat(result).isEqualTo(5);
    }

    @Test
    public void testBindingRoot() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        PublicClass root = new PublicClass();
        root.message = "hello";

        engine.put(SpringExpressionScriptEngine.ROOT, root);
        Object result = engine.eval("message");

        assertThat(result).isEqualTo("hello");
    }

    @Test
    public void testBindingChangedVariables() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        engine.put("alpha", 2);
        Object result = engine.eval("#alpha = 3");
        assertThat(result).isEqualTo(3);

        Object alpha = engine.get("alpha");
        assertThat(alpha).isEqualTo(3);
    }

    @Test
    public void testConstructingReturnValue() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        PublicClass result = (PublicClass) engine.eval("new ch.obermuhlner.scriptengine.spring.expression.SpringExpressionScriptEngineTest.PublicClass(\"test\")");
        System.out.println(result.getClass());
        assertThat(result.message).isEqualTo("test");
    }

    @Test
    public void testCompilable() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");
        assertThat(engine).isInstanceOf(Compilable.class);

        Compilable compiler = (Compilable) engine;

        CompiledScript compiledScript = compiler.compile("#alpha + #beta");

        {
            Bindings bindings = engine.createBindings();

            bindings.put("alpha", 2);
            bindings.put("beta", 3);
            Object result = compiledScript.eval(bindings);
            assertThat(result).isEqualTo(5);
        }

        {
            Bindings bindings = engine.createBindings();

            bindings.put("alpha", "aaa");
            bindings.put("beta", "bbb");
            Object result = compiledScript.eval(bindings);
            assertThat(result).isEqualTo("aaabbb");
        }
    }

    @Test
    public void testErrors() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        assertThatThrownBy(() -> {
            Compilable compiler = (Compilable) engine;
            compiler.compile("#"); // parse error
        }).isInstanceOf(ScriptException.class);

        assertThatThrownBy(() -> {
            Object result = engine.eval("#"); // parse error
        }).isInstanceOf(ScriptException.class);

        assertThatThrownBy(() -> {
            Object result = engine.eval("1/0"); // evaluation error
        }).isInstanceOf(ScriptException.class);
    }

    @Test
    public void testEvalReader() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        Reader reader = new StringReader("1234");
        Object result = engine.eval(reader);
        assertThat(result).isEqualTo(1234);
    }

    @Test
    public void testEvalReaderContext() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        ScriptContext context = new SimpleScriptContext();
        context.getBindings(ScriptContext.ENGINE_SCOPE).put("alpha", 1000);
        Reader reader = new StringReader("#alpha+999");
        Object result = engine.eval(reader, context);
        assertThat(result).isEqualTo(1999);
    }

    @Test
    public void testEvalReaderBindings() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        SimpleBindings bindings = new SimpleBindings();
        bindings.put("alpha", 1000);
        Reader reader = new StringReader("#alpha+321");
        Object result = engine.eval(reader, bindings);
        assertThat(result).isEqualTo(1321);
    }

    @Test
    public void testSetGetContext() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        assertThat(engine.getContext()).isNotNull();

        SimpleScriptContext context = new SimpleScriptContext();
        engine.setContext(context);
        assertThat(engine.getContext()).isSameAs(context);

        assertThatThrownBy(() -> {
            engine.setContext(null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testCreateBindings() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        assertThat(engine.createBindings()).isNotNull();
    }

    @Test
    public void testSetBindings() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        engine.setBindings(null, ScriptContext.GLOBAL_SCOPE);

        assertThatThrownBy(() -> {
            engine.setBindings(null, ScriptContext.ENGINE_SCOPE);
        }).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> {
            engine.setBindings(new SimpleBindings(), -999);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetFactory() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("spel");

        ScriptEngineFactory factory = engine.getFactory();
        assertThat(factory.getClass()).isSameAs(SpringExpressionScriptEngineFactory.class);
    }

    public static class PublicClass {
        public String message;

        public PublicClass() {
            this("nothing");
        }

        public PublicClass(String message) {
            this.message = message;
        }
    }

}
