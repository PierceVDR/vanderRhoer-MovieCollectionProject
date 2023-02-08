import java.util.ArrayList;

public class MovieCollectionRunner {
  public static void main(String[] args) {
    /* TASK 1: finish the code for the importMovieList helper method
       in the MovieCollection class, then write some test code here to create
       a new MovieCollection object from the movies in the movies_data.csv file,
       get the movies arraylist, and print out each movie.
       use the code in the CerealCollectionRunner as an example. */

    MovieCollection collection = new MovieCollection("src\\movies_data.csv");
    ArrayList<Movie> movies = collection.getMovies();
    for(Movie v : movies) {
      System.out.println(v);
    }
  }
}