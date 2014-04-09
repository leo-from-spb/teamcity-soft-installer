package jetbrains.buildServer.softinstall.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static jetbrains.buildServer.softinstall.common.Utils.splitSoftNames;



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
    final AgentRunningBuild build = myContext.getBuild();
    myLogger.debug("Build "+ build.getBuildId()+" session started");
    myBuildLogger.message("SESSION START"); // TODO

    String softToInstallText = myContext.getRunnerParameters().get("softToInstall");
    Set<String> softToInstallNames = splitSoftNames(softToInstallText);
    for (String name : softToInstallNames) {
      myBuildLogger.message("TO INSTALL: " + name); // TODO
    }
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
