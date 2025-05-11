public class Main {
  public static void main(String[] args) {
    // TesterModule tester = new TesterModule();

    // Initialize bugs
    // tester.initBug("Login Error", "UI", "High", "P1", "ProjectX");
    // tester.initBug("Slow Performance", "Performance", "Medium", "P2",
    //                "ProjectY");
    //
    // // Assign a bug
    // tester.assignBugToDev("Login Error", "John Doe");
    //
    // // Attach screenshot
    // tester.attachScreenshotForBug("Login Error",
    //                               "/screenshots/login_error.png");
    //
    // // Mail developer
    // tester.mailDev("Login Error", "Jane Tester");
    //
    // // Monitor bugs
    // tester.monitorBugs();
    //
    // // Close a bug
    // tester.closeBug("Login Error");
    //
    // // Monitor again
    // tester.monitorBugs();

    // new tester  doing his testing process and found bug reported it and got
    // email adn screenshot of it
    TesterModule tester2 = new TesterModule("Hacker", "Hackora@gmail.com");
    tester2.initBug("UI Bug", "UI", "High", "P1", "ProjectX",
                    "/screenshots/ui_bug.png");
    tester2.initBug("UI Bug2", "UI", "High", "P1", "ProjectX",
                    "/screenshots/ui_bug.png");
    tester2.initBug("UI Bug3", "UI", "High", "P1", "ProjectX",
                    "/screenshots/ui_bug.png");
    tester2.initBug("UI Bug4", "UI", "High", "P1", "ProjectX",
                    "/screenshots/ui_bug.png");
    tester2.getName();
    tester2.getEmail();
    tester2.attachScreenshotForBug("UI Bug", "/screenshots/ui_bug.pnglalalal");
    // tester2.mailDev("UI Bug", "Hacker",
    // "/screenshots/ui_bug.png","Dev@g.com"); // WATING FOR KHALED
    tester2.RecieveMail("Dev", "moreInfo", "UI Bug", "/screenshots/ui_bug.png");
    // print tester data as whole in here
    System.out.println(tester2.getAllData());
    System.out.println("All Bugs:");
    for (Bug bug : tester2.getBugs()) {
      System.out.println(bug); // Uses your custom toString()
    }
  }
}
