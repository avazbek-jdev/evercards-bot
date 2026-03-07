package com.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;

public class TelegramUtils {
    public static InlineKeyboardMarkup createMainMenu() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    

        // Кнопка "Каталог"
        InlineKeyboardButton btnCatalog = new InlineKeyboardButton();
        btnCatalog.setText("🗂 Посмотреть карточки");
        btnCatalog.setCallbackData(BotCommands.VIEW_CATALOG);

        // Кнопка "Поддержка"
        InlineKeyboardButton btnHelp = new InlineKeyboardButton();
        btnHelp.setText("🆘 Помощь");
        btnHelp.setCallbackData(BotCommands.GET_HELP);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(btnCatalog);
        
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(btnHelp);

        rows.add(row1);
        rows.add(row2);
        markup.setKeyboard(rows);
        return markup;
    }


    public static InlineKeyboardMarkup getMainKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        
        InlineKeyboardButton toShop = new InlineKeyboardButton();
        toShop.setText("к покупкам");
        toShop.setCallbackData(BotCommands.toShop);

        InlineKeyboardButton getHelp = new InlineKeyboardButton();
        getHelp.setText("🆘 Помощь");
        getHelp.setCallbackData(BotCommands.GET_HELP);

        InlineKeyboardButton showProfile = new InlineKeyboardButton();
        showProfile.setText("профиль");
        showProfile.setCallbackData("show_profile");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(toShop);
        row1.add(showProfile);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(getHelp);


        rows.add(row1);
        rows.add(row2);
        markup.setKeyboard(rows);


        return markup;
    }

    public static InlineKeyboardMarkup getNavigationKeyboard(int index, int totalSize) {
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    List<InlineKeyboardButton> row = new ArrayList<>();

    // Кнопка Влево (индекс - 1)
    InlineKeyboardButton leftBtn = new InlineKeyboardButton();
    leftBtn.setText("⬅️");
    leftBtn.setCallbackData("nav_" + ((index - 1 + totalSize) % totalSize)); // Зацикливаем поиск

    // Кнопка Купить
    InlineKeyboardButton buyBtn = new InlineKeyboardButton();
    buyBtn.setText("💰 Купить");
    buyBtn.setCallbackData("buy_" + index);

    // Кнопка Вправо (индекс + 1)
    InlineKeyboardButton rightBtn = new InlineKeyboardButton();
    rightBtn.setText("➡️");
    rightBtn.setCallbackData("nav_" + ((index + 1) % totalSize)); // Зацикливаем поиск

    row.add(leftBtn);
    row.add(buyBtn);
    row.add(rightBtn);
    rows.add(row);
    
    markup.setKeyboard(rows);
    return markup;
}



}