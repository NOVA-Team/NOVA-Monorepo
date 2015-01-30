/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.event;

import java.io.Closeable;

/**
 *
 * @author Stan
 */
public interface EventListenerHandle extends Closeable
{
	@Override
	public void close();
}
