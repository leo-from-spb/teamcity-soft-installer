package jetbrains.buildServer.softinstall.agent;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import jetbrains.buildServer.ExecResult;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.SimpleCommandLineProcessRunner;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.CommandExecution;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSession;
import jetbrains.buildServer.softinstall.models.loader.WrongModelException;
import jetbrains.buildServer.softinstall.models.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

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

  @NotNull
  private final OS myOS;


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
    myOS = determineOS();
  }


  private static OS determineOS()
  {
    if (SystemInfo.isMac) return OS.MAC;
    else if (SystemInfo.isLinux) return OS.LINUX;
    else if (SystemInfo.isWindows) return OS.WINDOWS;
    else return OS.UNIX;
  }


  //// PROCESS \\\\

  @Override
  public void sessionStarted()
          throws RunBuildException
  {
    final AgentRunningBuild build = myContext.getBuild();
    myLogger.debug("Build "+ build.getBuildId()+" session started");
    logMessage("SESSION START"); // TODO

    String softToInstallText = myContext.getRunnerParameters().get("softToInstall");
    Set<String> softToInstallNames = splitSoftNames(softToInstallText);

    logMessage("Retrieving software definitions for: " + softToInstallNames);
    myModel = myModelObtainer.retrieveModel(softToInstallNames);
    myDescriptorsToInstall = new TreeSet<SoftDescriptor>(myModel.getDescriptors(softToInstallNames));
    logMessage("Software definitions are retrieved");

    myReadyDescriptors = new LinkedHashSet<SoftDescriptor>(myDescriptorsToInstall.size());
  }


  @Nullable
  @Override
  public CommandExecution getNextCommand()
          throws RunBuildException
  {
    try {
      processInstallation();
    }
    catch (SoftInstallException sie) {
      myBuildLogger.exception(sie);
      myBuildLogger.buildFailureDescription(sie.getMessage());
    }

    return null;
  }


  private void processInstallation()
          throws SoftInstallException
  {
    while (!myDescriptorsToInstall.isEmpty()) {
      SoftDescriptor curr = myDescriptorsToInstall.first(); // already sorted in proper order

      // select section
      SoftSection section = selectApplicableSection(curr);
      if (section == null) {
        String msg = "Unknown how to install " + curr.getName() + " onto this agent.";
        myBuildLogger.buildFailureDescription(msg);
        return;
      }

      // check already installed software
      final File workingDirectory = myContext.getWorkingDirectory();
      SoftExistent foundExistent = null;
      for (SoftExistent existent : section.getExistents()) {
        boolean checksOk = true;
        for (SoftCheckFile checkFile : existent.getChecks()) {
          File file = new File(workingDirectory, checkFile.getFileSpec());
          if (!file.isFile()) {
            myLogger.debug("File \"" + file.getAbsolutePath() + "\" doesn't exist");
            checksOk = false;
          }
        }
        if (!checksOk) continue;  // doesn't exist
        foundExistent = existent;
        break;
      }
      if (foundExistent != null) {
        logMessage("Software " + curr.getName() + " already installed on this agent");
        myReadyDescriptors.add(curr);
        myDescriptorsToInstall.remove(curr);
        processVariables(foundExistent.getVars());
        continue;
      }

      // install
      final List<SoftInstallStep> installSteps = section.getInstallSteps();
      for (SoftInstallStep step : installSteps) {
        processStep(step);
      }
      myReadyDescriptors.add(curr);
      myDescriptorsToInstall.remove(curr);
      processVariables(section.getVars());
    }
  }


  @Nullable
  private SoftSection selectApplicableSection(SoftDescriptor descriptor)
  {
    for (SoftSection section : descriptor.getSections()) {
      if (section.getOS() != myOS) continue;

      // TODO check other conditions and requirements

      return section;
    }

    return null;
  }


  private final static String PARAM_MESSAGE =
          "##teamcity[setParameter name='{NAME}' value='{VALUE}']";


  private void processVariables(List<SoftVariable> vars)
  {
    for (SoftVariable var : vars) {
      String name = var.getName();
      String expression = var.getExpression();
      if (expression == null) continue;
      String value = processVarExpression(expression);
      String m = PARAM_MESSAGE;
      m = m.replace("{NAME}", name);
      m = m.replace("{VALUE}", value);
      myBuildLogger.message(m);
    }
  }


  private String processVarExpression(@NotNull String expression)
  {
    // TODO implement SoftInstallSession.processVarExpression
    return expression;
  }


  private void processStep(@NotNull SoftInstallStep step)
          throws SoftInstallException
  {
    if (step instanceof SoftInstallExecStep) processExecStep((SoftInstallExecStep)step);
    else throw new WrongModelException("Unknown how to process " + step.getClass().getSimpleName());
  }


  private void processExecStep(SoftInstallExecStep step)
          throws SoftInstallException
  {
    final String cmdLine = step.getCmdLine();
    myBuildLogger.message("RUNNING: " + cmdLine);

    ExecResult result = runCommand(cmdLine);
    if (result.getExitCode() != 0) {
      throw new SoftInstallException("Exit code "+result.getExitCode()+" from process: " + cmdLine);
    }
  }


  @Nullable
  @Override
  public BuildFinishedStatus sessionFinished()
  {
    myLogger.debug("SESSION FINISH");
    return BuildFinishedStatus.FINISHED_SUCCESS;
  }




  //// RUN \\\\

  ExecResult runCommand(@NotNull String commandLine)
          throws SoftInstallException
  {
    Pair<File,String> systemCommandInterpreter = getSystemCommandIntepreter();
    if (!systemCommandInterpreter.first.isFile()) {
      throw new SoftInstallException("System command interpreter not found: " + systemCommandInterpreter);
    }

    final List<String> givenArgs = parseCommandLine(commandLine);

    ArrayList<String> allArgs = new ArrayList<String>(givenArgs.size()+1);
    allArgs.add(systemCommandInterpreter.second);
    allArgs.addAll(givenArgs);

    try {
      return runExeFile(systemCommandInterpreter.first, allArgs, null);
    }
    catch (ExecutionException ee) {
      throw new SoftInstallException("Failed to run: " + commandLine);
    }
  }


  public Pair<File,String> getSystemCommandIntepreter()
  {
    final String exePath;
    final String param;
    switch (myOS) {
      case WINDOWS:
        exePath = "C:\\Windows\\System32\\cmd.exe";
        param = "/C";
        break;
      case MAC:
        exePath = "/bin/sh";
        param = "-c";
        break;
      case LINUX:
        exePath = "/bin/bash";
        param = "-c";
        break;
      default:
        exePath = "/bin/sh";
        param = "-c";
    }

    return new Pair<File,String>(new File(exePath), param);
  }


  static List<String> parseCommandLine(@NotNull String cmdLine) {
    final String[] strings = cmdLine.split(" +");
    List<String> args = new ArrayList<String>(strings.length);
    for (String str : strings) {
      String s = str.trim();
      if (s.length() > 0) args.add(s);
    }
    return args;
  }





  static ExecResult runExeFile(final @NotNull File exeFile,
                               final @Nullable List<String> args,
                               final @Nullable String inputText)
          throws ExecutionException
  {
    GeneralCommandLine cmdLine = new GeneralCommandLine();
    cmdLine.setExePath(exeFile.getAbsolutePath());
    cmdLine.setPassParentEnvs(true);
    if (args != null)
        cmdLine.addParameters(args);

    final byte[] input = inputText != null
            ? inputText.getBytes()
            : new byte[0];

    try
    {
      ExecResult execResult;
      final Thread currentThread = Thread.currentThread();
      final String threadName = currentThread.getName();
      final String newThreadName = "SoftInstall: waiting for: " + cmdLine.getCommandLineString();
      currentThread.setName(newThreadName);
      try
      {
        execResult = SimpleCommandLineProcessRunner.runCommand(cmdLine, input);
      }
      finally
      {
        currentThread.setName(threadName);
      }
      return execResult;
    }
    catch (Exception e)
    {
      throw new ExecutionException(e.getMessage(), e);
    }
  }





  //// UTILS \\\\


  private void logMessage(final String msg)
  {
    myBuildLogger.message(msg);
  }


}
