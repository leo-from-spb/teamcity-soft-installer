package jetbrains.buildServer.softinstall.models.loader;

import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
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
  }

}
