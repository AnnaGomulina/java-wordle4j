package ru.yandex.practicum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private File logFile;

    public List<String> getWords() {
        return words;
    }

    private List<String> words;

    public WordleDictionary(List words, File logFile) {
        this.words = words;
        this.logFile = logFile;
    }

    public String getRandomWord(){
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
    }

    public void isValid(String word) throws WordNotFoundInDictionary{
        if (words.contains(word)){

        } else {
            throw new WordNotFoundInDictionary("Слова нет в словаре!");
        }
    }
}
