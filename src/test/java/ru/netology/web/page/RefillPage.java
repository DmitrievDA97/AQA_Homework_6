package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class RefillPage {
    private SelenideElement amount = $("[data-test-id=amount] input");
    private SelenideElement from = $("[data-test-id=from] input");
    private SelenideElement refill = $("[data-test-id=action-transfer]");

    public RefillPage() {
        amount.shouldBe(Condition.visible);
    }

    public DashboardPage moneyTransfer (String sum, String card) {
        amount.sendKeys(Keys.CONTROL + "A", Keys.DELETE);
        amount.setValue(sum);
        from.sendKeys(Keys.CONTROL + "A", Keys.DELETE);
        from.setValue(card);
        refill.click();
        return new DashboardPage();
    }
    public SelenideElement error() {
        return $(byText("Ошибка")).shouldHave(Condition.visible);
    }

}

