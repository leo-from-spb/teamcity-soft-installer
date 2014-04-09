package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import org.jetbrains.annotations.NotNull;
import sun.net.www.http.HttpClient;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class ModelWebRetriever
{
  @NotNull
  private final ModelLoader myModelLoader;


  public ModelWebRetriever(@NotNull ModelLoader modelLoader)
  {
    myModelLoader = modelLoader;
  }


  public SoftModel retrieveModel(@NotNull String baseURL, @NotNull Set<String> names) {
    SoftModel model = new SoftModel();
    for (String name : names) {
      final SoftDescriptor descriptor = retrieveDescriptor(baseURL, name);
      model.addDescriptor(descriptor);
    }
    return model;
  }


  public SoftDescriptor retrieveDescriptor(@NotNull String baseURL, @NotNull String name)
  {
    try {
      URL theURL = new URL(baseURL + "/" + name + ".soft.xml");
      final InputStream inputStream = theURL.openStream();
      try {
        return myModelLoader.loadDescriptor(inputStream, name);
      }
      finally {
        inputStream.close();
      }
    }
    catch (Exception e) {
      throw new WrongModelException("Failed to download software descriptor "+name+" from \""+baseURL+"\": " + e.getMessage(), e);
    }
  }

}
