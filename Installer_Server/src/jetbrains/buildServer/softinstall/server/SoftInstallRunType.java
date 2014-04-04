package jetbrains.buildServer.softinstall.server;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static jetbrains.buildServer.softinstall.common.Consts.*;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallRunType extends RunType
{


  private final PluginDescriptor myDescriptor;


  public SoftInstallRunType(@NotNull final RunTypeRegistry reg,
                            @NotNull PluginDescriptor descriptor)
  {
    myDescriptor = descriptor;
    reg.registerRunType(this);
  }


  @NotNull
  @Override
  public String getType()
  {
    return RUN_TYPE_NAME;
  }


  @NotNull
  @Override
  public String getDisplayName()
  {
    return "Soft Installer";
  }


  @NotNull
  @Override
  public String getDescription()
  {
    return "Installs software on demand";
  }


  @Override
  public PropertiesProcessor getRunnerPropertiesProcessor()
  {
    return new SoftInstallPropertiesProcessor();
  }


  @Override
  public String getEditRunnerParamsJspFilePath()
  {
    return myDescriptor.getPluginResourcesPath("editSoftInstallParams.jsp");
  }


  @Override
  public String getViewRunnerParamsJspFilePath()
  {
    return myDescriptor.getPluginResourcesPath("viewSoftInstallParams.jsp");
  }


  @Override
  public Map<String, String> getDefaultRunnerProperties()
  {
    Map<String,String> props = new HashMap<String,String>();
    props.put("soft", "");
    return props;
  }
}
