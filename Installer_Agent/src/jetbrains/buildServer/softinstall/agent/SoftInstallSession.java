package jetbrains.buildServer.softinstall.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallSession implements MultiCommandBuildSession
{
  @NotNull
  private final Logger myLogger;


  public SoftInstallSession(@NotNull Logger logger)
  {
    myLogger = logger;
  }


  @Override
  public void sessionStarted()
          throws RunBuildException
  {
    myLogger.info("HELLO!"); // TODO

  }


  @Nullable
  @Override
  public CommandExecution getNextCommand()
          throws RunBuildException
  {
    // TODO implement SoftInstallSession.getNextCommand
    //throw new RuntimeException("The SoftInstallSession.getNextCommand has not been implemented yet.");
    return null; // TODO
  }


  @Nullable
  @Override
  public BuildFinishedStatus sessionFinished()
  {
    myLogger.info("OK!"); // TODO
    return BuildFinishedStatus.FINISHED_SUCCESS;
  }
}
