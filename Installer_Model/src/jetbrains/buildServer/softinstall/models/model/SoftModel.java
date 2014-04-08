package jetbrains.buildServer.softinstall.models.model;

import jetbrains.buildServer.softinstall.models.loader.WrongModelException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftModel
{

  private final Map<String,SoftDescriptor> myDescriptors = new TreeMap<String,SoftDescriptor>();


  public Map<String, SoftDescriptor> getDescriptors()
  {
    return Collections.unmodifiableMap(myDescriptors);
  }


  public void addDescriptor(@NotNull SoftDescriptor descriptor) {
    String name = descriptor.getName();
    if (myDescriptors.containsKey(name)) throw new WrongModelException("The Soft Descriptor \""+name+"\" already exists.");
    myDescriptors.put(name, descriptor);
  }


  public int getDescriptorsCount() {
    return myDescriptors.size();
  }

}
