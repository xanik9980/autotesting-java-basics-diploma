import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatalogTesting {

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

    private By withoutCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Без категории']");
    private By withoutCatQtyLocator = By.xpath("//a[.= 'Без категории']/following-sibling::span");
    private By appliancesCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Бытовая техника']");
    private By appliancesCatQtyLocator = By.xpath("//a[.= 'Бытовая техника']/following-sibling::span");
    private By booksCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Книги']");
    private By booksCatQtyLocator = By.xpath("//a[.= 'Книги']/following-sibling::span");
    private By clothesCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Одежда']");
    private By clothesCatQtyLocator = By.xpath("//a[.= 'Одежда']/following-sibling::span");
    private By tabsCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Планшеты']");
    private By tabsCatQtyLocator = By.xpath("//a[.= 'Планшеты']/following-sibling::span");
    private By washCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Стиральные машины']");
    private By washCatQtyLocator = By.xpath("//a[.= 'Стиральные машины']/following-sibling::span");
    private By tvCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Телевизоры']");
    private By tvCatQtyLocator = By.xpath("//a[.= 'Телевизоры']/following-sibling::span");
    private By phonesCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Телефоны']");
    private By phonesCatQtyLocator = By.xpath("//a[.= 'Телефоны']/following-sibling::span");
    private By photoCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Фото/видео']");
    private By photoCatQtyLocator = By.xpath("//a[.= 'Фото/видео']/following-sibling::span");
    private By refrigeratorsCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Холодильники']");
    private By refrigeratorsCatQtyLocator = By.xpath("//a[.= 'Холодильники']/following-sibling::span");
    private By watchCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Часы']");
    private By watchCatQtyLocator = By.xpath("//a[.= 'Часы']/following-sibling::span");
    private By electronicsCatLocator = By.xpath("//ul[@class = 'product-categories']//a[.= 'Электроника']");
    private By electronicsCatQtyLocator = By.xpath("//a[.= 'Электроника']/following-sibling::span");

    private By catTitleLocator =  By.cssSelector(".entry-title");
    private By productLocator = By.cssSelector(".product");
    private By nextPageButtonLocator = By.cssSelector(".next");
    private By searchInputLocator = By.cssSelector(".search-field");
    private By itemNameInBasketLocator =  By.cssSelector(".summary .product_title"); // - название товара на карточке товара

    String site = "http://intershop5.skillbox.ru/product-category/catalog/";

    @Test
    public void testCategoriesLinks() {

        driver.navigate().to(site);
        driver.findElement(withoutCatLocator).click();
        Assert.assertEquals("Ссылка Без категории ведёт не на ту страницу", "БЕЗ КАТЕГОРИИ", driver.findElement(catTitleLocator).getText());
        driver.findElement(appliancesCatLocator).click();
        Assert.assertEquals("Ссылка Бытовая техника ведёт не на ту страницу", "БЫТОВАЯ ТЕХНИКА", driver.findElement(catTitleLocator).getText());
        driver.findElement(booksCatLocator).click();
        Assert.assertEquals("Ссылка Книги ведёт не на ту страницу", "КНИГИ", driver.findElement(catTitleLocator).getText());
        driver.findElement(clothesCatLocator).click();
        Assert.assertEquals("Ссылка Одежда ведёт не на ту страницу", "ОДЕЖДА", driver.findElement(catTitleLocator).getText());
        driver.findElement(tabsCatLocator).click();
        Assert.assertEquals("Ссылка Планшеты ведёт не на ту страницу", "ПЛАНШЕТЫ", driver.findElement(catTitleLocator).getText());
        driver.findElement(washCatLocator).click();
        Assert.assertEquals("Ссылка Стиральные машины ведёт не на ту страницу", "СТИРАЛЬНЫЕ МАШИНЫ", driver.findElement(catTitleLocator).getText());
        driver.findElement(tvCatLocator).click();
        Assert.assertEquals("Ссылка Телевизоры ведёт не на ту страницу", "ТЕЛЕВИЗОРЫ", driver.findElement(catTitleLocator).getText());
        driver.findElement(phonesCatLocator).click();
        Assert.assertEquals("Ссылка Телефоны ведёт не на ту страницу", "ТЕЛЕФОНЫ", driver.findElement(catTitleLocator).getText());
        driver.findElement(photoCatLocator).click();
        Assert.assertEquals("Ссылка Фото/видео ведёт не на ту страницу", "ФОТО/ВИДЕО", driver.findElement(catTitleLocator).getText());
        driver.findElement(refrigeratorsCatLocator).click();
        Assert.assertEquals("Ссылка Холодильники ведёт не на ту страницу", "ХОЛОДИЛЬНИКИ", driver.findElement(catTitleLocator).getText());
        driver.findElement(watchCatLocator).click();
        Assert.assertEquals("Ссылка Часы ведёт не на ту страницу", "ЧАСЫ", driver.findElement(catTitleLocator).getText());
        driver.findElement(electronicsCatLocator).click();
        Assert.assertEquals("Ссылка Электроника ведёт не на ту страницу", "ЭЛЕКТРОНИКА", driver.findElement(catTitleLocator).getText());

    }

    @Test
    public void testCategoriesQuantities () {

        int expectedQty = -1;
        String qty = "";

        driver.navigate().to(site);
        driver.findElement(withoutCatLocator).click();
        qty = driver.findElement(withoutCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Без категории указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(appliancesCatLocator).click();
        qty = driver.findElement(appliancesCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Бытовая техника указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(booksCatLocator).click();
        qty = driver.findElement(booksCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Книги указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(clothesCatLocator).click();
        qty = driver.findElement(clothesCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Одежда указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(tabsCatLocator).click();
        qty = driver.findElement(tabsCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Планшеты указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(washCatLocator).click();
        qty = driver.findElement(washCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Стиральные машины указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(tvCatLocator).click();
        qty = driver.findElement(tvCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Телевизоры указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(phonesCatLocator).click();
        qty = driver.findElement(phonesCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Телефоны указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(photoCatLocator).click();
        qty = driver.findElement(photoCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Фото/видео указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(refrigeratorsCatLocator).click();
        qty = driver.findElement(refrigeratorsCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Холодильники указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(watchCatLocator).click();
        qty = driver.findElement(watchCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));
        Assert.assertEquals("Кол-во товаров Часы указано неверно", expectedQty, driver.findElements(productLocator).size());

        driver.findElement(electronicsCatLocator).click();
        qty = driver.findElement(electronicsCatQtyLocator).getText();
        expectedQty = Integer.valueOf(qty.substring(1, qty.length() - 1));

        for (int i = 0; i <= (expectedQty - 1) / 12; i++) { // по-идее в данный for нужно обернуть каждый assert, но сейчас у всех категорий 1 страница товаров
            if (i > 0) driver.findElement(nextPageButtonLocator).click();
            int expectedQtyOnPage = i < (expectedQty - 1) / 12 ? 12 : expectedQty % 12;
            Assert.assertEquals("Кол-во товаров Электроника указано неверно", expectedQtyOnPage, driver.findElements(productLocator).size());
        }

    }

    @Test
    public void testCatalogQuantities() {

        driver.navigate().to(site);
        By resultQtyLocator = By.cssSelector(".woocommerce-result-count");
        String resultQty = driver.findElement(resultQtyLocator).getText();
        ArrayList<Integer> quantities = qtyStrIntoList(resultQty);

        int expectedQty = quantities.get(2);
        for (int i = 0; i <= (expectedQty - 1) / 12; i++) {
            if (i > 0) driver.findElement(nextPageButtonLocator).click();

            resultQty = driver.findElement(resultQtyLocator).getText();
            quantities = qtyStrIntoList(resultQty);

            int expectedQtyOnPage = i < (expectedQty - 1) / 12 ? 12 : expectedQty % 12;
            Assert.assertEquals("Кол-во товаров в каталоге указано неверно", expectedQtyOnPage, driver.findElements(productLocator).size());
            Assert.assertEquals("Начальное кол-во товаров указано неверно", Integer.valueOf(i * 12 + 1), quantities.get(0));
            Assert.assertEquals("Конечное кол-во товаров указано неверно", i < (expectedQty - 1) / 12 ? Integer.valueOf(i * 12 + 12) : quantities.get(2), quantities.get(1));

        }
    }

    @Test
    public void testSearchGoodsByPartName() {

        var searchingText = "samsung";

        driver.navigate().to(site);
        driver.findElement(searchInputLocator).sendKeys(searchingText);
        driver.findElement(searchInputLocator).sendKeys(Keys.ENTER);

        for (int i = 1; i <= driver.findElements(productLocator).size(); i++) {
            By productNLocator = By.cssSelector(".product:nth-of-type(" + i + ")");
            Assert.assertTrue("Нашёлся неправильный товар по части названия", driver.findElement(productNLocator).getText().toLowerCase(Locale.ROOT).contains(searchingText.toLowerCase(Locale.ROOT)));
        }
    }

    @Test
    public void testSearchGoodsByPartNameRus() {

        var searchingText = "Фото";

        driver.navigate().to(site);
        driver.findElement(searchInputLocator).sendKeys(searchingText);
        driver.findElement(searchInputLocator).sendKeys(Keys.ENTER);

        for (int i = 1; i <= driver.findElements(productLocator).size(); i++) {
            By productNLocator = By.cssSelector(".product:nth-of-type(" + i + ")");
            Assert.assertTrue("Нашёлся неправильный товар по части названия", driver.findElement(productNLocator).getText().toLowerCase(Locale.ROOT).contains(searchingText.toLowerCase(Locale.ROOT)));
        }
    }

    @Test
    public void testSearchGoodsByName() {

        var searchingText = "appLe WATch 6";

        driver.navigate().to(site);
        driver.findElement(searchInputLocator).sendKeys(searchingText);
        driver.findElement(searchInputLocator).sendKeys(Keys.ENTER);

        Assert.assertTrue("Переход на карточку искомого товара не произошёл", driver.findElement(itemNameInBasketLocator).getText().toLowerCase(Locale.ROOT).contains(searchingText.toLowerCase(Locale.ROOT)));
    }

    @Test
    public void testSearchGoodsNotExists() {

        By infoLocator = By.cssSelector(".woocommerce-info");
        var searchingText = "plane";

        driver.navigate().to(site);
        driver.findElement(searchInputLocator).sendKeys(searchingText);
        driver.findElement(searchInputLocator).sendKeys(Keys.ENTER);
        Assert.assertEquals("Ошибка \"По вашему запросу товары не найдены\" не отображается", "По вашему запросу товары не найдены.", driver.findElement(infoLocator).getText());
    }

    @Test
    public void testFollowToCart() {

        int randomNum = (int) (1 + 8. * Math.random());
        By randomProductNameLocator = By.cssSelector(".product:nth-of-type(" + randomNum + ") h3");
        By randomProductToCartButtonLocator = By.cssSelector(".product:nth-of-type(" + randomNum + ") .add_to_cart_button");
        By randomProductMoreButtonLocator = By.cssSelector(".product:nth-of-type(" + randomNum + ") .added_to_cart");

        driver.navigate().to(site);
        var goodName = driver.findElement(randomProductNameLocator).getText();
        driver.findElement(randomProductToCartButtonLocator).click();
        driver.findElement(randomProductMoreButtonLocator).click();

        By goodNameInCartLocator = By.cssSelector("[data-title = 'Товар'] a");
        Assert.assertEquals("", goodName, driver.findElement(goodNameInCartLocator).getText());

    }


    public static ArrayList<Integer> qtyStrIntoList (String qtyStr) {

        ArrayList<Integer> list = new ArrayList<>();
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qtyStr);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            list.add(Integer.valueOf(qtyStr.substring(start, end)));
        }
        return list;
    }

}