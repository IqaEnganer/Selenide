import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public LocalDate getDateOfMeetingInLocalDate(int days) {
        return LocalDate.now().plusDays(days);
    }

    public String plusDay = generationDatePlusDay(3);
    public String minusDay = generationDateMinusDay(3);


    @BeforeEach
    void setup() {
        Configuration.headless = true;
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


    public String getMonthOfMeetingInRussian(String monthStrInEnglish) {
        String[] engMonths = {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        String[] ruMonths = {
                "Январь ", "Февраль ", "Март ", "Апрель ", "Май ", "Июнь ",
                "Июль ", "Август ", "Сентябрь ", "Октябрь ", "Ноябрь ", "Декабрь "};
        for (
                int t = 0;
                t < engMonths.length; t++) {
            if (monthStrInEnglish.contains(engMonths[t])) {
                monthStrInEnglish = monthStrInEnglish.replace(engMonths[t], ruMonths[t]);
                break;
            }
        }
        String monthOfMeetingInRussian = monthStrInEnglish;
        return monthOfMeetingInRussian;
    }


    @Test
    void shouldShowDropDownListDatesAndTheOptionSelect0() {
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        // Получение дня встречи и перевод в String
        Integer dayOfMeeting = getDateOfMeetingInLocalDate(29).getDayOfMonth();
        String dayOfMeetingStr = String.valueOf(dayOfMeeting);
        // Получение месяца встречи и перевод в String
        Month monthOfMeeting = getDateOfMeetingInLocalDate(30).getMonth();
        String monthStr = monthOfMeeting.toString();
        // Перевод месяца встречи на руссккий язык
        String monthStrInRussian = getMonthOfMeetingInRussian(monthStr);
        // Получение года встречи и перевод в String
        Integer year = getDateOfMeetingInLocalDate(7).getYear();
        String yearStr = String.valueOf(year);
        // Нажать на календарь
        $("[data-test-id=date]").click();
        // Убедиться, что календарь виден и предлагает текущий месяц
        $(".calendar__name").shouldBe(visible).shouldHave(exactText(monthStrInRussian + yearStr));
        // Кликнуть конкретный день
        Month currentMonth = LocalDate.now().getMonth();
        if (currentMonth == monthOfMeeting) {
            $$(".calendar__day").findBy(text(dayOfMeetingStr)).click();
        } else {
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
            $$(".calendar__day").findBy(text(dayOfMeetingStr)).click();
        }
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[class='button button_view_extra button_size_m button_theme_alfa-on-white']").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $("div.notification__content").shouldHave(exactText("Встреча успешно забронирована на " + getMonthOfMeetingInRussian("ru")));


    }

}