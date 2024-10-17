package org.example;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.example.constants.Pages.*;

public class Task6Test {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        SelenideLogger.addListener("AllureSSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }
    /*
    Перейти на страницу Drag and Drop. Перетащить элемент A на элемент B.
    Задача на 10 баллов – сделать это, не прибегая к методу DragAndDrop();
    Проверить, что элементы поменялись местами
     */
    @Test
    void dragAndDropTest() {
        open(baseUrl + DRAG_AND_DROP);
        ElementsCollection draggableElements = $$x("//div[@draggable=\"true\"]");
        Actions actions = new Actions(getWebDriver());

        draggableElements.get(0).should(Condition.text("A"));
        draggableElements.get(1).should(Condition.text("B"));

        actions.clickAndHold(draggableElements.get(0)).moveToElement(draggableElements.get(1)).release().perform();

        draggableElements.get(0).should(Condition.text("B"));
        draggableElements.get(1).should(Condition.text("A"));
    }
    /*
    Перейти на страницу Context menu.
    Нажать правой кнопкой мыши на отмеченной области и проверить, что JS Alert имеет ожидаемый текст.
     */
    @Test
    void contextMenuJSAlertHasTextTest() {
        open(baseUrl + CONTEXT_MENU);
        SelenideElement hot_spot = $x("//div[@id=\"hot-spot\"]");
        Actions actions = new Actions(getWebDriver());

        actions.contextClick(hot_spot).perform();
        Alert alert = Selenide.switchTo().alert();
        Assertions.assertEquals("You selected a context menu", alert.getText());
    }

    /*
    Перейти на страницу Infinite Scroll.
    Проскролить страницу до текста «Eius», проверить, что текст в поле зрения.
     */
    @Test
    void infiniteScrollToTextEiusTest() {
        open(baseUrl + INFINITE_SCROLL);
        Actions actions = new Actions(getWebDriver());
        Action action = actions.scrollByAmount(0, 500).build();
        ElementsCollection texts = $$x("//div[@class='jscroll-added']");
        int i = 0;

        while (true) {
            if (i < texts.size()) {
                if (texts.get(i).getText().contains("Eius")) {
                    texts.get(i).should(Condition.visible);
                    break;
                }
                i++;
            } else {
                action.perform();
            }
        }
    }

    /*
    Перейти на страницу Key Presses. Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
    Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.
     */
    @TestFactory
    List<DynamicTest> keyPressesTextIsVisibleTest() {
        open(baseUrl + KEY_PRESSES);
        Actions actions = new Actions(getWebDriver());
        SelenideElement result = $x("//p[@id=\"result\"]");
        List<DynamicTest> tests = new ArrayList<>();

        List<String> alphabetKeys = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");

        alphabetKeys.forEach(key -> {
            tests.add(DynamicTest.dynamicTest("Input key: " + key, () -> {
                actions.sendKeys(key).perform();
                sleep(1000);
                result.should(Condition.visible).should(Condition.text("You entered: " + key));
            }));
        });

        List<Keys> specialKeys = Arrays.asList(Keys.ENTER, Keys.CONTROL, Keys.ALT, Keys.TAB);

        specialKeys.forEach(key -> {
            tests.add(DynamicTest.dynamicTest("Input key: " + key.name(), () -> {
                actions.sendKeys(key).perform();
                sleep(1000);
                result.should(Condition.visible).should(Condition.text("You entered: " + key.name()));
            }));
        });

        return tests;
    }

    @AfterEach
    void tearDown() {
        getWebDriver().quit();
    }
}
