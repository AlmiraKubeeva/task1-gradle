package org.example;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.example.constants.Pages.*;

public class UiTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "firefox";
    }

    /*
    4. Перейти на страницу Checkboxes.
    Выделить первый чекбокс, снять выделение со второго чекбокса.
    Вывести в консоль состояние атрибута checked для каждого чекбокса.

    5. Добавить проверки в задание Checkboxes из предыдущей лекции.
    Проверять корректное состояние каждого чекбокса после каждого нажатия на него.
    Запустить тест с помощью @ParametrizedTest, изменяя порядок нажатия на чекбоксы с помощью одного параметра.
     */
    //int n;

    @ParameterizedTest()

    void checkboxesChangeBothValuesTest() {
        open(baseUrl + CHECKBOXES);
        ElementsCollection checkboxes = $$x("//input");


        checkboxes.get(0).click();

        System.out.printf("Value of checkbox 1 is %b.%n", checkboxes.get(0).getAttribute("checked"));
        System.out.printf("Value of checkbox 2 is %b.%n", checkboxes.get(1).getAttribute("checked"));

        checkboxes.get(0).should(Condition.attribute("checked", "true"));

        checkboxes.get(1).click();

        System.out.printf("Value of checkbox 1 is %b.%n", checkboxes.get(0).getAttribute("checked"));
        System.out.printf("Value of checkbox 2 is %b.%n", checkboxes.get(1).getAttribute("checked"));

        checkboxes.get(1).should(Condition.attribute("checked", ""));
    }

    /*
    4. Перейти на страницу Dropdown.
    Выбрать первую опцию, вывести в консоль текущий текст элемента dropdown,
    выбрать вторую опцию, вывести в консоль текущий текст элемента dropdown.

    5. Добавить проверки в задание Dropdown из предыдущей лекции.
    Проверять корректное состояние каждого dropDown после каждого нажатия на него.
     */
    @Test
    void dropdownPrintAllOOptionsTextTest() {
        open(baseUrl + DROPDOWN);
        ElementsCollection options = $$x("//option");
        SelenideElement dropdown = $x("//select[@id='dropdown']");
        options.get(1).click();
        System.out.printf("Current text of dropdown element is %s.%n", dropdown.getText());
        options.get(1).should(Condition.attribute("selected", "true"));
        options.get(2).should(Condition.attribute("selected", ""));

        options.get(2).click();
        System.out.printf("Current text of dropdown element is %s.%n", dropdown.getText());
        options.get(1).should(Condition.attribute("selected", ""));
        options.get(2).should(Condition.attribute("selected", "true"));
    }

    /*
    4. Перейти на страницу Disappearing Elements.
    Добиться отображения 5 элементов, максимум за 10 попыток, если нет, провалить тест с ошибкой.

    5. Добавить проверки в задание Disappearing Elements из предыдущей лекции.
    Для каждого обновления страницы проверять наличие 5 элементов. Использовать @RepeatedTest.
     */
    //@Test
   // @RepeatedTest(value = 10, failureThreshold = 9)
    @RepeatedTest(value = 10)
    void disappearingElementsShow5ElementsTest() {
         open(baseUrl + DISAPPEARING_ELEMENTS);
         ElementsCollection elements = $$x("//*[@id=\"content\"]//li");
         /*int countOfTrying = 10;

         while (countOfTrying > 0 && elements.size() != 5) {
             System.out.println(countOfTrying);
             countOfTrying--;
             open(baseUrl + DISAPPEARING_ELEMENTS);
         }
        assert countOfTrying != 0 || elements.size() == 5;*/
        //assert elements.size() == 5;
        System.out.println("size: " + elements.size());
        elements.should(CollectionCondition.size(5));

    }

    /*
    4. Перейти на страницу Inputs.
    Ввести любое случайное число от 1 до 10 000.
    Вывести в консоль значение элемента Input.

    5. Добавить проверки в задание Inputs из предыдущей лекции.
    Проверить, что в поле ввода отображается именно то число, которое было введено.
    Повторить тест 10 раз, используя @TestFactory, с разными значениями, вводимыми в поле ввода.
    Создать проверку негативных кейсов
    (попытка ввести в поле латинские буквы, спецсимволы, пробел до и после числа).
     */
    @Test
    void inputShowNumberTest() {
        open(baseUrl + INPUTS);
        SelenideElement input = $x("//input");
        input.sendKeys("5");
        System.out.println(input.getAttribute("value"));
        input.should(Condition.attribute("value", "5"));
    }

    /*
    4. Перейти на страницу Hovers.
    Навести курсор на каждую картинку.
    Вывести в консоль текст, который появляется при наведении.

    5. Добавить проверки в задание Hovers из предыдущей лекции.
    При каждом наведении курсора, проверить, что отображаемый текст совпадает с ожидаемым.
    Выполнить тест с помощью @ParametrizedTest, в каждом тесте, указывая на какой элемент наводить курсор
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void hoversPrintTextFromPictureTest() {
        int i;
        open(baseUrl + HOVERS);
        ElementsCollection figures = $$x("//div[@class=\"figure\"]");
        /*
        for (int i = 0; i < 3; ++i) {
            figures.get(i).hover();
            System.out.printf("Image %d:\n", i + 1);
            System.out.println(figures.get(i).getText());
            $$x("//div[@class=\"figcaption\"]").get(i).should(Condition.visible);
        }*/
        figures.get(i).hover();
        System.out.printf("Image %d:\n", i + 1);
        System.out.println(figures.get(i).getText());
        $$x("//div[@class=\"figcaption\"]").get(i).should(Condition.visible);
    }

    /*
    4. Перейти на страницу Notification Message.
    Кликать до тех пор, пока не покажется уведомление Action successful.
    После каждого неудачного клика закрывать всплывающее уведомление.

    5. Добавить проверки в задание Notification Message из предыдущей лекции.
    Добавить проверку, что всплывающее уведомление должно быть Successful.
    Если нет – провалить тест. Использовать @RepeatedTest.
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
    4. Перейти на страницу Add/Remove Elements.
    Нажать на кнопку Add Element 5 раз. С каждым нажатием выводить в консоль текст появившегося элемента.
    Нажать на разные кнопки Delete три раза.
    Выводить в консоль оставшееся количество кнопок Delete и их тексты.

    5. Добавить проверки в задание Add/Remove Elements.
    Проверять, что на каждом шагу остается видимым ожидаемое количество элементов.
    Запустить тест три раза, используя @TestFactory,
    меняя количество созданий и удалений на 2:1, 5:2, 1:3 соответственно.
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
    4. Перейти на страницу Status Codes.
    Кликнуть на каждый статус в новом тестовом методе, вывести на экран текст после перехода на страницу статуса.

    5. Добавить проверки в задание Status Codes.
    Добавить Проверку, что переход был осуществлен на страницу с корректным статусом.
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

    /*
    Добавить в Allure Listener так, чтобы записывались действия с элементами.
    По желанию можно внедрить в код аннотацию @Step.
     */

    @AfterEach
    void tearDown() {
        getWebDriver().quit();
    }
}
