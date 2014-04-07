package jetbrains.buildServer.softinstall.models.loader;

/**
 * @author Leonid Bushuev from JetBrains
 */
public class WrongModelException extends RuntimeException
{

  public WrongModelException(String message)
  {
    super(message);
  }


  public WrongModelException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
