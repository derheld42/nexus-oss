package org.sonatype.appcontext.source;

import java.util.HashMap;
import java.util.Map;

import org.sonatype.appcontext.AppContextException;
import org.sonatype.appcontext.AppContextRequest;
import org.sonatype.appcontext.internal.Preconditions;

public class FilteredEntrySource
    implements EntrySource
{
    private final EntrySource source;

    private final EntryFilter filter;

    private final EntrySourceMarker sourceMarker;

    public FilteredEntrySource( final EntrySource source, final EntryFilter filter )
    {
        this.source = Preconditions.checkNotNull( source );

        this.filter = Preconditions.checkNotNull( filter );

        this.sourceMarker = filter.getEntrySourceMarker( source.getEntrySourceMarker() );
    }

    public EntrySourceMarker getEntrySourceMarker()
    {
        return sourceMarker;
    }

    public Map<String, Object> getEntries( AppContextRequest request )
        throws AppContextException
    {
        final Map<String, Object> result = new HashMap<String, Object>();

        for ( Map.Entry<String, Object> entry : source.getEntries( request ).entrySet() )
        {
            if ( filter.accept( entry.getKey(), entry.getValue() ) )
            {
                result.put( entry.getKey(), entry.getValue() );
            }
        }

        return result;
    }

}
