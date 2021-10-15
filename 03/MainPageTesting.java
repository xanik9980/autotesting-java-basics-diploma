import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainPageTesting {

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

    // Ссылки главного меню
    private By menuMainLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Главная']");
    private By menuCatalogLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Каталог']");
    private By menuMyAccountLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Мой аккаунт']");
    private By menuBasketLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Корзина']");
    private By menuRegOrderLinkLocator =  By.xpath("//ul[@id = 'menu-primary-menu']//a[.= 'Оформление заказа']");

    private By catTitleLocator =  By.cssSelector(".entry-title"); // - Заголовок страницы
    private By treeLinkLocator =  By.cssSelector(".accesspress-breadcrumb span"); // ссылка в дереве под заголовком

    private By itemNameInBasketLocator =  By.cssSelector(".summary .product_title"); // - название товара на карточке товара
    private By promoBlockLocator = By.cssSelector(".promo-wrap1"); // промо блок на главной

    String site = "http://intershop5.skillbox.ru/";


    @Test
    public void testMainMenuLinks() {

        //arrange
        By treeOthersLinkLocator =  By.cssSelector(".current"); // ссылка в дереве под заголовком - Остальные кроме каталога

        //act-assert
        driver.navigate().to(site);
        driver.findElement(menuCatalogLinkLocator).click();
        Assert.assertEquals("Пункт главного меню Каталог ведёт не туда", "КАТАЛОГ", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка на каталог под заголовком неправильная", "Каталог", driver.findElement(treeLinkLocator).getText());

        driver.findElement(menuMyAccountLinkLocator).click();
        Assert.assertEquals("Пункт главного меню Мой аккаунт ведёт не туда", "Мой аккаунт", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка на Мой аккаунт под заголовком неправильная", "Мой Аккаунт", driver.findElement(treeOthersLinkLocator).getText());

        driver.findElement(menuBasketLinkLocator).click();
        Assert.assertEquals("Пункт главного меню Корзина ведёт не туда", "Корзина", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка на Корзину под заголовком неправильная", "Корзина", driver.findElement(treeOthersLinkLocator).getText());

        driver.findElement(menuMainLinkLocator).click();
        Assert.assertTrue("Пункт главного меню Главная ведёт не туда", driver.findElement(promoBlockLocator).isDisplayed());
    }

    @Test
    public void testHeroTilesLinks() {

        By booksTileLocator =  By.cssSelector("#accesspress_storemo-2 a"); //- Книги
        By tabsTileLocator =  By.cssSelector("#accesspress_storemo-3 a"); //- Планшеты
        By camerasTileLocator =  By.cssSelector("#accesspress_storemo-4 a"); // - Фотоаппараты

        driver.navigate().to(site);
        driver.findElement(booksTileLocator).click();
        Assert.assertEquals("Плитка КНИГИ ведёт не туда", "КНИГИ", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка Книги в дереве под заголовком неправильная", "Книги", driver.findElement(treeLinkLocator).getText());
        driver.findElement(menuMainLinkLocator).click();

        driver.findElement(tabsTileLocator).click();
        Assert.assertEquals("Плитка ПЛАНШЕТЫ ведёт не туда", "ПЛАНШЕТЫ", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка Планшеты в дереве под заголовком неправильная", "Планшеты", driver.findElement(treeLinkLocator).getText());
        driver.findElement(menuMainLinkLocator).click();

        driver.findElement(camerasTileLocator).click();
        Assert.assertEquals("Плитка ФОТОАППАРАТЫ ведёт не туда", "ФОТО/ВИДЕО", driver.findElement(catTitleLocator).getText());
        Assert.assertEquals("Ссылка Фотоаппараты в дереве под заголовком неправильная", "Фото/Видео", driver.findElement(treeLinkLocator).getText());

    }

    @Test
    public void testOnSale() {

        By saleFirstItemLocator =  By.cssSelector("#product1 li[data-slick-index  = '0']"); // - 1-й товар в списке
        By saleFirstItemNameLocator =  By.cssSelector("#product1 li[data-slick-index  = '0'] h3"); // - название 1 товара
        By saleFirstItemStickerLocator =  By.cssSelector("#product1 li[data-slick-index  = '0'] .onsale"); // - 1-й товар в списке, стикер "скидка"

        driver.navigate().to(site);
        Assert.assertEquals("Стикер Скидка! отсутствует или неправильный", "Скидка!", driver.findElement(saleFirstItemStickerLocator).getText());
        var itemName = driver.findElement(saleFirstItemNameLocator).getText().toLowerCase(Locale.ROOT);
        driver.findElement(saleFirstItemLocator).click();
        Assert.assertEquals("Произошёл переход не на тот товар из блока Распродажа", itemName, driver.findElement(itemNameInBasketLocator).getText());

    }

    @Test
    public void testAlreadyInSaleTileLink() {

        // Проверка блока Уже в продаже
        By bigTileLocator = By.cssSelector(".promo-widget-wrap-full .promo-image");
        By promoDescTitleLocator = By.cssSelector(".promo-desc-title");

        driver.navigate().to(site);
        var promoGoodName = driver.findElement(promoDescTitleLocator).getText().toLowerCase(Locale.ROOT);
        driver.findElement(bigTileLocator).click();
        var openedGoodName = driver.findElement(itemNameInBasketLocator).getText().toLowerCase(Locale.ROOT);
        Assert.assertTrue("", openedGoodName.contains(promoGoodName));

    }

    @Test
    public void testNewAdmissions() {

        //Проверка блока Новые поступления
        By newFirstItemLocator = By.cssSelector("#accesspress_store_product-3 li[data-slick-index  = '0']");
        By newFirstItemNameLocator = By.cssSelector("#accesspress_store_product-3 li[data-slick-index  = '0'] h3");
        By newFirstItemStickerLocator = By.cssSelector("#accesspress_store_product-3 li[data-slick-index  = '0'] .label-new");

        driver.navigate().to(site);
        Assert.assertEquals("Стикер Новый! отсутствует или неправильный", "Новый!", driver.findElement(newFirstItemStickerLocator).getText());
        var itemName = driver.findElement(newFirstItemNameLocator).getText().toLowerCase(Locale.ROOT);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(newFirstItemLocator));
        actions.perform();
        driver.findElement(newFirstItemLocator).click();
        Assert.assertEquals("Произошёл переход не на тот товар из блока Новые поступления", itemName, driver.findElement(itemNameInBasketLocator).getText().toLowerCase(Locale.ROOT));
    }

    @Test
    public void testSearchGoodsByName() {

        //Проверка поиска товара по названию
        By searchInputLocator = By.cssSelector(".search-field");
        var searchingText = "ipad air";

        driver.navigate().to(site);
        driver.findElement(searchInputLocator).sendKeys(searchingText);
        driver.findElement(searchInputLocator).sendKeys(Keys.ENTER);
        Assert.assertTrue("Переход на карточку искомого товара не произошёл", driver.findElement(itemNameInBasketLocator).getText().toLowerCase(Locale.ROOT).contains(searchingText));
    }

    @Test
    public void testFooterLinks() {

        //Проверка ссылок в футере
        By footerAllGoodsLocator = By.xpath("//footer//li/a[.= 'Все товары']");
        By footerMainPageLocator = By.xpath("//footer//li/a[.= 'Главная']");
        By footerBasketLocator = By.xpath("//footer//li/a[.= 'Корзина']");
        By footerMyAccountLocator = By.xpath("//footer//li/a[.= 'Мой аккаунт']");
        By footerRegOrderLocator = By.xpath("//footer//li/a[.= 'Оформление заказа']");
        By footerRegisterLocator = By.xpath("//footer//li/a[.= 'Регистрация']");

        driver.navigate().to(site);
        driver.findElement(footerAllGoodsLocator).click();
        Assert.assertEquals("Переход на страницу Все товары не произошёл", "ВСЕ ТОВАРЫ", driver.findElement(catTitleLocator).getText());

        driver.findElement(footerMainPageLocator).click();
        Assert.assertTrue("Переход на главную страницу не произошёл", driver.findElement(promoBlockLocator).isDisplayed());

        driver.findElement(footerBasketLocator).click();
        Assert.assertEquals("Переход на страницу Корзина не произошёл", "Корзина", driver.findElement(catTitleLocator).getText());

        driver.findElement(footerMyAccountLocator).click();
        Assert.assertEquals("Переход на страницу Мой аккаунт не произошёл", "Мой аккаунт", driver.findElement(catTitleLocator).getText());

        driver.findElement(footerRegisterLocator).click();
        Assert.assertEquals("Переход на страницу Регистрация не произошёл", "Регистрация", driver.findElement(catTitleLocator).getText());
    }
}