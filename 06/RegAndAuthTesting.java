import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public class RegAndAuthTesting {

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

    private By accountButtonLocator = By.cssSelector(".account");
    private By titleLocator =  By.cssSelector(".entry-title");
    private By registerButtonLocator = By.cssSelector(".custom-register-button");
    private By nameInputLocator = By.name("username");
    private By emailInputLocator = By.name("email");
    private By passwordInputLocator = By.name("password");
    private By toRegisterButtonLocator = By.name("register");
    private By toLoginButtonLocator = By.name("login");
    private By regDoneMsgLocator = By.cssSelector(".content-page");
    private By helloNameLocator = By.cssSelector(".user-name");
    private By helloNameOnMyAccountLocator = By.xpath("//p[contains(text(), 'Привет')]//strong");
    private By logoutButtonLocator = By.cssSelector(".logout");
    private By menuMyAccountLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Мой аккаунт']");
    private By alertMsgLocator = By.cssSelector("[role = 'alert'] li");

    String site = "http://intershop5.skillbox.ru/";
    String testName = "test2809";
    String testEmail = "test2809@test.com";
    String testPassword = "111";

    @Test
    public void testFollowRegForms() { // переход на страницы регистрации и авторизации

        driver.navigate().to(site);
        driver.findElement(accountButtonLocator).click();
        Assert.assertEquals("Переход на страницу Мой аккаунт не произошёл", "Мой аккаунт", driver.findElement(titleLocator).getText());
        driver.findElement(registerButtonLocator).click();
        Assert.assertEquals("Переход на страницу регистрации не произошёл", "Регистрация", driver.findElement(titleLocator).getText());
    }

    @Test
    public void testRegistrationNewUser() {

        String name = "TesteR" + (int)(10000 + 90000 * Math.random());
        String email = "tEsts" + (int)(10000 + 90000 * Math.random()) + "@test.com";
        String password = "qwe1234!";

        driver.navigate().to(site);
        driver.manage().window().maximize();
        regRandomUser(name, email, password);

        Assert.assertEquals("Ошибка при регистрации с валидными данными", "Регистрация завершена", driver.findElement(regDoneMsgLocator).getText());
        Assert.assertEquals("Сайт приветствует не того юзера", name, driver.findElement(helloNameLocator).getText().trim());
        driver.findElement(helloNameLocator).click();
        Assert.assertEquals("Страница Мой аккаунт приветствует не того юзера", name, driver.findElement(helloNameOnMyAccountLocator).getText());
        driver.findElement(logoutButtonLocator).click();
        Assert.assertEquals("Кнопка Войти после выхода не появилась", "Войти", driver.findElement(accountButtonLocator).getText());
        Assert.assertTrue("Выход из аккаунта не произошёл", driver.findElements(helloNameLocator).isEmpty());

    }

    @Test
    public void testAuthRegisteredUser() {

        driver.navigate().to(site);
        driver.manage().window().maximize();
        loginUser(testName, testPassword);

        Assert.assertFalse("Не произошёл переход в ЛК", driver.findElements(helloNameOnMyAccountLocator).isEmpty());
        Assert.assertEquals("Страница Мой аккаунт приветствует не того юзера", testName, driver.findElement(helloNameOnMyAccountLocator).getText());
        Assert.assertEquals("Сайт приветствует не того юзера", testName, driver.findElement(helloNameLocator).getText().trim());

        driver.findElement(logoutButtonLocator).click();
        loginUser(testEmail, testPassword);

        Assert.assertFalse("Не произошёл переход в ЛК - email", driver.findElements(helloNameOnMyAccountLocator).isEmpty());
        Assert.assertEquals("Страница Мой аккаунт приветствует не того юзера - email", testName, driver.findElement(helloNameOnMyAccountLocator).getText());
        Assert.assertEquals("Сайт приветствует не того юзера - email", testName, driver.findElement(helloNameLocator).getText().trim());

    }

    @Test
    public void testForgotForm() {

        By forgotPasswordLinkInAlertLocator = By.cssSelector("[role = 'alert'] a");
        By forgotFormTitleLocator = By.cssSelector(".post-title");
        By inputLocator = By.id("user_login");
        By resetButtonLocator = By.cssSelector("[value = 'Reset password']");
        By alertLocator = By.cssSelector("[role = 'alert']");
        String password = "sdjfhuksdf";

        driver.navigate().to(site);
        loginUser(testName, password);
        driver.findElement(forgotPasswordLinkInAlertLocator).click();
        Assert.assertEquals("Переход на форму восстановления пароля не произошёл", "Восстановление пароля", driver.findElement(forgotFormTitleLocator).getText());

        driver.findElement(inputLocator).sendKeys(testName);
        driver.findElement(resetButtonLocator).click();
        String msg = "Password reset email has been sent.";
        Assert.assertFalse("Сообщение о сбросе не появилось", driver.findElements(alertLocator).isEmpty());
        Assert.assertEquals("Текст сообщения о сбросе неправильный", msg, driver.findElement(alertLocator).getText().trim());
    }

    @Test
    public void testRegLongName() {

        String name = "testertestertestertester";
        String email = "tests@test.com";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser(name, email, password);

        String error = "Error: Максимальное допустимое количество символов: 20";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegLongEmail() {

        String name = "tester541561481";
        String email = "teststestertestertester@test.com";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser(name, email, password);

        String error = "Error: Максимальное допустимое количество символов: 20";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegWrongName() {

        String name = "Вася";
        String email = "tests@test.com";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser(name, email, password);

        String error = "Error: Пожалуйста введите корректное имя пользователя.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegEmptyName() {

        String email = "tests@test.com";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser("", email, password);

        String error = "Error: Пожалуйста введите корректное имя пользователя.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegWrongEmail() {

        String name = "tester45185148";
        String email = "test@test";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser(name, email, password);

        String error = "Error: Пожалуйста, введите корректный email.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegEmptyEmail() {

        String name = "tester";
        String password = "qwe1234!";

        driver.navigate().to(site);
        regRandomUser(name, "", password);

        String error = "Error: Пожалуйста, введите корректный email.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegLongPassword() {

        String name = "tester54561418";
        String email = "tests@test.com";
        String password = "123456789012345678901";

        driver.navigate().to(site);
        regRandomUser(name, email, password);

        String error = "Error: Максимальное допустимое количество символов: 20";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testRegEmptyPassword() {

        String name = "tester7484";
        String email = "tests@test.com";

        driver.navigate().to(site);
        regRandomUser(name, email, "");

        String error = "Error: Введите пароль для регистрации.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }


    @Test
    public void testAuthRegisteredUserWrongPassword() {

        String wrongPassword = "000";

        driver.navigate().to(site);
        driver.manage().window().maximize();
        loginUser(testName, wrongPassword);

        String error = "Веденный пароль для пользователя " + testName + " неверный. Забыли пароль?";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

        driver.findElement(menuMyAccountLinkLocator).click();
        loginUser(testEmail, wrongPassword);
        
        error = "Введенный пароль для почты " + testEmail + " неверный. Забыли пароль?";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testAuthNotRegisteredName() {

        String name = "hjsdgfsdhud";
        String password = "1234";

        driver.navigate().to(site);
        loginUser(name, password);

        String error = "Неизвестное имя пользователя. Попробуйте еще раз или укажите адрес почты.";
        Assert.assertEquals("Сообщение об ошибке Неизвестное имя пользователя не появилось", error, driver.findElement(alertMsgLocator).getText().trim());

    }

    @Test
    public void testAuthNotRegisteredEmail() {

        String email = "shfsdfsd@tsdfsdf.com";
        String password = "1234";

        driver.navigate().to(site);
        loginUser(email, password);

        String error = "Неизвестный адрес почты. Попробуйте еще раз или введите имя пользователя.";
        Assert.assertEquals("Сообщение об ошибке Неизвестный адрес почты не появилось", error, driver.findElement(alertMsgLocator).getText().trim());
    }

    @Test
    public void testAuthLongName() {

        String name = "hjsdgjhgkjsdhfksdhfsdffsdhud";
        String password = "1234";

        driver.navigate().to(site);
        loginUser(name, password);

        String error = "Максимальное допустимое количество символов: 20.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());
    }

    @Test
    public void testAuthEmptyName() {

        String password = "1234";

        driver.navigate().to(site);
        loginUser("", password);

        String error = "Error: Имя пользователя обязательно.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());
    }

    @Test
    public void testAuthEmptyPassword() {

        String name = "hjsdgjhhud";

        driver.navigate().to(site);
        loginUser(name, "");

        String error = "Пароль обязателен.";
        Assert.assertEquals("Сообщение об ошибке " + error + " не появилось", error, driver.findElement(alertMsgLocator).getText().trim());
    }




    public void regRandomUser(String name, String email, String password) {

        driver.findElement(accountButtonLocator).click();
        driver.findElement(registerButtonLocator).click();

        driver.findElement(nameInputLocator).sendKeys(name);
        driver.findElement(emailInputLocator).sendKeys(email);
        driver.findElement(passwordInputLocator).sendKeys(password);
        driver.findElement(toRegisterButtonLocator).click();
    }

    public void loginUser(String nameOrEmail, String password) {

        driver.findElement(menuMyAccountLinkLocator).click();
        driver.findElement(nameInputLocator).sendKeys(nameOrEmail);
        driver.findElement(passwordInputLocator).sendKeys(password);
        driver.findElement(toLoginButtonLocator).click();
    }


}