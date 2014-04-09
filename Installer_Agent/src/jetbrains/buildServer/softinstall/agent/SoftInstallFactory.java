package jetbrains.buildServer.softinstall.agent;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.runner.MultiCommandBuildSessionFactory;
import jetbrains.buildServer.softinstall.common.Consts;
import org.jetbrains.annotations.NotNull;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallFactory implements MultiCommandBuildSessionFactory
{
  @NotNull
  private final ModelObtainer myModelObtainer;

  @NotNull
  private final Logger myLogger;


  public SoftInstallFactory(@NotNull ModelObtainer modelObtainer)
  {
    myModelObtainer = modelObtainer;
    myLogger = Logger.getInstance("SoftInstaller");
    myLogger.debug("SoftInstallFactory is created");
  }


  @NotNull
  @Override
  public SoftInstallSession createSession(@NotNull BuildRunnerContext buildRunnerContext)
          throws RunBuildException
  {
    return new SoftInstallSession(myModelObtainer, buildRunnerContext, myLogger);
  }


  @NotNull
  @Override
  public AgentBuildRunnerInfo getBuildRunnerInfo()
  {
    return AGENT_BUILD_RUNNER_INFO;
  }


  private static final AgentBuildRunnerInfo AGENT_BUILD_RUNNER_INFO =
          new AgentBuildRunnerInfo()
          {
            @NotNull
            @Override
            public String getType()
            {
              return Consts.RUN_TYPE_NAME;
            }


            @Override
            public boolean canRun(@NotNull BuildAgentConfiguration buildAgentConfiguration)
            {
              return true;
            }
          };
}
