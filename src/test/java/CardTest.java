import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CardTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldCardValid() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов Иван");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(Condition.visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно забронирована на " + corDate));
    }

    @Test
    void shouldCardWithDef() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов-Васильев Иван");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(Condition.visible, ofSeconds(15)).shouldHave(exactText("Встреча успешно забронирована на " + corDate));
    }

    @Test
    void shouldCardWithWrongCity() {
        $("[placeholder=\"Город\"]").setValue("Стокгольм");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов-Васильев Иван");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"city\"]").getText();
        Assertions.assertEquals("Доставка в выбранный город недоступна", text.trim());
    }

    @Test
    void shouldCardWithWrongDate() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов-Васильев Иван");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"date\"]").getText();
        Assertions.assertEquals("Заказ на выбранную дату невозможен", text.trim());
    }

    @Test
    void shoudlCardWithWrongName() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Amazing 2");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"name\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldCardWithWrongPhone() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов Иван");
        $("[name=\"phone\"]").setValue("89999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"phone\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldCardWithoutName() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"name\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldCardWithoutPhone() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иван Иванов");
        $("[name=\"phone\"]").setValue("");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"phone\"].input_invalid .input__sub").getText();
        Assertions.assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldCardWithoutDate() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        $("[name=\"name\"]").setValue("Иван Иванов");
        $("[name=\"phone\"]").setValue("+79999999999");
        $("[data-test-id=\"agreement\"]").click();
        $(".button").click();
        String text = $("[data-test-id=\"date\"]").getText();
        Assertions.assertEquals("Неверно введена дата", text.trim());
    }

    @Test
    void shouldCardWithoutCheckBox() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        String corDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE + corDate);
        $("[name=\"name\"]").setValue("Иванов Иван");
        $("[name=\"phone\"]").setValue("+79999999999");
        $(".button").click();
        String text = $("[data-test-id=\"agreement\"]").getText();
        Assertions.assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text.trim());
    }

}
