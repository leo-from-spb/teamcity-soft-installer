package jetbrains.buildServer.softinstall.server;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.controllers.AuthorizationInterceptor;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.serverSide.auth.AccessDeniedException;
import jetbrains.buildServer.serverSide.auth.AuthorityHolder;
import jetbrains.buildServer.softinstall.models.loader.ModelLoader;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import jetbrains.buildServer.web.PermissionChecker;
import jetbrains.buildServer.web.WebAccessHelper;
import jetbrains.buildServer.web.WebAccessService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  private AuthorizationInterceptor myAuthorizationInterceptor;

  @NotNull
  private Logger myLogger = Loggers.SERVER; //Logger.getInstance("SoftInstall");



  public SoftInstallModelHolder(@NotNull final ServerPaths serverPaths,
                                @NotNull final WebAccessService webAccessService,
                                @NotNull AuthorizationInterceptor authorizationInterceptor)
  {
    myAuthorizationInterceptor = authorizationInterceptor;
    // READ MODEL

    mySoftConfigDir = new File(serverPaths.getDataDirectory(), "config/softinstall");
    readModel();

    // PROVIDE ACCESS

    PermissionChecker pc = new PermissionChecker()
    {
      @Override
      public boolean checkPermissions(@NotNull AuthorityHolder authorityHolder)
              throws AccessDeniedException
      {
        return true;
      }
    };

    WebAccessHelper wah = new WebAccessHelper()
    {
      @Nullable
      @Override
      public String getDesiredId()
      {
        return "softinstall";
      }

      @Override
      public boolean allowDownloadZip()
      {
        return false;
      }

      @Nullable
      @Override
      public String getFileNameForZip()
      {
        return "zip";
      }
    };

    webAccessService.allowAccess(mySoftConfigDir, pc, wah);

    myAuthorizationInterceptor.addPathNotRequiringAuth("/get/file/softinstall/**");
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
