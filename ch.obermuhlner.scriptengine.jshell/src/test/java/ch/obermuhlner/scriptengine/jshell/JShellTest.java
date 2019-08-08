package ch.obermuhlner.scriptengine.jshell;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JShellTest {
    @Test
    public void testEmpty() throws ScriptException {
        assertScript("", null);
    }

    @Test
    public void testSimple() throws ScriptException {
        assertScript("2+3", "5");
    }

    @Test
    public void testUnknownVariable() {
        assertScriptThrows("unknown", ScriptException.class);
    }

    @Test
    public void testIncompleteScript() {
        assertScriptThrows("foo(", ScriptException.class);
    }

    @Test
    public void testBindings() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jshell");
        engine.put("alpha", 2);
        engine.put("beta", 3);
        Object result = engine.eval("alpha + beta");
        assertEquals("5", result);
    }

    private void assertScript(String script, Object expectedResult) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jshell");
        Object result = engine.eval(script);
        assertEquals(expectedResult, result);
    }

    private void assertScriptThrows(String script, Class<? extends Throwable> throwableClass) {
        try {
            assertScript(script, "Should never reach the result");
            fail("Expected throwing: " + throwableClass.getName());
        } catch (Throwable throwable) {
            System.out.println(throwable.getClass().getName() + " : " + throwable.getMessage());
            if (!throwable.getClass().isAssignableFrom(throwableClass)) {
                fail("Expected throwing: " + throwableClass.getName() + " but was thrown: " + throwable.getClass().getName());
            }
        }
    }
}