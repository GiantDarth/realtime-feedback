package name.cphilabaum.feedback;

/**
 * Created by darth on 10/14/15.
 */
public class Entry
{
    private String name;
    private int votes;

    public Entry(String name)
    {
        this.name = name;
        this.votes = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getVotes()
    {
        return votes;
    }

    public void resetVotes()
    {
        this.votes = 0;
    }

    public void upVotes()
    {
        this.votes += 1;
    }

    public void downVotes()
    {
        this.votes -= 1;
    }
}
