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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        final int PORT = 17776;
        FeedbackServer server = new FeedbackServer(new InetSocketAddress(PORT), 2);
        server.start();
        System.out.printf("Server started on port %d...\n", PORT);

        System.out.println("Type 'help' for usage.");
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        while(true)
        {
//            System.out.print("> ");
            String[] input = systemIn.readLine().split(" ");

            switch(input[0])
            {
                case("list"):
                    for(Entry entry : server.getSurvey().getEntries())
                    {
                        System.out.printf("%s:%d\n", entry.getName(), entry.getVotes());
                    }
                    if(server.getSurvey().getEntries().isEmpty())
                    {
                        System.out.println("No entries.");
                    }
                    break;
//                case("stop"):
//                    System.out.println("Stopping...");
//                    server.stop();
//                    running = false;
//                    break;
//                case("start"):
//                    if(running)
//                    {
//                        System.err.println("Server already running.");
//                    }
//                    else
//                    {
//                        server.start();
//                        running = true;
//                        System.out.printf("Server started on port %d.\n", PORT);
//                    }
//                    break;
//                case("restart"):
//                    server.stop();
//                    server.start();
//                    System.out.println("Server restarted.");
//                    break;
                case("exit"):
                    System.out.println("Stopping...");
                    server.stop();
                    System.exit(0);
                    break;
                case("remove"):
                    try
                    {
                        int index = Integer.parseInt(input[1]);
                        try
                        {
                            server.updateRemove(index);
                        }
                        catch(IndexOutOfBoundsException e)
                        {
                            System.err.println("Index does not exist.");
                        }
                    }
                    catch(IndexOutOfBoundsException e)
                    {
                        System.err.println("Invalid number of arguments.");
                    }
                    catch(NumberFormatException e)
                    {
                        System.err.println("Invalid index.");
                    }
                    break;
                case("help"):
                    System.out.println("Usage:");
                    System.out.println("list - Lists all the current entries and values.");
                    System.out.println("remove [index] - Removes entry at index.");
                    System.out.println("help - Displays this screen.");
                    System.out.println("exit - Closes server.");
                    System.out.println();
                    break;
                default:
                    System.err.println("Invalid command.");
                    break;
            }
        }
    }
}
