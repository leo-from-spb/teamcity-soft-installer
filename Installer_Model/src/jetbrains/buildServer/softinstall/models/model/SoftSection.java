package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftSection implements SoftVarsProvider
{

  private OS myOS;

  private final List<SoftExistent> myExistents = new ArrayList<SoftExistent>();

  private final List<SoftInstallStep> myInstallSteps = new ArrayList<SoftInstallStep>();

  private final List<SoftVariable> myVars = new ArrayList<SoftVariable>();


  public OS getOS()
  {
    return myOS;
  }


  public void setOS(OS OS)
  {
    myOS = OS;
  }


  @NotNull
  public List<SoftExistent> getExistents()
  {
    return Collections.unmodifiableList(myExistents);
  }

  public void addExistent(@NotNull SoftExistent existent)
  {
    myExistents.add(existent);
  }


  @NotNull
  public List<SoftInstallStep> getInstallSteps()
  {
    return Collections.unmodifiableList(myInstallSteps);
  }


  public void addInstallStep(@NotNull SoftInstallStep step) {
    myInstallSteps.add(step);
  }


  public List<SoftVariable> getVars()
  {
    return Collections.unmodifiableList(myVars);
  }


  public void addVar(@NotNull SoftVariable var) {
    myVars.add(var);
  }

}
