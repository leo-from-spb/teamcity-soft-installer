package jetbrains.buildServer.softinstall.models.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;



/**
 * @author Leonid Bushuev from JetBrains
 */

public interface SoftVarsProvider
{
  List<SoftVariable> getVars();

  void addVar(@NotNull SoftVariable var);
}
