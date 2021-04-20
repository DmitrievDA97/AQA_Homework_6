package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV1;
import ru.netology.web.page.RefillPage;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selectors.byText;

class MoneyTransferTest {
    @BeforeEach
    void setup() {
      open("http://localhost:9999");
      val loginPage = new LoginPageV1();
      val authInfo = DataHelper.getAuthInfo();
      val verificationPage = loginPage.validLogin(authInfo);
      val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
      verificationPage.validVerify(verificationCode);

    }

    @AfterEach
    void setupDefaults() {
        val dashboardPage = new DashboardPage();
        int firstCardBalance = dashboardPage.getCardBalance(DataHelper.firstCard().getCardId());
        int secondCardBalance = dashboardPage.getCardBalance(DataHelper.secondCard().getCardId());
        int difference;
        if (firstCardBalance > secondCardBalance) {
            difference = firstCardBalance - secondCardBalance;
            val refillPage = dashboardPage.topUpCard(2);
            refillPage.moneyTransfer(String.valueOf(difference/2), DataHelper.firstCard().getCardNumber());
        }
        else if (firstCardBalance < secondCardBalance) {
            difference = secondCardBalance - firstCardBalance;
            val refillPage = dashboardPage.topUpCard(1);
            refillPage.moneyTransfer(String.valueOf(difference/2), DataHelper.secondCard().getCardNumber());
        }
    }
    @Test
    void shouldRefillFirstCard() {
        val dashboardPage = new DashboardPage();
        int transferAmount = 500;
        int expectedFirstCardBalance = dashboardPage.getCardBalance(DataHelper.firstCard().getCardId()) + transferAmount;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(DataHelper.secondCard().getCardId()) - transferAmount;
        val refillPage = dashboardPage.topUpCard(1);
        refillPage.moneyTransfer(String.valueOf(transferAmount), DataHelper.secondCard().getCardNumber());
        int firstCardBalance = dashboardPage.getCardBalance(DataHelper.firstCard().getCardId());
        int secondCardBalance = dashboardPage.getCardBalance(DataHelper.secondCard().getCardId());
        assertEquals(expectedFirstCardBalance, firstCardBalance);
        assertEquals(expectedSecondCardBalance, secondCardBalance);
    }

    @Test
    void shouldRefillSecondCard() {
        val dashboardPage = new DashboardPage();
        int transferAmount = 5000;
        int expectedSecondCardBalance = dashboardPage.getCardBalance(DataHelper.secondCard().getCardId()) + transferAmount;
        int expectedFirstCardBalance = dashboardPage.getCardBalance(DataHelper.firstCard().getCardId()) - transferAmount;
        val refillPage = dashboardPage.topUpCard(2);
        refillPage.moneyTransfer(String.valueOf(transferAmount), DataHelper.firstCard().getCardNumber());
        int secondCardBalance = dashboardPage.getCardBalance(DataHelper.secondCard().getCardId());
        int firstCardBalance = dashboardPage.getCardBalance(DataHelper.firstCard().getCardId());
        assertEquals(expectedSecondCardBalance, secondCardBalance);
        assertEquals(expectedFirstCardBalance, firstCardBalance);

    }

    @Test
    void shouldRefillAmountExceedingBalance() {
        val dashboardPage = new DashboardPage();
        int transferAmount = 11000;
        val refillPage = dashboardPage.topUpCard(2);
        refillPage.moneyTransfer(String.valueOf(transferAmount), DataHelper.firstCard().getCardNumber());
        refillPage.error();
    }
}

