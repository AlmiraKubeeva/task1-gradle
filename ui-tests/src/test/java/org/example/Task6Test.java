package org.example;

import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.interactions.Actions;

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
        /*
        Alert alert = new Alert() {
            @Override
            public void dismiss() {

            }

            @Override
            public void accept() {

            }

            @Override
            public String getText() {
                return "";
            }

            @Override
            public void sendKeys(String keysToSend) {

            }
        }

        actions.contextClick().perform();

        actions.getText()
         */
    }

    /*
    Перейти на страницу Infinite Scroll.
    Проскролить страницу до текста «Eius», проверить, что текст в поле зрения.
     */

    @RepeatedTest(value = 10)
    void infiniteScrollToTextEiusTest() {
        open(baseUrl + INFINITE_SCROLL);
        Actions actions = new Actions(getWebDriver());
        ElementsCollection texts = $$x("//div[@class=\"jscroll-added\"]");
        int i = 0;
        while(!texts.get(i).getText().contains("Eius")) {
            actions.scrollByAmount(0, 500).perform();
            sleep(1000);
            texts = $$x("//div[@class=\"jscroll-added\"]");
            ++i;
            //actions.scrollToElement(texts.get(i)).perform();
        }
    }
    /*
    @Test
    void infiniteScrollToTextEiusTest() {
        open(baseUrl + INFINITE_SCROLL);
        Actions actions = new Actions(getWebDriver());
        ElementsCollection texts = $$x("//div[@class=\"jscroll-added\"]/br");
        int i = 0;

        while (true) {
            // Проверяем, что индекс i не выходит за границы списка
            if (i >= texts.size()) {
                // Если текста с "Eius" пока не найдено, скроллим вниз и обновляем коллекцию
                actions.scrollByAmount(0, 1000).perform();
                texts = $$x("//div[@class=\"jscroll-added\"]/br");
            } else if (texts.get(i).getText().contains("Non")) {
                // Если найден нужный текст, выходим из цикла
                break;
            } else {
                // Переходим к следующему элементу
                ++i;
            }
        }

        // Проверка того, что текст с "Eius" теперь в поле зрения
        texts.get(i).scrollTo();
        sleep(10000); // Для наглядности, если нужно
    }
     */
    /*
    Перейти на страницу Key Presses. Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
    Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.
     */
    @Test
    void keyPressesTextIsVisibleTest() {
        open(baseUrl + KEY_PRESSES);

    }

    @AfterEach
    void tearDown() {
        getWebDriver().quit();
    }
}
