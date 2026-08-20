package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    void testDictionaryLoaderValidWords() throws IOException {
        File dictFile = File.createTempFile("dict", ".txt");
        dictFile.deleteOnExit();
        try (FileWriter fw = new FileWriter(dictFile)) {
            fw.write("абвгд\n");
            fw.write("абвге\n");
            fw.write("абвжз\n");
            fw.write("абвгдд\n");
            fw.write("аб-вг\n");
            fw.write("ёжики\n");
        }

        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        WordleDictionaryLoader loader = new WordleDictionaryLoader(logFile);
        WordleDictionary dict = loader.load(dictFile);

        List<String> words = dict.getWords();
        assertEquals(4, words.size());
        assertTrue(words.contains("абвгд"));
        assertTrue(words.contains("абвге"));
        assertTrue(words.contains("абвжз"));
        assertTrue(words.contains("ежики"));
        assertFalse(words.contains("абвгдд"));
        assertFalse(words.contains("аб-вг"));
    }

    @Test
    void testGetRandomWord() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге", "абвжз");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        String random = dict.getRandomWord();
        assertTrue(words.contains(random));
    }

    @Test
    void testIsValidPasses() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        assertDoesNotThrow(() -> dict.isValid("абвгд"));
    }

    @Test
    void testIsValidThrowsException() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        assertThrows(WordNotFoundInDictionary.class, () -> dict.isValid("абвжз"));
    }

    @Test
    void testCheckWordAllCorrect() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        String result = game.checkWord("абвгд");
        assertEquals("+++++", result);
        assertTrue(game.isWinner("абвгд"));
    }

    @Test
    void testCheckWordPartialMatch() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "бвгдв");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        String result = game.checkWord("бвгдв");
        assertEquals("^^^^^", result);
    }

    @Test
    void testCheckWordNoMatch() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "ежзик");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        String result = game.checkWord("ежзик");
        assertEquals("-----", result);
    }

    @Test
    void testIsWinnerTrue() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        assertTrue(game.isWinner("абвгд"));
    }

    @Test
    void testIsWinnerFalse() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        assertFalse(game.isWinner("абвге"));
    }

    @Test
    void testStepsDecrement() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);
        assertEquals(6, game.getSteps());
        game.checkWord("абвге");
        assertEquals(5, game.getSteps());
    }

    @Test
    void testTip() throws IOException {
        File logFile = File.createTempFile("log", ".txt");
        logFile.deleteOnExit();
        List<String> words = List.of("абвгд", "абвге", "абвжз");
        WordleDictionary dict = new WordleDictionary(words, logFile);
        WordleGame game = new WordleGame(logFile, dict, "абвгд", 6);

        String tip1 = game.tip();
        assertEquals("абвгд", tip1);

        game.checkWord("абвге");
        String tip2 = game.tip();
        assertEquals("абвгд", tip2);
    }
}