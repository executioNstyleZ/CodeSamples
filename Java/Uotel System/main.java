package phase2;

import java.io.BufferedReader;


import javafx.util.*;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.sql.*;
import java.io.*;

public class main {

	private static List<Pair> reservationsInCart = null;
	private static List<Pair> staysInCart = null;
	
	private static Housing housing = null;
	private static Statistics stats = null;
	private static Users user = null;
	private static Reservations reservations = null;
	private static Feedback feedback = null;;
	private static Keywords keywords = null;
	private static Period period = null;
	
	private static Connector con = null;
	
	public static void displayLoginMenu() {
		System.out.println("        Welcome to the Uotel System     ");
		System.out.println("1. Login:");
		System.out.println("2. Register:");
		System.out.println("please enter your choice:");
	}

	public static void displayMainMenu() {
		System.out.println("        Main Menu    ");
		System.out.println("1. Temporary Housing:");
		System.out.println("2. Feedback:");
		System.out.println("3. Two degrees of separation:");
		System.out.println("4. Statistics:");
		System.out.println("5. Administration:");
		System.out.println("6. exit:");
		System.out.println("please enter your choice:");
	}

	public static void displayTemporaryHousingMenu() {
		System.out.println("        Temporary Housing Menu     ");
		System.out.println("1. Reserve TH:");
		System.out.println("2. Record new permanent housing:");
		System.out.println("3. Update existing permanent housing:");
		System.out.println("4. Record stay:");
		System.out.println("5. Make favorite");
		System.out.println("6. Browse temporary housing:");
		System.out.println("7. Back to main menu:");
		System.out.println("please enter your choice:");
	}

	public static void displayFeedbackMenu() {
		System.out.println("        Feedback Menu    ");
		System.out.println("1. Record feedback:");
		System.out.println("2. Assess existing feedback:");
		System.out.println("3. Rate user:");
		System.out.println("4. Show most useful feedback for TH");
		System.out.println("5. Back to main menu:");
		System.out.println("please enter your choice:");
	}

	public static void displayStatisticsMenu() {
		System.out.println("        Statistics Menu    ");
		System.out.println("1. Most popular temporary housing:");
		System.out.println("2. Most expensive temporary housing:");
		System.out.println("3. Most highly rated permanent housing:");
		System.out.println("4. Back to main menu:");
		System.out.println("please enter your choice:");
	}
	public static void displayAdminMenu(){
		System.out.println("        Administrator Menu    ");
		System.out.println("1. Display top most trusted users:");
		System.out.println("2. Display top most useful users:");
		System.out.println("please enter your choice:");
	}

