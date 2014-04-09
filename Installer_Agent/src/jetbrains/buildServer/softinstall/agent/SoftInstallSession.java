package jetbrains.buildServer.softinstall.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;

import static jetbrains.buildServer.softinstall.common.Utils.splitSoftNames;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallSession implements MultiCommandBuildSession
{

  //// SETTINGS \\\\

  @NotNull
  private final Logger myLogger;

  @NotNull
  private final ModelObtainer myModelObtainer;

  @NotNull
  private final BuildRunnerContext myContext;

  @NotNull
  private final BuildProgressLogger myBuildLogger;


  //// CURRENT STATE \\\\


  private SoftModel myModel;

  private SortedSet<SoftDescriptor> myDescriptorsToInstall;

  private Set<SoftDescriptor> myReadyDescriptors;



  //// CONSTRUCTOR \\\\

  public SoftInstallSession(@NotNull ModelObtainer modelObtainer,
                            @NotNull BuildRunnerContext buildRunnerContext,
                            @NotNull Logger logger)
  {
    myModelObtainer = modelObtainer;
    myContext = buildRunnerContext;
    myBuildLogger = myContext.getBuild().getBuildLogger();
    myLogger = logger;
  }


  //// PROCESS \\\\

  @Override
  public void sessionStarted()
          throws RunBuildException
  {
    final AgentRunningBuild build = myContext.getBuild();
    myLogger.debug("Build "+ build.getBuildId()+" session started");
    myBuildLogger.message("SESSION START"); // TODO

    String softToInstallText = myContext.getRunnerParameters().get("softToInstall");
    Set<String> softToInstallNames = splitSoftNames(softToInstallText);

    myBuildLogger.message("Retrieving software definitions for: " + softToInstallNames);
    myModel = myModelObtainer.retrieveModel(softToInstallNames);
    myDescriptorsToInstall = myModel.getDescriptors(softToInstallNames);
    myBuildLogger.message("Software definitions are retrieved");

    myReadyDescriptors = new LinkedHashSet<SoftDescriptor>(myDescriptorsToInstall.size());

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
