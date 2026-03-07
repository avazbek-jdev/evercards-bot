package com.example;

public class BotCommands {
    // Команды каталога
    public static final String VIEW_CATALOG = "view_catalog";
    public static final String GET_HELP = "get_help";
    
    // Команды действий с товаром
    public static final String BUY_PREFIX = "buy_";
    public static final String DELETE_PREFIX = "delete_";
    
    // Текстовые команды (из чата)
    public static final String START_COMMAND = "/start";
    public static final String ADD_PRODUCT_PREFIX = "/add";
    public static final String BACK = "🔙 Назад";
    public static final String toShop = "к покупкам";
    public static final String showProfile = "профиль";
    public static final String HELP_TEXT = "Привет! Я бот для управления коллекцией карточек. Вот что я умею:\n\n" +
            "1. 🗂 Посмотреть карточки - открыть каталог с карточками.\n" +
            "2. 🆘 Помощь - показать это сообщение.\n\n" +
            "В каталоге вы можете листать карточки и нажимать 'Купить', чтобы узнать больше о каждой из них.";

    public static final String ALL_CARDS = "/allcards";
    
}