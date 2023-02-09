import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieCollection {
  private ArrayList<Movie> movies;
  private ArrayList<String> actors;

  private ArrayList<String> allGenres;
  private Scanner scanner;

  public MovieCollection(String fileName) {
    importMovieList(fileName);
    scanner = new Scanner(System.in);
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public void menu() {
    String menuOption = "";

    System.out.println("Welcome to the movie collection!");
    System.out.println("Total: " + movies.size() + " movies");

    while (!menuOption.equals("q")) {
      System.out.println("------------ Main Menu ----------");
      System.out.println("- search (t)itles");
      System.out.println("- search (k)eywords");
      System.out.println("- search (c)ast");
      System.out.println("- see all movies of a (g)enre");
      System.out.println("- list top 50 (r)ated movies");
      System.out.println("- list top 50 (h)igest revenue movies");
      System.out.println("- (q)uit");
      System.out.print("Enter choice: ");
      menuOption = scanner.nextLine();

      if (menuOption.equals("t")) {
        searchTitles();
      } else if (menuOption.equals("c")) {
        searchCast();
      } else if (menuOption.equals("k")) {
        searchKeywords();
      } else if (menuOption.equals("g")) {
        listGenres();
      } else if (menuOption.equals("r")) {
        listHighestRated();
      } else if (menuOption.equals("h")) {
        listHighestRevenue();
      } else if (menuOption.equals("q")) {
        System.out.println("Goodbye!");
      } else {
        System.out.println("Invalid choice!");
      }
    }
  }

  private void importMovieList(String fileName) {
    try {
      movies = new ArrayList<Movie>();
      actors = new ArrayList<String>();
      allGenres = new ArrayList<String>();
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();

      while ((line = bufferedReader.readLine()) != null) {
        // get data from the columns in the current row and split into an array
        String[] movieFromCSV = line.split(",");

        /* TASK 1: FINISH THE CODE BELOW */
        // using the movieFromCSV array,
        // obtain the title, cast, director, tagline,
        // keywords, overview, runtime (int), genres,
        // user rating (double), year (int), and revenue (int)
        String title = movieFromCSV[0];
        String[] cast = movieFromCSV[1].split("\\|");;
        String director = movieFromCSV[2];
        String tagline = movieFromCSV[3];
        String keywords = movieFromCSV[4];
        String overview = movieFromCSV[5];
        int runtime = Integer.parseInt(movieFromCSV[6]);
        String[] genres = movieFromCSV[7].split("\\|");
        double userRating = Double.parseDouble(movieFromCSV[8]);
        int year = Integer.parseInt(movieFromCSV[9]);
        int revenue = Integer.parseInt(movieFromCSV[10]);


        // create a Movie object with the row data:
        Movie thisMovie = new Movie(title,cast,director,tagline,
                keywords,overview,runtime,genres,userRating,year,
                revenue);

        // add the Movie to movies:
        movies.add(thisMovie);

        // Add to list of actors in correct spot:
        addAllNoDuplicates(cast,actors);
        addAllNoDuplicates(genres,allGenres);
      }



      bufferedReader.close();
    } catch(IOException exception) {
      System.out.println("Unable to access " + exception.getMessage());
    }
  }

  private void sortResults(ArrayList<Movie> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      String tempTitle = temp.getTitle();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }
  
  private void displayMovieInfo(Movie movie) {
    System.out.println();
    System.out.println("Title: " + movie.getTitle());
    System.out.println("Tagline: " + movie.getTagline());
    System.out.println("Runtime: " + movie.getRuntime() + " minutes");
    System.out.println("Year: " + movie.getYear());
    System.out.println("Directed by: " + movie.getDirector());
    System.out.println("Cast: " + movie.getCastText());
    System.out.println("Genre(s): " + movie.getGenresText());
    System.out.println("Overview: " + movie.getOverview());
    System.out.println("User rating: " + movie.getUserRating());
    System.out.println("Box office revenue: " + movie.getRevenue());
  }

  private void searchTitles() {
    System.out.print("Enter a title search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieTitle = movies.get(i).getTitle();
      movieTitle = movieTitle.toLowerCase();

      if (movieTitle.indexOf(searchTerm) != -1) {
        //add the Movie object to the results list
        results.add(movies.get(i));
      }
    }

    if (results.size() > 0) {
      // sort the results by title
      sortResults(results);

      printMatchingMovies(results);
      askWhichMovie(results);
    } else {
      System.out.println("\nNo movie titles match that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }
  }

  private void searchKeywords() {
    System.out.print("Enter a keyword: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String keywords = movies.get(i).getKeywords();
      keywords = keywords.toLowerCase();

      if (keywords.contains(searchTerm)) {
        //add the Movie object to the results list
        results.add(movies.get(i));
      }
    }

    if (results.size() > 0) {
      // sort the results by title
      sortResults(results);

      printMatchingMovies(results);
      askWhichMovie(results);
    } else {
      System.out.println("\nNo movie's keywords have that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }
  }

//  private void searchCast() {
//    System.out.println("Enter an actor's name: ");
//    String searchTerm = scanner.nextLine();
//
//    // Get list of actors (and begin to get list of movies)
//    ArrayList<Movie> results = new ArrayList<Movie>();
//    ArrayList<String> allActors = new ArrayList<>();
//    for (Movie thisMovie : movies) {
//      boolean movieAdded = false;
//
//      String[] thisMoviesActors = thisMovie.getCast();
//      // Search through list of actors in this movie
//      for (String thisName : thisMoviesActors) {
//        // For each actor that matches the search term...
//        if (thisName.toLowerCase().contains(searchTerm.toLowerCase())) {
//
//          if (!movieAdded) { // Add this movie to results if haven't already
//            results.add(thisMovie);
//            movieAdded=true;
//          }
//
//          // Check if actor has been added to list of actors that match the search
//          boolean actorAdded=false;
//          for(String checkAgainstName : allActors) {
//            if ( checkAgainstName.toLowerCase().equals(thisName.toLowerCase()) ) {
//              actorAdded=true;
//              break;
//            }
//          }
//
//          if(!actorAdded) {allActors.add(thisName);} // Add this actor to allActors if haven't already
//        }
//      }
//    }
//
//    for (int i = 0; i < allActors.size(); i++) {
//      String name = allActors.get(i);
//
//      // this will print index 0 as choice 1 in the results list; better for user!
//      int choiceNum = i + 1;
//      System.out.println("" + choiceNum + ". " + name);
//    }
//
//    System.out.println("Which actor are you referring to?");
//    System.out.print("Enter number: ");
//    int choice = scanner.nextInt();
//    scanner.nextLine();
//
//    // prevent case sensitivity
//    String exactName = allActors.get(choice-1).toLowerCase();
//
//    // search through list of movies from earlier, remove entries that aren't the specific actor
//    for (int i = 0; i < results.size(); i++) {
//      String cast = movies.get(i).getCastText();
//      cast = cast.toLowerCase();
//
//      if (!cast.contains(exactName)) {
//        results.remove(i);
//        i--;
//      }
//    }
//
//    if (results.size() > 0) {
//      // sort the results by title
//      sortResults(results);
//
//      // now, display them all to the user
//      for (int i = 0; i < results.size(); i++) {
//        String title = results.get(i).getTitle();
//
//        // this will print index 0 as choice 1 in the results list; better for user!
//        int choiceNum = i + 1;
//        System.out.println("" + choiceNum + ". " + title);
//      }
//
//      System.out.println("Which movie would you like to learn more about?");
//      System.out.print("Enter number: ");
//      int choice2 = scanner.nextInt();
//      scanner.nextLine();
//      Movie selectedMovie = results.get(choice2 - 1);
//      displayMovieInfo(selectedMovie);
//      System.out.println("\n ** Press Enter to Return to Main Menu **");
//      scanner.nextLine();
//    } else {
//      System.out.println("\nError occurred - actor not found!?");
//      System.out.println("** Press Enter to Return to Main Menu **");
//      scanner.nextLine();
//    }
//  }

  private void searchCast() {
    System.out.println("Enter an actor's name: ");
    String searchTerm = scanner.nextLine().toLowerCase();

    // Find names that match, do NOT print out matching actors' names since there might be only 1 match
    ArrayList<String> matchingActors = new ArrayList<>();
    for (String actor : actors) {
      if ( actor.toLowerCase().contains(searchTerm) ) {
        matchingActors.add(actor);
      }
    }
    // Because actors is already in alphabetical order, we don't need to sort matchingActors

    String selectedActor;
    if (matchingActors.size()==0) { // Make sure that matching actors were found
      System.out.println("\nNo actors match that name!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
      return; // Early return to stop early

    } else if (matchingActors.size()==1) { // Don't ask which actor if only one actor matches
      selectedActor = matchingActors.get(0);
    } else {

      for (int i = 0; i<matchingActors.size(); i++) {
        System.out.println(i+1 + ". " + matchingActors.get(i));
      }

      System.out.println("Which actor are you referring to?");
      System.out.print("Enter number: ");
      selectedActor = matchingActors.get(scanner.nextInt()-1);
      scanner.nextLine();
    }

    // Find movies with that actor, print them out
    ArrayList<Movie> matchingMovies = new ArrayList<>();
    for (Movie movie : movies) {

      for (String actor : movie.getCast()) {
        if (selectedActor.equals(actor)) {
          matchingMovies.add(movie);
          System.out.println(matchingMovies.size() + ". " + movie.getTitle());

          break;
        }
      }

    }

    askWhichMovie(matchingMovies);
  }

  private void listGenres() {
    for (int i = 0; i<allGenres.size(); i++) {
      System.out.println(i+1 + ". " + allGenres.get(i));
    }

    System.out.println("Which would you like to see all the movies for?");
    System.out.print("Enter number: ");
    String selectedGenre = allGenres.get(scanner.nextInt()-1);
    scanner.nextLine();

    // Find movies with that genre, print them out
    ArrayList<Movie> matchingMovies = new ArrayList<>();
    for (Movie movie : movies) {
      for (String genre : movie.getGenres()) {
        if (genre.equals(selectedGenre)) {
          matchingMovies.add(movie);
          System.out.println(matchingMovies.size() + ". " + movie.getTitle());
          break;
        }
      }

    }

    askWhichMovie(matchingMovies);
  }
  
  private void listHighestRated() {
    /* TASK 6: IMPLEMENT ME */
  }
  
  private void listHighestRevenue() {
    /* TASK 6: IMPLEMENT ME */
  }

  private void printMatchingMovies(ArrayList<Movie> matchingMovies) {
    for (int i=0; i<matchingMovies.size(); i++) {
      System.out.println(i+1 + ". " + matchingMovies.get(i).getTitle());
    }
  }

  private void askWhichMovie(ArrayList<Movie> matchingMovies) {
    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");
    int choice = scanner.nextInt();
    scanner.nextLine();
    Movie selectedMovie = matchingMovies.get(choice - 1);
    displayMovieInfo(selectedMovie);
    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }

  private void addAllNoDuplicates(String[] data, ArrayList<String> addTo) {

    for (String info : data) {

      // Find its spot in the list of actors
      int i = 0;
      int comparison = -1;
      while ( i<addTo.size() && (comparison = info.compareTo(addTo.get(i))) > 0) {
        i++;
      }

      // Place actor's name in list if it hasn't already been added
      if (comparison!=0) {addTo.add(i,info);}
    }

  }
}