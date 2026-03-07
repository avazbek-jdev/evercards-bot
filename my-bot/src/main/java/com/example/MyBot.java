package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import io.github.cdimascio.dotenv.Dotenv;

import com.example.model.Card;
import com.example.TelegramUtils;

import org.telegram.telegrambots.meta.api.objects.Message;

// Класс бота наследуется от стандартного класса библиотеки
public class MyBot extends TelegramLongPollingBot {

    private DatabaseManager db = new DatabaseManager();

    @Override
    public String getBotUsername() {
        return "evercards_bot";
    }

    @Override
    public String getBotToken() {

        Dotenv dotenv = Dotenv.load();

        String token = dotenv.get("BOT_TOKEN");

        // Проверка на случай, если забыл создать файл
        if (token == null || token.isEmpty()) {
            System.err.println("ОШИБКА: Токен не найден в файле .env!");
        }

        return token;

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

        TelegramUtils utils = new TelegramUtils();

        String text = message.getText();
        String user = message.getFrom().getFirstName();
        long chatId = message.getChatId();

        System.out.println("LOG: [" + user + "] написал: " + text);

        long adminId = 1041658076L;
        /*
         * if (chatId == adminId && text.startsWith("/add")) {
         * try {
         * // Формат: /add Название | Цена | Описание | Ссылка на фото
         * String[] parts = text.replace("/add ", "").split("\\|");
         * String name = parts[0].trim();
         * double price = Double.parseDouble(parts[1].trim());
         * String desc = parts[2].trim();
         * String img = parts[3].trim();
         * 
         * db.addCard(new Card(name, price, desc, img));
         * sendMsg(chatId, "✅ Карточка «" + name + "» добавлена в базу!");
         * } catch (Exception e) {
         * sendMsg(chatId, "❌ Ошибка! Формат: /add Имя | Цена | Описание | Ссылка");
         * }
         * }
         */
        /*
         * if (text.equals(BotCommands.START_COMMAND)) {
         * SendMessage sm = new SendMessage();
         * sm.setChatId(String.valueOf(chatId));
         * sm.setText("Добро пожаловать в EverCards! Выберите действие:");
         * sm.setReplyMarkup(TelegramUtils.createMainMenu());
         * 
         * try { execute(sm); } catch (Exception e) { e.printStackTrace(); }
         * }
         */

        switch (text) {

            case BotCommands.ALL_CARDS:
                // Замени 12345678L на свой реальный Telegram ID
                if (chatId == 1041658076L) {
                    sendAdminAllCards(chatId);
                } else {
                    sendMessage(chatId, "Эта команда доступна только администратору.", getMainKeyboard());
                }
                break;

            case BotCommands.START_COMMAND:
                sendMsgKybrd(chatId, "halo, " + user + "! Добро пожаловать в EverCards! Выберите действие:",
                        utils.getMainKeyboard());
                break;
            case BotCommands.toShop:
                sendCatalog(chatId);
                break;
            case "Menu":

                break;

            case "Корзина":

                break;

            case "Stop":
                sendMsg(chatId, "Бот остановлен. Напиши /start чтобы вернуться");
                break;

            default:
                sendMsgKybrd(chatId, "Я не понимаю эту команду", utils.getMainKeyboard());
        }

       // sendMessage(chatId, report.toString(), getMainKeyboard());
    }


