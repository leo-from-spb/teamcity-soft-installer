package jetbrains.buildServer.softinstall.agent;

/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallException extends Exception
{
  public SoftInstallException(String message)
  {
    super(message);
  }


  public SoftInstallException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