	public static void main(String[] args) {

		
		String choice;
		String username = "";
		String sql = null;
		int c = 0;
 
		List<Pair> reservationsInCart = new ArrayList<Pair>();
		List<Pair> staysInCart = new ArrayList<Pair>();
		
		Housing housing = new Housing();
		Statistics stats = new Statistics();
		Users user = new Users();
		Reservations reservations = new Reservations();
		Feedback feedback = new Feedback();
		Keywords keywords = new Keywords();
		Period period = new Period();
		
		try {
			// remember to replace the password
			System.out.println("Attemping to connect to database...");
			con = new Connector();
			System.out.println("Database connection established");

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			// login
			while (c != 1 && c != 2) {
				
				displayLoginMenu();
				while ((choice = in.readLine()) == null && choice.length() == 0)
					;
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
				if (c < 1 | c > 2)
					continue;

				if (c == 1) // login
				{
					String password;
					
					System.out.println("Username:");
					while ((username = in.readLine()) == null && username.length() == 0);
					System.out.println("Password:");
					while ((password = in.readLine()) == null && password.length() == 0);

					if (user.login(username, password, con.stmt) == false) {
						System.out.println("Login failed");
						c = 0;
					}else{
						System.out.println("Login successful");
					}
				} 
				else if (c == 2) // register
				{
					System.out.println("Please choose a username:");
					while ((username = in.readLine()) == null && username.length() == 0);
					
					// check for uniqueness of username
					while (user.checkForUsernameUniqueness(username, con.stmt) == false) {

						System.out.println(
								"The username " + username + " is already taken, please choose another username:");
						username = null;
						while ((username = in.readLine()) == null && username.length() == 0);
					}
					System.out.println(username + " is unique:");
					
					String password;
					String firstName;
					String lastName;
					String address;
					String phoneNumber;
					
					System.out.println("Please choose a password:");
					while ((password = in.readLine()) == null && password.length() == 0);
					System.out.println("Please enter your first name:");
					while ((firstName = in.readLine()) == null && firstName.length() == 0);
					System.out.println("Please enter your last name:");
					while ((lastName = in.readLine()) == null && lastName.length() == 0);
					System.out.println("Please enter your address:");
					while ((address = in.readLine()) == null && address.length() == 0);
					System.out.println("Please enter your phone number:");
					while ((phoneNumber = in.readLine()) == null && phoneNumber.length() == 0);

					if (user.registerNewUser(username, password, firstName, lastName, address, phoneNumber, con.stmt) == true) {
						System.out.println(username + " was successfully added to the system");
					} else {
						System.out
								.println("There was an error adding " + username + " to the system, please try again");
						username = password = firstName = lastName = address = phoneNumber = null;
						c = 0;
					}
				}
			}

			// reset choice
			c = 0;
			
			// main menu
			while (true) {
				displayMainMenu();
				while ((choice = in.readLine()) == null && choice.length() == 0);
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
				if (c < 1 | c > 6)
					continue;
				if (c == 1) // temporary housing
				{
					c = 0;
					while (c != 7) {
						displayTemporaryHousingMenu();
						while ((choice = in.readLine()) == null && choice.length() == 0);
						try {
							c = Integer.parseInt(choice);
						} catch (Exception e) {

							continue;
						}
						if (c < 1 | c > 7)
							continue;
						if (c == 1) // reserve TH
						{
							System.out.println(housing.displayAllHousing(con.stmt));
						
							System.out.println("Please select the ID of the temporary housing you would like to reserve:");
							String line;
							int idOfTH;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								idOfTH = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								continue;
							}
								
							if(housing.checkHousingExists(idOfTH, con.stmt) == false){
								System.out.println("That was not a valid temporary housing ID:");
								c = 0;
								continue;
							}
							else
							{
								System.out.println("These are the available reservation peroids for this temporary housing:");
								String periods = period.showAvailablePeriods(idOfTH, con.stmt);
								if(periods.equals("")){
									System.out.println("Sorry, but there are no available periods for this temporary housing:");
								}
								else{
									System.out.println("Please select the ID of the period for which you would like to reserve:");
									System.out.println(periods);
									line = null;
									while ((line = in.readLine()) == null && line.length() == 0);
									int periodID;
									try{
										periodID = Integer.parseInt(line);
									}catch(Exception e){
										System.out.println(e.getMessage());
										c = 0;
										continue;
									}
									Pair newReservation = new Pair(idOfTH, periodID);
									reservationsInCart.add(newReservation);

									System.out.println("The reservation was added to your cart");
									
									String nameOfTH = housing.displayNameFromID(idOfTH, con.stmt);
									System.out.println("Users that recently reserved " + nameOfTH + " also visited the following temporary housing");
									System.out.println(reservations.getSuggestedReservations(idOfTH, con.stmt));
								}
							}
						}
						else if (c == 2) // record new PH
						{
							String thName;
							String address;
							String url;
							String category = "";
							int yearBuilt;
							String phonenumber;
							String city;
							
							System.out.println("Enter the details for the new permanent housing:");
							System.out.println("Name:");
							while((thName = in.readLine()) == null && thName.length() == 0);
							
							System.out.println("Address:");
							while((address = in.readLine()) == null && address.length() == 0);
							
							System.out.println("City:");
							while((city = in.readLine()) == null && city.length() == 0);
							
							System.out.println("URL:");
							while((url = in.readLine()) == null && url.length() == 0);
							
							int descision = 0;
							while(descision != 1 && descision != 2 && descision != 3){
								System.out.println("Category:");
								System.out.println("1. Studio:");
								System.out.println("2. Condo:");
								System.out.println("3. House:");
								String line;
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									descision = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								
								if(descision < 1 || descision > 3) continue;
								
								if(descision == 1) category = "Studio";
								else if(descision == 2) category = "Condo";
								else if(descision == 3) category = "House";
							}
							
							System.out.println("Year built:");
							String line;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								yearBuilt = Integer.parseInt(line);
							}catch(Exception e){
								continue;
							}
							
							System.out.println("Phone number:");
							while((phonenumber = in.readLine()) == null && phonenumber.length() == 0);
							
							
							System.out.println(housing.addNewPH(thName, address, category, url, yearBuilt, phonenumber, username, city, con.stmt));
						} 
						else if (c == 3) // update existing PH
						{

								System.out.println(housing.showHousingOwnedByUser(username, con.stmt));
								System.out.println("Please select the ID of the permanent house you would like to update:");

								String line;
								int idOfPh;
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									idOfPh = Integer.parseInt(line);
								}catch(Exception e){
									System.out.println(e.getMessage());
									continue;
								}
								
								if(housing.checkHousingExists(idOfPh, con.stmt)){
								
									if(housing.checkHouseIDbelongsToUser(idOfPh, username, con.stmt) == false){
										System.out.println("The provided housing ID belongs to another user:");
										continue;
									}
									
									System.out.println("\tCurrent information");
									System.out.println(housing.displayPHinformation(idOfPh, con.stmt));
									System.out.println();
									
									System.out.println("Would you like to update or remove this housing?:");
									System.out.println("1. Update:");
									System.out.println("2. Remove:");
									
									int i = 0;
									
									while(i != 1 && i != 2){
										line = null;
										while((line = in.readLine()) == null && line.length() == 0);
										try{
											i = Integer.parseInt(line);
										}catch(Exception e){
											continue;
										}
									}
									
									if(i == 1)
										{
										System.out.println("Please enter updated information");
										
										String thName;
										String address;
										String url;
										int yearBuilt;
										String category = "";
										String phonenumber;
										String city;
										
										System.out.println("Name:");
										while((thName = in.readLine()) == null && thName.length() == 0);
										
										System.out.println("Address:");
										while((address = in.readLine()) == null && address.length() == 0);
										
										System.out.println("City:");
										while((city = in.readLine()) == null && city.length() == 0);
										
										System.out.println("URL:");
										while((url = in.readLine()) == null && url.length() == 0);
										
										int descision = 0;
										while(descision != 1 && descision != 2 && descision != 3){
											System.out.println("Category:");
											System.out.println("1. Studio:");
											System.out.println("2. Condo:");
											System.out.println("3. House:");
											line = null;
											while((line = in.readLine()) == null && line.length() == 0);
											try{
												descision = Integer.parseInt(line);
											}catch(Exception e){
												continue;
											}
											
											if(descision < 1 || descision > 3) continue;
											
											if(descision == 1) category = "Studio";
											else if(descision == 2) category = "Condo";
											else if(descision == 3) category = "House";
										}
										
										System.out.println("Year built:");
										line = null;
										while((line = in.readLine()) == null && line.length() == 0);
										try{
											yearBuilt = Integer.parseInt(line);
										}catch(Exception e){
											continue;
										}
										
										System.out.println("Phone number:");
										while((phonenumber = in.readLine()) == null && phonenumber.length() == 0);
										
										
										System.out.println(housing.updateCurrentPH(idOfPh, thName, address, category, url, yearBuilt, phonenumber, username, city, con.stmt));	
									}
									else if(i == 2){
										System.out.println(housing.removeHousingOwnedByUser(idOfPh, con.stmt));
									}
								}
								else{
									System.out.println("That was not a valid ID:");
								}
						}
						
						else if (c == 4) // record stay
						{
							System.out.println("Here is a list of your reservations:");

							reservations.displayReservations(username, con.stmt);
							
							System.out.println("Please select the id of the housing you would like to record a stay:");
							String line;
							while (( line = in.readLine()) == null && line.length() == 0);
							int housingID;
							try{
								housingID = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								c = 0;
								continue;
							}
							
							period.displayPeriod(housingID, username, con.stmt);
							System.out.println("Please select the id of which period you would like to record a stay:");
							int periodID;
							line = null;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								periodID = Integer.parseInt(line);
							}catch(Exception e){
								continue;
							}
							
							if(reservations.checkForReservation(username, housingID, periodID, con.stmt)){
								Pair pair = new Pair(housingID, periodID);
								staysInCart.add(pair);
								System.out.println("That stay was added to your cart:");
							}else{
								System.out.println("The provided ID's were not valid");
								c = 0;
							}
						} 
						else if (c == 5) // make favorite
						{
							System.out.println("The following housing was found:");
							System.out.println(housing.displayAllHousing(con.stmt));
							System.out.println();
							
							System.out.println("Please select the ID of the permanent house you would like to make your favorite:");
							String line;
							int idOfTh;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								idOfTh = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								continue;
							}
							
							if(housing.checkHousingExists(idOfTh, con.stmt)){
								System.out.println(user.makeHousingFavorite(username, idOfTh, con.stmt));
							}
							else{
								System.out.println("That was not a valid ID");
							}

							
						} 
						else if (c == 6) // browse TH
						{
							//search by price
							System.out.println("Building search query:");
							System.out.println("Would you like to add a price to search by?:");
							
							System.out.println("1. Yes:");
							System.out.println("2. No:");

							int i = 0;
							String line;
							while(i != 1 && i != 2){
								while((line = in.readLine())== null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								line = null;

							}
							
							int lowerPriceRange = 0;
							int upperPriceRange = 0;
							
							//get price range
							if(i == 1){
								line = null;
								System.out.println("Lower price range:");
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									lowerPriceRange = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								
								line = null;
								System.out.println("Upper price range:");
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									upperPriceRange = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
							}
							
							//search by city
							System.out.println("Would you like to add a city to search by?:");
							System.out.println("1. Yes:");
							System.out.println("2. No:");

							i = 0;
							line = null;
							while(i != 1 && i != 2){
								while((line = in.readLine())== null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								line = null;

							}
							
							//get city input
							String city = null;
							if(i == 1){
								System.out.println("Enter a city:");
								line = null;
								while((city = in.readLine()) == null && city.length() == 0);
							}
							
							//search by keyword
							System.out.println("Would you like to add a keyword to search by?:");
							System.out.println("1. Yes:");
							System.out.println("2. No:");

							i = 0;
							line = null;
							while(i != 1 && i != 2){
								while((line = in.readLine())== null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								line = null;

							}
							
							//get keyword input
							String keyword = null;
							if(i == 1){
								System.out.println("Enter a keyword:");
								while((keyword = in.readLine()) == null && keyword.length() == 0);
							}
							
							//search by category
							System.out.println("Would you like to add a category to search by?:");
							System.out.println("1. Yes:");
							System.out.println("2. No:");

							i = 0;
							line = null;
							while(i != 1 && i != 2){
								while((line = in.readLine())== null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								line = null;

							}
							
							//get category input
							String category = null;
							if(i == 1){
								System.out.println("Enter a category:");
								while((category = in.readLine()) == null && category.length() == 0);
							}
							
							System.out.println("How would you like to sort the results?:");
							System.out.println("1. by price:");
							System.out.println("2. by the average numerical score of the feedbacks:");
							System.out.println("3. by the average numerical score of the trusted user feedbacks:");
							i = 0;
							line = null;
							while(i != 1 && i != 2 && i != 3){
								while((line = in.readLine())== null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
								
								line = null;
							}
							
							int sorting = i;
							
							System.out.println(housing.browseTemporaryHousing(lowerPriceRange, upperPriceRange, city, keyword, category, sorting, con.stmt));
						}
					}

					// reset value and go back to main menu
					c = 0;
				} else if (c == 2) // feedback
				{
					c = 0;

					while (c != 4) {
						displayFeedbackMenu();
						while ((choice = in.readLine()) == null && choice.length() == 0)
							;
						try {
							c = Integer.parseInt(choice);
						} catch (Exception e) {

							continue;
						}
						if (c < 1 | c > 5)
							continue;

						if (c == 1) // record feedback
						{
							System.out.println(housing.displayAllHousing(con.stmt));
							System.out.println();

							System.out.println("Please select the ID of the temporary housing you would like to give feedback on:");
							String line;
							int idOfTH;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								idOfTH = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								continue;
							}
							
							if(feedback.duplicateFeedback(username, idOfTH, con.stmt)){
								System.out.println("You already gave feedback on that housing:");
								continue;
							}
								
							int score = -1;
								
							System.out.println("Please enter a score between 0 and 10 (0 = terrible, 10 = excellent):");
							line = null;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								score = Integer.parseInt(line);
							}catch(Exception e){
								continue;
							}

							while(score < 0 || score > 10){
								System.out.println("That was not a valid score");
								System.out.println("Please enter a score between 0 and 10 (0= terrible, 10 = excellent):");
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									score = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
							}
								
							System.out.println("Would you like to add any comments?:");
							System.out.println("1. Yes:");
							System.out.println("2. No:");

							int i = 0;
							
							while(i != 1 && i != 2){
								line = null;
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									i = Integer.parseInt(line);
								}catch(Exception e){
									continue;
								}
							}
							
							String comments = "";
							if(i == 1){
								System.out.println("Comments:");
								while((comments = in.readLine()).equals("") && comments.length() == 0);
							}
															
							String date = LocalDate.getSQLDate();
								
							System.out.println(feedback.recordFeedback(username, idOfTH, date, score, comments, con.stmt));
							
						} 
						else if (c == 2) // assess feedback
						{
							System.out.println(housing.displayAllHousing(con.stmt));
							
							System.out.println("Please select the ID of the temporary housing you would like to view feedback on:");
							String line;
							int idOfTH;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								idOfTH = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								continue;
							}
								
								
							System.out.println(feedback.showFeedbackForTH(idOfTH, con.stmt));
							System.out.println("Using the feedback ID, which feedback would you like to assess?:");
							int feedbackID;
							line = null;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								feedbackID = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								throw e;
							}
							
							if(feedback.assessingOwnFeedback(username, feedbackID, con.stmt)){
								System.out.println("You cannot assess your own feedback:");
								continue;
							}
							
							System.out.println("What rating would you like to give this feedback?:");
							System.out.println("(0 = useless, 1 = useful, 2 = very useful)");
							int rating = -1;
							line = null;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								rating = Integer.parseInt(line);
							} catch (Exception e){
								continue;
							}
							
