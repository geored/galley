/*******************************************************************************
 * Copyright (C) 2014 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.maven.galley.maven.spi.defaults;

import static org.commonjava.maven.galley.maven.model.view.XPathManager.A;
import static org.commonjava.maven.galley.maven.model.view.XPathManager.G;
import static org.commonjava.maven.galley.maven.model.view.XPathManager.V;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.commonjava.maven.atlas.ident.ref.ProjectRef;
import org.commonjava.maven.galley.maven.GalleyMavenException;
import org.commonjava.maven.galley.maven.model.view.PluginDependencyView;
import org.commonjava.maven.galley.maven.model.view.PluginView;
import org.commonjava.maven.galley.maven.parse.XMLInfrastructure;
import org.w3c.dom.Element;

public abstract class AbstractMavenPluginImplications
    implements MavenPluginImplications
{

    protected final XMLInfrastructure xml;

    protected AbstractMavenPluginImplications( final XMLInfrastructure xml )
    {
        this.xml = xml;
    }

    protected PluginDependencyView createPluginDependency( final PluginView pv, final ProjectRef ref )
        throws GalleyMavenException
    {
        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put( G, ref.getGroupId() );
        map.put( A, ref.getArtifactId() );
        map.put( V, pv.getVersion() );

        final Element element = pv.getElement();
        return new PluginDependencyView( pv.getPomView(), pv, xml.createElement( element, "dependencies/dependency", map ) );
    }

    @Override
    public Set<PluginDependencyView> getImpliedPluginDependencies( final PluginView pv )
        throws GalleyMavenException
    {
        final Map<ProjectRef, Set<ProjectRef>> impliedDepMap = getImpliedRefMap();
        final ProjectRef ref = pv.asProjectRef();
        final Set<ProjectRef> implied = impliedDepMap.get( ref );

        if ( implied == null || implied.isEmpty() )
        {
            return null;
        }

        final Set<PluginDependencyView> views = new HashSet<PluginDependencyView>( implied.size() );
        for ( final ProjectRef impliedRef : implied )
        {
            final PluginDependencyView pd = createPluginDependency( pv, impliedRef );
            views.add( pd );
        }

        return views;
    }

    protected abstract Map<ProjectRef, Set<ProjectRef>> getImpliedRefMap();

}