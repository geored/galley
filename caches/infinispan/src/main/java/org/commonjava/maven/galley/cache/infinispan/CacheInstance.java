/**
 * Copyright (C) 2013 Red Hat, Inc. (nos-devel@redhat.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.maven.galley.cache.infinispan;

import org.infinispan.Cache;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.function.Function;

/**
 * Created by jdcasey on 10/6/16.
 */
@SuppressWarnings( { "UnusedReturnValue", "unchecked" } )
public interface CacheInstance<K, V>
{
    String getName();

    <R> R execute( Function<Cache<K, V>, R> operation );

    void stop();

    boolean containsKey( K key );

    V put( K key, V value );

    V putIfAbsent( K key, V value );

    V remove( K key );

    V get( K key );

    void beginTransaction()
            throws NotSupportedException, SystemException;

    void rollback()
                    throws SystemException;

    void commit()
                            throws SystemException, HeuristicMixedException, HeuristicRollbackException,
                                   RollbackException;

    int getTransactionStatus()
                                    throws SystemException;

    Object getLockOwner( K key );

    boolean isLocked( K key );

    void lock( K... keys );

    void unlock( K key );
}
