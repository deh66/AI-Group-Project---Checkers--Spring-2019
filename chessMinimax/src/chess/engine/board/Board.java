package chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import chess.engine.alliance.Alliance;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;

public class Board 
{
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private Board (Builder builder)
	{
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
	}
	
    private Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) 
    {
    	final List<Piece> activePieces = new ArrayList<>();
    	
    	for (final Tile tile : gameBoard)
    	{
    		if (tile.isTileOccupied())
    		{
    			final Piece piece = tile.getPiece();
    			
    			if (piece.getPieceAlliance() == alliance)
    			{
    				activePieces.add(piece);
    			}
    		}
    	}
    	
		return ImmutableList.copyOf(activePieces);
	}

	public Tile getTile (final int tileCoordinate)
    {
    	return gameBoard.get(tileCoordinate);
    }
    
    private static List<Tile> createGameBoard(final Builder builder)
    {
    	final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
    	
    	for (int i = 0; i < BoardUtils.NUM_TILES; i++)
    	{
    		tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
    	}
    	
		return ImmutableList.copyOf(tiles);
    }
    
    public static Board createStandardBoard()
    {
    	final Builder builder = new Builder();
    	//blAck
    	builder.setPiece(new Rook(Alliance.BLACK, 0));
    	builder.setPiece(new Knight(Alliance.BLACK, 1));
    	builder.setPiece(new Bishop(Alliance.BLACK, 2));
    	builder.setPiece(new Queen(Alliance.BLACK, 3));
    	builder.setPiece(new King(Alliance.BLACK, 4));
    	builder.setPiece(new Bishop(Alliance.BLACK, 5));
    	builder.setPiece(new Knight(Alliance.BLACK, 6));
    	builder.setPiece(new Rook(Alliance.BLACK, 7));
    	builder.setPiece(new Pawn(Alliance.BLACK, 8));
    	builder.setPiece(new Pawn(Alliance.BLACK, 9));
    	builder.setPiece(new Pawn(Alliance.BLACK, 10));
    	builder.setPiece(new Pawn(Alliance.BLACK, 11));
    	builder.setPiece(new Pawn(Alliance.BLACK, 12));
    	builder.setPiece(new Pawn(Alliance.BLACK, 13));
    	builder.setPiece(new Pawn(Alliance.BLACK, 14));
    	builder.setPiece(new Pawn(Alliance.BLACK, 15));
    	//white
    	builder.setPiece(new Pawn(Alliance.WHITE, 48));
    	builder.setPiece(new Pawn(Alliance.WHITE, 49));
    	builder.setPiece(new Pawn(Alliance.WHITE, 50));
    	builder.setPiece(new Pawn(Alliance.WHITE, 51));
    	builder.setPiece(new Pawn(Alliance.WHITE, 52));
    	builder.setPiece(new Pawn(Alliance.WHITE, 53));
    	builder.setPiece(new Pawn(Alliance.WHITE, 54));
    	builder.setPiece(new Pawn(Alliance.WHITE, 55));
    	builder.setPiece(new Rook(Alliance.WHITE, 56));
    	builder.setPiece(new Knight(Alliance.WHITE, 57));
    	builder.setPiece(new Bishop(Alliance.WHITE, 58));
    	builder.setPiece(new King(Alliance.WHITE, 59));
    	builder.setPiece(new Queen(Alliance.WHITE, 60));
    	builder.setPiece(new Bishop(Alliance.WHITE, 61));
    	builder.setPiece(new Knight(Alliance.WHITE, 62));
    	builder.setPiece(new Rook(Alliance.WHITE, 63));
    	//whiet to move
    	builder.setMoveMaker(Alliance.WHITE);
    	
    	return builder.build();
    }
    
    public static class Builder
    {
    	Map<Integer, Piece> boardConfig;
    	Alliance nextMoveMaker;
    	
    	public Builder()
    	{
    		
    	}
    	
    	public Builder setPiece(final Piece piece)
    	{
    		this.boardConfig.put(piece.getPiecePosition(), piece);
    		return this;
    	}
    	
    	public Builder setMoveMaker(final Alliance nextMoveMaker)
    	{
    		this.nextMoveMaker = nextMoveMaker;
    		return this;
    	}
    	
    	public Board build()
    	{
    	    return new Board(this);
    	}
        
    }
}
