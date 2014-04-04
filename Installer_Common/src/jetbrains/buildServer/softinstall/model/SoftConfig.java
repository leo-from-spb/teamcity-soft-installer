package jetbrains.buildServer.softinstall.model;

import org.jetbrains.annotations.NotNull;

import java.util.Map;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftConfig
{

  @NotNull
  public final SoftSystemConfig myCommonConfig;

  @NotNull
  public final Map<OS,SoftSystemConfig> mySystemConfigs;


  public SoftConfig(@NotNull SoftSystemConfig commonConfig, @NotNull Map<OS, SoftSystemConfig> systemConfigs)
  {
    myCommonConfig = commonConfig;
    mySystemConfigs = systemConfigs;
  }

}
