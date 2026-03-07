package com.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.model.Card;

public class Main {
    Card card = new Card(null, 0, null, null);
    public static void main(String[] args) {
         try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());

            DatabaseManager db = new DatabaseManager();
// Добавим одну тестовую карту, если каталог пуст
/* 
if (db.getAllCards().isEmpty()) {
    db.addCard(new Card("Тестовая Карта", 99.0, "Описание из базы", "https://img.freepik.com/free-vector/card-game-concept-illustration_114360-634.jpg"));
    System.out.println("LOG: Тестовая карта добавлена в БД!");
}
*/

Card card1 = new Card("Charizard", 5000.0, "Огненный дракон, редкая голограмма", "C:\\Users\\Avazbek\\Desktop\\java project\\tg bot\\miliy jiraf.jpg");
Card card2 = new Card("Pikachu", 1200.0, "Электрический мышонок, классика", "C:\\Users\\Avazbek\\Desktop\\java project\\tg bot\\s prazdnikom.jpg");
Card card3 = new Card("Mewtwo", 8000.0, "Легендарный психический покемон", "C:\\Users\\Avazbek\\Desktop\\java project\\tg bot\\sunshine.jpg");
Card card4 = new Card("Blastoise", 4500.0, "Водная пушка, мощная защита", "C:\\Users\\Avazbek\\Desktop\\java project\\tg bot\\ti moy mir.jpg");

db.addCard(card1);
db.addCard(card2);
db.addCard(card3);
db.addCard(card4);
    System.out.println("LOG: 4 тестовые карты добавлены в БД!");


            System.out.println("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
