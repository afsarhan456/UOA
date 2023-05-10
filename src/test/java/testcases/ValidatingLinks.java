package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.CommonMethods;
import utils.ConfigReader;
import utils.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ValidatingLinks extends CommonMethods {


    // Open Browser and Launch the application
    @BeforeMethod
    public void setup() {
        openBrowser();
    }

    @Test
    public void validateLinks() {
        // List of elements for Need Help?
        List<WebElement> linkList = driver.findElements(By.xpath("//div[contains(@class, 'help')]/child::a[@target='_self']"));
        FileWriter csvWriter = null;
        try {
            //Open CSV File for writing
            csvWriter= new FileWriter(Constants.CSVFILE_PATH);
            csvWriter.append("Link Text,Expected URL,Actual URL,Pass/Fail"+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Loop through all the links associated with Need Help?
        for(int i=0; i< linkList.size(); i++) {
            // Getting each element form the list of WebElements
            WebElement link = linkList.get(i);
            // Getting the link text
            String linkText = link.getText();

            // Getting Expected URL from DOM
            String expectedUrl = link.getAttribute("href");

            // Check Link is clickable and click on the link
            if(link.isDisplayed() && link.isEnabled()){
                click(link);
            } else {
                System.out.println("Link is not Clickable");
            }

            // getting the Actual URL
            String actualUrl = driver.getCurrentUrl();
            boolean result = true;
            String passFail = "";

            // Verify that the actual URL matches the expected URL
            if(actualUrl.equalsIgnoreCase(expectedUrl)) {
                result = true;

                if(result == true) {
                    passFail = "Pass";
                }
            } else {
                passFail = "Fail";
            }
            try {
                // Appending and storing the results into the CSV file
                csvWriter.append(linkText +"," + expectedUrl + ","+ actualUrl + "," + passFail + "\n");

                // Navigate back to the original Page
                driver.navigate().back();
            } catch (IOException e) {
                e.printStackTrace();
            }}

        try {
            // Flush data to create CSV file data
            csvWriter.flush();

            //Close CSV File upon successfully adding the data
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close the browser
        driver.quit();
    }


}
