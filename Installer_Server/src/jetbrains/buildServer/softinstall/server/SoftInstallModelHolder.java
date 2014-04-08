package jetbrains.buildServer.softinstall.server;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.softinstall.models.loader.ModelLoader;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import org.jetbrains.annotations.NotNull;

import java.io.File;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftInstallModelHolder
{

  private final File mySoftConfigDir;

  @NotNull
  private SoftModel myModel = new SoftModel();

  @NotNull
  private ModelLoader myLoader = new ModelLoader();

  @NotNull
  private Logger myLogger = Loggers.SERVER; //Logger.getInstance("SoftInstall");



  public SoftInstallModelHolder(@NotNull ServerPaths serverPaths)
  {

    mySoftConfigDir = new File(serverPaths.getDataDirectory(), "config/softinstall");
    readModel();

  }


  public void readModel() {
    if (mySoftConfigDir.isDirectory()) {
      myLogger.info("SoftInstall: Loading descriptors...");

      myModel = myLoader.loadModel(mySoftConfigDir);

      int cnt = myModel.getDescriptorsCount();
      myLogger.info("SoftInstall: " + (cnt == 0 ? "No descriptors found" : "Loaded "+cnt+" descriptors"));
    }
    else {
      myLogger.info("SoftInstall: Directory "+mySoftConfigDir.getAbsolutePath()+" is empty or doesn't exist");
    }
  }


  @NotNull
  public SoftModel getModel()
  {
    return myModel;
  }




}
