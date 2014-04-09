package jetbrains.buildServer.softinstall.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
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

  @NotNull
  private final BuildRunnerContext myContext;

  @NotNull
  private final BuildProgressLogger myBuildLogger;


  public SoftInstallSession(@NotNull BuildRunnerContext buildRunnerContext, @NotNull Logger logger)
  {
    myContext = buildRunnerContext;
    myBuildLogger = myContext.getBuild().getBuildLogger();
    myLogger = logger;
  }


  @Override
  public void sessionStarted()
          throws RunBuildException
  {
    myLogger.debug("Build "+myContext.getBuild().getBuildId()+" session started");
    myBuildLogger.message("SESSION START"); // TODO
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
    myBuildLogger.message("SESSION FINISH"); // TODO
    return BuildFinishedStatus.FINISHED_SUCCESS;
  }
}
