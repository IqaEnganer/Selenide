import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
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


    @Test
    void shouldShowDropDownListDatesAndTheOptionSelect0() {
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79287775566");
        $("[data-test-id=city] .input__control").setValue("Волгоград");
        // Создание переменной в которой хранится количество дней
        int daysToAdd = 7;
        // Создание переменной для месяца и получение номера месяца
        int defaultMonth = LocalDate.now().plusDays(3).getMonthValue();
        // Создание переменной для планируемого месяца. Получение месяца  (+ daysToAdd дней от текущей даты).
        int planningDateMonth = LocalDate.now().plusDays(daysToAdd).getMonthValue();
        // Создание переменной для перевода планируемого дня в стринг (+ daysToAdd от текущей даты) и его получения
        String dayOfPlanningDate = String.valueOf(LocalDate.now().plusDays(daysToAdd).getDayOfMonth());
        // Создание переменной стринг для планируемой даты (+daysToAdd от текущей) и установка формата
        String planningDate = LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        // Если дефолтный месяц отличается от планируемого, то нажать на кнопку переключения месяца
        $("[data-test-id=date]").click();
        if (!(defaultMonth == planningDateMonth)) {
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        }
        // Найти селектор в котором будет текст назначенный в переменной (dayOfPlanningDate) и кликнуть
        $$("td.calendar__day").find(exactText(dayOfPlanningDate)).click();
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, ofSeconds(11));
        $(".notification__content")
                .shouldBe(visible, Duration.ofSeconds(12))
                .shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }

}