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
if (db.getAllCards().isEmpty()) {
    db.addCard(new Card("Тестовая Карта", 99.0, "Описание из базы", "https://img.freepik.com/free-vector/card-game-concept-illustration_114360-634.jpg"));
    System.out.println("LOG: Тестовая карта добавлена в БД!");
}

            System.out.println("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
