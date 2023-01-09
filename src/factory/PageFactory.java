package factory;

import pages.*;

public class PageFactory {

    public static GeneralPage createPage(String pageType) {
        switch (pageType) {
            case "HomepageNotAuthentified": return HomepageNotAuthentified.getInstance();
            case "HomepageAuthentified": return HomepageAuthentified.getInstance();
            case "Login" : return Login.getInstance();
            case "Logout" : return Logout.getInstance();
            case "Movies" : return Movies.getInstance();
            case "Register" : return Register.getInstance();
            case "SeeDetails" : return SeeDetails.getInstance();
            case "Upgrades": return Upgrades.getInstance();
        }
        return null;
    }
}
