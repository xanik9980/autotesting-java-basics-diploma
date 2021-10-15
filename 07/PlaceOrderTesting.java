import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class PlaceOrderTesting { 

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

    // Купон
    private By showCouponLinkLocator = By.cssSelector(".showcoupon");
    private By couponInputLocator = By.id("coupon_code");
    private By applyCouponButtonLocator = By.name("apply_coupon");

    // Инпуты
    private By firstNameInputLocator = By.id("billing_first_name");
    private By lastNameInputLocator = By.id("billing_last_name");
    private By addressInputLocator = By.id("billing_address_1");
    private By cityInputLocator = By.id("billing_city");
    private By regionInputLocator = By.id("billing_state");
    private By postCodeInputLocator = By.id("billing_postcode");
    private By phoneInputLocator = By.id("billing_phone");
    private By commentAreaLocator = By.id("order_comments");
    private By emailInputLocator = By.id("billing_email");

    // Итоговая таблица
    private By productNameLocator = By.cssSelector("td.product-name");
    private By totalSumAllProductsLocator = By.cssSelector(".cart-subtotal bdi"); // цена всех товаров "Общая сумма"
    private By saleLocator = By.cssSelector(".cart-discount .amount"); // 500
    private By toPayLocator = By.cssSelector(".order-total bdi");
    private By removeCouponLinkLocator = By.cssSelector(".woocommerce-remove-coupon");

    // Оплата и оформление
    private By payAfterDeliveryLocator = By.id("payment_method_cod");
    private By placeOrderButtonLocator = By.cssSelector(".alt");

    // Ошибки
    private By alertLocator = By.cssSelector("[role = 'alert']");
    private By alertNameLocator = By.cssSelector("[role = 'alert'] [data-id='billing_first_name']");
    private By alertSurnameLocator = By.cssSelector("[role = 'alert'] [data-id='billing_last_name']");
    private By alertAddressLocator = By.cssSelector("[role = 'alert'] [data-id='billing_address_1']");
    private By alertCityLocator = By.cssSelector("[role = 'alert'] [data-id='billing_city']");
    private By alertRegionLocator = By.cssSelector("[role = 'alert'] [data-id='billing_state']");
    private By alertPostCodeLocator = By.cssSelector("[role = 'alert'] [data-id='billing_postcode']");
    private By alertPhoneLocator = By.cssSelector("[role = 'alert'] [data-id='billing_phone'] strong");

    // Страница "Заказ получен"
    private By postTitleLocator = By.cssSelector(".post-title");
    private By orderNumberLocator = By.cssSelector(".order strong");
    private By orderDateLocator = By.cssSelector(".date strong");
    private By orderEmailLocator = By.cssSelector(".email strong");
    private By orderSumLocator = By.cssSelector(".total bdi");
    private By orderMethodLocator = By.cssSelector(".method strong");

    // Добавление в корзину
    private By menuCatalogLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Каталог']");
    int productNum = 3;
    private By productToCartButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .add_to_cart_button");
    private By productMoreButtonLocator = By.cssSelector(".product:nth-of-type(" + productNum + ") .added_to_cart");
    private By checkoutButtonLocator = By.cssSelector(".checkout-button"); // кнопка оформление заказа
    private By totalPriceInCartLocator = By.cssSelector("[data-title = 'Сумма'] bdi"); // общая стоимость в корзине
    private By delCouponInCartLink = By.cssSelector(".woocommerce-remove-coupon"); // удалить купон в корзине

    ///
    private By toLoginButtonLocator = By.name("login");
    private By menuMyAccountLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Мой аккаунт']");
    private By nameInputLocator = By.name("username");
    private By passwordInputLocator = By.name("password");
    private By loaderLocator = By.cssSelector(".processing");

    String loginPage = "http://intershop5.skillbox.ru/my-account/";
    String email = "test2809@test.com";
    String method = "";
    String coupon = "sert500";

    @Test
    public void testOrderValidData() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();

        driver.findElement(firstNameInputLocator).sendKeys("Иван");
        driver.findElement(lastNameInputLocator).sendKeys("Петров");
        driver.findElement(addressInputLocator).sendKeys("Ленина 1");
        driver.findElement(cityInputLocator).sendKeys("Екатеринбург");
        driver.findElement(regionInputLocator).sendKeys("Свердловская");
        driver.findElement(postCodeInputLocator).sendKeys("620000");
        driver.findElement(phoneInputLocator).sendKeys("+79001234567");
        driver.findElement(commentAreaLocator).sendKeys("Привезите до обеда");
        driver.findElement(payAfterDeliveryLocator).click();
        method = "Оплата при доставке";
        String sum = driver.findElement(toPayLocator).getText();

        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertEquals("Переход на страницу итогов заказа не произошёл", "Заказ получен", driver.findElement(postTitleLocator).getText());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.y");
        String today = (dtf.format(LocalDateTime.now()));
        Assert.assertEquals("Дата в итогах заказа неправильная", today, driver.findElement(orderDateLocator).getText());
        Assert.assertEquals("Почта в итогах заказа неправильная", email, driver.findElement(orderEmailLocator).getText());
        Assert.assertEquals("Сумма в итогах заказа неправильная", sum, driver.findElement(orderSumLocator).getText());
        Assert.assertEquals("Метод доставки в итогах заказа неправильный", method, driver.findElement(orderMethodLocator).getText());

    }

    @Test
    public void testEmailInInput() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        Assert.assertEquals("email в инпуте заполняется неверно", email, driver.findElement(emailInputLocator).getAttribute("value"));
    }

    @Test
    public void testOrderPrice() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        String totalPriceInCart = driver.findElement(totalPriceInCartLocator).getText();
        driver.findElement(checkoutButtonLocator).click();

        Assert.assertEquals("Общая цена при оформлении заказа != таковой в корзине", totalPriceInCart, driver.findElement(toPayLocator).getText());
    }

    @Test
    public void testEnterCouponOnPlaceOrder() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        if (!driver.findElements(delCouponInCartLink).isEmpty()) {
            driver.findElement(delCouponInCartLink).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        }
        driver.findElement(checkoutButtonLocator).click();
        String totalPrice = driver.findElement(toPayLocator).getText();
        driver.findElement(showCouponLinkLocator).click();
        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        String expectedTotalPrice = String.valueOf(priceToDouble(totalPrice) - priceToDouble(driver.findElement(saleLocator).getText()));
        String actualTotalPrice = String.valueOf(priceToDouble(driver.findElement(toPayLocator).getText()));
        Assert.assertEquals("Скидка рассчиталась неправильно", expectedTotalPrice, actualTotalPrice);
    }

    @Test
    public void testEnterAppliedCouponOnPlaceOrder() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();

        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        driver.findElement(checkoutButtonLocator).click();

        String totalPrice = driver.findElement(toPayLocator).getText();
        driver.findElement(showCouponLinkLocator).click();
        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        String expectedTotalPrice = String.valueOf(priceToDouble(totalPrice));
        String actualTotalPrice = String.valueOf(priceToDouble(driver.findElement(toPayLocator).getText()));
        Assert.assertEquals("Скидка рассчиталась неправильно", expectedTotalPrice, actualTotalPrice);
        Assert.assertFalse("Алерт с сообщением об ошибке не появился", driver.findElements(alertLocator).isEmpty());
    }

    @Test
    public void testDeleteCouponLink() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();

        driver.findElement(couponInputLocator).sendKeys(coupon);
        driver.findElement(applyCouponButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();
        String totalPrice = driver.findElement(totalSumAllProductsLocator).getText();

        driver.findElement(removeCouponLinkLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        String expectedTotalPrice = String.valueOf(priceToDouble(totalPrice));
        String actualTotalPrice = String.valueOf(priceToDouble(driver.findElement(toPayLocator).getText()));
        Assert.assertEquals("Скидка не отменилась", expectedTotalPrice, actualTotalPrice);
    }

    @Test
    public void testProductNameAndQty() {

        By productNameInCartLocator = By.cssSelector("[data-title = 'Товар'] a");
        By productQtyLocator = By.cssSelector(".qty");

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        String productName = driver.findElement(productNameInCartLocator).getText();
        String productQty = driver.findElement(productQtyLocator).getAttribute("value");
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();
        Assert.assertEquals("Имя товара при оформлении неправильное", productName + "  × " + productQty, driver.findElement(productNameLocator).getText());

    }


    @Test
    public void testOrderEmptyData() {

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();

        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertFalse("Сообщение об ошибке в поле Имя не появилось", driver.findElements(alertNameLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Фамилия не появилось", driver.findElements(alertSurnameLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Адрес не появилось", driver.findElements(alertAddressLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Город не появилось", driver.findElements(alertCityLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Область не появилось", driver.findElements(alertRegionLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Почтовый индекс не появилось", driver.findElements(alertPostCodeLocator).isEmpty());
        Assert.assertFalse("Сообщение об ошибке в поле Телефон не появилось", driver.findElements(alertPhoneLocator).isEmpty());

    }

    @Test
    public void testNotValidPhone() {

        By alertPhoneLocator = By.xpath("//li[@data-id = 'billing_phone']/strong/..");

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();
        driver.findElement(firstNameInputLocator).sendKeys("Иван");
        driver.findElement(lastNameInputLocator).sendKeys("Петров");
        driver.findElement(addressInputLocator).sendKeys("Ленина 1");
        driver.findElement(cityInputLocator).sendKeys("Екатеринбург");
        driver.findElement(regionInputLocator).sendKeys("Свердловская");
        driver.findElement(postCodeInputLocator).sendKeys("620000");
        driver.findElement(phoneInputLocator).sendKeys("wqe$3");
        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertFalse("Алерт с сообщением об ошибке не появился", driver.findElements(alertLocator).isEmpty());
        Assert.assertEquals("Текст ошибки неправильный", "Телефон для выставления счета не валидный номер телефона.", driver.findElement(alertPhoneLocator).getText());
    }

    @Test
    public void testNotValidEmail() {

        By alertEmailLocator = By.cssSelector("[role = 'alert'] li");

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        clearInputs();
        driver.findElement(firstNameInputLocator).sendKeys("Иван");
        driver.findElement(lastNameInputLocator).sendKeys("Петров");
        driver.findElement(addressInputLocator).sendKeys("Ленина 1");
        driver.findElement(cityInputLocator).sendKeys("Екатеринбург");
        driver.findElement(regionInputLocator).sendKeys("Свердловская");
        driver.findElement(postCodeInputLocator).sendKeys("620000");
        driver.findElement(phoneInputLocator).sendKeys("+79001234567");
        driver.findElement(emailInputLocator).clear();
        driver.findElement(emailInputLocator).sendKeys("testtest.com");
        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertFalse("Алерт с сообщением об ошибке не появился", driver.findElements(alertLocator).isEmpty());
        Assert.assertEquals("Текст ошибки неправильный", "Invalid billing email address", driver.findElement(alertEmailLocator).getText());

        // Пустой email
        driver.findElement(emailInputLocator).clear();
        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertFalse("Алерт с сообщением об ошибке не появился", driver.findElements(alertLocator).isEmpty());
        Assert.assertEquals("Текст ошибки неправильный", "Адрес почты для выставления счета обязательное поле.", driver.findElement(alertEmailLocator).getText());
    }

    @Test
    public void testOrderNotValidData() { // тест валится, т.к. на сайте баг - можно разместить заказ с невалидными данными в полях

        driver.navigate().to(loginPage);
        loginTestUser();

        driver.findElement(menuCatalogLinkLocator).click();
        driver.findElement(productToCartButtonLocator).click();
        driver.findElement(productMoreButtonLocator).click();
        driver.findElement(checkoutButtonLocator).click();

        driver.findElement(firstNameInputLocator).clear();
        driver.findElement(lastNameInputLocator).clear();
        driver.findElement(addressInputLocator).clear();
        driver.findElement(cityInputLocator).clear();
        driver.findElement(regionInputLocator).clear();
        driver.findElement(postCodeInputLocator).clear();
        driver.findElement(phoneInputLocator).clear();

        driver.findElement(firstNameInputLocator).sendKeys("#$ds");
        driver.findElement(lastNameInputLocator).sendKeys("!!$5");
        driver.findElement(addressInputLocator).sendKeys("#$5");
        driver.findElement(cityInputLocator).sendKeys("^&*");
        driver.findElement(regionInputLocator).sendKeys("sdsd$$");
        driver.findElement(postCodeInputLocator).sendKeys("sds");
        driver.findElement(phoneInputLocator).sendKeys("+79001234567");

        driver.findElement(placeOrderButtonLocator).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loaderLocator));

        Assert.assertFalse("Заказ создался с невалидными данными", driver.findElement(postTitleLocator).getText().equals("Заказ получен"));
    }



    public void loginTestUser() {

        String name = "test2809";
        String password = "111";

        driver.findElement(menuMyAccountLinkLocator).click();
        driver.findElement(nameInputLocator).sendKeys(name);
        driver.findElement(passwordInputLocator).sendKeys(password);
        driver.findElement(toLoginButtonLocator).click();
    }

    public void clearInputs() {

        driver.findElement(firstNameInputLocator).clear();
        driver.findElement(lastNameInputLocator).clear();
        driver.findElement(addressInputLocator).clear();
        driver.findElement(cityInputLocator).clear();
        driver.findElement(regionInputLocator).clear();
        driver.findElement(postCodeInputLocator).clear();
        driver.findElement(phoneInputLocator).clear();
    }

    public static double priceToDouble(String price) {

        int L = price.length();
        return Double.valueOf(price.substring(0, L - 4) + "." + price.substring(L - 3, L - 1));
    }



}