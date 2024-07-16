package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ScreenTest {
    public static void main(String[] args) throws InterruptedException {

        String Dismiss = "//input[@data-action-type=\"DISMISS\"]";
        String search="//input[@placeholder=\"Search Amazon.in\"]";
        String submit="//input[@type=\"submit\"]";
        String verifyAmazonBasic_Result= "//div[@class=\"a-section a-spacing-small a-spacing-top-small\"]//span[contains(.,'\"Amazonbasics\"')]";

        String checkbox = "//span[contains(.,'From Our Brands')]/following::input[@type=\"checkbox\"]/following::span[contains(text(),'Amazon Brands')]";
        String allproductName="//div[@class=\"sg-col-inner\"]//div[@data-component-type=\"s-search-result\"]";

        String productWholePrice = "((//span[contains(text(),'Amazon Basics 20W One-Port USB-C Wall Charger')])[1]/following::span[@class=\"a-offscreen\"])[1]";
        String verifyProductTitle = "//span[@id=\"productTitle\" and contains(.,' Amazon Basics 20W One')]";
        String addCartButton="//input[@name=\"submit.add-to-cart\"]";


        System.setProperty("webdriver.chrome.drivers","C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy("myhttpproxy:3337");

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.amazon.com/");
        driver.manage().window().maximize();
        driver.navigate().refresh();


        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);



        driver.findElement(By.xpath(search)).sendKeys("Amazonbasics");

        driver.findElement(By.xpath(submit)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(verifyAmazonBasic_Result)));



        WebElement amazonBrandCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkbox)));
        amazonBrandCheckbox.click();

        boolean productFound = false;
        String productName = "20W One-Port USB-C Wall Charger with Power Delivery PD for Tablets & Phones (iPhone 15/14/13/12/11/X, iPad, Samsung, and more), non-PPS, 1.81 x 1.73 x 1.09 inches, White";
        String productPrice = "";
        while (!productFound) {
            List<WebElement> products = driver.findElements(By.xpath(allproductName));
            for (WebElement product : products) {
                if (product.getText().contains(productName)) {
                    productPrice = product.findElement(By.xpath(productWholePrice)).getText();
                    product.findElement(By.cssSelector("h2 a")).click();
                    productFound = true;
                    break;
                }
            }
            if (!productFound) {
                WebElement nextPageButton = driver.findElement(By.cssSelector(".s-pagination-next"));
                if (nextPageButton.isEnabled()) {
                    nextPageButton.click();
                    wait.until(ExpectedConditions.stalenessOf(products.get(0)));
                } else {
                    break;
                }
            }
        }

        if (!productFound) {
            System.out.println("Product not found.");
            return;
        }

        WebElement productTitle = driver.findElement(By.xpath(verifyProductTitle));
        assert productTitle.getText().contains(productName);

        WebElement displayedPrice = driver.findElement(By.id("priceblock_ourprice"));
        assert displayedPrice.getText().contains(productPrice);

        Select quantityDropdown = new Select(driver.findElement(By.id("quantity")));
        Random random = new Random();
        int randomQuantity = random.nextInt(quantityDropdown.getOptions().size() - 1) + 1;
        quantityDropdown.selectByIndex(randomQuantity);


        WebElement addToCartButton = driver.findElement(By.xpath(addCartButton));
        addToCartButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-cart-count")));
        WebElement cartCount = driver.findElement(By.id("nav-cart-count"));
        assert cartCount.getText().equals(String.valueOf(randomQuantity));





    }
}
