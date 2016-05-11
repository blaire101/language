/**
 * Date : 2016-05-09
 */
public class Singleton {

  private static Singleton instance = new Singleton();

  private Singleton() {

  }

  public static Singleton getInstance() {
    return instance;
  }
  public void print() {
    System.out.println("Hello World, I'm Singleton!!!");
  }
}
