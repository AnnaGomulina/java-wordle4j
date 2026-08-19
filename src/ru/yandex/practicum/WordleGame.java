package ru.yandex.practicum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private static final Set<Character> LETTERS = Set.of(
            'а', 'б', 'в', 'г', 'д', 'е',
            'ж', 'з', 'и', 'й', 'к', 'л', 'м',
            'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ',
            'ы', 'ь', 'э', 'ю', 'я'
    );

    private File logFile;
    private String answer;

    private LinkedHashMap<Character, List<Integer>> listLinkedHashMap = new LinkedHashMap<>();

    public int getSteps() {
        return steps;
    }

    private int steps;

    private WordleDictionary dictionary;

    public WordleGame(File logFile, WordleDictionary dictionary, String answer, int steps) {
        this.logFile = logFile;
        this.dictionary = dictionary;
        this.answer = answer;
        this.steps = steps;
        for (char letter : LETTERS) {
            ArrayList<Integer> ints = new ArrayList<>();
            ints.add(0);
            ints.add(1);
            ints.add(2);
            ints.add(3);
            ints.add(4);
            listLinkedHashMap.put(letter, ints);
        }
    }

    public String tip(){
        for (String candidate : dictionary.getWords()) {
            boolean isOk = true;
            for (int index = 0; index < candidate.length(); index++) {
                List<Integer> variants = listLinkedHashMap.get(candidate.charAt(index));
                if (!variants.contains(index)) {
                    isOk = false;
                    break;
                }
                for (Map.Entry<Character, List<Integer>> characterListEntry : listLinkedHashMap.entrySet()) {
                    if (characterListEntry.getValue().size() == 1) {
                        if ((characterListEntry.getValue().getFirst() == index)) {
                            if (candidate.charAt(index) != characterListEntry.getKey()) {
                                isOk = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (isOk) {
                return candidate;
            }
        }
        throw new RuntimeException("Ошибка в поиске подсказки");
    }

    public String checkWord(String selectedWord) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(int index = 0; index < answer.length(); index++){
            char charAnswer = answer.charAt(index);
            char charSelectedWord = selectedWord.charAt(index);
            if(charAnswer == charSelectedWord){
                stringBuilder.append("+");
            } else if(answer.contains(String.valueOf(charSelectedWord))){
                stringBuilder.append("^");
            } else {
                stringBuilder.append("-");
            }
        }
        String result = stringBuilder.toString();
        steps-=1;
        addResult(result, selectedWord);
        try(Writer fileWriter = new FileWriter(logFile, true)) {
            fileWriter.write(String.format("Пользователь ввел слово: %s \n", selectedWord));
            return result;
        }
    }

    private void addResult(String result, String selectedWord) {
        for(int index = 0; index < selectedWord.length(); index++){
            char charSelectedWord = selectedWord.charAt(index);
            if (result.charAt(index) == '+') {
                listLinkedHashMap.put(charSelectedWord, List.of(index));
            } else if (result.charAt(index) == '^') {
                List<Integer> places = listLinkedHashMap.get(charSelectedWord);
                if (places.size() > 1) {
                    places.remove(new Integer(index));
                }
            } else {
                listLinkedHashMap.put(charSelectedWord, List.of());
            }
        }
    }

    public boolean isWinner(String selectedWord){
        if(answer.equals(selectedWord)){
            return true;
        } else {
            return false;
        }
    }
}
