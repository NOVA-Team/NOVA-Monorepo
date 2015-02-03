package nova.core.deps;

/**
 * The interface NOVA mods that want to provide repos to download from should use.
 *
 * Not required. If not used, it will continue on and not generate dependencies.
 *
 * @author Mitchellbrine
 */
public interface DependencyRepoProvider {

	/**
	 * The method for determining the repo(s) for a certain dependency
	 *
	 * @param mod - The modid of the dependency. You can check this in any way you want.
	 * @return - the list of repos for the dependency
	 */
	String[] getModRepo(String mod);

}
