package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.OS;
import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import jetbrains.buildServer.softinstall.models.model.SoftSection;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class ModelLoaderTest
{

  @Test
  public void loadDescriptor_Python34()
  {
    File python34conf = new File("configs/Python34.soft.xml");
    assertTrue(python34conf.exists());

    ModelLoader loader = new ModelLoader();
    final SoftDescriptor descriptor = loader.loadDescriptor(python34conf);

    assertEquals(descriptor.getName(), "Python34");
    assertEquals(descriptor.getSections().size(), 1);

    final SoftSection windowsSection = descriptor.getSections().get(0);
    assertEquals(windowsSection.getOS(), OS.WINDOWS);
    assertEquals(windowsSection.getInstallSteps().size(), 4);
    assertEquals(windowsSection.getVars().size(), 3);
  }


  @Test(dependsOnMethods = "loadDescriptor_Python34")
  public void loadModel_examples()
  {
    File dir = new File("configs");

    ModelLoader loader = new ModelLoader();
    final SoftModel model = loader.loadModel(dir);

    assertEquals(model.getDescriptors().size(), 1);
  }

}
