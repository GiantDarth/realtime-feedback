/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Christopher Robert Philabaum
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package name.cphilabaum.feedback;

import java.util.ArrayList;
import java.util.List;

public class Survey
{
    private List<Entry> entries;

    public Survey()
    {
        entries = new ArrayList<>();
    }

    public void addEntry(Entry entry) throws InvalidEntryNameException
    {
        if(entry.getName().contains("\""))
        {
            throw new InvalidEntryNameException("Entry name cannot contain \"'s.");
        }
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
