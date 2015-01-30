/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.recipes;

import java.util.List;
import java.util.Optional;
import nova.core.item.Item;
import nova.core.item.ItemStack;

/**
 *
 * @author Stan
 */
public interface IItemIngredient
{
	public Optional<List<Item>> getPossibleItems();
	
	public Optional<List<Item>> getExampleItems();
	
	public boolean isSubsetOf(IItemIngredient ingredient);
	
	public boolean matches(ItemStack item);
}