							while(rating < 0 || rating > 3){
								line = null;
								System.out.println("That was not a valid rating, please try again:");
								System.out.println("What rating would you like to give this feedback?:");
								System.out.println("(0 = useless, 1 = useful, 2 = very useful)");
								while((line = in.readLine()) == null && line.length() == 0);
								try{
									rating = Integer.parseInt(line);
								} catch (Exception e){
									continue;
								}
							}
								
							System.out.println(feedback.assessFeedback(username, feedbackID, rating, con.stmt));
							
						} 
						else if (c == 3) // rate user
						{
							System.out.println(user.showAllUsers(con.stmt));
							
							String nameUserToBeRated;
							System.out.println("Select the username of the user you would like to rate?:");
							while((nameUserToBeRated = in.readLine()) == null && nameUserToBeRated.length() == 0);
							
							if(user.checkUserExists(nameUserToBeRated, con.stmt) == false){
								System.out.println("user " + nameUserToBeRated + " could not be found");
								continue;
							}
							else
							{
								int i = 0;
								while(i != 1 && i != 2)
								{
									System.out.println("Please select the rating you would like to give this user:");
									System.out.println("1. Trusted:");
									System.out.println("2. Not-trusted:");
										
									String lineToBeParsed;
									while((lineToBeParsed = in.readLine()) == null && lineToBeParsed.length() == 0);
									try{
											i = Integer.parseInt(lineToBeParsed);
									}catch (Exception e){
											continue;
									}
								}
									
								int rating = i;

								System.out.println(feedback.rateUser(username, nameUserToBeRated, rating, con.stmt));
							}

						}
						else if (c == 4) //show most useful feedback for TH
						{
							System.out.println(housing.displayAllHousing(con.stmt));
							System.out.println();
							System.out.println("Please select the ID of the temporary housing you would like to view the most useful feedback on:");
							String line;
							int idOfTH;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								idOfTH = Integer.parseInt(line);
							}catch(Exception e){
								System.out.println(e.getMessage());
								continue;
							}
							
							System.out.println("Max number of results?:");
							int max = 1;
							line = null;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								max = Integer.parseInt(line);
							}catch(Exception e){
								continue;
							}
							
							System.out.println(stats.mostUsefulFeedbackTH(max,  idOfTH, con.stmt));
						}
						
					}

					// reset value and go back to main menu
					c = 0;
				} 
				else if (c == 3) // two degrees of separation
				{
					String firstUsername;
					String secondUsername;
					
					System.out.println("The following are all the users of the system:");
					System.out.println(user.showAllUsers(con.stmt));
					System.out.println("Please select the username of the first user:");
					while((firstUsername =in.readLine())== null && firstUsername.length() == 0);

					
					System.out.println("Please select the username of the second user:");
					while((secondUsername = in.readLine()) == null && secondUsername.length() == 0);

					
					//perform calculation
					if(user.checkUserExists(firstUsername, con.stmt) && user.checkUserExists(secondUsername, con.stmt)){
						System.out.print("Degree: ");
						System.out.println(user.calculateDegreeOfSeparation(firstUsername, secondUsername, con.stmt));
					}else{
						System.out.println("Either one or both of the usernames provided were not valid:");
					}

				} 
				else if (c == 4) // statistics
				{
					c = 0;
					int numResults;
					while(c != 4){
						displayStatisticsMenu();
						while ((choice = in.readLine()) == null && choice.length() == 0);
						try {
							c = Integer.parseInt(choice);
						} catch (Exception e) {

							continue;
						}
						if (c < 1 | c > 4)
							continue;
						
						if (c == 1) // most popular TH
						{
							System.out.println("How many results would you like to display?:");
							String line;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								numResults = Integer.parseInt(line);
							}catch (Exception e){
								System.out.println(e.getMessage());
								throw e;
							}

							stats.mostPopularTH(numResults, con.stmt);
						} 
						else if (c == 2) // most expensive TH
						{
							System.out.println("How many results would you like to display?:");
							String line;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								numResults = Integer.parseInt(line);
							}catch (Exception e){
								System.out.println(e.getMessage());
								throw e;
							}

							
							stats.mostExpensiveTH(numResults, con.stmt);
						} 
						else if (c == 3) // most highly rated PH
						{
							System.out.println("How many results would you like to display?:");
							String line;
							while((line = in.readLine()) == null && line.length() == 0);
							try{
								numResults = Integer.parseInt(line);
							}catch (Exception e){
								System.out.println(e.getMessage());
								throw e;
							}

							
							stats.mostHighlyRatedPH(numResults, con.stmt);
						}
					}
					
					// reset value and go back to main menu
					c = 0;
				} 
				else if(c == 5) //administration
				{
					if(user.isAdmin(username, con.stmt)){
						
						displayAdminMenu();

						int i = 0;
						while(i != 1 && i != 2){
							String line = null;
							while((line =in.readLine()) == null && line.length() == 0);
							try{
								i = Integer.parseInt(line);
							}catch(Exception e){
								continue;
							}
						}
						
						System.out.println("Max number of results?:");
						int max = 1;
						String line;
						while((line = in.readLine()) == null && line.length() == 0);
						try{
							max = Integer.parseInt(line);
						}catch(Exception e){
							continue;
						}
						
						if(i == 1){
							System.out.println("Most trusted users:\n");
							System.out.println(stats.mostTrustedUsers(max, con.stmt));
						}
						else if(i == 2){
							System.out.println("Most useful users:\n");
							System.out.println(stats.mostUsefulUsers(max, con.stmt));
						}
						
					}
					else
					{
						System.out.println("You do not have admin privileges");
						c = 0;
					}
				}
				else 
				{
					if(reservationsInCart.size() > 0){
						System.out.println("Please review and confirm the following reservations:");
						for(Pair p : reservationsInCart){
							System.out.println(housing.displayNameFromID(p.first(), con.stmt) + " " + period.displayFromAndTo(p.second(), con.stmt)
							+ " " + period.displayCost(p.second(), con.stmt));
						}
						int i = 0;
						
						System.out.println("1. Confirm reservations:");
						System.out.println("2. Cancel reservations:");
						String answer;
						while ((answer = in.readLine()) == null && answer.length() == 0);
						try{
							i = Integer.parseInt(answer);
						}catch(Exception e){
							System.out.println(e.getMessage());
						}
						
						
						if(i == 1){
							for(Pair p : reservationsInCart){
								System.out.println(reservations.makeReservation(username, p.first(), p.second(), con.stmt));
								System.out.println(period.removePeriodFromAvailability(p.first(),  p.second(), con.stmt));
							}
						}else{
							System.out.println("Reservations were discarded:");
						}
					}


					if(staysInCart.size() > 0){
						System.out.println("Please review and confirm the following stays:");
						for(Pair p : staysInCart){
							System.out.println(housing.displayNameFromID(p.first(), con.stmt) + " " + period.displayFromAndTo(p.second(), con.stmt)
							+ " " + period.displayCost(p.second(), con.stmt));
						}
						System.out.println("1. Confirm stays:");
						System.out.println("2. Cancel stays:");
						String answer = null;
						int i = 0;
						while ((answer = in.readLine()) == null && answer.length() == 0);
						try{
							i = Integer.parseInt(answer);
						}catch(Exception e){
							System.out.println(e.getMessage());
						}
						
						if(i == 1){
							for (Pair p : staysInCart){
								System.out.println(reservations.recordStay(username, p.first(), p.second(), con.stmt));
								System.out.println(reservations.removeReservation(username, p.first(), p.second(), con.stmt));
							}
						}
						else{
							System.out.println("Stays were discarded:");
						}
					}

					
					con.stmt.close();

					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Either connection error or query execution error!");
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println("Database connection terminated");
				}

				catch (Exception e) {
					/* ignore close errors */ }
			}
		}
	}

}
