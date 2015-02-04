package nova.wrapper.mc1710.recipes;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import nova.core.game.Game;
import nova.core.recipes.RecipeAddedEvent;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.RecipeRemovedEvent;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.wrapper.mc1710.util.ReflectionUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by Stan on 1/02/2015.
 */
public class MinecraftRecipeRegistry {
    public static final MinecraftRecipeRegistry instance = new MinecraftRecipeRegistry();

    private final Map<CraftingRecipe, IRecipe> forwardWrappers = new HashMap<>();
    private final Map<IRecipe, CraftingRecipe> backwardWrappers = new HashMap<>();

    private MinecraftRecipeRegistry() {}

    public void registerRecipes() {
        long startTime = System.currentTimeMillis();

        RecipeManager recipeManager = Game.instance.get().recipeManager;

        List<IRecipe> recipes = (List<IRecipe>) CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            CraftingRecipe converted = convert(recipe);
            if (converted != null) {
                recipeManager.addRecipe(converted);
                backwardWrappers.put(recipe, converted);
                forwardWrappers.put(converted, recipe);
            }
        }

        ReflectionUtil.setCraftingRecipeList(new RecipeListWrapper(recipes));

        System.out.println("Initialized recipes in " + (System.currentTimeMillis() - startTime) + " ms");

        recipeManager.addRecipeAddedListener(CraftingRecipe.class, this::onNOVARecipeAdded);
        recipeManager.addRecipeRemovedListener(CraftingRecipe.class, this::onNOVARecipeRemoved);
    }

    private CraftingRecipe convert(IRecipe recipe) {
        return RecipeConverter.toNova(recipe);
    }

    private IRecipe convert(CraftingRecipe recipe) {
        return RecipeConverter.toMinecraft(recipe);
    }

    private void onNOVARecipeAdded(RecipeAddedEvent<CraftingRecipe> e) {
        CraftingRecipe recipe = e.getRecipe();
        if (forwardWrappers.containsKey(recipe))
            return;

        IRecipe minecraftRecipe = convert(recipe);

        backwardWrappers.put(minecraftRecipe, recipe);
        forwardWrappers.put(recipe, minecraftRecipe);

        CraftingManager.getInstance().getRecipeList().add(minecraftRecipe);
    }

    private void onNOVARecipeRemoved(RecipeRemovedEvent<CraftingRecipe> e) {
        IRecipe minecraftRecipe = forwardWrappers.get(e.getRecipe());

        forwardWrappers.remove(e.getRecipe());
        backwardWrappers.remove(minecraftRecipe);

        CraftingManager.getInstance().getRecipeList().remove(minecraftRecipe);
    }

    private void onMinecraftRecipeAdded(IRecipe recipe) {
        if (backwardWrappers.containsKey(recipe))
            return;

        CraftingRecipe novaRecipe = convert(recipe);

        backwardWrappers.put(recipe, novaRecipe);
        forwardWrappers.put(novaRecipe, recipe);

        Game.instance.get().recipeManager.addRecipe(novaRecipe);
    }

    private void onMinecraftRecipeRemoved(IRecipe recipe) {
        CraftingRecipe novaRecipe = backwardWrappers.get(recipe);

        forwardWrappers.remove(novaRecipe);
        backwardWrappers.remove(recipe);

        Game.instance.get().recipeManager.removeRecipe(novaRecipe);
    }

    private class RecipeListWrapper extends AbstractList<IRecipe> {
        private final List<IRecipe> original;

        public RecipeListWrapper(List<IRecipe> original) {
            this.original = original;
        }

        @Override
        public int size() {
            return original.size();
        }

        @Override
        public boolean isEmpty() {
            return original.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return original.contains(o);
        }

        @Override
        public Iterator<IRecipe> iterator() {
            return original.iterator();
        }

        @Override
        public void forEach(Consumer<? super IRecipe> action) {
            original.forEach(action);
        }

        @Override
        public Object[] toArray() {
            return original.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return original.toArray(a);
        }

        @Override
        public boolean add(IRecipe iRecipe) {
            boolean result = original.add(iRecipe);
            if (result && !backwardWrappers.containsKey(iRecipe))
                onMinecraftRecipeAdded(iRecipe);

            return result;
        }

        @Override
        public boolean remove(Object o) {
            if (!backwardWrappers.containsKey(o))
                return false;

            boolean result = original.remove(o);
            if (result)
                onMinecraftRecipeRemoved((IRecipe) o);

            return result;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return original.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends IRecipe> c) {
            boolean result = false;
            for (IRecipe recipe : c) {
                result |= add(recipe);
            }
            return result;
        }

        @Override
        public boolean addAll(int index, Collection<? extends IRecipe> c) {
            for (IRecipe recipe : c) {
                add(index, recipe);
                index++;
            }

            return !c.isEmpty();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean result = false;
            for (Object o : c) {
                result |= c.remove(o);
            }
            return result;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void sort(Comparator<? super IRecipe> c) {
            original.sort(c);
        }

        @Override
        public void clear() {
            // one would have to be a mad scientist to use this operation... but ok

            for (IRecipe recipe : original) {
                onMinecraftRecipeRemoved(recipe);
            }

            original.clear();
        }

        @Override
        public IRecipe get(int index) {
            return original.get(index);
        }

        @Override
        public IRecipe set(int index, IRecipe element) {
            IRecipe current = original.get(index);
            onMinecraftRecipeRemoved(current);

            original.set(index, element);
            onMinecraftRecipeAdded(element);

            return current;
        }

        @Override
        public void add(int index, IRecipe element) {
            original.add(index, element);
            onMinecraftRecipeAdded(element);
        }

        @Override
        public IRecipe remove(int index) {
            IRecipe current = original.remove(index);
            onMinecraftRecipeRemoved(current);
            return current;
        }

        @Override
        public int indexOf(Object o) {
            return original.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return original.lastIndexOf(o);
        }

        @Override
        public ListIterator<IRecipe> listIterator() {
            return original.listIterator();
        }

        @Override
        public ListIterator<IRecipe> listIterator(int index) {
            return original.listIterator(index);
        }

        @Override
        public List<IRecipe> subList(int fromIndex, int toIndex) {
            return new RecipeListWrapper(original.subList(fromIndex, toIndex));
        }

        @Override
        public Spliterator<IRecipe> spliterator() {
            return original.spliterator();
        }

        @Override
        public Stream<IRecipe> stream() {
            return original.stream();
        }

        @Override
        public Stream<IRecipe> parallelStream() {
            return original.parallelStream();
        }
    }
}
