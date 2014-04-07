package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftDescriptor
{

  @NotNull
  private String myName;

  private final List<SoftSection> mySections = new ArrayList<SoftSection>();


  public SoftDescriptor(@NotNull final String name)
  {
    myName = name;
  }


  @NotNull
  public String getName()
  {
    return myName;
  }


  public void setName(@NotNull String name)
  {
    myName = name;
  }


  public List<SoftSection> getSections()
  {
    return Collections.unmodifiableList(mySections);
  }

  public void addsection(SoftSection section) {
    mySections.add(section);
  }

}
