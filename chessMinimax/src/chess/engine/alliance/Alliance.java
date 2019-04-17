package chess.engine.alliance;

public enum Alliance 
{
    WHITE
    {
    	@Override
    	public int getDirection() 
    	{
    	    return -1;	
    	}
    },
	BLACK
	{
	    public int getDirection()
	    {
    	    return 1;
	    }
	};
    
    public abstract int getDirection();
}
