package jetbrains.buildServer.softinstall.server;

/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallPropertiesBean
{

  private String mySoftToInstall;


  public String getSoftToInstall()
  {
    return mySoftToInstall;
  }


  public void setSoftToInstall(String softToInstall)
  {
    mySoftToInstall = softToInstall;
  }
}
