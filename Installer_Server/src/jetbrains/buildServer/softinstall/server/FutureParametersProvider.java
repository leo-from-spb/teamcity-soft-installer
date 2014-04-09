package jetbrains.buildServer.softinstall.server;


import com.intellij.util.containers.HashSet;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildRunnerDescriptor;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.parameters.BuildParametersProvider;
import jetbrains.buildServer.softinstall.common.Consts;
import jetbrains.buildServer.softinstall.models.model.SoftDescriptor;
import jetbrains.buildServer.softinstall.models.model.SoftModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static jetbrains.buildServer.softinstall.common.Utils.splitSoftNames;



/**
 * @author Leonid Bushuev from JetBrains
 */
public class FutureParametersProvider implements BuildParametersProvider
{
  @NotNull
  private final SoftInstallModelHolder myModelHolder;


  public FutureParametersProvider(@NotNull SoftInstallModelHolder modelHolder)
  {
    myModelHolder = modelHolder;
  }


  @NotNull
  Set<String> getSoftToInstallList(final SBuildType bt)
  {
    if (bt == null) return Collections.emptySet();
    final List<SBuildRunnerDescriptor> brs = bt.getBuildRunners();
    Set<String> softToInstall = new LinkedHashSet<String>();
    for (SBuildRunnerDescriptor br : brs) {
      if (br.getRunType() instanceof SoftInstallRunType) {
        final String softToInstallText = br.getParameters().get("softToInstall");
        Set<String> names = splitSoftNames(softToInstallText);
        softToInstall.addAll(names);
      }
    }
    return softToInstall;
  }


  @NotNull
  SortedSet<SoftDescriptor> getSoftToInstallDescriptors(final SBuildType bt)
  {
    final Set<String> names = getSoftToInstallList(bt);
    if (names.isEmpty()) return NO_DESCRIPTORS;

    SoftModel model = myModelHolder.getModel();

    TreeSet<SoftDescriptor> set = new TreeSet<SoftDescriptor>();
    for (String name : names) {
      SoftDescriptor descriptor = model.getDescriptor(name);
      if (descriptor != null) set.add(descriptor);
    }

    return set.isEmpty() ? NO_DESCRIPTORS : set;
  }


  Set<String> getProvidedVarNames(final SBuildType bt)
  {
    final SortedSet<SoftDescriptor> descriptors = getSoftToInstallDescriptors(bt);
    if (descriptors.isEmpty()) return NO_NAMES;

    Set<String> names = new HashSet<String>(descriptors.size() * 4);
    for (SoftDescriptor descriptor : descriptors) {
      names.addAll(descriptor.getProvidedVariableNames());
    }

    return names;
  }


  @NotNull
  @Override
  public Map<String,String> getParameters(@NotNull SBuild build, boolean emulationMode)
  {
    final Set<String> names = getProvidedVarNames(build.getBuildType());
    if (names.isEmpty()) return Collections.emptyMap();

    Map<String,String> p = new HashMap<String, String>(names.size());
    for (String name : names) p.put(name, Consts.FUTURE_VALUE);
    return p;
  }


  @NotNull
  @Override
  public Collection<String> getParametersAvailableOnAgent(@NotNull SBuild build)
  {
    return getProvidedVarNames(build.getBuildType());
  }


  private static final SortedSet<String> NO_NAMES =
            Collections.unmodifiableSortedSet(new TreeSet<String>());

  private static final SortedSet<SoftDescriptor> NO_DESCRIPTORS =
          Collections.unmodifiableSortedSet(new TreeSet<SoftDescriptor>());

}
