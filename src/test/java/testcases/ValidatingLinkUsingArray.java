package testcases;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.CommonMethods;
import utils.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidatingLinkUsingArray extends CommonMethods {

    String browser = "chrome";
    String url = "https://apps.ualberta.ca/";

    @BeforeMethod
    // Open Browser and Launch the application
    public void setup(){
        openBrowser();
    }

    @Test
    public void validateLinksUsingArray(){
        // List of elements for Need Help?
        List<WebElement> linkList = driver.findElements(By.xpath("//div[contains(@class, 'help')]/child::a[@target='_self']"));


        CSVWriter writer = null;
        try {
            // Initializing CSVWriter class and defining path to store CSV file
            writer = new CSVWriter(new FileWriter(Constants.CSVFILE_PATH01));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Initializing List to store data into CSV File
        List<String[]> list = new ArrayList<>();

        //Header Row
        String[] header = {"Link Text","Expected URL", "Actual URL", "Results"};
        list.add(header);

        // Declaring String array to store data
        String[] values;

        // Loop through all the links associated with Need Help?
        for (int i = 0; i < linkList.size(); i++) {
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
            if (actualUrl.equalsIgnoreCase(expectedUrl)) {
                result = true;
                if (result == true) {
                    passFail = "Pass";
                }
            } else {
                passFail = "Fail";
            }

            // Storing the data in to array
            values = new String[]{linkText, expectedUrl, actualUrl, passFail};

            // Adding data to List<String[]>
            list.add(values);

            // Navigate back to the original Page
            driver.navigate().back();
        }

        // write the results to the CSV File
        writer.writeAll(list);

        try {
            // Flush data to create CSV file data
            writer.flush();

            //Close CSV File upon successfully adding the data
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the browser
        closeBrowser();
    }



}
