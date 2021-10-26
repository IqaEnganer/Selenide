import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.impl.Cleanup.of;
import static java.time.Duration.ofSeconds;

public class AppCardDeliveryTest {


    @BeforeEach
    void setup() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        open("http://localhost:9999");

    }

    // Задержка 10 сек
    // Все поля корректно заполнены.
    @Test
    void shouldСompleteRegistration() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        $("[type][placeholder][pattern]").doubleClick();
        $("[type][placeholder][pattern]").sendKeys("BACKSPACE");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));


        // $(:not([]))
    }


    // Не валидное значение поля Город
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInCityField() {
        $("[class='input__control'][type='text']:not([name])").setValue("Сочи");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear, ofSeconds(4));

    }

    // Не валидное значение поля (ФИО)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInNameField() {
        $("[class='input__control'][type='text']:not([name])").setValue("Волгоград");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Ivan Ivanov");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(appear, ofSeconds(4));

    }

    // Не валидное значение в поле (Номер телефона)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInPhoneNumberField0() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+792877755");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible, ofSeconds(2));
    }

    // Пустое поле (Номер телефона)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInPhoneNumberField1() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Поле обязательно для заполнения")).shouldBe(visible, ofSeconds(2));
    }

    // Поле дата с пустым значением
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInDateField0() {
        $("[class='input__control'][type='text']:not([name])").setValue("Краснодар");
        $("[type][placeholder][pattern]").doubleClick();
        $("[type][placeholder][pattern]").sendKeys("BACKSPACE");
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Неверно введена дата")).shouldBe(appear, ofSeconds(3));
    }

    // Не валидные значения в поле (Дата)
    @Test
    void shouldMessageAboutInvalidDataWillBeDisplayedInDateField1() {
        $("[class='input__control'][type='text']:not([name])").setValue("Краснодар");
        $("[type][placeholder][pattern]").doubleClick();
        $("[type][placeholder][pattern]").sendKeys("BACKSPACE");
        LocalDate date = LocalDate.now();
        date = date.minusDays(10);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(appear, ofSeconds(3));
    }

    // Проверка флажка
    @Test
    void shouldShowTheRequiredItemMessageAsAConsentFlag() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[class='button__text']").click();
        $(".input_invalid").shouldBe(appear);
    }

    // Проверка выпадающего списка городов
    @Test
    void showDropDownListAndSelectionOption() {
        $("[class='input__control'][autocomplete='off']").setValue("Мо");
        $(withText("Москва")).click();
        LocalDate date = LocalDate.now();
        date = date.plusDays(3);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));

    }

    // Проверка списка даты
    // Проверка кнопок переключения месяца и года
    // Проверка выбора дня
    @Test
    void shouldShowDropDownListDatesAndTheOptionSelect0() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        $("span.input__box  button").click();
        $("[ class='calendar__arrow calendar__arrow_direction_right']").click();
        $("[class='calendar__arrow calendar__arrow_direction_right calendar__arrow_double']").click();
        $(withText("27")).click();
    }

    // Выбор даты на неделю вперед
    @Test
    void shouldGetOutDateWeekInAdvance() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        $("span.input__box  button").click();
        LocalDate date = LocalDate.now();
        date = date.plusDays(7);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));


    }

    // Проверка текста всплывающего окна, на наличие в нем корректной даты на назначенную встречу.
    @Test
    void ShouldDatesMeetingMustMatchDateEnteredInputFieldForm() {
        $("[class='input__control'][autocomplete='off']").setValue("Краснодар");
        $("[type][placeholder][pattern]").doubleClick();
        $("[type][placeholder][pattern]").sendKeys("BACKSPACE");
        LocalDate date = LocalDate.now();
        date = date.plusDays(4);
        $("[type][placeholder][pattern]").setValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[class='input__control'][name='name']").setValue("Иванов Иван");
        $("[type='tel'][name='phone']").setValue("+79287775566");
        $("[data-test-id='agreement']").click();
        $("[class='button__text']").click();
        sleep(11000);
        $("[class='notification__content']").shouldHave(exactText("Встреча успешно забронирована на " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}