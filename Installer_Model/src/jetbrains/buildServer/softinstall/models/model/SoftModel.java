package jetbrains.buildServer.softinstall.models.model;

import jetbrains.buildServer.softinstall.models.loader.WrongModelException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftModel
{

  private final SortedMap<String,SoftDescriptor> myDescriptors = new ConcurrentSkipListMap<String,SoftDescriptor>();

  private final AtomicInteger mySoftCounter = new AtomicInteger(0);


  public Map<String, SoftDescriptor> getDescriptors()
  {
    return Collections.unmodifiableMap(myDescriptors);
  }


  public void addDescriptor(@NotNull SoftDescriptor descriptor) {
    String name = descriptor.getName();
    if (myDescriptors.containsKey(name)) throw new WrongModelException("The Soft Descriptor \""+name+"\" already exists.");
    descriptor.setOrderNum(mySoftCounter.incrementAndGet());
    myDescriptors.put(name, descriptor);
  }


  @Nullable
  public SoftDescriptor getDescriptor(@NotNull final String name)
  {
    return myDescriptors.get(name);
  }


  @NotNull
  public SortedSet<SoftDescriptor> getDescriptors(@NotNull final Collection<String> names)
  {
    TreeSet<SoftDescriptor> descriptors = new TreeSet<SoftDescriptor>();
    for (String name : names) {
      SoftDescriptor descriptor = getDescriptor(name);
      descriptors.add(descriptor);
    }
    return descriptors;
  }


  public int getDescriptorsCount() {
    return myDescriptors.size();
  }

}
