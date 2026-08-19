package ru.yandex.practicum;

import java.io.*;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        File logFile = new File("C:\\Java\\java-wordle4j\\log.txt");
        try (Writer fileWriter = new FileWriter(logFile, true)) {
            WordleDictionary wordleDictionary = new WordleDictionaryLoader(logFile).load(new File("C:\\Java\\java-wordle4j\\words_ru.txt"));
            String answer = wordleDictionary.getRandomWord();
            WordleGame wordleGame = new WordleGame(logFile, wordleDictionary, answer, 6);
            fileWriter.write("Новая игра");
            while (true){
                System.out.println("Введите слово из 5 букв");
                Scanner scanner = new Scanner(System.in);
                String selectedWord = scanner.nextLine().toLowerCase();
                if(selectedWord.equals("")){
                    System.out.println("Подсказка:");
                    String tip = wordleGame.tip();
                    System.out.println(tip);
                    selectedWord = tip;
                }
                wordleDictionary.isValid(selectedWord);

                String result = wordleGame.checkWord(selectedWord);
                System.out.println(result);

                if(wordleGame.isWinner(selectedWord)){
                    System.out.println("Ура, вы отгадали слово!");
                    break;
                } else if(wordleGame.getSteps() != 0){
                    System.out.println(String.format("Не угадали, попробуте еще раз. У вас осталось %d попыток.", wordleGame.getSteps()));
                } else {
                    System.out.println("Вы проиграли!\n# загаданное слово: "+answer);
                    break;
                }
            }
        } catch (FileNotFoundException exception){
            System.out.println("Нет файла со словами!");
        } catch (IOException exception){
            System.out.println("Проблема с файлом!");
        } catch (WordNotFoundInDictionary exception){
            System.out.println(exception.getMessage());
        }
        catch (Exception exception){
            System.out.println("Возникла непредвиденная ошибка!");
        }

    }

}
