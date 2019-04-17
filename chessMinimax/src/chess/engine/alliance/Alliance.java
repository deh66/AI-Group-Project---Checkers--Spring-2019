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

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public boolean isWhite() {
			return true;
		}
    },
	BLACK
	{
	    public int getDirection()
	    {
    	    return 1;
	    }

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public boolean isWhite() {
			return false;
		}
	};
    
    public abstract int getDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
}
