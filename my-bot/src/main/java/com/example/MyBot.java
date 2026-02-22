package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import io.github.cdimascio.dotenv.Dotenv;


import com.example.model.Card;

import org.telegram.telegrambots.meta.api.objects.Message;

// Класс бота наследуется от стандартного класса библиотеки
public class MyBot extends TelegramLongPollingBot {

    private DatabaseManager db = new DatabaseManager();
 //   public Card card = new Card(getBotUsername(), 0, getBaseUrl());

    @Override
    public String getBotUsername() {
        return  "evercards_bot"; 
    }

    @Override
    public String getBotToken() {

        // Загружаем файл .env
    Dotenv dotenv = Dotenv.load();
    
    // Берем значение по ключу "BOT_TOKEN"
    String token = dotenv.get("BOT_TOKEN");
    
    // Проверка на случай, если забыл создать файл
    if (token == null || token.isEmpty()) {
        System.err.println("ОШИБКА: Токен не найден в файле .env!");
    }
    
    return token;


       // return "5167467929:AAHe4WTC_nXgrTdpeIqkdjJ8eVHRK0TAw20"; 
    }

    @Override
public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
        handleText(update.getMessage());
    } else if (update.hasCallbackQuery()) {
        handleCallback(update.getCallbackQuery());
    }
}


private void handleText(Message message) {
    String text = message.getText();
    String user = message.getFrom().getFirstName();
    long chatId = message.getChatId();


    System.out.println("LOG: [" + user + "] написал: " + text);



    long adminId = 1041658076L; 

    if (chatId == adminId && text.startsWith("/add")) {
    try {
        // Формат: /add Название | Цена | Описание | Ссылка на фото
        String[] parts = text.replace("/add ", "").split("\\|");
        String name = parts[0].trim();
        double price = Double.parseDouble(parts[1].trim());
        String desc = parts[2].trim();
        String img = parts[3].trim();

        db.addCard(new Card(name, price, desc, img));
        sendMsg(chatId, "✅ Карточка «" + name + "» добавлена в базу!");
    } catch (Exception e) {
        sendMsg(chatId, "❌ Ошибка! Формат: /add Имя | Цена | Описание | Ссылка");
    }
}


    if (text.equals(BotCommands.START_COMMAND)) {
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText("Добро пожаловать в EverCards! Выберите действие:");
        sm.setReplyMarkup(TelegramUtils.createMainMenu());
        
        try { execute(sm); } catch (Exception e) { e.printStackTrace(); }
    }
}

private void handleCallback(CallbackQuery query) {
    String data = query.getData();
    String user = query.getFrom().getFirstName();
    long chatId = query.getMessage().getChatId();

    System.out.println("LOG: [" + user + "] нажал кнопку: " + data);

    if (data.startsWith("buy_")) {
    String cardName = data.replace("buy_", "");
    String userName = query.getFrom().getUserName(); // Юзернейм покупателя

    // 1. Отвечаем покупателю
    sendMsg(chatId, "✅ Заявка на покупку «" + cardName + "» принята! Админ свяжется с вами.");

    // 2. Уведомляем тебя (админа)
    // Опять же, используй свой ID вместо 12345678
    sendMsg(1041658076L, "🔔 НОВЫЙ ЗАКАЗ!\nТовар: " + cardName + "\nПокупатель: @" + userName);
}


    if (data.equals(BotCommands.GET_HELP)) {
    String helpText = "❓ **Как купить карту?**\n\n" +
                      "1. Выберите карту в каталоге.\n" +
                      "2. Нажмите кнопку 'Купить'.\n" +
                      "3. Наш менеджер напишет вам для оплаты и доставки.\n\n" +
                      "По всем вопросам: @твой_ник";
    sendMsg(chatId, helpText);
}

    if (data.equals(BotCommands.VIEW_CATALOG)) {
    List<Card> allCards = db.getAllCards();
    if (allCards.isEmpty()) {
        sendMsg(chatId, "В каталоге пока пусто!");
    } else {
        for (Card card : allCards) {
            sendCard(chatId, card);
        }
    }

}
}

// Тот самый недостающий метод!
private void sendCard(long chatId, Card card) {
    SendPhoto photo = new SendPhoto();
    photo.setChatId(String.valueOf(chatId));
    photo.setPhoto(new InputFile(card.getImageUrl()));
    photo.setCaption("📦 *Карточка:* " + card.getName() + "\n\n" + 
                     card.getDescription() + "\n\n" + 
                     "💰 *Цена:* " + card.getPrice() + " руб.");
    photo.setParseMode("Markdown");

    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    
    InlineKeyboardButton buyBtn = new InlineKeyboardButton();
    buyBtn.setText("🛒 Купить");
    buyBtn.setCallbackData("buy_" + card.getName());

    rows.add(List.of(buyBtn));
    markup.setKeyboard(rows);
    photo.setReplyMarkup(markup);

    try {
        execute(photo);
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
}


// Вспомогательный метод для быстрой отправки текста
private void sendMsg(long chatId, String text) {
    SendMessage sm = new SendMessage();
    sm.setChatId(String.valueOf(chatId));
    sm.setText(text);
    try { execute(sm); } catch (Exception e) { e.printStackTrace(); }
}

    // Главный метод, который запускает бота
}