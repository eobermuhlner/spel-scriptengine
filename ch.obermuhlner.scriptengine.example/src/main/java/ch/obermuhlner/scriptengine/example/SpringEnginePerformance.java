package ch.obermuhlner.scriptengine.example;

import javax.script.*;

public class SpringEnginePerformance {
    public static void main(String[] args) {
        runSpelCompilePerformance();
    }

    private static void runSpelCompilePerformance() {
        // warmup
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                runSpelMultiEvalExample(i);
                runSpelCompileMultiEvalExample(i);
            }
        }

        System.out.println("n, Multi Eval, Compile Multi Eval");
        for (int i = 0; i < 10000; i+=100) {
            int n = i;
            double millis1 = measure(() -> runSpelMultiEvalExample(n));
            double millis2 = measure(() -> runSpelCompileMultiEvalExample(n));
            System.out.println(i + ", " + millis1 + ", " + millis2);
        }
    }

    private static double measure(Runnable runnable) {
        long startNanos = System.nanoTime();
        runnable.run();
        long endNanos = System.nanoTime();
        return (endNanos - startNanos) / 1_000_000.0;
    }

    private static void runSpelMultiEvalExample(int n) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("spel");

            for (int i = 0; i < n; i++) {
                engine.put("alpha", 2);
                engine.put("beta", 3);
                Object result = engine.eval("#alpha + #beta");
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    private static void runSpelCompileMultiEvalExample(int n) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("spel");
            Compilable compiler = (Compilable) engine;

            CompiledScript compiledScript = compiler.compile("#alpha + #beta");

            for (int i = 0; i < n; i++) {
                Bindings bindings = engine.createBindings();

                bindings.put("alpha", 2);
                bindings.put("beta", 3);
                Object result = compiledScript.eval(bindings);
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

}
