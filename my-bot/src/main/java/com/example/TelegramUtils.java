package com.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

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
}