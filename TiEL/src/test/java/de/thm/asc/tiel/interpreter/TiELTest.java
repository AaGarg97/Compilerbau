package de.thm.asc.tiel.interpreter;

import org.junit.jupiter.api.Test;

class TiELTest {

    @Test
    void fibonacci() {
        var sourceCode = """
fun fib(n) {
    if n == 0 then {
        return 0;
    } else if n == 1 then {
        return 1;
    } else {
        return fib(n - 1) + fib(n - 2);
    }
}

print(fib(12));              
                """;
        var expectedOutput = "144%n";

        Assertions.assertPrintOutput(sourceCode, expectedOutput);
    }
    @Test
    void chess() {
        var sourceCode = """
fun plustwo(n) {
    if n == 0 then {
        return 0;
    } else if n == 1 then {
        return 2;
    } else {
        return plustwo(n - 1)+2;
    }
}

print(plustwo(4));              
                """;
        var expectedOutput = "8%n";

        Assertions.assertPrintOutput(sourceCode, expectedOutput);
    }
    @Test
    void logicalOperatorsProduceBoolean() {
        var sourceCode = """
print(1 and 2);                 // true
print(nil and 2);               // false
print(true and false);          // false
print(false or true);           // true
print(nil or 3);                // true
print(1 and 2 and 3);           // true (all truthy)
print(true and true and true);  // true
print(true and false and true); // false (short-circuits at false)
print(nil and 2 and 3);         // false
print(false or nil or 3);       // true (3 is truthy)
print(0 or 1 or 2);             // true (first truthy value)
print(true or false or nil);    // true (short-circuits at true)
print(1 or 0 and 2);            // true — `or` has lower precedence than `and`: 1 or (0 and 2)
print((1 or 0) and 2);          // true — (1 or 0) → 1 → 1 and 2 → true
print(nil or false and true);   // false — false and true → false → nil or false → false
print(true and nil or 1);       // true — true and nil → nil → nil or 1 → true
print(false or true and false); // false — true and false → false → false or false → false
print(nil and nil and nil);     // false
print(false or false or false); // false
print(true and true and nil);   // false
print(true or true or nil);     // true
""";
        var expectedOutput = "true%nfalse%nfalse%ntrue%ntrue%ntrue%ntrue%nfalse%nfalse%ntrue%ntrue%ntrue%ntrue%ntrue%nfalse%ntrue%nfalse%nfalse%nfalse%nfalse%ntrue%n";

        Assertions.assertPrintOutput(sourceCode, expectedOutput);
    }

    @Test
    void emptyParameterList() {
        var sourceCode = """
fun myFunction() {
    return "ok!";
}
print(myFunction());""";
        var expectedOutput = "ok!%n";
        Assertions.assertPrintOutput(sourceCode, expectedOutput);
    }


    @Test
    void numbersAreFloatsInternally() {
        var source = """
            var a = 2;
            var b = 2.0;
            print(a == b);
            print(b);
        """;
        Assertions.assertPrintOutput(source, "true%n2%n");
    }

    //<< 02-design, unit-test, Test helloWorld()
    @Test
    void helloWorld() {
        var sourceCode = "print(\"Hello World!\");";
        var expectedOutput = "Hello World!%n";

        Assertions.assertPrintOutput(sourceCode, expectedOutput);
    }
    //>>

    @Test
    void unterminatedString() {
        var sourceCode = "var s = \"Hello World!";

        Assertions.assertScanningError(sourceCode, "Unterminated string.");
    }

    @Test
    void closure() {
        var sourceCode = "fun a(x) { fun b(y) {} }";

        Assertions.assertParsingError(sourceCode, "Expect expression.");
    }

    @Test
    void incompatibleTypes() {
        var sourceCode = "var x = 1 + \"A\";";

        Assertions.assertRuntimeError(sourceCode, "Operands to '+' must be numbers.");
    }

    @Test
    void doubleDeclaration() {
        var sourceCode = """
            var a = 5;
            var a = 4;
            """;

        Assertions.assertRuntimeError(sourceCode, "Identifier already declared 'a'.");
    }
}