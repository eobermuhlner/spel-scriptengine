package ch.obermuhlner.scriptengine.example;

import javax.script.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ScriptEnginePerformance {
    public static void main(String[] args) {
        runCompilePerformance();
    }

    private static void runCompilePerformance() {
        System.out.print("Warmup ");
        for (int i = 0; i < 100; i++) {
            System.out.print(".");
            for (int j = 0; j < 10; j++) {
                runMultiEvalExample(i);
                runCompileMultiEvalExample(i);
            }
        }
        System.out.println();

        System.out.print("Measuring ");
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Compile_Multiple_Evaluations.csv")))) {
            out.println("# csv2chart.title=Multiple Eval vs. Single Compile + Multiple Eval");
            out.println("n, Multi Eval, Compile Multi Eval");
            for (int i = 0; i < 1000; i += 1) {
                int n = i;
                double millis1 = measure(() -> runMultiEvalExample(n));
                double millis2 = measure(() -> runCompileMultiEvalExample(n));
                out.println(i + ", " + millis1 + ", " + millis2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.println("Finished");
    }

    private static double measure(Runnable runnable) {
        return measure(10, runnable);
    }

    private static double measure(int n, Runnable runnable) {
        long startNanos = System.nanoTime();
        for (int i = 0; i < n; i++) {
            runnable.run();
        }
        long endNanos = System.nanoTime();
        return (endNanos - startNanos) / 1_000_000.0 / n;
    }

    private static void runMultiEvalExample(int n) {
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

    private static void runCompileMultiEvalExample(int n) {
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
