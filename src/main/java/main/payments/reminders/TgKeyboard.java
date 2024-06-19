package main.payments.reminders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

@SuppressWarnings("unchecked")
public interface TgKeyboard {

	default List<List<InlineKeyboardButton>> emptyKeyboard(int size) {
		return new ArrayList<>(size);
	}

	default InlineKeyboardMarkup inlineMarkup(List<InlineKeyboardRow> keyboard) {
		return new InlineKeyboardMarkup(keyboard);
	}

	default InlineKeyboardMarkup inlineMarkup(InlineKeyboardRow... rows) {
		return new InlineKeyboardMarkup(Arrays.asList(rows));
	}

	default List<InlineKeyboardRow> inlineTable(InlineKeyboardRow... rows) {
		return Arrays.asList(rows);
	}

	default ButtonPair btn() {
		return null;
	}

	default ButtonPair btn(String name, String query) {
		return new ButtonPair(name, query);
	}

	default InlineKeyboardRow row(ButtonPair... buttons) {
		List<InlineKeyboardButton> result = new LinkedList<>();
		for (ButtonPair buttonPair : buttons) {
			InlineKeyboardButton button = buttonPair == null ?
					builder().text("_").callbackData("_").build() :
					builder().text(buttonPair.name()).callbackData(buttonPair.query()).build();
			result.add(button);
		}
		return new InlineKeyboardRow(result);
	}

	record ButtonPair(String name, String query) {
	}
}
