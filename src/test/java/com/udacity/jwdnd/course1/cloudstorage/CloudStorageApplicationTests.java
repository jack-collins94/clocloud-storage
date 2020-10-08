package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	public String baseURL;

	private static final String firstName = "Jack";
	private static final String lastName = "Collins";
	private static final String username = "jack123";
	private static final String password = "abc123";
	private boolean signedUp = false;

	private static final String click = "arguments[0].click();";


	public static WebDriver driver;
	private static WebDriverWait wait;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll(){
		driver.quit();
		driver = null;
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = baseURL = "http://localhost:" + port;
		wait = new WebDriverWait(driver, 15);
	}

	@Test
	public void notLoggedInRedirect(){
		driver.get(baseURL + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}


	@Test
	public void getLoginPage() {
		driver.get(baseURL + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("username", "password");
		Assertions.assertEquals("Invalid username or password", loginPage.getErrorMsg().getText());
	}

	@Test
	public void invalidLogin(){
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);

	}

	private void signup(String fname, String lName, String uName, String pWord){
		if(!signedUp) {
			driver.get(baseURL + "/signup");
			Assertions.assertEquals("Sign Up", driver.getTitle());

			//signup new user
			SignupPage signupPage = new SignupPage(driver);
			signupPage.signup(fname, lName, uName, pWord);
			Assertions.assertEquals("You successfully signed up! Please continue to the login page.", signupPage.getSuccessMsg().getText());
			signedUp=true;
		}
	}

	private void login(String uName, String pWrord){
		driver.get(baseURL + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(uName, pWrord);
	}

	//login, signup and logout test
	@Test
	public void testUserSignupLoginLogout() {
		signup("First Name", "Last Name", "Username", "password");
		login("Username", "password");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	//Notes Tab
	@Test
	public void notesTab(){
		login(username, password);

		NotesPage notesPage = new NotesPage(driver);

		//navigate to note tab and create a new note
		((JavascriptExecutor) driver).executeScript(click, notesPage.getNavNotesTab());

		//create new note
		wait.until(ExpectedConditions.elementToBeClickable(By.id("addNotesButton"))).click();
		((JavascriptExecutor) driver).executeScript(click, notesPage.getAddNoteBtn());

		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getNoteSubmitBtn()));
		notesPage.populateNote("Original note title", "original note description");
		((JavascriptExecutor) driver).executeScript(click, notesPage.getNoteSubmitBtn());

//		//check to see if the note was created correctly
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getNavNotesTab()));
		((JavascriptExecutor) driver).executeScript(click, notesPage.getNavNotesTab());
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getAddNoteBtn()));

		Assertions.assertEquals("Original note title", notesPage.getNoteTitle().getText());
		Assertions.assertEquals("original note description", notesPage.getNoteDescription().getText());

		//edit note
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getEditNoteBtn()));
		((JavascriptExecutor) driver).executeScript(click, notesPage.getEditNoteBtn());


		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getNoteSubmitBtn()));
		Assertions.assertEquals("Original note title",notesPage.getInputTitle().getAttribute("value"));
		Assertions.assertEquals("original note description",notesPage.getInputDescription().getAttribute("value"));
		notesPage.populateNote("Edited note title", "Edited note description");
		((JavascriptExecutor) driver).executeScript(click, notesPage.getNoteSubmitBtn());

		//check to see if the note was edited correctly
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getNavNotesTab()));
		((JavascriptExecutor) driver).executeScript(click, notesPage.getNavNotesTab());
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getAddNoteBtn()));

		Assertions.assertEquals("Edited note title", notesPage.getNoteTitle().getText());
		Assertions.assertEquals("Edited note description", notesPage.getNoteDescription().getText());


		//Delete note
		wait.until(ExpectedConditions.elementToBeClickable(notesPage.getDeleteNoteBtn()));
		((JavascriptExecutor) driver).executeScript(click, notesPage.getDeleteNoteBtn());

		//logout
		((JavascriptExecutor) driver).executeScript(click, notesPage.getLogoutBtn());

	}

	@Test
	//Credentials Tab
	public void credentialsTab(){
		signup(firstName, lastName, username, password);
		login(username, password);

		CredentialsPage credentialsPage = new CredentialsPage(driver);

		//navigate to credentials tab
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getNavCredentialsTab());

		//create new credentials
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getAddCredentialsBtn()));
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getAddCredentialsBtn());

		//fill in credentials
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getCredentialSubmitBtn()));
		credentialsPage.populateCredentials("originalurl.com", "user", "password");
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getCredentialSubmitBtn());

		//check to see if the credential was created correctly
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getNavCredentialsTab()));
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getNavCredentialsTab());
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getAddCredentialsBtn()));

		Assertions.assertEquals("originalurl.com", credentialsPage.getCredentialUrl().getText());
		Assertions.assertEquals("user", credentialsPage.getCredentialUsername().getText());
		Assertions.assertNotEquals("password",credentialsPage.getCredentialPassword().getText());

		//edit credentials
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getEditCredentialBtn()));
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getEditCredentialBtn());


		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getCredentialSubmitBtn()));
		wait.until(ExpectedConditions.visibilityOf(credentialsPage.getInputURL()));
		Assertions.assertEquals("originalurl.com", credentialsPage.getInputURL().getAttribute("value"));
		Assertions.assertEquals("user", credentialsPage.getInputUsername().getAttribute("value"));
		Assertions.assertEquals("password", credentialsPage.getInputPassword().getAttribute("value"));
		credentialsPage.populateCredentials("editedurl.com", "newuser", "anotherPassword");
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getCredentialSubmitBtn());

		//check to see if the note was edited correctly
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getNavCredentialsTab()));
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getNavCredentialsTab());
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getAddCredentialsBtn()));

		Assertions.assertEquals("editedurl.com", credentialsPage.getCredentialUrl().getText());
		Assertions.assertEquals("newuser", credentialsPage.getCredentialUsername().getText());
		Assertions.assertNotEquals("anotherPassword",credentialsPage.getCredentialPassword().getText());

//		//delete credential
		wait.until(ExpectedConditions.elementToBeClickable(credentialsPage.getDeleteCredentialBtn()));
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getDeleteCredentialBtn());

		//logout
		((JavascriptExecutor) driver).executeScript(click, credentialsPage.getLogoutBtn());

	}







}
