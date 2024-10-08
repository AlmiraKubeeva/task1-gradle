package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.example.constants.Pages.*;

public class UiTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
    }

    /*
    Перейти на страницу Checkboxes.
    Выделить первый чекбокс, снять выделение со второго чекбокса.
    Вывести в консоль состояние атрибута checked для каждого чекбокса.
     */
    @Test
    void checkboxesChangeBothValuesTest() {
        open(baseUrl + CHECKBOXES);
        ElementsCollection checkboxes = $$x("//input");
        checkboxes.get(0).click();

        System.out.printf("Value of checkbox 1 is %b.%n", checkboxes.get(0).getAttribute("checked"));
        System.out.printf("Value of checkbox 2 is %b.%n", checkboxes.get(1).getAttribute("checked"));

        checkboxes.get(1).click();

        System.out.printf("Value of checkbox 1 is %b.%n", checkboxes.get(0).getAttribute("checked"));
        System.out.printf("Value of checkbox 2 is %b.%n", checkboxes.get(1).getAttribute("checked"));
    }

    /*
    Перейти на страницу Dropdown.
    Выбрать первую опцию, вывести в консоль текущий текст элемента dropdown,
    выбрать вторую опцию, вывести в консоль текущий текст элемента dropdown.
     */
    @Test
    void dropdownPrintAllOOptionsTextTest() {
        open(baseUrl + DROPDOWN);
        ElementsCollection options = $$x("//option");
        SelenideElement dropdown = $x("//select[@id='dropdown']");
        options.get(1).click();
        System.out.printf("Current text of dropdown element is %s.%n", dropdown.getText());

        options.get(2).click();
        System.out.printf("Current text of dropdown element is %s.%n", dropdown.getText());
    }

    /*
    Перейти на страницу Disappearing Elements.
    Добиться отображения 5 элементов, максимум за 10 попыток, если нет, провалить тест с ошибкой.
     */
    @Test
    void disappearingElementsShow5ElementsTest() {
         open(baseUrl + DISAPPEARING_ELEMENTS);
         ElementsCollection elements = $$x("//*[@id=\"content\"]//li");
         int countOfTrying = 10;

         while (countOfTrying > 0 && elements.size() != 5) {
             System.out.println(countOfTrying);
             countOfTrying--;
             open(baseUrl + DISAPPEARING_ELEMENTS);
         }
        assert countOfTrying != 0 || elements.size() == 5;
    }

    /*
    Перейти на страницу Inputs.
    Ввести любое случайное число от 1 до 10 000.
    Вывести в консоль значение элемента Input.
     */
    @Test
    void inputShowNumberTest() {
        open(baseUrl + INPUTS);
        SelenideElement input = $x("//input");
        input.sendKeys("5");
        System.out.println(input.getAttribute("value"));
    }

    /*
    Перейти на страницу Hovers.
    Навести курсор на каждую картинку.
    Вывести в консоль текст, который появляется при наведении.
     */
    @Test
    void hoversPrintTextFromPictureTest() {
        open(baseUrl + HOVERS);
        ElementsCollection figures = $$x("//div[@class=\"figure\"]");
        for (int i = 0; i < 3; ++i) {
            figures.get(i).hover();
            System.out.printf("Image %d:\n", i + 1);
            System.out.println(figures.get(i).getText());
        }
    }

    /*
    Перейти на страницу Notification Message.
    Кликать до тех пор, пока не покажется уведомление Action successful.
    После каждого неудачного клика закрывать всплывающее уведомление.
     */
    @Test
    void notificationMessagesTest() {
        open(baseUrl + NOTIFICATION_MESSAGE);
        SelenideElement notice = $x("//div[@class=\"flash notice\"]");

        while (notice.getText().compareTo("Action successful\n" + "×") != 0) {
            notice.find("a").click();
            $x("//a[text()=\"Click here\"]").click();
       }
    }

    /*
    Перейти на страницу Add/Remove Elements.
    Нажать на кнопку Add Element 5 раз. С каждым нажатием выводить в консоль текст появившегося элемента.
    Нажать на разные кнопки Delete три раза.
    Выводить в консоль оставшееся количество кнопок Delete и их тексты.
     */
    @Test
    void addElementsTest() {
        open(baseUrl + ADD_REMOVE_ELEMENTS);
        SelenideElement addElementButton = $x("//button[text()=\"Add Element\"]");
        ElementsCollection deleteButtons = $$x("//div[@id=\"elements\"]/*");

        for (int i = 0; i < 5; ++i) {
            addElementButton.click();
            System.out.println(deleteButtons.get(i).getText());
        }

        deleteButtons.get(4).click();
        deleteButtons.get(0).click();
        deleteButtons.get(2).click();

        System.out.println("Count of buttons: " + deleteButtons.size());
        System.out.println($x("//div[@id=\"elements\"]").getText());

    }

    /*
    Перейти на страницу Status Codes.
    Кликнуть на каждый статус в новом тестовом методе, вывести на экран текст после перехода на страницу статуса.
     */
    SelenideElement textElement = $x("//div[@class=\"example\"]");
    @Test
    void click200StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"200\"]").click();
        System.out.println(textElement.getText());
    }

    @Test
    void click301StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"301\"]").click();
        System.out.println(textElement.getText());
    }

    @Test
    void click404StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"404\"]").click();
        System.out.println(textElement.getText());
    }

    @Test
    void click500StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"500\"]").click();
        System.out.println(textElement.getText());
    }

    @AfterEach
    void tearDown() {
        getWebDriver().quit();
    }
}
