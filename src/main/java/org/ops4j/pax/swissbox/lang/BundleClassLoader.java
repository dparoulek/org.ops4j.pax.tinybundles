/*
 * Copyright 2007 Alin Dreghiciu.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.swissbox.lang;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import org.osgi.framework.Bundle;
import org.ops4j.lang.PreConditionException;

/**
 * Class loader that uses the a bundle in order to implement class loader functionality.
 *
 * @author Alin Dreghiciu
 * @since 0.1.0, December 29, 2007
 */
public class BundleClassLoader
    extends ClassLoader
{

    /**
     * Bundle used for class loading.
     */
    private final Bundle m_bundle;

    /**
     * Privileged factory method.
     *
     * @param bundle bundle to be used for class loading. Cannot be null.
     * @param parent parent clas loader
     *
     * @return created bundle class loader
     *
     * @see org.ops4j.pax.swissbox.lang.BundleClassLoader#BundleClassLoader(Bundle,ClassLoader)
     */
    public static BundleClassLoader newPriviledged( final Bundle bundle, final ClassLoader parent )
    {
        return AccessController.doPrivileged(
            new PrivilegedAction<BundleClassLoader>()
            {
                public BundleClassLoader run()
                {
                    return new BundleClassLoader( bundle, parent );
                }
            }
        );
    }

    /**
     * Creates a bundle class loader.
     *
     * @param bundle bundle to be used for class loading. Cannot be null.
     * @param parent parent class loader
     */
    public BundleClassLoader( final Bundle bundle, final ClassLoader parent )
    {
        super( parent );
        PreConditionException.validateNotNull( bundle, "Bundle" );
        m_bundle = bundle;
    }

    /**
     * Getter.
     *
     * @return the bundle the class loader loads from
     */
    public Bundle getBundle()
    {
        return m_bundle;
    }

    /**
     * If there is a parent class loader use the super implementation that will first use the parent and as a fallback
     * it will call findResource(). In case there is no parent directy use findResource() as if we call the super
     * implementation it will use the VMClassLoader, fact that should be avoided.
     *
     * @see ClassLoader#getResource(String)
     */
    @Override
    public URL getResource( final String name )
    {
        if( getParent() != null )
        {
            return super.getResource( name );
        }
        return findResource( name );
    }

    /**
     * If there is a parent class loader use the super implementation that will first use the parent and as a fallback
     * it will call findResources(). In case there is no parent directy use findResources() as if we call the super
     * implementation it will use the VMClassLoader, fact that should be avoided.
     *
     * @see ClassLoader#getResources(String)
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public Enumeration<URL> getResources( final String name )
        throws IOException
    {
        if( getParent() != null )
        {
            super.getResource( name );
        }
        return findResources( name );
    }

    /**
     * Use bundle to find find the class.
     *
     * @see ClassLoader#findClass(String)
     */
    @Override
    protected Class findClass( final String name )
        throws ClassNotFoundException
    {
        return m_bundle.loadClass( name );
    }

    /**
     * If there is a parent class loader use the super implementation that will first use the parent and as a fallback
     * it will call findClass(). In case there is no parent directy use findClass() as if we call the super
     * implementation it will use the VMClassLoader, fact that should be avoided.
     *
     * @see ClassLoader#getResource(String)
     */
    @Override
    protected Class loadClass( final String name, final boolean resolve )
        throws ClassNotFoundException
    {
        if( getParent() != null )
        {
            return super.loadClass( name, resolve );
        }
        final Class classToLoad = findClass( name );
        if( resolve )
        {
            resolveClass( classToLoad );
        }
        return classToLoad;
    }

    /**
     * Use bundle to find resource.
     *
     * @see ClassLoader#findResource(String)
     */
    @Override
    protected URL findResource( final String name )
    {
        return m_bundle.getResource( name );
    }

    /**
     * Use bundle to find resources.
     *
     * @see ClassLoader#findResources(String)
     */
    @Override
    @SuppressWarnings( "unchecked" )
    protected Enumeration<URL> findResources( final String name )
        throws IOException
    {
        return m_bundle.getResources( name );
    }

    @Override
    public String toString()
    {
        return new StringBuffer()
            .append( this.getClass().getSimpleName() )
            .append( "{" )
            .append( "bundle=" ).append( m_bundle )
            .append( ",parent=" ).append( getParent() )
            .append( "}" )
            .toString();
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        BundleClassLoader that = (BundleClassLoader) o;

        if( m_bundle != null ? !m_bundle.equals( that.m_bundle ) : that.m_bundle != null )
        {
            return false;
        }

        if( getParent() != null ? !getParent().equals( that.getParent() ) : that.getParent() != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return ( m_bundle != null ? m_bundle.hashCode() : 0 ) * 37
               + ( getParent() != null ? getParent().hashCode() : 0 );
    }
}

