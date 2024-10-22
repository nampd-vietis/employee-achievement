package dev.vietis.nampd.employee.achievement.service;

import dev.vietis.nampd.employee.achievement.model.search.SearchKeyword;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebDriverService {

    public WebDriver setupDriver(SearchKeyword.Device device) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (device == SearchKeyword.Device.SMARTPHONE) {
            // Giả lập mobile
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Pixel 2");
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }

        return new ChromeDriver(options);
    }

    public List<WebElement> getSuggestions(WebDriver driver, By searchBoxSelector, String keyword) {
        WebElement searchBox = driver.findElement(searchBoxSelector);
        searchBox.sendKeys(keyword);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox'] li")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("ul[role='listbox'] li")));

        return driver.findElements(By.cssSelector("ul[role='listbox'] li"));
    }

    public void closeDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}

