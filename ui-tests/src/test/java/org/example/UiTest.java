package org.example;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.example.constants.Pages.*;

public class UiTest {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        SelenideLogger.addListener("AllureSSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    /*
    4. Перейти на страницу Checkboxes.
    Выделить первый чекбокс, снять выделение со второго чекбокса.
    Вывести в консоль состояние атрибута checked для каждого чекбокса.

    5. Добавить проверки в задание Checkboxes из предыдущей лекции.
    Проверять корректное состояние каждого чекбокса после каждого нажатия на него.
    Запустить тест с помощью @ParametrizedTest, изменяя порядок нажатия на чекбоксы с помощью одного параметра.
     */
    // Метод для клика по чекбоксу и проверки его состояния
    void toggleAndCheckCheckbox(ElementsCollection checkboxes, int index, boolean shouldBeChecked) {
        checkboxes.get(index).click();
        String expectedState = shouldBeChecked ? "true" : "";
        checkboxes.get(index).should(Condition.attribute("checked", expectedState));
    }

    @Step("Тестируем чекбоксы")
    @ParameterizedTest
    @CsvSource({
            "0, true, 1, false", // Клик по первому чекбоксу, затем по второму
            "1, false, 0, true"  // Клик по второму чекбоксу, затем по первому
    })
    void checkboxesChangeBothValuesTest(int firstCheckbox, boolean firstShouldBeChecked,
                                        int secondCheckbox, boolean secondShouldBeChecked) {
        open(baseUrl + CHECKBOXES);
        ElementsCollection checkboxes = $$x("//input");

        toggleAndCheckCheckbox(checkboxes, firstCheckbox, firstShouldBeChecked);

        System.out.printf("Checkbox %d state after first click: %b%n", firstCheckbox,
                checkboxes.get(firstCheckbox).getAttribute("checked") != null);

        toggleAndCheckCheckbox(checkboxes, secondCheckbox, secondShouldBeChecked);

        System.out.printf("Checkbox %d state after second click: %b%n", secondCheckbox,
                checkboxes.get(secondCheckbox).getAttribute("checked") != null);
    }

    /*
    4. Перейти на страницу Dropdown.
    Выбрать первую опцию, вывести в консоль текущий текст элемента dropdown,
    выбрать вторую опцию, вывести в консоль текущий текст элемента dropdown.

    5. Добавить проверки в задание Dropdown из предыдущей лекции.
    Проверять корректное состояние каждого dropDown после каждого нажатия на него.
     */
    void clickAndCheckOptionPrintTextOfDropdown(ElementsCollection options, int optionIndex, SelenideElement dropdown) {
        options.get(optionIndex).click();
        dropdown.should(Condition.text(String.format("Option %d", optionIndex)));
        System.out.printf("Current text of dropdown element is %s.%n", dropdown.getText());
    }

    @Test
    void dropdownPrintAllOOptionsTextTest() {
        open(baseUrl + DROPDOWN);
        ElementsCollection options = $$x("//option");
        SelenideElement dropdown = $x("//select[@id='dropdown']");
        clickAndCheckOptionPrintTextOfDropdown(options, 1, dropdown);

        clickAndCheckOptionPrintTextOfDropdown(options, 2, dropdown);
    }

    /*
    4. Перейти на страницу Disappearing Elements.
    Добиться отображения 5 элементов, максимум за 10 попыток, если нет, провалить тест с ошибкой.

    5. Добавить проверки в задание Disappearing Elements из предыдущей лекции.
    Для каждого обновления страницы проверять наличие 5 элементов. Использовать @RepeatedTest.
     */
    @RepeatedTest(value = 5)
    void disappearingElementsShow5ElementsTest() {
        open(baseUrl + DISAPPEARING_ELEMENTS);
        ElementsCollection elements = $$x("//*[@id=\"content\"]//li");
        int countOfTrying = 10;

        while (countOfTrying > 0 && elements.size() != 5) {
            countOfTrying--;
            refresh();
        }
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
    @TestFactory
    List<DynamicTest> inputShowNumberTest() {
        List<String> validInputs = Arrays.asList("1", "5", "10", "100", "1000", "10000", " 5", "5 ");
        List<String> invalidInputs = Arrays.asList("d", "()");

        List<DynamicTest> tests = new ArrayList<>();

        // Тесты для валидных значений
        validInputs.forEach(value -> {
            tests.add(DynamicTest.dynamicTest("Valid Input: " + value, () -> {
                open(baseUrl + INPUTS);
                SelenideElement input = $x("//input");
                input.sendKeys(value);
                input.should(Condition.attribute("value", value.trim()));
                System.out.println(input.getAttribute("value"));
            }));
        });

        // Тесты для невалидных значений
        invalidInputs.forEach(value -> {
            tests.add(DynamicTest.dynamicTest("Invalid Input: " + value, () -> {
                open(baseUrl + INPUTS);
                SelenideElement input = $x("//input");
                input.sendKeys(value);
                input.shouldNot(Condition.attribute("value", value));
                System.out.println(input.getAttribute("value"));
            }));
        });

        return tests;
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
    @ValueSource(ints={0, 1, 2})
    void hoversPrintTextFromPictureTest(int i) {
        open(baseUrl + HOVERS);
        List<String> expectedTexts = Arrays.asList(
                "name: user1",
                "name: user2",
                "name: user3"
        );
        ElementsCollection figures = $$x("//div[@class=\"figure\"]");
        figures.get(i).hover();
        System.out.printf("Image %d:\n", i + 1);
        System.out.println(figures.get(i).getText());
        figures.get(i).should(Condition.text(expectedTexts.get(i)));
    }

    /*
    4. Перейти на страницу Notification Message.
    Кликать до тех пор, пока не покажется уведомление Action successful.
    После каждого неудачного клика закрывать всплывающее уведомление.

    5. Добавить проверки в задание Notification Message из предыдущей лекции.
    Добавить проверку, что всплывающее уведомление должно быть Successful.
    Если нет – провалить тест. Использовать @RepeatedTest.
     */
    @RepeatedTest(value=5)
    void notificationMessagesTest() {
        open(baseUrl + NOTIFICATION_MESSAGE);
        SelenideElement notice = $x("//div[@class=\"flash notice\"]");

        while (notice.getText().compareTo("Action successful\n" + "×") != 0) {
            notice.find("a").click();
            $x("//a[text()=\"Click here\"]").click();
       }
        notice.should(Condition.innerText("Action successful"));
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
    @TestFactory
    List<DynamicTest> addElementsTest() {
        List<DynamicTest> tests = new ArrayList<>();

        // Динамическое создание тестов с параметрами для различных сценариев
        List<int[]> testParams = Arrays.asList(
                new int[]{2, 1}, // 2 добавления, 1 удаление
                new int[]{5, 2}, // 5 добавлений, 2 удаления
                new int[]{1, 3}  // 1 добавление, 3 удаления
        );

        testParams.forEach(param -> {
            int adds = param[0];
            int deletes = param[1];
            tests.add(DynamicTest.dynamicTest(adds + " adds and " + deletes + " deletes", () -> {
                open(baseUrl + ADD_REMOVE_ELEMENTS);
                SelenideElement addElementButton = $x("//button[text()=\"Add Element\"]");
                ElementsCollection deleteButtons = $$x("//div[@id=\"elements\"]/*");

                for (int i = 0; i < adds; i++) {
                    addElementButton.click();
                    System.out.println("Button added: " + (i + 1));
                }

                deleteButtons.should(CollectionCondition.size(adds));
                System.out.println("Total buttons after adding: " + deleteButtons.size());

                for (int i = 0; i < deletes; i++) {
                    deleteButtons.first().click();
                    deleteButtons.should(CollectionCondition.size(adds - i - 1));
                    System.out.println("Remaining buttons after delete: " + deleteButtons.size());
                }

                System.out.println("Final count of buttons: " + deleteButtons.size());
            }));
        });

        return tests;
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
        textElement.should(Condition.text("This page returned a 200 status code."));
    }

    @Test
    void click301StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"301\"]").click();
        System.out.println(textElement.getText());
        textElement.should(Condition.text("This page returned a 301 status code."));
    }

    @Test
    void click404StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"404\"]").click();
        System.out.println(textElement.getText());
        textElement.should(Condition.text("This page returned a 404 status code."));
    }

    @Test
    void click500StatusCodeTest() {
        open(baseUrl + STATUS_CODES);
        $x("//a[text()=\"500\"]").click();
        System.out.println(textElement.getText());
        textElement.should(Condition.text("This page returned a 500 status code."));
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
