package jetbrains.buildServer.softinstall.model;

import org.jetbrains.annotations.NotNull;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftProvidingSection extends SoftSection
{

  public SoftProvidingSection()
  {
    super(Kind.PROVIDE);
  }


}
