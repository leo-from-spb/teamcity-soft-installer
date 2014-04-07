package jetbrains.buildServer.softinstall.models.model;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftSection
{

  private final List<SoftExec> myExecs = new ArrayList<SoftExec>();

  private final List<SoftVariable> myProvideVars = new ArrayList<SoftVariable>();

  private OS myOS;

}
