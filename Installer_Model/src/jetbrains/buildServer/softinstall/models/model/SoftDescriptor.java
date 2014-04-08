package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftDescriptor implements Comparable<SoftDescriptor>
{

  private int myOrderNum;

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

  public void addSection(SoftSection section) {
    mySections.add(section);
  }


  public int getOrderNum()
  {
    return myOrderNum;
  }


  void setOrderNum(int orderNum)
  {
    myOrderNum = orderNum;
  }


  public Set<String> getProvidedVariableNames()
  {
    TreeSet<String> names = new TreeSet<String>();
    for (SoftSection section : mySections) {
      for (SoftVariable var : section.getVars()) {
        names.add(var.getName());
      }
    }
    return names;
  }


  @Override
  public int compareTo(@NotNull SoftDescriptor that)
  {
    if (this == that) return 0;
    int z = this.myOrderNum - that.myOrderNum;
    if (z == 0) z = this.myName.compareToIgnoreCase(that.myName);
    if (z == 0) z = this.myName.compareTo(that.myName);
    return z;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SoftDescriptor that = (SoftDescriptor) o;

    if (myOrderNum != that.myOrderNum) return false;
    if (!myName.equals(that.myName)) return false;

    return true;
  }


  @Override
  public int hashCode()
  {
    return myOrderNum;
  }


  @Override
  public String toString()
  {
    return myName + '(' + myOrderNum + ')';
  }
}
