/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.event;

/**
 *
 * @author Stan
 * @param <T>
 */
public interface EventListener<T>
{
	public void onEvent(T value);
}
