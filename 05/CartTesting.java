import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class CartTesting {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 5);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); // неявное ожидание появления э-та в ДОМ дереве
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private By goodNameInCartLocator = By.cssSelector("[data-title = 'Товар'] a");
    private By goodQtyLocator = By.cssSelector(".qty");
    private By goodPriceLocator = By.cssSelector("[data-title = 'Цена'] bdi");
    private By goodTotalPriceLocator = By.cssSelector("[data-title = 'Общая стоимость'] bdi");
    private By totalPriceLocator = By.cssSelector("[data-title = 'Сумма'] bdi");
    private By checkoutButtonLocator = By.cssSelector(".checkout-button");
    private By applyCouponButtonLocator = By.cssSelector("[name = 'apply_coupon']");

    int productNum = 2;
    private By productNameLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") h3");
    private By productPriceLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") bdi");
    private By productToCartButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .add_to_cart_button");
    private By productMoreButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .added_to_cart");
    private By loaderLocator = By.cssSelector(".processing");
    private By alertLocator = By.cssSelector("[role = 'alert']");
    private By alertMsgLocator = By.cssSelector("[role = 'alert'] li");
    private By couponInputLocator = By.name("coupon_code");

    String catalog = "http://intershop5.skillbox.ru/product-category/catalog/";

    @Test
    public void testAddToCart() { // 1 товар

        driver.navigate().to(catalog);
        String goodName = driver.findElement(productNameLocator).getText();
        double goodCatPrice = priceToDouble(driver.findElement(productPriceLocator).getText());
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();

        double price = priceToDouble(driver.findElement(goodPriceLocator).getText());
        int qty = Integer.valueOf(driver.findElement(goodQtyLocator).getAttribute("value"));
        double expectedTotalPrice = price * qty;
        double totalPrice = priceToDouble(driver.findElement(goodTotalPriceLocator).getText());
        double sum = priceToDouble(driver.findElement(totalPriceLocator).getText());

        Assert.assertEquals("Название товара в корзине != добавленному", goodName, driver.findElement(goodNameInCartLocator).getText());
        Assert.assertEquals("Цена товара в корзине != каталожной", String.valueOf(goodCatPrice), String.valueOf(price));
        Assert.assertEquals("Общая цена товара рассчитывается неверно", String.valueOf(expectedTotalPrice), String.valueOf(totalPrice));
        Assert.assertEquals("Сумма к оплате рассчитывается неверно", String.valueOf(totalPrice),String.valueOf(sum));

    }

    @Test
    public void testAddSeveralToCart() { // несколько товаров

        driver.navigate().to(catalog);
        double sumPriceFromCat = 0;
        for (int productNum = 2; productNum <= 4; productNum++) {

            productPriceLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") bdi");
            productToCartButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .add_to_cart_button");
            sumPriceFromCat += priceToDouble(driver.findElement(productPriceLocator).getText());
            driver.findElement(productToCartButtonLocator).click();
            if (productNum == 4) {
                productMoreButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .added_to_cart");
                driver.findElement(productMoreButtonLocator).click();
            }
        }

        double sumPrice = 0;
        for (int i = 1; i <= 3; i++) {
            goodPriceLocator = By.cssSelector("tr:nth-of-type(" + i + ") [data-title = 'Цена'] bdi");
            sumPrice += priceToDouble(driver.findElement(goodPriceLocator).getText());
        }

        double sum = priceToDouble(driver.findElement(totalPriceLocator).getText());

        Assert.assertEquals("Сумма != сумме каталожных цен", String.valueOf(sumPriceFromCat), String.valueOf(sum));
        Assert.assertEquals("Сумма != сумме цен, указанных в корзине", String.valueOf(sumPrice), String.valueOf(sum));
    }


    @Test
    public void testChangeQty() { // изменить кол-во товара

        driver.navigate().to(catalog);
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();

        double price = priceToDouble(driver.findElement(goodPriceLocator).getText());
        int qty = (int)(2 + 8 * Math.random());
        driver.findElement(goodQtyLocator).sendKeys(Keys.DELETE);
        driver.findElement(goodQtyLocator).sendKeys(String.valueOf(qty));
        driver.findElement(goodQtyLocator).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        double expectedTotalPrice = price * qty;
        double totalPrice = priceToDouble(driver.findElement(goodTotalPriceLocator).getText());
        double sum = priceToDouble(driver.findElement(totalPriceLocator).getText());

        Assert.assertEquals("Общая цена товара рассчитывается неверно", String.valueOf(expectedTotalPrice), String.valueOf(totalPrice));
        Assert.assertEquals("Сумма к оплате рассчитывается неверно", String.valueOf(totalPrice),String.valueOf(sum));

    }

    @Test
    public void testDeleteGood() { // удалить/вернуть товар

        By xLocator = By.cssSelector(".remove");
        By restoreLocator = By.cssSelector("[role = 'alert'] .restore-item");
        By cartEmptyMsgLocator = By.cssSelector(".cart-empty");

        driver.navigate().to(catalog);
        String goodName = driver.findElement(productNameLocator).getText();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(xLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertTrue("Сообщение \"Корзина пуста\" не появилось", driver.findElement(cartEmptyMsgLocator).isDisplayed());
        Assert.assertEquals("Неправильный текст в сообщении о пустой корзине", "Корзина пуста.", driver.findElement(cartEmptyMsgLocator).getText());
        Assert.assertTrue("Ошибка в сообщении об удалении товара", driver.findElement(alertLocator).getText().contains(goodName));
        Assert.assertTrue("Товар не убрался из корзины", driver.findElements(goodNameInCartLocator).size() == 0);

        driver.findElement(restoreLocator).click();
        Assert.assertEquals("Товар не вернулся в корзину", goodName, driver.findElement(goodNameInCartLocator).getText());

    }

    @Test
    public void testApplyCoupon () {

        By delCouponLink = By.cssSelector(".woocommerce-remove-coupon");
        String coupon = "sert500";

        driver.navigate().to(catalog);
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        var expectedSale = priceToDouble(driver.findElement(goodTotalPriceLocator).getText()) - priceToDouble(driver.findElement(totalPriceLocator).getText());
        Assert.assertEquals("Скидка рассчиталась неправильно", "500.0", String.valueOf(expectedSale));

        driver.findElement(delCouponLink).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        Assert.assertEquals("Неправильная цена после удаления купона", driver.findElement(goodTotalPriceLocator).getText(), driver.findElement(totalPriceLocator).getText());
    }

    @Test
    public void testApplyCoupon2times () {

        String coupon = "sert500";

        driver.navigate().to(catalog);
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();

        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        String error = "Coupon code already applied!";
        Assert.assertFalse("Сообщение " + error + " не появилось", driver.findElements(alertMsgLocator).isEmpty());
        Assert.assertEquals("Текст сообщения об ошибке неверный", error, driver.findElement(alertMsgLocator).getText());
        var expectedSale = priceToDouble(driver.findElement(goodTotalPriceLocator).getText()) - priceToDouble(driver.findElement(totalPriceLocator).getText());
        Assert.assertEquals("Скидка рассчиталась неправильно", "500.0", String.valueOf(expectedSale));
    }

    @Test
    public void testApplyCouponNotExists() {

        String coupon = "sert5000";

        driver.navigate().to(catalog);
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(couponInputLocator).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertTrue("Сообщение \"Неверный купон.\" не появилось", driver.findElement(alertLocator).isDisplayed());
        Assert.assertEquals("Неправильный текст в сообщении о неверном купоне", "Неверный купон.", driver.findElement(alertMsgLocator).getText());
        Assert.assertEquals("Цена изменилась после добавления несуществующего купона", driver.findElement(goodTotalPriceLocator).getText(), driver.findElement(totalPriceLocator).getText());
    }

    @Test
    public void testFollowRegOrderNotRegistered() {

        By titleLocator =  By.cssSelector(".entry-title"); // - Заголовок страницы
        By formLoginToggleTextLocator = By.cssSelector(".woocommerce-form-login-toggle .woocommerce-info");
        By formLoginToggleLinkLocator = By.cssSelector(".woocommerce-form-login-toggle .showlogin");
        By loginFormLocator = By.cssSelector(".woocommerce-form");

        driver.navigate().to(catalog);
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();
        Assert.assertEquals("Переход на страницу Оформления заказа не произошёл", "Оформление заказа", driver.findElement(titleLocator).getText());
        Assert.assertTrue("Текст сообщения неверный", driver.findElement(formLoginToggleTextLocator).getText().contains("Зарегистрированы на сайте?"));
        Assert.assertTrue("Текст ссылки неверный", driver.findElement(formLoginToggleLinkLocator).getText().contains("Авторизуйтесь"));
        driver.findElement(formLoginToggleLinkLocator).click();
        Assert.assertFalse("Форма оформления заказа не появилась", driver.findElements(loginFormLocator).isEmpty());

    }



    public static double priceToDouble(String price) {

        int L = price.length();
        return Double.valueOf(price.substring(0, L - 4) + "." + price.substring(L - 3, L - 1));
    }

}
