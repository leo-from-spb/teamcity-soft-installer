package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class SoftVariable
{
  @NotNull
  private String myName;

  @Nullable
  private String myExpression;

  private boolean myOverride;


  public SoftVariable(@NotNull String name)
  {
    myName = name;
  }


  public SoftVariable(@NotNull String name, String expression, boolean override)
  {
    myName = name;
    myExpression = expression;
    myOverride = override;
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


  @Nullable
  public String getExpression()
  {
    return myExpression;
  }


  public void setExpression(@Nullable String expression)
  {
    myExpression = expression;
  }


  public boolean isOverride()
  {
    return myOverride;
  }


  public void setOverride(boolean override)
  {
    myOverride = override;
  }
}