    private void updateCardMessage(long chatId, int messageId, int index) {

        DatabaseManager db = new DatabaseManager();
        List<Card> cards = db.getAllCards();
        Card card = cards.get(index);

        // 1. Изменяем текст и кнопки
        EditMessageCaption editCaption = new EditMessageCaption();
        editCaption.setChatId(String.valueOf(chatId));
        editCaption.setMessageId(messageId);
        editCaption.setCaption(
                "🌟 *" + card.getName() + "* [" + (index + 1) + "/" + cards.size() + "]\n\n" + card.getDescription());
        editCaption.setParseMode("Markdown");
        editCaption.setReplyMarkup(TelegramUtils.getNavigationKeyboard(index, cards.size()));

        // 2. Изменяем само фото
        EditMessageMedia editMedia = new EditMessageMedia();
        editMedia.setChatId(String.valueOf(chatId));
        editMedia.setMessageId(messageId);
        editMedia.setMedia(new InputMediaPhoto(card.getImageUrl()));

        try {
            execute(editCaption);
            execute(editMedia);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallback(CallbackQuery query) {
        String data = query.getData();
        String user = query.getFrom().getFirstName();
        long chatId = query.getMessage().getChatId();
        int messageId = query.getMessage().getMessageId();

        System.out.println("LOG: [" + user + "] нажал кнопку: " + data);

        if (data.startsWith("nav_")) {
            int index = Integer.parseInt(data.split("_")[1]);
            updateCardMessage(chatId, messageId, index);
        }

        if (data.startsWith("buy_")) {
            String cardName = data.replace("buy_", "");
            String userName = query.getFrom().getUserName(); // Юзернейм покупателя

            // 1. Отвечаем покупателю
            sendMsg(chatId, "✅ Заявка на покупку «" + cardName + "» принята! Админ свяжется с вами.");

            // 2. Уведомляем тебя (админа)
            // Опять же, используй свой ID вместо 12345678
            sendMsg(1041658076L, "🔔 НОВЫЙ ЗАКАЗ!\nТовар: " + cardName + "\nПокупатель: @" + userName);
        }

        switch (data) {

            case BotCommands.GET_HELP:
                // Обработка кнопки помощи
                String helpText = "❓ **Как купить карту?**\n\n" +
                        "1. Выберите карту в каталоге.\n" +
                        "2. Нажмите кнопку 'Купить'.\n" +
                        "3. Наш менеджер напишет вам для оплаты и доставки.\n\n" +
                        "По всем вопросам: @твой_ник";
                sendMsg(chatId, helpText);
                break;
            case BotCommands.VIEW_CATALOG:
                // Обработка кнопки просмотра каталога
                List<Card> allCards = db.getAllCards();
                if (allCards.isEmpty()) {
                    sendMsg(chatId, "В каталоге пока пусто!");
                } else {
                    for (Card card : allCards) {
                        sendCard(chatId, card);
                    }
                }
                break;

            case BotCommands.toShop:
                sendMsgKybrd(chatId, "Вот наше меню:", TelegramUtils.createMainMenu());
                break;

            default:
                break;
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

    private void sendCatalog(long chatId) {
        DatabaseManager db = new DatabaseManager();
        List<Card> cards = db.getAllCards();

        if (cards.isEmpty()) {
            sendMessage(chatId, "📦 В магазине пока пусто. Заходите позже!", getMainKeyboard());
            return;
        }

        for (Card card : cards) {
            // Создаем инлайн-кнопку "Купить" под каждой карточкой
            InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton buyButton = new InlineKeyboardButton();
            buyButton.setText("💰 Купить за " + card.getPrice() + "р.");
            buyButton.setCallbackData("buy_" + card.getName()); // Команда, которую получит бот при нажатии

            rowInline.add(buyButton);
            rowsInline.add(rowInline);
            inlineMarkup.setKeyboard(rowsInline);

            // Отправляем фото с описанием и кнопкой
            sendPhotoWithButton(chatId, card, inlineMarkup);
        }
    }

    private void sendPhotoWithButton(long chatId, Card card, InlineKeyboardMarkup markup) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(String.valueOf(chatId));
        photo.setPhoto(new InputFile(card.getImageUrl()));
        photo.setCaption("🌟 *" + card.getName() + "*\n\n" + card.getDescription());
        photo.setParseMode("Markdown");
        photo.setReplyMarkup(markup);

        try {
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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


    private void sendAdminAllCards(long chatId) {
    DatabaseManager db = new DatabaseManager();
    List<Card> cards = db.getAllCards();

    if (cards.isEmpty()) {
        sendMessage(chatId, "📭 В базе данных пока нет ни одной карты.", getMainKeyboard());
        return;
    }

    StringBuilder report = new StringBuilder("📂 **Полный список товаров:**\n\n");
    
    for (int i = 0; i < cards.size(); i++) {
        Card card = cards.get(i);
        report.append(i + 1).append(". **").append(card.getName()).append("**\n")
              .append("   💰 Цена: ").append(card.getPrice()).append("р.\n")
              .append("   📝 Описание: ").append(card.getDescription()).append("\n")
              .append("----------------------------\n");
              
        // Если список очень длинный, Telegram может не отправить всё сразу. 
        // Поэтому, если карт больше 10, лучше отправлять их частями.
        if (i > 0 && i % 10 == 0) {
            sendMessage(chatId, report.toString(), null);
            report = new StringBuilder();
        }
    }




    private ReplyKeyboardMarkup getMainKeyboard() {
    ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
    markup.setSelective(true);
    markup.setResizeKeyboard(true); // Кнопки будут компактными
    markup.setOneTimeKeyboard(false); // Меню не исчезнет после нажатия

    List<KeyboardRow> keyboard = new ArrayList<>();

    // Первая строка кнопок
    KeyboardRow row1 = new KeyboardRow();
    row1.add(BotCommands.toShop); // "к покупкам"
    row1.add(BotCommands.showProfile); // "профиль"

    // Вторая строка кнопок
    KeyboardRow row2 = new KeyboardRow();
    row2.add("Корзина");
    row2.add("Меню");

    keyboard.add(row1);
    keyboard.add(row2);

    markup.setKeyboard(keyboard);
    return markup;
}

    private void sendMessage(long chatId, String string, Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendMessage'");
    }

    private void sendMsg(long chatId, String text) {
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(text);
        try {
            execute(sm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsgKybrd(long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(text);
        sm.setReplyMarkup(markup);
        try {
            execute(sm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}