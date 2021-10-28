package Testing;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import static com.sun.deploy.cache.Cache.copyFile;


public class Test1 {
    WebDriver driver;

    @BeforeClass
    public void setUp(){
        System.setProperty("webdriver.chrome.driver","driver/chromedriver.exe");
    }

    @BeforeMethod
    public void openBrowser(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://rozetka.com.ua");
    }

    @Test
    public void Test(){
        //поиск монитора
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement searchElement = driver.findElement(By.xpath("//input[@name='search']"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//video[@id='rz-banner-img']")));
        searchElement.clear();
        searchElement.sendKeys("Монитор");

        //нажатие кнопки "поиск"
        WebElement searchButton = driver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/header/div/div/div/form/button"));
        searchButton.click();

        //нажатие первого товара
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img.ng-lazyloaded")));
        WebElement firstProductElement = driver.findElement(By.xpath("//div[@data-goods-id='250703981']"));
        wait.until(ExpectedConditions.visibilityOfAllElements(firstProductElement));
        firstProductElement.click();

        //нажатие кнопки купить
        wait.until(driver -> driver.findElement(By.xpath("//*[@id=\"#scrollArea\"]/div[1]/div[2]/rz-product-main-info/div[1]/div/ul/li[1]/app-product-buy-btn/app-buy-button/button")).isDisplayed());
        WebElement buyButton = driver.findElement(By.xpath("//*[@id=\"#scrollArea\"]/div[1]/div[2]/rz-product-main-info/div[1]/div/ul/li[1]/app-product-buy-btn/app-buy-button/button"));
        buyButton.click();

        //нажатие кнопки оформить заказ
        wait.until(driver -> driver.findElement(By.xpath("//div[@class='modal__content']")).isDisplayed());
        WebElement buyOrder = driver.findElement(By.linkText("Оформить заказ"));
        buyOrder.click();

        //ожидание загрузки окна
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.checkout-product__picture")));

    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenshot(ITestResult result) {
        if (!result.isSuccess()) {
            try {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                copyFile(scrFile, new File(result.getName() + "[" + LocalDate.now() + "][" + System.currentTimeMillis() + "].png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        driver.quit();
    }

}