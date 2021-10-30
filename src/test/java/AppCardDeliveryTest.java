import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class AppCardDeliveryTest {

    public String generationDatePlusDay(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String generationDateMinusDay(int days) {
        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String plusDay = generationDatePlusDay(3);
    public String minusDay = generationDateMinusDay(3);


    @BeforeEach
    void setup() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        open("http://localhost:9999");

    }

    // Задержка 10 сек
    // Все поля корректно заполнены.
    // Проверка отображения даты, в сплывающем окне при успешном бронировании встречи.
    @Test
    void shouldCompleteRegistration() {
        $("[data-test-id=city] .input__control").setValue("Краснодар");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $("[data-test-id='notification'] [class='notification__content']").shouldHave(text("Встреча успешно забронирована на " + plusDay));


        // $(:not([]))
    }


    // Не валидное значение поля Город
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInCityField() {
        $("[data-test-id=city] .input__control").setValue("Сочи");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear, ofSeconds(4));


    }

    // Не валидное значение поля (ФИО)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInNameField() {
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[class='input__control'][name='name']").setValue("Ivan Ivanov");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(appear, ofSeconds(4));

    }

    // Не валидное значение в поле (Номер телефона)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInPhoneNumberField0() {
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+7928777556");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible, ofSeconds(2));
    }

    // Пустое поле (Номер телефона)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInPhoneNumberField1() {
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible, ofSeconds(2));
    }

    // Поле дата с пустым значением
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInDateField0() {
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Неверно введена дата")).shouldBe(appear, ofSeconds(3));
    }

    // Не валидные значения в поле (Дата)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInDateField1() {
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(minusDay);
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(appear, ofSeconds(3));
    }

    // Проверка флажка
    @Test
    void shouldShowTheRequiredItemMessageAsAConsentFlag() {
        $("[data-test-id=city] .input__control").setValue("Краснодар");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(".input_invalid[data-test-id='agreement']").shouldBe(visible);
    }

    // Проверка выпадающего списка городов
    @Test
    void showDropDownListAndSelectionOption() {
        // Ввел 2 буквы в поле город
        $("[data-test-id=city] .input__control").setValue("Мо");
        // Кликнул по городу из выпадающего списка
        $(withText("Москва")).click();
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(plusDay);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $("[class='notification__content']").shouldHave(text("Встреча успешно забронирована на " + plusDay));
    }

    // Проверка списка даты
    // Проверка кнопок переключения месяца и года
    // Проверка выбора дня
    @Test
    void shouldShowDropDownListDatesAndTheOptionSelect0() {
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        // Чтобы выбор даты происходил от текущей
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[type][placeholder][pattern]").setValue(generationDatePlusDay(0));
        // Клик по таблице для выбора даты встречи
        $("span.input__box  button").click();
        // Проверка, что окно стало видимым
        $("[class='calendar calendar_theme_alfa-on-white']").shouldBe(visible);
        // Клик по кнопке переключения месяца вперед
        $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        // Клик по кнопке переключения месяца назад
        $("[class='calendar__arrow calendar__arrow_direction_left']").click();
        // Клик по кнопке переключения года вперед
        $("[class='calendar__arrow calendar__arrow_direction_right calendar__arrow_double']").click();
        // Клик по кнопке переключения года назад
        $("[class='calendar__arrow calendar__arrow_direction_left calendar__arrow_double']").click();
        // клик по дню в календаре
        $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        $(withText("27")).click();
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $("[class='notification__content']").shouldHave(text("Встреча успешно забронирована на"));

    }


    // Выбор даты на неделю вперед
    @Test
    void shouldGetOutDateWeekInAdvance() {
        $("[data-test-id=city] .input__control").setValue("Краснодар");
        $("[data-test-id='date'] .input__control").doubleClick();
        $("[data-test-id='date'] .input__control").sendKeys("BACKSPACE");
        $("[data-test-id='date'] .input__control").setValue(generationDatePlusDay(7));
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $("[class='notification__content']").shouldHave(text("Встреча успешно забронирована на " + generationDatePlusDay(7)));


    }
}