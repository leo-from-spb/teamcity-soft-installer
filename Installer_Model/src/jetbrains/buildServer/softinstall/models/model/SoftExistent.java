package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftExistent implements SoftVarsProvider
{

  private final List<SoftCheckFile> myChecks = new ArrayList<SoftCheckFile>();

  private final List<SoftVariable> myVars = new ArrayList<SoftVariable>();



  @NotNull
  public List<SoftCheckFile> getChecks()
  {
    return Collections.unmodifiableList(myChecks);
  }

  public void addCheck(SoftCheckFile check)
  {
    myChecks.add(check);
  }


  public List<SoftVariable> getVars()
  {
    return Collections.unmodifiableList(myVars);
  }


  public void addVar(@NotNull SoftVariable var) {
    myVars.add(var);
  }

}
