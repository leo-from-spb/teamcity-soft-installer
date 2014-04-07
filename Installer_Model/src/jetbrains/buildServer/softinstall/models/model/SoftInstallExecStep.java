package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallExecStep extends SoftInstallStep
{
  @NotNull
  private String myCmdLine;


  public SoftInstallExecStep(@NotNull String cmdLine)
  {
    myCmdLine = cmdLine;
  }


  @NotNull
  public String getCmdLine()
  {
    return myCmdLine;
  }


  public void setCmdLine(@NotNull String cmdLine)
  {
    myCmdLine = cmdLine;
  }
}
