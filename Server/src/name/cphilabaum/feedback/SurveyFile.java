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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;

public class SurveyFile extends File
{
    public SurveyFile()
    {
       super("survey.csv");
    }

    /**
     * Creates a new <code>File</code> instance by converting the given
     * pathname string into an abstract pathname.  If the given string is
     * the empty string, then the result is the empty abstract pathname.
     *
     * @param pathname A pathname string
     * @throws NullPointerException If the <code>pathname</code> argument is <code>null</code>
     */
    public SurveyFile(String pathname)
    {
        super(pathname);
    }

    public void write(Survey survey) throws IOException
    {
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(this), "UTF-8"));
        String[] line;
        for(Entry entry : survey.getEntries())
        {
            line = new String[] { entry.getName(), String.valueOf(entry.getVotes()) };
            writer.writeNext(line);
        }
        writer.close();
    }

    public Survey read() throws IOException, MalformedFileException, NumberFormatException
    {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(this)));
        Survey survey = new Survey();

        String[] nextLine;
        while((nextLine = reader.readNext()) != null)
        {
            try
            {
                survey.addEntry(parseEntry(nextLine));
            }
            catch(InvalidEntryNameException e)
            {
                throw new MalformedFileException(e.getMessage());
            }
        }

        reader.close();

        return survey;
    }

    private Entry parseEntry(String[] line) throws MalformedFileException, NumberFormatException
    {
        if(line.length != 2)
        {
            throw new MalformedFileException("Line does not contain 2 inputs.");
        }

        int votes;
        try
        {
            votes = Integer.parseInt(line[1]);
            if(votes < 0)
            {
                throw new NumberFormatException("Integer is negative.");
            }
        }
        catch(NumberFormatException e)
        {
            throw new MalformedFileException("Votes is not a valid non-negative integer.");
        }

        return new Entry(line[0], votes);
    }
}
