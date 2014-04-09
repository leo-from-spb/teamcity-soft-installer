package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftCheckFile
{
  @NotNull
  private String myFileSpec;


  public SoftCheckFile(@NotNull final String fileSpec)
  {
    myFileSpec = fileSpec;
  }


  @NotNull
  public String getFileSpec()
  {
    return myFileSpec;
  }


  public void setFileSpec(@NotNull String fileSpec)
  {
    myFileSpec = fileSpec;
  }
}
