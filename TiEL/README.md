# Der TiEL Interpreter

Bei TiEL (Tiny Educational Language) handelt es sich um eine rudimentäre
Programmiersprache, die in der Veranstaltung "Automaten, Sprachen und Compiler" am
Fachbereich MNI der THM Gießen verwendet wird. Sie unterstützt Funktionen, Variablen,
mathematische Ausdrücke und grundlegende Kontrollflussanweisungen.

## Beispiel

Das folgende Programm berechnet den letzten Wert der Fibonacci-Sequenz für `n = 12`.

```
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
```

Weitere Beispiele befinden sich im Verzeichnis `example` und den Unit-Tests.

## Entwicklungs-Setup

Für die Arbeit mit diesem Projekt wird die Verwendung von IntelliJ IDEA empfohlen. Die
folgenden Schritte beschreiben, wie das Projekt aufgesetzt werden kann. Das Projekt
benötigt Java 21.

1. Laden Sie dieses Projekt aus dem Moodlekurs herunter (bereits geschehen).
2. Öffnen Sie das Projekt in IntelliJ (`File → Open`). Das Projekt sollte automatisch
   erkannt und die benötigte Java-Version installiert werden. Falls IntellIJ nachfragt,
   vertrauen Sie dem Projekt.
3. Wählen Sie die Run-Konfiguration `TiEL` (obere rechte Ecke des Fensters).
4. Drücken Sie das kleine grüne Dreieck neben der Run-Configuration, um das Projekt auszuführen.

Standardmäßig wird die Datei `fibonacci.tiel` aus dem Verzeichnis `example` als Eingabe für
den Interpreter verwendet. Durch Anpassung der Run-Konfiguration kann die auszuführende
Datei angepasst werden.

JUnit Tests können zur Datei `TiELTest.java` hinzugefügt werden oder in neue Klassen (in
das selbe Verzeichnis) gelegt werden. Durch Auslösen des `verification/test` Tasks können
alle Tests ausgeführt werden. Dies ist insbesondere hilfreich für das
Test-Driven-Development neuer Funktionalitäten.

## Erstellen einer portablen JAR-Datei

Das Projekt erlaubt das Erstellen einer portablen JAR-Datei mithilfe des
[Shadow-Plugins](https://gradleup.com/shadow/) für Gradle. Die folgenden Schritte
beschreiben, wie eine solche Datei erzeugt werden kann:

1. Öffnen Sie das Gradle-Menü in IntelliJ (rechte Seite des Fensters).
2. Erweitern Sie `interpreter/Tasks/shadow`.
3. Führen sie die Aufgabe `shadowJar` mithilfe eines Doppelklicks aus.

Sie finden die erzeugte JAR-Datei im Ordner `build/libs/` unter dem Namen `TiEL.jar`.

## Verwendung des CLIs (d.h., der TiEL.jar)

```
Usage: tiel [OPTIONS] INPUT

Options:
  --tokens      Scans the source code and prints the recognized tokens.
  --ast         Scans and parses the source code and prints the generated AST.
  --help        Shows this dialog.
```

Beispiel: `java -jar TiEL.jar input.tiel --tokens --ast`