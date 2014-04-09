package jetbrains.buildServer.softinstall.agent;

import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.softinstall.models.loader.ModelLoader;
import jetbrains.buildServer.softinstall.models.loader.ModelWebRetriever;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import org.jetbrains.annotations.NotNull;

import java.util.Set;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class ModelObtainer
{
  @NotNull
  private final String myServerURL;

  @NotNull
  private final ModelWebRetriever myModelRetriever;


  public ModelObtainer(@NotNull final BuildAgentConfiguration bac)  {
    myServerURL = bac.getServerUrl();
    myModelRetriever = new ModelWebRetriever(new ModelLoader());
  }


  public SoftModel retrieveModel(@NotNull Set<String> names) {
    String baseURL = myServerURL + "/get/file/softinstall";
    return myModelRetriever.retrieveModel(baseURL, names);
  }



}
