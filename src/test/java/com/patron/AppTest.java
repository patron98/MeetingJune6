package com.patron;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AppTest
{
    private WebDriver webDriver;


    @Before
    public void setUp() {
        String path = "https://the-internet.herokuapp.com/";
        webDriver = new ChromeDriver();
        webDriver.get(path);
    }

    @After
    public void teardown(){
        webDriver.close();
    }

    @Test
    public void checkLoginSuccessFull(){
        WebElement formAuthenticationLink = webDriver.findElement(By.linkText("Form Authentication"));
        formAuthenticationLink.click();

        WebElement username = webDriver.findElement(By.id("username"));
        WebElement password = webDriver.findElement(By.id("password"));
        WebElement login = webDriver.findElement(By.cssSelector("button[type='submit']"));

        username.sendKeys("tomsmith");
        password.sendKeys("SuperSecretPassword!");
        login.click();

        WebElement successBanner = webDriver.findElement(By.cssSelector("div[class='flash success']"));
        String bannerText = successBanner.getText();

        Assert.assertEquals("The banner text is incorrect", "You logged into a secure area!\n×", bannerText);
    }

    @Test
    public void addElement(){
        WebElement addRemoveElement = webDriver.findElement(By.linkText("Add/Remove Elements"));
        addRemoveElement.click();

        WebElement addButton = webDriver.findElement(By.xpath("//button[@onclick='addElement()']"));
        addButton.click();

        WebElement addedButton = webDriver.findElement(By.className("added-manually"));
        Assert.assertTrue("Button is not displayed", addedButton.isDisplayed());
    }

    @Test
    public void deleteAddedElement(){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMinutes(1));

        webDriver.findElement(By.linkText("Add/Remove Elements")).click();

        WebElement addButton = webDriver.findElement(By.xpath("//button[@onclick='addElement()']"));
        addButton.click();


        WebElement addedButton = webDriver.findElement(By.className("added-manually"));
        addedButton.click();

        wait.until(ExpectedConditions.invisibilityOf(addedButton));

        try{
            addedButton = webDriver.findElement(By.className("added-manually"));
        }catch (NoSuchElementException e){
            Assert.assertTrue(true);
        }
    }
    @Test
    public void slideSlider() {
        webDriver.findElement(By.linkText("Horizontal Slider")).click();

        WebElement slider = webDriver.findElement(By.xpath("//input[@type='range']"));
        Dimension sliderSize = slider.getSize();
        Point sliderLocation = slider.getLocation();
        int slideTo = sliderSize.getWidth();
        int targetPosition = sliderLocation.getX() + slideTo;
        Actions actions = new Actions(webDriver);
        actions.dragAndDropBy(slider, targetPosition, 0).perform();
        String sliderValue = webDriver.findElement(By.id("range")).getText();
        Assert.assertEquals("value is not correct", "5", sliderValue);
    }

    @Test
    public void basicAuth(){
        webDriver.findElement(By.linkText("Basic Auth")).click();

        webDriver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        String success = webDriver.findElement(By.cssSelector("h3")).getText();
        Assert.assertEquals("Basic Auth", success);
    }

    @Test
    public void checkEntryAddIsDisplayed(){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMinutes(1));
        webDriver.findElement(By.linkText("Entry Ad")).click();
        String adText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='modal']//div[@class='modal-body']//p"))).getText();
        String textToCheck = "It's commonly used to encourage a user to take an action (e.g., give their e-mail address to sign up for something or disable their ad blocker).";
        Assert.assertEquals(textToCheck, adText);
    }

    @Test
    public void checkKeyPressed(){
        webDriver.findElement(By.linkText("Key Presses")).click();

        WebElement input = webDriver.findElement(By.xpath("//input[@id='target']"));
        input.sendKeys(Keys.BACK_SPACE);

        String result = webDriver.findElement(By.id("result")).getText();
        String expected = "You entered: BACK_SPACE";
        Assert.assertEquals(expected, result);
    }
}
