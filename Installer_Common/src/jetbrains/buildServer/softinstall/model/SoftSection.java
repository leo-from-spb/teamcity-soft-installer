package jetbrains.buildServer.softinstall.model;

import org.jetbrains.annotations.NotNull;



/**
 * @author Leonid Bushuev from JetBrains
 */
public abstract class SoftSection
{

  public enum Kind {
    INSTALL,
    PROVIDE
  }



  @NotNull
  public final Kind myKind;


  protected SoftSection(@NotNull Kind kind)
  {
    myKind = kind;
  }

}
