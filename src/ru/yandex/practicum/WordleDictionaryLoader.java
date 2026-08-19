package ru.yandex.practicum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private File logFile;
    public WordleDictionaryLoader(File logFile){
        this.logFile = logFile;
    }
    public WordleDictionary load(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file)) {
            ArrayList<String> goodWords = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> words = bufferedReader.lines().toList();
            for (String word : words) {
                if (word.length() != 5 || word.contains("-")){
                    continue;
                }
                word = word.replace('ё', 'е');
                goodWords.add(word);
            }
            return new WordleDictionary(goodWords, logFile);
        }

   }
}

