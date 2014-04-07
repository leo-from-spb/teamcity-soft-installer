package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.OS;
import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
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
  public void load_Python34() {
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

}
