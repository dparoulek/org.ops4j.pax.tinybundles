package org.ops4j.pax.tinybundles;

import java.io.InputStream;
import org.junit.Test;
import org.osgi.framework.Constants;
import org.ops4j.pax.tinybundles.demo.HelloWorld;
import org.ops4j.pax.tinybundles.demo.intern.HelloWorldImpl;
import org.ops4j.pax.tinybundles.demo.intern.MyFirstActivator;

import static org.ops4j.pax.tinybundles.core.TinyBundles.*;

/**
 * Simple examples that showcase the regular usage of Tinybundles.
 */
public class Examples {

    @Test
    public void testExampleSimple() {
        InputStream inp = bundle()
            .add( MyFirstActivator.class )
            .add( HelloWorld.class )
            .add( HelloWorldImpl.class )
            .set( Constants.BUNDLE_SYMBOLICNAME, "Hello World Bundle" )
            .set( Constants.EXPORT_PACKAGE, "demo" )
            .set( Constants.IMPORT_PACKAGE, "demo" )
            .set( Constants.BUNDLE_ACTIVATOR, MyFirstActivator.class.getName() )
            .build( );
    }

    @Test
    public void testExampleWithExplicitBuilder() {
        InputStream inp = bundle()
            .add( MyFirstActivator.class )
            .add( HelloWorld.class )
            .add( HelloWorldImpl.class )
            .set( Constants.BUNDLE_SYMBOLICNAME, "Hello World Bundle" )
            .set( Constants.EXPORT_PACKAGE, "demo" )
            .set( Constants.IMPORT_PACKAGE, "demo" )
            .set( Constants.BUNDLE_ACTIVATOR, MyFirstActivator.class.getName() )
            .build( withClassicBuilder() );
    }

     @Test
    public void testExampleWithBNDBuilder() {
        InputStream inp = bundle()
            .add( MyFirstActivator.class )
            .add( HelloWorld.class )
            .add( HelloWorldImpl.class )
            .set( Constants.BUNDLE_SYMBOLICNAME, "Hello World Bundle" )
            .set( Constants.EXPORT_PACKAGE, "" )
            .set( Constants.IMPORT_PACKAGE, "*" )
            .set( Constants.BUNDLE_ACTIVATOR, MyFirstActivator.class.getName() )
            .build( withBnd() );
    }
}
