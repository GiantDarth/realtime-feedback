package name.cphilabaum.feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darth on 10/14/15.
 */
public class Survey
{
    private List<Entry> entries;

    public Survey()
    {
        entries = new ArrayList<>();
    }

    public void addEntry(Entry entry)
    {
        entries.add(entry);
    }

    public Entry removeEntry(int index)
    {
        return entries.remove(index);
    }

    public Entry getEntry(int index) throws IndexOutOfBoundsException
    {
        return entries.get(index);
    }

    public int getIndex(String name)
    {
        for(int i = 0; i < entries.size(); i++)
        {
            if(entries.get(i).getName().equalsIgnoreCase(name))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(String name)
    {
        for(Entry entry : entries)
        {
            if(entry.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }

        return false;
    }

    protected List<Entry> getEntries()
    {
        return this.entries;
    }
}
